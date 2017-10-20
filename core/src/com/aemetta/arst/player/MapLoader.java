package com.aemetta.arst.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

public class MapLoader {
	
	public static Matrix load(String path) {
		
		Pixmap p = new Pixmap(Gdx.files.internal(path));
		Matrix m = new Matrix(p.getWidth(), p.getHeight());
		for(int x = 0; x < m.getWidth(); x++)
			for(int y = 0; y < m.getHeight(); y++) {
				int v = p.getPixel(x, y);
				switch(v) {
				case 0xffffffff: set(m, x, y, Color.Empty); break;
				case 0x00ffffff: set(m, x, y, Color.Cyan); break;
				case 0xffff00ff: set(m, x, y, Color.Yellow); break;
				case 0xff00ffff: set(m, x, y, Color.Magenta); break;
				case 0xff0000ff: set(m, x, y, Color.Red); break;
				case 0x00ff00ff: set(m, x, y, Color.Green); break;
				case 0x0000ffff: set(m, x, y, Color.Blue); break;
				case 0xff8800ff: set(m, x, y, Color.Orange); break;
				case 0x888888ff: set(m, x, y, Color.Light); break;
				case 0x444444ff: set(m, x, y, Color.Light); break;
				}
			}
		
		m.smooth(0, m.getHeight()-1);
		return m;
	}
	
	private static void set(Matrix m, int x, int y, Color c) {
		m.setSquare(x, m.getHeight()-m.getTopOffset()-y-1, c, Wang.LONER);
		if(c != Color.Empty)
			m.makeSolid(x, m.getHeight()-m.getTopOffset()-y-1);
	}
}
