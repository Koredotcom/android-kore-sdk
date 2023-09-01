package kore.botssdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Locale;

import kore.botssdk.utils.StringUtils;

public class KoreContact implements Parcelable, Serializable {
	public static final int CATEGORY_COMPANY = 2;
	public static final String STATUS_ACTIVE = "active";

	public interface KoreContactColumns {
		String ID = "id";
		String ORG_ID = "org_id";
		String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
		String EMAIL_ID = "emain_id";
		String PHONE_NO = "phone_no";
        String ACCOUNT_INFO = "accountInfo";
		String CONTACT_TYPE = "contactType";
		String CONTACT_CATEGORY = "contactCategory";

        String DISPLAY_NAME = "display_name";
		String DISPLAY_FIRST_NAME = "display_first_name";
        String DISPLAY_NAME_INITIALS = "display_name_initials";
		String PROFILE_COLOR = "profile_color";
        String PROFILE_PIC_URL_128X128 = "profile_pic_url_128x128";
		String FAVOURITE = "favourite";
	}

	public static final String PROFILE = "profile";
	public static final String PROFILE_PNG = "profile.png";
	public static final String NO_AVATAR = "no-avatar";

	@SerializedName("_id")
	private String idInDl;

	public String id;

	public String firstName;

    /**
     * Don't initialize with empty string default rather let him null
     * else getDisplayName() will return "" if firstName and lastName is not available
     * which will create StringIndexOutOfBoundsException in
     * Utils.getMessageThreadRoundedImage() at String[] names = name.split("\\s+");
     * Keep it null then atleast it'll return emailId/phoneNo (which must be available)
     */
//	@DatabaseField(columnName = KoreContactColumns.LAST_NAME, dataType = DataType.STRING)
	public String lastName;// = "";

//	@DatabaseField(columnName = KoreContactColumns.ORG_ID, dataType = DataType.STRING)
	private String orgId;

//	@DatabaseField(columnName = KoreContactColumns.EMAIL_ID, dataType = DataType.STRING)
	private String emailId;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

//    @DatabaseField(columnName = KoreContactColumns.PHONE_NO, dataType = DataType.STRING)
	private String phoneNo;

//	@DatabaseField(columnName = "activation_status", dataType = DataType.STRING)
	private String activationStatus;

//	@DatabaseField(columnName = KoreContactColumns.FAVOURITE, dataType = DataType.BOOLEAN)
	private boolean isFavourite;

	@SerializedName("profImage")
//	@DatabaseField(columnName = "profile_image", dataType = DataType.STRING)
	private String profileImage;

	@SerializedName("profColour")
//	@DatabaseField(columnName = KoreContactColumns.PROFILE_COLOR, dataType = DataType.STRING)
	private String profileColor;

//	@DatabaseField(columnName = "online", dataType = DataType.BOOLEAN)
//	private boolean isOnline;

//    @DatabaseField(columnName = "status", dataType = DataType.INTEGER)
    private int onlineStatus;

//	@DatabaseField(columnName = "status_message", dataType = DataType.STRING)
	private String statusMessage;

//	@DatabaseField(columnName = "thumbnail_uri", dataType = DataType.STRING)
	private String thumbnailURI;

//    @DatabaseField(columnName = KoreContactColumns.DISPLAY_NAME, dataType = DataType.STRING)
    private String displayName;

//	@DatabaseField(columnName = KoreContactColumns.DISPLAY_FIRST_NAME, dataType = DataType.STRING)
	private String displayFirstName;

//    @DatabaseField(columnName = KoreContactColumns.DISPLAY_NAME_INITIALS, dataType = DataType.STRING)
    private String displayNameInitials;

//    @DatabaseField(columnName = KoreContactColumns.PROFILE_PIC_URL_128X128, dataType = DataType.STRING)
    private String profilePicUrl128;

//    @DatabaseField(columnName = KoreContactColumns.ACCOUNT_INFO, dataType = DataType.SERIALIZABLE)
//    private AccountInfoContact accountInfo;

