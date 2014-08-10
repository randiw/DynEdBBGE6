package com.randi.dyned6.tools;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class Stream {

	public static String asString(InputStream pStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(pStream, baos, true);
		return baos.toString();
	}

	/**
	 * This convenience method allows to read a
	 * {@link org.apache.commons.fileupload.FileItemStream}'s content into a
	 * string, using the given character encoding.
	 * 
	 * @param pStream
	 *            The input stream to read.
	 * @param pEncoding
	 *            The character encoding, typically "UTF-8".
	 * @see #asString(InputStream)
	 * @return The streams contents, as a string.
	 * @throws IOException
	 *             An I/O error occurred.
	 */
	public static String asString(InputStream pStream, String pEncoding)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(pStream, baos, true);
		return baos.toString();// (pEncoding);
	}

	/**
	 * Default buffer size for use in
	 * {@link #copy(InputStream, OutputStream, boolean)}.
	 */
	private static final int DEFAULT_BUFFER_SIZE = 8192;

	public static long copy(InputStream pInputStream, OutputStream pOutputStream, boolean pClose) throws IOException {
		return copy(pInputStream, pOutputStream, pClose,new byte[DEFAULT_BUFFER_SIZE]);
	}

	public static long copy(InputStream pIn, OutputStream pOut, boolean pClose,byte[] pBuffer) throws IOException {
		
		OutputStream out = pOut;
		InputStream in = pIn;
		try {
			long total = 0;
			for (;;) {
				int res = in.read(pBuffer);
				if (res == -1) {
					break;
				}
				if (res > 0) {
					total += res;
					if (out != null) {
						out.write(pBuffer, 0, res);
					}
				}
			}
			if (out != null) {
				if (pClose) {
					out.close();
				} else {
					out.flush();
				}
				out = null;
			}
			in.close();
			in = null;
			return total;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Throwable t) {
					/* Ignore me */
				}
			}
			if (pClose && out != null) {
				try {
					out.close();
				} catch (Throwable t) {
					/* Ignore me */
				}
			}
		}
	}
	
	public static String Streaming(DataInputStream dis){
		String des = "";  
        try {  
        	StringBuffer inputLine = new StringBuffer();
            int tmp; 
            while ((tmp = dis.read()) != -1) {
                inputLine.append(tmp);
                System.out.println(tmp);
            }
            des = inputLine.toString();
            //use inputLine.toString(); here it would have whole source

        } catch (IOException ex) {  
            System.out.println(ex.toString() );  
        } finally {  
            try {  
                dis.close();  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return des ; 
	}
}
