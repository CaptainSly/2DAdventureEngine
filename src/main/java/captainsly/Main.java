package captainsly;

import captainsly.adventure.core.Engine;
import captainsly.adventure.core.scenes.Editor;

public class Main {

	public static void main(String[] args) {
		Engine engine = new Engine(new Editor());
		engine.run();
	}

}
