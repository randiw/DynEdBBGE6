package com.randi.dyned6.tools;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

/**
 * Class to handle connection for saving file to local memory.
 * @author Randi Waranugraha
 *
 */
public class SaveFileConnection {

	private SaveFileConnectionListener listener;
	
	private FileConnection conn;
	private OutputStream os;

	/**
	 * Creates new SaveFileConnection object with the registered listener.
	 * @param listener SaveFileConnectionListener.
	 */
	public SaveFileConnection(SaveFileConnectionListener listener) {
		this.listener = listener;
	}

	/**
	 * Try to save file to the specified location.
	 * @param location
	 * @param value
	 */
	public void saveFile(String location, byte[] value){
		try {
			conn = (FileConnection) Connector.open(location, Connector.READ_WRITE);
			if(!conn.exists()){
				conn.create();
			}
			
			os = conn.openOutputStream();
			os.write(value);
			os.flush();
			
			if(listener != null) {
				listener.onDone();
			}
		} catch (IOException e) {
			if(listener != null) {
				listener.onError(e.getMessage());
			}
			e.printStackTrace();
		} finally {
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(conn != null) {
				try {
					conn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}