package kore.botssdk.fileupload.configurations;

/**
 * Created by Ramachandra Pradeep on 15-Feb-18.
 */

public class Constants {
    public static final int UPLOAD_ERROR_CODE_404 = 404;
    public static final int UPLOAD_ERROR_CODE_504 = 504;

    public static final int MERGE_RE_ATTEMPT_COUNT = 3;
    public static final int CONNECTION_TIMEOUT = 2 * 60000;
    public static final int CONNECTION_READ_TIMEOUT = 2 * 60000;


    public static final String SERVER_ERROR = "ERROR WHILE CONATCTING WITH SERVER";
    public static final String NETWORK_FAILURE = "NOT_CONNECTED";

    public static final String MESSAGE_ID = "messageId";
    public static final String TEAM_ID = "teamId";
    public static final String LOCAL_FILE_ID = "EMPTY_FILE_ID";

    public static String HTTP_AGENT = null;
    public static String getUserAgent(){
        if(HTTP_AGENT == null){
            HTTP_AGENT = System.getProperty("http.agent");
        }
        return HTTP_AGENT;
    }
}
