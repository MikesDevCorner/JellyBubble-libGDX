package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.xsheetgames.bloodstream.GameAssets;
import com.xsheetgames.bloodstream.elements.iGhostModeAble;
import com.xsheetgames.bloodstream.elements.iPunchModeAble;


public class Jelly extends GameObject implements iGhostModeAble, iPunchModeAble{
	
	/*******CHARACTER CONFIG************/
	
	//CHARACTER STATS
	
	private short energy = 3;
	private float invincibleTimeAfterHurt = 1f;
	public float invincible = 0f;
	
	private boolean dashMode = false;
	private float dashTime = 0f;
	private float dashMaxTime = 3f;
	private float dashCooldown = 1f;
	private float dashCooldownStart = 1f;
	private float dashRefillMultiplier = 0.5f;
		
	private boolean ghostMode = false;
	private boolean punchMode = false;

	
	/********************************/
	

	//Physic Stuff
	private Body connectionBody;
	private Joint connectionJoint;
	private Joint bubbleJoint;
	private Oxygen bubble;
	private boolean destroyBubbleJoint, createBubbleJoint;
	protected short categoryBits = 1;
	protected short maskBits = 190;
	private float linearDamping = 1.35f;
	private float forceToApply = 95;
	private float speedLimit = 5f;
	
	
	//Drawing Stuff
	private ParticleEffect p, p2, p3;
	private boolean accellerateTextureSwitched = false;
	public float xOld = 0f;
	public float yOld = 0f;
	
	

	public Jelly(World world, float x, float y) {
		
		super(world,GameAssets.fetchTextureAtlas("generic/game_objects.pack"),0f,new Vector2(0f,0f),new Vector2(x,y),true);			
		
		BodyDef bd = new BodyDef();
		this.bodyType = BodyType.DynamicBody;
		bd.linearDamping = this.linearDamping;
		bd.type = BodyType.DynamicBody;
		bd.angularDamping = 1f;
		bd.fixedRotation = true;
		bd.gravityScale = 0f;
		  
		FixtureDef fd = new FixtureDef();
		fd.density = 5.0f;
		fd.friction = 0.5f;
		fd.restitution = 0.4f;
		fd.filter.categoryBits = this.categoryBits;
		fd.filter.maskBits = this.maskBits;
		
		p = new ParticleEffect();
		p.load(Gdx.files.internal("particles/particles.p"), Gdx.files.internal("particles"));
		
		p2 = new ParticleEffect();
		p2.load(Gdx.files.internal("particles/particles.p"), Gdx.files.internal("particles"));
		
		p3 = new ParticleEffect();
		p3.load(Gdx.files.internal("particles/particles.p"), Gdx.files.internal("particles"));
		
		
		super.init(bd, fd, GameAssets.objectLoader, "jelly", "character_idle", Animation.PlayMode.NORMAL);
		
		
		//HEAD
		bd.position.set(x, y);
		this.connectionBody = world.createBody(bd);
		
		GameAssets.getObjectLoader().attachFixture(this.connectionBody, "jelly_top", fd, this.sprite.getWidth());
		
		RevoluteJointDef rd = new RevoluteJointDef();
		rd.initialize(this.body, this.connectionBody, new Vector2(this.body.getPosition().x, this.body.getPosition().y));		
		this.connectionJoint = world.createJoint(rd);
		this.connectionBody.setUserData(new JellyDecorator(this));
	}
	
	
	
	@Override
	public void draw(SpriteBatch batch, float delta) {
		if(!this.isDisposed() && this.isVisible()) {
			p.draw(batch);
			p2.draw(batch);
			p3.draw(batch);
			
			if(this.invincible > 0f && this.getEnergy() > 0) {
				this.setBlack();
			} else this.setOriginalColor();
			
			super.draw(batch, delta);				
				
			
			if(this.visible && this.firstTimeSmoothened && !this.isDisposed()) {
				
			}
		}
	}	
	
	@Override
	public void smoothStates(float fixedTimestepAccumulatorRatio) {
		if(!this.isDisposed()) {		
			super.smoothStates(fixedTimestepAccumulatorRatio);			
		}
	}
	
	@Override
	public void resetSmoothStates() {
		if(!this.isDisposed()) {
			super.resetSmoothStates();
		}
	}
	
	@Override
	public void resetGraphics(TextureAtlas atlas) {
		super.resetGraphics(atlas);
	}
	
	
	public void updateCameraAndDraw(SpriteBatch batch, float delta, OrthographicCamera camera) {
		this.draw(batch, delta);
		camera.update();
	}
	
	public void rotate(float rotation) {
		this.body.setTransform(this.body.getPosition(), (float) (this.body.getAngle() + Math.toRadians(rotation) * -1f));
	}
	
	
	
	
	
