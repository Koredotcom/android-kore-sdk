/* eslint-disable @typescript-eslint/no-shadow */
/* eslint-disable react-native/no-inline-styles */
import * as React from 'react';
import {ReactNode, RefObject} from 'react';
import {
  Platform,
  StyleSheet,
  View,
  SafeAreaView,
  Linking,
  ActivityIndicator,
  Text,
  TouchableOpacity,
  Modal,
  ScrollView,
  Keyboard,
  Dimensions,
  StatusBar,
  BackHandler,
  Alert,
} from 'react-native';
import uuid from '../utils/uuid';
import dayjs from 'dayjs';
import localizedFormat from 'dayjs/plugin/localizedFormat';

import InputToolbar from './components/InputToolbar';
import MessageContainer from './components/MessageContainer';
import NetInfo from '@react-native-community/netinfo';
import {ThemeProvider} from '../theme/ThemeContext';
import { useNavigation } from '@react-navigation/native';
import {
  MIN_COMPOSER_HEIGHT,
  MAX_COMPOSER_HEIGHT,
  DEFAULT_PLACEHOLDER,
  TIME_FORMAT,
  DATE_FORMAT,
  TEMPLATE_TYPES,
  KORA_ITEM_CLICK,
  LIMIT_MESSAGE,
  MAX_SOURCE_LIMIT,
  attach_menu_buttons,
  MAX_FILE_NAME_LENGTH,
  HeaderIconsId,
  BRANDING_RESPONSE_FILE,
} from '../constants/Constant';

import KoreBotClient, {
  RTM_EVENT,
  BotConfigModel,
  APP_STATE,
  ActiveThemeAPI,
} from 'rn-kore-bot-socket-lib-v77';

import {TEMPLATE_STYLE_VALUES} from '../theme/styles';
import {
  getDrawableByExt,
  getItemId,
  getTemplateType,
  isBlackStatusBar,
  isWhiteStatusBar,
  normalize,
  renderImage,
} from '../utils/helpers';
import QuickReplies from '../templates/QuickReplies';
import Color from '../theme/Color';
import TableTemplate from '../templates/TableTemplate';
import {IThemeType} from '../theme/IThemeType';
import TypingIndicator from './components/BotTypingIndicator';
import ChatHeader from './components/header/ChatHeader';
import {isAndroid, isIOS} from '../utils/PlatformCheck';
import {
  documentPickIOSPermission,
  documentPickPermission,
} from '../utils/PermissionsUtils';
import DocumentPicker, {types} from 'react-native-document-picker';
import FileUploadQueue from '../FileUploader/FileUploadQueue';

import * as Progress from 'react-native-progress';

import {launchCamera} from 'react-native-image-picker';
import {SvgIcon} from '../utils/SvgIcon';
import FileIcon from '../utils/FileIcon';
import AsyncStorage from '@react-native-async-storage/async-storage';
import ListViewTemplate from '../templates/ListViewTemplate';
import CustomTemplate, {
  CustomViewProps,
  CustomViewState,
} from '../templates/CustomTemplate';
import Welcome from '../components/WelcomeScreen';

// Conditional import for TTS
let Tts: any = null;
try {
  Tts = require('react-native-tts').default;
} catch (error) {
  console.warn('react-native-tts not available, text-to-speech features will be disabled');
}

import CustomAlertComponent from '../components/CustomAlertComponent';

dayjs.extend(localizedFormat);

const imageFilesTypes = ['jpg', 'jpeg', 'png'];
const windowWidth = Dimensions.get('window').width;

