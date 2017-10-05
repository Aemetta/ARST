package com.aemetta.arst.display;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public interface Display extends Disposable {
	
	public void init(AssetManager manager);
	public void draw(Batch batch, OrthographicCamera cam);
}
