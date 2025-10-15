package kore.botssdk.models;

import com.google.gson.annotations.SerializedName;

public class FormDataModel {
    @SerializedName("formLink")
    private String formLink;
    @SerializedName("renderType")
    private String renderType;
    @SerializedName("name")
    private String name;
    @SerializedName("displayName")
    private String displayName;
    @SerializedName("isSecured")
    private boolean isSecured;
    @SerializedName("formLinkExpiry")
    private int formLinkExpiry;
    @SerializedName("submitExpiry")
    private int submitExpiry;

    public String getFormLink() {
        return formLink;
    }

    public String getRenderType() {
        return renderType;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isSecured() {
        return isSecured;
    }

    public int getFormLinkExpiry() {
        return formLinkExpiry;
    }

    public int getSubmitExpiry() {
        return submitExpiry;
    }
}
