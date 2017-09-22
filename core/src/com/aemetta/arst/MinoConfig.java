package com.aemetta.arst;

import com.badlogic.gdx.graphics.Color;

public class MinoConfig {
	
	String path;
	int size;
	boolean colored;
	boolean shaped;
	boolean randomized;
	int randomamount;
	boolean randomPerPiece;

	int[] blocksize;
	String[] blockpath;
	
    Color[] colorset = new Color[11];
    
    public MinoConfig() {
    }
}
