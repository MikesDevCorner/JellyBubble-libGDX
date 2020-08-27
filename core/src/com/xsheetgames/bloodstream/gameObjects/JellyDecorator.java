package com.xsheetgames.bloodstream.gameObjects;

import com.xsheetgames.bloodstream.elements.iContactableGameObject;

public class JellyDecorator implements iContactableGameObject {

	private Jelly jelly;
	
	
	public JellyDecorator(Jelly jelly) {
		this.jelly = jelly;
	}
	
	public Jelly getJelly() {
		return this.jelly;
	}

	@Override
	public GameObject getGameObject() {
		return jelly;
	}
	
}
