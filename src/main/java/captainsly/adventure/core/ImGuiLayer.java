package captainsly.adventure.core;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2d;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.editor.GamePort;
import captainsly.adventure.core.editor.PropertiesWindow;
import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.core.input.KeyListener;
import captainsly.adventure.core.input.MouseListener;
import captainsly.adventure.core.render.PickingTexture;
import captainsly.adventure.core.scenes.Scene;
import captainsly.adventure.utils.Utils;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImBoolean;

public class ImGuiLayer implements Disposable {

	private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
	private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
	private final long glfwWindowPointer;
	private GamePort gamePort;
	private PropertiesWindow propertiesWindow;

	public ImGuiLayer(long glfwWindowPointer, PickingTexture pickingTexture) {
		this.glfwWindowPointer = glfwWindowPointer;
		this.gamePort = new GamePort();
		this.propertiesWindow = new PropertiesWindow(pickingTexture);
		initImGui();
	}

	// Initialize Dear ImGui.
	private void initImGui() {
		// IMPORTANT!!
		// This line is critical for Dear ImGui to work.
		ImGui.createContext();

		// ------------------------------------------------------------
		// Initialize ImGuiIO config
		final ImGuiIO io = ImGui.getIO();

		io.setIniFilename(Utils.ENGINE_WORKING_DIRECTORY + "config/imGui.ini");
		io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard | ImGuiConfigFlags.NavEnableGamepad
				| ImGuiConfigFlags.DockingEnable); // Navigation with keyboard
		io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
		io.setBackendPlatformName("imgui_java_impl_glfw");

		// ------------------------------------------------------------
		// Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[]
		// array.
		final int[] keyMap = new int[ImGuiKey.COUNT];
		keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
		keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
		keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
		keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
		keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
		keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
		keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
		keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
		keyMap[ImGuiKey.End] = GLFW_KEY_END;
		keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
		keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
		keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
		keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
		keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
		keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
		keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
		keyMap[ImGuiKey.A] = GLFW_KEY_A;
		keyMap[ImGuiKey.C] = GLFW_KEY_C;
		keyMap[ImGuiKey.V] = GLFW_KEY_V;
		keyMap[ImGuiKey.X] = GLFW_KEY_X;
		keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
		keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
		io.setKeyMap(keyMap);

		// ------------------------------------------------------------
		// Mouse cursors mapping
		mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
		mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

		// ------------------------------------------------------------
		// GLFW callbacks to handle user input

		glfwSetKeyCallback(glfwWindowPointer, (w, key, scancode, action, mods) -> {
			if (action == GLFW_PRESS) {
				io.setKeysDown(key, true);
			} else if (action == GLFW_RELEASE) {
				io.setKeysDown(key, false);
			}

			io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
			io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
			io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
			io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));
			KeyListener.keyCallback(w, key, scancode, action, mods);

		});

		glfwSetCharCallback(glfwWindowPointer, (w, c) -> {
			if (c != GLFW_KEY_DELETE) {
				io.addInputCharacter(c);
			}
		});

		glfwSetMouseButtonCallback(glfwWindowPointer, (w, button, action, mods) -> {
			final boolean[] mouseDown = new boolean[5];

			mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
			mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
			mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
			mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
			mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

			io.setMouseDown(mouseDown);

			if (!gamePort.doesWantCapture() && !io.getWantCaptureMouse() && mouseDown[1]) {
				ImGui.setWindowFocus(null);
			}

			if (!io.getWantCaptureMouse() || gamePort.doesWantCapture())
				MouseListener.mouseButtonCallback(w, button, action, mods);
		});

		glfwSetScrollCallback(glfwWindowPointer, (w, xOffset, yOffset) -> {
			io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
			io.setMouseWheel(io.getMouseWheel() + (float) yOffset);

			if (!io.getWantCaptureMouse() || gamePort.doesWantCapture())
				MouseListener.mouseScrollCallback(w, xOffset, yOffset);

		});

		io.setSetClipboardTextFn(new ImStrConsumer() {
			@Override
			public void accept(final String s) {
				glfwSetClipboardString(glfwWindowPointer, s);
			}
		});

		io.setGetClipboardTextFn(new ImStrSupplier() {
			@Override
			public String get() {
				final String clipboardString = glfwGetClipboardString(glfwWindowPointer);
				if (clipboardString != null) {
					return clipboardString;
				} else {
					return "";
				}
			}
		});

		// ------------------------------------------------------------
		// Fonts configuration
		// Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

		final ImFontAtlas fontAtlas = io.getFonts();
		final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

		// Glyphs could be added per-font as well as per config used globally like here
		fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());

		fontConfig.setPixelSnapH(true);
		fontAtlas.addFontFromFileTTF("src/main/resources/assets/fonts/typewriter.ttf", 18, fontConfig);

		fontConfig.destroy(); // After all fonts were added we don't need this config more

		// Method initializes LWJGL3 renderer.
		// This method SHOULD be called after you've initialized your ImGui
		// configuration (fonts and so on).
		// ImGui context should be created as well.
		imGuiGl3.init("#version 330 core");
	}

	public void render(float dt, Scene scene) {
		startFrame(dt);

		ImGui.newFrame();
		{
			setupDockspace();

			scene.onSceneImGui();
			gamePort.imgui();
			propertiesWindow.update(dt, scene);
			propertiesWindow.onImGui();

			ImGui.end(); // Ends the dockspace
		}
		ImGui.render();

		endFrame();
	}

	private void startFrame(final float deltaTime) {
		Vector2d mousePos = new Vector2d(MouseListener.getX(), MouseListener.getY());

		// We SHOULD call those methods to update Dear ImGui state for the current frame
		final ImGuiIO io = ImGui.getIO();
		io.setDisplaySize(Adventure.window.getWindowWidth(), Adventure.window.getWindowHeight());
		io.setDisplayFramebufferScale(1, 1);
		io.setMousePos((float) mousePos.x, (float) mousePos.y);
		io.setDeltaTime(deltaTime);

		// Update the mouse cursor
		final int imguiCursor = ImGui.getMouseCursor();
		glfwSetCursor(glfwWindowPointer, mouseCursors[imguiCursor]);
		glfwSetInputMode(glfwWindowPointer, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}

	private void endFrame() {
		// After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
		// At that moment ImGui will be rendered to the current OpenGL context.
		imGuiGl3.renderDrawData(ImGui.getDrawData());
	}

	private void setupDockspace() {
		int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

		ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
		ImGui.setNextWindowSize(Adventure.window.getWindowWidth(), Adventure.window.getWindowHeight());
		ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
		ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

		windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize
				| ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

		ImGui.begin("Dockspace Parent", new ImBoolean(true), windowFlags);
		ImGui.popStyleVar(2);

		// Dockspace
		ImGui.dockSpace(ImGui.getID("Dockspace"));
	}

	public PropertiesWindow getPropertiesWindow() {
		return propertiesWindow;
	}

	public GamePort getGamePort() {
		return gamePort;
	}
	
	@Override
	public void onDispose() {
		imGuiGl3.dispose();
		ImGui.destroyContext();

	}

}
