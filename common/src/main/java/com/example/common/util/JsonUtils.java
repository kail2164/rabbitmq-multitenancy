package com.example.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.common.constants.GlobalConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class JsonUtils {
	private static ObjectMapper mapper = new ObjectMapper();

	public static String toJson(Object obj) throws JsonProcessingException {
		if (Objects.nonNull(obj)) {
			return mapper.writeValueAsString(obj);
		}
		return GlobalConstants.EMPTY_STRING;		
	}

	public static <T> T toObject(String json, Class<T> clazz) throws JsonProcessingException {
		return mapper.readValue(json, clazz);
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
			} else {
				result = Arrays.asList(json.trim());
			}
		}
		return result.stream().filter(data -> !data.isEmpty()).map(String::trim).collect(Collectors.toList());
	}
}
