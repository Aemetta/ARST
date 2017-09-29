package com.aemetta.arst;

public class LevelTracker {
	
	Player host;
	
	int level;
	int maxLevel;
	int lines;
	int levelUpThreshold;
	
	boolean lineIncrements; //Whether the lines cleared indicator goes up or down
	
	public LevelTracker(Player host, int level, int maxLevel, int lines, boolean increments){
		
		this.host = host;
		this.level = level;
		this.maxLevel = maxLevel;
		this.lines = 0;
		this.levelUpThreshold = lines;
		
		lineIncrements = increments;
		
	}
	
	public void clearLines(int count) {
		boolean r = false;
		lines += count;
		while(lines >= levelUpThreshold) {
			lines -= levelUpThreshold;
			level++;
			host.handle(Player.LEVEL_UP);
		}
		if(level >= maxLevel)
			host.handle(Player.LEVEL_MAX);
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getLines() {
		if(lineIncrements) return lines;
		else return levelUpThreshold - lines;
	}
}
