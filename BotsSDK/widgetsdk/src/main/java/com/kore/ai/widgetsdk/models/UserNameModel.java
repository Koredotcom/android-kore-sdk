package com.kore.ai.widgetsdk.models;

import com.kore.ai.widgetsdk.utils.StringUtils;
import com.kore.ai.widgetsdk.utils.Utils;

import java.io.Serializable;

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
        return com.kore.ai.widgetsdk.utils.StringUtils.getInitials(fN,lN);
    }
    public void setlN(String lN) {
        this.lN = lN;
    }

    private String fN;
    private String lN;

    public String getNameInFirstNameFormat(){
//        if(!Utils.isNullOrEmpty(fN) && !Utils.isNullOrEmpty(lN)){
//
//            return StringUtils.capitalize( fN+ " "+ lN.charAt(0));
//        }else if(!Utils.isNullOrEmpty(fN)){
//            return StringUtils.capitalize(fN);
//        }else{
            return "";
//        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}