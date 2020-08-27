package com.xsheetgames.bloodstream.elements;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.xsheetgames.bloodstream.gameObjects.Dropzone;
import com.xsheetgames.bloodstream.gameObjects.EnemyAbstract;
import com.xsheetgames.bloodstream.gameObjects.GameObject;
import com.xsheetgames.bloodstream.gameObjects.Jelly;
import com.xsheetgames.bloodstream.gameObjects.JellyDecorator;
import com.xsheetgames.bloodstream.gameObjects.Obstacle;
import com.xsheetgames.bloodstream.gameObjects.Oxygen;
import com.xsheetgames.bloodstream.gameObjects.PowerupAbstract;
import com.xsheetgames.bloodstream.gameObjects.PowerupDash;
import com.xsheetgames.bloodstream.gameObjects.PowerupEnergy;
import com.xsheetgames.bloodstream.gameObjects.PowerupGhost;
import com.xsheetgames.bloodstream.gameObjects.PowerupPunch;

public class GameContactListener  implements ContactListener{

	@Override
	public void beginContact(Contact contact) {
		try {
			Body bodyA = contact.getFixtureA().getBody();
			Body bodyB = contact.getFixtureB().getBody();
			if(bodyA != null && bodyB != null) {
				if(bodyA.getUserData() != null && bodyB.getUserData() != null) {
					Fixture a = contact.getFixtureA();
					Fixture b = contact.getFixtureB();
					
					this.checkStartCounting(a, b, contact);
					this.checkStartCounting(b, a, contact);
					
					this.checkCreateBubbleJoint(a,  b, contact);
					this.checkCreateBubbleJoint(b, a, contact);
					
					this.checkJellyHurt(a, b, contact);
					this.checkJellyHurt(b, a, contact);
					
					this.checkEnemyHittedSomething(a, b, contact);
					this.checkEnemyHittedSomething(b, a, contact);
					
					this.checkPowerups(a, b, contact);
					this.checkPowerups(b, a, contact);
				}
			}
		} catch(Exception e) {}
	}

