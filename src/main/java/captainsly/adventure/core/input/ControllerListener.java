package captainsly.adventure.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_LAST;
import static org.lwjgl.glfw.GLFW.glfwGetGamepadState;

import org.lwjgl.glfw.GLFWGamepadState;

import captainsly.adventure.Adventure;

public class ControllerListener {

	private static ControllerListener instance;

	private boolean[] connectedControllers;
	private GLFWGamepadState[] glfwGamepadStates;

	private ControllerListener() {
		connectedControllers = new boolean[GLFW_JOYSTICK_LAST];
		glfwGamepadStates = new GLFWGamepadState[connectedControllers.length];
	}

	public static void controllerCallback(int jid, int event) {
		if (event == GLFW_CONNECTED) {
			Adventure.log.info("Connecting controller: " + jid);
			getInstance().connectedControllers[jid] = true;
			GLFWGamepadState state = GLFWGamepadState.create();
			getInstance().glfwGamepadStates[jid] = state;

		} else if (event == GLFW_DISCONNECTED) {
			Adventure.log.info("Disconnecting controller: " + jid);
			getInstance().connectedControllers[jid] = false;
			getInstance().glfwGamepadStates[jid].free();
		}
	}

	public static boolean isButtonDown(int jid, int button) {
		boolean buttonDown = false;

		GLFWGamepadState state = getInstance().glfwGamepadStates[jid];

		if (glfwGetGamepadState(jid, state))
			buttonDown = state.buttons(button) == 1 ? true : false;

		return buttonDown;
	}

	public static float getAxis(int jid, int axis) {
		float axisValue = 0.0f;

		GLFWGamepadState state = getInstance().glfwGamepadStates[jid];

		if (glfwGetGamepadState(jid, state)) {
			axisValue = state.axes(axis);
		}

		return axisValue;
	}

	public static ControllerListener getInstance() {
		if (instance == null)
			instance = new ControllerListener();

		return instance;
	}

}
