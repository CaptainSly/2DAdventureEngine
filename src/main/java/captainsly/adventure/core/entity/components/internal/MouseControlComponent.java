package captainsly.adventure.core.entity.components.internal;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.components.Component;
import captainsly.adventure.core.input.MouseListener;
import captainsly.adventure.utils.Settings;

/**
*
* 	This is an internal component used by the engine, not to be used by the user
*  
*/
public class MouseControlComponent extends Component {

	// TODO: Implement Brushes to draw map components. 
	
	private GameObject holdingObject = null;

	public void pickupObject(GameObject gObj) {
		holdingObject = gObj;
		Adventure.currentScene.addGameObjectToScene(gObj);
	}

	public void placeObject() {
		holdingObject = null;
	}

	@Override
	public void update(double delta) {
		if (holdingObject != null) {
			holdingObject.getObjectPosition().set(new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY()));

			holdingObject.getObjectPosition().x = (int) (holdingObject.getObjectPosition().x / Settings.GRID_SIZE) * Settings.GRID_SIZE;
			holdingObject.getObjectPosition().y = (int) (holdingObject.getObjectPosition().y / Settings.GRID_SIZE) * Settings.GRID_SIZE;
			
			if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
				placeObject();
			}
		}
	}

}
