package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xsheetgames.bloodstream.GameAssets;

public class EnemyBig extends EnemyAbstract {

	public EnemyBig(World world, float x, float y) {
		super(world, x, y);
		this.ghostCounterStart = 6f;
		
		BodyDef bd = new BodyDef();
		this.bodyType = BodyType.DynamicBody;
		bd.linearDamping = 0.2f;
		bd.type = BodyType.DynamicBody;
		bd.angularDamping = 1.25f;
		//bd.fixedRotation = true;
		bd.gravityScale = 0f;
		  
		FixtureDef fd = new FixtureDef();
		fd.density = 4.5f;
		fd.friction = 0.5f;
		fd.restitution = 0.5f;
		fd.filter.categoryBits = 8;
		fd.filter.maskBits = 63;
		
		this.forceVector = new Vector2();
		
		super.init(bd, fd, GameAssets.objectLoader, "virus", "virus", Animation.PlayMode.NORMAL);
	}

}
