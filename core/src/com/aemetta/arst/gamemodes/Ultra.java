package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Player;
import com.aemetta.arst.Timer;

public class Ultra extends Gamemode {

	Player human;
	
	public Ultra() {
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(seed);
		human = players[0];

		human.setTimer(new Timer(120));
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