interface KoraChatProps {
  messages?: any[];
  messagesContainerStyle?: any;
  text?: string | null;
  actionSheet: any;
  _actionSheetRef?: any;
  inverted: any;
  locale: string;
  isTyping?: boolean;
  onDragList: any;
  onSend: any;
  onInputTextChanged: any;
  renderSuggestionsView: any;
  renderMediaView: any;
  initialText?: any;
  minComposerHeight: number;
  textInputProps: any;
  maxInputLength: number;
  renderInputToolbar: any;
  renderComposer: any;
  renderSend: any;
  renderChatFooter: any;
  renderLoading: any;
  renderTypingIndicator: any;
  wrapInSafeArea: any;
  parsePatterns?: any;
  alignTop: boolean;
  alwaysShowSend?: boolean;
  scrollToBottom?: boolean;
  style?: any;
  onListItemClick?: any;
  onLongPress?: any;
  botConfig: BotConfigModel;
  renderQuickRepliesView?: any;
  renderChatHeader?: any;
  onHeaderActionsClick?: any;
  navigation?: any;
  route?: any;
  statusBarColor?: (colorCode: any) => void;
  templateInjection?: Map<
    string,
    CustomTemplate<CustomViewProps, CustomViewState>
  >;
}

interface KoraChatState {
  isInitialized: boolean;
  composerHeight: number;
  messagesContainerHeight: number | null;
  typingDisabled: boolean;
  text: string | null | undefined;
  messages: any[];
  isNetConnected?: boolean;
  showLoader?: boolean;
  quickReplies?: any;
  modalVisible?: boolean;
  viewMoreObj?: any;
  isBotResponseLoading?: boolean;
  menuItems?: any[];
  showAttachmentModal?: boolean;
  showSeeMoreModal?: boolean;
  progressObj?: any;
  isShowProgress?: boolean;
  mediaPayload?: any;
  onSTTValue?: any;
  currentTemplate?: any;
  currentTemplateData?: any;
  activetheme?: any;
  wlcomeModalVisible?: boolean;
  currentTemplateHeading?: any;
  themeData?: IThemeType;
  isTTSenable?: boolean;
  isReconnecting?: boolean;
}

export default class KoreChat extends React.Component<
  KoraChatProps,
  KoraChatState
