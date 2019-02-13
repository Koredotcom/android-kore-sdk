package kore.botssdk.models;
import java.util.ArrayList;

import kore.botssdk.utils.StringUtils;


/**
 * Created by Shiva Krishna on 1/30/2018.
 */

public class KnowledgeDetailModel  {
    private String streamId;
    private String creator;
    private String lMod;
    private String imageUrl;
    private String title;
    private ContactInfo owner;
    public String getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    private String sharedBy;
    private String lMUId;

    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    private long views;

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

    private String pId;
    String id;

    String type;
    private String orgId;
    private String desc;
    private long createdOn;

    public ArrayList<String> getHashTag() {
        return hashTag;
    }

    public void setHashTag(ArrayList<String> hashTag) {
        this.hashTag = hashTag;
    }

    private ArrayList<String> hashTag;

    private ArrayList<KoreComponentModel> components;
    private ArrayList<LinkPreviewModel> linkPreviews;
    private ArrayList<VoteModel> votes;

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    private ArrayList<String> followers;

    public ArrayList<VoteModel> getVotes() {
        return votes;
    }

    public void setVotes(ArrayList<VoteModel> votes) {
        this.votes = votes;
    }

    public int getSharesCount() {
        return sharesCount;
    }

    public void setSharesCount(int sharesCount) {
        this.sharesCount = sharesCount;
    }

    public int getUpVoteCount() {
        return upVoteCount;
    }

    public void setUpVoteCount(int upVoteCount) {
        this.upVoteCount = upVoteCount;
    }

    public int getDownVoteCount() {
        return downVoteCount;
    }

    public void setDownVoteCount(int downVoteCount) {
        this.downVoteCount = downVoteCount;
    }

    private int upVoteCount;
    private int downVoteCount;
    private int sharesCount;

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    private int commentsCount;

    public ArrayList<CommentModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentModel> comments) {
        this.comments = comments;
    }

    private ArrayList<CommentModel> comments;

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    private int followCount;

    public MyActions getMyActions() {
        return myActions;
    }

    public void setMyActions(MyActions myActions) {
        this.myActions = myActions;
    }

    private MyActions myActions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ContactInfo getOwner() {
        return owner;
    }

    public void setOwner(ContactInfo owner) {
        this.owner = owner;
    }


    public class VoteModel {
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

   public class MyActions{
       int vote;
       boolean like;
       boolean follow;

       public int getPrivilege() {
           return privilege;
       }

       public void setPrivilege(int privilege) {
           this.privilege = privilege;
       }

       int privilege;

       public int getVote() {
           return vote;
       }

       public void setVote(int vote) {
           this.vote = vote;
       }

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
   }

    public class CommentModel {
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

    public class ContactInfo{
        public String getlN() {
            return lN;
        }

        public void setlN(String lN) {
            this.lN = lN;
        }

        private String lN;
        private String fN;
        private String id;



        public String getfN() {
            return fN;
        }

        public void setfN(String fN) {
            this.fN = fN;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        private String color;
        private String emailId;

        public String getInitials(){
           return StringUtils.getInitials(fN,lN);
        }
        public String getName(){
            return fN+" "+lN;
        }
    }
}
