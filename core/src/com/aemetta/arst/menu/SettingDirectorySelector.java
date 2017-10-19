package com.aemetta.arst.menu;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

public class SettingDirectorySelector extends SettingItem {
	
	String def;
	String[] options;
	String directory;
	int selected = 0;
	
	SettingDirectorySelector(String label, String def, String directory) {
		super(label, label);
		this.def = def;
		FileHandle[] h = Gdx.files.getFileHandle(directory, FileType.Internal).list();
		options = new String[h.length];
		for(int i = 0; i < h.length; i++)
			options[i] = h[i].name();
		pushColumn(options[selected]);
	}

	void load(Preferences prefs) {
		this.prefs = prefs;
		column[1] = prefs.getString(name, def);
		selected = -1;
		for(int i = 0; i < options.length; i++) {
			if(column[1].contentEquals(options[i]))
				selected = i;
		}
		if(selected == -1) {
			selected = 0;
			column[1] = options[0];
		}
	}

	void save() {
		prefs.putString(name, column[1]);
	}
	void input(MenuSelector host, int key) {}
	void select(MenuSelector host) {}

	void turn(MenuSelector host, boolean right) {
		selected += (right) ? 1 : -1;
		selected = (selected >= options.length) ? 0 : selected;
		selected = (selected < 0) ? options.length - 1 : selected;
		
		column[1] = options[selected];
	}

}
