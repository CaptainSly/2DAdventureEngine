package captainsly.adventure.core;

import java.util.HashMap;
import java.util.Map;

import captainsly.adventure.core.render.Texture;
import captainsly.adventure.core.render.shaders.ShaderProgram;
import captainsly.adventure.core.render.sprite.SpriteSheet;
import captainsly.adventure.utils.Utils;

public class AssetPool {

	private static Map<String, ShaderProgram> shaderProgramMap = new HashMap<>();
	private static Map<String, Texture> texturesMap = new HashMap<>();
	private static Map<String, String> scriptMap = new HashMap<>();
	private static Map<String, SpriteSheet> spriteSheetsMap = new HashMap<>();

	public static ShaderProgram getShader(String shaderResourceName) {
		if (shaderProgramMap.containsKey(shaderResourceName)) {
			return shaderProgramMap.get(shaderResourceName);
		} else {
			createShaderAsset(shaderResourceName, "assets/shaders/" + shaderResourceName + ".vs",
					"assets/shaders/" + shaderResourceName + ".fs");
			return getShader(shaderResourceName);
		}
	}

	public static void createShaderAsset(String shaderResourceName, String vShader, String fShader) {
		try {
			ShaderProgram sp = new ShaderProgram(vShader, fShader);
			shaderProgramMap.put(shaderResourceName, sp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void addSpriteSheet(String spriteSheetResourceName, SpriteSheet spriteSheet) {
		spriteSheetsMap.put(spriteSheetResourceName, spriteSheet);
	}

	public static SpriteSheet getSpriteSheet(String spriteSheetResourceName) {
		if (spriteSheetsMap.containsKey(spriteSheetResourceName))
			return spriteSheetsMap.get(spriteSheetResourceName);

		return null;
	}

	public static String getScript(String scriptResourceName) {
		if (scriptMap.containsKey(scriptResourceName)) {
			return scriptMap.get(scriptResourceName);
		} else {
			String script = Utils.loadFileToStringInternal("assets/scripts/" + scriptResourceName + ".rb");
			scriptMap.put(scriptResourceName, script);
			return getScript(scriptResourceName);
		}
	}

	public static Texture getTexture(String textureResourceName) {
		if (texturesMap.containsKey(textureResourceName)) {
			return texturesMap.get(textureResourceName);
		} else {
			// Create new texture
			Texture texture = new Texture("assets/textures/" + textureResourceName + ".png");
			texturesMap.put(textureResourceName, texture);
			return getTexture(textureResourceName);
		}
	}

}
