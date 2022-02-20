package captainsly.adventure.core.entity;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class OrthographicCamera {

	private Matrix4f projectionMatrix, viewMatrix;
	public Vector2f cameraPosition;

	public OrthographicCamera(Vector2f cameraPosition) {
		this.cameraPosition = cameraPosition;
		this.projectionMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		adjustProjection();
	}

	private void adjustProjection() {
		projectionMatrix.identity();
		projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
	}

	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		this.viewMatrix.identity();
		viewMatrix.lookAt(new Vector3f(cameraPosition.x, cameraPosition.y, 20.0f),
				cameraFront.add(cameraPosition.x, cameraPosition.y, 0.0f), cameraUp);

		return this.viewMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

}
