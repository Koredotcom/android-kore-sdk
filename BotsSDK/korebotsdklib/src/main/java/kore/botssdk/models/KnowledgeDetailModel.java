package kore.botssdk.models;
import java.util.ArrayList;


/**
 * Created by Shiva Krishna on 1/30/2018.
 */

public class KnowledgeDetailModel extends BotCarouselModel {
    String streamId;
    String creator;
    String lMod;
    String imageUrl;

    String lMUId;

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getlMod() {
        return lMod;
    }

    public void setlMod(String lMod) {
        this.lMod = lMod;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getlMUId() {
        return lMUId;
    }

    public void setlMUId(String lMUId) {
        this.lMUId = lMUId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public ArrayList<KoreComponentModel> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<KoreComponentModel> components) {
        this.components = components;
    }

    public ArrayList<LinkPreviewModel> getLinkPreviews() {
        return linkPreviews;
    }

    public void setLinkPreviews(ArrayList<LinkPreviewModel> linkPreviews) {
        this.linkPreviews = linkPreviews;
    }

    String pId;
    String id;

    String type;
    String orgId;
    String desc;
    long createdOn;

    public ArrayList<String> getHashTag() {
        return hashTag;
    }

    public void setHashTag(ArrayList<String> hashTag) {
        this.hashTag = hashTag;
    }

    ArrayList<String> hashTag;

    ArrayList<KoreComponentModel> components;
    ArrayList<LinkPreviewModel> linkPreviews;

}