	public void createBubbleJoint(Oxygen o) {
		boolean isAcceleratePressed = Gdx.input.isKeyPressed(Keys.CONTROL_LEFT);
		boolean moreThanOneFinger = false;
		if((Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) || (Gdx.input.isTouched(0) && Gdx.input.isTouched(2)) || (Gdx.input.isTouched(1) && Gdx.input.isTouched(2))) moreThanOneFinger = true;
		
		if(isAcceleratePressed == false && moreThanOneFinger == false) {
			if(this.bubble == null && this.bubbleJoint == null && o.noJoinTimer <= 0f) {
				this.createBubbleJoint = true;
				this.bubble = o;
			}
		}		
	}

	
	public void destroyBubbleJoint() {
		this.destroyBubbleJoint = true;
	}
	

	public void resetBubbleJoint() {
		this.bubble.zeroNoJoinTimer();
		this.destroyBubbleJoint();
	}
	
	
	@Override
	public void doMotionLogic(float delta) {
		if(!this.isDisposed() && !this.setDisposing) {
			
			/*************************** MOVEMENT PROCESSING *************************/
			this.xOld = this.body.getPosition().x;
			this.yOld = this.body.getPosition().y;
			
			boolean isUpPressed = GameAssets.nativ.pollControllerButtonState(GameAssets.KEY_PRIMARY);
			boolean isAcceleratePressed = GameAssets.nativ.pollControllerButtonState(GameAssets.KEY_SECONDARY);
			
			
			boolean isTouched = Gdx.input.isTouched();
			boolean moreThanOneFinger = false;			
			if((Gdx.input.isTouched(0) && Gdx.input.isTouched(1)) || (Gdx.input.isTouched(0) && Gdx.input.isTouched(2)) || (Gdx.input.isTouched(1) && Gdx.input.isTouched(2))) moreThanOneFinger = true;
			
			
			if(this.dashCooldown <= 0f) this.dashCooldown = this.dashCooldownStart;
			if(this.dashCooldown != this.dashCooldownStart) {
				this.dashCooldown -= (delta * dashRefillMultiplier);
			}
			
			
			if(isAcceleratePressed || moreThanOneFinger) {
				if(this.bubbleJoint != null) {
					this.bubble.resetNoJointTimer();
					this.destroyBubbleJoint();					
				}
			}
			
			
			if(isUpPressed || isTouched) {
				
				float v = 0;
				float limit;				
				
				if((isAcceleratePressed || moreThanOneFinger) && this.dashTime > 0f && this.dashCooldown >= this.dashCooldownStart && this.dashMode == true) {

					if(p.isComplete()) {
						p.start();
						p2.start();
						p3.start();
					} 
					v = this.forceToApply * 2.5f;
					limit = this.speedLimit * 2;
					
					this.dashTime -= delta;
					if(this.dashTime <= 0.1f) this.dashTime = 0f;
				} else {
					if(p.isComplete()) {
						p.start();
					}
					v = this.forceToApply;
					limit = this.speedLimit;
					
					if(this.dashTime <= 0f) this.dashCooldown -= (delta * dashRefillMultiplier);
					if(this.dashTime < this.dashMaxTime) this.dashTime += (delta * dashRefillMultiplier);
					else this.dashTime = this.dashMaxTime;
				}
				
				
				if(this.body.getLinearVelocity().len() > limit) v = 0f;
				float x = (float) (v * Math.cos(this.body.getAngle() + Math.toRadians(90f)));
				float y = (float) (v * Math.sin(this.body.getAngle() + Math.toRadians(90f)));
				this.body.applyForceToCenter(new Vector2(x,y), true);
				
				
				if(accellerateTextureSwitched == false) {
					this.changeAnimation("jelly",false, Animation.PlayMode.NORMAL);
					accellerateTextureSwitched = true;
				}
			} else {
				
				if(this.dashTime < this.dashMaxTime) this.dashTime += (delta/2);
				else this.dashTime = this.dashMaxTime;
				
				if(accellerateTextureSwitched == true) {
					this.changeAnimation("character_idle",false, Animation.PlayMode.NORMAL);
					accellerateTextureSwitched = false;
				}
			}
			
			
			/*************************** SET ROTATION FOR CONNECTION BODY *************************/
			this.connectionBody.setTransform(this.body.getPosition(), this.body.getAngle());
			if(this.bubble != null) {
				if(this.bubble.getBody().isAwake() == false) this.bubble.getBody().setAwake(true);
			}
			
			
			/*************************** destroy or create bubble joints *************************/
			if(world.isLocked() == false) {
				if(this.createBubbleJoint) {
					if(this.bubbleJoint == null && this.bubble != null) {
						this.createBubbleJoint = false;
						WeldJointDef rd = new WeldJointDef();
						
						float r = 0.4f;
						double theta = this.body.getAngle()+Math.toRadians(90.0);			
						float x2 = (float) (r * Math.cos(theta));
						float y2 = (float) (r * Math.sin(theta));			
						
						rd.initialize(this.bubble.getBody(), this.connectionBody, new Vector2(this.connectionBody.getPosition().x + x2, this.connectionBody.getPosition().y + y2));		
						this.bubbleJoint = world.createJoint(rd);
						this.bubble.jelly = this;
						this.bubble.jellyJoint = this.bubbleJoint;
					}
				}
				
				if(this.destroyBubbleJoint) {
					this.world.destroyJoint(this.bubbleJoint);
					this.bubble.jelly = null;
					this.bubble.jellyJoint = null;
					this.bubbleJoint = null;
					this.bubble = null;
					this.destroyBubbleJoint = false;				
				}			
			}
			
			
			
			/*************************** deal with invinciblity *************************/
			if(this.invincible > 0f) {
				this.invincible -= delta;
			}
			
			
	
			/*************************** CHECK_DEAD *************************/
			if(this.energy < 1) {
				this.dispose();
			}
			
			/*************************** DO PARTICLES **********************/
			
			if(this.body != null) {
				float r = 0.85f;
				double theta = this.body.getAngle()-Math.toRadians(90.0);			
				float x2 = (float) (r * Math.cos(theta));
				float y2 = (float) (r * Math.sin(theta));			
				p.setPosition(this.body.getPosition().x + x2, this.body.getPosition().y + y2);
				
				theta = this.body.getAngle()-Math.toRadians(90.0+40.0);			
				x2 = (float) (r * Math.cos(theta));
				y2 = (float) (r * Math.sin(theta));
				p2.setPosition(this.body.getPosition().x + x2, this.body.getPosition().y + y2);
				
				theta = this.body.getAngle()-Math.toRadians(90.0-40.0);			
				x2 = (float) (r * Math.cos(theta));
				y2 = (float) (r * Math.sin(theta));
				p3.setPosition(this.body.getPosition().x + x2, this.body.getPosition().y + y2);
				//p.findEmitter("").allowCompletion();
				//p.getEmitters().first().getAngle().setLow((float) Math.toDegrees(this.body.getAngle())-90.0f);
				//p.getEmitters().first().getAngle().setHigh((float) Math.toDegrees(this.body.getAngle())-90.0f);
				p.update(delta);
				p2.update(delta);
				p3.update(delta);
			}
		}
	}
	
	
	/********************************************/
	
