package json;

import java.util.ArrayList;

public class JsonArray extends JsonType {

	private int size;
	private ArrayList<JsonType> array;

	@SuppressWarnings("unchecked")
	public JsonArray() {
		super(new ArrayList<JsonType>(), JSONDataType.Array);
		array = (ArrayList<JsonType>) value;
		size = array.size();
	}

	@Override
	public ArrayList<JsonType> getValue() {
		return array;
	}
	
	public void add(JsonType obj) {
		array.add(obj);
		size++;
	}
	
	public JsonType get(int i) {
		return array.get(i);
	}
	
	public int size() {
		return size;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < array.size(); i++)
			sb.append(array.get(i) + (i < array.size() - 1 ? ", " : ""));
		sb.append("]");
		return sb.toString();
	}
	
}
