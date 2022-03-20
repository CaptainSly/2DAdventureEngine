package captainsly.adventure.core.render.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.components.SpriteRenderer;
import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.core.render.Texture;
import captainsly.adventure.core.render.shaders.Shader;

public class Renderer implements Disposable {

	private final int MAX_BATCH_SIZE = 1000;

	private List<RenderBatch> batches;

	private static Shader currentShader;

	public Renderer() {
		batches = new ArrayList<>();
	}

	public void addGameObject(GameObject gObj) {
		SpriteRenderer sprite = gObj.getComponent(SpriteRenderer.class);

		if (sprite != null)
			add(sprite);
	}

	public void render() {
		currentShader.bind();
		for (RenderBatch batch : batches)
			batch.render();
		currentShader.unbind();
	}

	private void add(SpriteRenderer sprite) {
		boolean added = false;

		for (RenderBatch batch : batches) {
			if (batch.hasRoom() && batch.getZIndex() == sprite.gameObject.getZIndex()) {
				Texture texture = sprite.getTexture();
				if (texture != null || (batch.hasTexture(texture) || batch.hasTextureRoom())) {
					batch.addSprite(sprite);
					added = true;
					break;
				}
			}
		}

		if (!added) {
			RenderBatch batch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.getZIndex());
			batch.start();
			batch.addSprite(sprite);
			batches.add(batch);
			Collections.sort(batches);
		}
	}

	public static void bindShader(Shader shader) {
		currentShader = shader;
		try {
			currentShader.addUniform("uProjectionMatrix");

			currentShader.addUniform("uViewMatrix");

			currentShader.addUniform("uTextures");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Shader getBoundShader() {
		return currentShader;
	}

	@Override
	public void onDispose() {
		for (RenderBatch batch : batches)
			batch.onDispose();
	}

}
