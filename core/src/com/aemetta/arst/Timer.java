package com.aemetta.arst;

public class Timer {

	long time;
	final boolean countUp;
	
	public Timer(int seconds) {
		if(seconds==0) {
			time = 0;
			countUp = true;
		} else {
			time = seconds * 1000;
			countUp = false;
		}
	}
	
	public boolean update(long ms) {
		if(countUp) {
			time += ms;
			return true;
		}
		else {
			time -= ms;
			if(time <= 0) return false;
			else return true;
		}
	}
	
	public String view() {
		long a = (time / 1000) / 60;
		long b = (time / 1000) % 60;
		if(b<10) return a + ":0" + b;
		else return a + ":" + b;
	}
}
