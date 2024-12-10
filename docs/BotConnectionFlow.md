Bot Connection Flow Documentation

# Overview

The connectToBot flow in BotChatViewModel manages the WebSocket connection between the client and the Kore.ai bot server. This flow handles both initial connections and reconnection scenarios.

# Connection Flow Diagram

\[User\] -> \[BotChatViewModel\] -> \[BotSocketConnectionManager\] -> \[WebSocket Server\]

↑ ↓ ↓

└──────\[BotChatView\]←──────\[SocketChatListener\]

Main Components

1.  BotChatViewModel

public void connectToBot(boolean isReconnect) { if (!SDKConfiguration.Client.isWebHook) {

BotSocketConnectionManager.getInstance().setChatListener(sListener);

}

BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithReconnect( context,

SDKConfiguration.Server.customData, isReconnect

);

}

Parameters

isReconnect: Boolean flag indicating if this is a reconnection attempt

1.  Socket Listener Implementation

final SocketChatListener sListener = new SocketChatListener() { @Override

public void onMessage(BotResponse botResponse) @Override

public void onConnectionStateChanged(CONNECTION\_STATE state, boolean isReconnection) @Override

public void onMessage(SocketDataTransferModel data)

}

# Connection States and Events

*   1.  Connection States

CONNECTED: Successfully connected to the bot

RECONNECTION\_STOPPED: Reconnection attempts have been exhausted

Other states managed by BaseSocketConnectionManager.CONNECTION\_STATE

*   1.  Event Types

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAYAAADED76LAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAf0lEQVQYlX2PsQlDMQxExQ8HqewRskBwplAhDZkt/m/+EhZ4Ak2QlHajVIZgSA6uOh7co4ggMyuqugMYAIaq7mZWIoLIzEpK6U1E8d2c88vMysXdn621Oy3pvV/d/UYAxkrPAhjbSq7ZmPn8NTLzSbXWx7+TNDVF5JiaInJMzQ+ohE6avqQn4AAAAABJRU5ErkJggg==) TYPE\_TEXT\_MESSAGE: Regular text messages

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAgAAAAICAYAAADED76LAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAf0lEQVQYlX2PsQlDMQxExQ8HqewRskBwplAhDZkt/m/+EhZ4Ak2QlHajVIZgSA6uOh7co4ggMyuqugMYAIaq7mZWIoLIzEpK6U1E8d2c88vMysXdn621Oy3pvV/d/UYAxkrPAhjbSq7ZmPn8NTLzSbXWx7+TNDVF5JiaInJMzQ+ohE6avqQn4AAAAABJRU5ErkJggg==) TYPE\_MESSAGE\_UPDATE: Message updates/edits

# Connection Flow Steps

## Initialization

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAArklEQVQYlY2QMQ6FMAxDTYYO3KfNWWD4M1PvwpQdqTlLEefJwEDyJ6Qv9AcsebG9+CEi4O6Dqk7M3InoIqKLmbuqTu4+RATg7kOtdQUQ/1xrXd19gKpOACKldIrIYmajmY0isqSUTgChqhOYuQMIEVkiAr8WkQVAMHMHEV0AwszG59DMRgBBRBfhpSjnfADAtm2fZ3lnOefj/ZnXeG7grbW5lLLfwEspe2ttvoF/Adkyog2mm1YnAAAAAElFTkSuQmCC) Check if WebHook mode is disabled ![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAArklEQVQYlY2QMQ6FMAxDTYYO3KfNWWD4M1PvwpQdqTlLEefJwEDyJ6Qv9AcsebG9+CEi4O6Dqk7M3InoIqKLmbuqTu4+RATg7kOtdQUQ/1xrXd19gKpOACKldIrIYmajmY0isqSUTgChqhOYuQMIEVkiAr8WkQVAMHMHEV0AwszG59DMRgBBRBfhpSjnfADAtm2fZ3lnOefj/ZnXeG7grbW5lLLfwEspe2ttvoF/Adkyog2mm1YnAAAAAElFTkSuQmCC) Set up socket chat listener

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAArklEQVQYlY2QMQ6FMAxDTYYO3KfNWWD4M1PvwpQdqTlLEefJwEDyJ6Qv9AcsebG9+CEi4O6Dqk7M3InoIqKLmbuqTu4+RATg7kOtdQUQ/1xrXd19gKpOACKldIrIYmajmY0isqSUTgChqhOYuQMIEVkiAr8WkQVAMHMHEV0AwszG59DMRgBBRBfhpSjnfADAtm2fZ3lnOefj/ZnXeG7grbW5lLLfwEspe2ttvoF/Adkyog2mm1YnAAAAAElFTkSuQmCC) Initialize connection manager

## Connection Establishment

BotSocketConnectionManager.getInstance().startAndInitiateConnectionWithReconnect()

1.  **Post-Connection Actions** When connection is successful:

Update connection state Reset reconnection flags Fetch branding details

