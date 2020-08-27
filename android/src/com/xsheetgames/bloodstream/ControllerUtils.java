package com.xsheetgames.bloodstream;

import android.os.Handler;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.controllers.mappings.Xbox;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.xsheetgames.bloodstream.screens.AbstractScreen;

public class ControllerUtils  implements InputProcessor, com.badlogic.gdx.controllers.ControllerListener {

	InputMultiplexer multiplexer = null;
	Game mGame = null;
	
	public ControllerUtils(Game game) {
		this.mGame = game;
	}
	
	public void initializeControllers(AndroidApplication context, Handler handler) {

	    // LIBGDX CONTROLLER INIT
        Controllers.addListener(this);
        
        // KEYBOARD INIT	    
		multiplexer = new InputMultiplexer();
	    multiplexer.addProcessor(this);	
	    Gdx.input.setInputProcessor(multiplexer);
	    Gdx.input.setCatchBackKey(true);
	}
	
	public void destroy() {

	}
	
	public void pause() {

	}
	
	public void resume() {
		if(multiplexer != null) 
		{
			Gdx.input.setInputProcessor(this.multiplexer);
		    Gdx.input.setCatchBackKey(true);
		} else {
			multiplexer = new InputMultiplexer();
		    multiplexer.addProcessor(this);	
		    Gdx.input.setInputProcessor(multiplexer);
		    Gdx.input.setCatchBackKey(true);
		}
	}
	
	

	
	/****** CONTROLLER ACTIONS *************************************************************************/
	
	
	
	/******* CONTROLLER POLLING *******************************/
	
	public boolean pollControllerButtonState(int keycode) {
		
		//POLL OUYA AND HID CONTROLLS
		for(com.badlogic.gdx.controllers.Controller controller: Controllers.getControllers()) {			
			if(controller.getName().equals(Ouya.ID)) {	
				//OUYA
				if(keycode == GameAssets.KEY_PRIMARY) {
					return (controller.getButton(Ouya.BUTTON_O) || controller.getButton(Ouya.BUTTON_R1));
				}
				if(keycode == GameAssets.KEY_SECONDARY) {
					return (controller.getButton(Ouya.BUTTON_U) || controller.getButton(Ouya.BUTTON_L1));
				}
				if(keycode == GameAssets.KEY_START) {
					return controller.getButton(Ouya.BUTTON_MENU);
				}
				if(keycode == GameAssets.KEY_UP) {
					return controller.getButton(Ouya.BUTTON_DPAD_UP);
				}
				if(keycode == GameAssets.KEY_DOWN) {
					return controller.getButton(Ouya.BUTTON_DPAD_DOWN);
				}
				if(keycode == GameAssets.KEY_LEFT) {
					return controller.getButton(Ouya.BUTTON_DPAD_LEFT);
				}
				if(keycode == GameAssets.KEY_RIGHT) {
					return controller.getButton(Ouya.BUTTON_DPAD_RIGHT);
				}				
			} else {
				//HID
				if(keycode == GameAssets.KEY_PRIMARY) {
					return (controller.getButton(Keys.BUTTON_A) || controller.getButton(Keys.BUTTON_R1));
				}
				if(keycode == GameAssets.KEY_SECONDARY) {
					return (controller.getButton(Keys.BUTTON_X) || controller.getButton(Keys.BUTTON_L1));
				}
				if(keycode == GameAssets.KEY_START) {
					return controller.getButton(Keys.BUTTON_START);
				}
				if(keycode == GameAssets.KEY_UP) {
					return controller.getButton(Keys.DPAD_UP);
				}
				if(keycode == GameAssets.KEY_DOWN) {
					return controller.getButton(Keys.DPAD_DOWN);
				}
				if(keycode == GameAssets.KEY_LEFT) {
					return controller.getButton(Keys.DPAD_LEFT);
				}
				if(keycode == GameAssets.KEY_RIGHT) {
					return controller.getButton(Keys.DPAD_RIGHT);
				}
			}
		}		
		
		
		//POLL KEYBOARD CONTROLLS
		if(keycode == GameAssets.KEY_PRIMARY) {
			return Gdx.input.isKeyPressed(Keys.UP); //UP, WEIL DIE KEYBOARD-STEUERUNG BEIM POLLEN MIT UP FUNKTIONIERT! VORISCHT!
		}
		if(keycode == GameAssets.KEY_SECONDARY) {
			return Gdx.input.isKeyPressed(Keys.SPACE);
		}
		if(keycode == GameAssets.KEY_START) {
			return Gdx.input.isKeyPressed(Keys.P);
		}
		if(keycode == GameAssets.KEY_UP) {
			return Gdx.input.isKeyPressed(Keys.UP);
		}
		if(keycode == GameAssets.KEY_DOWN) {
			return Gdx.input.isKeyPressed(Keys.DOWN);
		}
		if(keycode == GameAssets.KEY_LEFT) {
			return Gdx.input.isKeyPressed(Keys.LEFT);
		}
		if(keycode == GameAssets.KEY_RIGHT) {
			return Gdx.input.isKeyPressed(Keys.RIGHT);
		}
		
		
		return false;		
	}

	
	public float pollControllerAxis(int axis) {
		
		//OUYA UND HID AXIS
		for(com.badlogic.gdx.controllers.Controller controller: Controllers.getControllers()) {			
			if(controller.getName().equals(Ouya.ID)) {	
				//OUYA
				if(axis == GameAssets.AXIS_X) {
					float retVal = controller.getAxis(Ouya.AXIS_LEFT_X); 
					if(Math.abs(retVal) > 0.05f) return retVal;
					else return 0.0f;
				}
				if(axis == GameAssets.AXIS_Y) {
					float retVal = controller.getAxis(Ouya.AXIS_LEFT_Y); 
					if(Math.abs(retVal) > 0.05f) return retVal;
					else return 0.0f;
				}
			} else {
				//HID
				if(axis == GameAssets.AXIS_X) {
					float retVal = controller.getAxis(Xbox.L_STICK_HORIZONTAL_AXIS);
					if(Math.abs(retVal) > 0.05f) return retVal;
					else retVal = 0.0f;
					
					if(retVal == 0.0f) {
						retVal = controller.getAxis(6); //HID DPAD XAXIS 
						if(Math.abs(retVal) > 0.05f) return retVal;
						else return 0.0f;
					}
					
				}
				if(axis == GameAssets.AXIS_Y) {
					float retVal = controller.getAxis(Xbox.L_STICK_VERTICAL_AXIS);
					if(Math.abs(retVal) > 0.05f) return retVal;
					else retVal = 0.0f;
					
					if(retVal == 0.0f) {
						retVal = controller.getAxis(7); //HID DPAD YAXIS 
						if(Math.abs(retVal) > 0.05f) return retVal;
						else return 0.0f;
					}
				}
			}
		}
		
		//NEIGUNG DES DEVICES
		if(axis == GameAssets.AXIS_X) {
			float retVal = Gdx.input.getAccelerometerY() / 10.0f; 
			if(Math.abs(retVal) > 0.05f) return retVal;
			else return 0.0f;
		}
		if(axis == GameAssets.AXIS_Y) {
			float retVal = Gdx.input.getAccelerometerZ();
			if(Math.abs(retVal) > 0.05f) return retVal;
			else return 0.0f;
		}
		
		return 0.0f;
	}
	
		
	
	
	
	
	/****** INPUT PROCESSOR HANDLERS ********************************/
	
