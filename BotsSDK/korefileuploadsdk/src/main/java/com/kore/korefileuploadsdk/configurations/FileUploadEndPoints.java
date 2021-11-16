package com.kore.korefileuploadsdk.configurations;

/**
 * Created by Ramachandra Pradeep on 15-Feb-18.
 */

public class FileUploadEndPoints {

    /**
     * End points for logged in users
     */
    public static String FILE_TOKEN = "/api/1.1/users/%s/file/token";
//    public static String TEAMS_END_POINT = "/api/1.1/teams/%s/file/token";

    public static String CHUNK_UPLOAD = "/api/1.1/users/%s/file/%s/chunk";
//    public static String TEAM_CHUNK_UPLOAD_END_POINT = "/api/1.1/teams/%s/file/%s/chunk";

    public static String MERGE_FILE = "/api/1.1/users/%s/file/%s";
//    public static String TEAM_MERGE_END_POINT = "/api/1.1/teams/%s/file/%s";

    /**
     * End points for anonymous user
     */
    public static String ANONYMOUS_FILE_TOKEN = "/api/1.1/attachment/file/token";

    public static String ANONYMOUS_CHUNK_UPLOAD = "/api/1.1/users/%s/file/%s/chunk";

    public static String ANONYMOUS_MERGE = "/api/1.1/users/%s/file/%s";

    /**
     * End points for anonymous user webHook
     */
    public static String WEBHOOK_ANONYMOUS_FILE_TOKEN = "api/attachments/%s/%s/token";
    
    public static String WEBHOOK_ANONYMOUS_CHUNK_UPLOAD = "api/attachments/%s/%s/token/%s/chunk";

    public static String WEBHOOK_ANONYMOUS_MERGE = "api/attachments/%s/ivr/token/%s";

}
