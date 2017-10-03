package com.aemetta.arst.gamemodes;

import com.aemetta.arst.player.LevelTracker;
import com.aemetta.arst.player.Player;
import com.aemetta.arst.player.Timer;

public class LineClear extends Gamemode {
	
	public LineClear() {
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		player1 = players[0];

		player1.setLevelTracker(new LevelTracker(player1, 0, 1, 40, false));
		player1.setTimer(new Timer(0));
		player1.hideScore();
		player1.hideLevels();
	}

}
