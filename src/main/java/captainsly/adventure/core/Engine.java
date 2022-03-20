package captainsly.adventure.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.nio.IntBuffer;
import java.util.Random;

import org.jruby.runtime.Constants;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.components.Component;
import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.core.input.ControllerListener;
import captainsly.adventure.core.input.KeyListener;
import captainsly.adventure.core.input.MouseListener;
import captainsly.adventure.core.render.PickingTexture;
import captainsly.adventure.core.render.Window;
import captainsly.adventure.core.render.renderer.DebugRenderer;
import captainsly.adventure.core.render.renderer.Framebuffer;
import captainsly.adventure.core.render.renderer.Renderer;
import captainsly.adventure.core.render.shaders.Shader;
import captainsly.adventure.core.scenes.Scene;
import captainsly.adventure.core.scripting.AdventureScriptEngine;
import captainsly.adventure.core.typeadapters.ComponentTypeAdapter;
import captainsly.adventure.core.typeadapters.GameObjectTypeAdapter;
import captainsly.adventure.utils.Utils;

public class Engine implements Disposable {

	private static final Logger log = LoggerFactory.getLogger(Engine.class);

	private Window window;
	private Scene currentScene;
	private AdventureScriptEngine adventureScript;
	private ImGuiLayer guiLayer;
	private Renderer renderer;

	private Framebuffer frameBuffer;
	private PickingTexture pickingTexture;
	private boolean isRunning;

	private int engineFps;

	public Engine(Scene scene) {
		Adventure.engine = this;
		Adventure.rnJesus = new Random(System.currentTimeMillis());
		Adventure.log = log;

		adventureScript = new AdventureScriptEngine();
		Adventure.adventureScript = adventureScript;

		currentScene = scene;
		Adventure.currentScene = currentScene;

		// Create the engine companion directory and it's sub directories if it does not
		// exist.

		// Companion Directory Layout
		if (!(new File(Utils.ENGINE_WORKING_DIRECTORY).exists())) {
			Utils.createEngineFileStructure();

			String[] paths = new String[] { "scripts", "mods", "config", "logs", "saves", };

			for (int i = 0; i < paths.length; i++) {
				File compDir = new File(Utils.ENGINE_WORKING_DIRECTORY + paths[i]);
				compDir.mkdirs();
			}

		}

		initalizeEngine();
	}

