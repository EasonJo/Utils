package com.eason.util.parse.test;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.database.TableName;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

@TableName(value = "student")
public class Student extends Bean{
	@JsonFieldName("name")
	@ListSaveType(vlaue = Student.class)
	private String dsfdsf = null;
	
}

//{[name:dfsdfsdfsfs]}