> {
  _messageContainerRef: RefObject<any> = React.createRef();
  textInput: any = null;
  alertRef = React.createRef<CustomAlertComponent>();

  _isFirstLayout = true;
  _locale = 'en';
  invertibleScrollViewProps = {};
  _unsubscribeConn: any;
  _backHandlerSubscription: any;
  _beforeRemoveListener: any;
  
  private fileUploadQueue?: FileUploadQueue;
  private themApi: ActiveThemeAPI;

  constructor(props: KoraChatProps) {
    super(props);
    this.themApi = new ActiveThemeAPI();
    this.fileUploadQueue = undefined;

    this.state = {
      isInitialized: false,
      composerHeight: 0,
      messagesContainerHeight: 0,
      typingDisabled: false,
      text: '',
      messages: [],
      isNetConnected: false,
      showLoader: true,
      quickReplies: [],
      modalVisible: false,
      viewMoreObj: undefined,
      isBotResponseLoading: false,
      menuItems: undefined,
      showAttachmentModal: false,
      showSeeMoreModal: false,
      progressObj: {},
      isShowProgress: false,
      mediaPayload: [],
      onSTTValue: null,
      currentTemplate: null,
      currentTemplateData: null,
      activetheme: null,
      wlcomeModalVisible: false,
      currentTemplateHeading: undefined,
      isReconnecting: false,
    };
  }

  componentDidMount() {
    this._unsubscribeConn = NetInfo.addEventListener(state => {
      const {isConnected} = state;
      if (!this.state.isNetConnected && isConnected) {
        setTimeout(() => {
          KoreBotClient.getInstance().setIsNetworkAvailable(isConnected);
          KoreBotClient.getInstance().checkSocketAndReconnect();
        }, 10);
      }

      this.setState({isNetConnected: isConnected ? isConnected : false});
    });

    // Add BackHandler listener
    this._backHandlerSubscription = BackHandler.addEventListener('hardwareBackPress', this.handleBackPress);

    // Add beforeRemove listener if navigation is available
    if (this.props.navigation) {
      this._beforeRemoveListener = this.props.navigation.addListener('beforeRemove', this.handleBeforeRemove);
    }

    KoreBotClient.getInstance().setSessionActive(true);
    const {text} = this.props;
    this.setisChatMounted(true);
    this.initLocale();
    this.setTextFromProp(text);
    this.stopTTS();

    setTimeout(() => {
      this.init();
    }, 1000);
  }

  componentWillUnmount() {
    this.setisChatMounted(false);
    
    // Remove BackHandler listener
    if (this._backHandlerSubscription) {
      this._backHandlerSubscription.remove();
    }

    // Remove beforeRemove listener
    if (this._beforeRemoveListener) {
      this._beforeRemoveListener();
    }

    const botClient = KoreBotClient.getInstance();
    botClient
      .removeAllListeners(RTM_EVENT.CONNECTING);
    botClient
      .removeAllListeners(RTM_EVENT.ON_OPEN);
    botClient
      .removeAllListeners(RTM_EVENT.ON_MESSAGE);
      KoreBotClient.getInstance()?.disconnect();
    this.stopTTS();
  }

  // Handle hardware back button press
  private handleBackPress = (): boolean => {
    // Check if any modals are open and close them first
    if (this.state.showAttachmentModal) {
      this.setState({ showAttachmentModal: false });
      return true; // Prevent default back behavior
    }

    if (this.state.showSeeMoreModal) {
      this.setState({ showSeeMoreModal: false });
      return true; // Prevent default back behavior
    }

    if (this.state.menuItems && this.state.menuItems.length > 0) {
      this.setState({ menuItems: undefined });
      return true; // Prevent default back behavior
    }

    if (this.state.modalVisible) {
      this.setState({ modalVisible: false, viewMoreObj: undefined });
      return true; // Prevent default back behavior
    }

    if (this.state.wlcomeModalVisible) {
      this.setState({ wlcomeModalVisible: false });
      return true; // Prevent default back behavior
    }

    // If no modals are open, handle navigation
    try {
      if (this.props.navigation?.canGoBack?.()) {
        this.props.navigation?.goBack?.();
      } else {
        BackHandler.exitApp();
      }
    } catch (error) {
      BackHandler.exitApp();
    }

    return true; // Prevent default back behavior
  };

  // Handle beforeRemove navigation event
  private handleBeforeRemove = (e: any) => {
    // Check if there are any unsaved changes or active operations
    const hasUnsavedChanges = this.state.mediaPayload?.length > 0 || 
                             this.state.text?.trim()?.length > 0 ||
                             this.state.isBotResponseLoading;

    if (hasUnsavedChanges) {
      // Prevent default behavior
      e.preventDefault();

      // Show confirmation dialog
      Alert.alert(
        'Leave Chat?',
        'You have unsaved changes. Are you sure you want to leave?',
        [
          {
            text: 'Cancel',
            style: 'cancel',
            onPress: () => {
              // Do nothing, stay on the screen
            },
          },
          {
            text: 'Leave',
            style: 'destructive',
            onPress: () => {
              // Clear any unsaved state
              this.setState({
                mediaPayload: [],
                text: '',
                isBotResponseLoading: false,
              });
              
              // Allow navigation to proceed
              this.props.navigation?.dispatch(e.data.action);
            },
          },
        ]
      );
    }
    // If no unsaved changes, allow navigation to proceed normally
  };

  // ... rest of your existing methods would go here ...
  
  // Placeholder for the rest of the component implementation
  private init = () => {
    // Your existing init implementation
  };

  private setisChatMounted = (value: boolean) => {
    this._isChatMounted = value;
  };

  private stopTTS = () => {
    if (!Tts || !Tts.stop) {
      return;
    }
    try {
      Tts.stop()
        .then((_value: any) => {
          //console.log('TTS stoped');
        })
        .catch((error: any) => {
          console.log('TTS stoped error -->:', error);
        });
    } catch (error1) {
      console.log('TTS stoped error1 -->:', error1);
    }
  };

  private initLocale = () => {
    if (this.props.locale === null) {
      this.setLocale('en');
    } else {
      this.setLocale(this.props.locale || 'en');
    }
  };

  private setLocale = (locale: string) => {
    this._locale = locale;
  };

  private setTextFromProp = (textProp: string | undefined | null) => {
    if (textProp !== undefined && textProp !== this.state.text) {
      this.setState({text: textProp});
    }
  };

  render() {
    return (
      <View style={styles.container}>
        <Text>KoreChat Component</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});