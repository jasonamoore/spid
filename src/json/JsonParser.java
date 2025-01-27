package json;

public class JsonParser {

	/**
	 * Parses a JSON String and creates a JsonType representing the decoded data.
	 * 
	 * @param json The String to parse.
	 * @return The data decoded as a JsonType object.
	 */
	public static JsonType read(String json) {
		last_end = 0;
		int start_index = json.indexOf('{');
		return read(start_index, json);
	}

	private static int last_end;

	private static JsonType read(int start, String json) {
		// find first character
		char first = ' ';
		int f = start;
		for (; f < json.length(); f++) {
			char c = json.charAt(f);
			if (!Character.isWhitespace(c)) {
				first = c;
				break;
			}
		}
		last_end = f; // start the end at the beginning of the object (will be incremented)
		switch (first) {
		case 'f':
			for (; Character.isAlphabetic(json.charAt(last_end + 1)); last_end++)
				; // increment end until end of "false"
			return new JsonBoolean(false);
		case 't':
			for (; Character.isAlphabetic(json.charAt(last_end + 1)); last_end++)
				; // increment end until end of "true"
			return new JsonBoolean(true);
		case 'n':
			for (; Character.isAlphabetic(json.charAt(last_end + 1)); last_end++)
				; // increment end until end of "null"
			return new JsonNull();
		case '"':
			last_end = json.indexOf('"', f + 1); // end is at the closing quotation marks
			return new JsonString(json.substring(f + 1, last_end));
		case '[':
			// read an array of JSON objects
			JsonArray jarr = new JsonArray();
			for (int i = f + 1; json.charAt(i) != ']'; i++) {
				char c = json.charAt(i);
				if (!Character.isWhitespace(c) && c != ',') {
					jarr.add(read(i, json));
					i = last_end;
					if (json.charAt(i) == ']')
						break; // in case last_end is at ] after reading
				}
			}
			return jarr;
		}
		// if first is numeric
		if (Character.isDigit(first)) {
			for (; Character.isDigit(json.charAt(last_end + 1)); last_end++)
				; // increment end until end of the number
			return new JsonInteger(Integer.parseInt(json.substring(f, last_end + 1)));
		}
		JsonObject obj = new JsonObject();
		int state = 0; // 0 = looking for key, 1 = looking for value
		String cur_key = null;
		int i = start;
		for (; i < json.length() && json.charAt(i) != '}'; i++) {
			// String lookahead = json.substring(i, i + 15);
			char c = json.charAt(i);
			if (c == '"' && state == 0) { // found key
				int close_quote = json.indexOf('"', i + 1);
				cur_key = json.substring(i + 1, close_quote);
				i = close_quote;
				state = 1;

				if (cur_key.equals("images"))
					System.out.flush(); // stop!
			}
			if (c == ':' && state == 1) { // found value
				JsonType value = read(i + 1, json);
				obj.add(cur_key, value);
				i = last_end;
				state = 0;
			}
		}
		last_end = i;
		return obj;
	}

}
