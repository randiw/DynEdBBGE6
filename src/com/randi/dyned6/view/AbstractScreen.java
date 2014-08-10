package com.randi.dyned6.view;

import com.randi.dyned6.MainApp;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;

/**
 * Abstract class for every screen.
 * @author Randi Waranugraha
 *
 */
public abstract class AbstractScreen extends MainScreen implements FieldChangeListener {
	
	public AbstractScreen() {
		this(0);
	}
	
	public AbstractScreen(long style) {
		super(style | NO_VERTICAL_SCROLL | NO_HORIZONTAL_SCROLL);
		setBackground();
		setMenuItems();
	}

	public abstract void setBackground();
	
	public abstract void fieldChanged(Field field, int context);

	public void setMenuItems() {
		addMenuItem(new MenuItem("Back", 0, 5) {
			public void run() {
				actionKeyEscape();
			}
		});
		addMenuItem(new MenuItem("Exit", 0, 6) {
			public void run() {
				MainApp.exitApplication();
			}
		});
	}

	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		for (int i = 0; i < menu.getSize(); i++) {  
			if(menu.getItem(i)==MenuItem.getPrefab(MenuItem.CLOSE)){  
				menu.deleteItem(i); 
			}  
		}
	}

	protected boolean keyChar(char c, int status, int time) {
		if (c == Keypad.KEY_ESCAPE) {
			actionKeyEscape();
		}
		return super.keyChar(c, status, time);
	}

	protected void actionKeyEscape(){
		close();
	}

	public boolean onClose() {
		return true;
	}

	protected boolean onSavePrompt() {
		return true;
	}
}