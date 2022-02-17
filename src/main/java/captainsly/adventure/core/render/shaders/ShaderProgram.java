package captainsly.adventure.core.render.shaders;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.impl.Disposable;
import captainsly.adventure.utils.Utils;

public class ShaderProgram implements Disposable {

	private final int shaderProgramId;
	private final int vShaderId, fShaderId;

	private Map<String, Integer> uniformMap;

	public ShaderProgram(String vShader, String fShader) throws Exception {
		shaderProgramId = glCreateProgram();
		uniformMap = new HashMap<>();

		if (shaderProgramId == 0)
			throw new Exception("Could not create shader");

		vShaderId = createShader(vShader, GL_VERTEX_SHADER);
		fShaderId = createShader(fShader, GL_FRAGMENT_SHADER);
		link();
	}

	public static String getShaderType(int shaderType) {
		switch (shaderType) {
		case GL_VERTEX_SHADER:
			return "Vertex Shader";
		case GL_FRAGMENT_SHADER:
			return "Fragment Shader";
		case GL_GEOMETRY_SHADER:
			return "Geometry Shader";
		}

		return "*BUG* - Unknown Shader - *BUG*";
	}

	private int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0)
			throw new Exception("Error creating shader. Type: " + getShaderType(shaderType));

		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);

		if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
			throw new Exception("Error compiling Shader code from Shader Type:  " + getShaderType(shaderType) + ", "
					+ glGetShaderInfoLog(shaderId, 1024));

		glAttachShader(shaderProgramId, shaderId);
		return shaderId;
	}

	private void link() throws Exception {
		glLinkProgram(shaderProgramId);
		if (glGetProgrami(shaderProgramId, GL_LINK_STATUS) == 0)
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(shaderProgramId, 1024));

		if (vShaderId != 0)
			glDetachShader(shaderProgramId, vShaderId);

		if (fShaderId != 0)
			glDetachShader(shaderProgramId, fShaderId);

		glValidateProgram(shaderProgramId);
		if (glGetProgrami(shaderProgramId, GL_VALIDATE_STATUS) == 0)
			Adventure.log.warn("Warning validating Shader Code: " + glGetProgramInfoLog(shaderProgramId, 1024));
	}

	public void bind() {
		glUseProgram(shaderProgramId);
	}

	public void unbind() {
		glUseProgram(0);
	}

	// -- Uniform Methods --
	public void addUniform(String uniformName) throws Exception {
		int uniformLocation = glGetUniformLocation(shaderProgramId, uniformName);

		if (uniformLocation < 0)
			throw new Exception("Could not find uniform location for uniform named: " + uniformName);

		uniformMap.put(uniformName, uniformLocation);
	}

	public void setUniform(String uniformName, float value) {
		glUniform1f(getUniform(uniformName), value);
	}

	public void setUniform(String uniformName, float value, float value2) {
		glUniform2f(getUniform(uniformName), value, value2);
	}

	public void setUniform(String uniformName, float value, float value2, float value3) {
		glUniform3f(getUniform(uniformName), value, value2, value3);
	}

	public void setUniform(String uniformName, float value, float value2, float value3, float value4) {
		glUniform4f(getUniform(uniformName), value, value2, value3, value4);
	}

	public void setUniform(String uniformName, Vector2f value) {
		glUniform2f(getUniform(uniformName), value.x, value.y);
	}

	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(getUniform(uniformName), value.x, value.y, value.z);
	}

	public void setUniform(String uniformName, Vector4f value) {
		glUniform4f(getUniform(uniformName), value.x, value.y, value.z, value.w);
	}

	public void setUniform(String uniformName, int value) {
		glGetUniformi(getUniform(uniformName), value);
	}

	public void setUniform(String uniformName, boolean value) {
		glGetUniformi(getUniform(uniformName), value ? 1 : 0);
	}

	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4fv(getUniform(uniformName), false, Utils.createMatrixBuffer(value));
	}

	public int getUniform(String uniformName) {
		return uniformMap.get(uniformName);
	}

	@Override
	public void onDispose() {
		unbind();

		if (shaderProgramId != 0)
			glDeleteProgram(shaderProgramId);
	}

}
