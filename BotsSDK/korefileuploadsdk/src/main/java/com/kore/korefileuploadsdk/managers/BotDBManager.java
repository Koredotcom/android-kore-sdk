package com.kore.korefileuploadsdk.managers;

import com.kore.korefileuploadsdk.models.ChunkInfo;
import com.kore.korefileuploadsdk.models.FileUploadInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ramachandra Pradeep on 02-Apr-18.
 */

public class BotDBManager {

    private volatile static BotDBManager botDBManager;

    private Map<String, FileUploadInfo> fileUploadInfoMap = new HashMap<>();


    private Map<String, Map<Integer, ChunkInfo>> chunkInfoMap = new HashMap<>();

    private BotDBManager(){}

    public static BotDBManager getInstance(){
        if(botDBManager == null){
            synchronized (BotDBManager.class){
                if(botDBManager == null)
                    botDBManager = new BotDBManager();
            }
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

    public synchronized void  setFileUploadInfoMap(HashMap<String, FileUploadInfo> fileUploadInfoMap) {
        this.fileUploadInfoMap = fileUploadInfoMap;
    }

    public Map<String, Map<Integer, ChunkInfo>> getChunkInfoMap() {
        return chunkInfoMap;
    }

    public synchronized void setChunkInfoMap(Map<String, Map<Integer, ChunkInfo>> chunkInfoMap) {
        this.chunkInfoMap = chunkInfoMap;
    }
}
