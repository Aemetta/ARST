package com.aemetta.arst;

public class PlayfieldConfig {
	
	String path;
	int minoSize;
	
	int width;
	int height;
	int imageOffsetX;
	int imageOffsetY;
	int matrixOffsetX;
	int matrixOffsetY;
	
	String warningPath;
	int warningOffsetX;
	int warningOffsetY;
	int warningWidth;
	
	int[][] blockcoords;
	
	String scoreFontPath;
	String scoreFontImagePath;
	int scoreOffsetX;
	int scoreOffsetY;
	int scoreWidth;
	int scoreAlign;
	
	String timeFontPath;
	String timeFontImagePath;
	int timeOffsetX;
	int timeOffsetY;
	int timeWidth;
	int timeAlign;
	
	String levelFontPath;
	String levelFontImagePath;
	int levelOffsetX;
	int levelOffsetY;
	int levelWidth;
	int levelAlign;
	
	String linesFontPath;
	String linesFontImagePath;
	int linesOffsetX;
	int linesOffsetY;
	int linesWidth;
	int linesAlign;
	
	String popupPath;
	int popupOffsetX;
	int popupOffsetY;
	int comboOffsetX;
	int comboOffsetY;
    
    public PlayfieldConfig() {
    }
}