	/*************** KEYBOARD HANDLERS *******************/
	@Override
	public boolean keyDown(int keycode) {
		AbstractScreen screen = (AbstractScreen) this.mGame.getScreen();
		
		//PRIMARY KEY
		if(keycode == Keys.SPACE) {
			screen.primaryPress();
			return true;
		}
		
		//SECONDARY KEY
		if(keycode == Keys.ALT_LEFT || keycode == Keys.ALT_RIGHT) {
			screen.secondaryPress();
			return true;
		}
		
		//START KEY
		if(keycode == Keys.P) {
			screen.startPress();
			return true;
		}
		
		//Escape KEY
		if(keycode == Keys.ESCAPE) {
			screen.stepBack("keyboard");
			return true;
		}
		
		//BACK KEY (Handy)
		if(keycode == Keys.BACK) {
			screen.stepBack("touch");
			return true;
		}
		
		
		
		//UP KEY
		if(keycode == Keys.UP) {
			screen.steerYAxis(1.0f);
			return true;
		}
		
		//DOWN KEY
		if(keycode == Keys.DOWN) {
			screen.steerYAxis(-1.0f);
			return true;
		}
		
		//LEFT KEY
		if(keycode == Keys.LEFT) {
			screen.steerXAxis(-1.0f);
			return true;
		}
		
		//RIGHT KEY
		if(keycode == Keys.RIGHT) {
			screen.steerXAxis(1.0f);
			return true;
		}
		return false;
	}



	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	
	
	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		Vector2 touchPoint = new Vector2(x*Configuration.TARGET_WIDTH/Gdx.graphics.getWidth(), Configuration.TARGET_HEIGHT - (y*Configuration.TARGET_HEIGHT/Gdx.graphics.getHeight()));
		AbstractScreen screen = (AbstractScreen) this.mGame.getScreen();
		return screen.screenTouched(touchPoint, pointer);
	}
	



	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}	


	@Override
	public boolean keyTyped(char character) {
		return false;
	}



	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}



	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}



	@Override
	public boolean scrolled(int amount) {
		return false;
	}


	
	
	/******** LIBGDX CONTROLLER HANDLERS (OUYA, HID, ...) **********/
	
	
	@Override
	public boolean buttonDown(com.badlogic.gdx.controllers.Controller controller, int keycode) {
		
		AbstractScreen screen = (AbstractScreen) this.mGame.getScreen();
		if(controller.getName().equals(Ouya.ID)) {			
			if(keycode == Ouya.BUTTON_O) {
				screen.primaryPress();
				return true;
			}
			if(keycode == Ouya.BUTTON_U) {
				screen.secondaryPress();
				return true;
			}
			if(keycode == Ouya.BUTTON_A) {
				screen.stepBack("controller");
				return true;
			}
			if(keycode == Ouya.BUTTON_MENU) {
				screen.startPress();
				return true;
			}
			if(keycode == Ouya.BUTTON_L1) {
				screen.secondaryPress();
				return true;
			}
			if(keycode == Ouya.BUTTON_R1) {
				screen.primaryPress();
				return true;
			}
			if(keycode == Ouya.BUTTON_DPAD_UP) {
				screen.steerYAxis(1.0f);
				return true;
			}
			if(keycode == Ouya.BUTTON_DPAD_DOWN) {
				screen.steerYAxis(-1.0f);
				return true;
			}
			if(keycode == Ouya.BUTTON_DPAD_LEFT) {
				screen.steerXAxis(-1.0f);
				return true;
			}
			if(keycode == Ouya.BUTTON_DPAD_RIGHT) {
				screen.steerXAxis(1.0f);
				return true;
			}
		} else {
			if(keycode == Keys.BUTTON_A) {
				screen.primaryPress();
				return true;
			}
			if(keycode == Keys.BUTTON_X) {
				screen.secondaryPress();
				return true;
			}
			if(keycode == Keys.BUTTON_B) {
				screen.stepBack("controller");
				return true;
			}
			if(keycode == Keys.BUTTON_START) {
				screen.startPress();
				return true;
			}
			if(keycode == Keys.BUTTON_L1) {
				screen.secondaryPress();
				return true;
			}
			if(keycode == Keys.BUTTON_R1) {
				screen.primaryPress();
				return true;
			}
			if(keycode == Keys.DPAD_UP) {
				screen.steerYAxis(1.0f);
				return true;
			}
			if(keycode == Keys.DPAD_DOWN) {
				screen.steerYAxis(-1.0f);
				return true;
			}
			if(keycode == Keys.DPAD_LEFT) {
				screen.steerXAxis(-1.0f);
				return true;
			}
			if(keycode == Keys.DPAD_RIGHT) {
				screen.steerXAxis(1.0f);
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public boolean axisMoved(com.badlogic.gdx.controllers.Controller controller, int axis, float peculiarity) {
		
		AbstractScreen screen = (AbstractScreen) this.mGame.getScreen();
		if(controller.getName().equals(Ouya.ID)) {
			if(axis == Ouya.AXIS_LEFT_X) {
				screen.steerXAxis(peculiarity);
				return true;
			}
			if(axis == Ouya.AXIS_LEFT_Y) {
				screen.steerYAxis(peculiarity);
				return true;
			}
		} else {
			if(axis == Xbox.L_STICK_HORIZONTAL_AXIS || axis == 6) {
				screen.steerXAxis(peculiarity);
				return true;
			}
			if(axis == Xbox.L_STICK_VERTICAL_AXIS || axis == 7) {
				screen.steerYAxis(peculiarity);
				return true;
			}
		}		
		return false;
	}
	
	@Override
	public boolean buttonUp(com.badlogic.gdx.controllers.Controller arg0,
			int arg1) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean accelerometerMoved(
			com.badlogic.gdx.controllers.Controller arg0, int arg1, Vector3 arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void connected(com.badlogic.gdx.controllers.Controller arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnected(com.badlogic.gdx.controllers.Controller arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean povMoved(com.badlogic.gdx.controllers.Controller arg0,
			int arg1, PovDirection arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean xSliderMoved(com.badlogic.gdx.controllers.Controller arg0,
			int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ySliderMoved(com.badlogic.gdx.controllers.Controller arg0,
			int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	


}
