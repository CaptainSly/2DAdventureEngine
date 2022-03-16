package captainsly.adventure.core.render;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public class Window {

	private String windowTitle;
	private int windowWidth, windowHeight;
	private IntBuffer windowWidthBuffer, windowHeightBuffer;
	private boolean isResized = false;
	private final long windowPointer;

	public Window(String windowTitle, int windowWidth, int windowHeight) {
		windowWidthBuffer = BufferUtils.createIntBuffer(1);
		windowHeightBuffer = BufferUtils.createIntBuffer(1);
		
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		windowPointer = glfwCreateWindow(windowWidth, windowHeight, windowTitle, MemoryUtil.NULL, MemoryUtil.NULL);

		glfwGetWindowSize(windowPointer, windowWidthBuffer, windowHeightBuffer);

		if (windowPointer == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create GLFW Window");

	}

	public String getWindowTitle() {
		return windowTitle;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
		glfwSetWindowTitle(windowPointer, windowTitle);
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public IntBuffer getWindowWidthBuffer() {
		return windowWidthBuffer;
	}

	public IntBuffer getWindowHeightBuffer() {
		return windowHeightBuffer;
	}

	public boolean isResized() {
		return isResized;
	}

	public void setIsResized(boolean isResized) {
		this.isResized = isResized;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

	public long getWindowPointer() {
		return windowPointer;
	}

}
