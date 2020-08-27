package com.xsheetgames.bloodstream;

import com.badlogic.gdx.Game;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.xsheetgames.bloodstream.BloodstreamGdxGame;

public class IOSLauncher extends IOSApplication.Delegate implements iNativeFunctions {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return new IOSApplication(new BloodstreamGdxGame(this), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public void showMessage(String title, String message) {

    }

    @Override
    public void openURL(String url) {

    }

    @Override
    public void share(String subject, String text) {

    }

    @Override
    public void rate() {

    }

    @Override
    public void more() {

    }

    @Override
    public boolean pollControllerButtonState(int keycode) {
        return false;
    }

    @Override
    public float pollControllerAxis(int axis) {
        return 0;
    }

    @Override
    public void initializeAnalytics() {

    }

    @Override
    public void trackPageView(String path) {

    }

    @Override
    public void sendException(String description, boolean fatal) {

    }

    @Override
    public void sendEvent(String category, String subCategory, String component, long value) {

    }

    @Override
    public void showFullScreenAd(String adPoint) {

    }

    @Override
    public void showBannerAd() {

    }

    @Override
    public void closeBannerAd() {

    }

    @Override
    public void cacheAds() {

    }

    @Override
    public void doLicencingCheck() {

    }

    @Override
    public void purchaseItem(String itemname) {

    }

    @Override
    public String numberFormat(float number) {
        return null;
    }
}