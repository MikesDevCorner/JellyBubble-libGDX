package com.xsheetgames.bloodstream.gameObjects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


public class BoundaryCollection {

	private Array<Boundary> boundaries;
	private ShapeRenderer sr;
	private float boundaryThickness = 15f;
	
	public BoundaryCollection(World world, float arenaSizeX, float arenasizeY) {
		boundaries = new Array<Boundary>(4);		
		boundaries.add(new Boundary(world, arenaSizeX * -0.5f-boundaryThickness, arenasizeY * -0.5f - boundaryThickness, arenaSizeX + 2 * boundaryThickness, boundaryThickness, "bottom"));
		boundaries.add(new Boundary(world, arenaSizeX * -0.5f-boundaryThickness, arenasizeY * 0.5f, arenaSizeX + 2 * boundaryThickness, boundaryThickness, "top"));
		boundaries.add(new Boundary(world, arenaSizeX * 0.5f, arenasizeY * -0.5f, boundaryThickness, arenasizeY, "right"));
		boundaries.add(new Boundary(world, arenaSizeX * -0.5f - boundaryThickness, arenasizeY * -0.5f, boundaryThickness, arenasizeY, "left"));
		sr = new ShapeRenderer();
	}
	
	
	public void draw(OrthographicCamera camera) {
		sr.setProjectionMatrix(camera.combined);
		sr.begin(ShapeType.Filled);
		sr.setColor(93/256f, 2/256f, 8/256f, 0f);
		for(Boundary b:boundaries) {
			sr.rect(b.x, b.y, b.width, b.height);
		}
		sr.end();
	}
	
	
	public void dispose() {
		for(Boundary b:boundaries) {
			b.dispose();
		}
		boundaries = null;
		sr.dispose();
		sr = null;
	}
	
}
