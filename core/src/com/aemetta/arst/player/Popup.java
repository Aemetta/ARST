package com.aemetta.arst.player;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Popup {
	
	TextureAtlas atlas;
	public TextureRegion image1;
	public TextureRegion image2;
	
	long life = 0;
	boolean end = false;
	
	public static final int END_TIMER = 0;
	public static final int END_BAD = 1;
	public static final int END_NEUTRAL = 2;
	public static final int END_GOOD = 3;
	
	public Popup() {
	}
	
	public void setAtlas(TextureAtlas a) {
		atlas = a;
	}
	
	public void create(int lines, boolean tspin, boolean btb, int combo, int height) {
		
		image1 = null;
		if(tspin) {
			if(btb) {
				switch(lines) {
				case 0: set(1, "tspin-btb"); break;
				case 1: set(1, "ts-single-btb"); break;
				case 2: set(1, "ts-double-btb"); break;
				case 3: set(1, "ts-triple-btb"); break;
				}
			} else {
				switch(lines) {
				case 0: set(1, "tspin"); break;
				case 1: set(1, "ts-single"); break;
				case 2: set(1, "ts-double"); break;
				case 3: set(1, "ts-triple"); break;
				}
			}
		} else {
			switch(lines) {
			case 1: set(1, "single"); break;
			case 2: set(1, "double"); break;
			case 3: set(1, "triple"); break;
			case 4: 
				if(btb)set(1, "quadra-btb");
				else set(1, "quadra");
				break;
			}
		}
		
		switch(combo) {
		case 0: image2 = null; break;
		case 1: set(2, "combo-2"); break;
		case 2: set(2, "combo-3"); break;
		case 3: set(2, "combo-4"); break;
		case 4: set(2, "combo-5"); break;
		case 5: set(2, "combo-6"); break;
		case 6: set(2, "combo-7"); break;
		case 7: set(2, "combo-8"); break;
		case 8: set(2, "combo-9"); break;
		case 9: set(2, "combo-10"); break;
		case 10: set(2, "combo-11"); break;
		case 11: set(2, "combo-12"); break;
		}
		
		if(image1 != null)
			life = 500;
	}
	
	public void perfectClear() {
		set(1, "perfect-clear");
		life = 1000;
	}
	
	public void create(boolean singleplayer, int place) {
		image1 = null;
		if(singleplayer)
			switch(place) {
			case END_TIMER: set(1, "end-timer"); break;
			case END_BAD: set(1, "end-bad"); break;
			case END_NEUTRAL: set(1, "end-neutral"); break;
			case END_GOOD: set(1, "end-good"); break;
			}
		else
			set(1, "end-place-" + place);
		
		if(image1 != null) {
			life = 3000;
			end = true;
		}
	}
	
	private void set(int i, String thing) {
		if(i==1)image1 = atlas.findRegion(thing);
		if(i==2)image2 = atlas.findRegion(thing);
	}
	
	public boolean update(long delta) {
		life -= delta;
		if(life <= 0) {
			image1 = null;
			if(end) return false;
		}
		return true;
	}
}
