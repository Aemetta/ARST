package com.aemetta.arst;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;

public class Renderer implements Disposable {
	
	private boolean disposed = false;
	private Player players[];
	
	private String fieldpath;
	private String minopath;
	
	private MinoConfig mino;
	private PlayfieldConfig playfield;
	
	private Texture minosheet;
	private Texture[] blocks;
	
	private Texture background;
	private Texture warning;
	
	private BitmapFont scorefont;
	private BitmapFont timefont;
	private BitmapFont levelfont;
	private BitmapFont linesfont;
	
	int[] blockref;
	
	public Renderer(String fpath, String mpath, Player[] pl, AssetManager manager) {

		players = pl;
		
		this.minopath = "Minos/"+mpath+"/";
		this.fieldpath = "Playfields/"+fpath+"/";
		
		Json json = new Json();
		mino = json.fromJson(MinoConfig.class,
				Gdx.files.internal(minopath+"mino.json"));
		playfield = json.fromJson(PlayfieldConfig.class,
				Gdx.files.internal(fieldpath+"playfield.json"));
		
		if(mino.size != playfield.minoSize)
			System.err.println("Playfield and mino theme size don't match!");
		
		if(mino.randomized) {
			for(Player p : players) {
				p.piece.randomized = true;
				p.piece.randomamount = mino.randomamount;
				p.piece.randomperpiece = mino.randomPerPiece;
			}
		}
		
		
		blockref = new int[playfield.blockcoords.length];
		for(int i = 0; i < playfield.blockcoords.length; i++){
			blockref[i] = 0;
			for(int j = 0; j < mino.blocksize.length; j++){
				if(playfield.blockcoords[i][3] == mino.blocksize[j]) blockref[i] = j;
			}
		}
		
		for(int i = 0; i < players.length; i++) {
			players[i].xoffset = (int) ((((players.length-1)/2f-i)*1.1)*playfield.width);
			players[i].yoffset = 0;
		}
		
		manager.load(minopath+mino.path, Texture.class);
		manager.load(fieldpath+playfield.path, Texture.class);
		manager.load(fieldpath+playfield.warningPath, Texture.class);
		for(int i = 0; i < mino.blockpath.length; i++)
			manager.load(minopath+mino.blockpath[i], Texture.class);
		
		manager.load(fieldpath + playfield.popupPath, TextureAtlas.class);
		
		manager.load(fieldpath + playfield.score.fontPath, BitmapFont.class);
		manager.load(fieldpath + playfield.time.fontPath, BitmapFont.class);
		manager.load(fieldpath + playfield.level.fontPath, BitmapFont.class);
		manager.load(fieldpath + playfield.lines.fontPath, BitmapFont.class);
	}
	
	public void init(AssetManager manager) {
		
		minosheet = manager.get(minopath+mino.path, Texture.class);
		
		background = manager.get(fieldpath+playfield.path, Texture.class);
		warning = manager.get(fieldpath+playfield.warningPath, Texture.class);

		blocks = new Texture[mino.blockpath.length];
		blo: for(int i = 0; i < mino.blockpath.length; i++)
			for(int j : blockref)
				if(i == j){
					blocks = Arrays.copyOf(blocks,blocks.length+1);
					blocks[i] = manager.get(minopath+mino.blockpath[i], Texture.class);
					continue blo;
				}
		
		scorefont = manager.get(fieldpath + playfield.score.fontPath, BitmapFont.class);
		timefont = manager.get(fieldpath + playfield.time.fontPath, BitmapFont.class);
		levelfont = manager.get(fieldpath + playfield.level.fontPath, BitmapFont.class);
		linesfont = manager.get(fieldpath + playfield.lines.fontPath, BitmapFont.class);
		
		TextureAtlas ta = manager.get(fieldpath + playfield.popupPath, TextureAtlas.class);
		
		for(Player p : players)
			p.popup.setAtlas(ta);
		
	}
	
