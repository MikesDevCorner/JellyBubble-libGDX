package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.physics.box2d.World;

public class EnemyFactory extends GameObjectFactory {
	
	private World world;
	private int enemyType;
	
	public EnemyFactory(Class<? extends GameObject> c, World w, int enemyType) {
		super(c);
		this.enemyType = enemyType;
		this.world = w;
	}

	@Override
	public GameObject createObject() {
		switch(this.enemyType) {
			case EnemyAbstract.ENEMY_BIG: return new EnemyBig(this.world, -20f, -20f);
			case EnemyAbstract.ENEMY_SMALL: return new EnemySmall(this.world, -20f, -20f);
			default: return null;
		}
	}

}