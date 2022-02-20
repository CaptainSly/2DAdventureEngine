package captainsly.adventure.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.joml.Vector2d;

public class MouseListener {

	private static MouseListener instance;

	private Vector2d scrollOffset;
	private Vector2d mousePosition;
	private Vector2d lastPosition;

	private boolean isDragging;

	private boolean[] mouseDown = new boolean[GLFW_MOUSE_BUTTON_LAST];

	public MouseListener() {
		scrollOffset = new Vector2d().zero();
		mousePosition = new Vector2d().zero();
		lastPosition = new Vector2d().zero();

		isDragging = false;

	}

	public static void update() {

		for (boolean drag : getInstance().mouseDown)
			getInstance().isDragging |= drag;
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
