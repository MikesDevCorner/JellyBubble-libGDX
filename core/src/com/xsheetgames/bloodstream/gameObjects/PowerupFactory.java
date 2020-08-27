package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.physics.box2d.World;

public class PowerupFactory extends GameObjectFactory {
	
	private World world;
	private int powerupType;
	
	public PowerupFactory(Class<? extends GameObject> c, World w, int powerupType) {
		super(c);
		this.world = w;
		this.powerupType = powerupType;
	}

	@Override
	public GameObject createObject() {
		switch(this.powerupType) {
			case PowerupAbstract.DASH_POWERUP: return new PowerupDash(this.world, 30, -30);
			case PowerupAbstract.ENERGY_POWERUP: return new PowerupEnergy(this.world, 30, -30);
			case PowerupAbstract.GHOST_POWERUP: return new PowerupGhost(this.world, 30, -30);
			case PowerupAbstract.PUNCH_POWERUP: return new PowerupPunch(this.world, 30, -30);
			default: return null;
		}
	}
}