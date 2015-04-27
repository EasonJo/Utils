package com.eason.util.parse.json;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

	static PreferencesUtil instance = null;
	private Context context;

	public PreferencesUtil(Context context) {
		this.context = context;
	}

    public PreferencesUtil(){

    }

    public void init(Context context){
        this.context = context;
    }

    public void release(){
        this.context = null;
    }
    @Deprecated
	public static PreferencesUtil getCurrentInstance(Context context){
		if(instance == null){
			instance = new PreferencesUtil(context);
		}
		instance.context = context;
		return instance;
	}

    public static PreferencesUtil getCurrentInstance(){
        if(instance == null){
            instance = new PreferencesUtil();
        }
        return instance;
    }

    public boolean delete(String key){
        SharedPreferences instance = context.getSharedPreferences(getSharedPreferencesName(), 0);
        return instance.edit().remove(key).commit();
    }
	public boolean save(String key,String value){
		SharedPreferences instance = context.getSharedPreferences(getSharedPreferencesName(), 0);
		return instance.edit().
		putString(key, value)
		.commit()
		;
	}
	public void save(String key,long value){
		SharedPreferences instance = context.getSharedPreferences(getSharedPreferencesName(), 0);
		instance.edit().
		putLong(key, value)
		.commit()
		;
	}

	public void save(String key,int value){
		SharedPreferences instance = context.getSharedPreferences(getSharedPreferencesName(), 0);
		instance.edit().
		putInt(key, value)
		.commit()
		;
	}
	
	public String getString(String key){
		SharedPreferences instance = context.getSharedPreferences(getSharedPreferencesName(), 0);
		return instance.getString(key, null);
	}
	
	public int getInt(String key,int defaultValue){
		SharedPreferences instance = context.getSharedPreferences(getSharedPreferencesName(), 0);
		return instance.getInt(key, defaultValue);
	}
	
	protected String getSharedPreferencesName() {
		return "scenic_SharedPreferences";
	}

    public long getLong(String key, long defaultValue) {
        SharedPreferences instance = context.getSharedPreferences(getSharedPreferencesName(), 0);
        return instance.getLong(key, defaultValue);
    }
}
