package com.example.gimjun_u.mobile;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.content.Context;


public class SqLite extends SQLiteOpenHelper{
    public SqLite(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE LocationTable(No INTEGER PRIMARY KEY AUTOINCREMENT,Latitude INTEGER,Longitude INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int i,int i1){

    }

    public Double select(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT Latitude FROM LocationTable WHERE No = 9;";
        Double result = 0.0;
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext()){
            result += c.getDouble(0);
        }

        return result;
    }

    public void insert(Double latitude,Double longitude){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO LocationTable VALUES(NULL,"+latitude+","+longitude+");");
        db.close();
    }

}