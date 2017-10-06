package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Arst;
import com.aemetta.arst.player.LevelTracker;
import com.aemetta.arst.player.Player;
import com.aemetta.arst.player.Timer;

public class LineClear extends Gamemode {
	
	public LineClear(Arst arst) {
		super(arst);
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		human1 = players[0];

		players[0].setLevelTracker(new LevelTracker(players[0], 0, 1, 40, false));
		players[0].setTimer(new Timer(0));
		players[0].hideScore();
		players[0].hideLevels();
	}

}
