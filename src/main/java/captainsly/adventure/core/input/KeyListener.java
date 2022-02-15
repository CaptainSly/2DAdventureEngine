package captainsly.adventure.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

	private static KeyListener instance;
	private boolean[] keyDown = new boolean[GLFW_KEY_LAST + 2];

	private KeyListener() {
	}

	public static void keyCallback(long windowPointer, int key, int scanCode, int action, int modifiers) {
		if (action == GLFW_PRESS)
			getInstance().keyDown[key] = true;
		else if (action == GLFW_RELEASE) {
			getInstance().keyDown[key] = false;
		}
	}

	public static boolean isKeyDown(int keyCode) {
		return getInstance().keyDown[keyCode];
	}

	public static KeyListener getInstance() {
		if (instance == null)
			instance = new KeyListener();

		return KeyListener.instance;
	}

}
