package com.aemetta.arst;

public class MenuSelector implements Controllable {
	
	private Menu host;
	
	private MenuItem menu;
	private int selected;
	private boolean activated;
	private boolean main;
	
	public static final int QUIT = 0;
	public static final int SUBMENU = 1;
	public static final int GAMEMODE = 2;
	public static final int INTEGER = 3;
	public static final int SELECTOR = 4;
	public static final int SLIDER = 5;
	public static final int HOTKEY = 6;
	public static final int ABOUT = 7;
	
	
	public MenuSelector(Menu menu) {
		this.menu = MenuItem.Main;
		this.host = menu;
		main = true;
		setActivated(true);
		
		setSelection(0);
	}
	
	public void input(int key, boolean pressed) {
		if(pressed) {
			if(key == Arst.MENU_UP) move(-1);
			if(key == Arst.MENU_DOWN) move(1);
			if(key == Arst.MENU_SELECT)
				switch(menu.type[getSelection()]) {
				case QUIT: host.handle(QUIT); break;
				case SUBMENU: newMenu(true); break;
				case GAMEMODE: host.handle(GAMEMODE); break;
				};
			if(key == Arst.MENU_BACK) newMenu(false);
		}
	}
	
	private void newMenu(boolean down) {
		if(down)
			menu = MenuItem.valueOf(menu.items[getSelection()]);
		else if(menu == MenuItem.Controls ||
				menu == MenuItem.Tuning ||
				menu == MenuItem.Theme ||
				menu == MenuItem.Audio)
				menu = MenuItem.Settings;
		else menu = MenuItem.Main;
		
		if(menu == MenuItem.Main) setMain(true); else setMain(false);
		setSelection(0);
	}
	
	private void move(int m) {
		setSelection(getSelection() + m);
		setSelection((getSelection() < 0) ? menu.items.length-1 : getSelection());
		setSelection((getSelection() >= menu.items.length) ? 0 : getSelection());
	}

	/**
	 * @return the main
	 */
	public boolean isMainMenu() {
		return main;
	}

	/**
	 * @param main the main to set
	 */
	public void setMain(boolean main) {
		this.main = main;
	}

	/**
	 * @return The items in the menu
	 */
	public String[] getItems() {
		return menu.items;
	}
	
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
}
