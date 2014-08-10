package com.randi.dyned6.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import com.randi.dyned6.model.Variables;

/**
 * Class to handle internet connection.
 * @author Randi Waranugraha
 *
 */
public class InternetConnection {

	private InternetConnectionListener listener;
	private HttpConnection conn;
	private InputStream is;
	private OutputStream os;
	
	private Hashtable headerHash;

	/**
	 * Creates new InternetConnection object with the registered listener.
	 * @param listener InternetConnectionListener.
	 */
	public InternetConnection(InternetConnectionListener listener) {
		this.listener = listener;
		headerHash = new Hashtable();
	}

	/**
	 * Access the specified URL with GET method.
	 * @param url
	 */
	public void accessURL(String url) {
		try {
			url = url+HttpUtils.getBestConnectionSuffix();
			System.out.println("access : " + url);
			conn = (HttpConnection)Connector.open(url);
			conn.setRequestMethod(HttpConnection.GET);
			conn.setRequestProperty("X-API-KEY", Variables.API_KEY);
			
			int responseCode = conn.getResponseCode();
			switch (responseCode) {
			case HttpConnection.HTTP_NOT_FOUND:
				if (listener != null) {
					listener.onNotFound();
				}
				break;

			case HttpConnection.HTTP_MOVED_PERM:
			case HttpConnection.HTTP_MOVED_TEMP:
			case HttpConnection.HTTP_TEMP_REDIRECT:
				String location = conn.getHeaderField("location");
				if (listener != null) {
					listener.onRedirectEvent(location);
				}
				break;

			case HttpConnection.HTTP_OK:
				if (listener != null) {
					listener.onStartEvent(conn.getLength(), conn.getType());

					is = conn.openInputStream();
					listener.onReceiveResponseEvent(is);
				}
				break;
			default:
				throw new IOException("Network Failure");
			}

			if (is != null) {
				is.close();
				is = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(listener != null){
				listener.onErrorOccurEvent(e);
			}
		}
	}

	/**
	 * Access the specified URL with POST method.
	 * @param url
	 * @param postData
	 */
	public void uploadParameter(String url, URLEncodedPostData postData){
		try {
			url = url+HttpUtils.getBestConnectionSuffix();
			System.out.println("uploadparameter : "+ url);
			conn = (HttpConnection)Connector.open(url);
			conn.setRequestMethod(HttpConnection.POST);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("X-API-KEY", Variables.API_KEY);
			if(headerHash.size() > 0){
				Enumeration keys = headerHash.keys();
				while(keys.hasMoreElements()){
					String key = (String)keys.nextElement();
					conn.setRequestProperty(key, (String)headerHash.get(key));
				}
			}

			os = conn.openOutputStream();

			if (os != null) {
				os.write(postData.getBytes());
			}

			if (os != null) {
				os.flush();
				os.close();
				os = null;
			}

			int responseCode = conn.getResponseCode();
			switch (responseCode) {
			case HttpConnection.HTTP_NOT_FOUND:
				if (listener != null) {
					listener.onNotFound();
				}
				break;
			case HttpConnection.HTTP_MOVED_PERM:
			case HttpConnection.HTTP_MOVED_TEMP:
			case HttpConnection.HTTP_TEMP_REDIRECT:
				String location = conn.getHeaderField("location");
				System.out.println("red location " + location);
				listener.onRedirectEvent(location);
				return;
			case HttpConnection.HTTP_OK:
				if (listener != null) {
					listener.onStartEvent(conn.getLength(), conn.getType());

					is = conn.openInputStream();
					listener.onReceiveResponseEvent(is);
				}
				break;
			
			case HttpConnection.HTTP_INTERNAL_ERROR:
				throw new IOException("There seems to be a problem with our system, you can try again later");

			default:
				throw new IOException("Please check your connection");
			}

			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(listener != null) {
				listener.onErrorOccurEvent(e);
			}
		}
	}
	
	public void addHeader(String key, String value){
		headerHash.put(key, value);
	}
}