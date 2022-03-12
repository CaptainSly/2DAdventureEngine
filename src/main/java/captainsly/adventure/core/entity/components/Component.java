package captainsly.adventure.core.entity.components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.joml.Vector3f;
import org.joml.Vector4f;

import captainsly.adventure.core.entity.GameObject;
import imgui.ImGui;

public abstract class Component {

	public transient GameObject gameObject = null;

	public void start() {
	}

	@SuppressWarnings("rawtypes")
	public void imgui() {
		try {
			Field[] fields = this.getClass().getFields();

			for (Field field : fields) {
				boolean isPrivate = Modifier.isPrivate(field.getModifiers());
				boolean isTransient = Modifier.isTransient(field.getModifiers());

				if (isTransient)
					continue;
				
				if (isPrivate)
					field.setAccessible(true);

				Class type = field.getType();
				Object value = field.get(this);
				String name = field.getName();

				if (type == int.class) {
					int val = (int) value;
					int[] imInt = { val };
					if (ImGui.dragInt(name + ": ", imInt)) {
						field.set(this, imInt[0]);
					}
				} else if (type == float.class) {
					float val = (float) value;
					float[] imFloat = { val };
					if (ImGui.dragFloat(name + ": ", imFloat))
						field.set(this, imFloat[0]);
				} else if (type == boolean.class) {
					boolean val = (boolean) value;
					if (ImGui.checkbox(name + ": ", val))
						field.set(this, !val);
				} else if (type == Vector3f.class) {
					Vector3f val = (Vector3f) value;
					float[] imVec = { val.x, val.y, val.z };
					if (ImGui.dragFloat3(name + ": ", imVec))
						val.set(imVec[0], imVec[1], imVec[2]);
				} else if (type == Vector4f.class) {
					Vector4f val = (Vector4f) value;
					float[] imVec = { val.x, val.y, val.z, val.w };
					if (ImGui.dragFloat4(name + ": ", imVec))
						val.set(imVec[0], imVec[1], imVec[2], imVec[4]);
				}

				if (isPrivate)
					field.setAccessible(false);
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void update(double delta) {
	}

}
