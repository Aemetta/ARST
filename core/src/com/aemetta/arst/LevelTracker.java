package com.aemetta.arst;

public class LevelTracker {
	
	int level;
	int maxLevel;
	int lines;
	int levelUpThreshold;
	
	boolean lineIncrements; //Whether the lines cleared indicator goes up or down
	
	public LevelTracker(int level, int maxLevel, int lines, boolean increments){
		
		this.level = level;
		this.maxLevel = maxLevel;
		this.lines = 0;
		this.levelUpThreshold = lines;
		
		lineIncrements = increments;
		
	}
	
	public void clearLines(int count) {
		lines += count;
		while(lines >= levelUpThreshold) {
			lines -= levelUpThreshold;
			level++;
		}
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getLines() {
		if(lineIncrements) return lines;
		else return levelUpThreshold - lines;
	}
}
