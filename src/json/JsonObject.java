package json;

import java.util.HashMap;

public class JsonObject extends JsonType {

	HashMap<String, JsonType> objects;
	
	@SuppressWarnings("unchecked")
	public JsonObject() {
		super(new HashMap<String, JsonType>(), JSONDataType.Object);
		objects = (HashMap<String, JsonType>) value;
	}

	@Override
	public HashMap<String, JsonType> getValue() {
		return objects;
	}
	
	public void add(String key, JsonType value) {
		objects.put(key, value);
	}
	
	public JsonType get(String key) {
		return objects.get(key);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		Object[] keys = objects.keySet().toArray();
		for (int i = 0; i < keys.length; i++)
			sb.append(String.format("\"%s\": %s", keys[i], objects.get(keys[i])) + (i < keys.length - 1 ? ", " : ""));
		sb.append("}");
		return sb.toString();
	}
	
}
