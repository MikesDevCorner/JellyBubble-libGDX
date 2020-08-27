package com.xsheetgames.bloodstream;

import java.util.Date;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;



public class GameAssets {

	public static BodyEditorLoader objectLoader;
	public static AssetManager manager, loadingManager;
	public static Vector2 emptyVector2 = new Vector2();
	public static Animation<TextureRegion> loadingAnimation;
	public static iNativeFunctions nativ; 
	public static FileHandle LogFileHandle;
	public static FileHandle settingsFileHandle;
	
	//FINAL KEY STATICS FOR POLLING INPUTS FROM SCREEN
	public static final int KEY_UP = 1;
	public static final int KEY_DOWN = 2;
	public static final int KEY_LEFT = 3;
	public static final int KEY_RIGHT = 4;
	public static final int KEY_PRIMARY = 5;
	public static final int KEY_SECONDARY = 6;
	public static final int KEY_START = 7;
	public static final int AXIS_X = 8;
	public static final int AXIS_Y = 9;
	/******************************************************************************************/
	
	public static void load() {
		GameAssets.LogFileHandle = null;
		GameAssets.settingsFileHandle = null;
		manager = new AssetManager();
		loadingManager = new AssetManager();
		objectLoader = new BodyEditorLoader(Gdx.files.internal("generic.json"));
		loadAssetsForLoadingScreen(true);
	}
	
	
	private static void initStaticFiles() {
		
		if(Gdx.files.isExternalStorageAvailable()) {
			GameAssets.LogFileHandle = Gdx.files.external("bloodstream/errorlog.txt");
		}
		
		if(Gdx.files.isLocalStorageAvailable()) {
			
			GameAssets.settingsFileHandle = Gdx.files.local("bloodstream/settings.txt");
		
			//Settings:
			try {
				if(GameAssets.settingsFileHandle.exists() == false) { //create settings if not exists
					GameAssets.settingsFileHandle.writeString(Configuration.musicEnabled + ";" + Configuration.soundEnabled + ";" + Configuration.vibrateEnabled,false);
				}
				
				String settingsString = GameAssets.settingsFileHandle.readString();
				String[] splitResult = settingsString.split(";");
				Configuration.musicEnabled = Boolean.parseBoolean(splitResult[0]);
				Configuration.soundEnabled = Boolean.parseBoolean(splitResult[1]);
				Configuration.vibrateEnabled = Boolean.parseBoolean(splitResult[2]);
				
				
			} catch(Exception e) {
				Date n = new Date();
				if(GameAssets.LogFileHandle != null) GameAssets.LogFileHandle.writeString(n.toString() + ": Problem reading the settings.txt.", true);
			}			
			
		} else {
			GameAssets.LogFileHandle = null;
		}
		
	}
	
	
	public static void loadAssetsForLoadingScreen(boolean firstTime) {		
		
		loadingManager.load("graphics" + Configuration.TARGET_WIDTH + "/loading/loading.pack", TextureAtlas.class);
		loadingManager.load("graphics" + Configuration.TARGET_WIDTH + "/loading/loading_back.jpg", Texture.class);
		loadingManager.load("graphics" + Configuration.TARGET_WIDTH + "/fonts/bradBury.fnt", BitmapFont.class);
		loadingManager.finishLoading();	
		
		
		loadingAnimation = new Animation(1/60f, loadingManager.get("graphics" + Configuration.TARGET_WIDTH + "/loading/loading.pack", TextureAtlas.class).findRegions("loading_bar"));
		
		if(firstTime) initStaticFiles();		
		if(firstTime) loadMenuAssets();
	}
	
	
	
	public static void loadMenuAssets() {
		manager.load("graphics" + Configuration.TARGET_WIDTH + "/menu/menuScreen.jpg", Texture.class);		
		manager.load("graphics" + Configuration.TARGET_WIDTH + "/menu/menu_items.pack", TextureAtlas.class);		
		manager.load("sounds/click.ogg", Sound.class);
	}
	
	public static void unloadMenuAssets() {
		try { manager.unload("graphics" + Configuration.TARGET_WIDTH + "/menu/menuScreen.jpg"); } catch(Exception e) {}
		try { manager.unload("graphics" + Configuration.TARGET_WIDTH + "/menu/menu_items.pack"); } catch(Exception e) {}		
		try { manager.unload("sounds/click.ogg"); } catch(Exception e) {}
	}
	
	
	public static void loadGameAssets() {
		manager.load("graphics" + Configuration.TARGET_WIDTH + "/tiles/backlayer.png", Texture.class);
		manager.load("graphics" + Configuration.TARGET_WIDTH + "/tiles/debuglayer.png", Texture.class);
		manager.load("graphics" + Configuration.TARGET_WIDTH + "/tiles/forelayer.png", Texture.class);
		manager.load("graphics" + Configuration.TARGET_WIDTH + "/generic/game_objects.pack", TextureAtlas.class);		
	}
	
	
	public static void unloadGameAssets() {
		try { manager.unload("graphics" + Configuration.TARGET_WIDTH + "/tiles/backlayer.png"); } catch(Exception e) {}
		try { manager.unload("graphics" + Configuration.TARGET_WIDTH + "/tiles/debuglayer.png"); } catch(Exception e) {}
		try { manager.unload("graphics" + Configuration.TARGET_WIDTH + "/tiles/forelayer.png"); } catch(Exception e) {}
		try { manager.unload("graphics" + Configuration.TARGET_WIDTH + "/generic/game_objects.pack"); } catch(Exception e) {}	
	}
	
