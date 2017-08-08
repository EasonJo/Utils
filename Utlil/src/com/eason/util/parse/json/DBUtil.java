package com.eason.util.parse.json;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import com.lenovo.nova.util.debug.mylog;
import com.lenovo.nova.util.debug.slog;
import com.lenovo.nova.util.parse.ParseUtil.ListSaveTypeException;
import com.lenovo.nova.util.parse.anntation.database.ID;
import com.lenovo.nova.util.parse.anntation.database.TableFieldName;
import com.lenovo.nova.util.parse.anntation.database.TableName;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("CommitPrefEdits")
public abstract class DBUtil extends SQLiteOpenHelper {

	private static String ID = "id";
	private static String TAG = DBUtil.class.getSimpleName();
	public Bean productData;
	// 数据库的名称
	private static final String DATABASE_NAME = "scenic";
	private boolean DEBUG = true;
	private SQLiteDatabase dbWrite;
	private SQLiteDatabase dbRead;

	public DBUtil(Context context) {
		super(context, DATABASE_NAME, null, 2);
	}

	public DBUtil(Context context, String dbName) {
		super(context, dbName, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// createTables(new ProductData(), db, null);
		if (DEBUG)
			mylog.i(TAG, "onCreate " + db);

		onPreCreateTable();
		Class<? extends Bean> beanClasses[] = onGetBeanForCreateTable();
		for (Class<? extends Bean> class1 : beanClasses) {
			createTables(class1, db, onGetForeignKeyForCreateTable());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@SuppressWarnings("unchecked")
	public void saveOrUpdate(@SuppressWarnings("rawtypes") List list) {
		mylog.i(TAG, "begin saveorupdate list " + list.size());
		dbWrite = getWritableDatabase();
		dbWrite.beginTransaction();
		for (Bean bean : (List<Bean>) list) {
			saveBean(bean, null, -1, false);
		}
		dbWrite.setTransactionSuccessful();
		dbWrite.endTransaction();
		dbWrite.close();
		mylog.i(TAG, "finish saveorupdate list ");
	}

	public void saveOrUpdate(Bean bean) {

		dbWrite = getWritableDatabase();
		dbWrite.beginTransaction();
		saveBean(bean, null, -1, false);
		dbWrite.setTransactionSuccessful();
		dbWrite.endTransaction();


	}

	/**
	 * 一个提交就是一个事务
	 * 
	 * @param bean
	 */
	public void saveBean(Bean bean) {
		dbWrite = getWritableDatabase();
		dbWrite.beginTransaction();
		saveBean(bean, null, -1, true);
		dbWrite.setTransactionSuccessful();
		dbWrite.endTransaction();
	}

	@Deprecated
	public int getDBCount(Class<? extends Bean> tableName) {
		return getCount(tableName);
	}

	public int getCount(Class<? extends Bean> tableName,String where){
		Cursor cursor = getCursorFromDB(tableName,where);
		int count = 0;
		if(cursor != null){
			count = cursor.getCount();
			cursor.close();
		}
		return count;
	}

	public int getCount(Class<? extends Bean> tableName) {
		int count = -1;
		String table = getTableName(tableName);
		String sql = "select count(*) from " + table;
		mylog.i(TAG, "sql " + sql);
		slog.p(TAG, "getDBCount sql is " + sql);
		Cursor cusror = null;
		try {
			cusror = getReadableDatabase().rawQuery(sql, null);
			if (cusror.moveToNext()) {
				count = cusror.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cusror != null) {
				cusror.close();
			}
		}
		return count;
	}

	public byte[] objectToByte(Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();
			bo.close();
			oo.close();
		} catch (Exception e) {
			mylog.e(TAG, "translation" + e.getMessage());
			e.printStackTrace();
		}
		return (bytes);
	}

	/**
	 * 填充一个javaBean
	 * 
	 * @param bean
	 * @param dataFrom
	 *            1 代表 数据来源是 数据库，2 代表数据来源是
	 *            Preference.如果被填充的Bean里边还有Bean的话，系统将选择默认的数据来源
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws ListSaveTypeException
	 */
	public void fillBean(Bean bean, int dataFrom) throws IllegalArgumentException, IllegalAccessException, ListSaveTypeException {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = getCursor(getTableName(bean.getClass()), db);
		Field fields[] = bean.getClass().getDeclaredFields();

		for (Field field : fields) {
			field.setAccessible(true);
			Object fieldValue = field.get(bean);
			if (fieldValue instanceof List<?>) {
				// getBeanList(ParseUtil.getListSaveType(field), (List<Bean>)
				// fieldValue);
			} else if (fieldValue instanceof Bean) {
				fillBean((Bean) fieldValue, 2);
			} else {
				Object obj = byteToObject(cursor.getBlob(cursor.getColumnIndex(getFieldAndAnnotationName(field))));
				field.set(bean, obj);
			}
		}

		cursor.close();

		for (Field field : fields) {
			field.setAccessible(true);
			Object fieldValue = field.get(bean);
			if (fieldValue instanceof List) {
				// fillBean(ParseUtil.getListSaveType(field), (List<Bean>)
				// fieldValue);
			} else if (fieldValue instanceof Bean) {
				// fillBean((Bean) fieldValue);
			}
		}

	}

	@SuppressWarnings("rawtypes")
	public Cursor getBeanListFromDB(Class<?> beanType, List list, int start, int size) {
		String sql = "select * from " + getTableName(beanType) + " limit " + start + "," + size;
		slog.p(TAG, "getBeanListFromDB sql is " + sql);
		return getBeanListFromDB(beanType, list, sql);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Cursor getBeanListFromDB(Class<?> beanType, List list, String where) {
		Cursor cursor = getCursorFromDB(beanType, where);

		mylog.i(TAG,"cursor size " + (cursor == null ? -1 : cursor.getCount()));
		if (list != null) {
			fillBeanListFromByCursor(beanType, list, cursor);
		}
		return cursor;
	}

	public Cursor getCursorFromDB(Class<?> beanType, String where) {
		SQLiteDatabase dbReader = getReadableDatabase();
		String tabName = getTableName(beanType);
		mylog.i(TAG, "tableName " + tabName + "  where " + where);
		return dbReader.query(tabName, null, where, null, null, null, null);
	}

	public void delete(int id) {

	}

	public void deleteAll(Class<?> beanTable) {
		try {
			getWritableDatabase().execSQL("delete  from " + getTableName(beanTable));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean delete(Bean bean) {
//		getWritableDatabase().delete(getTableName(bean.getClass()), "id=" + bean.getId(), null);
		StringBuffer stringBuilder = new StringBuffer();
		try {
			createWhereByPrimaryKey(bean,getBeanPrimarykeyField(bean),stringBuilder);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}

		int result = getWritableDatabase().delete(getTableName(bean.getClass()), stringBuilder.toString(), null);
		mylog.i(TAG,"result " + result + " sql " + stringBuilder.toString());
		return true;
	}



	/**
	 * example for sql<br>
	 *
	 *1. delete from admin limit 5. delete 5 item from top
	 *
	 * @param beanClass
	 * @param whereClause
	 */
	public void delete(Class<? extends Bean> beanClass, String whereClause){
		getWritableDatabase().delete(getTableName(beanClass),whereClause,null);
	}

	protected Class onGetForeignKeyForCreateTable() {
		return null;
	}

	/**
	 * 指定被即将被创建的数据表的名字
	 * 
	 * @return
	 */
	protected abstract Class<? extends Bean>[] onGetBeanForCreateTable();

	protected void onPreCreateTable() {

	}

	protected String onCreateTableSql() {
		return "CREATE TABLE mood(id integer primary key autoincrement ,name varchar(20),content varchar(20))";
	}

	protected void savePreference(Context context, Field field, Object value) {
		String key = field.toGenericString().replace(" ", ":");
		Editor ed = PreferenceManager.getDefaultSharedPreferences(context).edit();
		if (value instanceof Integer) {
			ed.putInt(key, (Integer) value);
		} else if (value instanceof Boolean) {
			ed.putBoolean(key, (Boolean) value);
		} else if (value instanceof Float || value instanceof Double) {
			ed.putFloat(key, (Float) value);
		} else if (value instanceof Long) {
			ed.putLong(key, (Long) value);
		} else if (value instanceof String) {
			ed.putString(key, (String) value);
		} else {
			mylog.e(TAG, "unkonw type " + field + "  " + value);
		}
	}

	/**
	 * @param beanClass
	 * @param db
	 * @param foreignKey
	 *            在多对一的映射中，要在多的一方加入另一方的ID
	 */
	protected void createTables(Class<?> beanClass, SQLiteDatabase db, Class<?> foreignKey) {
		if (DEBUG)
			mylog.i(TAG, "beanClass " + beanClass + "  foreignKey " + foreignKey);

		if (beanClass == null) {
			mylog.e(TAG, "method createTables error the Bean is null");
			return;
		}

		StringBuffer sql = new StringBuffer();
		Field fields[] = beanClass.getDeclaredFields();
		try {
			// 表的名字
			String tableName = getTableName(beanClass);
			List<Field> primaryKeyField = getBeanPrimarykeyField(beanClass);
			if (primaryKeyField == null) {
				mylog.e(TAG, "the table must have a primary key  表必须有个主键 : " + beanClass);
				return;
			}

			// 已经把字段加入sql之后就不应该有这个字段了，否则后面的会重复的
			sql.append("create table " + tableName + " (").append(getIDFieldName(beanClass) + " INTEGER PRIMARY KEY AUTOINCREMENT");

			if (foreignKey != null) {
				sql.append(", " + getForeignKeyName(foreignKey) + " INTEGER");
			}

			for (Field field : fields) {
				field.setAccessible(true);
				if (field.getName().equals(getIDFieldName(beanClass))) {
					// ID上面设置过了
					continue;
				}

				boolean isBean = false;
				try {
					isBean = field.getType().newInstance() instanceof Bean;
				} catch (Exception e) {
				}

				if (ReflectUtil.isListType(field.getType().getName())) {
					Class<?> listSaveType = ParseUtil.getListSaveType(field);
					createTables(listSaveType, db, beanClass);
				} else if (isBean) {
					createTables(field.getType(), db, null);
					sql.append(", " + getForeignKeyName(field.getType()) + " INTEGER");
				} else {
					// 字段名 字段类型
					sql.append("," + " ").append(getFieldAndAnnotationName(field)).append(" " + getDBFieldType(field.getType().getName()));
				}

			}
			sql.append(")");

			// 建表语句

			String sqlCreateTable = sql.toString();
			if (DEBUG)
				mylog.i(TAG, "sql is " + sql);
			db.execSQL(sqlCreateTable);
		} catch (SecurityException e) {
			mylog.e(TAG, e.toString());
		} catch (IllegalArgumentException e) {
			mylog.e(TAG, e.toString());
		} catch (ParseUtil.ListSaveTypeException e) {
			mylog.e(TAG, e.toString());
		} catch (SQLiteException e) {
			mylog.e(TAG, e.toString());
		}
	}

	private List<Bean> fillBeanListFromByCursor(Class<?> beanType, List<Bean> list, Cursor cursor) {
		if (list == null) {
			return list;
		}
		while (cursor.moveToNext()) {
			try {
				Bean oneBean = (Bean) beanType.newInstance();

				// init id
				Field[] fields = oneBean.getClass().getDeclaredFields();
				
				
				for (Field field : fields) {
					field.setAccessible(true);
					Object fieldValue = field.get(oneBean);
					if (fieldValue instanceof Bean) {
						List<Bean> listTemp = new ArrayList<Bean>();
						int foreginKeyValue = cursor.getInt(cursor.getColumnIndex(getForeignKeyName((Bean) fieldValue)));

						String where = getIDFieldName(beanType) + " = " + foreginKeyValue;
						getBeanListFromDB(fieldValue.getClass(), listTemp, where);
						if (listTemp.size() > 0) {
							field.set(oneBean, listTemp.get(0));
						}
					} else if (fieldValue instanceof List<?>) {
						Class<?> listSaveType = ParseUtil.getListSaveType(field);
						String where = getForeignKeyName(oneBean) + " = " + cursor.getInt(cursor.getColumnIndex(getIDFieldName(beanType)));
						getBeanListFromDB(listSaveType, (List<Bean>) fieldValue, where);
					} else {
						String fieldType = field.getType().getName();
						String columName = getFieldAndAnnotationName(field);
						int columnIndex = cursor.getColumnIndex(columName);
						if (columnIndex != -1) {
							if (ParseUtil.isBooleanType(fieldType)) {
								field.setBoolean(oneBean, cursor.getInt(columnIndex) == 1);
							} else if (ParseUtil.isByteType(fieldType)) {
								field.setByte(oneBean, (byte) cursor.getInt(columnIndex));
							} else if (ParseUtil.isFloatType(fieldType)) {
								field.setFloat(oneBean, cursor.getFloat(columnIndex));
							} else if (ParseUtil.isDoubleType(fieldType)) {
								field.setDouble(oneBean, cursor.getDouble(columnIndex));
							} else if (ParseUtil.isIntegerType(fieldType)) {
								field.setInt(oneBean, cursor.getInt(columnIndex));
							} else if (ParseUtil.isLongType(fieldType)) {
								field.setLong(oneBean, cursor.getLong(columnIndex));
							} else if (ParseUtil.isStringType(fieldType)) {
								field.set(oneBean, cursor.getString(columnIndex));
							}
						} else {
							mylog.i(TAG, "columnIndex is -1 " + columName);
						}
					}
				}
				
				// 设置id,因为继承Bean后，不会遍历ID
				oneBean.internalSetId(cursor.getInt(cursor.getColumnIndex(ID)));
				
				list.add(oneBean);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (ListSaveTypeException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 把一个Bean对象保存起来
	 * 
	 * @param bean
	 *            要保存的对象
	 * @param foreignKeyName
	 *            外键的名字
	 * @param foreignKeyValue
	 *            外键的值
	 * @param forceInsert
	 *            如果是false有可能执行更新的操作，true则会强制的增加到数据库中
	 */
	@SuppressWarnings("unchecked")
	private void saveBean(Bean bean, String foreignKeyName, int foreignKeyValue, boolean forceInsert) {
		if (DEBUG)
			mylog.i(TAG, "bean " + bean + " foreignKeyName " + foreignKeyName + "  foreignKeyValue " + foreignKeyValue + "  forceInsert " + forceInsert);
		// 取得主键，逻辑上可能有好几个主键
		List<Field> keyNameField = getBeanPrimarykeyField(bean);
		if (keyNameField == null) {
			mylog.e(TAG, " not set primary key  没有设置主键 ");
			return;
		}
		boolean existRecorder = false;

		try {
			StringBuffer querySql = new StringBuffer();
			createWhereByPrimaryKey(bean, keyNameField, querySql);
			querySql.append(" and ");
			querySql.append("1=1");

			if(foreignKeyName !=null && foreignKeyValue != -1){
				//防止更新的冲掉原来的
				querySql.append(" and ");
				querySql.append(foreignKeyName).append("=").append(foreignKeyValue);
			}

			String selection = querySql.toString().trim();
			if (DEBUG)
				mylog.i(TAG, "selection " + selection);
			if (dbRead == null) {
				dbRead = getReadableDatabase();
			}
			Cursor cursor = dbRead.query(getTableName(bean.getClass()), null, selection, null, null, null, null);
			if (cursor.getCount() > 0) {
				// 已经有该记录了
				existRecorder = true;
				//set bean id
				if(cursor.moveToNext()){
					int id = cursor.getInt(0);
					bean.internalSetId(id);
				}
			}
			cursor.close();
			if (DEBUG)
				mylog.i(TAG, "existRecorder " + existRecorder);

			// 开始保存数据或更新数据
			Field fields[] = bean.getClass().getDeclaredFields();
			ContentValues contentValues = new ContentValues();
			for (Field field : fields) {
				field.setAccessible(true);
				Object fieldValue = field.get(bean);
				// if(primaryKey(field,keyNameField )){
				// continue;
				// }
				if (fieldValue instanceof List) {
					// list里边的对象要持有他外面对象的外键，所以要参考它外面的对象，要最后存储
				} else if (fieldValue instanceof Bean) {
					saveBean((Bean) fieldValue, null, -1, forceInsert);
					contentValues.put(getForeignKeyName((Bean) fieldValue), getMaxId((Bean) fieldValue));
				} else {
					// 基础类型
					if (fieldValue != null) {
						fillContentValue(contentValues, field, fieldValue);
					}
				}
			}

			if (foreignKeyName != null && foreignKeyValue != -1) {
				if (DEBUG)
					mylog.i(TAG, "save foreignKeyName  " + foreignKeyName + "  foreignKeyValue " + foreignKeyValue);
				contentValues.put(foreignKeyName, foreignKeyValue);
			}

			String tableName = getTableName(bean.getClass());

			if (DEBUG)
				mylog.i(TAG, "tableName " + tableName + "  contentValues " + contentValues);
			if (existRecorder && !forceInsert) {
				// 存在的话就更新
				int rowsNumber = dbWrite.update(tableName, contentValues, selection, null);

				if (DEBUG)
					mylog.i(TAG, "rowsNumber " + rowsNumber);

			} else {
				long rowsID = dbWrite.insert(tableName, null, contentValues);
				if (rowsID == -1) {
					mylog.e(TAG, "insert have error");
				}

				if (DEBUG)
					mylog.i(TAG, "rowsID " + rowsID);
			}

			for (Field field : fields) {
				field.setAccessible(true);
				Object fieldValue = field.get(bean);
				if (fieldValue instanceof List) {
					// 最后要保存List对象

					@SuppressWarnings("rawtypes")
					List list = (List) fieldValue;
					if (list.size() > 0) {
						if (!(list.get(0) instanceof Bean)) {
							// list 可能存放的是基础类型
							// not support base type
							mylog.e(TAG, "not support the type except Bean");
						}

						int beanId = bean.getId();

						if (beanId == 0) {
							if (DEBUG)
								mylog.i(TAG, "get beanId is  0 , maybe first save ");

							beanId = getMaxId(bean);
						}

						for (Bean b : (List<Bean>) list) {
							String foreignKey = getForeignKeyName(bean);

							if (DEBUG)
								mylog.i(TAG, "save foreignKey of " + bean + "foreignKey " + foreignKey + "  beanId " + beanId);

							saveBean(b, foreignKey, beanId, forceInsert);
						}
					}

				}
			}
		} catch (IllegalArgumentException e) {
			mylog.e(TAG, e.toString());
			e.printStackTrace();
		} catch (SecurityException e) {
			mylog.e(TAG, e.toString());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			mylog.e(TAG, e.toString());
			e.printStackTrace();
		} catch (SQLiteException e) {
			mylog.e(TAG, e.toString());
			e.printStackTrace();
			// createTables(onGetBeanForCreateTable(), this.dbWrite,
			// onGetForeignKeyForCreateTable());
		}
	}

	private void createWhereByPrimaryKey(Bean bean, List<Field> keyNameField, StringBuffer querySql) throws IllegalAccessException {
		for (Field field : keyNameField) {
            String primaryKeyName = field.getName();
            Object value = field.get(bean);

            querySql.append(primaryKeyName + "=");

            if (ReflectUtil.isStringType(value)) {
                querySql.append("'");
            }
            querySql.append(value.toString());

            if (ReflectUtil.isStringType(value)) {
                querySql.append("'");
            }


        }
	}

	private boolean primaryKey(Field field, List<Field> keyNameField) {
		try {
			for (int i = 0; i < keyNameField.size(); i++) {
				String primaryKeyName = keyNameField.get(i).getName();
				if (field.getName().equals(primaryKeyName)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void fillContentValue(ContentValues contentValues, Field field, Object fieldValue) {
		String fieldType = field.getType().getName();
		if (ParseUtil.isStringType(fieldType)) {
			contentValues.put(getFieldAndAnnotationName(field), (String) fieldValue);
		} else if (ParseUtil.isBooleanType(fieldType)) {
			contentValues.put(getFieldAndAnnotationName(field), (Boolean) fieldValue);
		} else if (ParseUtil.isIntegerType(fieldType)) {
			contentValues.put(getFieldAndAnnotationName(field), (Integer) fieldValue);
		} else if (ParseUtil.isLongType(fieldType)) {
			contentValues.put(getFieldAndAnnotationName(field), (Long) fieldValue);
		} else if (ParseUtil.isFloatType(fieldType)) {
			contentValues.put(getFieldAndAnnotationName(field), (Float) fieldValue);
		} else if (ParseUtil.isByteType(fieldType)) {
			contentValues.put(getFieldAndAnnotationName(field), (Byte) fieldValue);
		} else if (ParseUtil.isDoubleType(fieldType)) {
			contentValues.put(getFieldAndAnnotationName(field), (Double) fieldValue);
		} else {
			mylog.e(TAG, "method fillContentValue : no match field alue " + fieldType);
		}
	}

	private int getMaxId(Bean bean) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "select max(" + getIDFieldName(bean.getClass()) + ") from " + getTableName(bean.getClass());
		Cursor cursor = db.rawQuery(sql, null);
		int id = -1;
		if (cursor.moveToNext()) {
			id = cursor.getInt(0);
		}
		cursor.close();
		return id;
	}

	private List<Field> getBeanPrimarykeyField(Bean bean) {
		return getBeanPrimarykeyField(bean.getClass());
	}

	private List<Field> getBeanPrimarykeyField(Class<?> beanClass) {
		Field fields[] = beanClass.getDeclaredFields();
		List<Field> primaryKeyLists = new ArrayList<Field>();
		for (Field field : fields) {
			field.setAccessible(true);
			Annotation ann = field.getAnnotation(ID.class);
			if (ann != null) {
				primaryKeyLists.add(field);
			}
		}
		if (primaryKeyLists.size() > 0) {
			return primaryKeyLists;
		}

		return null;
	}

	private Cursor getCursor(String tableName, SQLiteDatabase db) {
		return db.query(tableName, null, null, null, null, null, null);
	}

	private static Object byteToObject(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);

			obj = oi.readObject();

			bi.close();
			oi.close();
		} catch (Exception e) {
			mylog.e(TAG, "translation" + e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 返回数据库字段的类型
	 * 

	 * @return
	 */
	private String getDBFieldType(String fieldType) {
		if (ParseUtil.isStringType(fieldType)) {
			return "TEXT";
		}

		if (ParseUtil.isIntegerType(fieldType)) {
			return "INTEGER";
		}

		if (ParseUtil.isFloatType(fieldType)) {
			return "REAL";
		}

		if (ParseUtil.isDoubleType(fieldType)) {
			return "double";
		}

		if (ParseUtil.isByteType(fieldType)) {
			return "BLOB";
		}

		if (ParseUtil.isLongType(fieldType)) {
			return "LONG";
		}

		if (ParseUtil.isBooleanType(fieldType)) {
			return "BOOLEAN";
		}

		mylog.e(TAG, "methoed getDBFieldType erroe no match type " + fieldType);
		throw new RuntimeException("methoed getDBFieldType erroe no match type " + fieldType);
	}

	private String getIDFieldName(Class beanClass) {
		// List<Field> primaryKeyField = getBeanPrimarykeyField(beanClass);
		// if(primaryKeyField.size() > 0){
		// Field field = primaryKeyField.get(0);
		// return getFieldAndAnnotationName(field);
		// }
		Field[] field = Bean.class.getDeclaredFields();
		if (field.length < 1) {
			mylog.e(TAG, Bean.class.getName() + "中没有定义主键");
		}
		return field[0].getName();
	}

	private String getForeignKeyName(Bean foreignKey) {
		return getForeignKeyName(foreignKey.getClass());
	}

	private String getForeignKeyName(Class foreignKey) {
		String className = getClassLastName(foreignKey);
		return className + "_ID";
	}

	private String getClassLastName(Class<?> cc) {
		String tempName = cc.getName();
		String foreignKeyName = tempName.substring(tempName.lastIndexOf(".") + 1);
		return foreignKeyName;
	}

	private String getTableName(Class<?> cc) {
		Annotation annotations[] = cc.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof TableName) {
				return ((TableName) annotation).value();
			}
		}

		return getClassLastName(cc);
	}

	private String getFieldAndAnnotationName(Field field) {
		Annotation annotations[] = field.getDeclaredAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation instanceof TableFieldName) {
				return ((TableFieldName) annotation).value();
			}
		}
		String name = field.getName();
		return name;
	}

}
