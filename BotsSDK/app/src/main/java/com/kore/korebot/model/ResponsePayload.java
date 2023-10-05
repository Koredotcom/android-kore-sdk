package com.kore.korebot.model;

import android.annotation.SuppressLint;

@SuppressLint("UnknownNullness")
public class ResponsePayload {
    private boolean download;
    private String fileName;
    private String url;

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public boolean isDownload() {
        return download;
    }
}
