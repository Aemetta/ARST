package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Arst;
import com.aemetta.arst.Wrapper;
import com.aemetta.arst.display.Display;
import com.aemetta.arst.display.PlayerDisplay;
import com.aemetta.arst.player.Player;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Gamemode extends Wrapper {
	
	public Player[] players;
	
	public Gamemode(Arst arst) {
		super(arst);
	}
	
	public void init() {
		manager = new AssetManager();
		manager.load("Backgrounds/017.png", Texture.class);
		
		displays = new PlayerDisplay[1];
		displays[0] = new PlayerDisplay("purple-20", "candy-20", players, manager);

		manager.finishLoading();
		background = manager.get("Backgrounds/017.png", Texture.class);
		displays[0].init(manager);
	}
	
	public void draw(SpriteBatch batch, OrthographicCamera cam) {
		if(background != null)
			batch.draw(background, -background.getWidth()/2, -background.getHeight()/2);
		for(Display d : displays)
			d.draw(batch, cam);
	}
	
	public void act(float delta) {
		for(Player p : players)
			p.act(delta);
	}
	
	public boolean handle(Player p, int event) {
		if(event==Player.FINISHED)
			arst.newGame("menu");
		return false;
	}

	public void debug(int keycode) {
		if(keycode == Keys.F2)
			players[0].getLevelTracker().clearLines(5);
	}
}
