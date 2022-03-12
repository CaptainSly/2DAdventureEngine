package captainsly.adventure.core.editor;

import org.joml.Vector2f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.AssetPool;
import captainsly.adventure.core.Scene;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.Transform;
import captainsly.adventure.core.entity.components.ScriptComponent;
import captainsly.adventure.core.entity.components.SpriteRenderer;
import captainsly.adventure.core.render.sprite.Sprite;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class Editor extends Scene {

	private String jsonData;

	@Override
	public void onInitialization() {
		GameObject testObj = new GameObject("TestObj", new Transform(new Vector2f(100, 100), new Vector2f(100, 100)),
				0);
		SpriteRenderer objSpriteRenderer = new SpriteRenderer();
		ScriptComponent scriptComponent = new ScriptComponent();
		scriptComponent.setScript("AseCore");
		Sprite objSprite = new Sprite();
		objSprite.setTexture(AssetPool.getTexture("test"));
		objSpriteRenderer.setSprite(objSprite);
		testObj.addComponents(objSpriteRenderer, scriptComponent);

		addGameObjectToScene(testObj);
		activeGameObject = testObj;

		jsonData = Adventure.gson.toJson(activeGameObject);

	}

	int i = 1;

	@Override
	public void onGui() {
		ImGui.begin("Adventure", ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoMove);
		{
			ImGui.setWindowPos(Adventure.window.getWindowWidth() - ImGui.getWindowSizeX(), 0);
			ImGui.text("FPS: " + Adventure.engine.getEngineFPS());
			ImGui.newLine();
			ImGui.text(jsonData);
			if (ImGui.button("Test Load")) {
			}
		}
		ImGui.end();
	}

	@Override
	public void onRender(double delta) {
	}

	@Override
	public void onInput(double delta) {
	}

	@Override
	public void onUpdate(double delta) {
	}

}