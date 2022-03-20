package captainsly.adventure.core.render;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

public class Window {

	private String windowTitle;
	private int windowWidth, windowHeight;
	private int monitorWidth, monitorHeight;
	private IntBuffer windowWidthBuffer, windowHeightBuffer;
	private boolean isResized = false;
	private final long windowPointer;

	public Window(String windowTitle, int windowWidth, int windowHeight) {

		// Create Window
		windowWidthBuffer = BufferUtils.createIntBuffer(1);
		windowHeightBuffer = BufferUtils.createIntBuffer(1);

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

		windowPointer = glfwCreateWindow(windowWidth, windowHeight, windowTitle, MemoryUtil.NULL, MemoryUtil.NULL);

		glfwGetWindowSize(windowPointer, windowWidthBuffer, windowHeightBuffer);

		if (windowPointer == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create GLFW Window");

		this.windowWidth = windowWidthBuffer.get(0);
		this.windowHeight = windowHeightBuffer.get(0);

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

	public int getWindowHeight() {
		return windowHeight;
	}

	public float getAspectRatio() {
		return 16.0f / 9.0f;
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

	public long getWindowPointer() {
		return windowPointer;
	}

	public void setIsResized(boolean isResized) {
		this.isResized = isResized;
	}

	public int getMonitorWidth() {
		return monitorWidth;
	}

	public int getMonitorHeight() {
		return monitorHeight;
	}

	public void setMonitorWidth(int monitorWidth) {
		this.monitorWidth = monitorWidth;
	}

	public void setMonitorHeight(int monitorHeight) {
		this.monitorHeight = monitorHeight;
	}

	public void setWindowWidth(int windowWidth) {
		this.windowWidth = windowWidth;
	}

	public void setWindowHeight(int windowHeight) {
		this.windowHeight = windowHeight;
	}

}
