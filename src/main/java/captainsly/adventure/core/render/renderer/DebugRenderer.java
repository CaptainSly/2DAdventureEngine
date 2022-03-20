package captainsly.adventure.core.render.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.AssetPool;
import captainsly.adventure.core.render.Line2D;
import captainsly.adventure.core.render.shaders.Shader;

public class DebugRenderer {

	private static int MAX_LINES = 500;

	private static List<Line2D> lines = new ArrayList<>();
	// 6 floats per vertex, 3 pos, 3 color. 2 vertices per line
	private static float[] vertexArray = new float[MAX_LINES * 6 * 2];
	private static Shader shader = AssetPool.getShader("debugShader");

	private static int vaoId, vboId;

	private static boolean started = false;

	static {
		try {
			shader.addUniform("uViewMatrix");
			shader.addUniform("uProjectionMatrix");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void start() {
		// Generate VAO
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		vboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferData(GL_ARRAY_BUFFER, vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);

		// Enable Vertex Attribs
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);

		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
		glEnableVertexAttribArray(1);

		glLineWidth(2.0f);

	}

	public static void beginFrame() {
		if (!started) {
			start();
			started = true;
		}

		// Remove Dead Lines
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).beginFrame() < 0) {
				lines.remove(i);
				i--;
			}
		}
	}

	public static void draw() {
		if (lines.size() <= 0)
			return;

		int index = 0;
		for (Line2D line : lines) {
			for (int i = 0; i < 2; i++) {
				Vector2f pointPos = i == 0 ? line.getLineStart() : line.getLineEnd();
				Vector3f color = line.getLineColor();

				// Load Position
				vertexArray[index] = pointPos.x;
				vertexArray[index + 1] = pointPos.y;
				vertexArray[index + 2] = -10.0f;

				// Load Color
				vertexArray[index + 3] = color.x;
				vertexArray[index + 4] = color.y;
				vertexArray[index + 5] = color.z;

				index += 6;
			}
		}

		glBindBuffer(GL_ARRAY_BUFFER, vboId);
		glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2));

		shader.bind();
		shader.setUniform("uProjectionMatrix", Adventure.currentScene.getSceneCamera().getProjectionMatrix());
		shader.setUniform("uViewMatrix", Adventure.currentScene.getSceneCamera().getViewMatrix());

		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);

		glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);

		shader.unbind();
	}

	// ==========
	// Add Line2D
	// ==========
	public static void addLine2D(Vector2f from, Vector2f to) {
		addLine2D(from, to, new Vector3f(0, 1, 0), 1);
	}

	public static void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
		addLine2D(from, to, color, 1);
	}

	public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifeTime) {
		if (lines.size() >= MAX_LINES)
			return;

		DebugRenderer.lines.add(new Line2D(from, to, color, lifeTime));
	}

	// =========
	// Add Box2D
	// =========
	public static void addBox2D(Vector2f center, Vector2f dimensions) {
		// TODO: ADD CONSTANTS FOR COMMON COLORS
		addBox2D(center, dimensions, new Vector3f(0, 1, 0), 1);
	}

	public static void addBox2D(Vector2f center, Vector2f dimensions, Vector3f color) {
		addBox2D(center, dimensions, color, 1);
	}

	public static void addBox2D(Vector2f center, Vector2f dimensions, Vector3f color, int lifetime) {
		Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
		Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

		Vector2f[] vertices = { new Vector2f(min.x, min.y), new Vector2f(min.x, max.y), new Vector2f(max.x, max.y),
				new Vector2f(max.x, min.y) };

		addLine2D(vertices[0], vertices[1], color, lifetime);
		addLine2D(vertices[0], vertices[3], color, lifetime);
		addLine2D(vertices[1], vertices[2], color, lifetime);
		addLine2D(vertices[2], vertices[3], color, lifetime);
	}
	
	

}
