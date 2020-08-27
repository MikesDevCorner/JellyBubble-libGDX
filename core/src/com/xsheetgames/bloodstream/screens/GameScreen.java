package com.xsheetgames.bloodstream.screens;


import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.xsheetgames.bloodstream.Configuration;
import com.xsheetgames.bloodstream.GameAssets;
import com.xsheetgames.bloodstream.elements.GameContactListener;
import com.xsheetgames.bloodstream.elements.GraphicMeter;
import com.xsheetgames.bloodstream.elements.ParallaxLayer;
import com.xsheetgames.bloodstream.gameObjects.Dropzone;
import com.xsheetgames.bloodstream.gameObjects.DropzoneFactory;
import com.xsheetgames.bloodstream.gameObjects.EnemyAbstract;
import com.xsheetgames.bloodstream.gameObjects.EnemyBig;
import com.xsheetgames.bloodstream.gameObjects.EnemyFactory;
import com.xsheetgames.bloodstream.gameObjects.EnemySmall;
import com.xsheetgames.bloodstream.gameObjects.GameObject;
import com.xsheetgames.bloodstream.gameObjects.GameObjectCollection;
import com.xsheetgames.bloodstream.gameObjects.GameObjectPool;
import com.xsheetgames.bloodstream.gameObjects.ObstacleCollection;
import com.xsheetgames.bloodstream.gameObjects.ObstacleFactory;
import com.xsheetgames.bloodstream.gameObjects.Oxygen;
import com.xsheetgames.bloodstream.gameObjects.BoundaryCollection;
import com.xsheetgames.bloodstream.gameObjects.Jelly;
import com.xsheetgames.bloodstream.gameObjects.OxygenFactory;
import com.xsheetgames.bloodstream.gameObjects.PowerupAbstract;
import com.xsheetgames.bloodstream.gameObjects.PowerupDash;
import com.xsheetgames.bloodstream.gameObjects.PowerupEnergy;
import com.xsheetgames.bloodstream.gameObjects.PowerupFactory;
import com.xsheetgames.bloodstream.gameObjects.PowerupGhost;
import com.xsheetgames.bloodstream.gameObjects.PowerupPunch;


public class GameScreen extends AbstractScreen {
	
	private int debug = 0;
	private Box2DDebugRenderer debugRenderer;
	
	
	//ANGULAR MOVEMENT
	private float rotationDegreePerSec = 90; //degree
	private float rotationStartAngle = 0.8f; //device y-axis movement minimum
	private float rotationEndangle = 4f; //device y-axis movement maximum
	
	//SIZE OF ARENA (in m)
	private float arenaSizeX = 30f;
	private float arenaSizeY = 35f;
	
	
	//STARTPOSITION JELLY 
	private float jellyStartX = 0f; //m in karthesian
	private float jellyStartY = 0f; //m in karthesian
	private float jellyStartRotation = 0f; //degree
	
	
	//Important stuff
	private OrthographicCamera camera;
	private World world;
	private TweenManager tweenManager;
	private SpriteBatch batch;
	
	//Misc	
	private boolean assetsLoaded = false;
	private boolean disposed = false;
	public static boolean paused = false;
	public static short gameStatus = 0;
	
	public static String actualPauseMessage = "Pause";
	public static String pauseMessagePause = "Pause";
	public static String pauseMessageFinished = "You finished the level";
	public static String pauseMessageDead = "Sorry, you died";
	
	private boolean doDisposing = false;
	private ParallaxLayer background, debuglayer, foreground;
	private BitmapFont font;
	private GlyphLayout layout;
	private Sprite pauseLayer, energySprite;
	
	//Game Objects
	private Jelly jelly;
	private GameObjectCollection oxygen;
	private GameObjectCollection enemies;
	private GameObjectCollection powerups;
	private GameObjectCollection dropzones;
	private ObstacleCollection obstacles;
	private BoundaryCollection boundaries;
	
	private GraphicMeter dashMeter;
	
	
	
