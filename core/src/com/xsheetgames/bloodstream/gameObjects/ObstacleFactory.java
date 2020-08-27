package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.physics.box2d.World;

public class ObstacleFactory extends GameObjectFactory {
	
	private World world;
	private String modelName, textureName;
	
	public ObstacleFactory(Class<? extends GameObject> c, World w, String modelName, String textureName) {
		super(c);
		this.world = w;
		this.modelName = modelName;
		this.textureName = textureName;
	}

	@Override
	public GameObject createObject() {
		return new Obstacle(this.world, -20f, -20f, this.modelName, this.textureName);
	}

}