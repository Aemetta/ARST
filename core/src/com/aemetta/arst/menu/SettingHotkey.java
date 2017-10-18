package com.aemetta.arst.menu;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;

public class SettingHotkey extends SettingItem {
	
	int key;
	int def;
	
	SettingHotkey(String label, String setting, int def) {
		super(label, setting);
		pushColumn("");
		this.def = def;
	}

	void load(Preferences prefs) {
		this.prefs = prefs;
		key = prefs.getInteger(name, def);
		column[1] = Keys.toString(key);
	}

	void save() {
		prefs.putInteger(name, key);
	}

	void select(MenuSelector host) {
		column[1] = "";
		host.specialInput = true;
		host.activated = false;
	}

	void input(MenuSelector host, int key) {
		host.specialInput = false;
		host.activated = true;
		this.key = key;
		column[1] = Keys.toString(key);
		prefs.putInteger(name, key);
	}

	void turn(MenuSelector host, boolean right) {}

}
