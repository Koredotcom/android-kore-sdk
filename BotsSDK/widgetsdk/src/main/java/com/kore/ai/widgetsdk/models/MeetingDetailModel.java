package com.kore.ai.widgetsdk.models;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.kore.ai.widgetsdk.utils.DateUtils;
import com.kore.ai.widgetsdk.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shiva Krishna on 1/30/2018.
 */

public class MeetingDetailModel implements Serializable {
    public String getTempHeader() {
        return tempHeader;
    }

    public void setTempHeader(String tempHeader) {
        this.tempHeader = tempHeader;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    private boolean showHeader;


    private String tempHeader;
    private String streamId;
    private String creator;
    private String lMod;
    private String imageUrl;
    private String title;
    private String mId;
    private ContactInfoModel owner;

    private MeetingDetails meetingDetails;

    public MeetingDetails getMeetingDetails() {
        return meetingDetails;
    }

    public List<CalEventsTemplateModel.Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<CalEventsTemplateModel.Attendee> attendees) {
        this.attendees = attendees;
    }

    public void setMeetingDetails(MeetingDetails meetingDetails) {
        this.meetingDetails = meetingDetails;
    }

 /*   public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
*/
    private List<CalEventsTemplateModel.Attendee> attendees = null;
    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    private String url;
    private int nLikes;


    private List<SharedList> sharedList = null;


    public List<SharedList> getSharedList() {

        return sharedList;
    }


    public String getSharedListWithCommaSeperate() {
        if (sharedList != null && sharedList.size() > 0) {
            ArrayList<String> namesList = new ArrayList<>();
            for (SharedList shared : sharedList) {
                if (shared != null && shared.getName() != null && !TextUtils.isEmpty(shared.getName().trim())) {
                    namesList.add(shared.getName().trim());
                }
            }
            if (namesList.size() > 0) {
                return TextUtils.join(", ", namesList);
            }
        }
        return "";
    }

    public void setSharedList(List<SharedList> sharedList) {
        this.sharedList = sharedList;
    }

    public long getLastMod() {
        return lastMod;
    }

    public void setLastMod(long lastMod) {
        this.lastMod = lastMod;
    }

    private long lastMod;

    public String getSharedBy() {
        return sharedBy;
    }

    public String getFormattedModifiedDate() {
        if (formattedModifiedDate == null) {
            formattedModifiedDate = DateUtils.formattedSentDateV6((Long) lastMod);
        }
        return formattedModifiedDate;
    }

    public String getFormattedHeaderDate() {
        if (formattedHeaderDate == null) {
            formattedHeaderDate = DateUtils.formattedSentDateV8((Long) lastMod, false);
        }
        return formattedHeaderDate;
    }

    private String formattedHeaderDate;

    public void setFormattedModifiedDate(String formattedModifiedDate) {
        this.formattedModifiedDate = formattedModifiedDate;
    }

    public String getLastModifiedDate() {
        return "Modified " + DateUtils.formattedSentDateV8(lastMod, true);
    }

    private String formattedModifiedDate;


    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    private String sharedBy;

    public long getNViews() {
        return nViews;
    }

    public void setNViews(long nViews) {
        this.nViews = nViews;
    }

    private long nViews;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

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


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCreatedOn() {
        return createdOn;
    }


public String getFormatedDate()
{
   return DateUtils.formattedSentDateV8_InAnnouncement(createdOn);
}


public boolean getDateLabelVisblity()
{
    return DateUtils.formattedSentDateV8_InAnnouncement2(createdOn);
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

    private String pId;
    String id;

    String type;
    private String orgId;
    private String desc;
    private long createdOn;
    private String description;




    private ArrayList<KoreComponentModel> components;
    private ArrayList<LinkPreviewModel> linkPreviews;
    private ArrayList<String> likes;

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    private ArrayList<String> followers;

    public int getNShares() {
        return nShares;
    }

    public void setNShares(int nShares) {
        this.nShares = nShares;
    }

    private int nShares;

    private int nDownVotes;

    public int getNDownVotes() {
        return nDownVotes;
    }

    public void setNDownVotes(int nDownVotes) {
        this.nDownVotes = nDownVotes;
    }

    public int getNUpVotes() {
        return nUpVotes;
    }

    public void setNUpVotes(int nUpVotes) {
        this.nUpVotes = nUpVotes;
    }

    private int nUpVotes;


    public int getNComments() {
        return nComments;
    }

    public void setNComments(int nComments) {
        this.nComments = nComments;
    }

    private int nComments;

    public ArrayList<CommentModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentModel> comments) {
        this.comments = comments;
    }

