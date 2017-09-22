package com.aemetta.arst;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Json;

public class Player {
	
	Matrix matrix;
	Queue queue;
	Piece piece;
	Score score;
	Garbage garbage;
	Timer timer;
	
	int das = 80;
	int arr = 18;
	long nextRepeat = -1;
	int shiftDir = 0;
	
	int fall = 800;
	int drop = 75;
	boolean dropping = false;
	long nextFall = -1;
	
	final static int LEFT = 0;
	final static int RIGHT = 1;
	final static int ROTATE_LEFT = 2;
	final static int ROTATE_RIGHT = 3;
	final static int SOFT_DROP = 4;
	final static int HARD_DROP = 5;
	final static int HOLD = 6;
	final static int DEPLOY = 7;
	
	Texture minosheet;
	Texture[] blocks;
	
	Texture background;
	Texture warning;
	
	BitmapFont scorefont;
	BitmapFont timefont;
	
	int[] blockref;
	
	MinoConfig mino;
	PlayfieldConfig playfield;
	
	public Player(long seed, String fieldpath, String minopath) {
		queue = new Queue(seed);
		matrix = new Matrix();
		garbage = new Garbage(matrix, seed);
		score = new Score(matrix, garbage);
		piece = new Piece(matrix, queue, score, this);
		timer = new Timer(120);
		
		garbage.add(4);
		
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
		for(int i = 0; i < mino.blockpath.length; i++){
			blocks = Arrays.copyOf(blocks,blocks.length+1);
			blocks[i] = new Texture(minopath+mino.blockpath[i]);
		}
		if(mino.randomized) {
			piece.randomized = true;
			piece.randomamount = mino.randomamount;
			piece.randomperpiece = mino.randomPerPiece;
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
		
		scorefont = new BitmapFont(new FileHandle("Playfields/purple-20/" + playfield.scoreFontPath),
				new FileHandle("Playfields/purple-20/" + playfield.scoreFontImagePath),false);
		timefont = new BitmapFont(new FileHandle("Playfields/purple-20/" + playfield.timeFontPath),
				new FileHandle("Playfields/purple-20/" + playfield.timeFontImagePath),false);
	}
	
	public void act(float t){
		long time = (long) (1000*t);
		
		nextRepeat -= time;
		nextFall -= time;
		
		if(!timer.update(time)) lose();
		
		if(nextRepeat < 0 && shiftDir != 0){
			piece.shift(shiftDir, 0, 0);
			nextRepeat += arr;
		}
		if(nextFall < 0){
			if(piece.warn == 0) piece.shift(0, -1, 0);
			else{
				piece.warn++;
				dropping = false;
				if(piece.warn > 2) piece.place(false);
			}
			nextFall += drop;
			if(!dropping)nextFall += fall;
		}
	}
	
	public void setInput(int key, boolean pressed){
		if(pressed){
			if(key == LEFT){
				piece.shift(-1, 0, 0);
				nextRepeat = das;
				shiftDir = -1;
			}
			if(key == RIGHT){
				piece.shift(1, 0, 0);
				nextRepeat = das;
				shiftDir = 1;
			}
			if(key == SOFT_DROP){
				dropping = true;
				nextFall = 0;
			}
			if(key == HARD_DROP) piece.hardDrop();
			if(key == DEPLOY) ;
			if(key == HOLD) piece.hold();
			if(key == ROTATE_LEFT) piece.rotate(-1);
			if(key == ROTATE_RIGHT) piece.rotate(1);
		}
		else{
			
			if(key == LEFT) shiftDir = 0;
			if(key == RIGHT) shiftDir = 0;
			if(key == SOFT_DROP) dropping = false;
			
		}
	}
	
	public void lose(){
		
	}
	
	public void draw(Batch batch) {
		//background
		batch.draw(background, playfield.imageOffsetX, playfield.imageOffsetY);
		
		//minos
		for(int x = 0; x < matrix.WIDTH; x++)
			for(int y = 0; y < matrix.HEIGHT-matrix.TOP; y++)
			{
				if(matrix.getColor(x, y)==0) continue;
				if(matrix.getShape(x, y)==null) continue;
				Sprite sprite;
				if(matrix.hasUpdated(x, y)) {
					int dspx = x * mino.size + playfield.matrixOffsetX;
					int dspy = y * mino.size + playfield.matrixOffsetY;
					int srcx = 0;
					int srcy = 0;
					
					if(mino.colored)
						srcy = matrix.getColor(x,y);
					
					if(mino.randomized){
						srcx = matrix.getTexture(x,y);
					}
					
					if(mino.shaped){
						srcx = srcx*4+matrix.getShape(x,y).x;
						srcy = srcy*4+matrix.getShape(x,y).y;
					}
					
					srcx *= mino.size;
					srcy *= mino.size;
					
					sprite = new Sprite(minosheet, srcx, srcy, mino.size, mino.size);
					sprite.setBounds(dspx, dspy, mino.size, mino.size);
					matrix.setSprite(x, y, sprite);
					if(!mino.colored)
						sprite.setColor(mino.colorset[matrix.color[y][x]]);
				} else {
					sprite = matrix.getSprite(x, y);
				}
				sprite.draw(batch);
			}
		
		//queue
		for(int i = 0; i < blockref.length; i++){
			int a = mino.blocksize[blockref[i]];
			int b;
			if(i==0){
				if(queue.held!=null)
					b = queue.held.color-1;
				else continue;
			}
			else b = queue.que[i-1].color-1;
			
			Sprite sprite = new Sprite(blocks[blockref[i]], 0, b*a, a, a);
			sprite.setBounds(playfield.blockcoords[i][0],playfield.height-playfield.blockcoords[i][1]-playfield.blockcoords[i][3],
					playfield.blockcoords[i][2],playfield.blockcoords[i][3]);
			sprite.draw(batch);
		}
		
		//garbage
		batch.draw(warning, playfield.warningOffsetX, playfield.height-playfield.warningOffsetY,
				playfield.warningWidth, mino.size*garbage.warning);
		
		//score
		scorefont.draw(batch, Integer.toString(score.score),
				playfield.scoreOffsetX, playfield.height - playfield.scoreOffsetY);

		//time
		timefont.draw(batch, timer.view(),
				playfield.timeOffsetX, playfield.height - playfield.timeOffsetY);
	}
}
