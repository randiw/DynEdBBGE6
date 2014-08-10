package com.awan.dyned6.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.Datagram;
import javax.microedition.io.DatagramConnection;
import javax.microedition.io.HttpConnection;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class Utils {
	public static String getDateNow()
	{
		Date now = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(now);
	}
	public static void setDialog(final String title){		
		UiApplication.getUiApplication().invokeLater(new Runnable() {			
			public void run() {
				// TODO Auto-generated method stub
				Dialog.alert(title);
			}
		});		
	}
	public static String replace(String source, String pattern, String replacement)
	{	
	
		//If source is null then Stop
		//and return empty String.
		if (source == null)
		{
			return "";
		}

		StringBuffer sb = new StringBuffer();
		//Intialize Index to -1
		//to check against it later 
		int idx = -1;
		//Intialize pattern Index
		int patIdx = 0;
		//Search source from 0 to first occurrence of pattern
		//Set Idx equal to index at which pattern is found.
		idx = source.indexOf(pattern, patIdx);
		//If Pattern is found, idx will not be -1 anymore.
		if (idx != -1)
		{
			//append all the string in source till the pattern starts.
			sb.append(source.substring(patIdx, idx));
			//append replacement of the pattern.
			sb.append(replacement);
			//Increase the value of patIdx
			//till the end of the pattern
			patIdx = idx + pattern.length();
			//Append remaining string to the String Buffer.
			sb.append(source.substring(patIdx));
		}
		//Return StringBuffer as a String

                if ( sb.length() == 0)
                {
                    return source;
                }
                else
                {
                    return sb.toString();
                }
	}
	public static String parseInputStreamToString(InputStream is){
		
		String strResponse="";
		
		byte[] data = new byte[102400];
		int len = 0;
		StringBuffer raw = new StringBuffer();
		try {
			while (-1 != (len = is.read(data))) {
				raw.append(new String(data, 0, len));
				strResponse = raw.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strResponse;
	}
	public static String loadFileFromUrl(String url){
		StringBuffer b = new StringBuffer();
		int ch = 0;
		HttpConnection loadBitmapWeb = NetworkUtil.getWebContent(url+NetworkUtil.getConnectionString());
		if(loadBitmapWeb != null){
			try {
				InputStream is = loadBitmapWeb.openInputStream();
				long len = loadBitmapWeb.getLength();
				if( len != -1) {
					for(int i=0; i<len;i++){
						if((ch = is.read()) != -1) {
							b.append((char) ch);
						}
					}
				}else{
					while ((ch = is.read()) != -1) {
						len = is.available() ;
						b.append((char)ch);
					}
				}
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return b.toString();
	}
	
	public static String replaceAll(String source, String pattern, String replacement)
	{    

	    //If source is null then Stop
	    //and retutn empty String.
	    if (source == null)
	    {
	        return "";
	    }

	    StringBuffer sb = new StringBuffer();
	    //Intialize Index to -1
	    //to check agaist it later 
	    int idx = 0;
	    //Search source from 0 to first occurrence of pattern
	    //Set Idx equal to index at which pattern is found.

	    String workingSource = source;
	    
	    //Iterate for the Pattern till idx is not be -1.
	    while ((idx = workingSource.indexOf(pattern, idx)) != -1)
	    {
	        //append all the string in source till the pattern starts.
	        sb.append(workingSource.substring(0, idx));
	        //append replacement of the pattern.
	        sb.append(replacement);
	        //Append remaining string to the String Buffer.
	        sb.append(workingSource.substring(idx + pattern.length()));
	        
	        //Store the updated String and check again.
	        workingSource = sb.toString();
	        
	        //Reset the StringBuffer.
	        sb.delete(0, sb.length());
	        
	        //Move the index ahead.
	        idx += replacement.length();
	    }

	    return workingSource;
	}
	public static String[] split(String strString, String strDelimiter){
		int iOccurrences = 0;
		int iIndexOfInnerString = 0;
		int iIndexOfDelimiter = 0;
		int iCounter = 0;

		// Check for null input strings.
		if (strString == null)
		{
			throw new NullPointerException("Input string cannot be null.");
		}
		// Check for null or empty delimiter
		// strings.
		if (strDelimiter.length() <= 0 || strDelimiter == null)
		{
			throw new NullPointerException("Delimeter cannot be null or empty.");
		}

		// If strString begins with delimiter
		// then remove it in
		// order
		// to comply with the desired format.

		if (strString.startsWith(strDelimiter))
		{
			strString = strString.substring(strDelimiter.length());
		}

		// If strString does not end with the
		// delimiter then add it
		// to the string in order to comply with
		// the desired format.
		if (!strString.endsWith(strDelimiter))
		{
			strString += strDelimiter;
		}

		// Count occurrences of the delimiter in
		// the string.
		// Occurrences should be the same amount
		// of inner strings.
		while((iIndexOfDelimiter= strString.indexOf(strDelimiter,iIndexOfInnerString))!=-1)
		{
			iOccurrences += 1;
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
		}

		// Declare the array with the correct
		// size.
		String[] strArray = new String[iOccurrences];

		// Reset the indices.
		iIndexOfInnerString = 0;
		iIndexOfDelimiter = 0;

		// Walk across the string again and this
		// time add the
		// strings to the array.
		while((iIndexOfDelimiter= strString.indexOf(strDelimiter,iIndexOfInnerString))!=-1)
		{

			// Add string to
			// array.
			strArray[iCounter] = strString.substring(iIndexOfInnerString, iIndexOfDelimiter);

			// Increment the
			// index to the next
			// character after
			// the next
			// delimiter.
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();

			// Inc the counter.
			iCounter += 1;
		}
            return strArray;
	}
	public static void sendSms(final String keyword, final String sdc){
		new Thread() {
            public void run() {
                if (RadioInfo.getNetworkType() == RadioInfo.NETWORK_GPRS) {
                    DatagramConnection dc = null;
                    try {
                        dc = (DatagramConnection) Connector.open("sms://"+sdc);
                        byte[] data = keyword.getBytes();
                        Datagram dg = dc.newDatagram(dc.getMaximumLength());
                        dg.setData(data, 0, data.length);
                        dc.send(dg);
                        UiApplication.getUiApplication().invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    System.out.println("Message Sent Successfully ["+keyword+"]");
                                    Dialog.alert("Message Sent Successfully ["+keyword+"]");
                                } catch (Exception e) {
                                	Dialog.alert("Exception **1 : " + e.toString());
                                    System.out.println("Exception **1 : " + e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                    	Dialog.alert("Exception **1 : " + e.toString());
                        System.out.println("Exception 1 : " + e.toString());
                        e.printStackTrace();
                    } finally {
                        try {
                            dc.close();
                            dc = null;
                        } catch (IOException e) {
                        	Dialog.alert("Exception **2 : " + e.toString());
                            System.out.println("Exception 2 : " + e.toString());
                            e.printStackTrace();
                        }
                    }
                } else {
                    MessageConnection conn = null;
                    try {
                        conn = (MessageConnection) Connector.open("sms://"+sdc);
                        //generate a new text message
                        TextMessage tmsg = (TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
                        //set the message text and the address
                        tmsg.setAddress("sms://"+sdc);
                        tmsg.setPayloadText(keyword);
                        //finally send our message
                        conn.send(tmsg);
                        UiApplication.getUiApplication().invokeLater(new Runnable() {
                            public void run() {
                                try {
                                    System.out.println("Message Sent Successfully ["+keyword+"]");
                                    Dialog.alert("Message Sent Successfully ["+keyword+"]");
                                } catch (Exception e) {
                                	Dialog.alert("Exception **1 : " + e.toString());
                                    System.out.println("Exception **1 : " + e.toString());
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                    	Dialog.alert("Exception **3 : " + e.toString());
                        System.out.println("Exception 3 : " + e.toString());
                        e.printStackTrace();
                    } finally {
                        try {
                            conn.close();
                            conn = null;
                        } catch (IOException e) {
                        	Dialog.alert("Exception **4 : " + e.toString());
                            System.out.println("Exception 4 : " + e.toString());
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
	}
	public static void setSmsWithNotification(String notification, final String sdc, final String keyword){
		
		int ret = Dialog.ask(Dialog.D_YES_NO,notification,Dialog.YES);
		if(ret==Dialog.YES){
			try {
		        new Thread() {
		            public void run() {
		                if (RadioInfo.getNetworkType() == RadioInfo.NETWORK_GPRS) {
		                    DatagramConnection dc = null;
		                    try {
		                        dc = (DatagramConnection) Connector.open("sms://" + sdc);
		                        byte[] data = keyword.getBytes();
		                        Datagram dg = dc.newDatagram(dc.getMaximumLength());
		                        dg.setData(data, 0, data.length);
		                        dc.send(dg);
		                        UiApplication.getUiApplication().invokeLater(new Runnable() {
		                            public void run() {
		                                try {
		                                    System.out.println("Message Sent Successfully");
		                                    Dialog.alert("Aplikasi akan ditutup, silahkan buka kembali setelah menerima SMS");
		                                    System.exit(0);
		                                } catch (Exception e) {
		                                	Dialog.alert("Exception **1 : " + e.toString());
		                                    System.out.println("Exception **1 : " + e.toString());
		                                    e.printStackTrace();
		                                }
		                            }
		                        });
		                    } catch (Exception e) {
		                    	Dialog.alert("Exception **1 : " + e.toString());
		                        System.out.println("Exception 1 : " + e.toString());
		                        e.printStackTrace();
		                    } finally {
		                        try {
		                            dc.close();
		                            dc = null;
		                        } catch (IOException e) {
		                        	Dialog.alert("Exception **2 : " + e.toString());
		                            System.out.println("Exception 2 : " + e.toString());
		                            e.printStackTrace();
		                        }
		                    }
		                } else {
		                    MessageConnection conn = null;
		                    try {
		                        conn = (MessageConnection) Connector.open("sms://" + sdc);
		                        //generate a new text message
		                        TextMessage tmsg = (TextMessage) conn.newMessage(MessageConnection.TEXT_MESSAGE);
		                        //set the message text and the address
		                        tmsg.setAddress("sms://" + sdc);
		                        tmsg.setPayloadText(keyword);
		                        //finally send our message
		                        conn.send(tmsg);
		                        UiApplication.getUiApplication().invokeLater(new Runnable() {
		                            public void run() {
		                                try {
		                                    System.out.println("Message Sent Successfully, ");
		                                    Dialog.alert("Aplikasi akan ditutup, silahkan buka kembali setelah menerima SMS");
		                                    System.exit(0);
		                                } catch (Exception e) {
		                                	Dialog.alert("Exception **1 : " + e.toString());
		                                    System.out.println("Exception **1 : " + e.toString());
		                                    e.printStackTrace();
		                                }
		                            }
		                        });
		                    } catch (Exception e) {
		                    	Dialog.alert("Exception **3 : " + e.toString());
		                        System.out.println("Exception 3 : " + e.toString());
		                        e.printStackTrace();
		                    } finally {
		                        try {
		                            conn.close();
		                            conn = null;
		                        } catch (IOException e) {
		                        	Dialog.alert("Exception **4 : " + e.toString());
		                            System.out.println("Exception 4 : " + e.toString());
		                            e.printStackTrace();
		                        }
		                    }
		                }
		            }
		        }.start();
		    } catch (Exception e) {
		    	Dialog.alert("Exception **5 : " + e.toString());
		        System.out.println("Exception 5 : " + e.toString());
		        e.printStackTrace();
		    }
			
		}
		
	}
}
