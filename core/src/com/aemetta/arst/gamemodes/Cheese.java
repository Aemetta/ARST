package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Arst;
import com.aemetta.arst.player.Garbage;
import com.aemetta.arst.player.Matrix;
import com.aemetta.arst.player.Player;
import com.aemetta.arst.player.Popup;
import com.aemetta.arst.player.Timer;

public class Cheese extends Gamemode {
	
	public Cheese(Arst arst) {
		super(arst);
		long seed = Double.doubleToLongBits(Math.random());
		
		Matrix m = new Matrix();
		Garbage g = new Garbage(m, seed);
		
		for(int i = 0; i < 10; i++)
			g.add(1);
		
		g.fill();
		m.smooth(0, 9);
		m.recolor(2, 0, 0, m.getWidth(), 10);
		m.setSpecial(0, 0);
		
		players = new Player[1];
		
		players[0] = new Player(this, seed, m);
		human1 = players[0];

		players[0].setTimer(new Timer(0));
		players[0].hideScore();
	}
	
	@Override
	public boolean handle(Player p, int event) {
		switch(event) {
		default: return super.handle(p, event);
		case Player.SPECIAL:
			p.endGame(Popup.END_NEUTRAL);
			return true;
		}
	}
}