	@Override
	public void endContact(Contact contact) {
		try {
			Body bodyA = contact.getFixtureA().getBody();
			Body bodyB = contact.getFixtureB().getBody();
			if(bodyA != null && bodyB != null) {
				if(bodyA.getUserData() != null && bodyB.getUserData() != null) {
					Fixture a = contact.getFixtureA();
					Fixture b = contact.getFixtureB();		
					
					this.checkEndCounting(a, b, contact);
					this.checkEndCounting(b, a, contact);
				}
			}
		} catch(Exception e) {}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		try {
			Body bodyA = contact.getFixtureA().getBody();
			Body bodyB = contact.getFixtureB().getBody();
			if(bodyA != null && bodyB != null) {
				if(bodyA.getUserData() != null && bodyB.getUserData() != null) {
					Fixture a = contact.getFixtureA();
					Fixture b = contact.getFixtureB();		
					
					this.checkJellyBubbleContact(a, b, contact);
					this.checkJellyBubbleContact(b, a, contact);
					
					this.checkObstacleGhostSlipping(a, b, contact);
					this.checkObstacleGhostSlipping(b, a, contact);
				}
			}
		} catch(Exception e) {}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	/**************************************************************************************/
	
	
	private void checkStartCounting(Fixture a, Fixture b, Contact contact) {
		GameObject ag = ((iContactableGameObject) a.getBody().getUserData()).getGameObject();
		GameObject bg = ((iContactableGameObject) b.getBody().getUserData()).getGameObject();
		
		if(ag instanceof Dropzone) {
			if(bg instanceof Oxygen) {
				Oxygen x = ((Oxygen)bg);			
				x.startCounting();
			}
		}
	}
	
	
	private void checkEndCounting(Fixture a, Fixture b, Contact contact) {
		GameObject ag = ((iContactableGameObject) a.getBody().getUserData()).getGameObject();
		GameObject bg = ((iContactableGameObject) b.getBody().getUserData()).getGameObject();
		
		if(ag instanceof Dropzone) {
			if(bg instanceof Oxygen) {
				Oxygen x = ((Oxygen)bg);
				x.resetCounting();
			}
		}
	}
	
	private void checkEnemyHittedSomething(Fixture a, Fixture b, Contact contact) {
		GameObject ag = ((iContactableGameObject) a.getBody().getUserData()).getGameObject();
		GameObject bg = ((iContactableGameObject) b.getBody().getUserData()).getGameObject();
		
		if(ag instanceof EnemyAbstract) {
			if(bg instanceof Obstacle) {
				//((Enemy) ag).changeDirection();
			}
			if(bg instanceof Jelly) {
				((EnemyAbstract) ag).oppositeDirection();
			}
		}
	}
	
	
	private void checkCreateBubbleJoint(Fixture a, Fixture b, Contact contact) {
		GameObject ag = ((iContactableGameObject) a.getBody().getUserData()).getGameObject();
		GameObject bg = ((iContactableGameObject) b.getBody().getUserData()).getGameObject();
		
		if(((iContactableGameObject) a.getBody().getUserData()) instanceof JellyDecorator) {
			if(bg instanceof Oxygen) {
				Jelly j = (Jelly) ag;
				j.createBubbleJoint((Oxygen)bg);
			}
		}
	}
	
	private void checkJellyHurt(Fixture a, Fixture b, Contact contact) {
		GameObject ag = ((iContactableGameObject) a.getBody().getUserData()).getGameObject();
		GameObject bg = ((iContactableGameObject) b.getBody().getUserData()).getGameObject();
		
		if(ag instanceof Jelly) {
			if(bg instanceof EnemyAbstract) {
				Jelly j = (Jelly) ag;
				j.reduceEnergy();
			}
		}
	}
	
	
	private void checkPowerups(Fixture a, Fixture b, Contact contact) {
		GameObject ag = ((iContactableGameObject) a.getBody().getUserData()).getGameObject();
		GameObject bg = ((iContactableGameObject) b.getBody().getUserData()).getGameObject();
		
		if(ag instanceof Jelly) {
			Jelly j = (Jelly) ag;
			
			if(bg instanceof PowerupAbstract) {
				if(((PowerupAbstract) bg).getConsumed() == false) {
					if(bg instanceof PowerupDash) {
						if(j.getDashMode() == false) {
							j.setDashMode(true);
							((PowerupAbstract) bg).setConsumed(true);
						}
					}
					if(bg instanceof PowerupGhost) {
						if(j.getGhostMode() == false) {
							j.setGhostMode(true);
							((PowerupAbstract) bg).setConsumed(true);
						}
					}
					if(bg instanceof PowerupPunch) {
						if(j.getPunchMode() == false) {
							j.setPunchMode(true);
							((PowerupAbstract) bg).setConsumed(true);
						}
					}
					if(bg instanceof PowerupEnergy) {
						j.incrementEnergy();
						((PowerupAbstract) bg).setConsumed(true);
					}
				}
			}
		}
	}
	
	
	private void checkJellyBubbleContact(Fixture a, Fixture b, Contact contact) {
		GameObject ag = ((iContactableGameObject) a.getBody().getUserData()).getGameObject();
		GameObject bg = ((iContactableGameObject) b.getBody().getUserData()).getGameObject();
		
		if(ag instanceof Jelly) {
			if(bg instanceof Oxygen) {
				if(((Oxygen) bg).startCounting) {
					contact.setEnabled(false);
				}
			}
		}
	}
	
	private void checkObstacleGhostSlipping(Fixture a, Fixture b, Contact contact) {
		GameObject ag = ((iContactableGameObject) a.getBody().getUserData()).getGameObject();
		GameObject bg = ((iContactableGameObject) b.getBody().getUserData()).getGameObject();
		
		if(bg instanceof iGhostModeAble) {
			if(ag instanceof Obstacle) {
				if(((iGhostModeAble) bg).getGhostMode()) {
					if(((Obstacle)ag).getGhostLeakAble()) {
						contact.setEnabled(false);
					}
				}
			}
		}
	}

}
