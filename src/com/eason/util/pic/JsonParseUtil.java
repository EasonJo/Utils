package com.eason.util.pic;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Eason on 2015/4/21.
 */
public class JsonParseUtil {
    private static final String TAG = "JsonParseUtil";

    private static Gson buildGson() {

        /*
        Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation() //不导出实体中没有用@Expose注解的属性
            .enableComplexMapKeySerialization() //支持Map的key为复杂对象的形式
            .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//时间转化为特定格式
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写,注:对于实体上使用了@SerializedName注解的不会生效.
            .setPrettyPrinting() //对json结果格式化.
            .setVersion(1.0)    //有的字段不是一开始就有的,会随着版本的升级添加进来,那么在进行序列化和返序列化的时候就会根据版本号来选择是否要序列化.
                //@Since(版本号)能完美地实现这个功能.还的字段可能,随着版本的升级而删除,那么
                //@Until(版本号)也能实现这个功能,GsonBuilder.setVersion(double)方法需要调用.
            .create();
            */


        Gson gson = new GsonBuilder()
            //Set First Letter Up
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .create();
        return gson;
    }


    public static JSONObject beanParseToString(Object o) {
        JSONObject jsonObject = null;
        try {
            Gson gson = buildGson();
            String string = gson.toJson(o);
            jsonObject = new JSONObject(string);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static <T> T parseStringToBean(String s, Class<T> cls) {
        Gson gson = buildGson();
        T t = gson.fromJson(s, cls);
        return t;
    }

    public static JSONObject addMoreParams(JSONObject jsonObject, Map<String, Object> params) {
        if (jsonObject == null) {
            return null;
        }
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                Slog.d(TAG, "key=" + key + " value=" + params.get(key));
                try {
                    jsonObject.put(key, params.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject;
        }
        return null;
    }

    /**
     * Build a Params form a Map<String, Object>
     *
     * @param params
     * @return
     */
    public static JSONObject buildJsonObject(Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            JSONObject body = new JSONObject();
            for (String key : params.keySet()) {
                Slog.d(TAG, "key=" + key + " value=" + params.get(key));
                try {
                    body.put(key, params.get(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return body;
        }
        return null;
    }


}
