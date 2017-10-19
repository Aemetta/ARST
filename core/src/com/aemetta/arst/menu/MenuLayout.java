package com.aemetta.arst.menu;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;

public enum MenuLayout {
	Main(
			new Item[] {
					new ItemSubmenu("Singleplayer"),
					new ItemSubmenu("Hotseat"),
					new ItemSubmenu("High Scores"),
					new ItemSubmenu("Settings"),
					new ItemFunction("About", MenuSelector.ABOUT),
					new ItemFunction("Quit", MenuSelector.QUIT)
			}, "Main"),
	Singleplayer(
			new Item[] {
					new ItemGamemode("Marathon"),
					new ItemGamemode("Line Clear"),
					new ItemGamemode("Ultra"),
					new ItemGamemode("Cheese")
			}, "Main"),
	Hotseat(
			new Item[] {
					new ItemGamemode("Versus")
			}, "Main"),
	HighScores(
			new Item[] {
					new ItemFunction("not yet, sorry", MenuSelector.ABOUT)
			}, "Main"),
	Settings(
			new Item[] {
					new SettingTextEntry("Name", "Anon"),
					new ItemSubmenu("P1 Controls"),
					new ItemSubmenu("P2 Controls"),
					new ItemSubmenu("Menu Controls"),
					new ItemSubmenu("Tuning"),
					new ItemSubmenu("Theme"),
					new ItemSubmenu("Audio")
			}, "Main"),
	P1Controls(
			new Item[] {
					new SettingHotkey("Left", "Controls P1 Left", Keys.LEFT),
					new SettingHotkey("Right", "Controls P1 Right", Keys.RIGHT),
					new SettingHotkey("Hard Drop", "Controls P1 Hard Drop", Keys.UP),
					new SettingHotkey("Soft Drop", "Controls P1 Soft Drop", Keys.DOWN),
					new SettingHotkey("Rotate Left", "Controls P1 Rotate Left", Keys.Z),
					new SettingHotkey("Rotate Right", "Controls P1 Rotate Right", Keys.X),
					new SettingHotkey("Rotate 180", "Controls P1 Rotate 180", Keys.S),
					new SettingHotkey("Hold", "Controls P1 Hold", Keys.C),
					new SettingHotkey("Deploy", "Controls P1 Deploy", Keys.V)
			}, "Settings"),
	P2Controls(
			new Item[] {
					new SettingHotkey("Left", "Controls P2 Left", Keys.UNKNOWN),
					new SettingHotkey("Right", "Controls P2 Right", Keys.UNKNOWN),
					new SettingHotkey("Hard Drop", "Controls P2 Hard Drop", Keys.UNKNOWN),
					new SettingHotkey("Soft Drop", "Controls P2 Soft Drop", Keys.UNKNOWN),
					new SettingHotkey("Rotate Left", "Controls P2 Rotate Left", Keys.UNKNOWN),
					new SettingHotkey("Rotate Right", "Controls P2 Rotate Right", Keys.UNKNOWN),
					new SettingHotkey("Rotate 180", "Controls P2 Rotate 180", Keys.UNKNOWN),
					new SettingHotkey("Hold", "Controls P2 Hold", Keys.UNKNOWN),
					new SettingHotkey("Deploy", "Controls P2 Deploy", Keys.UNKNOWN)
			}, "Settings"),
	MenuControls(
			new Item[] {
					new SettingHotkey("Left", "Controls Menu Left", Keys.LEFT),
					new SettingHotkey("Right", "Controls Menu Right", Keys.RIGHT),
					new SettingHotkey("Up", "Controls Menu Up", Keys.UP),
					new SettingHotkey("Down", "Controls Menu Down", Keys.DOWN),
					new SettingHotkey("Select", "Controls Menu Select", Keys.ENTER),
					new SettingHotkey("Back", "Controls Menu Back", Keys.ESCAPE),
					new SettingHotkey("Pause", "Controls Pause", Keys.ESCAPE),
			}, "Settings"),
	Tuning(
			new Item[] {
					new ItemGamemode("Test"),
					new SettingInteger("P1 DAS", 200),
					new SettingInteger("P1 ARR", 80, 0, 500),
					new SettingInteger("P1 Drop", 50, 0, 500),
					new SettingInteger("P2 DAS", 200),
					new SettingInteger("P2 ARR", 80, 0, 500),
					new SettingInteger("P2 Drop", 50, 0, 500)
			}, "Settings"),
	Theme(
			new Item[] {
					new SettingDirectorySelector("Menu", "default", new String[] {"default"}),
					new SettingDirectorySelector("Playfield", "purple-20", new String[] {"green-20", "purple-20"}),
					new SettingDirectorySelector("Minos", "candy-20", new String[] {"candy-20", "test-20", "graphite-20"}),
					new SettingDirectorySelector("Background", "aemetta", new String[] {"aemetta"})
			}, "Settings"),
	Audio(
			new Item[] {
					new SettingInteger("Master", 100, 0, 100),
					new SettingInteger("SFX", 100, 0, 100),
					new SettingInteger("Music", 100, 0, 100)
			}, "Settings"),
	Pause(
			new Item[] {
					new ItemFunction("Resume", MenuSelector.RESUME),
					new ItemFunction("Restart", MenuSelector.RESTART),
					new ItemSubmenu("Main")
			}, "Pause");
	
	String parent;
	Item[] items;
	
	boolean initialized = false;
	int selected = 0;
	
	MenuLayout(Item[] items, String parent){
		this.items = items;
		this.parent = parent;
	}
	
	public static void fullyInit(Preferences prefs) {
		MenuLayout.HighScores.init(prefs);
		MenuLayout.Settings.init(prefs);
		
		MenuLayout.P1Controls.init(prefs);
		MenuLayout.P2Controls.init(prefs);
		MenuLayout.MenuControls.init(prefs);
		MenuLayout.Tuning.init(prefs);
		MenuLayout.Theme.init(prefs);
		MenuLayout.Audio.init(prefs);
	}
	
	public void init(Preferences prefs){
		for(Item i : items)
			if(i instanceof SettingItem) {
				((SettingItem) i).load(prefs);
				((SettingItem) i).save();
			}
		initialized = true;
	}
}
