package captainsly.adventure.core;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import captainsly.adventure.Adventure;
import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.components.Component;
import captainsly.adventure.utils.Utils;

public class SaveManager {

	/**
	 * Saving and Loading GameObjects/Components
	 * 
	 * When loading, make sure the following snippet is included int maxGObjId = -1;
	 * int maxCompId = -1;
	 * 
	 * GameObject[] gObjs = gson.fromJson(save, GameObject[].class); for (int i = 0;
	 * i < gObjs.length) { Adventure.currentScene.addGameObjectsToScene(gObjs[i]);
	 * 
	 * for (Component c : gObjs[i].getComponents()) if (c.getuID() > maxCompId)
	 * maxCompId = c.getuID();
	 *
	 * if (gObjs[i].getuID() > maxGObjId) maxGObjId = gObjs[i].getuID(); }
	 * 
	 * maxGObjId++; maxCompId++;
	 * 
	 * GameObject.init(maxGObjId); Component.init(maxCompId);
	 */

	/*
	 * TODO: Change so it uses the Map System instead of saving GameObjects directly
	 */
	public static void saveMap(String mapName, List<GameObject> mapGObjs) {
		Gson gson = Adventure.gson;

		try {
			FileWriter mapWriter = new FileWriter(Utils.ENGINE_WORKING_DIRECTORY + "data/maps/" + mapName + ".smf");

			List<GameObject> objsToSerialize = new ArrayList<>();
			for (GameObject gObj : mapGObjs) {
				if (gObj.doSerialization())
					objsToSerialize.add(gObj);
			}

			mapWriter.write(gson.toJson(objsToSerialize));
			mapWriter.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	/*
	 * TODO: Change so it loads Maps directly instead of a list of GameObjects
	 */
	public static void load(String mapName) {
		Gson gson = Adventure.gson;
		String mapFile = Utils.loadFileToStringExternal("data/maps/" + mapName + ".smf");

		if (!mapFile.equals("")) {
			int maxGObjId = -1;
			int maxCompId = -1;

			GameObject[] gObjs = gson.fromJson(mapFile, GameObject[].class);

			Adventure.currentScene.getGameObjects().clear();
			Adventure.log.debug("Loading mapFile: " + mapName);
			for (int i = 0; i < gObjs.length; i++) {

				Adventure.currentScene.addGameObjectToScene(gObjs[i]);

				for (Component c : gObjs[i].getComponents())
					if (c.getuID() > maxCompId)
						maxCompId = c.getuID();

				if (gObjs[i].getuID() > maxGObjId)
					maxGObjId = gObjs[i].getuID();
			}

			maxGObjId++;
			maxCompId++;
			GameObject.init(maxGObjId);
			Component.init(maxCompId);
		}
	}

}
