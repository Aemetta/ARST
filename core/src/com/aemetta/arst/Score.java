package com.aemetta.arst;

public class Score {
	
	Player target;
	Matrix matrix;
	Garbage garbage;
	Popup popup;
	LevelTracker level;
	
	int score = 0;
	boolean backtoback = false;
	int combo = 0;
	final int[] table = {0,0,1,1,1,2,2,3,3,4,4,4,5,5};
	
	public Score(Matrix b, Garbage g, Popup p){
		target = null;
		matrix = b;
		garbage = g;
		popup = p;
	}
	
	public Score(Matrix b, Garbage g, Popup p, LevelTracker l) {
		this(b, g, p);
		level = l;
	}
	
	public void setTarget(Player t) {
		target = t;
	}
	
	public void place(int lines, int cells, boolean tspin, int height){
		
		if(lines==0) combo = 0;
		if(!(lines==4 || lines==0 || tspin)) backtoback = false;
		
		//Add score
		int add = 0;
		if(tspin)
			switch(lines){
			case 0: add = 400; break;
			case 1: add = 800; break;
			case 2: add = 1200; break;
			case 3: add = 1600; break;
			}
		else
			switch(lines){
			case 1: add = 100; break;
			case 2: add = 300; break;
			case 3: add = 500; break;
			case 4: add = 800; break;
			}
		
		if(backtoback) add *= 1.5;
		
		add += 50*combo;
		if(level != null) {
			level.clearLines(lines);
			add *= level.level;
		}
		add += cells;
		score += add;
		
		//create a popup
		popup.create(lines, tspin, backtoback, combo, height);
		
		if(lines == 0){
			garbage.fill();
		}
		else{
			if((lines==4 || tspin)) backtoback = true;
			
			//Throw garbage
			lines--;
			if(lines == 3) lines++;
			lines += table[combo];
			lines = garbage.remove(lines);
			combo++;
			if(target != null)
				target.garbage.add(lines);
		}
	}
}