	public static void setNative(iNativeFunctions n) {
		nativ = n;
	}
	
	/******************************************************************************************/
	
	
	public static void playSound (Sound sound) {
		if (Configuration.soundEnabled == true) sound.play(1f);
	}
	
	public static void playSound (Sound sound, float volume) {
		if (Configuration.soundEnabled == true) sound.play(volume);
	}
	
	public static void playMusic (Music music, boolean looping, float volume) {
		if (Configuration.musicEnabled == true) {
			music.setLooping(looping);
			music.setVolume(volume);
			music.play();
		}
	}
	
	public static void pauseMusic(Music music) {
		try {
			music.pause();
		} catch(Exception e) {}
	}	
	
	public static void queueTextureAssetLoading(String asset) {
		manager.load(asset, Texture.class);
	}
	
	public static void queueSoundAssetLoading(String asset) {
		manager.load(asset, Sound.class);
	}
	
	public static void queueMusicAssetLoading(String asset) {
		manager.load(asset, Music.class);
	}
	
	public static void queueTextureAtlasAssetLoading(String asset) {
		manager.load(asset, TextureAtlas.class);
	}
	
	
	public static Texture fetchTexture(String name) {
		if(manager.isLoaded("graphics" + Configuration.TARGET_WIDTH + "/" + name)) {
			return manager.get("graphics" + Configuration.TARGET_WIDTH + "/" + name, Texture.class);
		} else return null;
	}
	
	public static BitmapFont fetchFont(String name) {
		if(loadingManager.isLoaded("graphics" + Configuration.TARGET_WIDTH + "/" + name)) {
			return loadingManager.get("graphics" + Configuration.TARGET_WIDTH + "/" + name, BitmapFont.class);
		} else return null;
	}
	
	public static Sound fetchSound(String name) {
		if(manager.isLoaded(name)) {
			return manager.get(name, Sound.class);
		} else return null;
	}
	
	public static Music fetchMusic(String name) {
		if(manager.isLoaded(name)) {
			return manager.get(name, Music.class);
		} else return null;
	}
	
	public static TextureAtlas fetchTextureAtlas(String name) {
		if(manager.isLoaded("graphics" + Configuration.TARGET_WIDTH + "/" + name)) {
			return manager.get("graphics" + Configuration.TARGET_WIDTH + "/" + name, TextureAtlas.class);
		} else return null;
	}
	
	public static void vibrate(int duration) {
		if(Configuration.vibrateEnabled) Gdx.input.vibrate(duration);
	}
	
	
	
	/******************************************************************************************/
	
	public static boolean areGameAssetsLoaded() {
		return manager.isLoaded("graphics" + Configuration.TARGET_WIDTH + "/" + "tiles/backlayer.png");
	}
	
	
	public static boolean assetsLoaded(SpriteBatch batch) {
		if(manager != null && batch != null) {
			boolean val = manager.update();
			
			if(val == false) {
				batch.getProjectionMatrix().setToOrtho2D(0, 0, Configuration.TARGET_WIDTH, Configuration.TARGET_HEIGHT);
				drawLoadingScreen(batch);
			}
			return val;
		} else return false;
	}
	
	
	public static boolean loadAssets() {
		return !manager.update();
	}
	
	public static float getLoadingProcess() {
		return manager.getProgress();
	}
	
	public static int getLoadingSteps(int step) {
		int progress = (int)(getLoadingProcess()*100);
		progress = ((int)(progress/step))*step;
		return progress;	
	}
	
	public static void drawLoadingScreen(SpriteBatch batch) {
		batch.begin();
		Texture tex = loadingManager.get("graphics" + Configuration.TARGET_WIDTH + "/loading/loading_back.jpg", Texture.class);
		float tmpWidth = tex.getWidth();
		float tmpHeight = tex.getHeight();
		float ratio = tmpWidth / tmpHeight;
		float newHeight = Configuration.TARGET_WIDTH / ratio;
		float distance = (newHeight - Configuration.TARGET_HEIGHT) / 2f;
		batch.draw(tex, 0, distance*-1, Configuration.TARGET_WIDTH, newHeight);
		batch.draw(loadingAnimation.getKeyFrame(getLoadingProcess()),Configuration.bezugifyX(270),Configuration.bezugifyY(90)-distance);		
		batch.end();
	}
	
	public static void unloadAsset(String fileName) {
		manager.unload(fileName);
	}
	
	public static BodyEditorLoader getObjectLoader() {
		return objectLoader;
	}
	
	public static void dispose() {
		manager.dispose();
		loadingManager.dispose();
		manager = null;
		loadingManager = null;
		objectLoader = null;
	}	
	
}