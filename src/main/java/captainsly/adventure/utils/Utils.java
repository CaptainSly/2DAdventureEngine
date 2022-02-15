package captainsly.adventure.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import captainsly.adventure.core.render.mesh.Vertex;

public class Utils {

	/* String References */
	public static final String ENGINE_ASSET_DIRECTORY = "src/main/resources/";
	public static final String ENGINE_WORKING_DIRECTORY = System.getProperty("user.dir") + "/adventure/";

	/* Buffer Utility Methods */
	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
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
		File file = new File(fileName);
		StringBuilder sBuilder = new StringBuilder();
		if (file.exists()) {
			try {
				BufferedReader fileReader = new BufferedReader(new FileReader(file));

				String line = fileReader.readLine();

				while (line != null) {
					sBuilder.append(line + "\n");
					line = fileReader.readLine();
				}

				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return sBuilder.toString();
	}

}
