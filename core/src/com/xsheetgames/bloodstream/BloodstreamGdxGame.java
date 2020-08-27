package com.xsheetgames.bloodstream;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.xsheetgames.bloodstream.elements.GameContactListener;
import com.xsheetgames.bloodstream.gameObjects.GameObject;
import com.xsheetgames.bloodstream.gameObjects.GameObjectAccessor;
import com.xsheetgames.bloodstream.screens.MenuScreen;




public class BloodstreamGdxGame extends Game {

	World w;

    public BloodstreamGdxGame(iNativeFunctions nativeFunctions) {
    	GameAssets.setNative(nativeFunctions);
    }
	   
	
	@Override
	public void create() {
		Configuration.load();
		if(w == null) {
			w = new World(new Vector2(0f,0f), true);
			w.setContactListener(new GameContactListener());
		}
		GameAssets.load();
		Gdx.app.setLogLevel(Configuration.debugLevel);
		Tween.registerAccessor(GameObject.class, new GameObjectAccessor());
		Gdx.input.setCatchMenuKey(true);
		setScreen(new MenuScreen(this));
    }
	   
	   

   @Override
   public void render() {
	   //try {
	   		Gdx.gl.glClearColor(0, 0, 0, 1);
	   		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	   		super.render();
	   /*} catch(Exception e) {
		   try {
			   	GameAssets.nativ.trackPageView("/Exception");
			   	String myException = "";
			   	String ExceptionName = "";
			   	for(StackTraceElement ste : e.getStackTrace()) {
			   		myException += ste.toString() + "\r\n";
			   	}
			   	if(e.getMessage() == null || e.getMessage() == "" || e.getMessage() == "null") ExceptionName = e.getClass().getName();
			   	else ExceptionName = e.getMessage();
		   		GameAssets.nativ.sendException(ExceptionName + "\r\n" + myException, true);
		   } catch( Exception ex) {
			   
		   }
		   try {
			   if(Gdx.files.isExternalStorageAvailable()) {
				   Date n = new Date();
				   String stackTrace = "";
				   String ExceptionName = "";
				   if(e.getMessage() == null || e.getMessage() == "" || e.getMessage() == "null") ExceptionName = e.getClass().getName();
				   else ExceptionName = e.getMessage();
				   for(StackTraceElement element : e.getStackTrace()) {
					   stackTrace += "\r\n"+element.toString();
				   }
				   if(GameAssets.LogFileHandle != null) GameAssets.LogFileHandle.writeString("\r\n\r\n\r\n\r\n\r\n-----------------------\r\nException occured. Time: "+n.toString() +"\r\nMessage: "+ExceptionName+"\r\nStackTrace: "+stackTrace+"\r\n-----------------------", true);
			   }
		   }
		   catch(Exception ex) {
			   
		   }
		   finally {
			   if(Configuration.debugLevel >= Application.LOG_DEBUG) {
				   Gdx.app.error("Exception occured", "OH NO, AN EXCEPTION!", e);
			   }
			   Gdx.app.exit();
		   }
	   }*/
   }
   
   

   
   
   public void dispose() {
	   try {
		   this.getScreen().dispose();
		   GameAssets.dispose();
		   super.dispose();
	   } catch(Exception excp) { }	   
		
   }

}
