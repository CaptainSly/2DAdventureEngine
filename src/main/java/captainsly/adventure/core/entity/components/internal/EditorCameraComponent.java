package captainsly.adventure.core.entity.components.internal;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

import org.joml.Vector2f;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.entity.Camera;
import captainsly.adventure.core.entity.components.Component;
import captainsly.adventure.core.input.KeyListener;
import captainsly.adventure.core.input.MouseListener;

public class EditorCameraComponent extends Component {

	private float dragDebounce = 0.008f;

    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private boolean reset = false;

    private float lerpTime = 0.0f;
    private float dragSensitivity = 30.0f;
    private float scrollSensitivity = 0.1f;

    public EditorCameraComponent(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void update(double dt) {
    	float d = (float) dt;
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0) {
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= d;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            levelEditorCamera.cameraPosition.sub(delta.mul(d).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, d);
        }

        if (dragDebounce <= 0.0f && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            dragDebounce = 0.008f;
        }

        if (MouseListener.getScrollY() != 0.0f) {
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
                    1 / levelEditorCamera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());
            levelEditorCamera.addZoom(addValue);
        }
        
        if (!Adventure.guiLayer.getGamePort().doesWantCapture()) {
        	MouseListener.setButtonUp(GLFW_MOUSE_BUTTON_MIDDLE);
        }

        if (KeyListener.isKeyDown(GLFW_KEY_R)) {
            reset = true;
        }

        if (reset) {
            levelEditorCamera.cameraPosition.lerp(new Vector2f(), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() +
                    ((1.0f - levelEditorCamera.getZoom()) * lerpTime));
            this.lerpTime += 0.1f * dt;
            if (Math.abs(levelEditorCamera.cameraPosition.x) <= 5.0f &&
                    Math.abs(levelEditorCamera.cameraPosition.y) <= 5.0f) {
                this.lerpTime = 0.0f;
                levelEditorCamera.cameraPosition.set(0f, 0f);
                this.levelEditorCamera.setZoom(1.0f);
                reset = false;
            }
        }
    }
    
}
