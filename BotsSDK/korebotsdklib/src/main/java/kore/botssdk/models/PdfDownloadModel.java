package kore.botssdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class PdfDownloadModel
{
    private String title;
    private String url;
    private String type;
    private HashMap<String, Object> body;
    private HashMap<String, String> header;
    private String format;
    private int pdfType;

    public int getPdfType() {
        return pdfType;
    }

    public void setPdfType(int pdfType) {
        this.pdfType = pdfType;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, String> getHeader() {
        return header;
    }

    public void setHeader(HashMap<String, String> header) {
        this.header = header;
    }

    public HashMap<String, Object> getBody() {
        return body;
    }

    public void setBody(HashMap<String, Object> body) {
        this.body = body;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String pdfType) {
        this.format = pdfType;
    }

    public class Header
    {
        private String Authorization;

        @SerializedName("X-Auth-Token")
        @Expose
        private String xAuthToken;

        @SerializedName("X-MOB-CHANNEL-NAME")
        @Expose
        private String xMobileChannel;

        @SerializedName("X-CORRELATION-ID")
        @Expose
        private String xCorrelation;

        public String getAuthorization() {
            return Authorization;
        }

        public void setAuthorization(String authorization) {
            Authorization = authorization;
        }

        public String getxAuthToken() {
            return xAuthToken;
        }

        public void setxAuthToken(String xAuthToken) {
            this.xAuthToken = xAuthToken;
        }

        public String getxCorrelation() {
            return xCorrelation;
        }

        public void setxCorrelation(String xCorrelation) {
            this.xCorrelation = xCorrelation;
        }

        public void setxMobileChannel(String xMobileChannel) {
            this.xMobileChannel = xMobileChannel;
        }

        public String getxMobileChannel() {
            return xMobileChannel;
        }
    }

}
