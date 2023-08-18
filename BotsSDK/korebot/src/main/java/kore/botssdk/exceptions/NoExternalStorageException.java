package kore.botssdk.exceptions;

public class NoExternalStorageException extends Exception {
	
	private static final long serialVersionUID = -422379123686026468L;
	
	final String msg = "No External Storage found on this device.";

	@Override
	public String getMessage() {
		return msg;
	}

	@Override
	public String toString() {
		return msg;
	}
}
