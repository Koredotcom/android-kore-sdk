package com.kore.ai.widgetsdk.models;

import com.kore.ai.widgetsdk.utils.Utils;

/**
 * Created by Ramachandra Pradeep on 01-Apr-19.
 */

public class TaskOwner {

    public String getlN() {
        return lN;
    }

    public void setlN(String lN) {
        this.lN = lN;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFN() {
        return fN;
    }


    public String getNameInFirstNameFormat(){
//        if(!Utils.isNullOrEmpty(fN) && !Utils.isNullOrEmpty(lN)){
//
//            return StringUtils.capitalize( fN+ " "+ lN.charAt(0));
//        }else if(!Utils.isNullOrEmpty(fN)){
//            return StringUtils.capitalize(fN);
//        }else{
            return lN;
//        }
    }

    public void setFN(String fN) {
        this.fN = fN;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String lN;
    private String label;
    private String id;
    private String name;
    private String fN;
    private String _id;


}
