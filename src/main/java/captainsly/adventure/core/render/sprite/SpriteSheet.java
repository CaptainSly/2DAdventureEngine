package captainsly.adventure.core.render.sprite;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import captainsly.adventure.core.render.Texture;

public class SpriteSheet {

	private Texture parentTexture;
	private List<Sprite> spriteSheet;

	public SpriteSheet(Texture parentTexture, int spriteWidth, int spriteHeight) {
		this(parentTexture, spriteWidth, spriteHeight, 0);
	}
	
	public SpriteSheet(Texture parentTexture, int spriteWidth, int spriteHeight, int padding) {
		spriteSheet = new ArrayList<>();
		this.parentTexture = parentTexture;

		// Equation to figure out how many sprites are in a sheet
		// numSprites = (textureWidth / spriteWidth) * (textureHeight / spriteHeight)
		int numSprites = (parentTexture.getWidth() / spriteWidth) * (parentTexture.getHeight() / spriteHeight);

		int currentX = 0, currentY = parentTexture.getHeight() - spriteHeight;

		for (int i = 0; i < numSprites; i++) {
			float topY = (currentY + spriteHeight) / (float) parentTexture.getHeight();
			float rightX = (currentX + spriteWidth) / (float) parentTexture.getWidth();
			float leftX = currentX / (float) parentTexture.getWidth();
			float bottomY = currentY / (float) parentTexture.getHeight();

			Vector2f[] uvCoords = { new Vector2f(rightX, topY), new Vector2f(rightX, bottomY),
					new Vector2f(leftX, bottomY), new Vector2f(leftX, topY) };

			Sprite sprite = new Sprite();
			sprite.setTexture(this.parentTexture);
			sprite.setUVCoords(uvCoords);
			sprite.setSpriteWidth(spriteWidth);
			sprite.setSpriteHeight(spriteHeight);

			spriteSheet.add(sprite);

			currentX += spriteWidth + padding;

			if (currentX >= parentTexture.getWidth()) {
				currentX = 0;
				currentY -= spriteHeight + padding;
			}

		}
	}

	public List<Sprite> getSpriteSheet() {
		return spriteSheet;
	}

	public Sprite getSprite(int index) {
		return spriteSheet.get(index);
	}
	
	public int getSpriteSheetSize() {
		return spriteSheet.size();
	}

}
