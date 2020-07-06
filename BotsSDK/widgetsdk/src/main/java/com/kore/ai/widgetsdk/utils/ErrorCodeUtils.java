package com.kore.ai.widgetsdk.utils;

import java.util.HashMap;

/**
 * Created by Shiva Krishna on 5/31/2018.
 */

public class ErrorCodeUtils {
    private static HashMap<String,String> errorCodeMessageMap = new HashMap<>();

    private static void loadErrorCodes(){
        errorCodeMessageMap.put( "QuestionAlreadyExist", "Question already exists");
        errorCodeMessageMap.put( "InvalidQuestion", "The trigger question does not contain enough unique words after the filler words are ignored. Try re-writing the question in a more specific or unique way.");
    }

    public static String getErrorMessageByCode(String code){
        if(errorCodeMessageMap.size() == 0){
            loadErrorCodes();
        }
        return errorCodeMessageMap.get(code);
    }
}
