package com.aemetta.arst.display;

import java.util.Arrays;

import com.aemetta.arst.player.Color;
import com.aemetta.arst.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Json;

public class PlayerDisplay implements Display {
	
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
	
	public PlayerDisplay(String fpath, String mpath, Player[] pl, AssetManager manager) {

		setPlayers(pl);
		
		this.minopath = "Minos/"+mpath+"/";
		this.fieldpath = "Playfields/"+fpath+"/";
		
		Json json = new Json();
		setMinoConfig(json.fromJson(MinoConfig.class,
				Gdx.files.internal(minopath+"mino.json")));
		setPlayfieldConfig(json.fromJson(PlayfieldConfig.class,
				Gdx.files.internal(fieldpath+"playfield.json")));
		
		if(getMinoConfig().size != getPlayfieldConfig().minoSize)
			System.err.println("Playfield and mino theme size don't match!");
		
		if(getMinoConfig().randomized) {
			for(Player p : getPlayers()) {
				p.piece.setRandomized(true);
				p.piece.setRandomamount(getMinoConfig().randomamount);
				p.piece.setRandomperpiece(getMinoConfig().randomPerPiece);
			}
		}
		
		
		blockref = new int[getPlayfieldConfig().blockcoords.length];
		for(int i = 0; i < getPlayfieldConfig().blockcoords.length; i++){
			blockref[i] = 0;
			for(int j = 0; j < getMinoConfig().blocksize.length; j++){
				if(getPlayfieldConfig().blockcoords[i][3] == getMinoConfig().blocksize[j]) blockref[i] = j;
			}
		}
		
		for(int i = 0; i < getPlayers().length; i++) {
			getPlayers()[i].xoffset = (int) ((((getPlayers().length-1)/2f-i)*1.1)*getPlayfieldConfig().width);
			getPlayers()[i].yoffset = 0;
		}
		
		manager.load(minopath+getMinoConfig().path, Texture.class);
		manager.load(fieldpath+getPlayfieldConfig().path, Texture.class);
		manager.load(fieldpath+getPlayfieldConfig().warningPath, Texture.class);
		for(int i = 0; i < getMinoConfig().blockpath.length; i++)
			manager.load(minopath+getMinoConfig().blockpath[i], Texture.class);
		
		manager.load(fieldpath + getPlayfieldConfig().popupPath, TextureAtlas.class);
		
		manager.load(fieldpath + getPlayfieldConfig().score.fontPath, BitmapFont.class);
		manager.load(fieldpath + getPlayfieldConfig().time.fontPath, BitmapFont.class);
		manager.load(fieldpath + getPlayfieldConfig().level.fontPath, BitmapFont.class);
		manager.load(fieldpath + getPlayfieldConfig().lines.fontPath, BitmapFont.class);
	}
	
