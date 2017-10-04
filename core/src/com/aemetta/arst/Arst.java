package com.aemetta.arst;

import com.aemetta.arst.gamemodes.Gamemode;
import com.aemetta.arst.gamemodes.LineClear;
import com.aemetta.arst.gamemodes.Marathon;
import com.aemetta.arst.gamemodes.Ultra;
import com.aemetta.arst.gamemodes.Versus;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Cursor.SystemCursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;

public class Arst extends ApplicationAdapter {
	
	SpriteBatch batch;
	OrthographicCamera cam;
	
	Gamemode game;
	Preferences prefs;
	
	private int[] controls = new int[13];

	final static public int MENU_LEFT = 0;
	final static public int MENU_RIGHT = 1;
	final static public int MENU_UP = 2;
	final static public int MENU_DOWN = 3;
	final static public int LEFT = 4;
	final static public int RIGHT = 5;
	final static public int HARD_DROP = 6;
	final static public int SOFT_DROP = 7;
	final static public int ROTATE_LEFT = 8;
	final static public int ROTATE_RIGHT = 9;
	final static public int ROTATE_180 = 10;
	final static public int HOLD = 11;
	final static public int DEPLOY = 12;
	
	@Override
	public void create () {
		cam = new OrthographicCamera();
		batch = new SpriteBatch();
		
		game = new Marathon();
		game.init();
		
		prefs = Gdx.app.getPreferences("arst");
		loadPrefs();
		applyPrefs();
		
		if(game.player2 != null)
		Controllers.addListener(new ControllerAdapter () {
			@Override
			public boolean buttonDown(Controller controller, int buttonCode) {
				if(buttonCode == 0) game.player2.input(ROTATE_LEFT, true);
				if(buttonCode == 1) game.player2.input(ROTATE_RIGHT, true);
				if(buttonCode == 4) game.player2.input(HOLD, true);
				if(buttonCode == 5) game.player2.input(HOLD, true);
				return true;
			}
			@Override
			public boolean buttonUp(Controller controller, int buttonCode) {
				if(buttonCode == 0) game.player2.input(ROTATE_LEFT, false);
				if(buttonCode == 1) game.player2.input(ROTATE_RIGHT, false);
				if(buttonCode == 4) game.player2.input(HOLD, false);
				if(buttonCode == 5) game.player2.input(HOLD, false);
				return true;
			}
			@Override
			public boolean axisMoved(Controller controller, int axisCode, float value) {
				return true;
			}
			@Override
			public boolean povMoved(Controller controller, int povCode, PovDirection value) {
				if(value == PovDirection.east) game.player2.input(RIGHT, true);
				if(value == PovDirection.west) game.player2.input(LEFT, true);
				if(value == PovDirection.north) game.player2.input(HARD_DROP, true);
				if(value == PovDirection.south) game.player2.input(SOFT_DROP, true);
				if(value == PovDirection.center) {
					game.player2.input(LEFT, false);
					game.player2.input(RIGHT, false);
					game.player2.input(SOFT_DROP, false);
					
				}
				return true;
			}
			
		});
		
		if(game.player1 != null)
		Gdx.input.setInputProcessor(new InputAdapter () {
			
			public boolean keyDown (int keycode) {
					
				//Debug hotkeys
				if(keycode == Keys.F2) game.players[0].getLevelTracker().clearLines(5);
				
				for(int i = 0; i < controls.length; i++)
					if(keycode == controls[i]) {
						game.player1.input(i, true);
					}
				
				return true;
			}

			public boolean keyUp (int keycode) {
					
				for(int i = 0; i < controls.length; i++)
					if(keycode == controls[i]) {
						game.player1.input(i, false);
					}
				
				return true;
			}
			
			public boolean mouseMoved(int x, int y) {
				
				CursorPos cp = new CursorPos(x, y, game.displays[0]);
				if(cp.valid) Gdx.graphics.setSystemCursor(SystemCursor.Crosshair);
				else Gdx.graphics.setSystemCursor(SystemCursor.Arrow);
				return true;
			}
			
			public boolean touchDown (int x, int y, int pointer, int button) {
				
				Gdx.graphics.setSystemCursor(SystemCursor.Hand);
				
				return true;
			}
			
			});
	}
	
	private void loadPrefs() {
		
		controls[0] = prefs.getInteger("Menu Left", Keys.LEFT);
		controls[1] = prefs.getInteger("Menu Right", Keys.RIGHT);
		controls[2] = prefs.getInteger("Menu Up", Keys.UP);
		controls[3] = prefs.getInteger("Menu Down", Keys.DOWN);
		controls[4] = prefs.getInteger("Left", Keys.LEFT);
		controls[5] = prefs.getInteger("Right", Keys.RIGHT);
		controls[6] = prefs.getInteger("Hard Drop", Keys.UP);
		controls[7] = prefs.getInteger("Soft Drop", Keys.DOWN);
		controls[8] = prefs.getInteger("Rotate Left", Keys.Z);
		controls[9] = prefs.getInteger("Rotate Right", Keys.X);
		controls[10] = prefs.getInteger("Rotate 180", Keys.C);
		controls[11] = prefs.getInteger("Hold", Keys.SPACE);
		controls[12] = prefs.getInteger("Deploy", Keys.V);
	}
	
	private void writePrefs() {
		
		prefs.putInteger("Menu Left", controls[0]);
		prefs.putInteger("Menu Right", controls[1]);
		prefs.putInteger("Menu Up", controls[2]);
		prefs.putInteger("Menu Down", controls[3]);
		prefs.putInteger("Left", controls[4]);
		prefs.putInteger("Right", controls[5]);
		prefs.putInteger("Hard Drop", controls[6]);
		prefs.putInteger("Soft Drop", controls[7]);
		prefs.putInteger("Rotate Left", controls[8]);
		prefs.putInteger("Rotate Right", controls[9]);
		prefs.putInteger("Rotate 180", controls[10]);
		prefs.putInteger("Hold", controls[11]);
		prefs.putInteger("Deploy", controls[12]);
		
		prefs.flush();
	}
	
	private void applyPrefs() {
		if(game.player1 != null) {
			game.player1.setDAS(prefs.getInteger("P1 DAS", 200));
			game.player1.setARR(prefs.getInteger("P1 ARR", 80));
			game.player1.setDropRate(prefs.getInteger("P1 Drop", 75));
		}
		
		if(game.player2 != null) {
			game.player2.setDAS(prefs.getInteger("P2 DAS", 200));
			game.player2.setARR(prefs.getInteger("P2 ARR", 80));
			game.player2.setDropRate(prefs.getInteger("P2 Drop", 75));
			
		}
	}

	public void render () {
		float delta = Gdx.graphics.getRawDeltaTime();
		game.act(delta);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		game.draw(batch, cam);
		
		batch.end();
		
		Gdx.graphics.setTitle("" + Gdx.graphics.getFramesPerSecond());
	}
	
	public void resize(int width, int height) {
		cam.setToOrtho(false, width, height);
		cam.translate(-width/2,-height/2);
		cam.update();
	}
	
	@Override
	public void dispose () {
		writePrefs();
		
		batch.dispose();
		game.manager.dispose();
		for(Renderer d : game.displays)
			d.dispose();
	}
}
