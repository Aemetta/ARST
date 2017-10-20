package com.aemetta.arst.player;

import java.util.Arrays;

public class Piece {
	
	Matrix matrix;
	Queue queue;
	Score score;
	Player player;
	
	Shape shape;
	
	int[] x = new int[4];
	int[] y = new int[4];
	int[] gx = new int[4];
	int[] gy = new int[4];
	Wang[] wang = new Wang[4]; //The wang is what image to use for the mino
	int[] texture = new int[4];
	
	int cells = 0; //number of cells it takes up horizontally
					//used to calculate the score of placing a piece

	int rotation;
	int[][] offsetx = { {0,0,0,0,0},
						{0,1,1,0,1},
						{0,0,0,0,0},
						{0,-1,-1,0,-1}};
	int[][] offsety = { {0,0,0,0,0},
						{0,0,1,2,2},
						{0,0,0,0,0},
						{0,0,1,2,2}};
	int[][] offsetxI = {{0,-1,2,-1,2},
						{-1,0,0,0,0},
						{-1,1,-2,1,-2},
						{0,0,0,0,0}};
	int[][] offsetyI = {{0,0,0,0,0},
						{0,0,0,1,-2},
						{1,1,1,0,0},
						{1,1,1,-1,2}};
	
	private boolean randomized = false;
	private int randomamount = 0;
	private boolean randomperpiece = false;
	
	public Piece(Matrix b, Queue q, Score s, Player p){
		matrix = b;
		queue = q;
		score = s;
		player = p;;
		shape = queue.pull();
		newPiece();
	}
	
	void place(boolean hardDrop){
		boolean survive = false;
		for(int i = 0; i < 4; i++)
			if(y[i] < 20) survive = true;
		if(!survive) player.handle(Player.TOP_OUT);
		
		//T-spin 3-corner check
		boolean tspin = false;
		if(shape==Shape.T && !canShift(0, 1)
			&& !canShift(1, 0) && !canShift(-1, 0)){
			int corners = 0;
			if(matrix.isSolid(x[1]+1, y[1]+1)) corners++;
			if(matrix.isSolid(x[1]-1, y[1]+1)) corners++;
			if(matrix.isSolid(x[1]+1, y[1]-1)) corners++;
			if(matrix.isSolid(x[1]-1, y[1]-1)) corners++;
			if(corners >= 3) tspin = true;
		}
		
		int lowy = 20;
		for(int i = 0; i < 4; i++) {
			matrix.makeSolid(x[i],y[i]);
			lowy = (y[i] < lowy) ? y[i] : lowy;
		}
		
		if(hardDrop) cells *= 2;
		score.place(matrix.consolidate(), cells, tspin, lowy);
		
		shape = queue.pull();
		newPiece();
		queue.resetHeldStatus();
	}
	
	void hold(){
		Shape oldtype = shape;
		shape = queue.hold(shape);
		if(shape != oldtype) {
			if(shape==null) shape = queue.pull();
			for(int i = 0; i < 4; i++){
				matrix.hideSquare(x[i], y[i]);
				matrix.hideSquare(gx[i], gy[i]);
			}
			newPiece();
		}
	}
	
	void newPiece(){
		
		for(int i = 0; i < 4; i++){
			x[i] = shape.x[i];
			y[i] = shape.y[i];
			wang[i] = shape.wang[i];
			
			gx[i] = x[i];
			gy[i] = y[i]; //Move the ghost away from its old position to avoid glitchy
		}
		
		if(isRandomized()){
			if(isRandomperpiece())
				Arrays.fill(texture, (int)(Math.random()*getRandomamount()));
			else{
				texture[0] = (int)(Math.random()*getRandomamount());
				texture[1] = (int)(Math.random()*getRandomamount());
				texture[2] = (int)(Math.random()*getRandomamount());
				texture[3] = (int)(Math.random()*getRandomamount());
			}
		}
		
		if(!shift(0,-2,2)) //Move the piece to its new current position
			player.handle(Player.TOP_OUT);
		shift(0,-1,1);
		rotation = 0;
		countCells();
	}
	
