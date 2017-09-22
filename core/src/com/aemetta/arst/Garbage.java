package com.aemetta.arst;

import java.util.Arrays;
import java.util.Random;

public class Garbage {
	
	final Matrix matrix;
	final Random rand;
	int warning = 0;
	int[] fillings = new int[]{};
	
	public Garbage(Matrix b, long s){
		matrix = b;
		rand = new Random(s << 1); //Seed is bitshifted to the left to avoid correllation between queue and garbage
	}
	
	public void add(int amt){
		if(amt==0) return;
		warning += amt;
		fillings = Arrays.copyOf(fillings, fillings.length+1);
		fillings[fillings.length-1] = amt;
	}
	
	public int remove(int amt){
		while(amt > 0){
			if(fillings.length != 0)
				if(fillings[0] > amt){
					fillings[0] -= amt;
					warning -= amt;
					amt = 0;
				}
				else{
					amt -= fillings[0];
					warning -= fillings[0];
					fillings = Arrays.copyOfRange(fillings, 1, fillings.length);
				}
			else
				return amt;
		}
		return 0;
	}
	
	public void fill(){
		warning = 0;
		for(int z = 0; z < fillings.length; z++){
			int a = fillings[z];
			for(int i = 0; i < a; i++)
				matrix.shift(0, true);
			int b = (int)(rand.nextDouble()*matrix.WIDTH);
			for(int i = 0; i < a; i++)
				for(int j = 0; j < matrix.WIDTH; j++){
					if(j==b) continue;
					matrix.makeSolid(j, i);
					matrix.setSquare(j, i, 9, Wang.LONER);
				}
			matrix.smooth(0, a-1);
		}
		fillings = new int[]{};
	}
}
