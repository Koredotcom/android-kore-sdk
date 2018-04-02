package com.kore.korefileuploadsdk.core;


import com.kore.korefileuploadsdk.listeners.FileUploadedListener;

public interface Work {
    
    void initiateFileUpload(FileUploadedListener file);
}  
