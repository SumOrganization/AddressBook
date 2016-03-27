package com.jason.addressbook.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Created by Jason on 2016/3/12.
 */
public class DatabaseTool {


    public static SQLiteDatabase openDatabase() {
       // SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase("/data/data/com.jason.addressbook.db/databases/contacts.db", null);
        SQLiteDatabase  sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase("/sdcard/contacts.db",null,null);
        return sqLiteDatabase;

    }

    public static void creatTable(SQLiteDatabase db) {
        String sql = "create table constacts_table(user_name varchar(255)," + "user_phonenumber varchar(255) primary key," +
                "user_sex varchar(255),"+"user_photo BLOB)";
        db.execSQL(sql);
    }

    public static void insertTable(SQLiteDatabase db, String user_name, String user_phonenumber, String user_sex,byte[] img) {
       /* ContentValues cv = new ContentValues();
        cv.put("user_photo", img);*/
        String sql = "insert into constacts_table(user_name,user_phonenumber,user_sex，user_photo) values(user_name,user_honenumber,user_sex,img)";
    }
    public static String getUserPhoneFromName(SQLiteDatabase db,String user_name)
    {      String[] args = new String[] {user_name};
           String strValue = null;
           Cursor cursor =  db.query("constacts_table",new String[]{"user_phonenumber"},"user_name=?",args,null,null,null);
        while(cursor.moveToNext())
        {
            //根据列名获取列索引
            int nameColumnIndex = cursor.getColumnIndex("user_name");
              strValue = cursor.getString(nameColumnIndex);
        }
        return strValue;
    }

    public static  boolean tableisExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        Cursor cursor = null;

        String sql = "select count(*)  from sqlite_master where type='table' and name = 'tableName' ";
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                result = true;
            }

        }
        return result;
    }
}
