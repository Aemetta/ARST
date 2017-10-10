package com.aemetta.arst.display;

import com.aemetta.arst.menu.MenuSelector;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MenuDisplay implements Display {

	private boolean disposed = false;
	private MenuSelector ms;
	
	private Texture logo;
	private BitmapFont font;
	private BitmapFont select;
	
	private String path;
	
	public MenuDisplay(String p, MenuSelector ms, AssetManager manager) {
		this.ms = ms;
		this.path = "Menus/"+p+"/";
		
		manager.load(path + "logo.png", Texture.class);
		manager.load(path + "/fonts/normal.fnt", BitmapFont.class);
		manager.load(path + "/fonts/selected.fnt", BitmapFont.class);
	}
	
	public void init(AssetManager manager) {
		logo = manager.get(path + "logo.png", Texture.class);
		font = manager.get(path + "/fonts/normal.fnt", BitmapFont.class);
		select = manager.get(path + "/fonts/selected.fnt", BitmapFont.class);
	}

	public void draw(Batch batch, OrthographicCamera cam) {
		if(disposed) return;
		
		if(ms.isMainMenu()) batch.draw(logo, -logo.getWidth()/2, 100);
		
		for(int i = 0; i < ms.numberOfItems(); i++) {
			if(i==ms.getSelection() && ms.isActivated()) continue;
			drawText(batch, font, i, ms.isMainMenu(), ms.isSetting(i));
		}
		if(ms.isActivated())
			drawText(batch, select, ms.getSelection(),
					ms.isMainMenu(), ms.isSetting(ms.getSelection()));
	}
	
	private void drawText(Batch batch, BitmapFont f, int i, boolean main, boolean setting){
		if(setting) {
			f.draw(batch, ms.getItems()[i].toLowerCase(),
				-250, -50*((main) ? i : i-ms.numberOfItems()/2),
				500, -1, false);
			f.draw(batch, ms.getSetting(i).toLowerCase(),
					-250, -50*((main) ? i : i-ms.numberOfItems()/2),
					500, 2, false);
		} else {
			f.draw(batch, ms.getItems()[i].toLowerCase(),
					-100, -50*((main) ? i : i-ms.numberOfItems()/2),
					200, 1, false);
		}
	}

	public void dispose() {
		logo.dispose();
		disposed = true;
	}

}
