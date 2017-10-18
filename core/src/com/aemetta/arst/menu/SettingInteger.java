package com.aemetta.arst.menu;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;

public class SettingInteger extends SettingItem {
	
	int value;
	int def;
	
	SettingInteger(String label, int def) {
		super(label, label);
		pushColumn("0");
		this.def = def;
	}

	@Override
	void load(Preferences prefs) {
		this.prefs = prefs;
		value = prefs.getInteger(name, def);
		column[1] = value + "";
	}

	@Override
	void select(MenuSelector host) {
		column[1] = "";
		host.specialInput = true;
		host.activated = false;
	}

	@Override
	void save() {
		prefs.putInteger(name, value);
	}

	@Override
	void input(MenuSelector host, int key) {
		if(key == Keys.ENTER) {
			host.specialInput = false;
			host.activated = true;
			try { value = 
					Integer.parseInt(column[1]); }
			catch(Exception e) { value = 0; }
			column[1] = "" + value;
			prefs.putInteger(name, value);
		} else if(key == Keys.BACKSPACE && !column[1].contentEquals(""))
			column[1] = column[1]
					.substring(0, column[1].length()-1);
		else if(key >= Keys.NUM_0 && key <= Keys.NUM_9)
			column[1] += Keys.toString(key);
	}

	void turn(MenuSelector host, boolean right) {
		value += (right) ? 5 : -5;
		column[1] = "" + value;
		prefs.putInteger(name, value);
	}

}