	private void initalizeEngine() {
		Adventure.log.info("Starting Engine Initialization");
		Adventure.gson = new GsonBuilder().setPrettyPrinting()

				.registerTypeAdapter(Component.class, new ComponentTypeAdapter())
				.registerTypeAdapter(GameObject.class, new GameObjectTypeAdapter()).create();

		// Setup GLFW Error Callback
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		Adventure.log.info("Creating Window");
		window = new Window("Adventure", 1280, 720);
		Adventure.window = window;

		Adventure.log.info("Setting callbacks");
		glfwSetKeyCallback(window.getWindowPointer(), KeyListener::keyCallback);
		glfwSetMouseButtonCallback(window.getWindowPointer(), MouseListener::mouseButtonCallback);
		glfwSetCursorPosCallback(window.getWindowPointer(), MouseListener::mousePosCallback);
		glfwSetScrollCallback(window.getWindowPointer(), MouseListener::mouseScrollCallback);
		glfwSetJoystickCallback(ControllerListener::controllerCallback);

		glfwSetFramebufferSizeCallback(window.getWindowPointer(), (window, windowWidth, windowHeight) -> {
			this.window.setWindowWidth(windowWidth);
			this.window.setWindowHeight(windowHeight);
			this.window.setIsResized(true);
		});

		// Get the thread stack and push a new frame
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window.getWindowPointer(), pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			window.setMonitorWidth(vidmode.width());
			window.setMonitorHeight(vidmode.height());

			// Center the window
			glfwSetWindowPos(window.getWindowPointer(), (vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		glfwMakeContextCurrent(window.getWindowPointer());
		glfwSwapInterval(1);
		glfwShowWindow(window.getWindowPointer());

		Adventure.log.info("Creating GL Capabilities");
		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

		guiLayer = new ImGuiLayer(window.getWindowPointer());

		renderer = new Renderer();

		frameBuffer = new Framebuffer(window.getMonitorWidth(), window.getMonitorHeight());
		pickingTexture = new PickingTexture(window.getMonitorWidth(), window.getMonitorHeight());
		glViewport(0, 0, window.getMonitorWidth(), window.getMonitorHeight());

		// ==========================
		// Engine Version Information
		// ==========================

		String[] versions = { "LWJGL Version: " + Version.getVersion(), "OpenGL Version: " + Utils.getOpenGLVersion(),
				"JRuby Version: " + Constants.VERSION + " | Ruby Version: " + Constants.RUBY_VERSION,
				"AdventureScript Engine Version: " + AdventureScriptEngine.SCRIPT_ENGINE_VERSION

		};

		for (String version : versions)
			Adventure.log.info(version);

		Adventure.log.info("Finishing Engine Initialization");
	}

	private void engineLoop() {
		isRunning = true;

		int frames = 0;
		double frameCounter = 0;

		double lastTime = glfwGetTime();
		double unprocessedTime = 0;

		double frameTime = 1.0 / 300;

		currentScene.onStart();

		// Create Default Shaders
		Shader defaultShader = AssetPool.getShader("defaultShader");
		Shader pickingShader = AssetPool.getShader("pickingShader");

		while (isRunning) {
			boolean render = false;

			double startTime = glfwGetTime();
			double processedTime = startTime - lastTime;

			while (unprocessedTime > frameTime) {
				render = true;

				unprocessedTime -= frameTime;

				glfwPollEvents();

				if (glfwWindowShouldClose(window.getWindowPointer()))
					isRunning = false;

				if (KeyListener.isKeyDown(GLFW_KEY_ESCAPE))
					glfwSetWindowShouldClose(window.getWindowPointer(), true);

				MouseListener.update();
				currentScene.onInput(frameTime);
				currentScene.update(frameTime);

				if (frameCounter >= 1.0) {
					engineFps = frames;
					frames = 0;
					frameCounter = 0;
				}

			}

			if (render) {

				// Render Pass 1 - Render to Picking Texture
				glDisable(GL_BLEND);
				pickingTexture.enableWriting();
				{
					glViewport(0, 0, window.getMonitorWidth(), window.getMonitorHeight());
					glClearColor(0, 0, 0, 0);
					glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
					Renderer.bindShader(pickingShader);
					currentScene.render(frameTime);

					if (MouseListener.isButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
						int x = (int) MouseListener.getMouseScreenPosition().x;
						int y = (int) MouseListener.getMouseScreenPosition().y;

						Adventure.log.debug("entityId: " + pickingTexture.readPixel(x, y));
					}

				}
				pickingTexture.disableWriting();
				glEnable(GL_BLEND);

				// Render Pass 2 - Render Game
				DebugRenderer.beginFrame();
				frameBuffer.bind();
				{
					render(1, 1, 1);
					DebugRenderer.draw();
					Renderer.bindShader(defaultShader);
					currentScene.render(frameTime);
				}
				frameBuffer.unbind();

				guiLayer.render((float) frameTime, currentScene);

				glfwSwapBuffers(window.getWindowPointer());
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			lastTime = startTime;

			unprocessedTime += processedTime;
			frameCounter += processedTime;
		}
	}

	public void render(float r, float g, float b) {
		glClearColor(r, g, b, 1);
		glClear(GL_COLOR_BUFFER_BIT);

	}

	public void switchScene(Scene scene) {
		if (currentScene == null) {
			currentScene = scene;
			currentScene.onStart();
		} else {
			currentScene.onDispose();

			currentScene = scene;
			currentScene.onStart();
		}

		Adventure.currentScene = currentScene;
	}

	public void run() {
		engineLoop();
		onDispose();
	}

	public int getEngineFPS() {
		return engineFps;
	}

	public Scene getCurrentScene() {
		return currentScene;
	}

	public Framebuffer getEngineFramebuffer() {
		return frameBuffer;
	}

	public Renderer getEngineRenderer() {
		return renderer;
	}

	public boolean isEngineRunning() {
		return isRunning;
	}

	@Override
	public void onDispose() {
		Adventure.log.info("Disposing");
		currentScene.onDispose();
		adventureScript.onDispose();
		guiLayer.onDispose();

		// Free the window callbacks
		Callbacks.glfwFreeCallbacks(window.getWindowPointer());
		glfwDestroyWindow(window.getWindowPointer());

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

}