	@SerializedName("type")
//    @DatabaseField(columnName= KoreContactColumns.CONTACT_TYPE, dataType = DataType.STRING, defaultValue = "")
    private String contactType;

//	@DatabaseField(columnName = KoreContactColumns.CONTACT_CATEGORY, dataType = DataType.INTEGER)//0->other, 1->Recent, 2->Company, 3->Device
	private int contactCategory = CATEGORY_COMPANY;

	// for Gson parsinf
	private String koreId;

	public static final Creator<KoreContact> CREATOR = new ContactCreator();

	public KoreContact(Parcel source) {
		id = source.readString();
		firstName = source.readString();
		lastName = source.readString();
		orgId = source.readString();
		emailId = source.readString();
		phoneNo = source.readString();
		activationStatus = source.readString();
		isFavourite = source.readInt() != 0;
		profileImage = source.readString();
		profileColor = source.readString();
		onlineStatus = source.readInt();
		statusMessage = source.readString();
		koreId = source.readString();
		thumbnailURI = source.readString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return (firstName == null) ? "" : firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		if (firstName == null)
			this.firstName = "";
	}

	public String getLastName() {
		return (lastName == null) ? "" : lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		if (lastName == null)
			this.lastName = "";
	}

    public String getDisplayName() {
		if(StringUtils.isNullOrEmptyWithTrim((displayName))){
			prepareDisplayName();
		}
        return displayName;
    }

    public void prepareDisplayName() {
        if ((firstName != null && !firstName.equalsIgnoreCase("")) || (lastName != null && !lastName.equalsIgnoreCase(""))) {
            displayName = ((firstName == null) ? "" : (firstName + " ")) + ((lastName == null) ? "" : lastName);
        } else if (emailId != null) {
            displayName = emailId;
        } else if (phoneNo != null) {
            displayName = phoneNo;
        } else{
            displayName = "";
        }
    }

	public String getDisplayFirstName() {
		return displayFirstName;
	}

	/*
	 * This includes only FirstName else only lastName or emailIdd or Phone Number
	 */
    public void prepareDisplayFirstName() {
		if ((firstName != null && !firstName.equalsIgnoreCase(""))) {
			displayFirstName = ((firstName == null) ? "" : (firstName + " "));
		} else if (lastName != null && !lastName.equalsIgnoreCase("")) {
			displayFirstName = ((lastName == null) ? "" : lastName);
		} else if (emailId != null) {
			displayFirstName = emailId;
		} else if (phoneNo != null) {
			displayFirstName = phoneNo;
		} else{
			displayFirstName = "";
		}
	}

	public String getDisplayNameInitials() {
        if(StringUtils.isNullOrEmptyWithTrim(displayNameInitials)){
//            prepareDisplayNameInitials();
        }
        return displayNameInitials;
    }

//    public void prepareDisplayNameInitials() {
//        this.displayNameInitials = ProfilePicUtils.getDisplayNameInitials(firstName, lastName, emailId);
//    }

    public String getProfilePicUrl128() {
        return profilePicUrl128;
    }

    public void prepareProfilePicUrl128() {
		if(profileImage != null && profileImage.equals("no-avatar")){
			profilePicUrl128 = null;
		}else {
			profilePicUrl128 = "/api/1.1/getMediaStream/" + "profilePictures/" + id + "/r_128x128_profile.png";
		}
    }

	public String getShortDisplayName() {
		String name = getDisplayName();
		String nameInitials = "";
		if (isValidEmail(name)) {
			nameInitials = "@";
		} else {
			String[] names = name.split("\\s+");
			nameInitials = names[0].substring(0, 1);
			if (names.length > 1) {
				nameInitials += names[1].substring(0, 1);
			}
			nameInitials = nameInitials.toUpperCase(Locale.ENGLISH);
		}

		return nameInitials;
	}

