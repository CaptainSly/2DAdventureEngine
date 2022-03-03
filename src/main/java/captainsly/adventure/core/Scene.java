package captainsly.adventure.core;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.OrthographicCamera;
import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.core.render.Renderer;

public abstract class Scene implements Disposable {

	private List<GameObject> gameObjects;
	protected OrthographicCamera camera;
	protected Renderer renderer;

	public Scene() {
		gameObjects = new ArrayList<>();
		camera = new OrthographicCamera(new Vector2f());
		renderer = new Renderer();
	}

	public void onStart() {
		onInitialization();
	}

	public void addGameObjectToScene(GameObject obj) {
		if (!Adventure.engine.isEngineRunning())
			getGameObjects().add(obj);
		else {
			getGameObjects().add(obj);
			obj.start();
			renderer.addGameObject(obj);
		}
	}

	public void update(double frameTime) {
		onUpdate(frameTime);
		for (GameObject gObj : getGameObjects())
			gObj.update(frameTime);

	}

	public void render(double frameTime) {
		Adventure.engine.render(1, 0, 0);

		renderer.render();
		onRender(frameTime);
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public OrthographicCamera getSceneCamera() {
		return camera;
	}

	public abstract void onInitialization();

	public abstract void onRender(double delta);

	public abstract void onInput(double delta);

	public abstract void onUpdate(double delta);
	
	public void onDispose() {
		renderer.onDispose();
	}

}
