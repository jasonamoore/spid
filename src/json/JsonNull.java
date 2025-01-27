package json;

public class JsonNull extends JsonType {
	
	public JsonNull() {
		super(null, JSONDataType.Null);
	}

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public Object getValue() {
		return null;
	}
	
}