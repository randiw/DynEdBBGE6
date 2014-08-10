package com.awan.dyned6.utils;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Graphics;

public class GraphicUtil
{
	public static void drawGradientLine(Graphics graphics, int x1, int y1, int x2, int y2, int color1, int color2, boolean isVertical)
	{
		int xPoints[] = {x1, x2, x2, x1};
		int yPoints[] = {y1, y1, y2, y2};
		byte pointTypes[] = {Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT};
		int colors[] = {color1, color2, color2, color1};
		if (isVertical)
		{
			colors[0] = color1; colors[1] = color1; colors[2] = color2; colors[3] = color2;
		}
		graphics.drawShadedFilledPath(xPoints, yPoints, pointTypes, colors, null);
	}

    public static int hexToRgb(String hex)
    {
        hex = hex.toUpperCase();
        hex = net.rim.device.api.util.StringUtilities.removeChars(hex, "#");
        if (hex.startsWith("0X"))
        {
            hex = hex.substring(2, 8);
        }
        int ret = Integer.parseInt(hex, 16);
        return ret;
    }

    public static Bitmap resizeBitmap(EncodedImage image, int width, int height)
    {
        return getScaledBitmapImage(image, width, height);
    }

    public static Bitmap getScaledBitmapImage(EncodedImage image, int width, int height)
    {
        // Handle null image
        if (image == null)
        {
            return null;
        }
        
        int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
        int currentHeightFixed32 = Fixed32.toFP(image.getHeight());
        
        int requiredWidthFixed32 = Fixed32.toFP(width);
        int requiredHeightFixed32 = Fixed32.toFP(height);
        
        int scaleXFixed32 = Fixed32.div(currentWidthFixed32, requiredWidthFixed32);
        int scaleYFixed32 = Fixed32.div(currentHeightFixed32, requiredHeightFixed32);
        
        image = image.scaleImage32(scaleXFixed32, scaleYFixed32);
        
        return image.getBitmap();
    }

    public static Bitmap bestFit(EncodedImage image, int maxWidth, int maxHeight){
    	
        if (maxWidth < 0) maxWidth = 0;
        if (maxHeight < 0) maxHeight = 0;
        
        // getting image properties
        int w = image.getWidth();
        int h = image.getHeight();
        
        //  get the ratio
        int ratiow = 100 * maxWidth / w;
        int ratioh = 100 * maxHeight / h;
        
        // this is to find the best ratio to
        // resize the image without deformations
        int ratio = Math.min(ratiow, ratioh);
        
        // computing final desired dimensions
        int desiredWidth = w * ratio / 100;
        int desiredHeight = h * ratio / 100;
        
        //resizing
        return getScaledBitmapImage(image, desiredWidth, desiredHeight);
    }  
    public static EncodedImage resizeImage(EncodedImage image, int newWidth, int newHeight) {
        int scaleFactorX = Fixed32.div(Fixed32.toFP(image.getWidth()), Fixed32.toFP(newWidth));
        int scaleFactorY = Fixed32.div(Fixed32.toFP(image.getHeight()), Fixed32.toFP(newHeight));
        return image.scaleImage32(scaleFactorX, scaleFactorY);
    }
}