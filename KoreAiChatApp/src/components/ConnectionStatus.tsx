import React from 'react';
import { View, Text, StyleSheet, ActivityIndicator } from 'react-native';
import { ConnectionState } from '../types/BotConfigModel';

interface ConnectionStatusProps {
  connectionState: ConnectionState;
  isVisible?: boolean;
}

export const ConnectionStatus: React.FC<ConnectionStatusProps> = ({
  connectionState,
  isVisible = true,
}) => {
  if (!isVisible) return null;

  const getStatusColor = () => {
    switch (connectionState) {
      case ConnectionState.CONNECTED:
        return '#4CAF50';
      case ConnectionState.CONNECTING:
      case ConnectionState.RECONNECTING:
        return '#FF9800';
      case ConnectionState.DISCONNECTED:
        return '#F44336';
      default:
        return '#999999';
    }
  };

  const getStatusText = () => {
    switch (connectionState) {
      case ConnectionState.CONNECTED:
        return 'Connected';
      case ConnectionState.CONNECTING:
        return 'Connecting...';
      case ConnectionState.RECONNECTING:
        return 'Reconnecting...';
      case ConnectionState.DISCONNECTED:
        return 'Disconnected';
      default:
        return 'Unknown';
    }
  };

  const showSpinner = connectionState === ConnectionState.CONNECTING || 
                      connectionState === ConnectionState.RECONNECTING;

  return (
    <View style={[styles.container, { backgroundColor: getStatusColor() }]}>
      <View style={styles.content}>
        {showSpinner && (
          <ActivityIndicator size="small" color="#FFFFFF" style={styles.spinner} />
        )}
        <Text style={styles.statusText}>{getStatusText()}</Text>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    paddingVertical: 8,
    paddingHorizontal: 16,
  },
  content: {
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  spinner: {
    marginRight: 8,
  },
  statusText: {
    color: '#FFFFFF',
    fontSize: 14,
    fontWeight: '500',
  },
});