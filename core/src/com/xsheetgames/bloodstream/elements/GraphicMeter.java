package com.xsheetgames.bloodstream.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.xsheetgames.bloodstream.Configuration;

public class GraphicMeter {
	
	private Sprite track, display;
	private boolean disposed;
	private boolean meter;
	private float fullWidth;
	private float fullHeight;
	private float displayOffsetX, displayOffsetY, x, y;
	private float percent = 100f;
	private String trackName, displayName;
	private TextureAtlas atlas;
	private Vector2 spriteOriginOffset;
	
	public GraphicMeter(float x, float y, String trackName, String displayName, boolean meter, TextureAtlas atlas, Vector2 spriteOriginOffset) {
		
		this.atlas = atlas;
		
		this.trackName = trackName;
		this.displayName = displayName;
		this.meter = meter;
		
		this.spriteOriginOffset = spriteOriginOffset;
		
		this.disposed = false;
		this.track = new Sprite(atlas.findRegion(trackName));
		if(meter) this.track.setSize(Configuration.toMeter(atlas.findRegion(trackName).getRegionWidth()), Configuration.toMeter(atlas.findRegion(trackName).getRegionHeight()));
		else this.track.setSize(atlas.findRegion(trackName).getRegionWidth(), atlas.findRegion(trackName).getRegionHeight());
		
		
		if(meter) {
			this.fullWidth = Configuration.toMeter(atlas.findRegion(displayName).getRegionWidth());
			this.fullHeight = Configuration.toMeter(atlas.findRegion(displayName).getRegionHeight());
		} else {
			this.fullWidth = atlas.findRegion(displayName).getRegionWidth();
			this.fullHeight = atlas.findRegion(displayName).getRegionHeight();
		}
		
		
		this.display = new Sprite(atlas.findRegion(displayName));
		this.display.setSize(this.fullWidth, this.fullHeight);
		
		/*****************/
		this.displayOffsetX = (this.track.getWidth() - this.display.getWidth()) / 2f;
		this.displayOffsetY = (this.track.getHeight() - this.display.getHeight()) / 2f;	
		/*****************/
		
		this.track.setOrigin(this.track.getWidth()/2 + this.spriteOriginOffset.x, this.track.getHeight()/2 + this.spriteOriginOffset.y);
		this.display.setOrigin(this.display.getWidth()/2 + this.spriteOriginOffset.x, this.display.getHeight()/2 + this.spriteOriginOffset.y);
		
		this.x = x-this.track.getWidth()/2f;
		this.y = y-this.track.getHeight()/2f;
		
		this.track.setPosition(this.x - this.spriteOriginOffset.x,this.y - this.spriteOriginOffset.y);
		this.display.setPosition(this.x + this.displayOffsetX - this.spriteOriginOffset.x,this.y + this.displayOffsetY - this.spriteOriginOffset.y);
	}
	
	
	public void setPercent(float percent) {
		this.percent = percent; 
	}
	
	
	public void setPosition(float x, float y) {
		this.x = x-this.track.getWidth()/2;
		this.y = y-this.track.getHeight()/2;
		this.track.setPosition(this.x - this.spriteOriginOffset.x,this.y - this.spriteOriginOffset.y);
		this.display.setPosition(this.x + displayOffsetX - this.spriteOriginOffset.x,this.y + displayOffsetY - this.spriteOriginOffset.y);
	}
	
	public void setRotation(float degrees) {
		this.track.setRotation(degrees);
		this.display.setRotation(degrees);
	}
	
	public void draw(SpriteBatch batch) {
		if(this.disposed == false)
		{			
			if(this.track != null) track.draw(batch);
			if(this.display != null) {
				float newWidth = percent * fullWidth / 100;
				this.display.setSize(newWidth, this.fullHeight);
				display.draw(batch);
			}
		}
	}
	
	public void resetGraphics() {
		this.track = new Sprite(atlas.findRegion(trackName));
		
		if(meter) this.track.setSize(Configuration.toMeter(atlas.findRegion(trackName).getRegionWidth()), Configuration.toMeter(atlas.findRegion(trackName).getRegionHeight()));
		else this.track.setSize(atlas.findRegion(trackName).getRegionWidth(), atlas.findRegion(trackName).getRegionHeight());
		this.track.setPosition(x - this.spriteOriginOffset.x, y - this.spriteOriginOffset.y);
		
		if(meter) {
			this.fullWidth = Configuration.toMeter(atlas.findRegion(displayName).getRegionWidth());
			this.fullHeight = Configuration.toMeter(atlas.findRegion(displayName).getRegionHeight());
		} else {
			this.fullWidth = atlas.findRegion(displayName).getRegionWidth();
			this.fullHeight = atlas.findRegion(displayName).getRegionHeight();
		}
		
		this.display = new Sprite(atlas.findRegion(displayName));
		this.display.setSize(this.fullWidth, this.fullHeight);
		this.display.setPosition(x + displayOffsetX - this.spriteOriginOffset.x,y + displayOffsetY - this.spriteOriginOffset.y);
	}
	
	public void dispose() {
		this.track = null;
		this.display = null;
		this.disposed = true;
	}
	
	
}
