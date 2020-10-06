package com.kore.korefileuploadsdk.configurations;

/**
 * Created by Ramachandra Pradeep on 15-Feb-18.
 */

public class FileUploadEndPoints {
    public static String END_POINT = "/api/1.1/users/%s/file/token";
    public static String TEAMS_END_POINT = "/api/1.1/teams/%s/file/token";

    public static String CHUNK_UPLOAD_END_POINT = "/api/1.1/users/%s/file/%s/chunk";
    public static String TEAM_CHUNK_UPLOAD_END_POINT = "/api/1.1/teams/%s/file/%s/chunk";

    public static String MERGE_END_POINT = "/api/1.1/users/%s/file/%s";
    public static String TEAM_MERGE_END_POINT = "/api/1.1/teams/%s/file/%s";



    public static String END_POINT_TOKEN = "/api/1.1/attachment/file/token";
    public static String COMPOSEBAR_CHUNK_UPLOAD_END_POINT = "/api/1.1/attachment/file/%s/chunk";
    public static String COMPOSEBAR_MERGE_END_POINT = "/api/1.1/attachment/file/%s";
}
