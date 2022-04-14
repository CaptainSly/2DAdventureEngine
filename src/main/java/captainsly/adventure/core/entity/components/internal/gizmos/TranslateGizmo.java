package captainsly.adventure.core.entity.components.internal.gizmos;

import org.joml.Vector2f;
import org.joml.Vector4f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.Prefab;
import captainsly.adventure.core.editor.PropertiesWindow;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.components.Component;
import captainsly.adventure.core.entity.components.SpriteRenderer;
import captainsly.adventure.core.render.sprite.Sprite;

public class TranslateGizmo extends Component {

	private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
	private Vector4f xAxisColorHover = new Vector4f();
	
	private Vector2f xAxisOffset = new Vector2f(64, -5);
    private Vector2f yAxisOffset = new Vector2f(16, 61);
    
	private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
	private Vector4f yAxisColorHover = new Vector4f();

	private GameObject xAxisGObj;
	private GameObject yAxisGObj;

	private SpriteRenderer xAxisSprite;
	private SpriteRenderer yAxisSprite;

	private GameObject activeGObj = null;

	private PropertiesWindow propertiesWindow;

	public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
		xAxisGObj = Prefab.generateSpriteObject(arrowSprite, 16, 48);
		yAxisGObj = Prefab.generateSpriteObject(arrowSprite, 16, 48);

		xAxisGObj.setzIndex(1);
		yAxisGObj.setzIndex(1);
		
		xAxisSprite = xAxisGObj.getComponent(SpriteRenderer.class);
		yAxisSprite = yAxisGObj.getComponent(SpriteRenderer.class);

		this.propertiesWindow = propertiesWindow;

		Adventure.currentScene.addGameObjectToScene(xAxisGObj);
		Adventure.currentScene.addGameObjectToScene(yAxisGObj);

	}

	@Override
	public void start() {
		xAxisGObj.setObjectRotation(90);
		yAxisGObj.setObjectRotation(180);
		
		xAxisGObj.setNoSerialize();
		yAxisGObj.setNoSerialize();
	}

	@Override
	public void update(double delta) {
		if (activeGObj != null) {
			xAxisGObj.getObjectPosition().set(activeGObj.getObjectPosition());
			xAxisGObj.getObjectPosition().add(xAxisOffset);
			yAxisGObj.getObjectPosition().set(activeGObj.getObjectPosition());
			yAxisGObj.getObjectPosition().add(yAxisOffset);
			
		}

		activeGObj = propertiesWindow.getActiveGameObject();
		if (activeGObj != null)
			setActive();
		else
			setInactive();
	}

	private void setActive() {
		xAxisSprite.setColor(xAxisColor);
		yAxisSprite.setColor(yAxisColor);
	}

	private void setInactive() {
		activeGObj = null;
		xAxisSprite.setColor(new Vector4f().zero());
		yAxisSprite.setColor(new Vector4f().zero());
	}

}
