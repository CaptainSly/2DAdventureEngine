package captainsly.adventure.core.render.mesh;

import org.joml.Vector3f;

public class Vertex {

	public static final int SIZE = 3;

	private Vector3f position;

	public Vertex(Vector3f position) {
		this.position = position;
	}

	public Vertex(float pos1, float pos2, float pos3) {
		this.position = new Vector3f(pos1, pos2, pos3);
	}

	public Vector3f getPosition() {
		return position;
	}

}
