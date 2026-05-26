package kore.botssdk.voicemode.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Service for fetching JWT tokens and user IDs from the authentication server.
 */
public class JWTService {

    private static final String TAG = "JWTService";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final Handler mainHandler;
    private final Gson gson;

    public JWTService() {
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.gson = new Gson();
    }

    /**
     * Fetch JWT token from the JWT service.
     * @param jwtServiceUrl The JWT service URL
     * @param clientId Client ID
     * @param clientSecret Client secret
     * @param identity User identity
     * @param callback Callback for result
     */
    public void fetchJwtToken(String jwtServiceUrl, String clientId, String clientSecret, 
                             String identity, JWTCallback callback) {
        
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("clientId", clientId);
        requestBody.addProperty("clientSecret", clientSecret);
        requestBody.addProperty("identity", identity);
        requestBody.addProperty("aud", "https://idproxy.kore.com/authorize");
        requestBody.addProperty("isAnonymous", false);

        RequestBody body = RequestBody.create(requestBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(jwtServiceUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "JWT fetch failed: " + e.getMessage());
                mainHandler.post(() -> callback.onError("JWT fetch failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    Log.e(TAG, "JWT fetch failed: " + response.code() + " - " + errorBody);
                    mainHandler.post(() -> callback.onError("JWT fetch failed: " + response.code() + " - " + errorBody));
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                    
                    String token = null;
                    if (json.has("jwt")) {
                        token = json.get("jwt").getAsString();
                    } else if (json.has("token")) {
                        token = json.get("token").getAsString();
                    } else if (json.has("accessToken")) {
                        token = json.get("accessToken").getAsString();
                    }

                    if (token == null || token.isEmpty()) {
                        mainHandler.post(() -> callback.onError("No token found in JWT response"));
                        return;
                    }

                    final String jwtToken = token;
                    mainHandler.post(() -> callback.onSuccess(jwtToken));
                } catch (Exception e) {
                    Log.e(TAG, "Failed to parse JWT response: " + e.getMessage());
                    mainHandler.post(() -> callback.onError("Failed to parse JWT response: " + e.getMessage()));
                }
            }
        });
    }

    /**
     * Fetch user ID from the JWT grant API.
     * @param serverUrl The server URL
     * @param jwtToken The JWT token
     * @param botId The bot ID
     * @param callback Callback for result
     */
    public void fetchUserId(String serverUrl, String jwtToken, String botId, UserIdCallback callback) {
        String apiUrl = serverUrl + "/api/oAuth/token/jwtgrant";

        JsonObject botInfo = new JsonObject();
        botInfo.addProperty("chatBot", "Bot");
        botInfo.addProperty("taskBotId", botId);

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("assertion", jwtToken);
        requestBody.add("botInfo", botInfo);
        requestBody.add("token", new JsonObject());

        RequestBody body = RequestBody.create(requestBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(apiUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "User ID fetch failed: " + e.getMessage());
                mainHandler.post(() -> callback.onError("User ID fetch failed: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                    Log.e(TAG, "User ID fetch failed: " + response.code() + " - " + errorBody);
                    mainHandler.post(() -> callback.onError("User ID fetch failed: " + response.code() + " - " + errorBody));
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                    
                    String userId = null;
                    
                    if (json.has("userInfo")) {
                        JsonObject userInfo = json.getAsJsonObject("userInfo");
                        if (userInfo.has("userId")) {
                            userId = userInfo.get("userId").getAsString();
                        }
                    }
                    if (userId == null && json.has("userId")) {
                        userId = json.get("userId").getAsString();
                    }
                    if (userId == null && json.has("user")) {
                        JsonObject user = json.getAsJsonObject("user");
                        if (user.has("id")) {
                            userId = user.get("id").getAsString();
                        }
                    }
                    if (userId == null && json.has("id")) {
                        userId = json.get("id").getAsString();
                    }

                    if (userId == null || userId.isEmpty()) {
                        mainHandler.post(() -> callback.onError("No userId found in response"));
                        return;
                    }

                    final String finalUserId = userId;
                    mainHandler.post(() -> callback.onSuccess(finalUserId));
                } catch (Exception e) {
                    Log.e(TAG, "Failed to parse user ID response: " + e.getMessage());
                    mainHandler.post(() -> callback.onError("Failed to parse user ID response: " + e.getMessage()));
                }
            }
        });
    }

    public interface JWTCallback {
        void onSuccess(String jwtToken);
        void onError(String error);
    }

    public interface UserIdCallback {
        void onSuccess(String userId);
        void onError(String error);
    }
}
