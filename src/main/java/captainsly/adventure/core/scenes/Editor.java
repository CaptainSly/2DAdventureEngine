package captainsly.adventure.core.scenes;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import captainsly.adventure.core.AssetPool;
import captainsly.adventure.core.Prefab;
import captainsly.adventure.core.editor.GamePort;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.Transform;
import captainsly.adventure.core.entity.components.internal.GridLinesComponent;
import captainsly.adventure.core.entity.components.internal.MouseControlComponent;
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
		levelEditorObject.addComponents(new MouseControlComponent(), new GridLinesComponent());

		spriteSheets.add(AssetPool.createSpriteSheet("Door0", 16, 16, 0));
		spriteSheets.add(AssetPool.createSpriteSheet("Floor", 16, 16, 0));
		spriteSheets.add(AssetPool.createSpriteSheet("Wall", 16, 16, 0));
		spriteSheets.add(AssetPool.createSpriteSheet("Decor0", 16, 16, 0));

	}

	@Override
	public void onGui() {

		GamePort.imgui(); // Draw the Gameport
		ImGui.begin("Tileset");
		{
			if (ImGui.button("Add new Tileset")) {
				// TODO: Implement Adding Tilesets
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