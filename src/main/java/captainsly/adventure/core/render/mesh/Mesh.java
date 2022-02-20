package captainsly.adventure.core.render.mesh;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Vector2i;
import org.joml.Vector3f;

import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.utils.Utils;

public class Mesh implements Disposable {

	private int vaoId, pVboId, iVboId, cVboId, tVboId;
	private int vertexSize;

	public Mesh(Vertex[] vertices, int[] indices, Vector3f[] colors, Vector2i[] uvCoords) {

		vertexSize = indices.length;

		// Vertex Array Object Setup
		vaoId = glGenVertexArrays();
		glBindVertexArray(vaoId);

		// Position Data
		pVboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, pVboId);
		glBufferData(GL_ARRAY_BUFFER, Utils.createFlippedVerticesBuffer(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// Indices
		iVboId = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iVboId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Utils.createIndicesBuffer(indices), GL_STATIC_DRAW);

		// Colors
		cVboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, cVboId);
		glBufferData(GL_ARRAY_BUFFER, Utils.createFlippedColorBuffer(colors), GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

		// uvCoords
		tVboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, tVboId);
		glBufferData(GL_ARRAY_BUFFER, Utils.createFlippedUvCoordsBuffer(uvCoords), GL_STATIC_DRAW);
		glVertexAttribPointer(2, 2, GL_INT, false, 0, 0);

		glBindVertexArray(0);
	}

	public void draw() {
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		glDrawElements(GL_TRIANGLES, vertexSize, GL_UNSIGNED_INT, 0);

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	@Override
	public void onDispose() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(pVboId);
		glDeleteBuffers(iVboId);
		glDeleteBuffers(cVboId);
		glDeleteBuffers(tVboId);

		glBindVertexArray(0);
		glDeleteVertexArrays(vaoId);
	}

}
