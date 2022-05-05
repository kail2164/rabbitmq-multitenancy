package com.example.common.util;

import java.lang.reflect.Field;

import org.hibernate.Hibernate;

import com.example.common.dto.APIStatus;
import com.example.common.dto.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectUtils {

	public static boolean checkFieldEquals(Object newObject, Object objectToUpdate, String fieldName)
			throws CustomException {
		try {
			Object unproxiedNew = Hibernate.unproxy(newObject);
			Object unproxiedToUpdate = Hibernate.unproxy(newObject);
			Field field = unproxiedNew.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(unproxiedNew).equals(field.get(unproxiedToUpdate));
		} catch (Exception e) {
			log.error("Error in checkFieldEquals: ", e);
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}
	}

	public static void setFieldValue(Object obj, String fieldName, Object value) throws CustomException {
		try {
			Object unproxied = Hibernate.unproxy(obj);
			Field field = unproxied.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(unproxied, value);
		} catch (Exception e) {
			log.error("Error in setFieldValue: ", e);
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	public static Object getFieldValue(Object obj, String fieldName) throws CustomException {
		try {
			Object unproxied = Hibernate.unproxy(obj);
			Field field = unproxied.getClass().getDeclaredField(fieldName);
			return field.get(obj);
		} catch (Exception e) {
			log.error("Error in getFieldValue: ", e);
			throw new CustomException(APIStatus.BAD_REQUEST, e.getMessage());
		}
	}
	

	public static Object setFieldValue(Object newObject, Object objectToUpdate, String fieldName, boolean nullableUpdate)
			throws CustomException {
		if (nullableUpdate || !ObjectUtils.checkFieldEquals(newObject, objectToUpdate, fieldName)) {
			Object valueToUpdate = ObjectUtils.getFieldValue(newObject, fieldName);
			ObjectUtils.setFieldValue(objectToUpdate, fieldName, valueToUpdate);
		}
		return objectToUpdate;
	}

}
