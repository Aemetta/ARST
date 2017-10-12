package com.aemetta.arst;

import com.aemetta.arst.display.CursorPos;
import com.aemetta.arst.display.Display;
import com.aemetta.arst.gamemodes.Cheese;
import com.aemetta.arst.gamemodes.LineClear;
import com.aemetta.arst.gamemodes.Marathon;
import com.aemetta.arst.gamemodes.Ultra;
import com.aemetta.arst.gamemodes.Versus;
import com.aemetta.arst.menu.Menu;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;

public class Arst extends ApplicationAdapter {
	
	SpriteBatch batch;
	OrthographicCamera cam;
	
	String startupGamemode = null;
	Wrapper game;
	Preferences prefs;
	
	public static int[] controls = new int[15];
	public static String[] theme = new String[3];

	final static public int MENU_LEFT = 0;
	final static public int MENU_RIGHT = 1;
	final static public int MENU_UP = 2;
	final static public int MENU_DOWN = 3;
	final static public int MENU_SELECT = 4;
	final static public int MENU_BACK = 5;
	final static public int LEFT = 6;
	final static public int RIGHT = 7;
	final static public int HARD_DROP = 8;
	final static public int SOFT_DROP = 9;
	final static public int ROTATE_LEFT = 10;
	final static public int ROTATE_RIGHT = 11;
	final static public int ROTATE_180 = 12;
	final static public int HOLD = 13;
	final static public int DEPLOY = 14;
	
	boolean debug = false;
	
	@Override
	public void create () {
		
		cam = new OrthographicCamera();
		batch = new SpriteBatch();
		
		prefs = Gdx.app.getPreferences("arst");
		debug = prefs.getBoolean("Debug Mode", false);
		
		loadControls();
		loadTheme();
		
		if(startupGamemode != null) newGame(startupGamemode);
		else newGame("");
	}
	
	public void newGame(String gamemode) {
		
		if(gamemode.contentEquals("marathon")) game = new Marathon(this);
		else if(gamemode.contentEquals("lineclear")) game = new LineClear(this);
		else if(gamemode.contentEquals("line clear")) game = new LineClear(this);
		else if(gamemode.contentEquals("ultra")) game = new Ultra(this);
		else if(gamemode.contentEquals("cheese")) game = new Cheese(this);
		else if(gamemode.contentEquals("versus")) game = new Versus(this);
		else game = new Menu(this);
		
		game.init(prefs);
		
		if(game.human2 != null)
			Controllers.addListener(new ControllerAdapter () {
				@Override
				public boolean buttonDown(Controller controller, int buttonCode) {
					if(buttonCode == 0) game.human2.input(ROTATE_LEFT, true);
					if(buttonCode == 1) game.human2.input(ROTATE_RIGHT, true);
					if(buttonCode == 4) game.human2.input(HOLD, true);
					if(buttonCode == 5) game.human2.input(HOLD, true);
					return true;
				}
				@Override
				public boolean buttonUp(Controller controller, int buttonCode) {
					if(buttonCode == 0) game.human2.input(ROTATE_LEFT, false);
					if(buttonCode == 1) game.human2.input(ROTATE_RIGHT, false);
					if(buttonCode == 4) game.human2.input(HOLD, false);
					if(buttonCode == 5) game.human2.input(HOLD, false);
					return true;
				}
				@Override
				public boolean axisMoved(Controller controller, int axisCode, float value) {
					return true;
				}
				@Override
				public boolean povMoved(Controller controller, int povCode, PovDirection value) {
					if(value == PovDirection.east) game.human2.input(RIGHT, true);
					if(value == PovDirection.west) game.human2.input(LEFT, true);
					if(value == PovDirection.north) game.human2.input(HARD_DROP, true);
					if(value == PovDirection.south) game.human2.input(SOFT_DROP, true);
					if(value == PovDirection.center) {
						game.human2.input(LEFT, false);
						game.human2.input(RIGHT, false);
						game.human2.input(SOFT_DROP, false);
						
					}
					return true;
				}
				
			});
			
			if(game.human1 != null)
			Gdx.input.setInputProcessor(new InputAdapter () {
				
				public boolean keyDown (int keycode) {
					
					if(game.human1.isRawInput())
						game.human1.input(keycode, true);
					else {
						//Debug hotkeys
						if(debug) game.debug(keycode);
						
						for(int i = 0; i < controls.length; i++)
							if(keycode == controls[i]) {
								game.human1.input(i, true);
							}
					}
					
					return true;
				}

				public boolean keyUp (int keycode) {
						
					for(int i = 0; i < controls.length; i++)
						if(keycode == controls[i]) {
							game.human1.input(i, false);
						}
					
					return true;
				}
				
				public boolean mouseMoved(int x, int y) {
					
				//	CursorPos cp = new CursorPos(x, y, game.displays[0]);
				//	if(cp.valid) Gdx.graphics.setSystemCursor(SystemCursor.Crosshair);
				//	else Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
					return true;
				}
				
				public boolean touchDown (int x, int y, int pointer, int button) {
					
					Gdx.graphics.setSystemCursor(SystemCursor.Hand);
					
					return true;
				}
				
				});
	}
	
	public void loadControls() {
		controls[0] = prefs.getInteger("Menu Left", Keys.LEFT);
		controls[1] = prefs.getInteger("Menu Right", Keys.RIGHT);
		controls[2] = prefs.getInteger("Menu Up", Keys.UP);
		controls[3] = prefs.getInteger("Menu Down", Keys.DOWN);
		controls[4] = prefs.getInteger("Menu Select", Keys.ENTER);
		controls[5] = prefs.getInteger("Menu Back", Keys.ESCAPE);
		controls[6] = prefs.getInteger("Left", Keys.LEFT);
		controls[7] = prefs.getInteger("Right", Keys.RIGHT);
		controls[8] = prefs.getInteger("Hard Drop", Keys.UP);
		controls[9] = prefs.getInteger("Soft Drop", Keys.DOWN);
		controls[10] = prefs.getInteger("Rotate Left", Keys.Z);
		controls[11] = prefs.getInteger("Rotate Right", Keys.X);
		controls[12] = prefs.getInteger("Rotate 180", Keys.C);
		controls[13] = prefs.getInteger("Hold", Keys.SPACE);
		controls[14] = prefs.getInteger("Deploy", Keys.V);
	}
	
	public void loadTheme() {
		theme[0] = prefs.getString("Menu", "default");
		theme[1] = prefs.getString("Playfield", "purple-20");
		theme[2] = prefs.getString("Minos", "candy-20");
	}

	public void render () {
		float delta = Gdx.graphics.getRawDeltaTime();
		game.act(delta);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		game.draw(batch, cam);
		
		batch.end();
	}
	
	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
		cam.translate(-width/2,-height/2);
		cam.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		game.manager.dispose();
		for(Display d : game.displays)
			d.dispose();
	}
	
	public void setStartupGamemode(String g) {
		startupGamemode = g;
	}
}
