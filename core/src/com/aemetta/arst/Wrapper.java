package com.aemetta.arst;

import com.aemetta.arst.display.Display;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Wrapper {

	public AssetManager manager;
	public Display[] displays;
	public Texture background;
	
	public Controllable human1;
	public Controllable human2;
	abstract public void init();
	abstract public void act(float delta);
	abstract public void draw(SpriteBatch batch, OrthographicCamera cam);
	abstract public void debug(int keycode);
}