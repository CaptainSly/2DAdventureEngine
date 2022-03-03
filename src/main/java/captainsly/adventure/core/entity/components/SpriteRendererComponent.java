package captainsly.adventure.core.entity.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import captainsly.adventure.core.entity.Transform;
import captainsly.adventure.core.render.Texture;
import captainsly.adventure.core.render.sprite.Sprite;

public class SpriteRendererComponent extends Component {

	private Sprite sprite;
	private Transform lastTransform;
	private boolean isDirty;

	public SpriteRendererComponent(Sprite sprite) {
		this.sprite = sprite;
		isDirty = true;
	}

	@Override
	public void start() {
		this.lastTransform = gameObject.getObjectTransform().copy();
	}

	@Override
	public void update(double delta) {

		if (!lastTransform.equals(gameObject.getObjectTransform())) {
			gameObject.getObjectTransform().copy(lastTransform);
			isDirty = true;
		}

	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
		isDirty = true;
	}

	public void setColor(Vector4f spriteColor) {
		if (!(this.getSpriteColor().equals(spriteColor))) {
			sprite.getSpriteColor().set(spriteColor);
			isDirty = true;
		}
	}
	
	public void clean() {
		isDirty = false;
	}

	public Vector4f getSpriteColor() {
		return sprite.getSpriteColor();
	}

	public Vector2f[] getTextureCoords() {
		return sprite.getUvCoords();
	}

	public Texture getTexture() {
		return sprite.getSpriteTexture();
	}
	
	public boolean isDirty() {
		return isDirty;
	}

}
