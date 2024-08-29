package kore.botssdk.fileupload.listeners;

import java.io.IOException;
import java.util.Hashtable;

public interface FileTokenListener {
	/**
	 * Callback for when filetoken received successfully 
	 */
	void fileTokenReceivedSuccessfully(Hashtable<String, String> hsh) throws IOException;
	
	/**
	 * Callback for when filetoken service unsuccessful 
	 */
	void fileTokenReceivedWithFailure(String errCode, String reason);
}
