package com.eason.util.parse.json;

import java.io.Serializable;

/**
 *bean 所支持的数据类型有 所有的非应用类型，List类型，Bean类型 
 * @author liuzhd
 */
@SuppressWarnings("serial")
public abstract class Bean implements Serializable{
	/**
	 * 此处的id 用来再创建的表的时候，提供一个自动生成的主键
	 */
	private int id;

	public int getId() {
		return id;
	}

	public void internalSetId(int id) {
		this.id = id;
	}
}
