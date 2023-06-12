package kore.botssdk.fileupload.managers;

import java.util.HashMap;
import java.util.Map;

import kore.botssdk.fileupload.models.ChunkInfo;
import kore.botssdk.fileupload.models.FileUploadInfo;

/**
 * Created by Ramachandra Pradeep on 02-Apr-18.
 */

public class BotDBManager {

    private volatile static BotDBManager botDBManager;

    private static final Map<String, FileUploadInfo> fileUploadInfoMap = new HashMap<>();


    private static final Map<String, Map<Integer, ChunkInfo>> chunkInfoMap = new HashMap<>();

    private BotDBManager(){}

    public static BotDBManager getInstance(){
        if(botDBManager == null){
//            synchronized (BotDBManager.class){
//                if(botDBManager == null)
                    botDBManager = new BotDBManager();
//            }
        }
        return botDBManager;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException("Clone not supported");
    }

    public Map<String, FileUploadInfo> getFileUploadInfoMap() {
        return fileUploadInfoMap;
    }


    public Map<String, Map<Integer, ChunkInfo>> getChunkInfoMap() {
        return chunkInfoMap;
    }

}