    private ArrayList<CommentModel> comments;

    public int getNFollows() {
        return nFollows;
    }

    public void setNFollows(int nFollows) {
        this.nFollows = nFollows;
    }

    private int nFollows;

    public MyActions getMyActions() {
        return myActions;
    }

    public void setMyActions(MyActions myActions) {
        this.myActions = myActions;
    }

    private MyActions myActions;

    private ArrayList<VoteModel> votes;

    public ArrayList<VoteModel> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<VoteModel> votes) {
        this.votes = votes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ContactInfoModel getOwner() {
        return owner;
    }

    public void setOwner(ContactInfoModel owner) {
        this.owner = owner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNLikes() {
        return nLikes;
    }

    public void setNLikes(int nLikes) {
        this.nLikes = nLikes;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public static class SharedList implements Serializable {


        private String lN;

        private String role;

        private String color;

        private String id;

        private String name;

        private String fN;

        private String label;

        private Boolean isPending;

        private Long privilege;

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

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Boolean getIsPending() {
            return isPending;
        }

        public void setIsPending(Boolean isPending) {
            this.isPending = isPending;
        }

        public Long getPrivilege() {
            return privilege;
        }

        public void setPrivilege(Long privilege) {
            this.privilege = privilege;
        }

    }

    public static class VoteModel implements Serializable{
        int vote;

        public int getVote() {
            return vote;
        }

        public void setVote(int vote) {
            this.vote = vote;
        }

        public String getBy() {
            return by;
        }

        public void setBy(String by) {
            this.by = by;
        }

        String by;


    }

    public static class MyActions implements Serializable {
        boolean like;
        boolean follow;
        int vote;

        public int getPrivilege() {
            return privilege;
        }

        public void setPrivilege(int privilege) {
            this.privilege = privilege;
        }

        int privilege;

        public boolean isLike() {
            return like;
        }

        public void setLike(boolean like) {
            this.like = like;
        }

        public boolean isFollow() {
            return follow;
        }

        public void setFollow(boolean follow) {
            this.follow = follow;
        }

        public int getVote() {
            return vote;
        }

        public void setVote(int vote) {
            this.vote = vote;
        }
    }

    public static class CommentModel implements Serializable{
        private long cOn;
        private String id;
        private long lMod;
        private String by;

        public long getcOn() {
            return cOn;
        }

        public void setcOn(long cOn) {
            this.cOn = cOn;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getlMod() {
            return lMod;
        }

        public void setlMod(long lMod) {
            this.lMod = lMod;
        }

        public String getBy() {
            return by;
        }

        public void setBy(String by) {
            this.by = by;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        private String comment;
        private String label;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof MeetingDetailModel) {
            if (this.getId() != null && ((MeetingDetailModel) obj).getId() != null) {
                return this.getId().equals(((MeetingDetailModel) obj).getId());
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
        int result = id == null ? 0 : 31 * id.hashCode();
        result = 32 * result + (id == null ? 0 : id.hashCode());
        result = 33 * result + (creator == null ? 0 : creator.hashCode());
        return result;
    }

    public Spanned getSpannedString() {

        return StringUtils.isNullOrEmpty(desc) ? null : Html.fromHtml(desc.replaceAll("<br>", " "));
    }

}
