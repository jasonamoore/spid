package json;

public class JsonBoolean extends JsonType {

	public JsonBoolean(boolean value) {
		super(value, JSONDataType.Boolean);
	}

	@Override
	public Boolean getValue() {
		return (boolean) value;
	}
	
}
