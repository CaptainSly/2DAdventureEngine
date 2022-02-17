package captainsly.adventure.core.impl;

public interface Scene extends Disposable {

	void onInitialization();
	
	/**
	 * Must have a call to <code>Adventure.engine.render(r, g, b)</code> as it clears the screen and set's the clear color
	 */
	void onRender(double delta);

	void onInput(double delta);

	void onUpdate(double delta);

}
