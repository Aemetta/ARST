package com.aemetta.arst.player;

import com.aemetta.arst.Arst;
import com.aemetta.arst.gamemodes.Gamemode;

public class Player {
	
	Gamemode game;
	
	public Matrix matrix;
	public Queue queue;
	public Piece piece;
	public Score score;
	public Garbage garbage;
	public Timer timer;
	public Popup popup;
	public LevelTracker level;
	
	private int das = 80;
	private int arr = 18;
	long nextRepeat = -1;
	int shiftDir = 0;
	
	int fall = 800;
	private int drop = 75;
	int lockDelay = 800;
	boolean dropping = false;
	boolean onGround = false;
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
		
		while(nextRepeat <= 0 && shiftDir != 0 && 
				piece.shift(shiftDir, 0, 0)){
			nextRepeat += getARR();
		}
		while(nextFall <= 0){
			if(piece.shift(0, -1, 0)) {
				nextFall += getDropRate();
				if(!dropping)nextFall += fall;
			}
			else{
				if(onGround) {
					piece.place(false);
					onGround = false;
				}
				nextFall += lockDelay;
				dropping = false;
				onGround = true;
			}
		}
	}
	
	public void input(int key, boolean pressed){
		if(gameover) return;
		if(pressed){
			if(key == Arst.LEFT){
				piece.shift(-1, 0, 0);
				nextRepeat = getDAS();
				shiftDir = -1;
			}
			if(key == Arst.RIGHT){
				piece.shift(1, 0, 0);
				nextRepeat = getDAS();
				shiftDir = 1;
			}
			if(key == Arst.SOFT_DROP){
				if(onGround) {
					piece.place(false);
					onGround = false;
				}
				else {
					dropping = true;
					nextFall = 0;
				}
			}
			if(key == Arst.HARD_DROP) piece.hardDrop();
			if(key == Arst.DEPLOY) ;
			if(key == Arst.HOLD) piece.hold();
			if(key == Arst.ROTATE_LEFT) piece.rotate(-1);
			if(key == Arst.ROTATE_RIGHT) piece.rotate(1);
		} else {
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
		return getDropRate();
	}
	
	public void setDropSpeed(int d) {
		setDropRate(d);
	}
	
	public int getLockDelay() {
		return lockDelay;
	}
	
	public void setLockDelay(int delay) {
		lockDelay = delay;
	}

	public LevelTracker getLevelTracker() {
		return level;
	}

	/**
	 * @return Delayed Auto Shift, the delay between first pressing left or right and starting to repeat
	 */
	public int getDAS() {
		return das;
	}

	/**
	 * @param das Delayed Auto Shift, the delay between first pressing left or right and starting to repeat
	 */
	public void setDAS(int das) {
		this.das = das;
	}

	/**
	 * @return Auto Repeat Rate, the rate at which a piece moves when holding left or right
	 */
	public int getARR() {
		return arr;
	}
	
	/**
	 * @param arr Auto Repeat Rate, the rate at which a piece moves when holding left or right
	 */
	public void setARR(int arr) {
		this.arr = arr;
	}

	/**
	 * @return The rate at which a piece falls when soft drop is used
	 */
	public int getDropRate() {
		return drop;
	}

	/**
	 * @param rate The rate at which a piece falls when soft drop is used
	 */
	public void setDropRate(int rate) {
		this.drop = rate;
	}
}
