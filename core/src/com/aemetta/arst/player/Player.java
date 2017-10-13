package com.aemetta.arst.player;

import com.aemetta.arst.Arst;
import com.aemetta.arst.Controllable;
import com.aemetta.arst.gamemodes.Gamemode;

public class Player implements Controllable {
	
	Gamemode game;
	
	public Matrix matrix;
	public Queue queue;
	public Piece piece;
	public Score score;
	public Garbage garbage;
	public Timer timer;
	public Popup popup;
	public LevelTracker level;
	
	long clock;
	
	private int das = 80;
	private int arr = 18;
	long nextRepeat = -1;
	int shiftDir = 0;
	
	int fall = 800;
	private int drop = 75;
	int lockDelay = 800;
	boolean dropping = false;
	long nextFall = -1;
	boolean onGround = false;
	long toForcedLock = -1;
	
	public boolean gameover = false;
	
	final static public int FINISHED = 99;
	final static public int TOP_OUT = 100;
	final static public int TIME_UP = 101;
	final static public int LEVEL_MAX = 102;
	final static public int PAUSE = 103;
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
	}
	
	public Player(Gamemode g, long seed) {
		this(g, seed, new Matrix());
	}
	
	public void act(float t){
		long time = (long) (1000*t);
		if(!popup.update(time)) handle(FINISHED);
		if(gameover) return;
		clock += time;
		
		if(timer != null && !timer.update(time)) handle(TIME_UP);
		
		while(nextRepeat <= clock && shiftDir != 0 && 
				piece.shift(shiftDir, 0, 0)){
			nextRepeat += getARR();
		}
		while(nextFall <= clock){
			nextFall += drop;
			if(piece.shift(0, -1, 1)) {
				if(!dropping) nextFall += fall;
			} else {
				if(onGround) {
					if(toForcedLock <= clock)
						place(false);
				} else {
					toForcedLock = lockDelay + clock;
					dropping = false;
					onGround = true;
				}
			}
		}
	}
	
	public void input(int key, boolean pressed){
		if(gameover) return;
		if(pressed){
			if(key == Arst.LEFT){
				piece.shift(-1, 0, 0);
				nextRepeat = das + clock;
				shiftDir = -1;
			}
			if(key == Arst.RIGHT){
				piece.shift(1, 0, 0);
				nextRepeat = das + clock;
				shiftDir = 1;
			}
			if(key == Arst.SOFT_DROP){
				if(onGround)
					place(false);
				else {
					dropping = true;
					nextFall = clock;
				}
			}
			if(key == Arst.HARD_DROP) {
				piece.fullyMove(0, -1);
				place(true);
			}
			if(key == Arst.DEPLOY) ;
			if(key == Arst.HOLD) {
				piece.hold();
				onGround = false;
				dropping = false;
				nextFall = fall + clock;
			}
			if(key == Arst.ROTATE_LEFT) piece.rotate(-1);
			if(key == Arst.ROTATE_RIGHT) piece.rotate(1);
			if(key == Arst.ROTATE_180) piece.rotate(2);
		} else {
			if(key == Arst.LEFT) shiftDir = 0;
			if(key == Arst.RIGHT) shiftDir = 0;
			if(key == Arst.SOFT_DROP) dropping = false;

			if(key == Arst.PAUSE) handle(PAUSE);
		}
	}
	
	public void handle(int event){
		if(!game.handle(this, event)) {
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
	
	private void place(boolean hardDrop) {
		piece.place(hardDrop);
		onGround = false;
		dropping = false;
		nextFall = fall + clock;
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

	@Override
	public boolean isRawInput() {
		return false;
	}
}
