package com.eason.util.parse.json;

import com.lenovo.nova.util.debug.MyOldLog;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Bean 对象的输入输出
 * 
 * @author liuzhd
 */
public class JsonToBeanParser {

	private JsonToBeanParser() {
	};

	private static JsonToBeanParser instance;

	public static JsonToBeanParser getInstance() {
		if (instance == null) {
			instance = new JsonToBeanParser();
		}
		return instance;
	}

	public synchronized void fillBeanWithJson(List list, JSONArray jsonArray, OnJSONFillBeanHelper help) {
		if (help != null && jsonArray!=null) {
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					Bean bean = help.getNewBean();
					fillBeanWithJson(bean, jsonArray.getJSONObject(i));
					list.add(bean);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 把json对象转化成javaBean，注意，Bean的字段必须和json的对应起来
	 * 
	 * @param beFilledObj
	 * @param jsonObj
	 */
	@SuppressWarnings("unchecked")
	public synchronized void fillBeanWithJson(Bean beFilledObj, JSONObject jsonObj) {
		Field[] fields = beFilledObj.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				Object fieldVaule = field.get(beFilledObj);
				if (fieldVaule instanceof Bean) {
					String jsonName = getJsonFieldName(field);
					if (jsonObj.has(jsonName)) {
						fillBeanWithJson((Bean) fieldVaule, jsonObj.getJSONObject(jsonName));
					}
				} else if (fieldVaule instanceof List<?>) {
					String jsonName = getJsonFieldName(field);
					if (jsonObj.has(jsonName)) {
						JSONArray jsonArray = jsonObj.getJSONArray(jsonName);
						List<Bean> list = (List<Bean>) fieldVaule;
						Class<?> listSaveType = ParseUtil.getListSaveType(field);
						for (int index = 0; index < jsonArray.length(); index++) {
							Bean oneBean = (Bean) listSaveType.newInstance();
							fillBeanWithJson(oneBean, jsonArray.getJSONObject(index));
							list.add(oneBean);
						}
					}
				} else {
					// 获取Json字段的值
					String jsonName = getJsonFieldName(field);
					if (jsonObj.has((jsonName))) {
						try {
							field.set(beFilledObj, jsonObj.get(jsonName));
						} catch (Exception e) {
							MyOldLog.e("exeute field.set error maybe declare error" + " fieldName is " + field.getName() + " you declare fieldType is " + field.getType().getName()
									+ " right fieldType is " + " " + ReflectUtil.getObjectType(jsonObj.get(jsonName)));
							e.printStackTrace();
						}
					}

				}
			} catch (SecurityException e) {
				MyOldLog.error(this, "fillBeanWithJson SecurityException error . setAccessible error" + e + " the current Field is :" + field + " JsonFieldName is :"
						+ getJsonFieldName(field));
			} catch (IllegalArgumentException e) {
				MyOldLog.error(this, "fillBeanWithJson method IllegalArgumentException " + e + " the current Field is " + field + " JsonFieldName is :" + getJsonFieldName(field)
						+ " FilledValue is : " + " beFilledObj is " + beFilledObj);
			} catch (IllegalAccessException e) {
				MyOldLog.error(this, "fillBeanWithJson IllegalAccessException " + e + " the current Field is " + field + " JsonFieldName is :" + getJsonFieldName(field));
			} catch (JSONException e) {
				MyOldLog.error(this, "fillBeanWithJson method JSONException " + e + " the current Field is " + field + " JsonFieldName is :" + getJsonFieldName(field));
			} catch (InstantiationException e) {
				MyOldLog.error(this, "fillBeanWithJson method InstantiationException " + e + "JsonArray fieldName is : " + getJsonFieldName(field));
			} catch (ParseUtil.ListSaveTypeException e) {
				MyOldLog.error(this, "fillBeanWithJson method ListSaveTypeException " + e);
			}
		}
	}

	private String getJsonFieldName(Field field) {
		Annotation[] fieldAnnotations = field.getAnnotations();
		if (fieldAnnotations != null) {
			for (Annotation annotation : fieldAnnotations) {
				if (annotation instanceof JsonFieldName) {
					return ((JsonFieldName) annotation).value();
				}
			}
		}
		String jsonObjectName = field.getName();
		jsonObjectName = jsonObjectName.substring(jsonObjectName.lastIndexOf(".") + 1);
		return jsonObjectName;
	}

	public interface JSONOperatorHelper {
		void onPaseString(Object jsonObj, int index);
	}

	public interface OnJSONFillBeanHelper {
		Bean getNewBean();
	}

	public static <T> List<T> jsonToArray(JSONArray jsonArray, List<T> list, JSONOperatorHelper helper) {
		try {
			if (list != null) {
				list.clear();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				if (list != null) {
					list.add((T) jsonArray.get(i));
				}

				if (helper != null) {
					helper.onPaseString(jsonArray.get(i), i);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
