package com.aemetta.arst.display;

import com.aemetta.arst.MenuSelector;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MenuDisplay implements Display {

	private boolean disposed = false;
	private MenuSelector ms;
	private Texture logo;
	
	private String path;
	
	public MenuDisplay(String p, MenuSelector ms, AssetManager manager) {
		this.ms = ms;
		this.path = "Menus/"+p+"/";
		
		manager.load(path + "logo.png", Texture.class);
	}
	
	public void init(AssetManager manager) {
		logo = manager.get(path + "logo.png", Texture.class);
	}

	public void draw(Batch batch, OrthographicCamera cam) {
		
		batch.draw(logo, 0, 0);
		
	}

	public void dispose() {
		logo.dispose();
	}

}
