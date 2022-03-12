package captainsly.adventure.core.entity.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import captainsly.adventure.core.entity.Transform;
import captainsly.adventure.core.render.Texture;
import captainsly.adventure.core.render.sprite.Sprite;
import imgui.ImGui;

public class SpriteRenderer extends Component {

	private Sprite sprite = new Sprite();
	private transient Transform lastTransform;
	private boolean isDirty = false;

	public SpriteRenderer() {
	}

	public SpriteRenderer(Sprite sprite) {
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

	@Override
	public void imgui() {
		float[] colors = { getSpriteColor().x, getSpriteColor().y, getSpriteColor().z, getSpriteColor().w };
		if (ImGui.colorPicker4("Color Picker: ", colors)) {
			setColor(new Vector4f(colors[0], colors[1], colors[2], colors[3]));
			this.isDirty = true;
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
