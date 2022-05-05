package com.example.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.common.constants.GlobalConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StringUtils {
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.isEmpty();
	}

	public static String concatStrings(String delimiter, String... strings) {
		StringBuilder builder = new StringBuilder(strings[0]);
		for (int i = 1; i < strings.length; i++) {
			builder.append(delimiter).append(strings[i]);
		}
		return builder.toString();
	}

	public static String getQueueName(String... queueName) {
		return StringUtils.concatStrings(GlobalConstant.DASH, queueName);
	}

	public static String getExchange(String serviceName, String exchange) {
		return StringUtils.concatStrings(GlobalConstant.DOT, GlobalConstant.DEMO_STRING, serviceName, exchange);
	}

	public static String getAction(String... action) {
		return StringUtils.concatStrings(GlobalConstant.DASH, action);
	}

	public static String getRoutingKey(String serviceName, String action, String charsAfter) {
		return StringUtils.concatStrings(GlobalConstant.DOT, GlobalConstant.DEMO_STRING, serviceName, action,
				charsAfter);
	}

	public static String getJsonArrayStringFromList(List<String> strings) {
		if (Objects.nonNull(strings) && !strings.isEmpty()) {
			Gson gson = new Gson();
			return gson.toJson(strings);
		} else {
			return GlobalConstant.EMPTY_ARRAY_STRING;
		}
	}

	public static List<String> getListStringFromString(String json) {
		// try if the string is json format
		List<String> result = new ArrayList<>();
		try {
			if (json != null) {
				Gson gson = new Gson();
				result = Arrays.asList(gson.fromJson(json.trim(), String[].class));
			}
		} catch (Exception e) {
			// not a json format
			if (json.contains(",")) {
				result = Arrays.asList(json.trim().split(","));
			} else if (json.contains("===")) {
				result = Arrays.asList(json.trim().split("==="));
			} else {
				result = Arrays.asList(json.trim());
			}
		}
		return result.stream().filter(data -> !data.isEmpty()).map(String::trim).collect(Collectors.toList());
	}

	public static String getJsonArrayStringFromString(String string) {
		return getJsonArrayStringFromList(getListStringFromString(string));
	}

	public static String getJsonStringFromObject(Object object) {
		if (Objects.nonNull(object)) {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			return gson.toJson(object);
		}
		return "";
	}

	public static String getJsonArrayStringFromObject(Object object) {
		if (Objects.nonNull(object)) {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			return gson.toJson(object);
		}
		return GlobalConstant.EMPTY_ARRAY_STRING;
	}

	public static String getJsonArrayStringFromListObjects(List<Object> objects) {
		if (!objects.isEmpty()) {
			Gson gson = new Gson();
			return gson.toJson(objects);
		}
		return GlobalConstant.EMPTY_ARRAY_STRING;
	}

	public static String[] convertToLowerCaseStringArrayFromList(List<String> list) {
		return list.stream().map(String::toLowerCase).collect(Collectors.toList()).toArray(String[]::new);
	}

	public static String removeBearer(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		return token;
	}

	public static String trimValue(String value) {
		return isNullOrEmpty(value) ? GlobalConstant.EMPTY_STRING : value.trim();
	}

	public static String getFileFormat(String fileName) {
		String[] bits = fileName.split(GlobalConstant.FILE_REGEX_SPLIT);
		return bits[bits.length - 1];
	}
}
