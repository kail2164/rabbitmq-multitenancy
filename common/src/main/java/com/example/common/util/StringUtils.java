package com.example.common.util;

import com.example.common.constants.GlobalConstants;

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
		return StringUtils.concatStrings(GlobalConstants.DASH, queueName);
	}

	public static String getExchange(String serviceName, String exchange) {
		return StringUtils.concatStrings(GlobalConstants.DOT, GlobalConstants.DEMO_STRING, serviceName, exchange);
	}

	public static String getAction(String... action) {
		return StringUtils.concatStrings(GlobalConstants.DASH, action);
	}

	public static String getRoutingKey(String serviceName, String action, String charsAfter) {
		return StringUtils.concatStrings(GlobalConstants.DOT, GlobalConstants.DEMO_STRING, serviceName, action,
				charsAfter);
	}	

	public static String removeBearer(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		return token;
	}

	public static String trimValue(String value) {
		return isNullOrEmpty(value) ? GlobalConstants.EMPTY_STRING : value.trim();
	}

	public static String getFileFormat(String fileName) {
		String[] bits = fileName.split(GlobalConstants.FILE_REGEX_SPLIT);
		return bits[bits.length - 1];
	}
}
