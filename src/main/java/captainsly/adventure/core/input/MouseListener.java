package captainsly.adventure.core.input;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector4f;

import captainsly.adventure.Adventure;

public class MouseListener {

	private static MouseListener instance;

	private Vector2d scrollOffset;
	private Vector2d mousePosition;
	private Vector2d mouseOrthoPosition;
	private Vector2d lastPosition;
	private Vector2d mouseScreenPosition;

	// Editor Viewport Stuff
	private Vector2f gameViewportPos = new Vector2f();
	private Vector2f gameViewportSize = new Vector2f();

	private boolean isDragging;

	private boolean[] mouseDown = new boolean[GLFW_MOUSE_BUTTON_LAST];

	public MouseListener() {
		scrollOffset = new Vector2d().zero();
		mousePosition = new Vector2d().zero();
		mouseOrthoPosition = new Vector2d().zero();
		mouseScreenPosition = new Vector2d().zero();
		lastPosition = new Vector2d().zero();

		isDragging = false;

	}

	public static void update() {

		for (boolean drag : getInstance().mouseDown)
			getInstance().isDragging |= drag;

		calculateOrthoPosition();
		calculateScreenPosition();
	}

	private static void calculateScreenPosition() {
		// X-Ortho
		float currentX = (float) getInstance().mousePosition.x - getInstance().gameViewportPos.x;
		currentX = (currentX / (float) getInstance().gameViewportSize.x) * Adventure.window.getMonitorWidth();

		// Y-Ortho
		float currentY = (float) (getInstance().mousePosition.y - getInstance().gameViewportPos.y);
		currentY = Adventure.window.getMonitorHeight() - ((currentY / (float) getInstance().gameViewportSize.y) * Adventure.window.getMonitorHeight());

		getInstance().mouseScreenPosition.x = currentX;
		getInstance().mouseScreenPosition.y = currentY;
	}

	private static void calculateOrthoPosition() {
		// X-Ortho
		float currentX = (float) getInstance().mousePosition.x - getInstance().gameViewportPos.x;
		currentX = (currentX / (float) getInstance().gameViewportSize.x) * 2.0f - 1.0f;
		Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
		tmp.mul(Adventure.currentScene.getSceneCamera().getInverseProjectionMatrix()
				.mul(Adventure.currentScene.getSceneCamera().getInverseViewMatrix()));
		currentX = tmp.x;

		// Y-Ortho
		float currentY = (float) (getInstance().mousePosition.y - getInstance().gameViewportPos.y);
		currentY = -((currentY / (float) getInstance().gameViewportSize.y) * 2.0f - 1.0f);
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

	public static void setGameViewportPos(Vector2f gameViewportPos) {
		getInstance().gameViewportPos.set(gameViewportPos);
	}

	public static void setGameViewportSize(Vector2f gameViewportSize) {
		getInstance().gameViewportSize.set(gameViewportSize);
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

	public static Vector2d getMouseScreenPosition() {
		return getInstance().mouseScreenPosition;
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

	public static Vector2f getGameViewportPos() {
		return getInstance().gameViewportPos;
	}

	public static Vector2f getGameViewportSize() {
		return getInstance().gameViewportSize;
	}

	public static MouseListener getInstance() {
		if (instance == null)
			instance = new MouseListener();

		return instance;
	}

}
