package captainsly.adventure.utils;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

import java.io.*;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

public class Utils {

	/* String References */
	public static final String ENGINE_WORKING_DIRECTORY = System.getProperty("user.dir") + "/adventure/";
	public static final String ENGINE_VERSION = "0.1.3";

	
	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	/* Buffer Utility Methods */
	public static FloatBuffer createMatrixBuffer(Matrix4f data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		data.get(buffer);

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
	public static String loadFileToStringInternal(String fileName) {
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

	public static String loadFileToStringExternal(String fileName) {
		StringBuilder sBuilder = new StringBuilder();
		try {
			File f = new File(Utils.ENGINE_WORKING_DIRECTORY + fileName);
			BufferedReader fileReader = new BufferedReader(new FileReader(f));

			String line = fileReader.readLine();

			while (line != null) {
				sBuilder.append(line + "\n");
				line = fileReader.readLine();
			}

			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sBuilder.toString();
	}

	public static File loadFileInternal(String fileName) {
		File f = new File(Utils.class.getResource(fileName).getFile());
		if (f.exists())
			return f;
		return null;
	}

	public static File loadFileExternal(String fileName) {
		File f = new File(fileName);
		if (f.exists())
			return f;
		return null;
	}

	public static void createEngineFileStructure() {
		File engineFolder = new File(ENGINE_WORKING_DIRECTORY);
		engineFolder.mkdir();
	}

	public static boolean compareVector(Vector2d c1, Vector2d c2) {
		return c1.x == c2.x && c1.y == c2.y;
	}
	
	public static boolean compareVector(Vector2f c1, Vector2f c2) {
		return c1.x == c2.x && c1.y == c2.y;
	}
	
	public static boolean compareVector(Vector2i c1, Vector2i c2) {
		return c1.x == c2.x && c1.y == c2.y;
	}

}
