package captainsly.adventure.core;

import org.joml.Vector2f;

import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.Transform;
import captainsly.adventure.core.entity.components.SpriteRenderer;
import captainsly.adventure.core.render.sprite.Sprite;

public class Prefab {

	public static GameObject generateSpriteObject(Sprite sprite, float spriteSizeX, float spriteSizeY) {
		GameObject spriteObject = new GameObject("Generated Sprite Object",
				new Transform(new Vector2f(), new Vector2f(spriteSizeX, spriteSizeY)));
		SpriteRenderer spriteRenderer = new SpriteRenderer(sprite);
		spriteObject.addComponent(spriteRenderer);

		return spriteObject;
	}

}
