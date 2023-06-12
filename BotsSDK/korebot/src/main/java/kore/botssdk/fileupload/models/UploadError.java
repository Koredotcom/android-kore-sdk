package kore.botssdk.fileupload.models;

/**
 * Created by Ramachandra Pradeep on 16-Feb-18.
 */

public class UploadError {

    private String msg;
    private int code;

    /**
     * @return
     * The msg
     */
    public String getMsg() {
        return msg;
    }
    /**
     * @param msg
     * The msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * @return
     * The code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code
     * The code
     */
    public void setCode(int code) {
        this.code = code;
    }
}