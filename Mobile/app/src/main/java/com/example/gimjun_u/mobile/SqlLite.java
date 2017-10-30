package com.example.gimjun_u.mobile;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.content.Context;


public class SqlLite extends SQLiteOpenHelper{
    public SqlLite(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);


    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE LocationTable(No INTEGER PRIMARY KEY AUTOINCREMENT,Latitude INTEGER,Longitude INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int i,int i1){

    }

    public void insert(int longitude,int latitude){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO LocationTable VALUES(NULL"+latitude+","+longitude+");");
        db.close();
    }

}