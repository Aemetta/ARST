package com.aemetta.arst.gamemodes;

import com.aemetta.arst.Player;
import com.aemetta.arst.Renderer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Gamemode {
	
	public AssetManager manager;
	
	public Texture background;
	public Renderer[] displays;
	
	public Player players[];
	public Player player1;
	public Player player2;
	
	public void init() {
		manager = new AssetManager();
		manager.load("Backgrounds/017.png", Texture.class);
		
		displays = new Renderer[1];
		displays[0] = new Renderer("purple-20", "candy-20", players, manager);

		manager.finishLoading();
		background = manager.get("Backgrounds/017.png", Texture.class);
		displays[0].init(manager);
	}
	
	public void draw(SpriteBatch batch, OrthographicCamera cam) {
		if(background != null)
			batch.draw(background, -background.getWidth()/2, -background.getHeight()/2);
		for(Renderer d : displays)
			d.draw(batch, cam);
	}
	
	public void act(float delta) {
		for(Player p : players)
			p.act(delta);
	}
	
	public boolean handle(int event) {
		return false;
	}
}
