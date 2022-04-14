package captainsly.adventure.core.editor;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2i;

import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.input.MouseListener;
import captainsly.adventure.core.render.PickingTexture;
import captainsly.adventure.core.scenes.Scene;
import imgui.ImGui;

public class PropertiesWindow {

	private GameObject activeGameObject = null;
	private PickingTexture pickingTexture;

	private float debounce = 0.2f;

	public PropertiesWindow(PickingTexture pickingTexture) {
		this.pickingTexture = pickingTexture;
	}

	public void update(double delta, Scene currentScene) {

		debounce -= delta;

		if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
			Vector2i mousePos = new Vector2i((int) MouseListener.getScreenX(), (int) MouseListener.getScreenY());

			activeGameObject = currentScene.getGameObject(pickingTexture.readPixel(mousePos.x, mousePos.y));
			debounce = 0.1f;
		}
	}

	public void onImGui() {
		if (this.activeGameObject != null) {
			ImGui.begin("Properties");
			this.activeGameObject.imgui();
			ImGui.end();
		}
	}

	public GameObject getActiveGameObject() {
		return activeGameObject;
	}
}
