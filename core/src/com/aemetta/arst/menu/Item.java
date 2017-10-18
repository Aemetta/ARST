package com.aemetta.arst.menu;

import java.util.Arrays;

public abstract class Item {
	
	String[] column;
	
	Item(String label) {
		this.column = new String[]{label};
	}
	
	abstract void select(MenuSelector host);
	abstract void turn(MenuSelector host, boolean right);
	
	void pushColumn(String value) {
		column = Arrays.copyOf(column, column.length+1);
		column[column.length-1] = value;
	}
	
	public String getColumn(int i) {
		return column[i];
	}
}
