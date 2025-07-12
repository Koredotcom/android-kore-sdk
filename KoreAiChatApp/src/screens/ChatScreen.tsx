import React, { useState, useEffect, useRef } from 'react';
import {
  View,
  StyleSheet,
  FlatList,
  SafeAreaView,
  KeyboardAvoidingView,
  Platform,
  Alert,
} from 'react-native';
import { ChatMessage } from '../components/ChatMessage';
import { ChatInput } from '../components/ChatInput';
import { ConnectionStatus } from '../components/ConnectionStatus';
import { BotClient } from '../services/BotClient';
import {
  BotConfigModel,
  BotMessage,
  ConnectionState,
  BotConnectionListener,
  BotRequestState,
  BotRequest,
} from '../types/BotConfigModel';

interface ChatScreenProps {
  botConfig: BotConfigModel;
}

export const ChatScreen: React.FC<ChatScreenProps> = ({ botConfig }) => {
  const [messages, setMessages] = useState<Array<BotMessage & { isUser: boolean }>>([]);
  const [connectionState, setConnectionState] = useState<ConnectionState>(ConnectionState.DISCONNECTED);
  const [isLoading, setIsLoading] = useState(false);
  const botClient = useRef<BotClient>(BotClient.getInstance());
  const flatListRef = useRef<FlatList>(null);

  useEffect(() => {
    initializeBotClient();
    return () => {
      botClient.current.disconnectBot();
    };
  }, []);

  const initializeBotClient = async () => {
    const listener: BotConnectionListener = {
      onBotResponse: (response: string) => {
        try {
          const parsedResponse = JSON.parse(response);
          const botMessage: BotMessage & { isUser: boolean } = {
            message: parsedResponse.message || JSON.stringify(parsedResponse),
            timestamp: Date.now(),
            type: 'text',
            payload: parsedResponse,
            isUser: false,
          };

          setMessages(prev => [...prev, botMessage]);
          scrollToBottom();
        } catch (error) {
          console.error('Error parsing bot response:', error);
          const errorMessage: BotMessage & { isUser: boolean } = {
            message: 'Error parsing response',
            timestamp: Date.now(),
            type: 'text',
            isUser: false,
          };
          setMessages(prev => [...prev, errorMessage]);
        }
      },
      onConnectionStateChanged: (state: ConnectionState, isReconnection: boolean) => {
        setConnectionState(state);
        setIsLoading(state === ConnectionState.CONNECTING || state === ConnectionState.RECONNECTING);
        
        if (state === ConnectionState.CONNECTED && !isReconnection) {
          // Add welcome message
          const welcomeMessage: BotMessage & { isUser: boolean } = {
            message: 'Connected to bot. How can I help you today?',
            timestamp: Date.now(),
            type: 'text',
            isUser: false,
          };
          setMessages(prev => [...prev, welcomeMessage]);
        }
      },
      onBotRequest: (code: BotRequestState, botRequest: BotRequest) => {
        if (code === BotRequestState.SENT_BOT_REQ_SUCCESS) {
          // Message sent successfully
          console.log('Message sent successfully');
        } else {
          // Message failed to send
          Alert.alert('Error', 'Failed to send message. Please try again.');
        }
      },
      onAccessTokenGenerated: (token: string) => {
        console.log('Access token generated:', token);
      },
    };

    botClient.current.setListener(listener);
    
    try {
      await botClient.current.connectToBot(botConfig, true);
    } catch (error) {
      console.error('Error connecting to bot:', error);
      Alert.alert('Connection Error', 'Failed to connect to bot. Please check your configuration.');
    }
  };

  const sendMessage = async (message: string) => {
    if (!message.trim()) return;

    // Add user message to chat
    const userMessage: BotMessage & { isUser: boolean } = {
      message: message,
      timestamp: Date.now(),
      type: 'text',
      isUser: true,
    };

    setMessages(prev => [...prev, userMessage]);
    scrollToBottom();

    // Send message to bot
    try {
      await botClient.current.sendMessage(message);
    } catch (error) {
      console.error('Error sending message:', error);
      Alert.alert('Error', 'Failed to send message. Please try again.');
    }
  };

  const scrollToBottom = () => {
    setTimeout(() => {
      if (flatListRef.current) {
        flatListRef.current.scrollToEnd({ animated: true });
      }
    }, 100);
  };

  const handleMessagePress = (message: BotMessage) => {
    // Handle message press (e.g., show details, copy text, etc.)
    console.log('Message pressed:', message);
  };

  const renderMessage = ({ item }: { item: BotMessage & { isUser: boolean } }) => (
    <ChatMessage
      message={item}
      isUser={item.isUser}
      onMessagePress={handleMessagePress}
    />
  );

  return (
    <SafeAreaView style={styles.container}>
      <ConnectionStatus connectionState={connectionState} />
      
      <KeyboardAvoidingView
        style={styles.chatContainer}
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        keyboardVerticalOffset={Platform.OS === 'ios' ? 88 : 0}
      >
        <FlatList
          ref={flatListRef}
          data={messages}
          renderItem={renderMessage}
          keyExtractor={(item, index) => `${item.timestamp}-${index}`}
          style={styles.messagesList}
          contentContainerStyle={styles.messagesContainer}
          onContentSizeChange={scrollToBottom}
          showsVerticalScrollIndicator={false}
        />
        
        <ChatInput
          onSendMessage={sendMessage}
          disabled={connectionState !== ConnectionState.CONNECTED || isLoading}
          placeholder="Type your message..."
        />
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#FFFFFF',
  },
  chatContainer: {
    flex: 1,
  },
  messagesList: {
    flex: 1,
  },
  messagesContainer: {
    paddingTop: 16,
    paddingBottom: 16,
    flexGrow: 1,
  },
});