package com.xsheetgames.bloodstream.elements;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.xsheetgames.bloodstream.Configuration;
import com.xsheetgames.bloodstream.GameAssets;
import com.xsheetgames.bloodstream.gameObjects.Jelly;



public class ParallaxLayer {

	private OrthographicCamera camera;
	private Texture texture;
	private Vector2 positionOld, positionNew;
	private float tileCount;
	private float speedMultiplier;
	
	public ParallaxLayer(float timesScreen, float speedMultiplier, String tileName, boolean oversizing) {
		this.camera = new OrthographicCamera(Configuration.VIEWPORT_WIDTH, Configuration.VIEWPORT_HEIGHT);
		texture = GameAssets.fetchTexture("tiles/"+tileName);
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		positionOld = new Vector2();
		positionNew = new Vector2();
		this.speedMultiplier = speedMultiplier;
		
		int arenaOversizing = 0;
		if(oversizing) {
			arenaOversizing = (int) (Math.round(Configuration.VIEWPORT_WIDTH / (float)Configuration.toMeter(texture.getWidth()) + 0.5f) * 3 * speedMultiplier);
		}
		
		tileCount = (int) (Math.round(Configuration.VIEWPORT_WIDTH / (float)Configuration.toMeter(texture.getWidth()) + 0.5f) * timesScreen * speedMultiplier) + arenaOversizing;
	}

	
	public OrthographicCamera getCamera() {
		return this.camera;
	}
	
	public void rotateCamera(float angle) {
		this.camera.rotate(angle);
	}
	
	
	public void translateCamera(float x, float y) {
		this.camera.translate(x * this.speedMultiplier, y * this.speedMultiplier);
	}
	
	public void setCameraCombinedAndDraw(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(texture, (Configuration.toMeter(texture.getWidth()) * tileCount) *-1f /2f, (Configuration.toMeter(texture.getWidth()) * tileCount) *-1f /2f,
				Configuration.toMeter(texture.getWidth()) * tileCount, 
				Configuration.toMeter(texture.getHeight()) * tileCount, 
				0, tileCount, 
				tileCount, 0);
		batch.end();		
	}
	
	
	public void updateCamera() {
		this.camera.update();
	}
	
	
	
	public void doMotionLogicStep(Jelly jelly, float dt) {
		if(jelly.isDisposed() == false && jelly.getBody() != null) {
			float mx = jelly.getBody().getLinearVelocity().x;
			float my = jelly.getBody().getLinearVelocity().y;
			float atan2 = (float) (Math.atan2(my * -1, mx * -1) / Math.PI * 180 + 180);
			positionNew.x = (float) (jelly.getBody().getLinearVelocity().len() * dt * speedMultiplier * Math.cos(Math.toRadians(atan2))) + positionOld.x;
			positionNew.y = (float) (jelly.getBody().getLinearVelocity().len() * dt * speedMultiplier * Math.sin(Math.toRadians(atan2))) + positionOld.y;
			
			camera.translate(positionNew.x - positionOld.x, positionNew.y - positionOld.y);
			positionOld = positionNew.cpy();
		}
	}
	

	
}
