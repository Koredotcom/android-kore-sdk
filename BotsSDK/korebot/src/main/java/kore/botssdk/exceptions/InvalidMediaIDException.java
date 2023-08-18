package kore.botssdk.exceptions;

public class InvalidMediaIDException extends Exception {

	
	private static final long serialVersionUID = 8126863287976216324L;
	
	final String msg = "InvalidMediaID, Expected MEDIA_TYPE_AUDIO or MEDIA_TYPE_VIDEO";

	@Override
	public String getMessage() {
		return msg;
	}

	@Override
	public String toString() {
		return msg;
	}
}
