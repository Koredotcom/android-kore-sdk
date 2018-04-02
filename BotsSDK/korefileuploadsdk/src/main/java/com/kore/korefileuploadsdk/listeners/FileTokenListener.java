package com.kore.korefileuploadsdk.listeners;

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
	void fileTokenRecievedSuccessfully(Hashtable<String, String> hsh);
	
	/**
	 * Callback for when filetoken service unsuccessful 
	 */
	void fileTokenRecievedWithFailure(String errCode, String reason);
}
