package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.xsheetgames.bloodstream.GameAssets;
import com.xsheetgames.bloodstream.elements.iGhostModeAble;
import com.xsheetgames.bloodstream.elements.iHurtAble;

public abstract class EnemyAbstract extends GameObject implements iGhostModeAble, iHurtAble {
	
	public static final int ENEMY_BIG = 0;
	public static final int ENEMY_SMALL = 1;
	
	
	protected float motionTimer = 0f;
	protected float motionTimerStart = 3f;
	protected float minRandom =-80f;
	protected float maxRandom = 80f;
	private float speedLimit;
	protected float minLimit = 5f;
	protected float maxLimit = 12f;
	private boolean ghostMode = false;
	private float ghostCounter;
	protected float ghostCounterStart;
	
	private short energy = 2;
	private short StartEnergy = 2;
	
	protected Vector2 forceVector;

	public EnemyAbstract(World world, float x, float y) {
		
		super(world, GameAssets.fetchTextureAtlas("generic/game_objects.pack"), 0f, GameAssets.emptyVector2.cpy(), new Vector2(x,y), true);

	}

	@Override
	public void doMotionLogic(float delta) {
		if(this.motionTimer >= 0f) this.motionTimer -= delta;
		else this.changeDirection();
		
		if(this.ghostCounterStart > 0f) {
			if(this.ghostCounter >= 0f) this.ghostCounter -= delta;
			else this.setGhostMode(!this.getGhostMode());
		}
		
		float v = 1f;
		if(this.body.getLinearVelocity().len() > speedLimit) v = 0f;
		this.body.applyForceToCenter(this.forceVector.scl(v), true);
		if(this.energy <= 0) this.die();
	}
	
	public void resetRandomTimer() {
		float motionTimerMax = this.motionTimerStart * 1.5f;
		float motionTimerMin = this.motionTimerStart / 2f;		
		this.motionTimer = (float) Math.random() * (motionTimerMax-motionTimerMin) + motionTimerMin;
	}
	
	
	@Override
	public void reset() {
		this.changeDirection();
		this.setGhostMode(false);
		this.energy = this.StartEnergy;
	}
	
	
	public void changeDirection() {
		this.resetRandomTimer();
		this.forceVector.x = (float) Math.random() * (this.maxRandom-this.minRandom) + this.minRandom;
		this.forceVector.y = (float) Math.random() * (this.maxRandom-this.minRandom) + this.minRandom;
		this.speedLimit = (float) Math.random() * (this.maxLimit-this.minLimit) + this.minLimit;
	}
	
	public void oppositeDirection() {
		this.resetRandomTimer();
		this.forceVector.scl(-0.5f,-0.5f);
	}
	
	public boolean getGhostMode() {
		return this.ghostMode;
	}
	
	
	
	public void setGhostMode(boolean ghostMode) {
		this.ghostMode = ghostMode;
		if(ghostMode) {
			this.sprite.setColor(1.0f, 1.0f, 1.0f, 0.4f);
			float ghostTimerMax = this.ghostCounterStart * 1.5f;
			float ghostTimerMin = this.ghostCounterStart / 2f;		
			this.ghostCounter = (float) Math.random() * (ghostTimerMax-ghostTimerMin) + ghostTimerMin;
		}
		else {
			this.sprite.setColor(1.0f, 1.0f, 1.0f, 1.0f);
			float ghostTimerMax = this.ghostCounterStart * 3f;
			float ghostTimerMin = this.ghostCounterStart;		
			this.ghostCounter = (float) Math.random() * (ghostTimerMax-ghostTimerMin) + ghostTimerMin;
		}
	}

	@Override
	public void reduceEnergy() {
		this.energy--;
	}

	@Override
	public void die() {
		this.setDisposing = true;		
	}

}