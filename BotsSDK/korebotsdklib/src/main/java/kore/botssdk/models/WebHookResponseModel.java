package kore.botssdk.models;

public class WebHookResponseModel
{
    private String type;
    private String val;
    private String createdOn;
    private String _v;
    private boolean endOfTask;

    public void set_v(String _v) {
        this._v = _v;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setEndOfTask(boolean endOfTask) {
        this.endOfTask = endOfTask;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String get_v() {
        return _v;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getType() {
        return type;
    }

    public String getVal() {
        return val;
    }

    public boolean getEndOfTask(){
        return endOfTask;
    }
}
