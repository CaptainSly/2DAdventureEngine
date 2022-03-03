package captainsly.adventure.core.render.sprite;

import org.joml.Vector2f;
import org.joml.Vector4f;

import captainsly.adventure.core.render.Texture;

public class Sprite {

	private Texture spriteTexture;
	private Vector4f spriteColor;
	private Vector2f[] uvCoords = { new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0), new Vector2f(0, 1), };

	public Sprite(Texture spriteTexture, Vector4f spriteColor) {
		this.spriteTexture = spriteTexture;
		this.spriteColor = spriteColor;
	}
	
	public Sprite(Texture spriteTexture, Vector2f[] uvCoords) {
		this(spriteTexture);
		this.uvCoords = uvCoords;
	}
	
	public Sprite(Texture spriteTexture) {
		this.spriteTexture = spriteTexture;
		spriteColor = new Vector4f(1, 1, 1, 1);
	}

	public Texture getSpriteTexture() {
		return spriteTexture;
	}

	public Vector4f getSpriteColor() {
		return spriteColor;
	}

	public Vector2f[] getUvCoords() {
		return uvCoords;
	}
	
	public void setUVCoords(Vector2f[] uvCoords) {
		this.uvCoords = uvCoords;
	}

}