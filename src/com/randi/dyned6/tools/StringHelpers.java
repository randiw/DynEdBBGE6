/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.randi.dyned6.tools;

import java.util.Vector;

/**
 * Helper tools for String processing.
 * @author Randi Waranugraha
 */
public class StringHelpers {

	public static String EncodeDataHTTP(String s) {
		String data = "";
		int i;
		char c;

		try {
			for (i = 0; i < s.length(); i++) {
				c = s.charAt(i);

				if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '-' || c == '_' || c == '.') {
					data += s.substring(i, i + 1);
				} else {
					int k = (int) c;
					data += "%" + (Integer.toHexString(k).length() < 2 ? "0" : "") + Integer.toHexString(k).toUpperCase();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String urlEncode(String sUrl) {
		StringBuffer urlOK = new StringBuffer();

		for (int i = 0; i < sUrl.length(); i++) {
			char ch = sUrl.charAt(i);

			switch (ch) {
			case '<':
				urlOK.append("%3C");
				break;
			case '>':
				urlOK.append("%3E");
				break;
			case '/':
				urlOK.append("%2F");
				break;
			case ' ':
				urlOK.append("%20");
				break;
			case ':':
				urlOK.append("%3A");
				break;
			case '-':
				urlOK.append("%2D");
				break;
			default:
				urlOK.append(ch);
				break;
			}
		}
		return urlOK.toString();
	}

	public static String[] explode(char separator, String str) {
		Vector exploded = new Vector(0, 1);
		String tmpstr = null;
		int beginIndex = 0, endIndex = 0;
		while (endIndex < str.length()) {
			if (str.charAt(endIndex) == separator) {
				if (endIndex > beginIndex) {
					tmpstr = str.substring(beginIndex, endIndex);
					exploded.addElement(tmpstr);
					endIndex++;
					beginIndex = endIndex;
					tmpstr = null;
				} else {
					exploded.addElement("");
					endIndex++;
					beginIndex = endIndex;
				}
			} else {
				endIndex++;
			}
		}
		if (endIndex > beginIndex) {
			tmpstr = str.substring(beginIndex, endIndex);
			exploded.addElement(tmpstr);
		}
		String[] res = new String[exploded.size()];
		exploded.copyInto(res);
		return res;
	}

	public static String implode(String[] arrayString, String delim) {
		String out = "";
		for (int i = 0; i < arrayString.length; i++) {
			if (i != 0) {
				out += delim;
			}
			out += arrayString[i];
		}
		return out;
	}

	public static boolean contains(String str, char character) {
		String s;
		boolean contains = false;
		for (int i = 0; i < str.length() - 1; i++) {
			s = str.substring(i, i + 1);
			if (s.equals(character + "")) {
				contains = true;
			}
		}
		return contains;
	}

	public static String[] convertIntoArray(Vector vector) {
		String[] arrayString = new String[vector.size()];
		for (int i = 0; i < arrayString.length; i++) {
			arrayString[i] = (String) vector.elementAt(i);
		}
		return arrayString;
	}
}