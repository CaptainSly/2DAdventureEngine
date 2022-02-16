package captainsly.adventure.core.render.mesh;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.utils.Utils;

public class Mesh implements Disposable {

	private int pVboId;
	private int vertexSize;

	public Mesh(Vertex[] vertices) {
		vertexSize = vertices.length * Vertex.SIZE;

		// Position Data
		pVboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, pVboId);
		glBufferData(GL_ARRAY_BUFFER, Utils.createFlippedVerticesBuffer(vertices), GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	public void draw() {
		glEnableVertexAttribArray(0);
		glDrawArrays(GL_TRIANGLES, 0, vertexSize);		
		glDisableVertexAttribArray(0);
	}

	@Override
	public void onDispose() {
	}

}
