package com.aemetta.arst;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

public class MapLoader {
	
	public static Matrix load(String path) {
		
		Pixmap p = new Pixmap(Gdx.files.internal(path));
		Matrix m = new Matrix(p.getWidth(), p.getHeight());

		for(int x = 0; x < m.WIDTH; x++)
			for(int y = 0; y < m.HEIGHT; y++) {
				int v = p.getPixel(x, y);
				switch(v) {
				case 0xffffffff: set(m, x, y, 0); break;
				case 0x00ffffff: set(m, x, y, 1); break;
				case 0xffff00ff: set(m, x, y, 2); break;
				case 0xff00ffff: set(m, x, y, 3); break;
				case 0xff0000ff: set(m, x, y, 4); break;
				case 0x00ff00ff: set(m, x, y, 5); break;
				case 0x0000ffff: set(m, x, y, 6); break;
				case 0xff8800ff: set(m, x, y, 7); break;
				case 0x888888ff: set(m, x, y, 8); break;
				}
			}
		
		m.smooth(0, m.HEIGHT-1);
		return m;
	}
	
	private static void set(Matrix m, int x, int y, int c) {
		m.setSquare(x, m.HEIGHT-m.TOP-y-1, c, Wang.LONER);
		if(c != 0)
			m.makeSolid(x, m.HEIGHT-m.TOP-y-1);
	}
}
