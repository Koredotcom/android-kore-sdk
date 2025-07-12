export interface BotConfigModel {
  botName: string;
  botId: string;
  clientId: string;
  clientSecret: string;
  botUrl: string;
  identity: string;
  isWebHook: boolean;
  jwtServerUrl: string;
  jwtToken: string;
  enablePanel?: boolean;
}

export interface BotInfoModel {
  botName: string;
  botId: string;
  customData: Record<string, any>;
}

export interface BotRequest {
  id: string;
  type: string;
  payload: any;
  timestamp: number;
  message: string;
  meta?: Record<string, any>;
}

export interface BotResponse {
  id: string;
  type: string;
  payload: any;
  timestamp: number;
  message: any;
  botInfo: BotInfoModel;
  createdOn: string;
  timeMillis: number;
}

export interface BotMessage {
  message: string;
  timestamp: number;
  type: 'text' | 'image' | 'file' | 'audio' | 'video' | 'template';
  payload?: any;
  attachments?: Array<{
    name: string;
    url?: string;
    type?: string;
    size?: number;
  }>;
}

export enum ConnectionState {
  DISCONNECTED = 'DISCONNECTED',
  CONNECTING = 'CONNECTING',
  CONNECTED = 'CONNECTED',
  RECONNECTING = 'RECONNECTING'
}

export enum BotRequestState {
  SENT_BOT_REQ_SUCCESS = 'SENT_BOT_REQ_SUCCESS',
  SENT_BOT_REQ_FAIL = 'SENT_BOT_REQ_FAIL'
}

export interface BotConnectionListener {
  onBotResponse: (response: string) => void;
  onConnectionStateChanged: (state: ConnectionState, isReconnection: boolean) => void;
  onBotRequest: (code: BotRequestState, botRequest: BotRequest) => void;
  onAccessTokenGenerated: (token: string) => void;
}

export interface JwtTokenResponse {
  jwt: string;
}

export interface BotAuthorizationResponse {
  userInfo: {
    userId: string;
  };
  authorization: {
    accessToken: string;
  };
}

export interface RtmUrlResponse {
  url: string;
}

export interface WebHookResponse {
  messages: BotResponse[];
  pollId: string;
}