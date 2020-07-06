package com.kore.ai.widgetsdk.models;

import com.kore.ai.widgetsdk.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by Shiva Krishna on 2/26/2018.
 */

public class ContactsTeamsHolder implements Serializable {
    private static final long serialVersionUID = 6837271458428376093L;
    String id;
    private String fN;
    private String lN;
    private String displayNameInitials;
    private String emailId;
    private String displayName;
    private String atMention;
    private String label;
    private String role;
    private String color;
    private String phoneNo;
    private String name;
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAtMention() {
        return atMention;
    }

    public void setAtMention(String atMention) {
        this.atMention = atMention;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getDisplayName() {
        if (StringUtils.isNullOrEmptyWithTrim(displayName)) {
            displayName = isTeam() ? getTeamName() : fN + " " + lN;
        }
        return displayName != null ? displayName.trim() : "";
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }


    public String getPfColor() {
        return color;
    }

    public void setPfColor(String color) {
        this.color = color;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getFirstName() {
        return fN;
    }

    public void setFirstName(String fN) {
        this.fN = fN;
    }

    public String getLastName() {
        return lN;
    }

    public void setLastName(String lN) {
        this.lN = lN;
    }

    public String getDisplayNameInitials() {
        if (StringUtils.isNullOrEmptyWithTrim(displayNameInitials)) {
            displayNameInitials = StringUtils.getInitials(isTeam() ? getTeamName() : getDisplayName());
        }
        return displayNameInitials;
    }

    public void setDisplayNameInitials(String displayNameInitials) {
        this.displayNameInitials = displayNameInitials;
    }

    public String getEmailId() {

        return emailId != null ? emailId.trim() : "";
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getTeamName() {
        return name;
    }

    public void setTeamName(String name) {
        this.name = name;
    }


    public boolean isTeam() {

        return "team".equalsIgnoreCase(label);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof ContactsTeamsHolder) {
            if (this.getId() != null && ((ContactsTeamsHolder) obj).getId() != null) {
                return this.getId().equals(((ContactsTeamsHolder) obj).getId());
            } else if (this.getEmailId() != null && ((ContactsTeamsHolder) obj).getEmailId() != null) {
                return this.getEmailId().equals(((ContactsTeamsHolder) obj).getEmailId());
            } else if (this.getPhoneNo() != null && ((ContactsTeamsHolder) obj).getPhoneNo() != null) {
                return this.getPhoneNo().equals(((ContactsTeamsHolder) obj).getPhoneNo());
            }
        }
        return false;
    }

    /**
     * to avoid same hash code generation if any of fields are equal like in one object email and other object kore id are same and remaining or null
     * for that we are multiplying with different numbers
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = emailId == null ? 0 : 31 * emailId.hashCode();
        result = 32 * result + (phoneNo == null ? 0 : phoneNo.hashCode());
        result = 33 * result + (id == null ? 0 : id.hashCode());
        return result;
    }


}
