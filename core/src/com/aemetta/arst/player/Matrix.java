package com.aemetta.arst.player;

import java.util.Arrays;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Matrix {
	
	private final int WIDTH;
	private final int HEIGHT;
	private final int TOP;
	boolean[][] solid;
	int[][] color;
	Wang[][] wang;
	int[][] texture;
	boolean[][] updated;
	Sprite[][] sprite;
		//ALL ARRAYS WITH X/Y VALUES FOR BLOCKS ON THE matrix
		//ARE IN THE FORM var[y][x] STARTING WITH Y
	
	int specialY = -1;
	int specialX = 0;
	int specialType = 0;
	boolean specialClearedFlag = false;
	boolean specialUpdated = false;
	Sprite specialSprite = null;
	
	public Matrix(){
		this(10, 24, 4);
	}
	
	public Matrix(int w, int h) {
		this(w, h+4, 4);
	}
	
	public Matrix(int w, int h, int t) {
		WIDTH = w;
		HEIGHT = h;
		TOP = t;
		
		solid = new boolean[getHeight()][getWidth()];
		color = new int[getHeight()][getWidth()];
		wang = new Wang[getHeight()][getWidth()];
		texture = new int[getHeight()][getWidth()];
		updated = new boolean[getHeight()][getWidth()];
		sprite = new Sprite[getHeight()][getWidth()];
	}
	
	public int consolidate(){
		int cleared = 0;
		for(int i = getHeight()-1; i >= 0; i--){
			boolean c = true;
			for(int j = 0; j < getWidth(); j++)
				if(!solid[i][j]) c = false;
			if(c){
				cleared++;
				shift(i, false);
				for(int j = 0; j < getWidth(); j++){
					if(solid[i][j])
						wang[i][j] = wang[i][j].shear(true);
					if(i > 0)
						if(solid[i-1][j]) {
							wang[i-1][j] = wang[i-1][j].shear(false);
							updated[i-1][j] = true;
						}
				}
				if(specialY == i) {
					clearSpecial();
					specialClearedFlag = true;
				}
			}
		}
		return cleared;
	}
	
	public void shift(int line, boolean up){
		if(!up){ //Shift everything down, clearing lines
			for(int i = line; i < getHeight()-1; i++){
				solid[i] = solid[i+1];
				color[i] = color[i+1];
				wang[i] = wang[i+1];
				for(int j = 0; j < getWidth(); j++)
					updated[i][j] = true;
			}
			solid[getHeight()-1] = new boolean[getWidth()];
			color[getHeight()-1] = new int[getWidth()];
			wang[getHeight()-1] = new Wang[getWidth()];
			updated[getHeight()-1] = new boolean[getWidth()];
			Arrays.fill(updated[getHeight()-1], true);
		}
		else{ //shift everything up, adding garbage
			for(int i = getHeight()-1; i > line; i--){
				solid[i] = solid[i-1];
				color[i] = color[i-1];
				wang[i] = wang[i-1];
				for(int j = 0; j < getWidth(); j++)
					updated[i][j] = true;
			}
			solid[line] = new boolean[getWidth()];
			color[line] = new int[getWidth()];
			wang[line] = new Wang[getWidth()];
			updated[line] = new boolean[getWidth()];
			Arrays.fill(updated[getHeight()-1], true);
		}
	}
	
	public void smooth(int start, int end){
		if(start > end){int c = start; start = end; end = c;}
		for(int i = start; i <= end; i++){
			for(int j = 0; j < getWidth(); j++){
				wang[i][j] = Wang.MIDDLE;
				if(j == 0 || color[i][j] != color[i][j-1])
					wang[i][j] = Wang.WTEE;
				if(j == getWidth()-1 || color[i][j] != color[i][j+1]){
					if(wang[i][j] == Wang.WTEE) wang[i][j] = Wang.VERT;
					else wang[i][j] = Wang.ETEE;
				}
				if(i == start || color[i][j] != color[i-1][j])
					wang[i][j] = wang[i][j].shear(true);
				if(i == end || color[i][j] != color[i+1][j])
					wang[i][j] = wang[i][j].shear(false);
				
				updated[i][j] = true;
			}
		}
	}
	
	public void recolor(int color, int x1, int y1, int x2, int y2) {
		for(int i = y1; i < y2; i++)
			for(int j = x1; j < x2; j++)
				if(this.color[i][j] != 0)
					this.color[i][j] = color;
	}
	
	public void setSquare(int x, int y, int c, Wang s, int t){
		setSquare(x, y, c, s);
		texture[y][x] = t;
	}
	
	public void setSquare(int x, int y, int c, Wang s){
		color[y][x] = c;
		wang[y][x] = s;
		updated[y][x] = true;
	}
	
	public void hideSquare(int x, int y){
		color[y][x] = 0;
		wang[y][x] = Wang.LONER;
	}
	
	public boolean isSolid(int x, int y){
		try{
			return solid[y][x];
		} catch(ArrayIndexOutOfBoundsException e){
			return true; }
	}
	
	public boolean hasUpdated(int x, int y) {
		if(updated[y][x]) {
			updated[y][x] = false;
			return true;
		}
		return false;
	}
	
	public void setSprite(int x, int y, Sprite s) {
		sprite[y][x] = s;
	}
	
	public Sprite getSprite(int x, int y) {
		return sprite[y][x];
	}
	
	public void makeSolid(int x, int y){
		solid[y][x] = true;;
	}
	
	public int getColor(int x, int y) {
		return color[y][x];
	}

	public Wang getShape(int x, int y) {
		return wang[y][x];
	}

	public int getTexture(int x, int y) {
		return texture[y][x];
	}
	
	public void setSpecial(int x, int y) {
		this.setSpecial(x, y, 0);
	}
	
	public void setSpecial(int x, int y, int type) {
		specialX = x;
		specialY = y;
		specialType = type;
		if(specialType != 0) specialUpdated = true;
	}
	
	public void clearSpecial() {
		specialY = -1;
		if(specialType != 0) specialUpdated = true;
	}
	
	public Matrix clone() {
		Matrix m = new Matrix(this.getWidth(), this.getHeight(), this.getTopOffset());
		
		for(int i = 0; i < this.getHeight(); i++)
			for(int j = 0; j < this.getWidth(); j++) {
				m.solid[i][j] = this.solid[i][j];
				m.color[i][j] = this.color[i][j];
				m.wang[i][j] = this.wang[i][j];
				m.texture[i][j] = this.texture[i][j];
				m.updated[i][j] = true;
			}
		
		m.specialX = this.specialX;
		m.specialY = this.specialY;
		m.specialUpdated = true;
		
		return m;
	}
	
	public boolean isEmpty() {
		boolean r = true;
		
		for(int i = 0; i < getWidth(); i++)
			if(isSolid(i, 0)) r = false;
			
		return r;
	}

	/**
	 * @return the wIDTH
	 */
	public int getWidth() {
		return WIDTH;
	}

	/**
	 * @return the hEIGHT
	 */
	public int getHeight() {
		return HEIGHT;
	}

	/**
	 * @return the tOP
	 */
	public int getTopOffset() {
		return TOP;
	}
}
