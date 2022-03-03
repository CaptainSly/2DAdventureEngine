package captainsly.adventure.core.scripting;

import java.util.ArrayList;
import java.util.List;

import org.jruby.Ruby;
import org.jruby.embed.ScriptingContainer;

import captainsly.adventure.Adventure;
import captainsly.adventure.utils.Utils;

public class AdventureScriptEngine {

	public final static String SCRIPT_ENGINE_VERSION = "0.1.0";

	private final Ruby ruby;
	private final ScriptingContainer scriptContainer;

	public AdventureScriptEngine() {
		scriptContainer = new ScriptingContainer();
		ruby = scriptContainer.getProvider().getRuntime();

		// Setup JRuby classpath
		List<String> classpaths = new ArrayList<>();
		classpaths.add(scriptContainer.getHomeDirectory());
		classpaths.add(Utils.ENGINE_WORKING_DIRECTORY + "scripts/");
		classpaths.add(System.getProperty("java.class.path"));

		scriptContainer.setLoadPaths(classpaths);

		// Set Common 2DAdventureEngine Global Variables
		scriptContainer.put("aseVersion", SCRIPT_ENGINE_VERSION);
		scriptContainer.put("Log", Adventure.log);
		scriptContainer.put("RnJesus", Adventure.rnJesus);
		scriptContainer.put("AdventureEngine", Adventure.engine);
	}

	public String callMethodSi(String script, String method) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, String.class);
	}

	public String callMethodSi(String script, String method, Object... params) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, params, String.class);
	}

	public String callMethodSe(String script, String method) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, String.class);
	}

	public String callMethodSe(String script, String method, Object... params) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, params, String.class);
	}

	public float callMethodFi(String script, String method) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, Float.class);
	}

	public float callMethodFi(String script, String method, Object... params) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, params, Float.class);
	}

	public float callMethodFe(String script, String method) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, Float.class);
	}

	public float callMethodFe(String script, String method, Object... params) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, params, Float.class);
	}

	public boolean callMethodBi(String script, String method) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, Boolean.class);
	}

	public boolean callMethodBi(String script, String method, Object... params) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, params, Boolean.class);
	}

	public boolean callMethodBe(String script, String method) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, Boolean.class);
	}

	public boolean callMethodBe(String script, String method, Object... params) {
		Object reciever = scriptContainer.runScriptlet(script);
		return scriptContainer.callMethod(reciever, method, params, Boolean.class);
	}

	public void callMethodi(String script, String method) {
		Object reciever = scriptContainer.runScriptlet(script);
		scriptContainer.callMethod(reciever, method);
	}

	public void callMethode(String script, String method) {
		Object reciever = scriptContainer.runScriptlet(script);
		scriptContainer.callMethod(reciever, method);
	}

	public Ruby getRubyContext() {
		return ruby;
	}
	
	public ScriptingContainer getAdventureScriptContainer() {
		return scriptContainer;
	}

}
