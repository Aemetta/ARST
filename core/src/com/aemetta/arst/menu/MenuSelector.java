package com.aemetta.arst.menu;

import com.aemetta.arst.Arst;
import com.aemetta.arst.Controllable;
import com.badlogic.gdx.Preferences;

public class MenuSelector implements Controllable {
	
	Menu host;
	
	MenuLayout menu;
	boolean activated;
	boolean main;
	
	boolean specialInput;
	
	float autoRepeat;
	boolean moving;
	boolean movingDirection;
	boolean movingSideways;
	
	int DAS = 200;
	int ARR = 80;
	
	Preferences prefs;
	
	public static final int QUIT = 0;
	public static final int GAMEMODE = 1;
	public static final int ABOUT = 7;
	public static final int RESUME = 8;
	public static final int RESTART = 9;
	
	public static final int ENTER_MAIN_MENU = 99;
	public static final int UPDATE_CONTROLS = 100;
	public static final int UPDATE_THEME = 101;
	
	
	public MenuSelector(Menu menu) {
		this.host = menu;
		setActivated(true);
		
		newMenu(MenuLayout.Main);
	}
	
	public void input(int key, boolean pressed) {
		if(!pressed) {
			moving = false;
			return;
		}
		
		if(specialInput && currentItem() instanceof SettingItem) {
			((SettingItem)currentItem()).input(this, key);
			return;
		}

		if(key == Arst.MENU_LEFT) { currentItem().turn(this, false);
			movingDirection = false; movingSideways = true;
			moving = true; autoRepeat = (float)DAS / 1000;}
		else if(key == Arst.MENU_RIGHT) { currentItem().turn(this, true);
			movingDirection = true; movingSideways = true;
			moving = true; autoRepeat = (float)DAS / 1000;}
		else if(key == Arst.MENU_UP) { move(-1);
			movingDirection = false; movingSideways = false;
			moving = true; autoRepeat = (float)DAS / 1000;}
		else if(key == Arst.MENU_DOWN) { move(1);
			movingDirection = true; movingSideways = false;
			moving = true; autoRepeat = (float)DAS / 1000;}
		else if(key == Arst.MENU_SELECT) currentItem().select(this);
		else if(key == Arst.MENU_BACK) newMenu(Enum.valueOf(MenuLayout.class, menu.parent));
	}

	public void act(float delta) {
		if(!moving) return;
		autoRepeat -= delta;
		if(autoRepeat <= 0) {
			autoRepeat += (float)ARR / 1000;
			if(movingSideways)
				currentItem().turn(this, movingDirection);
			else
				move((movingDirection) ? 1 : -1);
		}
	}
	
	void newMenu(MenuLayout link) {
		
		if(menu == MenuLayout.P1Controls ||
					menu == MenuLayout.P2Controls || 
					menu == MenuLayout.MenuControls)
			host.handle(UPDATE_CONTROLS);
		else if(menu == MenuLayout.Theme) host.handle(UPDATE_THEME);
		
		menu = link;
		
		if(link == MenuLayout.Settings && prefs != null) prefs.flush();
			
		
		if(menu == MenuLayout.Main) {
			main = true;
			host.handle(ENTER_MAIN_MENU);
		}
		else main = false;

		if(!menu.initialized && prefs != null) menu.init(prefs);
	}
	
	private void move(int m) {
		setSelection(getSelection() + m);
		setSelection((getSelection() < 0) ? menu.items.length-1 : getSelection());
		setSelection((getSelection() >= menu.items.length) ? 0 : getSelection());
	}
	
	public void setMenu(MenuLayout m) {
		newMenu(m);
	}
	
	public void setPrefs(Preferences prefs) {
		this.prefs = prefs;
	}
	
	Item currentItem() {
		return menu.items[menu.selected];
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
	public Item getItem(int i) {
		return menu.items[i];
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
		return menu.selected;
	}

	/**
	 * @param selected The text item that's selected
	 */
	public void setSelection(int selected) {
		menu.selected = selected;
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
		return menu.items[i] instanceof SettingItem;
	}

	@Override
	public boolean isRawInput() {
		return specialInput;
	}
	
	public int numColumns(int i) {
		return menu.items[i].column.length;
	}
}
