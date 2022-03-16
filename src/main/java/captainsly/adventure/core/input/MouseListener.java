package captainsly.adventure.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.joml.Vector2d;
import org.joml.Vector4f;

import captainsly.adventure.Adventure;

public class MouseListener {

	private static MouseListener instance;

	private Vector2d scrollOffset;
	private Vector2d mousePosition;
	private Vector2d mouseOrthoPosition;
	private Vector2d lastPosition;

	private boolean isDragging;

	private boolean[] mouseDown = new boolean[GLFW_MOUSE_BUTTON_LAST];

	public MouseListener() {
		scrollOffset = new Vector2d().zero();
		mousePosition = new Vector2d().zero();
		mouseOrthoPosition = new Vector2d().zero();
		lastPosition = new Vector2d().zero();

		isDragging = false;

	}

	public static void update() {

		for (boolean drag : getInstance().mouseDown)
			getInstance().isDragging |= drag;

		// X-Ortho
		float currentX = (float) getInstance().mousePosition.x;
		currentX = (currentX / (float) Adventure.window.getWindowWidth()) * 2.0f - 1.0f;
		Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
		tmp.mul(Adventure.currentScene.getSceneCamera().getInverseProjectionMatrix()
				.mul(Adventure.currentScene.getSceneCamera().getInverseViewMatrix()));
		currentX = tmp.x;

		// Y-Ortho
		float currentY = (float) (Adventure.window.getWindowHeight() - getInstance().mousePosition.y);
		currentY = (currentY / (float) Adventure.window.getWindowHeight()) * 2.0f - 1.0f;
		tmp = new Vector4f(0, currentY, 0, 1);
		tmp.mul(Adventure.currentScene.getSceneCamera().getInverseProjectionMatrix()
				.mul(Adventure.currentScene.getSceneCamera().getInverseViewMatrix()));
		currentY = tmp.y;

		getInstance().mouseOrthoPosition.x = currentX;
		getInstance().mouseOrthoPosition.y = currentY;
	}

	public static void mousePosCallback(long windowPointer, double xPos, double yPos) {
		getInstance().lastPosition.set(getInstance().mousePosition);

		getInstance().mousePosition.set(xPos, yPos);
	}

	public static void mouseButtonCallback(long windowPointer, int button, int action, int mods) {
		if (action == GLFW_PRESS) {
			getInstance().mouseDown[button] = true;
		} else if (action == GLFW_RELEASE) {
			getInstance().mouseDown[button] = false;
			getInstance().isDragging = false;
		}
	}

	public static void mouseScrollCallback(long windowPointer, double xOffset, double yOffset) {
		getInstance().scrollOffset.set(xOffset, yOffset);
	}

	public static boolean isButtonDown(int button) {
		return getInstance().mouseDown[button];
	}

	public static Vector2d getScrollOffset() {
		return getInstance().scrollOffset;
	}

	public static Vector2d getMousePosition() {
		return getInstance().mousePosition;
	}

	public static Vector2d getMouseOrthoPosition() {
		return getInstance().mouseOrthoPosition;
	}

	public static Vector2d getLastPosition() {
		return getInstance().lastPosition;
	}

	public static boolean isDragging() {
		return getInstance().isDragging;
	}

	public static boolean[] getMouseDown() {
		return getInstance().mouseDown;
	}

	public static MouseListener getInstance() {
		if (instance == null)
			instance = new MouseListener();

		return instance;
	}

}
