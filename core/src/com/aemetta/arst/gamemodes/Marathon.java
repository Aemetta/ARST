package com.aemetta.arst.gamemodes;

import com.aemetta.arst.LevelTracker;
import com.aemetta.arst.Player;

public class Marathon extends Gamemode{
	
	private final float SPEED_DECAY = 0.7f;
	
	public Marathon() {
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		player1 = players[0];

		player1.setLevelTracker(new LevelTracker(player1, 1, 16, 10, true));
	}
	
	@Override
	public boolean handle(int event) {
		switch(event) {
		default: return false;
		case Player.LEVEL_UP:
			int fs = (int) (player1.getFallSpeed() * SPEED_DECAY);
			player1.setFallSpeed(fs);
			int ds = player1.getDropSpeed();
			player1.setDropSpeed((ds < fs/2) ? ds : fs/2);
			return true;
		}
	}
}
