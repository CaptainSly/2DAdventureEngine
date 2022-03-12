package captainsly.adventure.core.typeadapters;

import java.lang.reflect.Type;

import com.google.gson.*;

import captainsly.adventure.core.entity.GameObject;
import captainsly.adventure.core.entity.Transform;
import captainsly.adventure.core.entity.components.Component;

public class GameObjectTypeAdapter implements JsonDeserializer<GameObject> {

	@Override
	public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		
		String objectId = jsonObject.get("objectId").getAsString();
		Transform objectTransform = context.deserialize(jsonObject.get("transform"), Transform.class);
		int zIndex = jsonObject.get("zIndex").getAsInt();
		JsonArray objectComponents = jsonObject.getAsJsonArray("components");
		
		GameObject gObj = new GameObject(objectId, objectTransform, zIndex);
		
		for (JsonElement e : objectComponents) {
			Component c = context.deserialize(e, Component.class);
			gObj.addComponent(c);
		}
			
		
		return gObj;
	}

}
