package com.aemetta.arst.menu;

public class ItemFunction extends Item {

	int function;
	
	ItemFunction(String label, int function) {
		super(label);
		this.function = function;
	}

	void select(MenuSelector host) {
		host.host.handle(function);
	}

	void turn(MenuSelector host, boolean right) {}

}
