package com.aemetta.arst;

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
			}),
	Singleplayer(
			new String[] {
					"Marathon",
					"Line Clear",
					"Ultra"
			}, new int[] {
					MenuSelector.GAMEMODE,
					MenuSelector.GAMEMODE,
					MenuSelector.GAMEMODE
			}),
	Hotseat(
			new String[] {
					"VERSUS"
			}, new int[] {
					MenuSelector.GAMEMODE
			}),
	Network(new String[] {}, new int[] {}),
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
			}),
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
			}),
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
			}),
	Theme(
			new String[] {
					"Menu",
					"Minos",
					"Playfield"
			}, new int[] {
					MenuSelector.SELECTOR,
					MenuSelector.SELECTOR,
					MenuSelector.SELECTOR
			}),
	Audio(
			new String[] {
					"Master",
					"SFX",
					"Music"
			}, new int[] {
					MenuSelector.SLIDER,
					MenuSelector.SLIDER,
					MenuSelector.SLIDER
			});
	
	public String[] items;
	public int[] type;
	
	MenuItem(String[] items, int[] type){
		this.items = items;
		this.type = type;
	}
}
