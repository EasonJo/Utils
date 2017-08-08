package com.eason.util.parse.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于告诉系统，List保存的对象的类型
 * @author liuzhd
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListSaveType {
	public Class<?> vlaue();
}