	public boolean isValidEmail(CharSequence email) {
		if (email == null)
			return false;
		else
			return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getEmailId() {
		return emailId != null ? emailId : phoneNo;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setActivationStatus(String activationStatus) {
		this.activationStatus = activationStatus;
	}

	public Boolean getIsFavourite() {
		return isFavourite;
	}

	public void setIsFavourite(Boolean isFavourite) {
		this.isFavourite = isFavourite;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

    public void setOnlineStatus(int status){
        onlineStatus = status;
    }

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getKoreId() {
		return isKoreContact() ? id : null;
	}

	public void setKoreId(String koreId) {
		this.koreId = koreId;
	}

	public String getThumbnailURI() {
		return thumbnailURI;
	}

	public void setThumbnailURI(String thumbnailURI) {
		this.thumbnailURI = thumbnailURI;
	}

    public void setProfColor(String profileColor) {
		this.profileColor = profileColor;
	}

	public String getProfColor() {
		return profileColor;
	}

    public String getType() {
        return contactType;
    }

    public void setType(String type) {
            this.contactType = type;
    }

	public int getContactCategory() {
		return contactCategory;
	}

	public void setContactCategory(int contactCategory) {
		this.contactCategory = contactCategory;
	}

	@Override
	public int describeContents() {
		return 0;
	}

    public boolean isKoreContact() {
		return (id != null
				&& !(id.equalsIgnoreCase(emailId) || id
						.equalsIgnoreCase(phoneNo))) && (activationStatus!=null && activationStatus.equalsIgnoreCase(STATUS_ACTIVE));
	}

	public String getDetail() {
		return emailId != null ? emailId : phoneNo;
	}

	public void processForPersistance() {
		prepareId();
		if (lastName == null) {
			lastName = "";
		}

		prepareEssentials();
	}

	public void prepareId() {
		if (id == null) {
			id = getDetail();
		}
	}

	public void prepareEssentials() {
		prepareDisplayName();
		prepareDisplayFirstName();
		prepareProfilePicUrl128();
	}

    @Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.v(this.getClass().getSimpleName(), "writeToParcel..." + flags);
		dest.writeString(id);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(orgId);
		dest.writeString(emailId);
		dest.writeString(phoneNo);
		dest.writeString(activationStatus);
		dest.writeInt(isFavourite ? 1 : 0);
		dest.writeString(profileImage);
		dest.writeString(profileColor);
		dest.writeInt(onlineStatus);
		dest.writeString(statusMessage);
		dest.writeString(koreId);
		dest.writeString(thumbnailURI);
	}

	public static class ContactCreator implements
			Creator<KoreContact> {

		@Override
		public KoreContact createFromParcel(Parcel source) {
			return new KoreContact(source);
		}

		@Override
		public KoreContact[] newArray(int size) {
			return new KoreContact[size];
		}

	}



	public String getNameForDisplay() {
		StringBuilder stringBuilder = new StringBuilder();
		if (firstName == null || firstName.isEmpty()) {
			stringBuilder.append(getEmailId());
		} else {
			stringBuilder.append(firstName);
			stringBuilder.append(" ");
			stringBuilder.append(lastName);
		}
		return stringBuilder.toString();
	}

	public String getIdInDl() {
		return idInDl;
	}

	public void setIdInDl(String idInDl) {
		this.idInDl = idInDl;
	}

	public boolean isProfilePicAvail() {
		return profileImage !=null && profileImage.contains(PROFILE);
	}

    /**
     * to avoid same hash code generation if any of fields are equal like in one object email and other object kore id are same and remaining or null
     * for that we are multiplying with different numbers
     * @return
     */
    @Override
    public int hashCode() {
        int result = emailId == null ? 0 : 31 * emailId.hashCode();
        result = 32 * result + (phoneNo == null ? 0 : phoneNo.hashCode());
        result = 33 * result + (koreId == null ? 0 : koreId.hashCode());
        return result;
    }
}
