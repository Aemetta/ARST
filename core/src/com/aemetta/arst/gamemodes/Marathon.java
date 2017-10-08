package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Arst;
import com.aemetta.arst.player.LevelTracker;
import com.aemetta.arst.player.Player;

public class Marathon extends Gamemode{
	
	private final float SPEED_DECAY = 0.7f;
	
	public Marathon(Arst arst) {
		super(arst);
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		human1 = players[0];

		players[0].setLevelTracker(new LevelTracker(players[0], 1, 16, 10, true));
	}
	
	@Override
	public boolean handle(Player p, int event) {
		switch(event) {
		default: return super.handle(p, event);
		case Player.LEVEL_UP:
			int fs = (int) (p.getFallSpeed() * SPEED_DECAY);
			p.setFallSpeed(fs);
			int ds = p.getDropSpeed();
			p.setDropSpeed((ds < fs/2) ? ds : fs/2);
			return true;
		}
	}
}
