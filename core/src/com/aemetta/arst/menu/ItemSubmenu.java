package com.aemetta.arst.menu;

public class ItemSubmenu extends Item {
	
	String link;
	
	ItemSubmenu(String label) {
		super(label);
		this.link = label.replaceAll(" ", "");
	}

	void select(MenuSelector host) {
		host.newMenu(Enum.valueOf(MenuLayout.class, link));
	}

	void turn(MenuSelector host, boolean right) {}
}
