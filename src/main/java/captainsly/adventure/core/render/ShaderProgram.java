package captainsly.adventure.core.render;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import captainsly.adventure.core.impl.Disposable;

public class ShaderProgram implements Disposable {

	private final int shaderProgramId;
	private final int vertexShaderId, fragmentShaderId;

	private Map<String, Integer> uniformMap;

	public ShaderProgram(String vShader, String fShader) throws Exception {
		uniformMap = new HashMap<>();
		shaderProgramId = glCreateProgram();
		if (shaderProgramId == 0)
			throw new Exception("Could not create Shader");

		vertexShaderId = createShader(vShader, GL_VERTEX_SHADER);
		fragmentShaderId = createShader(fShader, GL_FRAGMENT_SHADER);
		link();
	}

	private int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);

		if (shaderId == 0)
			throw new Exception("Error creating shader type: " + getShaderType(shaderType));

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader Code from type: " + getShaderType(shaderType) + ": "
					+ glGetShaderInfoLog(shaderId, 1024));
		}

		glAttachShader(shaderProgramId, shaderId);
		return shaderId;
	}

	private void link() throws Exception {
		glLinkProgram(shaderProgramId);
		if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == 0)
			throw new Exception("Error Linking Shader Code: " + glGetProgramInfoLog(shaderProgramId, 1024));

		if (vertexShaderId != 0)
			glDetachShader(shaderProgramId, vertexShaderId);

		if (fragmentShaderId != 0)
			glDetachShader(shaderProgramId, fragmentShaderId);

		glValidateProgram(shaderProgramId);
		if (glGetProgrami(shaderProgramId, GL_VALIDATE_STATUS) == 0)
			throw new Exception("Warning validating Shader code: " + glGetProgramInfoLog(shaderProgramId, 1024));

	}

	public static String getShaderType(int shaderType) {
		switch (shaderType) {
		case GL_VERTEX_SHADER:
			return "Vertex Shader";
		case GL_FRAGMENT_SHADER:
			return "Fragment Shader";
		}

		return "*BUG* - Unknown Shader - *BUG*";
	}

	public void bind() {
		glUseProgram(shaderProgramId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	@Override
	public void onDispose() {
		unbind();

		if (shaderProgramId != 0)
			glDeleteProgram(shaderProgramId);
	}

}
