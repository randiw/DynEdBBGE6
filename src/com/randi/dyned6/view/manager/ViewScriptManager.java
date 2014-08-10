package com.randi.dyned6.view.manager;

import com.randi.dyned6.tools.FontSetting;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class ViewScriptManager extends Manager implements FieldChangeListener {
	
	private FontSetting fontSetting = FontSetting.getInstance();
	
	private PlusField plusField;
	private boolean isOpen;
	private boolean isPlaying;
	private String text;
	
	private HorizontalFieldManager hfManager;
	private VerticalFieldManager vfManager;

	public ViewScriptManager(String text) {
		super(USE_ALL_WIDTH | FOCUSABLE);
		this.text = text;
		
		hfManager = new HorizontalFieldManager(USE_ALL_WIDTH);
		hfManager.setBackground(BackgroundFactory.createLinearGradientBackground(Color.WHITE, Color.WHITE, Color.GRAY, Color.GRAY));
		hfManager.setPadding(5, 5, 0, 5);
		add(hfManager);
		
		plusField = new PlusField("+");
		plusField.setChangeListener(this);
		plusField.setPadding(0, 5, 0, 0);
		fontSetting.setPoint(Font.BOLD, 11);
		plusField.setFont(fontSetting.getFont());
		hfManager.add(plusField);
		
		LabelField labelField = new LabelField("View Script", USE_ALL_WIDTH | FIELD_HCENTER | FOCUSABLE){
			public void clickButton() {
				fieldChangeNotify(0);
			}

			protected boolean invokeAction(int action) {
				switch (action) {
				case ACTION_INVOKE: {
					clickButton();
					return true;
				}
				}
				return super.invokeAction(action);
			}

			protected boolean keyChar(char character, int status, int time) {
				if (character == Characters.ENTER) {
					clickButton();
					return true;
				}
				return super.keyChar(character, status, time);
			}

			protected boolean navigationClick(int status, int time) {
				clickButton();
				return true;
			}

			protected boolean trackwheelClick(int status, int time) {
				clickButton();
				return true;
			}
			
			public void setDirty(boolean dirty) {
			}

			public void setMuddy(boolean muddy) {
			}	
		};
		labelField.setChangeListener(this);
		fontSetting.setPoint(Font.PLAIN, 8);
		labelField.setFont(fontSetting.getFont());
		hfManager.add(labelField);
		
		vfManager = new VerticalFieldManager(USE_ALL_WIDTH | VERTICAL_SCROLL | VERTICAL_SCROLLBAR);
		add(vfManager);
	}

	protected void sublayout(int width, int height) {
		layoutChild(hfManager, width, height);
		layoutChild(vfManager, width, height - hfManager.getHeight());
		
		setPositionChild(hfManager, 0, 0);
		setPositionChild(vfManager, 0, hfManager.getHeight());
		
		setExtent(width, hfManager.getHeight() + vfManager.getHeight());
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	private class PlusField extends LabelField {
	
		public PlusField(String label) {
			super(label, LabelField.HCENTER | LabelField.VCENTER | FOCUSABLE);
		}
		
		protected void paint(Graphics g) {
			g.setColor(Color.BLUE);
			super.paint(g);
		}
		
		public void clickButton() {
			fieldChangeNotify(0);
		}

		protected boolean invokeAction(int action) {
			switch (action) {
			case ACTION_INVOKE: {
				clickButton();
				return true;
			}
			}
			return super.invokeAction(action);
		}

		protected boolean keyChar(char character, int status, int time) {
			if (character == Characters.ENTER) {
				clickButton();
				return true;
			}
			return super.keyChar(character, status, time);
		}

		protected boolean navigationClick(int status, int time) {
			clickButton();
			return true;
		}

		protected boolean trackwheelClick(int status, int time) {
			clickButton();
			return true;
		}
		
		public void setDirty(boolean dirty) {
		}

		public void setMuddy(boolean muddy) {
		}
	}

	public void fieldChanged(Field field, int context) {
		if(field instanceof PlusField || field instanceof LabelField){
			if(!isPlaying){
				if(isOpen){
					vfManager.deleteAll();				
					plusField.setText("+");					
					isOpen = false;
				} else {
					if(!(vfManager.getFieldCount() > 0)){
						RichTextField rtField = new RichTextField(text);
						rtField.setPadding(10, 10, 10, 10);
						rtField.setBackground(BackgroundFactory.createSolidBackground(Color.WHITESMOKE));
						rtField.setBorder(BorderFactory.createSimpleBorder(new XYEdges(2, 2, 2, 2), new XYEdges(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK), Border.STYLE_SOLID));
						fontSetting.setPoint(Font.PLAIN, 6);
						rtField.setFont(fontSetting.getFont());
						vfManager.add(rtField);

						plusField.setText("-");
						isOpen = true;
					}
				}				
			}
		}
	}
}