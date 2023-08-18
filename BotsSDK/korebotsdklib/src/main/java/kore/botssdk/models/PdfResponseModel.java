package kore.botssdk.models;

public class PdfResponseModel {
    private String status;
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
