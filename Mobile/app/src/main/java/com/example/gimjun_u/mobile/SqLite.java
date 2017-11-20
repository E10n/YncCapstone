package com.example.gimjun_u.mobile;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.content.Context;
import android.provider.UserDictionary;



public class SqLite extends SQLiteOpenHelper{
    public SqLite(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sql = "CREATE TABLE LocationTable(No INTEGER PRIMARY KEY AUTOINCREMENT,Location TEXT,isFingerAuth BOOLEAN,Date DATE);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        String sql = "DROP TABLE LocationTable;";
        db.execSQL(sql);
        onCreate(db);
    }

    public String select(int No){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT Location FROM LocationTable WHERE No="+No+";";
        String result = "";
        Cursor c = db.rawQuery(sql,null);
        while (c.moveToNext()){
            result += c.getString(0);
        }
        db.close();

        return result;
    }

    public void insert(String LocationDt,int isFingerAuth){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "INSERT INTO LocationTable VALUES(NULL,'"+LocationDt+"',"+isFingerAuth+",datetime('now'));";
        db.execSQL(sql);
        db.close();
    }

}