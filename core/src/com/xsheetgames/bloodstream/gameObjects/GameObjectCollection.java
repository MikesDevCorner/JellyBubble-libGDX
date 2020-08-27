package com.xsheetgames.bloodstream.gameObjects;

import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;


public class GameObjectCollection implements Iterator<GameObject> {
	
	protected Array<GameObject> objects;
	private ObjectMap<String,GameObjectPool> pools;
	public int length;
	
	public GameObjectCollection(int size) {
		this.length = 0;
		objects = new Array<GameObject>(false, size);
		this.pools = new ObjectMap<String,GameObjectPool>();
	}
	
	public GameObjectCollection(Class<?> myClass, GameObjectPool pool) {
		this.length = 0;
		objects = new Array<GameObject>(false, pool.initialCapacity);
		this.pools = new ObjectMap<String,GameObjectPool>();
		this.pools.put(myClass.getName(), pool);
		this.preFillPools();
	}	
	
	public void smoothStates(float fixedTimestepAccumulatorRatio) {
		for(GameObject d : objects) {
			d.smoothStates(fixedTimestepAccumulatorRatio);
		}
	}
	
	public void resetSmoothStates() {
		for(GameObject d : objects) {
			d.resetSmoothStates();
		}
	}
	
	
	protected void add(GameObject a) {
		objects.add(a);
		this.length++;
	}
	
	public void invokeObjectLogic(float delta) {
		for(GameObject d : objects) {
			d.doMotionLogic(delta);
			if(d.isResetted() == true || d.isDisposed() == true || d.setDisposing == true) {
				this.objects.removeValue(d, true);
				this.length--;
			}	
		}
	}
	
	public void drawObjects(SpriteBatch batch, float delta) {
		for(GameObject d : objects) {
			d.draw(batch, delta);
		}
	}


	@Override
	public boolean hasNext() {
		return objects.iterator().hasNext();
	}


	@Override
	public GameObject next() {
		return objects.iterator().next();
	}


	@Override
	public void remove() {
		objects.iterator().remove();
		this.length--;
	}
	
	
	public GameObjectPool getPool(String poolname) {
		return this.pools.get(poolname);
	}
	
	
	public GameObjectPool getPool(Class<?> myClass) {
		return this.pools.get(myClass.getName());
	}
	
	public void addPool(String poolname, GameObjectPool pool) {
		this.pools.put(poolname, pool);
	}
	
	public void addPool(Class<?> myClass, GameObjectPool pool) {
		this.pools.put(myClass.getName(), pool);
	}
	
	
	public void preFillPools() {
		for(ObjectMap.Entry<String,GameObjectPool> a : this.pools.entries()) {
			Array<GameObject> objects = new Array<GameObject>();
			for(int i = 0; i < a.value.initialCapacity; i++)  //Pool prebefüllen 
			{
				GameObject o = (GameObject) a.value.obtain();
				o.setPool(a.value);
				objects.add(o);
			}
			for(GameObject o : objects) {
				a.value.free(o);
			}
			objects.clear();
			this.length = 0;
			objects = null;
		}
	}
	
	public void resetGraphics(TextureAtlas atlas) {
		for(ObjectMap.Entry<String,GameObjectPool> a : this.pools.entries()) {
			a.value.resetGraphics(atlas);
		}		
	}
	
	
	
	
	public GameObject spawnPooledGameObject(String myClass, float x, float y) {
		return this.spawnPooledGameObject(myClass, x, y, 0f);
	}
	
	public GameObject spawnPooledGameObject(String myClass, float x, float y, float angle) {
		return this.spawnPooledGameObject(myClass, x, y, angle, 0f, 0f);
	}
	
	public GameObject spawnPooledGameObject(String myClass, float x, float y, float velocityX, float velocityY) {
		return this.spawnPooledGameObject(myClass, x, y, 0f, velocityX, velocityY);
	}
	
	public GameObject spawnPooledGameObject(String myClass, float x, float y, float angle, float velocityX, float velocityY) {
		GameObject o = this.getPool(myClass).obtain();
		o.setPosition(x, y, angle);
		o.setVelocity(velocityX, velocityY);
		o.setPool(this.getPool(myClass));
		this.add(o);
		return o;
	}
	
	
	
	public GameObject spawnPooledGameObject(Class<?> myClass, float x, float y) {
		return this.spawnPooledGameObject(myClass, x, y, 0f);
	}
	
	public GameObject spawnPooledGameObject(Class<?> myClass, float x, float y, float angle) {
		return this.spawnPooledGameObject(myClass, x, y, angle, 0f, 0f);
	}
	
	public GameObject spawnPooledGameObject(Class<?> myClass, float x, float y, float velocityX, float velocityY) {
		return this.spawnPooledGameObject(myClass, x, y, 0f, velocityX, velocityY);
	}
	
	public GameObject spawnPooledGameObject(Class<?> myClass, float x, float y, float angle, float velocityX, float velocityY) {
		GameObject o = this.getPool(myClass).obtain();
		o.setPosition(x, y, angle);
		o.setVelocity(velocityX, velocityY);
		o.setPool(this.getPool(myClass));
		this.add(o);
		return o;
	}
	
	
	
	
	public void clear() {
		for(GameObject g:objects) {
			if(!g.free()) g.dispose();
		}
		this.objects.clear();
		this.length = 0;
	}
	
	public void dispose() {
		for(GameObject g:objects) {
			if(g != null) {
				if(!g.free()) g.dispose();
			}
		}
		this.length = 0;
		this.pools.clear();
		this.pools = null;
		this.objects.clear();
		this.objects = null;
	}	

}