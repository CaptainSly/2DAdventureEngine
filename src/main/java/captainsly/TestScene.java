package captainsly;

import org.joml.Vector2f;
import org.joml.Vector4f;

import captainsly.adventure.core.Scene;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.components.SpriteRendererComponent;

public class TestScene extends Scene {

	public TestScene() {
		super();
	}
	
	@Override
	public void onInitialization() {

		int xOffset = 10, yOffset = 10;

		float totalWidth = (float) (600 - xOffset * 2);
		float totalHeight = (float) (300 - yOffset * 2);

		float sizeX = totalWidth / 100.0f;
		float sizeY = totalHeight / 100.0f;

		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {

				float xPos = xOffset + (x * sizeX);
				float yPos = yOffset + (y * sizeY);

				GameObject obj = new GameObject("gObj-x" + x + "y" + y, new Vector2f(xPos, yPos),
						new Vector2f(sizeX, sizeY));
				obj.addComponent(
						new SpriteRendererComponent(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
				renderer.addGameObject(obj);
			}
		}

	}

	@Override
	public void onRender(double delta) {
	}

	@Override
	public void onUpdate(double delta) {
	}

	@Override
	public void onInput(double delta) {

	}

	@Override
	public void onDispose() {
	}

}
