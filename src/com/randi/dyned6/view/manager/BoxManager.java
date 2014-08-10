package com.randi.dyned6.view.manager;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.container.VerticalFieldManager;

public abstract class BoxManager extends VerticalFieldManager implements FieldChangeListener {

	private Bitmap center_bottom;
	private Bitmap center_mid;
	private Bitmap center_top;
	private Bitmap left_bottom;
	private Bitmap left_mid;
	private Bitmap left_top;
	private Bitmap right_bottom;
	private Bitmap right_mid;
	private Bitmap right_top;

	public BoxManager() {
		super(USE_ALL_WIDTH | NO_VERTICAL_SCROLL);
		center_bottom = Bitmap.getBitmapResource("tr_center_bottom.png");
		center_mid = Bitmap.getBitmapResource("tr_center_mid.png");
		center_top = Bitmap.getBitmapResource("tr_center_top.png");

		left_bottom = Bitmap.getBitmapResource("tr_left_bottom.png");
		left_mid = Bitmap.getBitmapResource("tr_left_mid.png");
		left_top = Bitmap.getBitmapResource("tr_left_top.png");

		right_bottom = Bitmap.getBitmapResource("tr_right_bottom.png");
		right_mid = Bitmap.getBitmapResource("tr_right_mid.png");
		right_top = Bitmap.getBitmapResource("tr_right_top.png");
	}

	abstract public void fieldChanged(Field arg0, int arg1);

	protected void paint(Graphics g) {
		g.drawBitmap(0, 0, left_top.getWidth(), left_top.getHeight(), left_top, 0, 0);

		int totalWidth = getWidth() - (left_top.getWidth() + right_top.getWidth());
		int upperWidth = 0;
		while (upperWidth < totalWidth) {
			g.drawBitmap(left_top.getWidth() + upperWidth, 0, center_top.getWidth(), center_top.getHeight(), center_top, 0, 0);
			upperWidth += center_top.getWidth();
		}

		g.drawBitmap(getWidth() - right_top.getWidth(), 0, right_top.getWidth(), right_top.getHeight(), right_top, 0, 0);

		int totalHeight = getHeight() - (left_top.getHeight() + right_bottom.getHeight());
		int leftHeight = 0;
		while (leftHeight < totalHeight) {
			g.drawBitmap(0, left_top.getHeight() + leftHeight, left_mid.getWidth(), left_mid.getHeight(), left_mid, 0, 0);
			leftHeight += left_mid.getHeight();
		}

		int midWidth = 0;
		int midHeight = 0;
		while (midHeight < totalHeight) {
			midWidth = 0;
			while (midWidth < totalWidth) {
				g.drawBitmap(left_mid.getWidth() + midWidth, center_top.getHeight() + midHeight, center_mid.getWidth(), center_mid.getHeight(), center_mid, 0, 0);
				midWidth += center_mid.getWidth();
			}
			midHeight += center_mid.getHeight();
		}

		int rightHeight = 0;
		while (rightHeight < totalHeight) {
			g.drawBitmap(getWidth() - right_mid.getWidth(), right_top.getHeight() + rightHeight, right_mid.getWidth(), right_mid.getHeight(), right_mid, 0, 0);
			rightHeight += right_mid.getHeight();
		}

		g.drawBitmap(0, getHeight() - left_bottom.getHeight(), left_bottom.getWidth(), left_bottom.getHeight(), left_bottom, 0, 0);

		int bottomWidth = 0;
		while (bottomWidth < totalWidth) {
			g.drawBitmap(left_bottom.getWidth() + bottomWidth, getHeight() - center_bottom.getHeight(), center_bottom.getWidth(), center_bottom.getHeight(), center_bottom, 0, 0);
			bottomWidth += center_bottom.getWidth();
		}

		g.drawBitmap(getWidth() - right_bottom.getWidth(), getHeight() - right_bottom.getHeight(), right_bottom.getWidth(), right_bottom.getHeight(), right_bottom, 0, 0);
		super.paint(g);
	}
}