package captainsly;

import captainsly.adventure.core.Engine;

public class Main {

	public static void main(String[] args) {
		Engine engine = new Engine(new TestScene());
		engine.run();
	}

}
