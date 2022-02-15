package captainsly.adventure.core.render.mesh;

import org.joml.Vector3f;

public class Vertex {

	public static final int SIZE = 3;

	private Vector3f position;

	public Vertex(Vector3f position) {
		this.position = position;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public static int getSize() {
		return SIZE;
	}

}