Register for push notifications Update UI elements

## Message Handling

Process incoming messages Handle message updates Manage delivery receipts

# Event Handling

1.  Message Events

public void onMessage(BotResponse botResponse) { processPayload("", botResponse);

}

1.  Connection State Changes

public void onConnectionStateChanged(CONNECTION\_STATE state, boolean isReconnection) { if (state == CONNECTION\_STATE.CONNECTED) {

// Handle successful connection

} else if (state == CONNECTION\_STATE.RECONNECTION\_STOPPED) {

// Handle reconnection failure

}

}

1.  Data Transfer Events

public void onMessage(SocketDataTransferModel data) {

// Handle different event types

if (data.getEvent\_type().equals(TYPE\_TEXT\_MESSAGE)) { processPayload(data.getPayLoad(), null);

} else if (data.getEvent\_type().equals(TYPE\_MESSAGE\_UPDATE)) { chatView.updateContentListOnSend(data.getBotRequest());

}

}

# Additional Features

1.  Push Notification Registration

new PushNotificationRegister().registerPushNotification( botClient.getUserId(),

botClient.getAccessToken(), getUniqueDeviceId(context)

);

1.  Branding Integration

getBrandingDetails( SDKConfiguration.Client.bot\_id,

SocketWrapper.getInstance(context).getAccessToken(), "published",

"1",

"en\_US"

);

1.  Message Acknowledgment

if (botClient != null && enable\_ack\_delivery) { botClient.sendMsgAcknowledgement(timestamp, key);

}

# Error Handling

## Reconnection Management

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAArklEQVQYlY2QMQ6FMAxDTYYO3KfNWWD4M1PvwpQdqTlLEefJwEDyJ6Qv9AcsebG9+CEi4O6Dqk7M3InoIqKLmbuqTu4+RATg7kOtdQUQ/1xrXd19gKpOACKldIrIYmajmY0isqSUTgChqhOYuQMIEVkiAr8WkQVAMHMHEV0AwszG59DMRgBBRBfhpSjnfADAtm2fZ3lnOefj/ZnXeG7grbW5lLLfwEspe2ttvoF/Adkyog2mm1YnAAAAAElFTkSuQmCC) Tracks reconnection attempts

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAuUlEQVQYlY2QMW7DQAwEVyyu0H90fIsMJLWq+4sr9hKOb5GT97BwIW6aCDAEF15gm8U0OyCJzBzcfVbVXUQOETlUdXf3OTMHkkBmDq21OwC+a2vtnpkD3H0GwFLK08yWiBgjYjSzpZTyBEB3n6GqOwCa2UISrzWzBQBVdYeIHAAYEeMVjIgRAEXkEPyH5IBLXjeptf4AwLqu31dw27YvAJim6ffzMx/rOYX33m+11scpvNb66L3fTuF/LumoDaRVc3wAAAAASUVORK5CYII=) Notifies UI when reconnection stops ![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAtklEQVQYlY2QIQ7DQAwE9wwO5EH2V5qA4qD7S5F5pHO+kuY/BgWxSxqpigq60pLVkB1kJiKimNkoIhsRHUR0iMhmZmNElMwEIqK01h4A8ldba4+IKDCzEUDWWl+qOrv74O6Dqs611heANLMRIrIBSFWdMxPfVdUZQIrIBiI6AKS7D1fQ3QcASUQH4ZPMLLjkeyNm3gFgWZb7FTw3Zt7/OrOu6+1/Pafw3vvEzM9TODM/e+/TKfwNL4WoDw8xwbQAAAAASUVORK5CYII=) Manages reconnection state

## Message Processing Errors

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAuUlEQVQYlY2QMWrEQBAEWxNsoPdIO2/RgR0r2r9cNLnEzlt02N+Z4AJNOxKYw5hraOigki6QRGYO7r6o6iEip4icqnq4+5KZA0kgM4fW2h0A/2pr7Z6ZA9x9AcBSytPM1ogYI2I0s7WU8gRAd1+gqgcAmtlKEr9rZisAquoBETkBMCLGVzAiRgAUkVPwT0gO15Zpmr4BYNu2z1dw3/cPAJjn+ev9M2/ruYT33m+11sclvNb66L3fLuE/5LilCwyF3bgAAAAASUVORK5CYII=) Handles JSON parsing errors

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAtklEQVQYlY2QIQ7DQAwE9wwO5EH2V5qA4qD7S5F5pHO+kuY/BgWxSxqpigq60pLVkB1kJiKimNkoIhsRHUR0iMhmZmNElMwEIqK01h4A8ldba4+IKDCzEUDWWl+qOrv74O6Dqs611heANLMRIrIBSFWdMxPfVdUZQIrIBiI6AKS7D1fQ3QcASUQH4ZPMLLjkeyNm3gFgWZb7FTw3Zt7/OrOu6+1/Pafw3vvEzM9TODM/e+/TKfwNL4WoDw8xwbQAAAAASUVORK5CYII=) Manages different message formats ![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAtklEQVQYlY2QIQ7DQAwE9wwO5EH2V5qA4qD7S5F5pHO+kuY/BgWxSxqpigq60pLVkB1kJiKimNkoIhsRHUR0iMhmZmNElMwEIqK01h4A8ldba4+IKDCzEUDWWl+qOrv74O6Dqs611heANLMRIrIBSFWdMxPfVdUZQIrIBiI6AKS7D1fQ3QcASUQH4ZPMLLjkeyNm3gFgWZb7FTw3Zt7/OrOu6+1/Pafw3vvEzM9TODM/e+/TKfwNL4WoDw8xwbQAAAAASUVORK5CYII=) Provides fallback mechanisms

