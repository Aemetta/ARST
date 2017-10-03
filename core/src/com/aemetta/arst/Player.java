package com.aemetta.arst;

import com.aemetta.arst.gamemodes.Gamemode;

public class Player {
	
	Gamemode game;
	
	Matrix matrix;
	Queue queue;
	Piece piece;
	public Score score;
	Garbage garbage;
	Timer timer;
	Popup popup;
	LevelTracker level;
	
	int das = 80;
	int arr = 18;
	long nextRepeat = -1;
	int shiftDir = 0;
	
	int fall = 800;
	int drop = 75;
	boolean dropping = false;
	long nextFall = -1;
	
	boolean gameover = false;
	
	final static public int TOP_OUT = 100;
	final static public int TIME_UP = 101;
	final static public int LEVEL_MAX = 102;
	final static public int LEVEL_UP = 200;
	final static public int PERFECT_CLEAR = 201;
	
	public int xoffset = 0;
	public int yoffset = 0;

	public boolean levelsVisible = false;
	public boolean linesVisible = false;
	public boolean timeVisible = false;
	public boolean scoreVisible = true;
	
	public Player(Gamemode g, long seed, Matrix m) {
		
		game = g;
		
		matrix = m;
		queue = new Queue(seed);
		garbage = new Garbage(matrix, seed);
		popup = new Popup();
		score = new Score(this);
		piece = new Piece(matrix, queue, score, this);
		
	//	garbage.add(4);
	}
	
	public Player(Gamemode g, long seed) {
		this(g, seed, new Matrix());
	}
	
	public void act(float t){
		if(gameover) return;
		long time = (long) (1000*t);
		
		nextRepeat -= time;
		nextFall -= time;
		
		if(timer != null && !timer.update(time)) handle(TIME_UP);
		popup.update(time);
		
		while(nextRepeat <= 0 && shiftDir != 0){
			piece.shift(shiftDir, 0, 0);
			nextRepeat += arr;
		}
		while(nextFall <= 0){
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
	
	public void input(int key, boolean pressed){
		if(gameover) return;
		if(pressed){
			if(key == Arst.LEFT){
				piece.shift(-1, 0, 0);
				nextRepeat = das;
				shiftDir = -1;
			}
			if(key == Arst.RIGHT){
				piece.shift(1, 0, 0);
				nextRepeat = das;
				shiftDir = 1;
			}
			if(key == Arst.SOFT_DROP){
				dropping = true;
				nextFall = 0;
			}
			if(key == Arst.HARD_DROP) piece.hardDrop();
			if(key == Arst.DEPLOY) ;
			if(key == Arst.HOLD) piece.hold();
			if(key == Arst.ROTATE_LEFT) piece.rotate(1);
			if(key == Arst.ROTATE_RIGHT) piece.rotate(-1);
		}
		else{
			
			if(key == Arst.LEFT) shiftDir = 0;
			if(key == Arst.RIGHT) shiftDir = 0;
			if(key == Arst.SOFT_DROP) dropping = false;
			
		}
	}
	
	public void handle(int event){
		if(!game.handle(event)) {
			switch(event) {
			case TOP_OUT:	gameover = true;
							popup.create(true, 1);
							break;
			case TIME_UP:	gameover = true;
							popup.create(true, 0);
							break;
			case LEVEL_MAX:	gameover = true;
							popup.create(true, 3);
							break;
			}
		}
	}
	
	public void setLevelTracker(LevelTracker t) {
		level = t;
		levelsVisible = true;
		linesVisible = true;
	}
	
	public void setTimer(Timer t) {
		timer = t;
		timeVisible = true;
	}
	
	public void hideScore() {
		scoreVisible = false;
	}
	
	public void hideLevels() {
		levelsVisible = false;
	}
	
	public boolean hasTimer() {
		return timeVisible;
	}
	
	public boolean hasLevelTracker() {
		return levelsVisible;
	}
	
	public boolean hasLineTracker() {
		return linesVisible;
	}
	
	public boolean hasScore() {
		return scoreVisible;
	}
	
	public int getFallSpeed() {
		return fall;
	}
	
	public void setFallSpeed(int f) {
		fall = f;
	}
	
	public int getDropSpeed() {
		return drop;
	}
	
	public void setDropSpeed(int d) {
		drop = d;
	}
}
