package kore.botssdk.models;
public interface KoreMedia{
	
	String MEDIA_TYPE_AUDIO = "audio";
	String MEDIA_TYPE_VIDEO = "video";
	String MEDIA_TYPE_IMAGE = "image";
	String MEDIA_TYPE_NONE = "text";
	String MEDIA_TYPE_ATTACHMENT = "attachment";
	String MEDIA_TYPE_FILELINK = "filelink";
	String MEDIA_TYPE_DOCUMENT = "docs";
	String MEDIA_TYPE_ARCHIVE = "archieve";
	String MEDIA_TYPE_CONTACT = "contact";
	String MEDIA_TYPE_LOCATION = "location";
	String MEDIA_TYPE_MEETING = "meeting";
	String MEDIA_TYPE_ONBOARD_BOTS = "onboard_bots";
	String MEDIA_TYPE_ONBOARD_SPACE = "onboard_spaces";
	String MEDIA_TYPE_ONBOARD_COWORKERS = "onboard_coworkers";
	String MEDIA_TYPE_ONBOARD_DEVICECONTACTS = "onboard_devicecontacts";
	String MEDIA_TYPE_EMAIL = "email";
    String MEDIA_TYPE_ACTION = "action";
    String MEDIA_TYPE_ALERT = "alert";
	String MEDIA_TYPE_UPGRADE = "upgrade";
    String MEDIA_TYPE_ALERT_ERROR = "error";
	String MEDIA_TYPE_TEMPLATE = "template";
	String MEDIA_TYPE_LINK = "link";

	String CHOOSE_TYPE_GALLERY = "choose";
	String CHOOSE_TYPE_IMAGE_VIDEO= "choose_image_video";
	String CHOOSE_TYPE_VIDEO_GALLERY = "choose_video";
	String CHOOSE_TYPE_FILE = "choose_file";
	String CHOOSE_TYPE_CAMERA = "camera";
	String FOR_MESSAGE = "message";
	String FOR_PROFILE = "profile";
	
	
	int BUFFER_SIZE_AUDIO = 20*1024;
	int BUFFER_SIZE_VIDEO = 256 * 1024;
	int BUFFER_SIZE_IMAGE = 256 * 1024;

    int THUMBNAIL_TYPE_IMAGE = -1;
    int THUMBNAIL_TYPE_VIDEO = -2;


	/**
	 * Total components allowed for a message
	 */
    int COMPONENT_COUNT = 6;
	
	/** Video Recorder States */
    byte RECORD = 0;
	byte STOP = 1;
	byte PLAY = 2;
	
	void startMediaRecording();
	void stopMediaRecording();

	void createMediaFilePath();

	void playOrViewMedia();
	void pauseOrStopMedia();
	void resumeMedia();
	
	void getUserinfo();
	void getFileTokenAndStartUpload();

	
}
