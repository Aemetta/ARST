package com.aemetta.arst.player;

public enum Color {
		Empty(0),
		Cyan(1),
		Yellow(2),
		Magenta(3),
		Red(4),
		Green(5),
		Blue(6),
		Orange(7),
		Ghost(8),
		Light(9),
		Dark(10),
		Brown(11),
		White(12);
		
	public final int code;
	
		Color(int code){
			this.code = code;
		}
}
