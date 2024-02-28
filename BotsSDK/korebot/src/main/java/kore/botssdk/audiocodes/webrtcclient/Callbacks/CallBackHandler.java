package kore.botssdk.audiocodes.webrtcclient.Callbacks;



import com.audiocodes.mv.webrtcsdk.im.InstanceMessageStatus;
import com.audiocodes.mv.webrtcsdk.session.CallState;

import java.util.ArrayList;
import java.util.List;

public class CallBackHandler {
    private static List<LoginStateChanged> loginStateChangeCallBacks = new ArrayList<>();
    private static List<CallStateChanged> callStateChangeCallBacks = new ArrayList<>();
    private static List<ChatCallback> chatCallBacks = new ArrayList<>();
    private static List<TabChangeCallback> tabChangeCallbacks = new ArrayList<>();

    //LoginStateChanged
    public interface LoginStateChanged {
        public void loginStateChange(boolean state);
    }

    public static void loginStateChange(boolean state) {
        if (loginStateChangeCallBacks != null) {
            for (int i = 0; i < loginStateChangeCallBacks.size(); ++i) {
                loginStateChangeCallBacks.get(i).loginStateChange(state);
            }
        }
    }

    public static void unregisterLoginStateChange(LoginStateChanged cb) {
        loginStateChangeCallBacks.remove(cb);
    }

    public static void registerLginStateChange(LoginStateChanged cb) {
        loginStateChangeCallBacks.add(cb);
    }

    //CallStateChanged
    public interface CallStateChanged {
        public void callStateChanged(CallState callState);
    }

    public static void callStateChanged(CallState callState) {
        if (callStateChangeCallBacks != null) {
            for (int i = 0; i < callStateChangeCallBacks.size(); ++i) {
                callStateChangeCallBacks.get(i).callStateChanged(callState);
            }
        }
    }

    public static void unregisterCallStateChanged(CallStateChanged cb) {
        callStateChangeCallBacks.remove(cb);
    }

    public static void registerCallStateChanged(CallStateChanged cb) {
        callStateChangeCallBacks.clear();
        callStateChangeCallBacks.add(cb);
    }

    //ChatCallback
    public interface ChatCallback {
        void onNewMessage(String user, String message);
        void onMessageStatus(InstanceMessageStatus instanceMessageStatus, long ID);
    }

    public static void onNewMessage(String user, String message) {
        if (chatCallBacks != null) {
            for (int i = 0; i < chatCallBacks.size(); ++i) {
                chatCallBacks.get(i).onNewMessage(user, message);
            }
        }
    }

    public static void onMessageStatus(InstanceMessageStatus instanceMessageStatus, long ID) {
        if (chatCallBacks != null) {
            for (int i = 0; i < chatCallBacks.size(); ++i) {
                chatCallBacks.get(i).onMessageStatus(instanceMessageStatus, ID);
            }
        }
    }

    public static void unregisterChatCallback(ChatCallback chatCallback) {
        chatCallBacks.remove(chatCallback);
    }

    public static void registerChatCallback(ChatCallback chatCallback) {
        chatCallBacks.clear();
        chatCallBacks.add(chatCallback);
    }

    //TabChangeCallback
    public interface TabChangeCallback {
        void onTabChange(int tabIndex);
    }

    public static void onTabChange(int tabIndex) {
        if (tabChangeCallbacks != null) {
            for (int i = 0; i < tabChangeCallbacks.size(); ++i) {
                tabChangeCallbacks.get(i).onTabChange(tabIndex);
            }
        }
    }

    public static void unregisterTabChangeCallback(TabChangeCallback tabChangeCallback) {
        chatCallBacks.remove(tabChangeCallback);
    }

    public static void registerTabChangeCallback(TabChangeCallback tabChangeCallback) {
        tabChangeCallbacks.clear();
        tabChangeCallbacks.add(tabChangeCallback);
    }
}
