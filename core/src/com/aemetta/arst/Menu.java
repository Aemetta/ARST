package com.aemetta.arst;

import com.aemetta.arst.display.MenuDisplay;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu extends Wrapper {
	
	MenuSelector ms;
	
	public Menu() {
		ms = new MenuSelector();
		human1 = ms;
	}

	public void init() {
		manager = new AssetManager();
		manager.load("Backgrounds/017.png", Texture.class);
		
		displays = new MenuDisplay[1];
		displays[0] = new MenuDisplay("default", ms, manager);

		manager.finishLoading();
		background = manager.get("Backgrounds/017.png", Texture.class);
		displays[0].init(manager);
	}

	public void act(float delta) {
		
	}

	public void draw(SpriteBatch batch, OrthographicCamera cam) {
		displays[0].draw(batch, cam);
	}

	public void debug(int keycode) {
		
	}
	
}
