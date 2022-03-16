package captainsly.adventure.core;

public class SaveManager {

	/**
	 * Saving and Loading GameObjects/Components
	 * 
	 * When loading, make sure the following snippet is included
	 * int maxGObjId = -1;
	 * int maxCompId = -1;
	 * 
	 * GameObject[] gObjs = gson.fromJson(save, GameObject[].class);
	 * for (int i = 0; i < gObjs.length) {
	 * 	Adventure.currentScene.addGameObjectsToScene(gObjs[i]);
	 * 
	 * 	for (Component c : gObjs[i].getComponents())
	 *		if (c.getuID() > maxCompId)
	 *			maxCompId = c.getuID();
	 *
	 *	if (gObjs[i].getuID() > maxGObjId)
	 *		maxGObjId = gObjs[i].getuID();
	 * } 
	 * 
	 * maxGObjId++;
	 * maxCompId++;
	 * 
	 * GameObject.init(maxGObjId);
	 * Component.init(maxCompId);
	 */
	
}
