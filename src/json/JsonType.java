package json;

public abstract class JsonType {

	public enum JSONDataType {Object, Array, Integer, String, Boolean, Null};
	
	protected Object value;
	protected JSONDataType type;
	
	protected JsonType(Object value, JSONDataType type) {
		this.value = value;
		this.type = type;
	}
	
	public abstract Object getValue();
	
	public boolean isObject() {
		return type == JSONDataType.Object;
	}
	
	public boolean isArray() {
		return type == JSONDataType.Array;
	}

	public boolean isInteger() {
		return type == JSONDataType.Integer;
	}

	public boolean isString() {
		return type == JSONDataType.String;
	}

	public boolean isBoolean() {
		return type == JSONDataType.Boolean;
	}

	public boolean isNull() {
		return type == JSONDataType.Null;
	}
	
	/*
	public HashMap<String, JSONItem> asHashMap() throws JSONMismatchException {
		try {
			return ((JSONObject) this).getValue();
		} catch (ClassCastException e) {
			throw new JSONMismatchException(String.format("Cannot cast %s to JSONObject", type.toString()));
		}
	}

	public ArrayList<JSONItem> asArrayList() throws JSONMismatchException {
		try {
			return ((JSONArray) this).getValue();
		} catch (ClassCastException e) {
			throw new JSONMismatchException(String.format("Cannot cast %s to JSONArray", type.toString()));
		}
	}
	
	public int asInteger() throws JSONMismatchException {
		try {
			return ((JSONInteger) this).getValue();
		} catch (ClassCastException e) {
			throw new JSONMismatchException(String.format("Cannot cast %s to JSONInteger", type.toString()));
		}
	}
	
	public String asString() throws JSONMismatchException {
		try {
			return ((JSONString) this).getValue();
		} catch (ClassCastException e) {
			throw new JSONMismatchException(String.format("Cannot cast %s to JSONString", type.toString()));
		}
	}
	
	public boolean asBoolean() throws JSONMismatchException {
		try {
			return ((JSONBoolean) this).getValue();
		} catch (ClassCastException e) {
			throw new JSONMismatchException(String.format("Cannot cast %s to JSONBoolean", type.toString()));
		}
	}
	*/
	
	@Override
	public String toString() {
		return value.toString();
	}
	
}