package kore.botssdk.models;

import org.apache.commons.lang3.StringUtils;

import kore.botssdk.utils.Utils;

public class UserNameModel {
    public String getfN() {
        return fN;
    }

    public void setfN(String fN) {
        this.fN = fN;
    }

    public String getlN() {
        return lN;
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
}