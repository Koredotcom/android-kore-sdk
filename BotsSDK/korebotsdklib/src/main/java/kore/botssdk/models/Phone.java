package kore.botssdk.models;

import java.util.List;

public class Phone {
    private String name;
    private List<PhoneItem> phArray;

    public Phone(String name, List phArray){
        this.name = name;
        this.phArray = phArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhoneItem> getPhArray() {
        return phArray;
    }

    public void setPhArray(List<PhoneItem> phArray) {
        this.phArray = phArray;
    }
}
