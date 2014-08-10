package com.randi.dyned6.model.connector;

import java.io.IOException;
import java.io.InputStream;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.randi.dyned6.model.Resources;
import com.randi.dyned6.tools.Stream;

/**
 * Open connection for Statistic.
 * @author Randi Waranugraha
 *
 */
public class StatisticConnector extends UploadConnector {

	/**
	 * Creates a StatisticConnector object with postData.
	 * @param postData Post data; parameter and value pair.
	 */
	public StatisticConnector(URLEncodedPostData postData) {
		super(Resources.API_NEW_UPDATE, postData);
	}

	public void onReceiveResponseEvent(InputStream is) {
		System.out.println("[StatistikConnector] onReceiveResponseEvent");
		String jsonString;
		try {
			jsonString = Stream.asString(is);
			System.out.println("[StatistikConnector] JSON: " + jsonString);
			JSONObject object = new JSONObject(jsonString);
			if(object != null){
				String status = object.getString("status");
				String message = null;
				if("true".equals(status)){
					message = object.getString("message");
				} else {
					message = object.getString("error");
				}
				System.out.println("[StatistikConnector] status " + status + " " + message);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}