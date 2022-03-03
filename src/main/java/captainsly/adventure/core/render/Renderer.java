package captainsly.adventure.core.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.components.SpriteRendererComponent;
import captainsly.adventure.core.impl.Disposable;

public class Renderer implements Disposable {

	private final int MAX_BATCH_SIZE = 1000;

	private List<RenderBatch> batches;

	public Renderer() {
		batches = new ArrayList<>();
	}

	public void addGameObject(GameObject gObj) {
		SpriteRendererComponent sprite = gObj.getComponent(SpriteRendererComponent.class);

		if (sprite != null)
			add(sprite);
	}

	public void render() {
		for (RenderBatch batch : batches) 
			batch.render();
	}

	private void add(SpriteRendererComponent sprite) {
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

	@Override
	public void onDispose() {
		for (RenderBatch batch : batches)
			batch.onDispose();
	}

}
