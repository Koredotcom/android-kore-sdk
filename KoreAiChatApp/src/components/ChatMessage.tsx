import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Dimensions,
} from 'react-native';
import { BotMessage } from '../types/BotConfigModel';

interface ChatMessageProps {
  message: BotMessage;
  isUser: boolean;
  onMessagePress?: (message: BotMessage) => void;
}

const { width } = Dimensions.get('window');

export const ChatMessage: React.FC<ChatMessageProps> = ({
  message,
  isUser,
  onMessagePress,
}) => {
  const handlePress = () => {
    if (onMessagePress) {
      onMessagePress(message);
    }
  };

  const renderMessageContent = () => {
    switch (message.type) {
      case 'text':
        return (
          <Text style={[styles.messageText, isUser ? styles.userText : styles.botText]}>
            {message.message}
          </Text>
        );
      case 'template':
        return (
          <View style={styles.templateContainer}>
            <Text style={[styles.messageText, isUser ? styles.userText : styles.botText]}>
              {message.message}
            </Text>
            {message.payload && (
              <View style={styles.templatePayload}>
                <Text style={styles.templateText}>
                  {JSON.stringify(message.payload, null, 2)}
                </Text>
              </View>
            )}
          </View>
        );
      default:
        return (
          <Text style={[styles.messageText, isUser ? styles.userText : styles.botText]}>
            {message.message}
          </Text>
        );
    }
  };

  return (
    <TouchableOpacity
      style={[
        styles.messageContainer,
        isUser ? styles.userMessage : styles.botMessage,
      ]}
      onPress={handlePress}
      disabled={!onMessagePress}
    >
      <View style={[styles.messageBubble, isUser ? styles.userBubble : styles.botBubble]}>
        {renderMessageContent()}
        <Text style={[styles.timestamp, isUser ? styles.userTimestamp : styles.botTimestamp]}>
          {new Date(message.timestamp).toLocaleTimeString()}
        </Text>
      </View>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  messageContainer: {
    marginVertical: 4,
    marginHorizontal: 16,
    maxWidth: width * 0.8,
  },
  userMessage: {
    alignSelf: 'flex-end',
  },
  botMessage: {
    alignSelf: 'flex-start',
  },
  messageBubble: {
    padding: 12,
    borderRadius: 16,
    minWidth: 60,
  },
  userBubble: {
    backgroundColor: '#007AFF',
    borderBottomRightRadius: 4,
  },
  botBubble: {
    backgroundColor: '#E5E5EA',
    borderBottomLeftRadius: 4,
  },
  messageText: {
    fontSize: 16,
    lineHeight: 20,
  },
  userText: {
    color: '#FFFFFF',
  },
  botText: {
    color: '#000000',
  },
  timestamp: {
    fontSize: 12,
    marginTop: 4,
    opacity: 0.7,
  },
  userTimestamp: {
    color: '#FFFFFF',
    textAlign: 'right',
  },
  botTimestamp: {
    color: '#000000',
    textAlign: 'left',
  },
  templateContainer: {
    flex: 1,
  },
  templatePayload: {
    marginTop: 8,
    padding: 8,
    backgroundColor: 'rgba(255, 255, 255, 0.1)',
    borderRadius: 8,
  },
  templateText: {
    fontSize: 12,
    color: '#FFFFFF',
    fontFamily: 'monospace',
  },
});