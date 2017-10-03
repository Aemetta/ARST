package com.aemetta.arst.gamemodes;

import com.aemetta.arst.player.MapLoader;
import com.aemetta.arst.player.Player;

public class Versus extends Gamemode {
	
	public Versus() {
		players = new Player[2];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed, MapLoader.load("Maps/Untitled.png"));
		players[1] = new Player(this, seed, MapLoader.load("Maps/Untitled.png"));
		player1 = players[0];
		player2 = players[1];
		player1.score.setTarget(player2);
		player2.score.setTarget(player1);
		player1.hideScore();
		player2.hideScore();
	}

}
