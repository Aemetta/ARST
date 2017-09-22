package com.aemetta.arst;

import java.util.Arrays;

public class Piece {
	
	Matrix matrix;
	Queue queue;
	Score score;
	Player player;
	
	Shape shape;
	
	int[] x;
	int[] y;
	int[] gx = new int[4];
	int[] gy = new int[4];
	Wang[] wang = new Wang[4]; //The shape is what image to use for the mino
	int[] texture = new int[4];
	
	int cells = 0; //number of cells it takes up horizontally
	int warn = 0; //0 falling, 1 on ground, 2 on ground and about to be placed
	
//	boolean moving = false; //so multiple threads aren't both yanking...

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
	
	boolean randomized = false;
	int randomamount = 0;
	boolean randomperpiece = false;
	
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
		if(!survive) player.lose();
		
		//T-spin check
				boolean tspin = false;
				if(shape==Shape.T){
					tspin = true;
					if(canShift(0,-1)) tspin = false;
					if(canShift(0,1)) tspin = false;
					if(canShift(-1,0)) tspin = false;
					if(canShift(1,0)) tspin = false;
				/*	if(x[2]!=x[1]) {
						if(!matrix.isSolid(x[2], y[0])) break;
						if(!matrix.isSolid(x[2], y[3])) break;
						tspin++;
					} else {
						if(!matrix.isSolid(x[0], y[2])) break;
						if(!matrix.isSolid(x[3], y[2])) break;
						tspin++;
					}*/
				}
		
		for(int i = 0; i < 4; i++)
			matrix.makeSolid(x[i],y[i]);
		
		if(hardDrop) cells *= 2;
		score.place(matrix.consolidate(), cells, tspin);
		
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
		
		x = shape.x.clone();
		y = shape.y.clone();
		wang = shape.wang.clone();
		for(int i = 0; i < 4; i++){
			gx[i] = x[i];
			gy[i] = y[i]; //Move the ghost away from its old position to avoid glitchy
		}
		
		if(randomized){
			if(randomperpiece)
				Arrays.fill(texture, (int)(Math.random()*randomamount));
			else{
				texture[0] = (int)(Math.random()*randomamount);
				texture[1] = (int)(Math.random()*randomamount);
				texture[2] = (int)(Math.random()*randomamount);
				texture[3] = (int)(Math.random()*randomamount);
			}
		}
		
		if(!shift(0,-2,2)) //Move the piece to its new current position
			player.lose();
		shift(0,-1,1);
		rotation = 0;
		countCells();
	}
	
	public boolean canShift(int sx, int sy){
		for(int i = 0; i < 4; i++)
			if(matrix.isSolid(x[i]+sx,y[i]+sy)) return false;
		return true;
	}
	
	public boolean shift(int sx, int sy, int mode){ //0 normal, 1 falling, 2 rotating/spawning
		if(!canShift(sx, sy)) return false;
		
		for(int i = 0; i < 4; i++){
			if(mode != 2)
				matrix.hideSquare(x[i], y[i]); //Hide the piece
			y[i] += sy; x[i] += sx; //Shift
		}
		if(mode != 1) placeGhost(); //The ghost won't move when falling, so there's a boolean to update the ghost
		for(int i = 0; i < 4; i++){
			matrix.setSquare(x[i], y[i], shape.color, wang[i], texture[i]); //Show the piece
		}
		
		return true;
	}
	
	public void placeGhost(){
		
		for(int i = 0; i < 4; i++){
			matrix.hideSquare(gx[i], gy[i]); //Hide the ghost
		}
		int j = 0;
		while(canShift(0, j)) j--; //Check how far down it can go
		j++; //Above code goes one more down than needed...off by one error
		if(j == 0) warn = 1; else{ //If the piece is on the ground
			warn = 0;
			for(int i = 0; i < 4; i++){
				gx[i] = x[i]; //Set the ghost's position
				gy[i] = y[i]+j;
				matrix.setSquare(gx[i], gy[i], 8, wang[i], texture[i]); //Show the ghost
			}
		}
	}
	
	public void rotate(int a){
		if(a<0)a+=4;          //1 is right, -1 is left...but 3 works, too
		if(a==0) return;	  //not rotating at all
		if(shape==Shape.O) return;	  //squares don't rotate
		
		int[] oldx = x.clone();
		int[] oldy = y.clone();
		
		for(int i = 0; i < 4; i++)
			matrix.hideSquare(x[i], y[i]); //Hide the piece
		
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
		boolean[] filled = new boolean[matrix.WIDTH];
		for(int i = 0; i < 4; i++)
			if(!filled[x[i]]) {
				filled[x[i]] = true;
				cells++;
			}
	}
	
	public void hardDrop(){
		int j = 0;
		while(canShift(0, j)) j--; //Check how far down it can go
		j++;
		shift(0, j, 0);
		place(true);
	}
}
