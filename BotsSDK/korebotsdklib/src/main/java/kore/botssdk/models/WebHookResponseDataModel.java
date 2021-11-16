package kore.botssdk.models;

import java.util.ArrayList;

public class WebHookResponseDataModel
{
    private ArrayList<WebHookResponseModel> data;
    private String _v;
    private String pollId;

    public void set_v(String _v) {
        this._v = _v;
    }

    public String get_v() {
        return _v;
    }

    public void setData(ArrayList<WebHookResponseModel> data) {
        this.data = data;
    }

    public ArrayList<WebHookResponseModel> getData() {
        return data;
    }

    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }
}
