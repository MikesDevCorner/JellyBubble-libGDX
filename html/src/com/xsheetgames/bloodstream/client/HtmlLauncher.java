package com.xsheetgames.bloodstream.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.xsheetgames.bloodstream.BloodstreamGdxGame;
import com.xsheetgames.bloodstream.iNativeFunctions;

public class HtmlLauncher extends GwtApplication implements iNativeFunctions {

        // USE THIS CODE FOR A FIXED SIZE APPLICATION
        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }
        // END CODE FOR FIXED SIZE APPLICATION

        // UNCOMMENT THIS CODE FOR A RESIZABLE APPLICATION
        // PADDING is to avoid scrolling in iframes, set to 20 if you have problems
        // private static final int PADDING = 0;
        // private GwtApplicationConfiguration cfg;
        //
        // @Override
        // public GwtApplicationConfiguration getConfig() {
        //     int w = Window.getClientWidth() - PADDING;
        //     int h = Window.getClientHeight() - PADDING;
        //     cfg = new GwtApplicationConfiguration(w, h);
        //     Window.enableScrolling(false);
        //     Window.setMargin("0");
        //     Window.addResizeHandler(new ResizeListener());
        //     cfg.preferFlash = false;
        //     return cfg;
        // }
        //
        // class ResizeListener implements ResizeHandler {
        //     @Override
        //     public void onResize(ResizeEvent event) {
        //         int width = event.getWidth() - PADDING;
        //         int height = event.getHeight() - PADDING;
        //         getRootPanel().setWidth("" + width + "px");
        //         getRootPanel().setHeight("" + height + "px");
        //         getApplicationListener().resize(width, height);
        //         Gdx.graphics.setWindowedMode(width, height);
        //     }
        // }
        // END OF CODE FOR RESIZABLE APPLICATION

        @Override
        public ApplicationListener createApplicationListener () {
                return new BloodstreamGdxGame(this);
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