package com.eason.util.parse.json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class ParseUtil {

	protected static Class<?> getListSaveType(Field field)
			throws ListSaveTypeException {
		Annotation[] anntation = field.getAnnotations();
		for (Annotation annotation : anntation) {
			if (annotation instanceof ListSaveType) {
				return ((ListSaveType) annotation).vlaue();
			}
		}

		throw new ListSaveTypeException(
				"必须指定List的ListSaveType的注解，告诉系统List是保存什么样子的对象");
	}

	protected static boolean isFloatType(String fieldType) {
		return fieldType.equals("float") || fieldType.equals("java.lang.Float");
	}

	protected static boolean isByteType(String fieldType) {
		return fieldType.equals("byte") || fieldType.equals("java.lang.Byte");
	}

	protected static boolean isDoubleType(String fieldType) {
		return fieldType.equals("double")
				|| fieldType.equals("java.lang.Double");
	}

	protected static boolean isBooleanType(String fieldType) {
		return fieldType.equals("boolean")
				|| fieldType.equals("java.lang.Boolean");
	}

	protected static boolean isLongType(String fieldType) {
		return fieldType.equals("long") || fieldType.equals("java.lang.Long");
	}

	protected static boolean isIntegerType(String fieldType) {
		return fieldType.equals("int") || fieldType.equals("java.lang.Integer");
	}

	protected static boolean isStringType(String fieldType) {
		return fieldType.equals("java.lang.String");
	}

	protected static class ListSaveTypeException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ListSaveTypeException(String s) {
			super(s);
		}

	}

}