# Best Practices

## Connection Management

Always check connection state before sending messages

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAxUlEQVQYlY2QMW7DQAwE91hcoQeRX4lcpFZz9xe7YS/g6K/I/g8LFyLTRIAhOEAW2GYxzQ4yExFRzGwWkY2IdiLaRWQzszkiSmYCEVFaazcA+amttVtEFJjZDCBrrS9VXdx9cvdJVZda6wtAmtkMEdkApKoumYn3quoCIEVkAxHtANLdpzPo7hOAJKKd8JvMLDjlfSNmfgLAuq7fZ/DYmPn5rzP3+/0LEVF679e/9PTerxFRcAgfY1yY+XEIZ+bHGONyCP8BKg2oC66NSX8AAAAASUVORK5CYII=) Implement proper reconnection logic ![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAxUlEQVQYlY2QMW7DQAwE91hcoQeRX4lcpFZz9xe7YS/g6K/I/g8LFyLTRIAhOEAW2GYxzQ4yExFRzGwWkY2IdiLaRWQzszkiSmYCEVFaazcA+amttVtEFJjZDCBrrS9VXdx9cvdJVZda6wtAmtkMEdkApKoumYn3quoCIEVkAxHtANLdpzPo7hOAJKKd8JvMLDjlfSNmfgLAuq7fZ/DYmPn5rzP3+/0LEVF679e/9PTerxFRcAgfY1yY+XEIZ+bHGONyCP8BKg2oC66NSX8AAAAASUVORK5CYII=) Handle timeouts appropriately

## Message Handling

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAxUlEQVQYlY2QMW7DQAwE91hcoQeRX4lcpFZz9xe7YS/g6K/I/g8LFyLTRIAhOEAW2GYxzQ4yExFRzGwWkY2IdiLaRWQzszkiSmYCEVFaazcA+amttVtEFJjZDCBrrS9VXdx9cvdJVZda6wtAmtkMEdkApKoumYn3quoCIEVkAxHtANLdpzPo7hOAJKKd8JvMLDjlfSNmfgLAuq7fZ/DYmPn5rzP3+/0LEVF679e/9PTerxFRcAgfY1yY+XEIZ+bHGONyCP8BKg2oC66NSX8AAAAASUVORK5CYII=) Validate message format before processing

![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAxUlEQVQYlY2QMW7DQAwE91hcoQeRX4lcpFZz9xe7YS/g6K/I/g8LFyLTRIAhOEAW2GYxzQ4yExFRzGwWkY2IdiLaRWQzszkiSmYCEVFaazcA+amttVtEFJjZDCBrrS9VXdx9cvdJVZda6wtAmtkMEdkApKoumYn3quoCIEVkAxHtANLdpzPo7hOAJKKd8JvMLDjlfSNmfgLAuq7fZ/DYmPn5rzP3+/0LEVF679e/9PTerxFRcAgfY1yY+XEIZ+bHGONyCP8BKg2oC66NSX8AAAAASUVORK5CYII=) Handle different message types appropriately ![](data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAA7EAAAOxAGVKw4bAAAAxUlEQVQYlY2QMW7DQAwE91hcoQeRX4lcpFZz9xe7YS/g6K/I/g8LFyLTRIAhOEAW2GYxzQ4yExFRzGwWkY2IdiLaRWQzszkiSmYCEVFaazcA+amttVtEFJjZDCBrrS9VXdx9cvdJVZda6wtAmtkMEdkApKoumYn3quoCIEVkAxHtANLdpzPo7hOAJKKd8JvMLDjlfSNmfgLAuq7fZ/DYmPn5rzP3+/0LEVF679e/9PTerxFRcAgfY1yY+XEIZ+bHGONyCP8BKg2oC66NSX8AAAAASUVORK5CYII=) Implement proper error handling

## State Management

Track connection states Manage reconnection attempts

Handle UI updates based on connection state

Usage Example

// Initialize BotChatViewModel

BotChatViewModel viewModel = new BotChatViewModel(context, botClient, chatView);

// Connect to bot

viewModel.connectToBot(false); // false for initial connection

// Handle reconnection

viewModel.connectToBot(true); // true for reconnection attempt