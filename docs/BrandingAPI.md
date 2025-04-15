# Branding API Documentation

## Overview

The Branding API in the Kore Bot SDK provides functionality to customize the visual appearance of the chat interface. This document details the complete flow of the `getBrandingDetails` process and related components.

## Flow Diagram

```
[BotChatFragment/Activity] → [BotChatViewModel] → [BrandingRepository] → [BrandingApi] → [API Call]
                                                                     ↳ [Local Fallback] → [branding_response.txt]
```

## Process Flow

### 1. Initialization

```java
// In BotChatFragment or NewBotChatActivity
    brandingRepository.getBranding(context, botConfigModel.botId, token))
```

### 2. API Call Process

If api call fails then will return the default branding config details.

#### 2.1 Parameters

- `context`: Context
- `botId`: Bot identifier
- `jwtToken`: JWT token which created using jwtServerUrl

#### 2.2 API Response Handling

The API supports two response formats:

- `BotActiveThemeModel` (Primary format)

## Customizable Elements

1. **Chat Bubbles**

   - Bot message background and text colors
   - User message background and text colors
   - Chat bubble style

2. **Buttons**

   - Active/Inactive background colors
   - Active/Inactive text colors
   - Border colors

3. **Widget**

   - Body background color
   - Header background and text colors
   - Footer background and border colors
   - Footer hint text and colors

4. **Quick Replies**
   - Background color
   - Text color
   - Border color

## Configuration Override

The API supports overriding default SDK configurations:

```java
if (brandingNewDos.getOverride_kore_config() != null) {
    SDKConfiguration.OverrideKoreConfig.isEmojiShortcutEnable =
        brandingNewDos.getOverride_kore_config().isEmoji_short_cut();
    SDKConfiguration.OverrideKoreConfig.typing_indicator_timeout =
        brandingNewDos.getOverride_kore_config().getTyping_indicator_timeout();
}
```

## Error Handling

1. **Network Errors**

   - Falls back to local branding file
   - Maintains consistent UI appearance

2. **Parse Errors**

   - Attempts multiple format parsing
   - Uses fallback mechanisms

3. **Missing Properties**
   - Uses default values from SDKConfiguration
   - Maintains backward compatibility

## Best Practices

1. **Initialization**

   - Call getBrandingDetails after SDK initialization
   - Ensure proper JWT token availability

2. **Caching**

   - Use SharedPreferences for persistence
   - Implement proper fallback mechanisms

3. **UI Updates**

   - Apply changes on UI thread
   - Handle configuration changes properly

4. **Error Handling**
   - Implement proper fallbacks
   - Maintain consistent user experience
