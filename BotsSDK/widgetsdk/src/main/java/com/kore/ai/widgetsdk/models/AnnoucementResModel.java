package com.kore.ai.widgetsdk.models;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AnnoucementResModel implements Serializable {

    @SerializedName("creator")
    @Expose
    private String creator;
    @SerializedName("nViews")
    @Expose
    private Long nViews;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("nShares")
    @Expose
    private Long nShares;
    @SerializedName("nLikes")
    @Expose
    private Long nLikes;
    @SerializedName("nComments")
    @Expose
    private Long nComments;
    @SerializedName("myPrivilege")
    @Expose
    private Long myPrivilege;
    @SerializedName("myActions")
    @Expose
    private MyActions myActions;
    @SerializedName("createdOn")
    @Expose
    private Long createdOn;

    @SerializedName("sharedOn")
    @Expose
    private Long sharedOn;

    @SerializedName("lastMod")
    @Expose
    private Long lastMod;



    public Long getNUpVotes() {
        return nUpVotes;
    }

    public void setnUpVotes(Long nUpVotes) {
        this.nUpVotes = nUpVotes;
    }

    @SerializedName("nUpVotes")
    @Expose
    private Long nUpVotes;


    @SerializedName("sharedList")
    @Expose
    private List<SharedList> sharedList = null;

    public List<SharedList> getSharedList() {
        return sharedList;
    }

    public void setSharedList(List<SharedList> sharedList) {
        this.sharedList = sharedList;
    }
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getNViews() {
        return nViews;
    }

    public void setNViews(Long nViews) {
        this.nViews = nViews;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public int getTitleVisiblity() {
        if (title != null && !TextUtils.isEmpty(title.trim())) {
            return View.VISIBLE;
        }
        return View.GONE;
    }

    public String getSharedUserList() {
        String users = "";
        if (getSharedList() != null && getSharedList().size() > 0) {
            if (getSharedList().size() == 1) {

                return  getSharedList().get(0).getName()  ;
            } else {
                int remaining = getSharedList().size() - 1;
                if(remaining > 1)
                    return String.format("%1$s and %2$d others",getSharedList().get(0).getName() , remaining);
                else
                    return String.format("%1$s and %2$d other",getSharedList().get(0).getName() , remaining);

            }
        }


        return users;

    }
   public int getSharedVisiblity()
    {
        if (getSharedList() != null && getSharedList().size()>0) {
            return View.VISIBLE;
        }
        return View.GONE;
    }
    public int getDescriptionVisiblity() {
        if (desc != null && !TextUtils.isEmpty(desc.replaceAll("(^\\h*)|(\\h*$)",""))) {
            return View.VISIBLE;
        }
        return View.GONE;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Long getNShares() {
        return nShares;
    }

    public void setNShares(Long nShares) {
        this.nShares = nShares;
    }

    public Long getNLikes() {
        return nLikes;
    }

    public void setNLikes(Long nLikes) {
        this.nLikes = nLikes;
    }

    public Long getNComments() {
        return nComments;
    }

    public void setNComments(Long nComments) {
        this.nComments = nComments;
    }

    public Long getMyPrivilege() {
        return myPrivilege;
    }

    public void setMyPrivilege(Long myPrivilege) {
        this.myPrivilege = myPrivilege;
    }

    public MyActions getMyActions() {
        return myActions;
    }

    public void setMyActions(MyActions myActions) {
        this.myActions = myActions;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getLastMod() {
        return lastMod;
    }

    public void setLastMod(Long lastMod) {
        this.lastMod = lastMod;
    }

    public Long getSharedOn() {
        return sharedOn;
    }

    public void setSharedOn(Long sharedOn) {
        this.sharedOn = sharedOn;
    }
    public static class MyActions implements Serializable {

        @SerializedName("follow")
        @Expose
        private boolean follow;
        @SerializedName("privilege")
        @Expose
        private long privilege;

        public boolean getFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public long getPrivilege() {
            return privilege;
        }

        public void setPrivilege(long privilege) {
            this.privilege = privilege;
        }

    }
    public static class SharedList implements Serializable{

        @SerializedName("lN")
        @Expose
        private String lN;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("id")
        @Expose
        private String id;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        @SerializedName("_id")
        @Expose
        private String _id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("fN")
        @Expose
        private String fN;

        public String getLN() {
            return lN;
        }

        public void setLN(String lN) {
            this.lN = lN;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
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

        public void setFN(String fN) {
            this.fN = fN;
        }

    }

    public static class Owner implements Serializable {

        @SerializedName("lN")
        @Expose
        private String lN;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("fN")
        @Expose
        private String fN;
        @SerializedName("emailId")
        @Expose
        private String emailId;
        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("icon")
        @Expose
        private String icon;



        public String getLN() {
            return lN;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public void setLN(String lN) {
            this.lN = lN;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getFN() {
            return fN;
        }

        public void setFN(String fN) {
            this.fN = fN;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFullName() {
            return fN + " " + lN;
        }

    }


}