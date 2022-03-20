package captainsly.adventure.core.entity;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class OrthographicCamera {

	private Matrix4f projectionMatrix, viewMatrix, inverseProjectionMatrix, inverseViewMatrix;
	public Vector2f cameraPosition;
	
	private Vector2f projectionSize = new Vector2f(16.0f * 42.0f, 16.0f * 21.0f);

	public OrthographicCamera(Vector2f cameraPosition) {
		this.cameraPosition = cameraPosition;
		this.projectionMatrix = new Matrix4f();
		this.inverseProjectionMatrix = new Matrix4f();
		this.inverseViewMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		adjustProjection();
	}

	private void adjustProjection() {
		projectionMatrix.identity();
		projectionMatrix.ortho(0.0f, projectionSize.x, 0.0f, projectionSize.y, 0.0f, 100.0f);
		projectionMatrix.invert(inverseProjectionMatrix);
	}

	public Matrix4f getViewMatrix() {
		Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
		Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
		this.viewMatrix.identity();
		viewMatrix.lookAt(new Vector3f(cameraPosition.x, cameraPosition.y, 20.0f),
				cameraFront.add(cameraPosition.x, cameraPosition.y, 0.0f), cameraUp);

		this.viewMatrix.invert(inverseViewMatrix);
		return this.viewMatrix;
	}

	public void centerOnGameObject(GameObject obj) {
		this.cameraPosition.x = obj.getObjectTransform().position.x;
		this.cameraPosition.y = obj.getObjectTransform().position.y;

	}

	public Vector2f getProjectionSize() {
		return projectionSize;
	}
	
	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public Matrix4f getInverseProjectionMatrix() {
		return inverseProjectionMatrix;
	}

	public Matrix4f getInverseViewMatrix() {
		return inverseViewMatrix;
	}
	
}
