package com.aemetta.arst.gamemodes;

import com.aemetta.arst.LevelTracker;
import com.aemetta.arst.Player;
import com.aemetta.arst.Timer;

public class LineClear extends Gamemode {

	Player human;
	
	public LineClear() {
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		human = players[0];

		human.setLevelTracker(new LevelTracker(human, 0, 1, 40, false));
		human.setTimer(new Timer(0));
		human.hideScore();
		human.hideLevels();
	}

}
