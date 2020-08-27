package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.physics.box2d.World;

public class PowerupPunch extends PowerupAbstract {

	public PowerupPunch(World world, float x, float y) {
		super(world, x, y);
	}

	@Override
	public String GetPowerupTypeString() {
		return "pu_shock";
	}

}
