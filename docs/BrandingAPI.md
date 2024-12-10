# Branding API Documentation

## Overview
The Branding API in the Kore Bot SDK provides functionality to customize the visual appearance of the chat interface. This document details the complete flow of the `getBrandingDetails` process and related components.

## Flow Diagram
```
[BotChatFragment/Activity] → [BotChatViewModel] → [BrandingRepository] → [BrandingRestBuilder] → [API Call]
                                                                     ↳ [Local Fallback] → [branding_response.txt]
```

## Process Flow

### 1. Initialization
```java
// In BotChatFragment or NewBotChatActivity
mViewModel.getBrandingDetails(
    SDKConfiguration.Client.bot_id,
    jwt,
    "published",
    "1",
    "en_US"
);
```

### 2. API Call Process

#### 2.1 Parameters
- `botId`: Bot identifier
- `botToken`: JWT token with "bearer" prefix
- `state`: Current state (e.g., "published")
- `version`: API version
- `language`: Language code (e.g., "en_US")

#### 2.2 API Response Handling
The API supports two response formats:
1. `BotActiveThemeModel` (Primary format)
2. `BrandingNewModel` with `BrandingV3Model` (Fallback format)

### 3. Data Processing Flow

#### 3.1 Primary Flow (BotActiveThemeModel)
```java
try {
    // Parse response to BotActiveThemeModel
    BotActiveThemeModel brandingNewDos = gson.fromJson(resp, avtiveThemeType);
    
    // Convert to BrandingModel
    BrandingModel brandingModel = new BrandingModel();
    // Set bot chat properties
    brandingModel.setBotchatBgColor(brandingNewDos.getBotMessage().getBubbleColor());
    brandingModel.setBotchatTextColor(brandingNewDos.getBotMessage().getFontColor());
    
    // Set user chat properties
    brandingModel.setUserchatBgColor(brandingNewDos.getUserMessage().getBubbleColor());
    brandingModel.setUserchatTextColor(brandingNewDos.getUserMessage().getFontColor());
    
    // Set button properties
    brandingModel.setButtonActiveBgColor(brandingNewDos.getButtons().getDefaultButtonColor());
    // ... additional properties
}
```

#### 3.2 Fallback Flow (BrandingV3Model)
```java
try {
    // Parse response to BrandingNewModel
    BrandingNewModel brandingNewModel = gson.fromJson(resp, avtiveThemeType);
    BrandingV3Model brandingNewDos = brandingNewModel.getV3();
    
    // Convert to BrandingModel
    BrandingModel brandingModel = new BrandingModel();
    // Set properties from V3 model
    brandingModel.setBotchatBgColor(brandingNewDos.getBody().getBot_message().getBg_color());
    // ... additional properties
}
```

#### 3.3 Local Fallback
If both API response formats fail or network is unavailable:
```java
public BrandingModel getBrandingDataFromTxt() {
    // Read from local resource
    InputStream is = context.getResources().openRawResource(R.raw.branding_response);
    // Parse and return default branding
}
```

### 4. Data Storage

The branding details are stored in SharedPreferences:
```java
public void onEvent(BrandingModel brandingModel) {
    SharedPreferences.Editor editor = context.getSharedPreferences(
        BotResponse.THEME_NAME,
        Context.MODE_PRIVATE
    ).edit();
    
    // Store branding colors
    editor.putString(BotResponse.BUBBLE_LEFT_BG_COLOR, brandingModel.getBotchatBgColor());
    editor.putString(BotResponse.BUBBLE_LEFT_TEXT_COLOR, brandingModel.getBotchatTextColor());
    // ... additional properties
    editor.apply();
}
```

### 5. UI Update Process

The branding changes are applied through the `BotChatViewListener`:
```java
public void onBrandingDetails(BrandingModel brandingModel) {
    // Update chat interface colors
    botContentFragment.changeThemeBackGround(
        brandingModel.getWidgetBodyColor(),
        brandingModel.getWidgetHeaderColor(),
        brandingModel.getWidgetTextColor(),
        brandingModel.getBotName()
    );
    
    // Update footer colors
    baseFooterFragment.changeThemeBackGround(
        brandingModel.getWidgetFooterColor(),
        brandingModel.getWidgetFooterHintColor()
    );
}
```

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
