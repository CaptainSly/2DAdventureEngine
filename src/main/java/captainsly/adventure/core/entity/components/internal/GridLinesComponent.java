package captainsly.adventure.core.entity.components.internal;

import org.joml.Vector2f;
import org.joml.Vector3f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.entity.Camera;
import captainsly.adventure.core.entity.components.Component;
import captainsly.adventure.core.render.renderer.DebugRenderer;
import captainsly.adventure.utils.Settings;

/**
 *
 * This is an internal component used by the engine, not to be used by the user
 * 
 */
public class GridLinesComponent extends Component {

	private Vector3f LINE_COLOR = new Vector3f(0.2f, 0.2f, 0.2f);

	@Override
	public void update(double delta) {
		Camera camera = Adventure.getSceneCamera();
		Vector2f cameraPos = camera.cameraPosition;
		Vector2f projectionSize = camera.getProjectionSize();

		
		int firstX = ((int) (cameraPos.x / Settings.GRID_SIZE) - 1) * Settings.GRID_SIZE;
		int firstY = ((int) (cameraPos.y / Settings.GRID_SIZE) - 1) * Settings.GRID_SIZE;

		int numVtLines = (int) (projectionSize.x * camera.getZoom() / Settings.GRID_SIZE) + 2;
		int numHzLines = (int) (projectionSize.y * camera.getZoom() / Settings.GRID_SIZE) + 2;

		int height = (int) (projectionSize.y * camera.getZoom()) + Settings.GRID_SIZE * 2;
		int width = (int) (projectionSize.x * camera.getZoom()) + Settings.GRID_SIZE * 2;

		
		int maxLines = Math.max(numVtLines, numHzLines);
		for (int i = 0; i < maxLines; i++) {

			int x = firstX + (Settings.GRID_SIZE * i);
			int y = firstY + (Settings.GRID_SIZE * i);

			if (i < numVtLines) {
				DebugRenderer.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), LINE_COLOR);
			}

			if (i < numHzLines) {
				DebugRenderer.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), LINE_COLOR);
			}

		}
	}

}
