
package kore.botssdk.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KnowledgeDetailModelResponseNew {

    @SerializedName("buttons")
    @Expose
    private List<Button> buttons = null;
    @SerializedName("preview_length")
    @Expose
    private Long previewLength;
    @SerializedName("hasMore")
    @Expose
    private Boolean hasMore;
    @SerializedName("elements")
    @Expose
    private List<KnowledgeDetailModel> elements = null;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public Long getPreviewLength() {
        return previewLength;
    }

    public void setPreviewLength(Long previewLength) {
        this.previewLength = previewLength;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<KnowledgeDetailModel> getElements() {
        return elements;
    }

    public void setElements(List<KnowledgeDetailModel> elements) {
        this.elements = elements;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public class Button {

        @SerializedName("template_type")
        @Expose
        private String templateType;
        @SerializedName("action_type")
        @Expose
        private String actionType;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("hasMore")
        @Expose
        private Boolean hasMore;

        public String getTemplateType() {
            return templateType;
        }

        public void setTemplateType(String templateType) {
            this.templateType = templateType;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Boolean getHasMore() {
            return hasMore;
        }

        public void setHasMore(Boolean hasMore) {
            this.hasMore = hasMore;
        }

    }






    public class Element {

        @SerializedName("nViews")
        @Expose
        private Long nViews;
        @SerializedName("creator")
        @Expose
        private String creator;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("desc")
        @Expose
        private String desc;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("owner")
        @Expose
        private Owner owner;
        @SerializedName("sharedList")
        @Expose
        private List<KnowledgeDetailModel.SharedList> sharedList = null;
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
        private KnowledgeDetailModel.MyActions myActions;
        @SerializedName("sharedOn")
        @Expose
        private Long sharedOn;
        @SerializedName("createdOn")
        @Expose
        private Long createdOn;
        @SerializedName("lastMod")
        @Expose
        private Long lastMod;
        @SerializedName("nFollows")
        @Expose
        private Long nFollows;
        @SerializedName("nUpVotes")
        @Expose
        private Long nUpVotes;
        @SerializedName("nDownVotes")
        @Expose
        private Long nDownVotes;

        public Long getNViews() {
            return nViews;
        }

        public void setNViews(Long nViews) {
            this.nViews = nViews;
        }

        public String getCreator() {
            return creator;
        }

        public void setCreator(String creator) {
            this.creator = creator;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public List<KnowledgeDetailModel.SharedList> getSharedList() {
            return sharedList;
        }

        public void setSharedList(List<KnowledgeDetailModel.SharedList> sharedList) {
            this.sharedList = sharedList;
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

        public KnowledgeDetailModel.MyActions getMyActions() {
            return myActions;
        }

        public void setMyActions(KnowledgeDetailModel.MyActions myActions) {
            this.myActions = myActions;
        }

        public Long getSharedOn() {
            return sharedOn;
        }

        public void setSharedOn(Long sharedOn) {
            this.sharedOn = sharedOn;
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

        public Long getNFollows() {
            return nFollows;
        }

        public void setNFollows(Long nFollows) {
            this.nFollows = nFollows;
        }

        public Long getNUpVotes() {
            return nUpVotes;
        }

        public void setNUpVotes(Long nUpVotes) {
            this.nUpVotes = nUpVotes;
        }

        public Long getNDownVotes() {
            return nDownVotes;
        }

        public void setNDownVotes(Long nDownVotes) {
            this.nDownVotes = nDownVotes;
        }

    }

}
