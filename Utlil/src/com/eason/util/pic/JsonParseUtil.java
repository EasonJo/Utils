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
            .excludeFieldsWithoutExposeAnnotation() //������ʵ����û����@Exposeע�������
            .enableComplexMapKeySerialization() //֧��Map��keyΪ���Ӷ������ʽ
            .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss:SSS")//ʱ��ת��Ϊ�ض���ʽ
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//����ֶ�����ĸ��д,ע:����ʵ����ʹ����@SerializedNameע��Ĳ�����Ч.
            .setPrettyPrinting() //��json�����ʽ��.
            .setVersion(1.0)    //�е��ֶβ���һ��ʼ���е�,�����Ű汾��������ӽ���,��ô�ڽ������л��ͷ����л���ʱ��ͻ���ݰ汾����ѡ���Ƿ�Ҫ���л�.
                //@Since(�汾��)��������ʵ���������.�����ֶο���,���Ű汾��������ɾ��,��ô
                //@Until(�汾��)Ҳ��ʵ���������,GsonBuilder.setVersion(double)������Ҫ����.
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
