package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xsheetgames.bloodstream.GameAssets;

public class Dropzone extends GameObject {

	public Dropzone(World world, float x, float y) {
		
		super(world, GameAssets.fetchTextureAtlas("generic/game_objects.pack"), 0f, new Vector2(0f,0f), new Vector2(x,y), true);

		BodyDef bd = new BodyDef();
		this.bodyType = BodyType.KinematicBody;
		bd.type = BodyType.KinematicBody;
		  
		FixtureDef fd = new FixtureDef();
		fd.density = 1.0f;
		fd.friction = 1.0f;
		fd.restitution = 1.0f;
		fd.filter.categoryBits = 64;
		fd.filter.maskBits = 4;
		
		
		super.init(bd, fd, GameAssets.objectLoader, "dropzone", "dropzone", Animation.PlayMode.NORMAL);
	}

	@Override
	public void doMotionLogic(float delta) {
		// TODO Auto-generated method stub
		
	}

}