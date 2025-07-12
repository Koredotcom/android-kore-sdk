# Kore AI Chat App - React Native

A React Native application equivalent to the Kore.ai Android SDK, providing chat capabilities with Kore AI bots for both Android and iOS platforms.

## Features

- **Cross-platform**: Runs on both Android and iOS
- **WebSocket Communication**: Real-time chat with bot using WebSocket connections
- **Webhook Support**: Alternative communication method using HTTP webhooks
- **JWT Authentication**: Secure authentication with JWT tokens
- **Auto-reconnection**: Automatic reconnection on connection loss
- **Custom Templates**: Support for custom message templates
- **File Attachments**: Support for sending files and attachments
- **Connection Status**: Visual indicators for connection state
- **Modern UI**: Clean, modern chat interface with message bubbles
- **TypeScript**: Full TypeScript support for better development experience

## Architecture

The application follows a clean architecture pattern:

```
src/
├── components/          # Reusable UI components
│   ├── ChatMessage.tsx  # Individual message display
│   ├── ChatInput.tsx    # Message input component
│   └── ConnectionStatus.tsx # Connection status indicator
├── screens/            # Screen components
│   └── ChatScreen.tsx  # Main chat interface
├── services/           # Business logic and API services
│   ├── BotClient.ts    # Core bot communication service
│   ├── JwtRepository.ts # JWT token management
│   └── WebHookRepository.ts # Webhook communication
├── types/              # TypeScript type definitions
│   ├── BotConfigModel.ts # Bot configuration types
│   └── global.d.ts     # Global type declarations
├── utils/              # Utility functions
│   ├── NetworkUtils.ts # Network connectivity utilities
│   └── LogUtils.ts     # Logging utilities
└── config/             # Configuration files
    └── BotConfig.ts    # Bot configuration
```

## Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd KoreAiChatApp
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **iOS specific setup**
   ```bash
   cd ios && pod install && cd ..
   ```

## Configuration

1. **Update Bot Configuration**
   
   Edit `src/config/BotConfig.ts` and replace the placeholder values with your actual bot credentials:

   ```typescript
   export const getBotConfig = (): BotConfigModel => {
     return {
       botName: 'Your Bot Name',
       botId: 'your-bot-id',
       clientId: 'your-client-id',
       clientSecret: 'your-client-secret',
       botUrl: 'https://your-bot-url.com/',
       identity: 'unique-device-identity',
       isWebHook: false, // Set to true for webhook communication
       jwtServerUrl: 'https://your-jwt-server.com/token',
       jwtToken: '', // Optional: Direct JWT token
       enablePanel: false,
     };
   };
   ```

2. **JWT Server Setup**
   
   If you're using JWT server authentication, ensure your JWT server is accessible and returns tokens in the expected format:

   ```typescript
   {
     "jwt": "your-jwt-token"
   }
   ```

## Running the Application

### Android
```bash
npx react-native run-android
```

### iOS
```bash
npx react-native run-ios
```

### Metro Bundler
```bash
npx react-native start
```

## API Reference

### BotClient

The main service for bot communication:

```typescript
import { BotClient } from './src/services/BotClient';

const botClient = BotClient.getInstance();

// Set up listener
botClient.setListener({
  onBotResponse: (response: string) => {
    // Handle bot response
  },
  onConnectionStateChanged: (state: ConnectionState, isReconnection: boolean) => {
    // Handle connection state changes
  },
  onBotRequest: (code: BotRequestState, botRequest: BotRequest) => {
    // Handle request status
  },
  onAccessTokenGenerated: (token: string) => {
    // Handle access token generation
  }
});

// Connect to bot
await botClient.connectToBot(botConfig, true);

// Send message
await botClient.sendMessage('Hello, bot!');

// Disconnect
botClient.disconnectBot();
```

### Connection States

- `DISCONNECTED`: Not connected to bot
- `CONNECTING`: Attempting to connect
- `CONNECTED`: Successfully connected
- `RECONNECTING`: Attempting to reconnect

### Message Types

- `text`: Plain text message
- `image`: Image message
- `file`: File attachment
- `audio`: Audio message
- `video`: Video message
- `template`: Custom template message

## Components

### ChatScreen

Main chat interface component:

```typescript
import { ChatScreen } from './src/screens/ChatScreen';

<ChatScreen botConfig={botConfig} />
```

### ChatMessage

Individual message display:

```typescript
import { ChatMessage } from './src/components/ChatMessage';

<ChatMessage
  message={message}
  isUser={false}
  onMessagePress={handleMessagePress}
/>
```

### ChatInput

Message input component:

```typescript
import { ChatInput } from './src/components/ChatInput';

<ChatInput
  onSendMessage={sendMessage}
  disabled={false}
  placeholder="Type your message..."
/>
```

### ConnectionStatus

Connection status indicator:

```typescript
import { ConnectionStatus } from './src/components/ConnectionStatus';

<ConnectionStatus connectionState={connectionState} />
```

## Customization

### Custom Message Templates

You can create custom message templates by extending the `ChatMessage` component:

```typescript
const renderCustomTemplate = (message: BotMessage) => {
  // Your custom template logic here
  return <CustomTemplateComponent message={message} />;
};
```

### Styling

All components use StyleSheet for styling. You can customize the appearance by modifying the styles in each component file.

### Branding

Update colors, fonts, and other branding elements in the component stylesheets.

## Troubleshooting

### Common Issues

1. **Connection Failed**
   - Check your bot configuration
   - Verify JWT server is accessible
   - Ensure bot is properly configured on Kore.ai platform

2. **WebSocket Connection Issues**
   - Check network connectivity
   - Verify WebSocket URL is correct
   - Check firewall settings

3. **JWT Token Issues**
   - Verify JWT server is returning valid tokens
   - Check token expiration
   - Ensure proper credentials

### Debug Mode

Enable debug logging by setting `__DEV__` to `true` in development mode.

## Platform-Specific Notes

### Android
- Requires Android SDK 26 or higher
- Network security config may need updates for HTTP connections

### iOS
- Requires iOS 12.0 or higher
- App Transport Security settings may need configuration

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions:
- Check the [Kore.ai Developer Documentation](https://developer.kore.ai/)
- Create an issue in this repository
- Contact support at support@kore.ai

## Comparison with Android SDK

This React Native implementation provides equivalent functionality to the original Kotlin Android SDK:

| Feature | Android SDK | React Native |
|---------|-------------|--------------|
| WebSocket Communication | ✅ | ✅ |
| Webhook Support | ✅ | ✅ |
| JWT Authentication | ✅ | ✅ |
| Auto-reconnection | ✅ | ✅ |
| Custom Templates | ✅ | ✅ |
| File Attachments | ✅ | ✅ |
| Cross-platform | ❌ | ✅ |
| TypeScript Support | ❌ | ✅ |

## Next Steps

- Implement file upload functionality
- Add support for voice messages
- Enhance custom template system
- Add offline message queueing
- Implement push notifications
- Add analytics and logging
