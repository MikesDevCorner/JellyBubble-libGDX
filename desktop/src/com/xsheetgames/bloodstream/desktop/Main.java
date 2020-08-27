package com.xsheetgames.bloodstream.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.xsheetgames.bloodstream.BloodstreamGdxGame;
import com.xsheetgames.bloodstream.iNativeFunctions;


public class Main implements iNativeFunctions {
	
	static Game game;
	static ControllerUtils controllerUtils;
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		Main nf = new Main();
		cfg.title = "bloodstream";
		//cfg.useGL20 = true;
		cfg.width = 800; //800;  //1280;  //480;
		cfg.height = 480;  //480;  //800;  //320;
        //cfg.fullscreen = true;
        cfg.vSyncEnabled = true;
		Main.game = new BloodstreamGdxGame(nf);
		new LwjglApplication(game, cfg);
		controllerUtils = new ControllerUtils();
		controllerUtils.initializeControllers();
		
	}
	
	public Game getGame() {
		return game;
	}

	@Override
	public void showMessage(String title, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openURL(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void share(String subject, String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void more() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initializeAnalytics() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trackPageView(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendException(String description, boolean fatal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendEvent(String category, String subCategory,
			String component, long value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showFullScreenAd(String adPoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showBannerAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeBannerAd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cacheAds() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doLicencingCheck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void purchaseItem(String itemname) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String numberFormat(float number) {
		return String.format("%.2f",number);
	}

	@Override
	public boolean pollControllerButtonState(int keycode) {
		return controllerUtils.pollControllerButtonState(keycode);
	}

	@Override
	public float pollControllerAxis(int axis) {
		return controllerUtils.pollControllerAxis(axis);
	}

	
	
	
	
	
	
}
