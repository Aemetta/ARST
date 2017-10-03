package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Player;
import com.aemetta.arst.Timer;

public class Ultra extends Gamemode {
	
	public Ultra() {
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		player1 = players[0];

		player1.setTimer(new Timer(120));
	}

}
