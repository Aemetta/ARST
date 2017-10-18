package com.aemetta.arst;

public interface Controllable {
	
	public void input(int key, boolean pressed);
	public void act(float delta);
	public boolean isRawInput();
}
