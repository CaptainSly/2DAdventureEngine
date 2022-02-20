package captainsly;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.entity.OrthographicCamera;
import captainsly.adventure.core.impl.Scene;
import captainsly.adventure.core.input.KeyListener;
import captainsly.adventure.core.input.MouseListener;
import captainsly.adventure.core.render.mesh.Mesh;
import captainsly.adventure.core.render.mesh.Texture;
import captainsly.adventure.core.render.mesh.Vertex;
import captainsly.adventure.core.render.shaders.ShaderProgram;
import captainsly.adventure.utils.Utils;

public class TestScene implements Scene {

	private Mesh mesh;
	private Texture texture;
	private OrthographicCamera camera;
	private ShaderProgram defaultShader;

	@Override
	public void onInitialization() {
		camera = new OrthographicCamera(new Vector2f());
		Vertex[] vertices = new Vertex[] { new Vertex(100.5f, 0.5f, 0.0f), new Vertex(0.5f, 100.5f, 0.0f),
				new Vertex(100.5f, 100.5f, 0.0f), new Vertex(0.5f, 0.5f, 0.0f) };

		int[] indices = new int[] { 2, 1, 0, 0, 1, 3 };

		Vector3f[] color = new Vector3f[] { new Vector3f(0.5f, 0.0f, 0.0f), new Vector3f(0.0f, 0.5f, 0.0f),
				new Vector3f(0.0f, 0.0f, 0.5f), new Vector3f(0.5f, 0.5f, 0.5f) };

		Vector2i[] uvCoords = new Vector2i[] { new Vector2i(1, 0), new Vector2i(0, 1), new Vector2i(1, 1),
				new Vector2i(0, 0) };

		texture = new Texture("src/main/resources/test.png");
		mesh = new Mesh(vertices, indices, color, uvCoords);

		try {
			defaultShader = new ShaderProgram(Utils.loadFileToStringInternal("defaultShader.vs"),
					Utils.loadFileToStringInternal("defaultShader.fs"));
			defaultShader.addUniform("uProjectionMatrix");
			defaultShader.addUniform("uViewMatrix");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onRender(double delta) {
		Adventure.engine.render(0, 0, 0);

		defaultShader.bind();

		defaultShader.setUniform("uProjectionMatrix", camera.getProjectionMatrix());
		defaultShader.setUniform("uViewMatrix", camera.getViewMatrix());

		texture.bind();
		mesh.draw();
		texture.unbind();

		defaultShader.unbind();
	}

	@Override
	public void onUpdate(double delta) {
	}

	@Override
	public void onInput(double delta) {

		if (KeyListener.isKeyDown(GLFW_KEY_A)) {
			Adventure.log.debug("A Key Down");
		}
		
		if (MouseListener.isButtonDown(GLFW_MOUSE_BUTTON_LEFT))
			Adventure.log.debug("Left Mouse Button Down");
		
	}

	@Override
	public void onDispose() {
		mesh.onDispose();
	}

}
