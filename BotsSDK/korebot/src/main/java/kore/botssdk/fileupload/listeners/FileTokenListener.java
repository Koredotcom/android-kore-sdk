package kore.botssdk.fileupload.listeners;

import java.io.IOException;
import java.util.Hashtable;

/**
 * 
 * @author Ramchandra
 *
 */
public interface FileTokenListener {
	/**
	 * Callback for when filetoken received successfully 
	 */
	void fileTokenRecievedSuccessfully(Hashtable<String, String> hsh) throws IOException;
	
	/**
	 * Callback for when filetoken service unsuccessful 
	 */
	void fileTokenRecievedWithFailure(String errCode, String reason);
}
