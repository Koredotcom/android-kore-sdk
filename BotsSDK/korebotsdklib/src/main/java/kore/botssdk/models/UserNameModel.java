package kore.botssdk.models;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import kore.botssdk.utils.Utils;

public class UserNameModel implements Serializable {
    public String getfN() {
        return fN;
    }
    private String _id;

    public void setfN(String fN) {
        this.fN = fN;
    }
    public String getName(){
        return fN+" "+lN;
    }
    public String getlN() {
        return lN;
    }
    public String getInitials(){
        return kore.botssdk.utils.StringUtils.getInitials(fN,lN);
    }
    public void setlN(String lN) {
        this.lN = lN;
    }

    private String fN;
    private String lN;

    public String getNameInFirstNameFormat(){
        if(!Utils.isNullOrEmpty(fN) && !Utils.isNullOrEmpty(lN)){

            return StringUtils.capitalize( fN+ " "+ lN.charAt(0));
        }else if(!Utils.isNullOrEmpty(fN)){
            return StringUtils.capitalize(fN);
        }else{
            return StringUtils.capitalize(lN);
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}