	public String getDashTime() {
		if(this.dashTime <= 0.01f) return GameAssets.nativ.numberFormat(0f);
		return GameAssets.nativ.numberFormat(this.dashTime);
	}
	
	public float getDashPercent() {
		return this.dashTime * 100f / this.dashMaxTime;
	}
	
	public boolean getDashMode() {
		return this.dashMode;
	}
	
	public void setDashMode(boolean dashmode) {
		this.dashTime = 0f;
		this.dashMode = dashmode;
	}
	
	public boolean getGhostMode() {
		return this.ghostMode;
	}
	
	public void setGhostMode(boolean ghostMode) {
		this.ghostMode = ghostMode;
	}
	
	public boolean getPunchMode() {
		return this.punchMode;
	}
	
	public void setPunchMode(boolean punchMode) {
		this.punchMode = punchMode;
	}
	
	public void reduceEnergy() {
		if(this.invincible <= 0f) {
			if(this.getGhostMode()) {
				this.setGhostMode(false);
			}
			if(this.getPunchMode()) {
				this.setPunchMode(false);
			}
			this.energy--;
			
			this.invincible = this.invincibleTimeAfterHurt;
			if(this.energy > 0) {
				//GameAssets.playSound(GameAssets.fetchSound("game/sounds/energylose.ogg"));
				GameAssets.vibrate(350);
			}
		}
	}	
	
	public short getEnergy() {
		return this.energy;
	}
	
	public void incrementEnergy() {
		//GameAssets.playSound(GameAssets.fetchSound("game/sounds/powerup.ogg"),0.8f);
		GameAssets.vibrate(200);
		this.energy++;
	}
	
	public void setEnergy(short e) {
		this.energy = e;
	}
	
	
	public void setInvincible(float time) {
		this.invincible = time;
	}
	
	public void startInvincibleMode() {
		this.invincible = 25f;
	}
	
	public void endInvincibleMode() {
		this.invincible = 0f;
	}
	
	public float getInvincible() {
		return this.invincible;
	}
	
	
	/*******************************************/
	
	
	
	
	public void dispose() {
		this.energy = 0;
		this.p.dispose();
		this.p2.dispose();
		this.p3.dispose();
		if(this.world != null) {
			if(this.connectionJoint != null) this.world.destroyJoint(connectionJoint);
			if(this.bubbleJoint != null) this.world.destroyJoint(this.bubbleJoint);
			if(this.connectionBody != null) this.world.destroyBody(connectionBody);
		}
		super.dispose();
	}
	
}
