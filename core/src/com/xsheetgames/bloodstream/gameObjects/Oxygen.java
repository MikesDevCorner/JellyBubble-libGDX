package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.xsheetgames.bloodstream.GameAssets;
import com.xsheetgames.bloodstream.elements.GraphicMeter;

public class Oxygen extends GameObject {

	private float remainTime = 5f;
	private float startRemainTime = 5f;
	private GraphicMeter timeDisplay;
	public boolean startCounting = false;
	public Joint jellyJoint;
	public Jelly jelly;
	
	public float noJoinTimer = 0f;
	private float noJoinTimerStart = 0.6f;
	
	
	public Oxygen(World world, float x, float y) {
		
		super(world, GameAssets.fetchTextureAtlas("generic/game_objects.pack"), 0f, new Vector2(0f,0f), new Vector2(x,y), true);

		BodyDef bd = new BodyDef();
		this.bodyType = BodyType.DynamicBody;
		bd.linearDamping = 1.5f;
		bd.type = BodyType.DynamicBody;
		bd.angularDamping = 1f;
		  
		FixtureDef fd = new FixtureDef();
		fd.density = 1.0f;
		fd.friction = 1.0f;
		fd.restitution = 0.75f;
		fd.filter.categoryBits = 4;
		fd.filter.maskBits = 127;
		
		super.init(bd, fd, GameAssets.objectLoader, "ball", "ball", Animation.PlayMode.NORMAL);
		
		this.timeDisplay = new GraphicMeter(x, y, "track_small","display_small",true, GameAssets.fetchTextureAtlas("generic/game_objects.pack"), new Vector2(0f,0.15f));
	}
	
	
	public void resetNoJointTimer() {
		this.noJoinTimer = this.noJoinTimerStart;
	}
	
	public void zeroNoJoinTimer() {
		this.noJoinTimer = 0f;
	}
	
	public void resetCounting() {
		this.startCounting = false;
		this.remainTime = this.startRemainTime;
		this.noJoinTimer = 0f;
	}
	
	public void startCounting() {
		if(this.jelly != null && this.jellyJoint != null) {
			jelly.destroyBubbleJoint();
			this.jelly = null;
			this.jellyJoint = null;
			this.noJoinTimer = this.startRemainTime;
		}
		this.startCounting = true;
	}
	
	
	@Override
	public boolean free() {
		if(this.jelly != null && this.jellyJoint != null) {
			jelly.destroyBubbleJoint();
			this.jelly = null;
			this.jellyJoint = null;
		}
		return super.free();
	}
	
	
	
	@Override
	public void reset() {
		super.reset();
		this.resetCounting();
	}
	

	@Override
	public void doMotionLogic(float delta) {
		if(this.noJoinTimer > 0f) this.noJoinTimer -= delta;
		
		if(this.startCounting == true) {
			this.remainTime -= delta;
			this.timeDisplay.setPercent(this.remainTime * 100 / this.startRemainTime);
			if(this.remainTime <= 0f) {
				this.free();
			}
		}
	}
	
	@Override
	public void smoothStates(float fixedTimestepAccumulatorRatio) {
		super.smoothStates(fixedTimestepAccumulatorRatio);
		
		float oneMinusRatio = 1.0f - fixedTimestepAccumulatorRatio;
		Vector2 position = body.getPosition();
		
		
		this.timeDisplay.setPosition((fixedTimestepAccumulatorRatio * position.x) + (oneMinusRatio * (this.previousPosition.x + this.modelOrigin.x)), (fixedTimestepAccumulatorRatio * position.y) + (oneMinusRatio * (this.previousPosition.y+ this.modelOrigin.y)));
		this.timeDisplay.setRotation(body.getAngle() * MathUtils.radiansToDegrees * fixedTimestepAccumulatorRatio + oneMinusRatio * this.previousAngle);
	}
	
	@Override
	public void resetSmoothStates() {
		super.resetSmoothStates();
		this.timeDisplay.setPosition(this.previousPosition.x + this.modelOrigin.x, this.previousPosition.y+this.modelOrigin.y);
		this.timeDisplay.setRotation(this.previousAngle);
	}
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		super.draw(batch, delta);
		if(this.startCounting) this.timeDisplay.draw(batch);
	}
	
	@Override
	public void resetGraphics(TextureAtlas atlas) {
		super.resetGraphics(atlas);
		this.timeDisplay.resetGraphics();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		this.timeDisplay.dispose();
	}

}
