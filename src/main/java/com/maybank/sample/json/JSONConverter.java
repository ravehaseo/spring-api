package com.maybank.sample.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.maybank.sample.util.FormatterUtil;

@Deprecated
public final class JSONConverter {

	private JSONConverter() {

	}

	private static Gson create() {
		GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setDateFormat(FormatterUtil.DATETIMEMILLIS);

		return gsonBuilder.create();

	}

	public static String toJson(final Object dto) {
		return create().toJson(dto);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object fromJSON(final String serializedDto, final Class clazz) {
		return create().fromJson(serializedDto, clazz);
	}

	public static String formatJSON(final String jsonString) throws IllegalArgumentException {
		String prettyJson = null;

		try {
			// enabling pretty printing for easier debugging
			JsonParser parser = new JsonParser();
			JsonObject rootObj = parser.parse(jsonString).getAsJsonObject();

			String msgString = rootObj.get("message").getAsString();
			JsonElement msgObj = parser.parse(msgString).getAsJsonObject();

			rootObj.remove("message");
			rootObj.add("message", msgObj);

			prettyJson = create().toJson(rootObj);
		} catch (Exception e) {
			throw new IllegalArgumentException("JSON format is invalid", e);
		}
		return prettyJson;
	}

}
