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
	
	private int scroll;
	private int scope;
	private int maxScope = 10;
	
	public MenuDisplay(String p, MenuSelector ms, AssetManager manager) {
		this.ms = ms;
		this.path = "Menus/"+p+"/";
		
		manager.load(path + "logo.png", Texture.class);
		manager.load(path + "/fonts/normal.fnt", BitmapFont.class);
		manager.load(path + "/fonts/selected.fnt", BitmapFont.class);
		
		this.scroll = 0;
		this.scope = 0;
	}
	
	public void init(AssetManager manager) {
		logo = manager.get(path + "logo.png", Texture.class);
		font = manager.get(path + "/fonts/normal.fnt", BitmapFont.class);
		select = manager.get(path + "/fonts/selected.fnt", BitmapFont.class);
	}

	public void draw(Batch batch, OrthographicCamera cam) {
		if(disposed) return;
		
		if(ms.isMainMenu()) batch.draw(logo, -logo.getWidth()/2, 100);
		
		scope = (ms.numberOfItems() < maxScope) ? ms.numberOfItems() : maxScope;
		
		int sel = ms.getSelection();
		if(sel < scroll) scroll = sel;
		else if(sel >= scroll + scope) scroll = sel - scope + 1;
		
		if(scroll + scope > ms.numberOfItems()) scroll = 0;
		
		for(int i = scroll; i < scope+scroll; i++) {
			if(i==sel && ms.isActivated()) continue;
			drawText(batch, font, i, ms.isMainMenu(), ms.numColumns(i));
		}
		if(ms.isActivated())
			drawText(batch, select, sel,
					ms.isMainMenu(), ms.numColumns(sel));
	}
	
	private void drawText(Batch batch, BitmapFont f, int i, boolean main, int columns){
		for(int j = 0; j < columns; j++) {
			f.draw(batch, 
					ms.getItem(i)
					.getColumn(j)
					.toLowerCase(),
				-250, -50*((main) ? i-scroll-1 : i-scroll-scope/2), 500, 
				(columns == 1) ? 1 :
				(columns == 2) ? (j == 0) ? -1 : 0 :
				(columns == 3) ? (j == 0) ? -1 : (j == 1) ? 1 : 0 :
					0,
				false);
		}
	}

	public void dispose() {
		logo.dispose();
		disposed = true;
	}

}
