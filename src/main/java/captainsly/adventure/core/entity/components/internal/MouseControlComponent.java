package captainsly.adventure.core.entity.components.internal;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

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

	GameObject holdingObject = null;

	@Override
	public void start() {
	}

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
			holdingObject.getObjectTransform().position.set(MouseListener.getMouseOrthoPosition());

			holdingObject.getObjectTransform().position.x = (int) (holdingObject.getObjectTransform().position.x / Settings.GRID_SIZE) * Settings.GRID_SIZE;
			holdingObject.getObjectTransform().position.y = (int) (holdingObject.getObjectTransform().position.y / Settings.GRID_SIZE) * Settings.GRID_SIZE;
			
			if (MouseListener.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
				placeObject();
			}
		}
	}

}
