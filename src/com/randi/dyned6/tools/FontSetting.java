package com.randi.dyned6.tools;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;

/**
 * Class to configure every fonts used.
 * @author Randi Waranugraha
 *
 */
public class FontSetting {

	private static FontSetting instance = null;
	private Font font;
	private FontFamily[] fontFamilies;
	private FontFamily fontFamily;
	private String _fontname;

	private FontSetting() {
		fontFamilies = FontFamily.getFontFamilies();
		fontFamily = fontFamilies[0];
		font = Font.getDefault();
	}

	/**
	 * Retrieve the system's FontSetting instance.
	 * @return
	 */
	public static FontSetting getInstance(){
		if(instance == null){
			instance = new FontSetting();
		}
		return instance;
	}

	/**
	 * Set the instance FontFamily
	 * @param i
	 */
	public void setFontFamily(int i){
		fontFamily = fontFamilies[i];
		font = fontFamily.getFont(FontFamily.UNKNOWN_FONT, 16);
	}

	/**
	 * Return an array containing all registered FontFamily used by this instance.
	 * @return
	 */
	public FontFamily[] getFontFamilies(){
		return fontFamilies;
	}

	/**
	 * Returns a font used by this instance.
	 * @return
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * Set a new font specified by style and height.
	 * @param style The new style.
	 * @param size The new height.
	 */
	public void setFontSize(int style, int size){
		font = font.derive(style, size);
	}

	/**
	 * Set a new font specified by style and height using the units for height. See the UNITS_* constants in the Ui class.
	 * @param style The new style.
	 * @param size The new height.
	 */
	public void setPoint(int style, int size){
		font = font.derive(style, size, Ui.UNITS_pt);
	}
	
	/**
	 * Set a new font with Font.ITALIC style.
	 */
	public void setItalic(){
		font = font.derive(Font.ITALIC);
	}

	/**
	 * Set a new font with Font.BOLD style.
	 */
	public void setBold(){
		font = font.derive(Font.BOLD);
	}

	/**
	 * Set a new font with Font.PLAIN style.
	 */
	public void setPlain(){
		font = font.derive(Font.PLAIN);
	}
	
	/**
	 *Create new FontSetting object.
	 * @param fontname
	 */
	public FontSetting(String fontname){
        _fontname=fontname;
    }
	
	/**
	 * Reload the font used by this instance.
	 * @param style
	 * @param height
	 * @return
	 */
	public Font ReLoadFont(int style,int height){
        try {
        	FontFamily theFam = FontFamily.forName(_fontname);
        	return theFam.getFont(style, height);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
	}    
}