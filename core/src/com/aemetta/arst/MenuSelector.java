package com.aemetta.arst;

public class MenuSelector implements Controllable {
	
	String[] items;
	int selected;
	
	public MenuSelector() {
		items = new String[5];
		items[0] = "Sinpleplayer";
		items[1] = "Hotseat";
		items[2] = "Settings";
		items[3] = "About";
		items[4] = "Quit";
		
		selected = 0;
	}
	
	public void input(int key, boolean pressed) {
		
	}
	
	
	
}
