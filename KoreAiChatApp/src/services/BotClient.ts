import {
  BotConfigModel,
  BotInfoModel,
  BotRequest,
  BotResponse,
  BotConnectionListener,
  ConnectionState,
  BotRequestState,
  JwtTokenResponse,
  BotAuthorizationResponse,
  RtmUrlResponse,
  WebHookResponse
} from '../types/BotConfigModel';
import { NetworkUtils } from '../utils/NetworkUtils';
import { LogUtils } from '../utils/LogUtils';
import { JwtRepository } from './JwtRepository';
import { WebHookRepository } from './WebHookRepository';

export class BotClient {
  private static instance: BotClient | null = null;
  private static accessToken: string = '';
  private static jwtToken: string = '';
  private static userJwtToken: string = '';
  private static botUserId: string = '';
  private static connectionState: ConnectionState = ConnectionState.DISCONNECTED;
  private static callEventMessage: Record<string, any> | null = null;

  private listener: BotConnectionListener | null = null;
  private botCustomData: Record<string, any> = {};
  private botInfoModel: BotInfoModel | null = null;
  private botInfoMap: Record<string, any> = {};
  private websocket: WebSocket | null = null;
  private connecting: boolean = false;
  private isReconnectAttemptRequired: boolean = false;
  private reconnectAttemptCount: number = 1;
  private reconnectTimer: NodeJS.Timeout | null = null;
  private botConfigModel: BotConfigModel | null = null;

  // Constants
  private static readonly RECONNECT_ATTEMPT_LIMIT = 16;
  private static readonly POLL_DELAY_REPEAT = 5000;
  private static readonly DATA_IDENTITY = 'identity';
  private static readonly DATA_USERNAME = 'userName';
  private static readonly DATA_USER_AGENT = 'userAgent';
  private static readonly KEY_ASSERTION = 'assertion';
  private static readonly KEY_BOT_INFO = 'botInfo';
  private static readonly IS_RECONNECT_PARAM = '&isReconnect=true';
  private static readonly CONNECTION_MODE_PARAM = '&ConnectionMode=Reconnect';
  private static readonly MSG_ON_CONNECT = 'ON_CONNECT';

  private jwtRepository: JwtRepository;
  private webHookRepository: WebHookRepository;

  private constructor() {
    this.jwtRepository = new JwtRepository();
    this.webHookRepository = new WebHookRepository();
  }

  public static getInstance(): BotClient {
    if (!BotClient.instance) {
      BotClient.instance = new BotClient();
    }
    return BotClient.instance;
  }

  public static getJwtToken(): string {
    return BotClient.userJwtToken || BotClient.jwtToken;
  }

  public static getAccessToken(): string {
    return BotClient.accessToken;
  }

  public static getUserId(): string {
    return BotClient.botUserId;
  }

  public static isConnected(): boolean {
    return BotClient.instance?.websocket?.readyState === WebSocket.OPEN;
  }

  public static setCallEventMessage(callEventMessage: Record<string, any>): void {
    BotClient.callEventMessage = callEventMessage;
  }

  public static getCallEventMessage(): Record<string, any> | null {
    return BotClient.callEventMessage;
  }

  public setListener(listener: BotConnectionListener): void {
    this.listener = listener;
  }

  public getConnectionState(): ConnectionState {
    return BotClient.connectionState;
  }

  public isConnecting(): boolean {
    return this.connecting;
  }

  public disconnectBot(): void {
    this.isReconnectAttemptRequired = false;
    try {
      if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
        this.websocket.close();
      }
    } catch (error) {
      LogUtils.e('BotClient', 'Exception while bot disconnection...', error);
    }
    
