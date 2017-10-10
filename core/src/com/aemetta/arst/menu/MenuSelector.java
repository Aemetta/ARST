package com.aemetta.arst.menu;

import com.badlogic.gdx.Input.Keys;
import com.aemetta.arst.Arst;
import com.aemetta.arst.Controllable;
import com.badlogic.gdx.Preferences;

public class MenuSelector implements Controllable {
	
	private Menu host;
	
	private MenuItem menu;
	private int selected;
	private boolean activated;
	private boolean main;
	
	private boolean[] setting;
	int[] vals;
	String[] disp;
	
	boolean specialInput;
	
	Preferences prefs;
	
	public static final int QUIT = 0;
	public static final int SUBMENU = 1;
	public static final int GAMEMODE = 2;
	public static final int INTEGER = 3;
	public static final int SELECTOR = 4;
	public static final int SLIDER = 5;
	public static final int HOTKEY = 6;
	public static final int ABOUT = 7;
	
	
	public MenuSelector(Menu menu) {
		this.host = menu;
		setActivated(true);
		
		this.menu = MenuItem.Main;
		newMenu(false);
	}
	
	public void input(int key, boolean pressed) {
		if(pressed) {
			if(specialInput) {
				switch(menu.type[getSelection()]) {
				case HOTKEY:	specialInput = false;
								activated = true;
								vals[selected] = key;
								disp[selected] = Keys.toString(key);
								break;
				case INTEGER:	if(key == Keys.ENTER) {
									specialInput = false;
									activated = true;
									try { vals[selected] = 
											Integer.parseInt(disp[selected]); }
									catch(Exception e) { vals[selected] = 0; }
									disp[selected] = "" + key;
									prefs.putInteger(menu.items[selected], vals[selected]);
								} else if(key == Keys.BACKSPACE && disp[selected] != "")
									disp[selected] = disp[selected]
											.substring(disp[selected].length()-1);
								else if(key >= Keys.NUM_0 && key <= Keys.NUM_9)
									disp[selected] += Keys.toString(key);
				}
			} else {
				if(key == Arst.MENU_UP) move(-1);
				else if(key == Arst.MENU_DOWN) move(1);
				else if(key == Arst.MENU_SELECT) {
					switch(menu.type[getSelection()]) {
					case QUIT: host.handle(QUIT); break;
					case SUBMENU: newMenu(true); break;
					case GAMEMODE: host.handle(GAMEMODE); break;
					case INTEGER:
					case SELECTOR:
					case HOTKEY:	disp[selected] = "";
									specialInput = true;
									activated = false;
									break;
					};}
				else if(key == Arst.MENU_BACK) newMenu(false);
			}
		}
	}
	
	private void newMenu(boolean down) {
		if(down) {
			menu = MenuItem.valueOf(menu.items[getSelection()]);
			setSelection(0);
		}
		else {
			String backward = menu.name();
			menu = MenuItem.valueOf(menu.parent);
			setSelection(0);
			for(int i = 0; i < numberOfItems(); i++)
				if(backward == menu.items[i])
					setSelection(i);
		}
		
		if(menu == MenuItem.Main) main = true;
		else{
			main = false;
		}

		vals = new int[numberOfItems()];
		disp = new String[numberOfItems()];
		setting = new boolean[numberOfItems()];
		for(int i = 0; i < numberOfItems(); i++) {
			setting[i] = true;
			switch(menu.type[i]){
			case INTEGER: vals[i] = prefs.getInteger(menu.items[i]);
						disp[i] = "" + vals[i]; break;
			case SELECTOR: disp[i] = prefs.getString(menu.items[i]); break;
			case HOTKEY: vals[i] = prefs.getInteger(menu.items[i]); 
						disp[i] = Keys.toString(vals[i]); break;
			
			default: setting[i] = false; 
					disp[i] = "";
					break;
			}
		}
	}
	
	private void move(int m) {
		setSelection(getSelection() + m);
		setSelection((getSelection() < 0) ? menu.items.length-1 : getSelection());
		setSelection((getSelection() >= menu.items.length) ? 0 : getSelection());
	}
	
	public void setPrefs(Preferences prefs) {
		this.prefs = prefs;
	}

	/**
	 * @return Whether the current menu is the main one, to draw the logo
	 */
	public boolean isMainMenu() {
		return main;
	}

	/**
	 * @return The items in the menu
	 */
	public String[] getItems() {
		return menu.items;
	}
	
	/**
	 * @return How many items are in the menu
	 */
	public int numberOfItems() {
		return menu.items.length;
	}

	/**
	 * @return The text item that's selected
	 */
	public int getSelection() {
		return selected;
	}

	/**
	 * @param selected The text item that's selected
	 */
	public void setSelection(int selected) {
		this.selected = selected;
	}

	/**
	 * @return Whether one of the text items is selected
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * @param activated Whether one of the text items is selected
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	/**
	 * @return the setting
	 */
	public boolean isSetting(int i) {
		return setting[i];
	}
	
	public String getSetting(int i) {
		return disp[i];
	}
}
