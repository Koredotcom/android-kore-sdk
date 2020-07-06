package com.kore.ai.widgetsdk.models;

import com.kore.ai.widgetsdk.utils.DateUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Ramachandra Pradeep on 09-Aug-18.
 */

public class KaFileLookupModel {
    private static DecimalFormat df2 = new DecimalFormat("###.##");
    private String fileName;
    private String fileId;
    private String sharedBy;
    private String iconLink;
    private String thumbnailLink;
    private String lastModified;
    private long fileSize;

    public String getWebViewLink() {
        return webViewLink;
    }

    public void setWebViewLink(String webViewLink) {
        this.webViewLink = webViewLink;
    }

    private String webViewLink;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    private String fileType;

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }


    private ArrayList<BotCaourselButtonModel> buttons;

    public ArrayList<BotCaourselButtonModel> getButtons() {
        return buttons;
    }

    public void setButtons(ArrayList<BotCaourselButtonModel> buttons) {
        this.buttons = buttons;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getFileSizeMegaBytes() {
        if(fileSize < (1024*1024)){
            return df2.format((double) fileSize / (1024)) + "KB";
        }else	return df2.format((double)fileSize / (1024 * 1024)) + " MB";
    }

    public String getSharedByDetails(){
        return "Shared by "+ sharedBy;
    }
    public String getLastModifiedDate(){
        return "Last Edited "+ DateUtils.getDateFromString(lastModified);
    }
}