    BotClient.jwtToken = '';
    BotClient.accessToken = '';
    BotClient.botUserId = '';
    
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
      this.reconnectTimer = null;
    }
  }

  public async connectToBot(botConfig: BotConfigModel, isFirstTime: boolean, jwtToken?: string): Promise<void> {
    if (!this.init(botConfig, isFirstTime)) return;
    
    if (jwtToken) {
      BotClient.userJwtToken = jwtToken;
    }
    
    await this.initiateConnection(!isFirstTime);
  }

  private init(botConfig: BotConfigModel, isFirstTime: boolean): boolean {
    if (BotClient.connectionState === ConnectionState.CONNECTED || this.connecting) {
      return false;
    }

    if (!NetworkUtils.isNetworkAvailable()) {
      BotClient.connectionState = ConnectionState.DISCONNECTED;
      this.listener?.onConnectionStateChanged(BotClient.connectionState, false);
      return false;
    }

    this.botConfigModel = botConfig;
    this.isReconnectAttemptRequired = true;
    this.botCustomData = {};
    
    // Set custom data
    this.botCustomData[BotClient.DATA_USERNAME] = '';
    this.botCustomData[BotClient.DATA_IDENTITY] = '';
    this.botCustomData[BotClient.DATA_USER_AGENT] = typeof navigator !== 'undefined' ? navigator.userAgent : 'ReactNative';

    this.botInfoModel = {
      botName: botConfig.botName,
      botId: botConfig.botId,
      customData: this.botCustomData
    };

    this.botInfoMap = JSON.parse(JSON.stringify(this.botInfoModel));
    BotClient.connectionState = ConnectionState.CONNECTING;
    this.listener?.onConnectionStateChanged(BotClient.connectionState, !isFirstTime);
    
    return true;
  }

  private async initiateConnection(isReconnectionAttempt: boolean): Promise<void> {
    if (this.connecting) return;
    
    this.connecting = true;
    
    if (!this.botConfigModel) {
      this.connecting = false;
      return;
    }

    if (!BotClient.userJwtToken) {
      BotClient.userJwtToken = this.botConfigModel.jwtToken;
    }

    if (BotClient.userJwtToken) {
      await this.createAccessTokenAndRtmUrl(isReconnectionAttempt);
      return;
    }

    try {
      const jwtResponse = await this.jwtRepository.getJwtToken(
        this.botConfigModel.jwtServerUrl,
        this.botConfigModel.clientId,
        this.botConfigModel.clientSecret,
        this.botConfigModel.identity
      );

      if (!jwtResponse?.jwt) {
        this.onError(isReconnectionAttempt, 'JWT token is not created!');
        return;
      }

      BotClient.jwtToken = jwtResponse.jwt;
      await this.createAccessTokenAndRtmUrl(isReconnectionAttempt);
    } catch (error) {
      LogUtils.e('BotClient', 'JWT Token creation failed', error);
      this.onError(isReconnectionAttempt, 'JWT Token creation failed');
    }
  }

  private async createAccessTokenAndRtmUrl(isReconnectionAttempt: boolean): Promise<void> {
    if (!BotClient.getJwtToken()) {
      this.connecting = false;
      LogUtils.e('BotClient', 'jwtToken is empty!. Please create jwtToken to continue!');
      return;
    }

    BotClient.accessToken = '';
    const request: Record<string, any> = {
      [BotClient.KEY_ASSERTION]: BotClient.getJwtToken(),
      [BotClient.KEY_BOT_INFO]: this.botInfoModel
    };

    try {
      const authResponse = await this.jwtRepository.getJwtGrant(request);
      
      if (!authResponse) {
        this.onError(isReconnectionAttempt, 'Authorization failed');
        return;
      }

      BotClient.botUserId = authResponse.userInfo?.userId || '';
      BotClient.accessToken = authResponse.authorization?.accessToken || '';

      if (this.botConfigModel?.isWebHook) {
        await this.getWebHookMeta(BotClient.getJwtToken(), isReconnectionAttempt);
        return;
      }

      const requestCopy = { ...request };
      delete requestCopy[BotClient.KEY_ASSERTION];
      const rtmResponse = await this.jwtRepository.getRtmUrl(BotClient.accessToken, requestCopy);
      
      if (rtmResponse?.url) {
        await this.connectToSocket(rtmResponse.url, isReconnectionAttempt);
      } else {
        this.onError(isReconnectionAttempt, 'Failed to get RTM URL');
      }
    } catch (error) {
      LogUtils.e('BotClient', 'Error in createAccessTokenAndRtmUrl', error);
      this.onError(isReconnectionAttempt, 'Authentication failed');
    }
  }

  private async connectToSocket(url: string, isReconnectionAttempt: boolean): Promise<void> {
    this.listener?.onAccessTokenGenerated(BotClient.accessToken);
    
    let socketUrl = url;
    if (isReconnectionAttempt) {
      socketUrl += BotClient.IS_RECONNECT_PARAM;
    }

    try {
      this.websocket = new WebSocket(socketUrl);
      
      this.websocket.onopen = () => {
        LogUtils.d('BotClient', 'WebSocket connection opened');
        if (this.reconnectTimer) {
          clearTimeout(this.reconnectTimer);
          this.reconnectTimer = null;
        }
                 BotClient.connectionState = ConnectionState.CONNECTED;
         this.listener?.onConnectionStateChanged(BotClient.connectionState, isReconnectionAttempt);
         this.connecting = false;
         this.reconnectAttemptCount = 1;
      };

      this.websocket.onclose = (event) => {
        LogUtils.d('BotClient', 'WebSocket connection closed', event);
        BotClient.accessToken = '';
        BotClient.jwtToken = '';
        BotClient.botUserId = '';
                 BotClient.connectionState = ConnectionState.DISCONNECTED;
         this.listener?.onConnectionStateChanged(BotClient.connectionState, isReconnectionAttempt);
         this.connecting = false;
         this.reconnectToBot(true);
      };

      this.websocket.onmessage = (event) => {
        LogUtils.i('BotClient', 'Received message:', event.data);
        this.listener?.onBotResponse(event.data);
      };

      this.websocket.onerror = (error) => {
        LogUtils.e('BotClient', 'WebSocket error:', error);
        this.onError(isReconnectionAttempt, 'WebSocket connection error');
      };

    } catch (error) {
      LogUtils.e('BotClient', 'Error creating WebSocket connection:', error);
      this.onError(isReconnectionAttempt, 'Failed to create WebSocket connection');
    }
  }

  private onError(isReconnectionAttempt: boolean, message: string): void {
    this.connecting = false;
    BotClient.connectionState = ConnectionState.DISCONNECTED;
    LogUtils.e('BotClient', message);
    this.listener?.onConnectionStateChanged(BotClient.connectionState, isReconnectionAttempt);
    this.reconnectToBot(isReconnectionAttempt);
  }

  private reconnectToBot(isReconnectionAttempt: boolean): void {
    if (this.isReconnectAttemptRequired && this.botConfigModel) {
      this.connecting = false;
      const delay = this.getReconnectDelay();
      
      this.reconnectTimer = setTimeout(() => {
        if (this.isReconnectAttemptRequired) {
          this.initiateConnection(isReconnectionAttempt);
        }
      }, delay);
    }
  }

  private getReconnectDelay(): number {
    this.reconnectAttemptCount += this.reconnectAttemptCount === 1 ? 1 : 2;
    if (this.reconnectAttemptCount > BotClient.RECONNECT_ATTEMPT_LIMIT) {
      this.reconnectAttemptCount = 1;
    }
    
    LogUtils.d('BotClient', `Reconnection count: ${this.reconnectAttemptCount}`);
    return this.reconnectAttemptCount * 1000;
  }

  public async sendMessage(
    message: string,
    payload?: string,
    attachments?: Array<Record<string, any>>
  ): Promise<void> {
    if (!this.botConfigModel) return;

    this.setMoreCustomData();
    
    const botRequest = this.createRequestPayload(
      message,
      payload || message,
      payload ? message : undefined,
      attachments
    );

    if (this.botConfigModel.isWebHook) {
      await this.sendWebHookMessage(botRequest, false, message, payload, attachments);
      return;
    }

    const jsonPayload = JSON.stringify(botRequest);
    LogUtils.d('BotClient', 'Payload:', jsonPayload);
    
    if (BotClient.isConnected() && this.websocket) {
      this.websocket.send(jsonPayload);
      if (message) {
        this.listener?.onBotRequest(BotRequestState.SENT_BOT_REQ_SUCCESS, botRequest);
      }
    } else {
      if (message) {
        this.listener?.onBotRequest(BotRequestState.SENT_BOT_REQ_FAIL, botRequest);
      }
    }
  }

  private createRequestPayload(
    message: string,
    payload: string,
    displayText?: string,
    attachments?: Array<Record<string, any>>
  ): BotRequest {
    const timestamp = Date.now();
    
    return {
      id: `${timestamp}`,
      type: 'message',
      payload: {
        message: payload,
        displayText: displayText,
        attachments: attachments || []
      },
      timestamp,
      message: message,
      meta: {
        ...this.botCustomData,
        ...this.botInfoMap
      }
    };
  }

  private setMoreCustomData(): void {
    this.botCustomData['botToken'] = BotClient.accessToken;
    // Add any additional custom data here
  }

  private async sendWebHookMessage(
    botRequest: BotRequest,
    isNewSession: boolean,
    message: string,
    payload?: string,
    attachments?: Array<Record<string, any>>
  ): Promise<void> {
    if (!NetworkUtils.isNetworkAvailable()) {
      LogUtils.e('BotClient', 'No network available');
      return;
    }

    if (!this.botConfigModel) return;

    if (message && message !== BotClient.MSG_ON_CONNECT) {
      this.listener?.onBotRequest(BotRequestState.SENT_BOT_REQ_SUCCESS, botRequest);
    }

    this.setMoreCustomData();
    
    const webhookPayload = this.createWebHookRequestPayload(
      isNewSession,
      message,
      payload,
      attachments
    );

    LogUtils.d('BotClient', 'Webhook Payload:', JSON.stringify(webhookPayload));

    try {
      const response = await this.webHookRepository.sendMessage(
        this.botConfigModel.botId,
        BotClient.getJwtToken(),
        webhookPayload
      );

      if (message === BotClient.MSG_ON_CONNECT) {
        this.listener?.onConnectionStateChanged(ConnectionState.CONNECTED, false);
      }

             if (response?.messages) {
         response.messages.forEach((msg: any) => {
           this.listener?.onBotResponse(JSON.stringify(msg));
         });
       }
    } catch (error) {
      LogUtils.e('BotClient', 'Webhook error:', error);
    }
    
    this.connecting = false;
  }

  private createWebHookRequestPayload(
    isNewSession: boolean,
    message: string,
    payload?: string,
    attachments?: Array<Record<string, any>>
  ): Record<string, any> {
    return {
      message: payload || message,
      displayText: payload ? message : undefined,
      attachments: attachments || [],
      isNewSession,
      meta: {
        ...this.botCustomData,
        ...this.botInfoMap
      }
    };
  }

  private async getWebHookMeta(token: string, isReconnectionAttempt: boolean): Promise<void> {
    if (!NetworkUtils.isNetworkAvailable()) {
      this.connecting = false;
      this.reconnectToBot(isReconnectionAttempt);
      return;
    }

    if (!this.botConfigModel) return;

    try {
      await this.webHookRepository.getWebhookMeta(token, this.botConfigModel.botId);
      await this.sendWebHookMessage(
        {} as BotRequest,
        true,
        BotClient.MSG_ON_CONNECT,
        undefined,
        undefined
      );
    } catch (error) {
      LogUtils.e('BotClient', 'Webhook meta error:', error);
      this.reconnectToBot(isReconnectionAttempt);
    }
  }
}