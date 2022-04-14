package captainsly.adventure.core.scenes;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.AssetPool;
import captainsly.adventure.core.Prefab;
import captainsly.adventure.core.SaveManager;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.Transform;
import captainsly.adventure.core.entity.components.internal.EditorCameraComponent;
import captainsly.adventure.core.entity.components.internal.GridLinesComponent;
import captainsly.adventure.core.entity.components.internal.MouseControlComponent;
import captainsly.adventure.core.entity.components.internal.gizmos.TranslateGizmo;
import captainsly.adventure.core.render.sprite.Sprite;
import captainsly.adventure.core.render.sprite.SpriteSheet;
import captainsly.adventure.utils.Settings;
import imgui.ImGui;
import imgui.ImVec2;

public class Editor extends Scene {

	private List<SpriteSheet> spriteSheets = new ArrayList<>();
	private GameObject levelEditorObject = new GameObject("LEVEL EDITOR", new Transform());

	@Override
	public void onInitialization() {
		spriteSheets.add(AssetPool.createSpriteSheet("terrain", 16, 16, 0));

		SpriteSheet spriteSheet = AssetPool.createSpriteSheet("gizmos", 2, 24, 48, 0);
		Sprite gizmoSprite = spriteSheet.getSprite(1);

		levelEditorObject.addComponents(new MouseControlComponent(), new GridLinesComponent(),
				new EditorCameraComponent(camera),
				new TranslateGizmo(gizmoSprite, Adventure.guiLayer.getPropertiesWindow()));

		levelEditorObject.setNoSerialize();
		levelEditorObject.start();
	}

	@Override
	public void onGui() {
		ImGui.begin("Level Editor");
		levelEditorObject.imgui();
		ImGui.end();

		ImGui.begin("Tileset");
		{

			ImGui.text("FPS: " + Adventure.engine.getEngineFPS());
			// Map Save/Load System TODO: Clean and Polish
			if (ImGui.button("Save Map")) {
				SaveManager.saveMap("test", getGameObjects());
			}

			if (ImGui.button("Load Map")) {
				SaveManager.load("test");
			}

			// TODO: Implement TileMap Brush System
			if (ImGui.button("Paint")) {
				System.out.println("Regular Brush");
			}

			// Get the tilesets in the array and draw them
			for (SpriteSheet sheet : spriteSheets)
				createTilesetUi(sheet);
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
		levelEditorObject.update(delta);
	}

	private void createTilesetUi(SpriteSheet spriteSheet) {
		ImVec2 windowPos = new ImVec2();
		ImVec2 windowSize = new ImVec2();
		ImVec2 itemSpacing = new ImVec2();

		ImGui.getWindowPos(windowPos);
		ImGui.getWindowSize(windowSize);
		ImGui.getStyle().getItemSpacing(itemSpacing);

		float windowX = windowPos.x + windowSize.x;

		for (int i = 0; i < spriteSheet.getSpriteSheetSize(); i++) {
			Sprite sprite = spriteSheet.getSprite(i);
			float spriteWidth = sprite.getSpriteWidth() * 4;
			float spriteHeight = sprite.getSpriteHeight() * 4;

			int id = sprite.getSpriteTextureId();

			Vector2f[] uvCoords = sprite.getUvCoords();

			ImGui.pushID(i);
			if (ImGui.imageButton(id, spriteWidth, spriteHeight, uvCoords[2].x, uvCoords[0].y, uvCoords[0].x,
					uvCoords[2].y)) {
				GameObject object = Prefab.generateSpriteObject(sprite, Settings.GRID_SIZE, Settings.GRID_SIZE);
				levelEditorObject.getComponent(MouseControlComponent.class).pickupObject(object);
			}
			ImGui.popID();

			ImVec2 lastButtonPosition = new ImVec2();
			ImGui.getItemRectMax(lastButtonPosition);

			float lastButtonX = lastButtonPosition.x;
			float nextButtonX = lastButtonX + itemSpacing.x + spriteWidth;

			if (i + 1 < spriteSheet.getSpriteSheetSize() && nextButtonX < windowX) {
				ImGui.sameLine();
			}

		}
	}

	@Override
	public void onDispose() {
	}

}