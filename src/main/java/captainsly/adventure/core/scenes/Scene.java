package captainsly.adventure.core.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.joml.Vector2f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.Camera;
import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.core.render.renderer.Renderer;

public abstract class Scene implements Disposable {

	private List<GameObject> gameObjects;
	protected Camera camera;
	protected Renderer renderer;

	public Scene() {
		gameObjects = new ArrayList<>();
		camera = new Camera(new Vector2f());
		renderer = new Renderer();
	}

	public void onStart() {
		onInitialization();
		for (GameObject gObj : gameObjects) {
			gObj.start();
			renderer.addGameObject(gObj);
		}
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
		camera.adjustProjection();
		
		for (GameObject gObj : getGameObjects())
			gObj.update(frameTime);

		onUpdate(frameTime);
	}

	public void render(double frameTime) {
		renderer.render();
		onRender(frameTime);
	}

	

	public void onSceneImGui() {
		onGui();
	}

	public GameObject getGameObject(int uId) {
		Optional<GameObject> result = this.gameObjects.stream().filter(gObj -> gObj.getuID() == uId).findFirst();
		
		return result.orElse(null);
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public Camera getSceneCamera() {
		return camera;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public abstract void onInitialization();

	public abstract void onRender(double delta);

	public abstract void onInput(double delta);

	public abstract void onUpdate(double delta);
	
	public abstract void onGui(); 
	
}
