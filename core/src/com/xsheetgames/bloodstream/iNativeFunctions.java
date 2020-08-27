package com.xsheetgames.bloodstream;

import com.badlogic.gdx.Game;

public interface iNativeFunctions {
	
	//misc
	public Game getGame();
	public void showMessage(String title, String message);
	public void openURL(String url);
	public void share(String subject, String text);
	public void rate();
	public void more();
	
	//Controller
	public boolean pollControllerButtonState(int keycode);
	public float pollControllerAxis(int axis);
	
	//Analytics
	public void initializeAnalytics();	
	public void trackPageView(String path);
	
	//Event and Exceptionhandling
	public void sendException(String description, boolean fatal);
	public void sendEvent(String category, String subCategory, String component, long value);
    
	
	//Ad Handling
	public void showFullScreenAd(String adPoint);
	public void showBannerAd();
	public void closeBannerAd();
	public void cacheAds();
    
	//in app purchase and licence check
	public void doLicencingCheck();    
	public void purchaseItem(String itemname);
    
	//utils
    public String numberFormat(float number);
}