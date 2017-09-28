package com.aemetta.arst.gamemodes;

import com.aemetta.arst.LevelTracker;
import com.aemetta.arst.Player;

public class Marathon extends Gamemode{
	
	Player human;
	
	public Marathon() {
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(seed);
		human = players[0];

		human.setLevelTracker(new LevelTracker(1, 15, 10, true));
	}

	@Override
	public void act(float delta) {
		human.act(delta);
	}

	@Override
	public void setInput(int key, boolean pressed) {
		human.input(key, pressed);
	}
	
}
