package com.aemetta.arst.menu;

public class ItemGamemode extends Item {

	ItemGamemode(String label) {
		super(label);
	}

	void select(MenuSelector host) {
		host.host.handle(MenuSelector.GAMEMODE);
	}

	void turn(MenuSelector host, boolean right) {}

}
