package com.aemetta.arst.player;

public class Score {
	
	Player target;
	Player host;
	
	private int score = 0;
	boolean backtoback = false;
	int combo = 0;
	final int[] table = {0,0,1,1,1,2,2,3,3,4,4,4,5,5};
	
	public Score(Player p){
		target = null;
		host = p;
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
		if(host.level != null) {
			host.level.clearLines(lines);
			add *= host.level.level;
		}
		add += cells;
		setScore(getScore() + add);
		
		//handle perfect clear
		if(host.matrix.isEmpty()) {
			setScore(getScore() + 1000);
			host.popup.perfectClear();
			host.handle(Player.PERFECT_CLEAR);
		}
		
		//create a popup
		host.popup.create(lines, tspin, backtoback, combo, height);
		
		if(lines == 0){
			host.garbage.fill();
		}
		else{
			if((lines==4 || tspin)) backtoback = true;
			
			//Throw garbage
			lines--;
			if(lines == 3) lines++;
			lines += table[combo];
			lines = host.garbage.remove(lines);
			combo++;
			if(target != null)
				target.garbage.add(lines);
		}
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
}
