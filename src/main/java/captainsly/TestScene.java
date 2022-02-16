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
		Vertex[] data = new Vertex[] { new Vertex(new Vector3f(-1, -1, 0)), new Vertex(new Vector3f(0, 1, 0)),
				new Vertex(new Vector3f(1, -1, 0)) };

		mesh = new Mesh(data);

		try {
			defaultShader = new ShaderProgram(
					Utils.loadFileContentsToString(Utils.ENGINE_ASSET_DIRECTORY + "defaultShader.vs"),
					Utils.loadFileContentsToString(Utils.ENGINE_ASSET_DIRECTORY + "defaultShader.fs"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRender(double delta) {
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
