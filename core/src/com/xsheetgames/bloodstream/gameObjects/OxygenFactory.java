package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.physics.box2d.World;


public class OxygenFactory extends GameObjectFactory {
	
	private World world;
	
	
	public OxygenFactory(Class<? extends GameObject> c, World w) {
		super(c);
		this.world = w;
	}

	@Override
	public GameObject createObject() {
		return new Oxygen(this.world, 20f, 20f);
	}

}