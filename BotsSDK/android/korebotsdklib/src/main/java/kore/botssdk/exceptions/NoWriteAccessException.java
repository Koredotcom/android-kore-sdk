package kore.botssdk.exceptions;

public class NoWriteAccessException extends Exception {
	
	private static final long serialVersionUID = 1752238736307501472L;
	
	final String msg = "No Write permission to External Storage";

	@Override
	public String getMessage() {
		return msg;
	}

	@Override
	public String toString() {
		return msg;
	}	
}