	//Allan Bishop's Fixing Time Step Stuff (syncronize render-framerate with box2d-steprate)
	private final float FIXED_TIMESTEP = 1.0f/60.0f; //seconds
	private float fixedTimestepAccumulator = 0f;
	private float fixedTimestepAccumulatorRatio = 0f;
	private int velocityIterations = 8;
	private int positionIterations = 8;
	
	
	
	
	public GameScreen(Game game) {
		this.game = game;
		if(Configuration.debugLevel >= Application.LOG_INFO) Gdx.app.log("GameScreen", "Konstructor aufgerufen");
	}
	
	
	
	@Override
	public void render(float delta) {
	    if(this.disposed == false) {
			if(GameAssets.assetsLoaded(batch)) {		   
				if(assetsLoaded == false) this.doAssetProcessing();
			   
				/***************** LOGIC OPERATIONS AND SIMULATIONS *********************/
				
				if(GameScreen.paused == false) {
					
					final int MAX_STEPS = 5;
					fixedTimestepAccumulator += delta;
					final int nSteps = (int) Math.floor(fixedTimestepAccumulator / FIXED_TIMESTEP);
					
					if(nSteps > 0) {
						fixedTimestepAccumulator -= nSteps * FIXED_TIMESTEP;
					}
					fixedTimestepAccumulatorRatio = fixedTimestepAccumulator / FIXED_TIMESTEP;
					final int nStepsClamped = Math.min(nSteps, MAX_STEPS);
					
					for(int i = 0; i < nStepsClamped; ++i) {
						this.resetSmoothStates();
						this.singleStep(FIXED_TIMESTEP);
					}
					this.world.clearForces();
					this.smoothStates();
					
				}
				
				
				/***************** DRAWING *********************/					
				
				this.background.setCameraCombinedAndDraw(batch);
				if(this.debug > 2) this.debuglayer.setCameraCombinedAndDraw(batch);
				
				batch.setProjectionMatrix(camera.combined);
				batch.begin();
				this.dropzones.drawObjects(batch, delta);
				
				this.obstacles.drawObjects(batch, delta);
				
				this.powerups.drawObjects(batch, delta);
				this.oxygen.drawObjects(batch, delta);
				
				this.enemies.drawObjects(batch, delta);
				this.jelly.draw(batch, delta);				
				
				
				batch.end();
				if(this.debug > 1) debugRenderer.render(this.world, camera.combined);
				this.boundaries.draw(camera);
				
				//this.foreground.setCameraCombinedAndDraw(batch);
				
				
				
				
			    //DRAW THINGS DEPENDING ON TARGET_WIDTH SIZES IN PIXEL (HUD)
				batch.getProjectionMatrix().setToOrtho2D(0, 0, Configuration.TARGET_WIDTH, Configuration.TARGET_HEIGHT);
				batch.begin();
				if(debug > 0) {
					if(jelly.isDisposed() == false && world.isLocked() == false) {
						layout.setText(font, "A");
						font.draw(batch, "X: " + GameAssets.nativ.numberFormat(Gdx.input.getAccelerometerX()) + "   Y: " + GameAssets.nativ.numberFormat(Gdx.input.getAccelerometerY()) + "   Z:" + GameAssets.nativ.numberFormat(Gdx.input.getAccelerometerZ()),  Configuration.bezugifyX(50f), Configuration.bezugifyY(50f)  + layout.height);
						layout.setText(font, "FPS: " + Gdx.graphics.getFramesPerSecond() + " pos: " + GameAssets.nativ.numberFormat(this.jelly.getBody().getPosition().x) + "/" + GameAssets.nativ.numberFormat(this.jelly.getBody().getPosition().y));
						font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + " pos: " + GameAssets.nativ.numberFormat(this.jelly.getBody().getPosition().x) + "/" + GameAssets.nativ.numberFormat(this.jelly.getBody().getPosition().y),  Configuration.TARGET_WIDTH - (layout.width+Configuration.bezugifyX(50f)), Configuration.bezugifyY(50f) + layout.height);
					}
				}
				
				for(int i = 0; i < jelly.getEnergy(); i++) {
					this.batch.draw(this.energySprite, (Configuration.bezugifyX(5f) + (this.energySprite.getWidth()* i + Configuration.bezugifyX(5f))), Configuration.TARGET_HEIGHT - ( Configuration.bezugifyY(10f) + this.energySprite.getHeight()));
				}
				
				String bubblesString = "bubbles";
				if(oxygen.length == 1) bubblesString = "bubble";
				layout.setText(font, oxygen.length + " " + bubblesString +" left");
				font.draw(batch, oxygen.length + " " + bubblesString +" left",  Configuration.TARGET_WIDTH/2-layout.width/2, Configuration.TARGET_HEIGHT - Configuration.bezugifyY(15f));
				if(jelly.getDashMode()) dashMeter.draw(batch);				
				
				
				
				if(GameScreen.paused == true) {
					this.pauseLayer.draw(batch);
					layout.setText(font, GameScreen.actualPauseMessage);
					font.draw(batch, GameScreen.actualPauseMessage, Configuration.TARGET_WIDTH/2-layout.width/2, Configuration.TARGET_HEIGHT/2-layout.height/2);
					batch.end();
				} else {
					batch.end();
					if(GameScreen.gameStatus == 1 || GameScreen.gameStatus == 2) {
						this.unloadAssetsAndFinalize();
						this.game.setScreen(new MenuScreen(game));
						return;
					}
				}				
				
				
				
				/***************** LEAVING SZENARIO DETECTED *********************/
				
				//check if leaving szenario is given:
				if(this.doDisposing == true) {
					this.unloadAssetsAndFinalize();
					this.game.setScreen(new MenuScreen(game));
					return;
				}
				
				if(jelly.getEnergy() == 0) {
					GameScreen.gameStatus = 1;
					GameScreen.actualPauseMessage = GameScreen.pauseMessageDead;
					this.pause();
				}
				
				if(oxygen.length == 0) {
					GameScreen.gameStatus = 2;
					GameScreen.actualPauseMessage = GameScreen.pauseMessageFinished;
					this.pause();
				}
				
				
			}
	    }
		
	}
	
	
	
	private void singleStep(float dt) {
		
		//APPLY PHYSICS, FORCES, INPUTS, ETC.
		tweenManager.update(dt);		
		
		
		//polling input states
		boolean isLeftPressed = GameAssets.nativ.pollControllerButtonState(GameAssets.KEY_LEFT);
		boolean isRightPressed = GameAssets.nativ.pollControllerButtonState(GameAssets.KEY_RIGHT);
	    float accelX = GameAssets.nativ.pollControllerAxis(GameAssets.AXIS_X) * 10f;
		
	    
	    
	    float karthesianRotationDelta = rotationDegreePerSec * dt;
	    float rotationKompressorProzent = (Math.abs(accelX) - rotationStartAngle) * 100 / (rotationEndangle - rotationStartAngle);
	    if(rotationKompressorProzent < 0) rotationKompressorProzent *= -1;
	    if(rotationKompressorProzent < 10) rotationKompressorProzent = 10;
	    if(rotationKompressorProzent > 100 || isRightPressed || isLeftPressed) rotationKompressorProzent = 100;
	    karthesianRotationDelta = karthesianRotationDelta / 100 * rotationKompressorProzent;
	    
	    
	    
	    
		if(isRightPressed || accelX > rotationStartAngle) {
			this.camera.rotate(karthesianRotationDelta);
			this.background.rotateCamera(karthesianRotationDelta);
			if(this.debug > 1)  this.debuglayer.rotateCamera(karthesianRotationDelta);
			this.foreground.rotateCamera(karthesianRotationDelta);
			this.jelly.rotate(karthesianRotationDelta);
		}
		
		if(isLeftPressed || accelX < (rotationStartAngle*-1)) {
			this.camera.rotate(karthesianRotationDelta * -1);
			this.background.rotateCamera(karthesianRotationDelta * -1);
			if(this.debug > 1)  this.debuglayer.rotateCamera(karthesianRotationDelta * -1);
			this.foreground.rotateCamera(karthesianRotationDelta * -1);
			this.jelly.rotate(karthesianRotationDelta * -1);
		}	
		
		
		//invoke object logic
		this.jelly.doMotionLogic(dt);
		if(jelly.getGhostMode()) {
			if(obstacles.isOpaque() == false) obstacles.setOpaque(true);
		} else {
			if(obstacles.isOpaque() == true) obstacles.setOpaque(false);
		}
		
		this.oxygen.invokeObjectLogic(dt);
		this.enemies.invokeObjectLogic(dt);
		this.obstacles.invokeObjectLogic(dt);
		this.dropzones.invokeObjectLogic(dt);
		this.powerups.invokeObjectLogic(dt);
		
		dashMeter.setPercent(jelly.getDashPercent());
		
		background.doMotionLogicStep(jelly, dt);
		if(this.debug > 1)  debuglayer.doMotionLogicStep(jelly, dt);
		foreground.doMotionLogicStep(jelly, dt);
		
		
		
		//SIMULATE A SINGLE TIMESTEP IN THIS WORLD
		world.step(dt, this.velocityIterations, this.positionIterations);
		this.world.clearForces();
		
		if(jelly. isDisposed() == false && jelly.getBody() != null) camera.translate(jelly.getBody().getPosition().x - jelly.xOld, jelly.getBody().getPosition().y - jelly.yOld);
		camera.update();
		this.background.updateCamera();
		if(this.debug > 1) this.debuglayer.updateCamera();
		this.foreground.updateCamera();
		
	}
	
	private void smoothStates() {		
		this.jelly.smoothStates(fixedTimestepAccumulatorRatio);
		this.oxygen.smoothStates(fixedTimestepAccumulatorRatio);
		this.enemies.smoothStates(fixedTimestepAccumulatorRatio);
		this.obstacles.smoothStates(fixedTimestepAccumulatorRatio);
		this.dropzones.smoothStates(fixedTimestepAccumulatorRatio);
		this.powerups.smoothStates(fixedTimestepAccumulatorRatio);
	}
	
	private void resetSmoothStates() {
		this.jelly.resetSmoothStates();
		this.oxygen.resetSmoothStates();
		this.enemies.resetSmoothStates();
		this.obstacles.resetSmoothStates();
		this.dropzones.resetSmoothStates();
		this.powerups.resetSmoothStates();
	}	
	
	
	

	@Override
	public void resize(int width, int height) {
		if(Configuration.debugLevel >= Application.LOG_INFO) Gdx.app.log("GameScreen", "Resize auf width " + width + " and height "+height);
	}

	@Override
	public void show() {
		GameScreen.paused = false;
		GameAssets.nativ.trackPageView("/GameScreen");
		if(Configuration.debugLevel >= Application.LOG_INFO) Gdx.app.log("GameScreen", "Show aufgerufen");
	    
	    if(this.world == null) {
	    	this.world = new World(new Vector2(0f,0f), true);
	    	this.world.setContactListener(new GameContactListener());
	    }
	    
	    this.batch = new SpriteBatch();	    
	}

	@Override
	public void hide() {
		if(Configuration.debugLevel >= Application.LOG_INFO) Gdx.app.log("GameScreen", "Hide aufgerufen");
	    this.dispose();
	}

	@Override
	public void pause() {
		if(Configuration.debugLevel >= Application.LOG_INFO) {
			Gdx.app.log("GameScreen", "Pause aufgerufen");
		}
		GameScreen.paused = true;
	}
	
	public void endPause() {
		if(Configuration.debugLevel >= Application.LOG_INFO) Gdx.app.log("GameScreen", "End Pause aufgerufen");
		GameScreen.actualPauseMessage = GameScreen.pauseMessagePause;
		GameScreen.paused = false;
	}

	@Override
	public void resume() {
		if(Configuration.debugLevel >= Application.LOG_INFO) Gdx.app.log("GameScreen", "Resume aufgerufen");
		if(GameAssets.areGameAssetsLoaded() == false) {
			this.assetsLoaded = false;
			GameAssets.unloadMenuAssets();
			GameAssets.manager.clear();
			GameAssets.manager.finishLoading();
			GameAssets.loadGameAssets();
		}
	}

	
	public void unloadAssetsAndFinalize() {
		this.dispose();
		GameAssets.unloadGameAssets();
		GameAssets.manager.clear();
		GameAssets.manager.finishLoading();
		GameAssets.loadMenuAssets();
	}
	
	
	@Override
	public void dispose() {
		if(disposed == false) {
		   this.camera = null;
		   
		   this.pauseLayer = null;
		   
		   if(this.jelly != null) {
			   this.jelly.dispose();
			   this.jelly = null;
		   }

		   if(this.oxygen != null) {
			   this.oxygen.dispose();
			   this.oxygen = null;
		   }
		   if(this.enemies != null) {
			   this.enemies.dispose();
			   this.enemies = null;
		   }
		   if(this.obstacles != null) {
			   this.obstacles.dispose();
			   this.obstacles = null;
		   }
		   if(this.dropzones != null) {
			   this.dropzones.dispose();
			   this.dropzones = null;
		   }
		   if(this.powerups != null) {
			   this.powerups.dispose();
			   this.powerups = null;
		   }
		   if(this.boundaries != null) {
			   this.boundaries.dispose();
			   this.boundaries = null;
		   }
		   
		   if(this.world != null) {
			   this.world.dispose();
			   this.world = null;
		   } 	
		   if(this.batch != null) {
			   this.batch.dispose();
			   this.batch = null;
		   }
		   this.tweenManager = null;
		   
		   this.disposed = true;
		   this.doDisposing = false;
		   GameScreen.gameStatus = 0;
		   GameScreen.actualPauseMessage = GameScreen.pauseMessagePause;
		}
	}
	
	
	private void doAssetProcessing() {
		this.assetsLoaded = true;
		if(Configuration.debugLevel >= Application.LOG_INFO) Gdx.app.log("GameScreen", "Assetprocessing aufgerufen");
		
		float biggerValue = this.arenaSizeX;
		if(this.arenaSizeY > this.arenaSizeX) biggerValue = this.arenaSizeY;
		float timesScreen = biggerValue / Configuration.VIEWPORT_WIDTH;
		
	    this.boundaries = new BoundaryCollection(world, this.arenaSizeX, this.arenaSizeY);	    
		
		this.background = new ParallaxLayer(timesScreen, 1f/2f, "backlayer.png", true);
		if(this.debug > 1)  this.debuglayer = new ParallaxLayer(timesScreen, 1f, "debuglayer.png", false);
		this.foreground = new ParallaxLayer(timesScreen, 1.2f, "forelayer.png", true);
		
		this.pauseLayer = new Sprite(GameAssets.fetchTextureAtlas("generic/game_objects.pack").findRegion("blackLayer"));
   		this.pauseLayer.setSize(Configuration.TARGET_WIDTH+10f, Configuration.TARGET_HEIGHT+10f);
   		this.pauseLayer.setPosition(-5f, -5f);
   		
   		this.energySprite = new Sprite(GameAssets.fetchTextureAtlas("generic/game_objects.pack").findRegion("energy"));
   		this.energySprite.setSize(GameAssets.fetchTextureAtlas("generic/game_objects.pack").findRegion("energy").getRegionHeight(), GameAssets.fetchTextureAtlas("generic/game_objects.pack").findRegion("energy").getRegionWidth());
		
		if(jelly == null) {
			this.jelly = new Jelly(world,this.jellyStartX,this.jellyStartY);
			this.camera = new OrthographicCamera(Configuration.VIEWPORT_WIDTH, Configuration.VIEWPORT_HEIGHT);
			this.camera.zoom = 1.0f;
			this.camera.translate(this.jellyStartX,this.jellyStartY);
			this.background.translateCamera(this.jellyStartX,this.jellyStartY);
			this.foreground.translateCamera(this.jellyStartX,this.jellyStartY);
			if(this.debug > 1)  this.debuglayer.translateCamera(this.jellyStartX,this.jellyStartY);
			
			this.camera.rotate(jellyStartRotation);
			this.background.rotateCamera(jellyStartRotation);
			if(this.debug > 1)  this.debuglayer.rotateCamera(jellyStartRotation);
			this.foreground.rotateCamera(jellyStartRotation);
			this.jelly.rotate(jellyStartRotation);
		}
	   	else this.jelly.resetGraphics(GameAssets.fetchTextureAtlas("generic/game_objects.pack"));
		
		
		if(this.dashMeter == null) {
			this.dashMeter = new GraphicMeter(Configuration.bezugifyX(1135f), Configuration.bezugifyY(770), "meter_track_big","meter_display_big",false, GameAssets.fetchTextureAtlas("generic/game_objects.pack"), Configuration.emptyVector2);
		} else this.dashMeter.resetGraphics();
		
		
		if(this.oxygen == null) 
		{
			GameObjectPool oxygenPool = new GameObjectPool(8, 15, new OxygenFactory(Oxygen.class, world));
			this.oxygen = new GameObjectCollection(Oxygen.class, oxygenPool);
			this.oxygen.spawnPooledGameObject(Oxygen.class, 2f, 2f, 1f, -2f);
			this.oxygen.spawnPooledGameObject(Oxygen.class, -5f, -1.3f);
			this.oxygen.spawnPooledGameObject(Oxygen.class, 8f, -2f);
			this.oxygen.spawnPooledGameObject(Oxygen.class, -6.6f, 9f);
			this.oxygen.spawnPooledGameObject(Oxygen.class, -13f, -13f);
			this.oxygen.spawnPooledGameObject(Oxygen.class, 12f, -12.5f);
			this.oxygen.spawnPooledGameObject(Oxygen.class, -11f, 13.5f);
		} else {
			this.oxygen.resetGraphics(GameAssets.fetchTextureAtlas("generic/game_objects.pack"));
		}
		
		if(this.enemies == null) 
		{
			this.enemies = new GameObjectCollection(10);
			GameObjectPool tmpPool = new GameObjectPool(1, 6, new EnemyFactory(EnemyBig.class, world, EnemyAbstract.ENEMY_BIG));
			this.enemies.addPool(EnemyBig.class, tmpPool);
			
			tmpPool = new GameObjectPool(2, 6, new EnemyFactory(EnemySmall.class, world, EnemyAbstract.ENEMY_SMALL));
			this.enemies.addPool(EnemySmall.class, tmpPool);
			
			tmpPool = null;
			this.enemies.preFillPools();
			
			this.enemies.spawnPooledGameObject(EnemyBig.class, 7f, 10f);
			this.enemies.spawnPooledGameObject(EnemySmall.class, -11f, -15f);
			this.enemies.spawnPooledGameObject(EnemySmall.class, -13f, 15f);
		} else {
			this.enemies.resetGraphics(GameAssets.fetchTextureAtlas("generic/game_objects.pack"));
		}
		
		
		if(this.powerups == null) 
		{
			this.powerups = new GameObjectCollection(15);
			GameObjectPool tmpPool = new GameObjectPool(1,1, new PowerupFactory(PowerupDash.class, world,PowerupAbstract.DASH_POWERUP));
			this.powerups.addPool(PowerupDash.class, tmpPool);
			
			tmpPool = new GameObjectPool(5,8, new PowerupFactory(PowerupEnergy.class, world,PowerupAbstract.ENERGY_POWERUP));
			this.powerups.addPool(PowerupEnergy.class, tmpPool);
			
			tmpPool = new GameObjectPool(2,5, new PowerupFactory(PowerupGhost.class, world,PowerupAbstract.GHOST_POWERUP));
			this.powerups.addPool(PowerupGhost.class, tmpPool);
			
			tmpPool = new GameObjectPool(2,5, new PowerupFactory(PowerupPunch.class, world,PowerupAbstract.PUNCH_POWERUP));
			this.powerups.addPool(PowerupPunch.class, tmpPool);
			
			tmpPool = null;
			this.powerups.preFillPools();
			
			
			this.powerups.spawnPooledGameObject(PowerupGhost.class, 7.15f, -3.5f);
			this.powerups.spawnPooledGameObject(PowerupEnergy.class, -13.5f, 1f);
			this.powerups.spawnPooledGameObject(PowerupEnergy.class, -13.5f, -1f);
			this.powerups.spawnPooledGameObject(PowerupEnergy.class, -5.5f, 9.5f, (float) Math.toRadians(110f));
			this.powerups.spawnPooledGameObject(PowerupDash.class, 13f, 8f);
			this.powerups.spawnPooledGameObject(PowerupEnergy.class, 9f, -13.5f);
			this.powerups.spawnPooledGameObject(PowerupEnergy.class, -9.6f, 11.6f, (float) Math.toRadians(340f));
		} else {
			this.powerups.resetGraphics(GameAssets.fetchTextureAtlas("generic/game_objects.pack"));
		}
		
		
		
		if(this.obstacles == null) 
		{
			//Collection
			this.obstacles = new ObstacleCollection(30);
			
			//Pools:
			GameObjectPool obstaclePool = new GameObjectPool(2, 4, new ObstacleFactory(EnemyAbstract.class, world, "obs_corn1", "obs_corn1"));
			this.obstacles.addPool("obs_corn1", obstaclePool);
			
			obstaclePool = new GameObjectPool(2, 4, new ObstacleFactory(EnemyAbstract.class, world, "obs_corn2", "obs_corn2"));
			this.obstacles.addPool("obs_corn2", obstaclePool);
			
			obstaclePool = new GameObjectPool(1, 6, new ObstacleFactory(EnemyAbstract.class, world, "obs_curv1", "obs_curv1"));
			this.obstacles.addPool("obs_curv1", obstaclePool);
			
			obstaclePool = new GameObjectPool(1, 6, new ObstacleFactory(EnemyAbstract.class, world, "obs_stain1", "obs_stain1"));
			this.obstacles.addPool("obs_stain1", obstaclePool);
		
			obstaclePool = new GameObjectPool(5, 8, new ObstacleFactory(EnemyAbstract.class, world, "obs_straight1", "obs_straight1"));
			this.obstacles.addPool("obs_straight1", obstaclePool);
			
			obstaclePool = new GameObjectPool(5, 8, new ObstacleFactory(EnemyAbstract.class, world, "obs_straight2", "obs_straight2"));
			this.obstacles.addPool("obs_straight2", obstaclePool);
			
			obstaclePool = new GameObjectPool(5, 8, new ObstacleFactory(EnemyAbstract.class, world, "obs_straight3", "obs_straight3"));
			this.obstacles.addPool("obs_straight3", obstaclePool);
			
			obstaclePool = new GameObjectPool(5, 8, new ObstacleFactory(EnemyAbstract.class, world, "obs_straight4", "obs_straight4"));
			this.obstacles.addPool("obs_straight4", obstaclePool);
			
			obstaclePool = null;
			
			//Fill Pools
			this.obstacles.preFillPools();
			
			//Spawn Objects
			this.obstacles.spawnPooledGameObject("obs_corn1", -15f, 17.5f);
			this.obstacles.spawnPooledGameObject("obs_corn2", 15f, 17.5f, (float) Math.toRadians(-90f));
			this.obstacles.spawnPooledGameObject("obs_corn2", -15f, -17.5f, (float) Math.toRadians(90f));
			this.obstacles.spawnPooledGameObject("obs_corn1", 15f, -17.5f, (float) Math.toRadians(180f));
			
			this.obstacles.spawnPooledGameObject("obs_straight2", -15f, 0f, (float) Math.toRadians(90f));
			this.obstacles.spawnPooledGameObject("obs_straight1", -15f, 8f, (float) Math.toRadians(-90f));
			this.obstacles.spawnPooledGameObject("obs_straight1", -15f, -8f, (float) Math.toRadians(90f));
			
			this.obstacles.spawnPooledGameObject("obs_straight3", 15f, 0f, (float) Math.toRadians(90f));
			this.obstacles.spawnPooledGameObject("obs_straight2", 15f, 8f, (float) Math.toRadians(-90f));
			this.obstacles.spawnPooledGameObject("obs_straight3", 15f, -8f, (float) Math.toRadians(90f));
			
			this.obstacles.spawnPooledGameObject("obs_straight4", 0f, -17.5f);
			this.obstacles.spawnPooledGameObject("obs_straight3", 8f, -17.5f);
			this.obstacles.spawnPooledGameObject("obs_straight4", -8f, -17.5f);
			
			this.obstacles.spawnPooledGameObject("obs_straight2", 0f, 17.5f);
			this.obstacles.spawnPooledGameObject("obs_straight1", 8f, 17.5f);
			this.obstacles.spawnPooledGameObject("obs_straight4", -8f, 17.5f);
			
			this.obstacles.spawnPooledGameObject("obs_straight3", 6f, 1f, (float) Math.toRadians(90f), true);
			this.obstacles.spawnPooledGameObject("obs_straight2", -4f, -1.5f, (float) Math.toRadians(90f), true);
			this.obstacles.spawnPooledGameObject("obs_straight1", 7f, -4.8f, true);
			this.obstacles.spawnPooledGameObject("obs_straight3", -3f, 8.5f, (float) Math.toRadians(15f), true);
			this.obstacles.spawnPooledGameObject("obs_straight1", -4f, 11.5f, (float) Math.toRadians(17f), true);
			this.obstacles.spawnPooledGameObject("obs_straight4", -7f, -7.3f, true);
			this.obstacles.spawnPooledGameObject("obs_stain1", -10f, 10f, true);
			this.obstacles.spawnPooledGameObject("obs_straight2", -0.2f, -11.4f, (float) Math.toRadians(180f), true);
			this.obstacles.spawnPooledGameObject("obs_straight4", 9.7f, -11.5f, (float) Math.toRadians(180f),true);
			this.obstacles.spawnPooledGameObject("obs_curv1", 8.5f, 9.5f, (float) Math.toRadians(80f), true);
			
		} else {
			this.obstacles.resetGraphics(GameAssets.fetchTextureAtlas("generic/game_objects.pack"));
		}
		
		
		if(this.dropzones == null) 
		{
			GameObjectPool dropzonesPool = new GameObjectPool(1, 3, new DropzoneFactory(Dropzone.class, world));
			this.dropzones = new GameObjectCollection(Dropzone.class, dropzonesPool);
			GameObject o = this.dropzones.spawnPooledGameObject(Dropzone.class, 11f, 13f);
			o.setAsSensor(true);
		} else {
			this.dropzones.resetGraphics(GameAssets.fetchTextureAtlas("generic/game_objects.pack"));
		}
		
		if(this.debug >= 2) this.debugRenderer = new Box2DDebugRenderer();
		
		this.font = GameAssets.fetchFont("fonts/bradBury.fnt");
		this.layout = new GlyphLayout();
	   	if(tweenManager == null) this.tweenManager = new TweenManager();
	}
	
	
	public World getActualWorld() {
		return this.world;
	}
	
	
	
	@Override
	public void stepBack(String source) {
		if(this.assetsLoaded == true && this.disposed == false) {
			if(GameScreen.paused) {
				this.doDisposing = true;
			} else {
				if(source.equals("touch")) this.pause();
			}
		}
	}



	@Override
	public void startPress() {
		if(this.assetsLoaded == true && this.disposed == false) {
			if(GameScreen.paused) {
				this.endPause();
			} else {
				this.pause();
			}
		}
	}



	@Override
	public void primaryPress() {
		if(this.assetsLoaded == true && this.disposed == false) {
			if(GameScreen.paused) {
				this.endPause();
			}
		}
	}



	@Override
	public void secondaryPress() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void steerXAxis(float peculiarity) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void steerYAxis(float peculiarity) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean screenTouched(Vector2 touchPoint, int pointer) {
		if(GameScreen.paused == true) {
			this.endPause();
			return true;
		} else return false;		
	}

	


}
