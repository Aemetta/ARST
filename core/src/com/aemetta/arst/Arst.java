package com.aemetta.arst;

import com.aemetta.arst.display.Display;
import com.aemetta.arst.gamemodes.Cheese;
import com.aemetta.arst.gamemodes.LineClear;
import com.aemetta.arst.gamemodes.Marathon;
import com.aemetta.arst.gamemodes.Ultra;
import com.aemetta.arst.gamemodes.Versus;
import com.aemetta.arst.menu.Menu;
import com.aemetta.arst.menu.MenuLayout;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;

public class Arst extends ApplicationAdapter {
	
	SpriteBatch batch;
	OrthographicCamera cam;
	
	String startupGamemode = null;
	Wrapper game;
	Preferences prefs;
	
	public static int[] p1controls = new int[9];
	public static int[] p2controls = new int[9];
	public static int[] menucontrols = new int[9];
	public static String[] theme = new String[3];

	final static public int MENU_LEFT = 0;
	final static public int MENU_RIGHT = 1;
	final static public int MENU_UP = 2;
	final static public int MENU_DOWN = 3;
	final static public int MENU_SELECT = 4;
	final static public int MENU_BACK = 5;
	final static public int MENU_PAUSE = 6;
	
	final static public int LEFT = 0;
	final static public int RIGHT = 1;
	final static public int UP = 2;
	final static public int DOWN = 3;
	final static public int SELECT = 4;
	final static public int BACK = 5;
	final static public int PAUSE = 6;
	
	final static public int HARD_DROP = 2;
	final static public int SOFT_DROP = 3;
	final static public int ROTATE_LEFT = 4;
	final static public int ROTATE_RIGHT = 5;
	final static public int ROTATE_180 = 6;
	final static public int HOLD = 7;
	final static public int DEPLOY = 8;
	
	boolean debug = false;
	
	@Override
	public void create () {
		
		cam = new OrthographicCamera();
		batch = new SpriteBatch();
		
		prefs = Gdx.app.getPreferences("arst");
		debug = prefs.getBoolean("Debug Mode", false);
		
		MenuLayout.P1Controls.init(prefs);
		MenuLayout.P2Controls.init(prefs);
		MenuLayout.MenuControls.init(prefs);
		MenuLayout.Tuning.init(prefs);
		MenuLayout.Theme.init(prefs);
		MenuLayout.Audio.init(prefs);
		
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

		newGame();
	}
	
	public void newGame(Wrapper wrapper) {
		game = wrapper;
		newGame();
	}
	
	public void newGame() {
		game.init(prefs);
			
			Gdx.input.setInputProcessor(new InputAdapter () {
				
				public boolean keyDown (int keycode) {
					
					//Debug hotkeys
					if(debug) game.debug(keycode);
					
					if(game.human1 != null && game.human1.isRawInput()) {
						game.human1.input(keycode, true);
						return true;
					}
					
					for(int i = 0; i < menucontrols.length; i++)
						if(keycode == menucontrols[i]) {
							game.input(0, i, true);
						}
					for(int i = 0; i < p1controls.length; i++)
						if(keycode == p1controls[i]) {
							game.input(1, i, true);
						}
					for(int i = 0; i < p2controls.length; i++)
						if(keycode == p2controls[i]) {
							game.input(2, i, true);
						}
					
					return true;
				}

				public boolean keyUp (int keycode) {
					

					for(int i = 0; i < menucontrols.length; i++)
						if(keycode == menucontrols[i]) {
							game.input(0, i, false);
						}
					for(int i = 0; i < p1controls.length; i++)
						if(keycode == p1controls[i]) {
							game.input(1, i, false);
						}
					for(int i = 0; i < p2controls.length; i++)
						if(keycode == p2controls[i]) {
							game.input(2, i, false);
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
		menucontrols[0] = prefs.getInteger("Controls Menu Left", Keys.LEFT);
		menucontrols[1] = prefs.getInteger("Controls Menu Right", Keys.RIGHT);
		menucontrols[2] = prefs.getInteger("Controls Menu Up", Keys.UP);
		menucontrols[3] = prefs.getInteger("Controls Menu Down", Keys.DOWN);
		menucontrols[4] = prefs.getInteger("Controls Menu Select", Keys.ENTER);
		menucontrols[5] = prefs.getInteger("Controls Menu Back", Keys.ESCAPE);
		menucontrols[6] = prefs.getInteger("Controls Pause", Keys.ESCAPE);
		
		p1controls[0] = prefs.getInteger("Controls P1 Left", Keys.LEFT);
		p1controls[1] = prefs.getInteger("Controls P1 Right", Keys.RIGHT);
		p1controls[2] = prefs.getInteger("Controls P1 Hard Drop", Keys.UP);
		p1controls[3] = prefs.getInteger("Controls P1 Soft Drop", Keys.DOWN);
		p1controls[4] = prefs.getInteger("Controls P1 Rotate Left", Keys.Z);
		p1controls[5] = prefs.getInteger("Controls P1 Rotate Right", Keys.X);
		p1controls[6] = prefs.getInteger("Controls P1 Rotate 180", Keys.C);
		p1controls[7] = prefs.getInteger("Controls P1 Hold", Keys.SPACE);
		p1controls[8] = prefs.getInteger("Controls P1 Deploy", Keys.V);
		
		p2controls[0] = prefs.getInteger("Controls P2 Left", Keys.DPAD_LEFT);
		p2controls[1] = prefs.getInteger("Controls P2 Right", Keys.DPAD_RIGHT);
		p2controls[2] = prefs.getInteger("Controls P2 Hard Drop", Keys.DPAD_UP);
		p2controls[3] = prefs.getInteger("Controls P2 Soft Drop", Keys.DPAD_DOWN);
		p2controls[4] = prefs.getInteger("Controls P2 Rotate Left", Keys.BUTTON_A);
		p2controls[5] = prefs.getInteger("Controls P2 Rotate Right", Keys.BUTTON_B);
		p2controls[6] = prefs.getInteger("Controls P2 Rotate 180", Keys.BUTTON_Y);
		p2controls[7] = prefs.getInteger("Controls P2 Hold", Keys.BUTTON_R1);
		p2controls[8] = prefs.getInteger("Controls P2 Deploy", Keys.BUTTON_L1);
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