	/*
	 * Drawing each playfield is now handled outside the player object.
	 * This is more efficient.
	 * For each element of the display, it iterates through
	 * each player and draws it, requiring less texture
	 * updates in the GPU.
	 */
	public void draw(Batch batch, OrthographicCamera cam) {
		
		if(disposed) return;
		
		for(int i = 0; i < 10; i++)
			for(Player p : players) {
				
				int w = playfield.width/2 + p.xoffset;
				int h = playfield.height/2 + p.yoffset;
				cam.translate(w,h);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
				
				switch(i) {
				case 0: background(batch, p); break;
				case 1: queue(batch, p); break;
				case 2: garbage(batch, p); break;
				case 3: if(p.hasScore())
							text(batch, p, scorefont, playfield.score,
							Integer.toString(p.score.score)); break;
				case 4: if(p.hasTimer())
							text(batch, p, timefont, playfield.time,
							p.timer.view()); break;
				case 5: if(p.hasLineTracker())
							text(batch, p, linesfont, playfield.lines,
							Integer.toString(p.level.getLines())); break;
				case 6: if(p.hasLevelTracker())
							text(batch, p, levelfont, playfield.level,
							Integer.toString(p.level.getLevel())); break;
				case 7: popup(batch, p); break;
				case 8: combo(batch, p); break;
				case 9: minos(batch, p); break;
				}
				cam.translate(-w,-h);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
			}
	}
	
	private void background(Batch batch, Player p) {
		batch.draw(background, playfield.imageOffsetX, playfield.imageOffsetY);
	}
		
	private void minos(Batch batch, Player p) {
		for(int x = 0; x < p.matrix.WIDTH; x++)
			for(int y = 0; y < p.matrix.HEIGHT-p.matrix.TOP; y++)
			{
				if(p.matrix.getColor(x, y)==0) continue;
				if(p.matrix.getShape(x, y)==null) continue;
				Sprite sprite;
				if(p.matrix.hasUpdated(x, y)) {
					int dspx = x * mino.size + playfield.matrixOffsetX;
					int dspy = y * mino.size + playfield.matrixOffsetY;
					int srcx = 0;
					int srcy = 0;
					
					if(mino.colored)
						srcy = p.matrix.getColor(x,y);
					
					if(mino.randomized){
						srcx = p.matrix.getTexture(x,y);
					}
					
					if(mino.shaped){
						srcx = srcx*4+p.matrix.getShape(x,y).x;
						srcy = srcy*4+p.matrix.getShape(x,y).y;
					}
					
					srcx *= mino.size;
					srcy *= mino.size;
					
					sprite = new Sprite(minosheet, srcx, srcy, mino.size, mino.size);
					sprite.setBounds(dspx, dspy, mino.size, mino.size);
					p.matrix.setSprite(x, y, sprite);
					if(!mino.colored)
						sprite.setColor(mino.colorset[p.matrix.color[y][x]]);
				} else {
					sprite = p.matrix.getSprite(x, y);
				}
				sprite.draw(batch);
			}
	}
	
	private void queue(Batch batch, Player p) {
		for(int i = 0; i < blockref.length; i++){
			int a = mino.blocksize[blockref[i]];
			int b;
			if(i==0){
				if(p.queue.held!=null)
					b = p.queue.held.color-1;
				else continue;
			}
			else b = p.queue.que[i-1].color-1;
			
			Sprite sprite = new Sprite(blocks[blockref[i]], 0, b*a, a, a);
			sprite.setBounds(playfield.blockcoords[i][0],playfield.height-playfield.blockcoords[i][1]-playfield.blockcoords[i][3],
					playfield.blockcoords[i][2],playfield.blockcoords[i][3]);
			sprite.draw(batch);
		}
	}
		
	private void garbage(Batch batch, Player p) {
		batch.draw(warning, playfield.warningOffsetX, playfield.height-playfield.warningOffsetY,
				playfield.warningWidth, mino.size*p.garbage.warning);
	}
		
	private void text(Batch batch, Player p, BitmapFont font, TextConfig config, String text) {
		font.draw(batch, text,
				config.offsetX, playfield.height - config.offsetY,
				config.width, config.align, false);
	}
	
	private void popup(Batch batch, Player p) {
		if(p.popup.alive)
			batch.draw(p.popup.image, playfield.popupOffsetX, 
					playfield.height - playfield.popupOffsetY);
	}
		
	private void combo(Batch batch, Player p) {
		if(p.popup.image2 != null)
			batch.draw(p.popup.image2, playfield.comboOffsetX, 
					playfield.height - playfield.comboOffsetY);
	}
	
	public void dispose() {
		disposed = true;
		minosheet.dispose();
		for(Texture t : blocks)
			if(t != null)
				t.dispose();
		background.dispose();
		warning.dispose();
		scorefont.dispose();
		timefont.dispose();
	}
}
