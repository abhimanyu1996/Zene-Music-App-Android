package com.example.kapils.zene;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

public class MyDBhandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="favourites.db";
    public static final String TABLE_FAVOURITES="favourites";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_SONGNAME="songname";

    public MyDBhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+TABLE_FAVOURITES+"("+
                COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_SONGNAME+" TEXT "+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_FAVOURITES);
        onCreate(db);
    }

    public void addFav(Favourites favourite){
        ContentValues values=new ContentValues();
        values.put(COLUMN_SONGNAME,favourite.get_songname());
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_FAVOURITES, null, values);
        db.close();
    }

    public void deleteFav(String songname){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_FAVOURITES + " WHERE " + COLUMN_SONGNAME + " =\"" + songname + "\";");
    }

    public ArrayList<File> getFav(){
        int index=0;
        String dbString[]=new String[100];
        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT * FROM "+TABLE_FAVOURITES+" WHERE 1";

        ArrayList<File> al = new ArrayList<File>();

        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            String k = c.getString(c.getColumnIndex("songname"));
            if(k!=null){
                    al.add(new File(k));
            }
        }
        db.close();
        return al;
    }
}