package com.aemetta.arst.player;

import java.util.Arrays;
import java.util.Random;

public class Garbage {
	
	final Matrix matrix;
	final Random rand;
	private int warning = 0;
	int[] fillings = new int[]{};
	int lastHole = -1;
	
	public Garbage(Matrix b, long s){
		matrix = b;
		rand = new Random(s << 1); //Seed is bitshifted to the left to avoid correllation between queue and garbage
	}
	
	public void add(int amt){
		if(amt==0) return;
		setWarningAmount(getWarningAmount() + amt);
		fillings = Arrays.copyOf(fillings, fillings.length+1);
		fillings[fillings.length-1] = amt;
	}
	
	public int remove(int amt){
		while(amt > 0){
			if(fillings.length != 0)
				if(fillings[0] > amt){
					fillings[0] -= amt;
					setWarningAmount(getWarningAmount() - amt);
					amt = 0;
				}
				else{
					amt -= fillings[0];
					setWarningAmount(getWarningAmount() - fillings[0]);
					fillings = Arrays.copyOfRange(fillings, 1, fillings.length);
				}
			else
				return amt;
		}
		return 0;
	}
	
	public void fill(){
		setWarningAmount(0);
		for(int z = 0; z < fillings.length; z++){
			int a = fillings[z];
			for(int i = 0; i < a; i++)
				matrix.shift(0, true);
			int b = 0;
			do {
				b = (int)(rand.nextDouble()*matrix.getWidth());
			} while(b == lastHole);
			lastHole = b;
			for(int i = 0; i < a; i++)
				for(int j = 0; j < matrix.getWidth(); j++){
					if(j==b) continue;
					matrix.makeSolid(j, i);
					matrix.setSquare(j, i, 9, Wang.LONER);
				}
			matrix.smooth(0, a-1);
		}
		fillings = new int[]{};
	}

	/**
	 * @return the warning
	 */
	public int getWarningAmount() {
		return warning;
	}

	/**
	 * @param warning the warning to set
	 */
	public void setWarningAmount(int warning) {
		this.warning = warning;
	}
}
