package captainsly.adventure.core.impl;

public interface Scene extends Disposable {

	void onInitialization();

	void onRender(double delta);

	void onInput(double delta);

	void onUpdate(double delta);

}
