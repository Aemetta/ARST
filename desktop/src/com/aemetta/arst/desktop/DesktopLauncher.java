package com.aemetta.arst.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.aemetta.arst.Arst;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		Arst a = new Arst();
		if(arg.length == 1)
			a.setStartupGamemode(arg[0].toLowerCase());
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(a, config);
	}
}
