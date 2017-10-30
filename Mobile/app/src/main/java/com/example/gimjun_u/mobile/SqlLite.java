package com.example.gimjun_u.mobile;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.content.Context;


public class SqlLite extends SQLiteOpenHelper{
    public SqlLite(Context context){
        super(context,"aaa.db",null,1);


    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE TestTable(testvalue INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int i,int i1){

    }

}