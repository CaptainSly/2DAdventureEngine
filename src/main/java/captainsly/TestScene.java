package captainsly;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.AssetPool;
import captainsly.adventure.core.Scene;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.Transform;
import captainsly.adventure.core.entity.components.SpriteRendererComponent;
import captainsly.adventure.core.input.KeyListener;
import captainsly.adventure.core.input.MouseListener;
import captainsly.adventure.core.render.sprite.SpriteSheet;

public class TestScene extends Scene {

	private GameObject leafObj;

	public TestScene() {
		super();
	}

	@Override
	public void onInitialization() {

		Adventure.adventureScript.callMethodi(AssetPool.getScript("AseCore"), "testFunc");
		
		AssetPool.addSpriteSheet("test", new SpriteSheet(AssetPool.getTexture("alchemist_idle"), 32, 48, 0));
		
		SpriteSheet testSheet = AssetPool.getSpriteSheet("test");
		GameObject testObj = new GameObject("testObj", new Transform(new Vector2f(100, 100), new Vector2f(100, 100)));
		testObj.addComponent(new SpriteRendererComponent(testSheet.getSprite(2)));
		addGameObjectToScene(testObj);
		
	}

	@Override
	public void onRender(double delta) {
	}

	float temp = 0.0f;

	@Override
	public void onUpdate(double delta) {

		temp += 10 * delta;
		// TEST Camera/Input Stuff
		if (KeyListener.isKeyDown(GLFW_KEY_W))
			camera.cameraPosition.y -= 5;
		if (KeyListener.isKeyDown(GLFW_KEY_A))
			camera.cameraPosition.x += 5;
		if (KeyListener.isKeyDown(GLFW_KEY_S))
			camera.cameraPosition.y += 5;
		if (KeyListener.isKeyDown(GLFW_KEY_D))
			camera.cameraPosition.x -= 5;

		if (MouseListener.isButtonDown(GLFW_MOUSE_BUTTON_LEFT))
			camera.cameraPosition.zero();

	}

	@Override
	public void onInput(double delta) {

	}

	@Override
	public void onDispose() {
		super.onDispose();
	}

}
