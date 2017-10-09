package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Arst;
import com.aemetta.arst.player.Player;
import com.aemetta.arst.player.Timer;

public class Cheese extends Gamemode {
	
	public Cheese(Arst arst) {
		super(arst);
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		human1 = players[0];

		players[0].setTimer(new Timer(0));
		players[0].hideScore();
		
		for(int i = 0; i < 10; i++)
			players[0].garbage.add(1);
		
		players[0].garbage.fill();
		players[0].matrix.smooth(0, 10);
	}

}
