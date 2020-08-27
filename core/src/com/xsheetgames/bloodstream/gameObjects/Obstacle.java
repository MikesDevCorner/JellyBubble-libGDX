package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xsheetgames.bloodstream.GameAssets;

public class Obstacle extends GameObject {	

	private boolean ghostLeakAble;
	
	
	public Obstacle(World world, float x, float y, String modelName, String textureName) {
		
		super(world, GameAssets.fetchTextureAtlas("generic/game_objects.pack"), 0f, new Vector2(0f,0f), new Vector2(x,y), true);

		BodyDef bd = new BodyDef();
		this.bodyType = BodyType.StaticBody;
		bd.linearDamping = 0.2f;
		bd.type = BodyType.StaticBody;
		bd.fixedRotation = true;
		bd.gravityScale = 0f;

		FixtureDef fd = new FixtureDef();
		fd.density = 20f;
        fd.friction = 0.8f;
        fd.restitution = 0.7f;
		fd.filter.categoryBits = 16;
		fd.filter.maskBits = 13;
		
		super.init(bd, fd, GameAssets.objectLoader, modelName, textureName, Animation.PlayMode.NORMAL);
	}

	@Override
	public void doMotionLogic(float delta) {
		// TODO Auto-generated method stub		
	}
	
	public void setGhostLeakAble(boolean ghostLeakAble) {
		this.ghostLeakAble = ghostLeakAble;
	}
	
	public boolean getGhostLeakAble() {
		return this.ghostLeakAble;
	}
	
	public void setOpaque(boolean opaque) {
		if(opaque) {
			this.sprite.setColor(1.0f, 1.0f, 1.0f, 0.4f);
		} else {
			this.sprite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

}