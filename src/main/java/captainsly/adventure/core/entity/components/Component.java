package captainsly.adventure.core.entity.components;

import captainsly.adventure.core.entity.GameObject;

public abstract class Component {

	public GameObject gameObject = null;

	public void start() {
	}

	public abstract void update(double delta);

}
