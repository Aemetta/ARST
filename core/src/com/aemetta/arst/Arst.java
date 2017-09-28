package com.aemetta.arst;

import com.aemetta.arst.gamemodes.Gamemode;
import com.aemetta.arst.gamemodes.Marathon;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	AssetManager manager;
	
	Texture background;
	
	Gamemode game;
	
	Preferences prefs;
	
	Renderer renderer;
	
	@Override
	public void create () {
		cam = new OrthographicCamera();
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("Backgrounds/017.png", Texture.class);
		//TODO add random backgrounds
		
		game = new Marathon();
		
		renderer = new Renderer("purple-20", "candy-20", game.players);
		/*
		Controllers.addListener(new ControllerAdapter () {
			@Override
			public boolean buttonDown(Controller controller, int buttonCode) {
				if(buttonCode == 1) human2.setInput(Player.ROTATE_RIGHT, true);
				if(buttonCode == 0) human2.setInput(Player.ROTATE_LEFT, true);
				if(buttonCode == 4) human2.setInput(Player.HOLD, true);
				if(buttonCode == 5) human2.setInput(Player.HOLD, true);
				return true;
			}
			@Override
			public boolean buttonUp(Controller controller, int buttonCode) {
				if(buttonCode == 1) human2.setInput(Player.ROTATE_RIGHT, false);
				if(buttonCode == 0) human2.setInput(Player.ROTATE_LEFT, false);
				if(buttonCode == 4) human2.setInput(Player.HOLD, false);
				if(buttonCode == 5) human2.setInput(Player.HOLD, false);
				return true;
			}
			@Override
			public boolean axisMoved(Controller controller, int axisCode, float value) {
				return true;
			}
			@Override
			public boolean povMoved(Controller controller, int povCode, PovDirection value) {
				if(value == PovDirection.east) human2.setInput(Player.RIGHT, true);
				if(value == PovDirection.west) human2.setInput(Player.LEFT, true);
				if(value == PovDirection.north) human2.setInput(Player.HARD_DROP, true);
				if(value == PovDirection.south) human2.setInput(Player.SOFT_DROP, true);
				if(value == PovDirection.center) {
					human2.setInput(Player.LEFT, false);
					human2.setInput(Player.RIGHT, false);
				}
				return true;
			}
		});*/
		
		Gdx.input.setInputProcessor(new InputAdapter () {
			
			public boolean keyDown (int keycode) {
					if(keycode == Keys.LEFT) game.setInput(Player.LEFT, true);
					if(keycode == Keys.RIGHT) game.setInput(Player.RIGHT, true);
					if(keycode == Keys.DOWN) game.setInput(Player.SOFT_DROP, true);
					if(keycode == Keys.UP) game.setInput(Player.HARD_DROP, true);
					if(keycode == Keys.SPACE) game.setInput(Player.DEPLOY, true);
					if(keycode == Keys.C) game.setInput(Player.HOLD, true);
					if(keycode == Keys.Z) game.setInput(Player.ROTATE_LEFT, true);
					if(keycode == Keys.X) game.setInput(Player.ROTATE_RIGHT, true);
					return true;
			   }

			   public boolean keyUp (int keycode) {
				   if(keycode == Keys.LEFT) game.setInput(Player.LEFT, false);
					if(keycode == Keys.RIGHT) game.setInput(Player.RIGHT, false);
					if(keycode == Keys.DOWN) game.setInput(Player.SOFT_DROP, false);
					if(keycode == Keys.UP) game.setInput(Player.HARD_DROP, false);
					if(keycode == Keys.SPACE) game.setInput(Player.DEPLOY, false);
					if(keycode == Keys.C) game.setInput(Player.HOLD, false);
					if(keycode == Keys.Z) game.setInput(Player.ROTATE_LEFT, false);
					if(keycode == Keys.X) game.setInput(Player.ROTATE_RIGHT, false);
					return true;
			   }
			});
		
		manager.finishLoading();
		background = manager.get("Backgrounds/017.png", Texture.class);
	}

	public void render () {
		float delta = Gdx.graphics.getRawDeltaTime();
		game.act(delta);
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		batch.draw(background, -background.getWidth()/2, -background.getHeight()/2);
		renderer.draw(batch, cam);
		
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
		manager.dispose();
		renderer.dispose();
	}
}