	public boolean canShift(int sx, int sy){
		for(int i = 0; i < 4; i++)
			if(matrix.isSolid(x[i]+sx,y[i]+sy)) return false;
		return true;
	}
	/*
	 * @return whether or not it was able to shift
	 * @param x Horizontal movement
	 * @param y Vertical movement
	 * @param mode One of the following: 0 for regular movement, 1 for vertical, 3 for rotating and spawning
	 */
	public boolean shift(int x, int y, int mode){
		if(!canShift(x, y)) return false;
		
		for(int i = 0; i < 4; i++){
			if(mode != 2)
				matrix.hideSquare(this.x[i], this.y[i]); //Hide the piece
			this.y[i] += y; this.x[i] += x; //Shift
		}
		if(mode != 1) placeGhost(); //The ghost won't move when falling, so there's a boolean to update the ghost
		for(int i = 0; i < 4; i++){
			matrix.setSquare(this.x[i], this.y[i], shape.color, wang[i], texture[i]); //Show the piece
		}
		
		return true;
	}
	
	public void placeGhost(){
		
		for(int i = 0; i < 4; i++){
			if(matrix.getColor(gx[i], gy[i]) == Color.Ghost)
				 matrix.hideSquare(gx[i], gy[i]); //Hide the ghost
		}
		int j = 0;
		while(canShift(0, j)) j--; //Check how far down it can go
		j++; //Above code goes one more down than needed...off by one error
		if(j != 0) { //Make sure the piece is above the ground
			for(int i = 0; i < 4; i++){
				gx[i] = x[i]; //Set the ghost's position
				gy[i] = y[i]+j;
				matrix.setSquare(gx[i], gy[i], Color.Ghost, wang[i], texture[i]); //Show the ghost
			}
		}
	}
	
	public void rotate(int a){
		if(a<0)a+=4;          //1 is right, -1 is left...but 3 works, too
		if(a==0) return;	  //not rotating at all
		if(shape==Shape.O) return;	  //squares don't rotate
		
		int[] oldx = new int[4];
		int[] oldy = new int[4];
		for(int i = 0; i < 4; i++) {
			oldx[i] = x[i];
			oldy[i] = y[i];
			
			matrix.hideSquare(x[i], y[i]); //Hide the piece
		}
		
		int rx = x[1]; int ry = y[1]; //The middle of the piece
		for(int i = 0; i < 4; i++){
			int c;
			x[i] -= rx; y[i] -= ry; //move the piece to center around 0 for the transformation
			switch(a) {
			case 1:c = x[i]; x[i] = y[i]; y[i] = -c; //right
			break;
			case 2:x[i] = -x[i]; y[i] = -y[i];       //180
			break;
			case 3:c = x[i]; x[i] = -y[i]; y[i] = c; //left
			break;
			}
			x[i] += rx; y[i] += ry; //move back into position
		}
		
		int newrot = a - rotation;
		if(newrot<0)newrot+=4;
		int sx=50; int sy=50;
		
		for(int i = 0; i < 5 && !canShift(sx, sy); i++) {
			if(shape==Shape.I) {
				sx = offsetxI[rotation][i] - offsetxI[newrot][i];
				sy = offsetyI[rotation][i] - offsetyI[newrot][i];
			} else {
				sx = offsetx[rotation][i] - offsetx[newrot][i];
				sy = offsety[rotation][i] - offsety[newrot][i];
			}
		}
		
		if(shift(sx, sy, 2)) {
			for(int i = 0; i < 4; i++){
				wang[i] = wang[i].rotate(a);
			}
			rotation = newrot;
			countCells();
		} else {
			x = oldx;
			y = oldy;
		}
		
		shift(0,0,2);
	}
	
	private void countCells() {
		cells = 0;
		boolean[] filled = new boolean[matrix.getWidth()];
		for(int i = 0; i < 4; i++)
			if(!filled[x[i]]) {
				filled[x[i]] = true;
				cells++;
			}
	}
	
	public void fullyMove(int x, int y){
		int j = 0;
		while(canShift(x*j, y*j)) j++; //Check how far down it can go
		j--; //Adjust for off-by-one error
		shift(x*j, y*j, 0);
	}

	/**
	 * @return the randomized
	 */
	public boolean isRandomized() {
		return randomized;
	}

	/**
	 * @param randomized the randomized to set
	 */
	public void setRandomized(boolean randomized) {
		this.randomized = randomized;
	}

	/**
	 * @return the randomamount
	 */
	public int getRandomamount() {
		return randomamount;
	}

	/**
	 * @param randomamount the randomamount to set
	 */
	public void setRandomamount(int randomamount) {
		this.randomamount = randomamount;
	}

	/**
	 * @return the randomperpiece
	 */
	public boolean isRandomperpiece() {
		return randomperpiece;
	}

	/**
	 * @param randomperpiece the randomperpiece to set
	 */
	public void setRandomperpiece(boolean randomperpiece) {
		this.randomperpiece = randomperpiece;
	}
}
