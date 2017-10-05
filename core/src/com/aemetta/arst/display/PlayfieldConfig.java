package com.aemetta.arst.display;

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
	
	TextConfig time;
	TextConfig score;
	TextConfig level;
	TextConfig lines;
	
	String popupPath;
	int popupOffsetX;
	int popupOffsetY;
	int comboOffsetX;
	int comboOffsetY;
    
    public PlayfieldConfig() {
    }
}
