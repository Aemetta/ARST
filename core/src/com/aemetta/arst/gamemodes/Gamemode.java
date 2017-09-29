package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Player;

public abstract class Gamemode {
	
	public Player players[];
	
	public abstract void act(float delta);
	public abstract void setInput(int key, boolean pressed);
	public boolean handle(int event) {
		return false;
	}
}
