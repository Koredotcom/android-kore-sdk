package kore.botssdk.fileupload.core;


import kore.botssdk.fileupload.listeners.FileUploadedListener;

public interface Work {
    
    void initiateFileUpload(FileUploadedListener file);
}  
