package com.xsheetgames.bloodstream;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;



public class MainActivity extends AndroidApplication implements iNativeFunctions {
    
	ControllerUtils controllerUtils = null;
	Game mGame = null;
	RelativeLayout layout;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{		
        super.onCreate(savedInstanceState);
		layout = new RelativeLayout(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = true;
        cfg.useCompass = false;
        cfg.useWakelock = true;
        
        this.mGame = new BloodstreamGdxGame(this);
		View gameView = initializeForView(this.mGame, cfg);

        setContentView(gameView);
        
        this.controllerUtils = new ControllerUtils(this.mGame);
        this.controllerUtils.initializeControllers(this, handler);
    }
	
	@Override
	public void onDestroy() 
	{
		this.controllerUtils.destroy();
		super.onDestroy();
	}
	
	@Override
	public void onPause() 
	{
		this.controllerUtils.pause();
		super.onPause();		
	}
	
	
	@Override
	public void onResume()
	{
		super.onResume();
		this.controllerUtils.resume();
	}
	
	
	public Game getGame() {
		return this.mGame;
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
	
	@SuppressLint("DefaultLocale")
	@Override
	public String numberFormat(float number) {
		return String.format("%.2f",number);
	}

	@Override
	public boolean pollControllerButtonState(int keycode) {
		return this.controllerUtils.pollControllerButtonState(keycode);
	}

	@Override
	public float pollControllerAxis(int axis) {
		return this.controllerUtils.pollControllerAxis(axis);
	}

	
}