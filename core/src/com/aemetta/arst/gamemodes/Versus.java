package com.aemetta.arst.gamemodes;

import com.aemetta.arst.player.MapLoader;
import com.aemetta.arst.player.Player;

public class Versus extends Gamemode {
	
	public Versus() {
		players = new Player[2];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(this, seed, MapLoader.load("Maps/Untitled.png"));
		players[1] = new Player(this, seed, MapLoader.load("Maps/Untitled.png"));
		human1 = players[0];
		human2 = players[1];
		players[0].score.setTarget(players[1]);
		players[1].score.setTarget(players[0]);
		players[0].hideScore();
		players[1].hideScore();
	}

}
