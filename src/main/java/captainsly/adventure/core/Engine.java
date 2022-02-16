package captainsly.adventure.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.IntBuffer;
import java.util.Random;

import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.core.impl.Scene;
import captainsly.adventure.core.input.KeyListener;
import captainsly.adventure.core.render.Window;
import captainsly.adventure.utils.Utils;

public class Engine implements Disposable {

	private static final Logger log = LoggerFactory.getLogger(Engine.class);

	private Window window;
	private Scene currentScene;

	public Engine(Scene scene) {
		Adventure.engine = this;
		Adventure.rnJesus = new Random(System.currentTimeMillis());
		Adventure.log = log;
		currentScene = scene;
		initalizeEngine();
	}

	private void initalizeEngine() {
		Adventure.log.info("LWJGL Version: " + Version.getVersion());

		// Setup GLFW Error Callback
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		window = new Window("Adventure", 1280, 720);
		Adventure.window = window;

		glfwSetKeyCallback(window.getWindowPointer(), KeyListener::keyCallback);

		glfwSetFramebufferSizeCallback(window.getWindowPointer(), (window, windowWidth, windowHeight) -> {
			this.window.setWindowWidth(windowWidth);
			this.window.setWindowHeight(windowHeight);
			this.window.setIsResized(true);

			Adventure.log.debug("Width and Height of Window has been changed to " + windowWidth + " x " + windowHeight);

		});

		// Get the thread stack and push a new frame
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window.getWindowPointer(), pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(window.getWindowPointer(), (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		glfwMakeContextCurrent(window.getWindowPointer());
		glfwSwapInterval(1);
		glfwShowWindow(window.getWindowPointer());

		GL.createCapabilities();
		Adventure.log.info("OpenGL Version: " + Utils.getOpenGLVersion());
		
		currentScene.onInitialization();

	}

	private void engineLoop() {
		boolean running = true;

		int frames = 0;
		double frameCounter = 0;

		double lastTime = glfwGetTime();
		double unprocessedTime = 0;

		double frameTime = 1.0 / 60;

		while (running) {
			boolean render = false;

			double startTime = glfwGetTime();
			double processedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += processedTime;
			frameCounter += processedTime;

			while (unprocessedTime > frameTime) {
				render = true;

				unprocessedTime -= frameTime;

				glfwPollEvents();

				if (glfwWindowShouldClose(window.getWindowPointer()))
					running = false;

				if (KeyListener.isKeyDown(GLFW_KEY_ESCAPE))
					glfwSetWindowShouldClose(window.getWindowPointer(), true);

				currentScene.onInput(frameTime);
				currentScene.onUpdate(frameTime);

				if (frameCounter >= 1.0) {
					window.setWindowTitle("Adventure fps:" + frames);
					frames = 0;
					frameCounter = 0;
				}

			}

			if (render) {

				if (window.isResized()) {
					glViewport(0, 0, window.getWindowWidth(), window.getWindowHeight());
					window.setIsResized(false);
					Adventure.log.debug("Resizing");
				}

				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glClearColor(0.0f, 1.0f, 0.0f, 1.0f);

				currentScene.onRender(frameTime);

				glfwSwapBuffers(window.getWindowPointer());
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public void switchScene(Scene scene) {
		if (currentScene == null) {
			currentScene = scene;
			currentScene.onInitialization();
		} else {
			currentScene.onDispose();

			currentScene = scene;
			currentScene.onInitialization();
		}
	}

	public void run() {
		engineLoop();
		onDispose();
	}

	@Override
	public void onDispose() {
		Adventure.log.debug("Disposing");
		currentScene.onDispose();

		// Free the window callbacks
		Callbacks.glfwFreeCallbacks(window.getWindowPointer());
		glfwDestroyWindow(window.getWindowPointer());

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();

	}

}
