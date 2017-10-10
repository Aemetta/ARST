package com.aemetta.arst.menu;

public enum MenuItem {
	Main(
			new String[] {
					"Singleplayer",
					"Hotseat",
					"Network",
					"Settings",
					"About",
					"Quit"
			}, new int[] {
					MenuSelector.SUBMENU,
					MenuSelector.SUBMENU,
					MenuSelector.SUBMENU,
					MenuSelector.SUBMENU,
					MenuSelector.ABOUT,
					MenuSelector.QUIT,
			}, "Main"),
	Singleplayer(
			new String[] {
					"Marathon",
					"Line Clear",
					"Ultra",
					"Cheese"
			}, new int[] {
					MenuSelector.GAMEMODE,
					MenuSelector.GAMEMODE,
					MenuSelector.GAMEMODE,
					MenuSelector.GAMEMODE
			}, "Main"),
	Hotseat(
			new String[] {
					"Versus"
			}, new int[] {
					MenuSelector.GAMEMODE
			}, "Main"),
	Network(new String[] {}, new int[] {}, "Main"),
	Settings(
			new String[] {
					"Test",
					"Controls",
					"Tuning",
					"Theme",
					"Audio"
			}, new int[] {
					MenuSelector.GAMEMODE,
					MenuSelector.SUBMENU,
					MenuSelector.SUBMENU,
					MenuSelector.SUBMENU,
					MenuSelector.SUBMENU
			}, "Main"),
	Controls(
			new String[] {
					"Menu Left",
					"Menu Right",
					"Menu Up",
					"Menu Down",
					"Menu Select",
					"Menu Back",
					"Left",
					"Right",
					"Rotate Left",
					"Rotate Right",
					"Rotate 180",
					"Hard Drop",
					"Soft Drop",
					"Hold",
					"Deploy"
			}, new int[] {
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY,
					MenuSelector.HOTKEY
			}, "Settings"),
	Tuning(
			new String[] {
					"Test",
					"P1 DAS",
					"P1 ARR",
					"P1 Drop",
					"P2 DAS",
					"P2 ARR",
					"P2 Drop"
			}, new int[] {
					MenuSelector.GAMEMODE,
					MenuSelector.INTEGER,
					MenuSelector.INTEGER,
					MenuSelector.INTEGER,
					MenuSelector.INTEGER,
					MenuSelector.INTEGER,
					MenuSelector.INTEGER
			}, "Settings"),
	Theme(
			new String[] {
					"Menu",
					"Minos",
					"Playfield"
			}, new int[] {
					MenuSelector.SELECTOR,
					MenuSelector.SELECTOR,
					MenuSelector.SELECTOR
			}, "Settings"),
	Audio(
			new String[] {
					"Master",
					"SFX",
					"Music"
			}, new int[] {
					MenuSelector.SLIDER,
					MenuSelector.SLIDER,
					MenuSelector.SLIDER
			}, "Settings");
	
	public String parent;
	public String[] items;
	public int[] type;
	
	MenuItem(String[] items, int[] type, String parent){
		this.items = items;
		this.type = type;
		this.parent = parent;
	}
}
