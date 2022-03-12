package captainsly.adventure.core.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import captainsly.adventure.core.entity.components.Component;

public class GameObject {

	private String objectId = "";
	private Transform transform = new Transform();
	private int zIndex = 0;
	private List<Component> components = new ArrayList<>();

	public GameObject() {
	}

	public GameObject(String objectId) {
		this.objectId = objectId;
		components = new ArrayList<>();
		transform = new Transform();
		zIndex = 0;
	}

	public GameObject(String objectId, Transform transform, int zIndex) {
		this.objectId = objectId;
		this.transform = transform;
		components = new ArrayList<>();
		this.zIndex = zIndex;
	}

	public GameObject(String objectId, Vector2f position, Vector2f scale, int zIndex) {
		this(objectId, new Transform(position, scale), zIndex);
	}

	public void listComponents() {
		for (Component c : components)
			System.out.println("Component: " + c.getClass().getSimpleName());
	}

	public <T extends Component> T getComponent(Class<T> componentClass) {

		for (Component c : components) {
			if (componentClass.isAssignableFrom(c.getClass())) {
				try {
					return componentClass.cast(c);
				} catch (ClassCastException e) {
					e.printStackTrace();
					assert false : "Error: Casting Component";
				}
			}
		}

		return null;
	}

	public <T extends Component> void removeComponent(Class<T> componentClass) {

		for (int i = 0; i < components.size(); i++) {
			if (componentClass.isAssignableFrom(components.get(i).getClass())) {
				components.remove(i);
				return;
			}
		}

	}

	public void addComponent(Component component) {
		this.components.add(component);
		component.gameObject = this;
	}
	
	public void addComponents(Component...components) {
		for (Component c : components) 
			addComponent(c);
	}

	public void update(double delta) {

		for (int i = 0; i < components.size(); i++) {
			components.get(i).update(delta);
		}

	}

	public void start() {

		for (int i = 0; i < components.size(); i++) {
			components.get(i).start();
		}
	}

	public void imgui() {
		for (Component c : components)
			c.imgui();
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

	public void setzIndex(int zIndex) {
		this.zIndex = zIndex;
	}

	public Transform getObjectTransform() {
		return transform;
	}

	public String getObjectId() {
		return objectId;
	}

	public int getZIndex() {
		return zIndex;
	}

}
