package com.aemetta.arst.gamemodes;

import com.aemetta.arst.LevelTracker;
import com.aemetta.arst.Player;

public class Marathon extends Gamemode{
	
	Player human;
	
	public Marathon() {
		
		players = new Player[1];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed);
		human = players[0];

		human.setLevelTracker(new LevelTracker(human, 1, 16, 10, true));
	}

	@Override
	public void act(float delta) {
		human.act(delta);
	}

	@Override
	public void setInput(int key, boolean pressed) {
		human.input(key, pressed);
	}
	
	@Override
	public boolean handle(int event) {
		switch(event) {
		default: return false;
		case Player.LEVEL_UP:
			int fs = (int) (human.getFallSpeed() * 0.8);
			human.setFallSpeed(fs);
			int ds = human.getDropSpeed();
			human.setDropSpeed((ds < fs/2) ? ds : fs/2);
			return true;
		}
	}
}
