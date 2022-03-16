package captainsly.adventure.core.render;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {

	private Vector2f lineStart, lineEnd;
	private Vector3f lineColor;
	private int lineLifetime;

	public Line2D(Vector2f lineStart, Vector2f lineEnd, Vector3f lineColor, int lineLifetime) {
		this.lineStart = lineStart;
		this.lineEnd = lineEnd;
		this.lineColor = lineColor;
		this.lineLifetime = lineLifetime;
	}
	
	public int beginFrame() {
		this.lineLifetime--;
		return lineLifetime;
	}

	public Vector2f getLineStart() {
		return lineStart;
	}

	public Vector2f getLineEnd() {
		return lineEnd;
	}

	public Vector3f getLineColor() {
		return lineColor;
	}

	public int getLineLifetime() {
		return lineLifetime;
	}

}
