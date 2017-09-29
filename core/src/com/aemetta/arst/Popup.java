package com.aemetta.arst;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Popup {
	
	TextureAtlas atlas;
	TextureRegion image;
	TextureRegion image2;
	
	boolean alive = false;
	long life = 0;
	
	public Popup() {
	}
	
	public void setAtlas(TextureAtlas a) {
		atlas = a;
	}
	
	public void create(int lines, boolean tspin, boolean btb, int combo, int height) {
		image = null;
		if(tspin) {
			if(btb) {
				switch(lines) {
				case 0: image = atlas.findRegion("tspin-btb"); break;
				case 1: image = atlas.findRegion("ts-single-btb"); break;
				case 2: image = atlas.findRegion("ts-double-btb"); break;
				case 3: image = atlas.findRegion("ts-triple-btb"); break;
				}
			} else {
				switch(lines) {
				case 0: image = atlas.findRegion("tspin"); break;
				case 1: image = atlas.findRegion("ts-single"); break;
				case 2: image = atlas.findRegion("ts-double"); break;
				case 3: image = atlas.findRegion("ts-triple"); break;
				}
			}
		} else {
			switch(lines) {
			case 1: image = atlas.findRegion("single"); break;
			case 2: image = atlas.findRegion("double"); break;
			case 3: image = atlas.findRegion("triple"); break;
			case 4: 
				if(btb)image = atlas.findRegion("quadra-btb");
				else image = atlas.findRegion("quadra");
				break;
			}
		}
		
		switch(combo) {
		case 0: image2 = null; break;
		case 1: image2 = atlas.findRegion("combo-2"); break;
		case 2: image2 = atlas.findRegion("combo-3"); break;
		case 3: image2 = atlas.findRegion("combo-4"); break;
		case 4: image2 = atlas.findRegion("combo-5"); break;
		case 5: image2 = atlas.findRegion("combo-6"); break;
		case 6: image2 = atlas.findRegion("combo-7"); break;
		case 7: image2 = atlas.findRegion("combo-8"); break;
		case 8: image2 = atlas.findRegion("combo-9"); break;
		case 9: image2 = atlas.findRegion("combo-10"); break;
		case 10: image2 = atlas.findRegion("combo-11"); break;
		case 11: image2 = atlas.findRegion("combo-12"); break;
		}
		
		if(image != null) {
			alive = true;
			life = 500;
		}
	}
	
	public void create(boolean singleplayer, int place) {
		image = null;
		if(singleplayer)
			switch(place) {
			case 0: image = atlas.findRegion("end-timer"); break;
			case 1: image = atlas.findRegion("end-bad"); break;
			case 2: image = atlas.findRegion("end-neutral"); break;
			case 3: image = atlas.findRegion("end-good"); break;
			}
		else
			image = atlas.findRegion("end-place-" + place);
		
		if(image != null) {
			alive = true;
			life = 3000;
		}
	}
	
	public void update(long delta) {
		life -= delta;
		if(life <= 0) alive = false;
	}
}
