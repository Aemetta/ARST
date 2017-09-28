package com.aemetta.arst.gamemodes;

import com.aemetta.arst.MapLoader;
import com.aemetta.arst.Player;

public class Versus extends Gamemode {

	Player human;
	Player human2; //Second player for hotseat games
	
	public Versus() {
		players = new Player[2];
		
		long seed = Double.doubleToLongBits(Math.random());
		players[0] = new Player(seed, MapLoader.load("Maps/Untitled.png"));
		players[1] = new Player(seed, MapLoader.load("Maps/Untitled.png"));
		human = players[0];
		human2 = players[1];
		players[0].score.setTarget(players[1]);
		players[1].score.setTarget(players[0]);
	}
	
	@Override
	public void act(float delta) {
		for(int i = 0; i < players.length; i++)
			players[i].act(delta);
	}

	@Override
	public void setInput(int key, boolean pressed) {
		human.input(key, pressed);
	}

}
