package com.aemetta.arst.menu;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;

public class SettingTextEntry extends SettingItem {
	
	String def;
	
	SettingTextEntry(String label, String def) {
		super(label, label);
		pushColumn("");
		this.def = def;
	}
	
	@Override
	void load(Preferences prefs) {
		this.prefs = prefs;
		column[1] = prefs.getString(name, def);
	}

	@Override
	void select(MenuSelector host) {
		host.specialInput = true;
		host.activated = false;
	}

	@Override
	void save() {
		prefs.putString(name, column[1]);
	}

	@Override
	void input(MenuSelector host, int key) {
		if(key == Keys.ENTER) {
			host.specialInput = false;
			host.activated = true;
			column[1] = column[1];
			prefs.putString(name, column[1]);
		} else if(key == Keys.BACKSPACE && !column[1].contentEquals(""))
			column[1] = column[1]
					.substring(0, column[1].length()-1);
		else if(Keys.toString(key).length() == 1 && column[1].length() <= 15)
			column[1] += Keys.toString(key);
	}

	void turn(MenuSelector host, boolean right) {}

}
