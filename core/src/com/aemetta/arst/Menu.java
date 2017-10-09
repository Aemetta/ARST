package com.aemetta.arst;

import com.aemetta.arst.display.Display;
import com.aemetta.arst.display.MenuDisplay;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu extends Wrapper {
	
	MenuSelector ms;
	
	public Menu(Arst arst) {
		super(arst);
		ms = new MenuSelector(this);
		human1 = ms;
	}

	public void init(Preferences prefs) {
		manager = new AssetManager();
		manager.load("Backgrounds/017.png", Texture.class);
		
		displays = new Display[1];
		displays[0] = new MenuDisplay("default", ms, manager);

		manager.finishLoading();
		background = manager.get("Backgrounds/017.png", Texture.class);
		displays[0].init(manager);
		
		ms.setPrefs(prefs);
	}

	public void draw(SpriteBatch batch, OrthographicCamera cam) {
		if(background != null)
			batch.draw(background, -background.getWidth()/2, -background.getHeight()/2);
		displays[0].draw(batch, cam);
	}

	public boolean handle(int event) {
		switch(event) {
		case MenuSelector.QUIT: Gdx.app.exit(); break;
		case MenuSelector.GAMEMODE: arst.newGame(ms.getItems()[ms.getSelection()].toLowerCase()); break;
		default: return false;
		}
		return true;
	}
	
	public void act(float delta) {}
	public void debug(int keycode) {}
}
