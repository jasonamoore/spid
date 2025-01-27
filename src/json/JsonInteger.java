package json;

public class JsonInteger extends JsonType {
	
	public JsonInteger(int value) {
		super(value, JSONDataType.Integer);
	}

	@Override
	public Integer getValue() {
		return (int) value;
	}
	
}
