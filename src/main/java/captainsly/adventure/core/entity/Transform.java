package captainsly.adventure.core.entity;

import org.joml.Vector2f;

public class Transform {

	public Vector2f position, scale;
	public float rotation;

	public Transform() {
		this.position = new Vector2f();
		this.scale = new Vector2f();
		this.rotation = 0.0f;
	}

	public Transform(Vector2f position) {
		this.position = position;
		this.scale = new Vector2f();
	}

	public Transform(Vector2f position, Vector2f scale) {
		this.position = position;
		this.scale = scale;
		rotation = 0.0f;
	}

	public Transform(Vector2f position, Vector2f scale, float rotation) {
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
	}

	public Transform copy() {
		Transform t = new Transform(new Vector2f(this.position), new Vector2f(this.scale), this.rotation);
		return t;
	}

	public void copy(Transform to) {
		to.position.set(this.position);
		to.scale.set(this.scale);
		to.scale.set(this.rotation);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Transform))
			return false;

		Transform t = (Transform) o;
		return t.position.equals(this.position) && t.scale.equals(this.scale) && t.rotation == this.rotation;
	}

}
