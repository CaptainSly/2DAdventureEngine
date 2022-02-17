package captainsly.adventure.utils;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import captainsly.adventure.core.render.mesh.Vertex;

public class Utils {

	/* String References */
	public static final String ENGINE_WORKING_DIRECTORY = System.getProperty("user.dir") + "/adventure/";

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	/* Buffer Utility Methods */

	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer createIndicesBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data).flip();
		return buffer;
	}
	
	public static FloatBuffer createMatrixBuffer(Matrix4f data) {
		FloatBuffer buffer = createFloatBuffer(16);
		data.get(buffer);
		
		return buffer;
	}

	public static FloatBuffer createFlippedColorBuffer(Vector3f[] data) {
		FloatBuffer buffer = createFloatBuffer(data.length * 3);
		
		for (int i = 0; i < data.length; i++) {
			buffer.put(data[i].x);
			buffer.put(data[i].y);
			buffer.put(data[i].z);
		}
		
		buffer.flip();
		return buffer;
		
	}
	
	public static FloatBuffer createFlippedVerticesBuffer(Vertex[] data) {
		FloatBuffer buffer = createFloatBuffer(data.length * Vertex.SIZE);

		for (int i = 0; i < data.length; i++) {
			buffer.put(data[i].getPosition().x);
			buffer.put(data[i].getPosition().y);
			buffer.put(data[i].getPosition().z);
		}

		buffer.flip();
		return buffer;

	}

	/* File Utiltiy Methods */

	/**
	 * Load's the Specified File's contents to a String
	 * 
	 * The directory of the file needs to be specified either with the Utils String
	 * references or through a secondary path
	 * 
	 * @param fileName - The ABSOLUTE Path to the File
	 * @return The File's contents as a string
	 */
	public static String loadFileContentsToString(String fileName) {
		StringBuilder sBuilder = new StringBuilder();
		try {
			InputStream fileStream = Utils.class.getResourceAsStream("/" + fileName);
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(fileStream));

			String line = fileReader.readLine();

			while (line != null) {
				sBuilder.append(line + "\n");
				line = fileReader.readLine();
			}

			fileReader.close();
			fileStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sBuilder.toString();
	}

}
