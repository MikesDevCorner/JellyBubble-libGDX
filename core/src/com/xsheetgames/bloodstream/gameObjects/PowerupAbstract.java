package com.xsheetgames.bloodstream.gameObjects;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xsheetgames.bloodstream.GameAssets;


public abstract class PowerupAbstract extends GameObject {

	public final static int DASH_POWERUP = 0;
	public final static int ENERGY_POWERUP = 1;
	public final static int GHOST_POWERUP = 2;
	public final static int PUNCH_POWERUP = 3;
	
	protected short categoryBits = 128;
	protected short maskBits = 1;	
	private boolean consumed = false;
	
	
	public PowerupAbstract(World world, float x, float y) {
		super(world, GameAssets.fetchTextureAtlas("generic/game_objects.pack"),0f,GameAssets.emptyVector2.cpy(),new Vector2(x,y),true);		
		
		BodyDef bd = new BodyDef();
		bd.type = BodyType.StaticBody;
		this.bodyType = BodyType.StaticBody;
		bd.fixedRotation = true;
		bd.gravityScale = 0f;
		
		FixtureDef fd = new FixtureDef();
		fd.density = 1f;
		fd.friction = 0.5f;
		fd.restitution = 0.001f;
		fd.filter.categoryBits = this.categoryBits;
		fd.filter.maskBits = this.maskBits;
		fd.isSensor = true;
		
		super.init(bd, fd, GameAssets.getObjectLoader(), "powerup", this.GetPowerupTypeString(), Animation.PlayMode.NORMAL);
	}
	
	
	public abstract String GetPowerupTypeString();
	
	public boolean getConsumed() {
		return this.consumed;
	}
	
	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}
	

	@Override
	public void reset() {
		super.reset();
		this.consumed = false;
	}
	
	
	@Override
	public void doMotionLogic(float delta) {
		if(this.consumed == true) {
			if(this.world.isLocked() == false) {
				if(this.free() == false) this.dispose();
			}
		}
	}
	
}
