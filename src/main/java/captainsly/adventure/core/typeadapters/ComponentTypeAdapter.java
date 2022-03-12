package captainsly.adventure.core.typeadapters;

import java.lang.reflect.Type;

import com.google.gson.*;

import captainsly.adventure.core.entity.components.Component;

public class ComponentTypeAdapter implements JsonSerializer<Component>, JsonDeserializer<Component> {

	@Override
	public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		String componentType = jsonObject.get("componentType").getAsString();
		JsonElement element = jsonObject.get("componentProperties");
		
		try {
			return context.deserialize(element, Class.forName(componentType));
		} catch (ClassNotFoundException e) {
			throw new JsonParseException("Uknown element type: " + componentType, e);
		}
	}

	@Override
	public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add("componentType", new JsonPrimitive(src.getClass().getCanonicalName()));
		result.add("componentProperties", context.serialize(src, src.getClass()));
		return result;
	}

}
