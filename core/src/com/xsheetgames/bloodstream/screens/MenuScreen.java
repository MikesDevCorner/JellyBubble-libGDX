package com.xsheetgames.bloodstream.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.xsheetgames.bloodstream.Configuration;
import com.xsheetgames.bloodstream.GameAssets;


public class MenuScreen extends AbstractScreen {

	private SpriteBatch batch;
	private Sprite screenBackground, playButton;
	private boolean assetsLoaded;
	private boolean disposed = false;
	private boolean endApp = false;
	private boolean doChangeGame = false;
	private float distance;
	
	public MenuScreen(Game game) {
		this.game = game;
	}
	
	
	@Override
	public void render(float delta) {
		
		if(this.endApp == true) {
			this.dispose();
			GameAssets.nativ.trackPageView("/Exit");
			Gdx.app.exit();
		}
		
		else if(this.doChangeGame == true && this.disposed == false) {
			this.changeToGame();
		}
		
		else if(this.disposed == false) {
			if(GameAssets.assetsLoaded(batch)) {				
				if(assetsLoaded == false) this.doAssetProcessing();
				this.batch.getProjectionMatrix().setToOrtho2D(0, 0, Configuration.TARGET_WIDTH, Configuration.TARGET_HEIGHT);
				this.batch.begin();
				batch.disableBlending();
				this.screenBackground.draw(batch);
				batch.enableBlending();
				this.playButton.draw(batch);				
				this.batch.end();				
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		
		GameAssets.nativ.trackPageView("/MenuScreen");
		
		this.disposed = false;
		this.endApp = false;
		this.doChangeGame = false;
		this.batch = new SpriteBatch();
		this.assetsLoaded = false;
		this.distance = 0f;
	}

	@Override
	public void hide() {
		//this.dispose();		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		GameAssets.loadMenuAssets();
		this.assetsLoaded = false;
	}

	@Override
	public void dispose() {
		if(this.disposed == false) {
			if(this.batch != null) this.batch.dispose();
			this.playButton = null;
			this.screenBackground = null;
			this.disposed = true;
		}		
	}
	
	private void doAssetProcessing() {
		this.assetsLoaded = true;
		if(this.screenBackground == null) {
			this.screenBackground = new Sprite(GameAssets.fetchTexture("menu/menuScreen.jpg"));
			float tmpWidth = GameAssets.fetchTexture("menu/menuScreen.jpg").getWidth();
			float tmpHeight = GameAssets.fetchTexture("menu/menuScreen.jpg").getHeight();
			float ratio = tmpWidth / tmpHeight;
			float newHeight = Configuration.TARGET_WIDTH / ratio;
			this.distance = (newHeight - Configuration.TARGET_HEIGHT) / 2f;
			this.screenBackground.setSize(Configuration.TARGET_WIDTH, newHeight);
			this.screenBackground.setPosition(0f, this.distance*-1);
		}
		if(this.playButton == null) {
			this.playButton = new Sprite(GameAssets.fetchTextureAtlas("menu/menu_items.pack").findRegion("playb"));
			this.playButton.setSize(GameAssets.fetchTextureAtlas("menu/menu_items.pack").findRegion("playb").getRegionWidth(), GameAssets.fetchTextureAtlas("menu/menu_items.pack").findRegion("playb").getRegionHeight());
			this.playButton.setPosition(Configuration.TARGET_WIDTH/2-this.playButton.getWidth()/2 - Configuration.bezugifyX(7f),Configuration.bezugifyX(142f)-this.distance);
		}
		
	}

	
	private void changeToGame() {
		this.dispose();
		//GameAssets.playSound(GameAssets.fetchSound("sounds/click.ogg"));
		GameScreen gs = new GameScreen(this.game);
		GameAssets.unloadMenuAssets();
		GameAssets.loadGameAssets();
		this.game.setScreen(gs);
	}
	
	
	@Override
	public boolean screenTouched(Vector2 touchPoint, int pointer) {
		if(playButton != null && playButton.getBoundingRectangle().contains(touchPoint.x, touchPoint.y)) {
			this.dispose();
			//GameAssets.playSound(GameAssets.fetchSound("sounds/click.ogg"));
			GameScreen gs = new GameScreen(this.game);
			GameAssets.unloadMenuAssets();
			GameAssets.loadGameAssets();
			this.game.setScreen(gs);
			return true;
		} else return false;
	}

	
	@Override
	public void stepBack(String source) {
		if(this.assetsLoaded == true && this.disposed == false) {
			this.endApp = true;
		}
	}


	@Override
	public void startPress() {
		if(this.assetsLoaded == true && this.disposed == false) {
			this.doChangeGame = true;
		}		
	}


	@Override
	public void primaryPress() {
		if(this.assetsLoaded == true && this.disposed == false) {
			this.doChangeGame = true;
		}
	}


	@Override
	public void secondaryPress() {
		//this.stepBack();
	}


	@Override
	public void steerXAxis(float peculiarity) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void steerYAxis(float peculiarity) {
		// TODO Auto-generated method stub
		
	}



}
