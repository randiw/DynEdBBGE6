package com.randi.dyned6.tools;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.math.Fixed32;

/**
 * Utility tools for image processing.
 * @author Randi Waranugraha
 *
 */
public class ImageUtils {

	private ImageUtils() {
		// do nothing here.
	}

	/**
	 * Scaling EncodedImage with specified width and height.
	 * @param image
	 * @param newW
	 * @param newH
	 * @return Scaled EncodedImage.
	 */
	public static EncodedImage scaleImage(EncodedImage image, int newW, int newH) {
		int numeratorW = Fixed32.toFP(image.getWidth());
		int denominatorW = Fixed32.toFP(newW);
		int scaleW = Fixed32.div(numeratorW, denominatorW);
		int numeratorH = Fixed32.toFP(image.getHeight());
		int denominatorH = Fixed32.toFP(newH);
		int scaleH = Fixed32.div(numeratorH, denominatorH);
		image.setDecodeMode(EncodedImage.DECODE_NO_DITHER);
		return image.scaleImage32(scaleW, scaleH);
	}

	/**
	 * Resizing Bitmap with the specified width and height.
	 * @param image
	 * @param width
	 * @param height
	 * @return Resized Bitmap.
	 */
	public static Bitmap resizeBitmap(Bitmap image, int width, int height) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// Need an array (for RGB, with the size of original image)
		int rgb[] = new int[imageWidth * imageHeight];

		// Get the RGB array of image into "rgb"
		image.getARGB(rgb, 0, imageWidth, 0, 0, imageWidth, imageHeight);

		// Call to our function and obtain rgb2
		int rgb2[] = rescaleArray(rgb, imageWidth, imageHeight, width, height);

		// Create an image with that RGB array
		Bitmap temp2 = new Bitmap(width, height);

		temp2.setARGB(rgb2, 0, width, 0, 0, width, height);

		return temp2;
	}

	private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2) {
		int out[] = new int[x2 * y2];
		for (int yy = 0; yy < y2; yy++) {
			int dy = yy * y / y2;
			for (int xx = 0; xx < x2; xx++) {
				int dx = xx * x / x2;
				out[(x2 * yy) + xx] = ini[(x * dy) + dx];
			}
		}
		return out;
	}
}