package captainsly.adventure.core.render.mesh;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import captainsly.adventure.utils.Utils;

public class Mesh {

	private int vbo, size;

	public Mesh() {
		vbo = glGenBuffers();
		size = 0;
	}

	public void addVertices(Vertex[] vertices) {
		size = vertices.length * Vertex.SIZE;

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, Utils.createFlippedVerticesBuffer(vertices), GL_STATIC_DRAW);
	}

	public void draw() {
		glEnableVertexAttribArray(0);

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);

		glDrawArrays(GL_TRIANGLES, 0, size);

		glDisableVertexAttribArray(0);
	}

}
