package com.eason.util.parse.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReflectUtil {

	public static String getObjectType(Object obj) {
		Method methods[] = ReflectUtil.class.getDeclaredMethods();
		for(int i = 0; i < methods.length; i++){
			Method method = methods[i];
			try {
				String name = method.getName() ;
				Class<?> firstMethodParam = method.getParameterTypes()[0];
				if(firstMethodParam.getName().equals("java.lang.Object") ){
					Pattern pattern = Pattern.compile("is(.+)type");
					Matcher matcher = pattern.matcher(name.toLowerCase());
					if(matcher.matches()){
						boolean isTure = (Boolean) method.invoke(ReflectUtil.class, obj);
						if(isTure){
							return matcher.group(1);
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Error " +method.getName() + " " + e);
			}	
		}
		
		return "class";
	}
	
	public static boolean isBaseType(String fieldType){
		return 
		isDoubleType(fieldType) ||
		isFloatType(fieldType) ||
		isLongType(fieldType) ||
		isIntegerType(fieldType) ||
		isByteType(fieldType) ||
		isBooleanType(fieldType) ||
		isStringType(fieldType) ||
		isCharType(fieldType)
		;
	}
	
	public static boolean isInvalideType(Field field,Object obj) throws IllegalArgumentException, IllegalAccessException{
		Object fieldValue = field.get(obj);
		if(fieldValue != null){
			if(fieldValue.getClass().getPackage() == null){
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isCharType(String fieldType) {
		return fieldType.equals("char") || fieldType.equals("java.lang.Char");
	}

	public static boolean isFloatType(String fieldType) {
		return fieldType.equals("float") || fieldType.equals("java.lang.Float");
	}

	public static boolean isByteType(String fieldType) {
		return fieldType.equals("byte") || fieldType.equals("java.lang.Byte");
	}

	public static boolean isDoubleType(String fieldType) {
		return fieldType.equals("double") || fieldType.equals("java.lang.Double");
	}

	public static boolean isBooleanType(String fieldType) {
		return fieldType.equals("boolean") || fieldType.equals("java.lang.Boolean");
	}

	public static boolean isLongType(String fieldType) {
		return fieldType.equals("long") || fieldType.equals("java.lang.Long");
	}


	public static boolean isIntegerType(String fieldType) {
		return fieldType.equals("int") || fieldType.equals("java.lang.Integer");
	}
	
	public static boolean isStringType(String fieldType) {
		return fieldType.equals("java.lang.String");
	}
	
	public static boolean isCharSequenceType(String fieldType){
	    return fieldType.equalsIgnoreCase("java.lang.CharSequence");
	}
	
	public static boolean isListType(String fieldType){
	    return fieldType.equalsIgnoreCase("java.util.List") ||
	    		fieldType.equalsIgnoreCase("java.util.ArrayList")
	    		;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static boolean isCharType(Object obj) {
		return obj instanceof Character  ;
	}

	public static boolean isFloatType(Object obj) {
		return obj instanceof Float;
	}

	public static boolean isByteType(Object obj) {
		return obj instanceof Byte;
	}

	public static boolean isDoubleType(Object obj) {
		return obj instanceof Double;
	}

	public static boolean isBooleanType(Object obj) {
		return obj instanceof Boolean;
	}

	public static boolean isLongType(Object obj) {
		return obj instanceof Long;
	}


	public static boolean isIntegerType(Object obj) {
		return obj instanceof Integer;
	}
	
	public static boolean isStringType(Object obj) {
		return obj instanceof String;
	}
	
	private static boolean isAlreadFindField = false;
	private static Field mFindField = null ;
	public static Field findFieldByName(Object rootObj,String name) {
		Field[] allFields = rootObj.getClass().getDeclaredFields();
		try {
			for (Field field : allFields) {
				field.setAccessible(true);
				Object fieldValue = field.get(rootObj);
				String fieldName = field.getName();
				
				if(fieldName.equals(name)){
					isAlreadFindField = true;
				}
				
				if(isAlreadFindField){
					if(fieldName.equals(name)){
						mFindField = field;
					}
					break;
				}
				
				if(!ReflectUtil.isBaseType(field.getType().getName()) && ! ReflectUtil.isInvalideType(field, rootObj)){
					if(fieldValue != null && !isAlreadFindField){
						findFieldByName(fieldValue, name);
					}
				}else{
//					System.out.println(field.getName() +"\t" +classPackage + "\t\t\t" + fieldValue + "\t\t\t " + i++);
				}
				
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mFindField;
	}

	public static boolean isDateType(String fieldType) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
