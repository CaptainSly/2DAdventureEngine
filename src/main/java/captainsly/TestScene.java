package captainsly;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

import org.joml.Vector3f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.impl.Scene;
import captainsly.adventure.core.input.KeyListener;
import captainsly.adventure.core.render.mesh.Mesh;
import captainsly.adventure.core.render.mesh.Vertex;
import captainsly.adventure.core.render.shaders.ShaderProgram;
import captainsly.adventure.utils.Utils;

public class TestScene implements Scene {

	private Mesh mesh;
	private ShaderProgram defaultShader;

	@Override
	public void onInitialization() {
		Vertex[] vertices = new Vertex[] { new Vertex(new Vector3f(-0.5f, 0.5f, 0.0f)),
				new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f)), new Vertex(new Vector3f(0.5f, -0.5f, 0.0f)),
				new Vertex(new Vector3f(0.5f, 0.5f, 0.0f)) };

		int[] indices = new int[] { 0, 1, 3, 3, 1, 2 };

		Vector3f[] color = new Vector3f[] { new Vector3f(0.5f, 0.0f, 0.0f), new Vector3f(0.0f, 0.5f, 0.0f),
				new Vector3f(0.0f, 0.0f, 0.5f), new Vector3f(0.5f, 0.5f, 0.5f) };

		mesh = new Mesh(vertices, indices, color);

		try {
			defaultShader = new ShaderProgram(Utils.loadFileContentsToString("defaultShader.vs"),
					Utils.loadFileContentsToString("defaultShader.fs"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRender(double delta) {
		Adventure.engine.render(1, 1, 1);

		defaultShader.bind();
		mesh.draw();
		defaultShader.unbind();
	}

	@Override
	public void onUpdate(double delta) {
	}

	@Override
	public void onInput(double delta) {

		if (KeyListener.isKeyDown(GLFW_KEY_A)) {
			Adventure.log.debug("TEST DOWN");
		}

	}

	@Override
	public void onDispose() {
		mesh.onDispose();
	}

}
