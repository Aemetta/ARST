package com.aemetta.arst;

import com.aemetta.arst.player.Player;

public class CursorPos {
	
	int xPixel;
	int yPixel;
	Player player;
	int x;
	int y;
	
	boolean valid = false;
	
	public CursorPos(int x, int y, Renderer r) {
		xPixel = x;
		yPixel = y;
		
		player = null;
		for(Player p : r.getPlayers()) {
			int offx = p.xoffset + r.getPlayfieldConfig().matrixOffsetX;
			int offy = p.yoffset + r.getPlayfieldConfig().matrixOffsetY;
			int msize = r.getMinoConfig().size;
			int width = msize * p.matrix.getWidth();
			int height = msize * p.matrix.getHeight();
			if(	x > offx &&
				y > offy &&
				x < offx + width &&
				y < offy + height) {
				player = p;
				valid = true;
				
				x = (x - offx) / msize;
				y = (y - offy) / msize;
				
				break;
			}
		}
	}
}
