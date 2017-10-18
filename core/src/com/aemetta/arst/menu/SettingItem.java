package com.aemetta.arst.menu;

import com.badlogic.gdx.Preferences;

public abstract class SettingItem extends Item {
	
	String name;
	Preferences prefs;
	
	SettingItem(String label, String setting) {
		super(label);
		this.name = setting;
	}
	
	abstract void load(Preferences prefs);
	abstract void save();
	
	abstract void input(MenuSelector host, int key);
}
