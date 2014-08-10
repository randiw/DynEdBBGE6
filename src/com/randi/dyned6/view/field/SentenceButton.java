package com.randi.dyned6.view.field;

import net.rim.device.api.ui.component.ButtonField;

public class SentenceButton extends ButtonField {

	public SentenceButton(String label) {
		super(label, ButtonField.CONSUME_CLICK | FIELD_HCENTER | FIELD_VCENTER);
	}
	
	public void setDirty(boolean dirty) {
	}

	public void setMuddy(boolean muddy) {
	}
}