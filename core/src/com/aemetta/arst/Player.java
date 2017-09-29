package com.aemetta.arst;

public class Player {
	
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
	
	final static int LEFT = 0;
	final static int RIGHT = 1;
	final static int ROTATE_LEFT = 2;
	final static int ROTATE_RIGHT = 3;
	final static int SOFT_DROP = 4;
	final static int HARD_DROP = 5;
	final static int HOLD = 6;
	final static int DEPLOY = 7;
	
	public int xoffset = 0;
	public int yoffset = 0;

	public boolean levelsVisible = false;
	public boolean linesVisible = false;
	public boolean timeVisible = false;
	public boolean scoreVisible = true;
	
	public Player(long seed, Matrix m) {
		
		matrix = m;
		queue = new Queue(seed);
		garbage = new Garbage(matrix, seed);
		popup = new Popup();
		score = new Score(matrix, garbage, popup);
		piece = new Piece(matrix, queue, score, this);
		
	//	garbage.add(4);
	}
	
	public Player(long seed) {
		this(seed, new Matrix());
	}
	
	public void act(float t){
		if(gameover) return;
		long time = (long) (1000*t);
		
		nextRepeat -= time;
		nextFall -= time;
		
		if(timer != null && !timer.update(time)) lose(0);
		popup.update(time);
		
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
	
	public void input(int key, boolean pressed){
		if(gameover) return;
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
	
	public void lose(int method){
		gameover = true;
		
		popup.create(true, method);
	}
	
	public void setLevelTracker(LevelTracker t) {
		level = t;
		score.level = t;
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
}
