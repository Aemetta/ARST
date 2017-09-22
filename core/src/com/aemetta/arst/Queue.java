package com.aemetta.arst;

import java.util.Random;

public class Queue {
	
	Shape[] que = new Shape[10];
	Shape held = null;
	boolean justHeld = false;
	Random rand;
	int[] inventory = new int[7];
	int index = 7;
	
	public Queue(long seed){
		rand = new Random(seed);
		for(int i = 0; i < que.length; i++){
			que[i] = next();
		}
	}
	
	public Shape pull(){
		Shape p = que[0];
		for(int i = 0; i < que.length-1; i++)
			que[i] = que[i+1];
		que[que.length-1] = next();
		return p;
	}
	
	public Shape next(){
		int c = 0;
		
		if(index < 7){
			c = inventory[index];
			index++;
		}else{
			double place[] = new double[7];
			for(int i = 0; i < 7; i++)
				place[i] = rand.nextDouble();
			for(int j = 0; j < 7; j++){
				int top = 0;
				for(int i = 0; i < 7; i++)
					if(place[i] > place[top]) top = i;
				inventory[j] = top;
				place[top] = 0;
			}
			
			c = inventory[0];
			index = 1;
		}
		
		Shape t;
		switch(c) {
		case 0: t = Shape.I; break;
		case 1: t = Shape.O; break;
		case 2: t = Shape.T; break;
		case 3: t = Shape.Z; break;
		case 4: t = Shape.S; break;
		case 5: t = Shape.J; break;
		case 6: t = Shape.L; break;
		default: t = null;
		}
		
		return t;
	}
	
	public Shape hold(Shape put){
		if(!justHeld) {
			Shape h = held;
			held = put;
			justHeld = true;
			return h;
		} else return put;
	}
	
	public void resetHeldStatus() {
		justHeld = false;
	}
}
