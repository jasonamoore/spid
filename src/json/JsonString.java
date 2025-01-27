package json;

public class JsonString extends JsonType {

	public JsonString(String value) {
		super(value, JSONDataType.String);
	}
	
	@Override
	public String toString() {
		return String.format("\"%s\"", value);
	}

	@Override
	public String getValue() {
		return (String) value;
	}

}
