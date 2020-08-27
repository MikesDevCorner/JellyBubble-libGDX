package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.physics.box2d.World;

public class DropzoneFactory  extends GameObjectFactory {
	
	private World world;
	
	
	public DropzoneFactory(Class<? extends GameObject> c, World w) {
		super(c);
		this.world = w;
	}

	@Override
	public GameObject createObject() {
		return new Dropzone(this.world, 20f, -20f);
	}

}