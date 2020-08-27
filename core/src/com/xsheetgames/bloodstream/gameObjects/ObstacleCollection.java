package com.xsheetgames.bloodstream.gameObjects;

public class ObstacleCollection extends GameObjectCollection {

	private boolean isOpaque = false;
	
	public ObstacleCollection(Class<?> myClass, GameObjectPool pool) {
		super(myClass, pool);
	}
	
	public ObstacleCollection(int size) {
		super(size);
	}
	
	
	public void setOpaque(boolean opaque) {
		this.isOpaque = opaque;
		for(GameObject d : objects) {
			Obstacle o = (Obstacle) d;
			if(o.getGhostLeakAble()) o.setOpaque(opaque);
		}
	}
	
	public boolean isOpaque() {
		return this.isOpaque;
	}
	
	
	public GameObject spawnPooledGameObject(String myClass, float x, float y, boolean ghostLeakAble) {
		return this.spawnPooledGameObject(myClass, x, y, 0f, 0f, 0f, ghostLeakAble);
	}
	
	public GameObject spawnPooledGameObject(String myClass, float x, float y, float angle, boolean ghostLeakAble) {
		return this.spawnPooledGameObject(myClass, x, y, angle, 0f, 0f, ghostLeakAble);
	}
	
	@Override
	public GameObject spawnPooledGameObject(String myClass, float x, float y, float angle, float velocityX, float velocityY) {
		return this.spawnPooledGameObject(myClass, x, y, angle, velocityX, velocityY, false);
	}
	
	public GameObject spawnPooledGameObject(String myClass, float x, float y, float angle, float velocityX, float velocityY, boolean ghostLeakAble) {
		Obstacle o = (Obstacle) this.getPool(myClass).obtain();
		o.setPosition(x, y, angle);
		o.setVelocity(velocityX, velocityY);
		o.setGhostLeakAble(ghostLeakAble);
		o.setPool(this.getPool(myClass));
		this.add(o);
		return o;
	}
	
	
	public GameObject spawnPooledGameObject(Class<?> myClass, float x, float y, boolean ghostLeakAble) {
		return this.spawnPooledGameObject(myClass, x, y, 0f, 0f, 0f, ghostLeakAble);
	}
	
	public GameObject spawnPooledGameObject(Class<?> myClass, float x, float y, float angle, boolean ghostLeakAble) {
		return this.spawnPooledGameObject(myClass, x, y, angle, 0f, 0f, ghostLeakAble);
	}
	
	@Override
	public GameObject spawnPooledGameObject(Class<?> myClass, float x, float y, float angle, float velocityX, float velocityY) {
		return this.spawnPooledGameObject(myClass, x, y, angle, velocityX, velocityY, false);
	}	
	
	public GameObject spawnPooledGameObject(Class<?> myClass, float x, float y, float angle, float velocityX, float velocityY, boolean ghostLeakAble) {
		Obstacle o = (Obstacle) this.getPool(myClass).obtain();
		o.setPosition(x, y, angle);
		o.setVelocity(velocityX, velocityY);
		o.setGhostLeakAble(ghostLeakAble);
		o.setPool(this.getPool(myClass));
		this.add(o);
		return o;
	}
	
	

}