	public void init(AssetManager manager) {
		
		minosheet = manager.get(minopath+getMinoConfig().path, Texture.class);
		
		background = manager.get(fieldpath+getPlayfieldConfig().path, Texture.class);
		warning = manager.get(fieldpath+getPlayfieldConfig().warningPath, Texture.class);

		blocks = new Texture[getMinoConfig().blockpath.length];
		blo: for(int i = 0; i < getMinoConfig().blockpath.length; i++)
			for(int j : blockref)
				if(i == j){
					blocks = Arrays.copyOf(blocks,blocks.length+1);
					blocks[i] = manager.get(minopath+getMinoConfig().blockpath[i], Texture.class);
					continue blo;
				}
		
		scorefont = manager.get(fieldpath + getPlayfieldConfig().score.fontPath, BitmapFont.class);
		timefont = manager.get(fieldpath + getPlayfieldConfig().time.fontPath, BitmapFont.class);
		levelfont = manager.get(fieldpath + getPlayfieldConfig().level.fontPath, BitmapFont.class);
		linesfont = manager.get(fieldpath + getPlayfieldConfig().lines.fontPath, BitmapFont.class);
		
		TextureAtlas ta = manager.get(fieldpath + getPlayfieldConfig().popupPath, TextureAtlas.class);
		
		for(Player p : getPlayers())
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
			for(Player p : getPlayers()) {
				
				int w = getPlayfieldConfig().width/2 + p.xoffset;
				int h = getPlayfieldConfig().height/2 + p.yoffset;
				cam.translate(w,h);
				cam.update();
				batch.setProjectionMatrix(cam.combined);
				
				switch(i) {
				case 0: background(batch, p); break;
				case 1: queue(batch, p); break;
				case 2: garbage(batch, p); break;
				case 3: if(p.hasScore())
							text(batch, p, scorefont, getPlayfieldConfig().score,
							Integer.toString(p.score.getScore())); break;
				case 4: if(p.hasTimer())
							text(batch, p, timefont, getPlayfieldConfig().time,
							p.timer.view()); break;
				case 5: if(p.hasLineTracker())
							text(batch, p, linesfont, getPlayfieldConfig().lines,
							Integer.toString(p.level.getLines())); break;
				case 6: if(p.hasLevelTracker())
							text(batch, p, levelfont, getPlayfieldConfig().level,
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
		batch.draw(background, getPlayfieldConfig().imageOffsetX, getPlayfieldConfig().imageOffsetY);
	}
		
	private void minos(Batch batch, Player p) {
		for(int x = 0; x < p.matrix.getWidth(); x++)
			for(int y = 0; y < p.matrix.getHeight()-p.matrix.getTopOffset(); y++)
			{
				if(p.matrix.getColor(x, y)==Color.Empty) continue;
				if(p.matrix.getShape(x, y)==null) continue;
				Sprite sprite;
				if(p.matrix.hasUpdated(x, y)) {
					int dspx = x * getMinoConfig().size + getPlayfieldConfig().matrixOffsetX;
					int dspy = y * getMinoConfig().size + getPlayfieldConfig().matrixOffsetY;
					int srcx = 0;
					int srcy = 0;
					
					if(getMinoConfig().colored)
						srcy = p.matrix.getColor(x,y).code;
					
					if(getMinoConfig().randomized){
						srcx = p.matrix.getTexture(x,y);
					}
					
					if(getMinoConfig().shaped){
						srcx = srcx*4+p.matrix.getShape(x,y).x;
						srcy = srcy*4+p.matrix.getShape(x,y).y;
					}
					
					srcx *= getMinoConfig().size;
					srcy *= getMinoConfig().size;
					
					sprite = new Sprite(minosheet, srcx, srcy, getMinoConfig().size, getMinoConfig().size);
					sprite.setBounds(dspx, dspy, getMinoConfig().size, getMinoConfig().size);
					p.matrix.setSprite(x, y, sprite);
					if(!getMinoConfig().colored)
						sprite.setColor(getMinoConfig().colorset[p.matrix.getColor(x, y).code]);
				} else {
					sprite = p.matrix.getSprite(x, y);
				}
				sprite.draw(batch);
			}
	}
	
	private void queue(Batch batch, Player p) {
		for(int i = 0; i < blockref.length; i++){
			int a = getMinoConfig().blocksize[blockref[i]];
			int b;
			if(i==0){
				if(p.queue.getHeldShape()!=null)
					b = p.queue.getHeldShape().color.code-1;
				else continue;
			}
			else b = p.queue.getQueSpot(i-1).color.code-1;
			
			Sprite sprite = new Sprite(blocks[blockref[i]], 0, b*a, a, a);
			sprite.setBounds(getPlayfieldConfig().blockcoords[i][0],getPlayfieldConfig().height-getPlayfieldConfig().blockcoords[i][1]-getPlayfieldConfig().blockcoords[i][3],
					getPlayfieldConfig().blockcoords[i][2],getPlayfieldConfig().blockcoords[i][3]);
			sprite.draw(batch);
		}
	}
		
	private void garbage(Batch batch, Player p) {
		batch.draw(warning, getPlayfieldConfig().warningOffsetX, getPlayfieldConfig().height-getPlayfieldConfig().warningOffsetY,
				getPlayfieldConfig().warningWidth, getMinoConfig().size*p.garbage.getWarningAmount());
	}
		
	private void text(Batch batch, Player p, BitmapFont font, TextConfig config, String text) {
		font.draw(batch, text,
				config.offsetX, getPlayfieldConfig().height - config.offsetY,
				config.width, config.align, false);
	}
	
	private void popup(Batch batch, Player p) {
		if(p.popup.image1 != null)
			batch.draw(p.popup.image1, getPlayfieldConfig().popupOffsetX, 
					getPlayfieldConfig().height - getPlayfieldConfig().popupOffsetY);
	}
		
	private void combo(Batch batch, Player p) {
		if(p.popup.image2 != null)
			batch.draw(p.popup.image2, getPlayfieldConfig().comboOffsetX, 
					getPlayfieldConfig().height - getPlayfieldConfig().comboOffsetY);
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

	/**
	 * @return the players
	 */
	public Player[] getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(Player players[]) {
		this.players = players;
	}

	/**
	 * @return the playfield
	 */
	public PlayfieldConfig getPlayfieldConfig() {
		return playfield;
	}

	/**
	 * @param playfield the playfield to set
	 */
	public void setPlayfieldConfig(PlayfieldConfig playfield) {
		this.playfield = playfield;
	}

	/**
	 * @return the mino
	 */
	public MinoConfig getMinoConfig() {
		return mino;
	}

	/**
	 * @param mino the mino to set
	 */
	public void setMinoConfig(MinoConfig mino) {
		this.mino = mino;
	}
}
