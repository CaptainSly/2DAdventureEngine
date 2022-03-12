package captainsly.adventure;

import java.util.Random;

import org.slf4j.Logger;

import com.google.gson.Gson;

import captainsly.adventure.core.Engine;
import captainsly.adventure.core.Scene;
import captainsly.adventure.core.render.Window;
import captainsly.adventure.core.scripting.AdventureScriptEngine;

public class Adventure {

	public static Engine engine;
	public static Gson gson;
	public static Random rnJesus;
	public static Window window;
	public static Scene currentScene;
	public static AdventureScriptEngine adventureScript;
	public static Logger log;
	
}
