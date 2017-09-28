package com.aemetta.arst;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
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
	
	public Renderer(String fieldpath, String minopath, Player[] pl) {
		
		minopath = "Minos/"+minopath+"/";
		fieldpath = "Playfields/"+fieldpath+"/";
		
		Json json = new Json();
		mino = json.fromJson(MinoConfig.class,
				Gdx.files.internal(minopath+"mino.json"));
		playfield = json.fromJson(PlayfieldConfig.class,
				Gdx.files.internal(fieldpath+"playfield.json"));
		
		if(mino.size != playfield.minoSize)
			System.err.println("Playfield and mino theme size don't match!");

		blocks = new Texture[mino.blockpath.length];
		minosheet = new Texture(minopath+mino.path);
		
		if(mino.randomized) {
			for(Player p : players) {
				p.piece.randomized = true;
				p.piece.randomamount = mino.randomamount;
				p.piece.randomperpiece = mino.randomPerPiece;
			}
		}

		background = new Texture(fieldpath+playfield.path);
		warning = new Texture(fieldpath+playfield.warningPath);
		
		blockref = new int[playfield.blockcoords.length];
		for(int i = 0; i < playfield.blockcoords.length; i++){
			blockref[i] = 0;
			for(int j = 0; j < mino.blocksize.length; j++){
				if(playfield.blockcoords[i][3] == mino.blocksize[j]) blockref[i] = j;
			}
		}
		blo: for(int i = 0; i < mino.blockpath.length; i++)
			for(int j : blockref)
			if(i == j){
				blocks = Arrays.copyOf(blocks,blocks.length+1);
				blocks[i] = new Texture(minopath+mino.blockpath[i]);
				continue blo;
			}
		
		scorefont = new BitmapFont(new FileHandle(fieldpath + playfield.scoreFontPath),
				new FileHandle(fieldpath + playfield.scoreFontImagePath),false);
		timefont = new BitmapFont(new FileHandle(fieldpath + playfield.timeFontPath),
				new FileHandle(fieldpath + playfield.timeFontImagePath),false);
		levelfont = new BitmapFont(new FileHandle(fieldpath + playfield.levelFontPath),
				new FileHandle(fieldpath + playfield.levelFontImagePath),false);
		linesfont = new BitmapFont(new FileHandle(fieldpath + playfield.linesFontPath),
				new FileHandle(fieldpath + playfield.linesFontImagePath),false);
		
		players = pl;
		
		for(int i = 0; i < players.length; i++) {
			players[i].xoffset = (int) ((((players.length-1)/2f-i)*1.1)*playfield.width);
			players[i].yoffset = 0;
		}
		
		for(Player p : players)
			p.popup.setAtlas(new TextureAtlas(fieldpath + playfield.popupPath));
	}
	
	
	public void draw(Batch batch, OrthographicCamera cam) {
		
		if(disposed) return;
		
		for(int i = 0; i < 9; i++)
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
				case 3: score(batch, p); break;
				case 4: timer(batch, p); break;
				case 5: level(batch, p); break;
				case 6: lines(batch, p); break;
				case 7: popup(batch, p); break;
				case 8: combo(batch, p); break;
				}
				cam.translate(-w,-h);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
			}
	}
	
	private void background(Batch batch, Player p) {
		batch.draw(background, playfield.imageOffsetX, playfield.imageOffsetY);
		
		//minos
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
		
	private void score(Batch batch, Player p) {
		scorefont.draw(batch, Integer.toString(p.score.score),
				playfield.scoreOffsetX, playfield.height - playfield.scoreOffsetY,
				playfield.scoreWidth, playfield.scoreAlign, false);
	}

	private void timer(Batch batch, Player p) {
		timefont.draw(batch, p.timer.view(),
				playfield.timeOffsetX, playfield.height - playfield.timeOffsetY,
				playfield.timeWidth, playfield.timeAlign, false);
	}
	
	private void level(Batch batch, Player p) {
		levelfont.draw(batch, Integer.toString(p.level.level),
				playfield.levelOffsetX, playfield.height - playfield.levelOffsetY,
				playfield.levelWidth, playfield.levelAlign, false);
	}

	private void lines(Batch batch, Player p) {
		linesfont.draw(batch, Integer.toString(p.level.lines),
				playfield.linesOffsetX, playfield.height - playfield.linesOffsetY,
				playfield.linesWidth, playfield.linesAlign, false);
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
