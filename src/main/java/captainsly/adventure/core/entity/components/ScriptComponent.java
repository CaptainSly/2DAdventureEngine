package captainsly.adventure.core.entity.components;

import captainsly.adventure.Adventure;
import imgui.ImGui;

public class ScriptComponent extends Component {

	private String script = "";

	public ScriptComponent() {
	}

	public ScriptComponent(String script) {
		this.script = script;
	}

	@Override
	public void imgui() {
		if (ImGui.button("Test"))
			onActivate();
	}

	public void onActivate() {
		Adventure.log.debug("Test");
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

}
