package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Arst;
import com.aemetta.arst.player.Player;
import com.aemetta.arst.player.Timer;

public class Ultra extends Gamemode {
	
	public Ultra(Arst arst) {
		super(arst);
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		human1 = players[0];

		players[0].setTimer(new Timer(120));
	}

}
