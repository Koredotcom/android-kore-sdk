package com.kore.ai.widgetsdk.models;
public class KoreMedia{
	
	public static final String MEDIA_TYPE_AUDIO = "audio";
	public static final String MEDIA_TYPE_VIDEO = "video";
	public static final String MEDIA_TYPE_IMAGE = "image";
	public static final String MEDIA_TYPE_NONE = "text";
	public static final String MEDIA_TYPE_ATTACHMENT = "attachment";
	public static final String MEDIA_TYPE_FILELINK = "filelink";
	public static final String MEDIA_TYPE_DOCUMENT = "docs";
	public static final String MEDIA_TYPE_ARCHIVE = "archieve";
	public static final String MEDIA_TYPE_CONTACT = "contact";
	public static final String MEDIA_TYPE_LOCATION = "location";
	public static final String MEDIA_TYPE_MEETING = "meeting";
	public static final String MEDIA_TYPE_ONBOARD_BOTS = "onboard_bots";
	public static final String MEDIA_TYPE_ONBOARD_SPACE = "onboard_spaces";
	public static final String MEDIA_TYPE_ONBOARD_COWORKERS = "onboard_coworkers";
	public static final String MEDIA_TYPE_ONBOARD_DEVICECONTACTS = "onboard_devicecontacts";
	public static final String MEDIA_TYPE_EMAIL = "email";
    public static final String MEDIA_TYPE_ACTION = "action";
    public static final String MEDIA_TYPE_ALERT = "alert";
	public static final String MEDIA_TYPE_UPGRADE = "upgrade";
    public static final String MEDIA_TYPE_ALERT_ERROR = "error";
	public static final String MEDIA_TYPE_TEMPLATE = "template";
	public static final String MEDIA_TYPE_LINK = "link";
	
	
	public static final int BUFFER_SIZE_AUDIO = 20*1024;
	public static final int BUFFER_SIZE_VIDEO = 256 * 1024;
	public static final int BUFFER_SIZE_IMAGE = 256 * 1024;

    public static final int THUMBNAIL_TYPE_IMAGE = -1;
    public static final int THUMBNAIL_TYPE_VIDEO = -2;


	/**
	 * Total components allowed for a message
	 */
	public static final int COMPONENT_COUNT = 6;
	
	/** Video Recorder States */
	public static final byte RECORD = 0;
	public static final byte STOP = 1;
	public static final byte PLAY = 2;
}
