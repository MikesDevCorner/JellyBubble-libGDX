package com.xsheetgames.bloodstream.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;


public abstract class AbstractScreen implements Screen {
	protected Game game;
	
	/*********** CONTROLLER EVENTS ************/
	public abstract void stepBack(String source); //controller, touch oder keyboard
	public abstract void startPress();
	public abstract void primaryPress();
	public abstract void secondaryPress();
	public abstract void steerXAxis(float peculiarity);
	public abstract void steerYAxis(float peculiarity);
	public abstract boolean screenTouched(Vector2 touchPoint, int pointer);
}