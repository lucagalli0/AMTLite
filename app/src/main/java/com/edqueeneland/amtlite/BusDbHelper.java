package com.edqueeneland.amtlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BusDbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "bus";
    private static final int DB_VERSION = 10;

    public BusDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS STOPS");
        db.execSQL("CREATE TABLE STOPS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "BNUMBER INTEGER, "
                + "STOPID INTEGER);");
        db.execSQL("DROP TABLE IF EXISTS LAST");
        db.execSQL("CREATE TABLE IF NOT EXISTS LAST(_id INTEGER PRIMARY KEY, "
                + "BNUMBER INTEGER, "
                + "STOPID INTEGER);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS STOPS");
        db.execSQL("CREATE TABLE STOPS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "BNUMBER INTEGER, "
                + "STOPID INTEGER);");
        db.execSQL("DROP TABLE IF EXISTS LAST");
        db.execSQL("CREATE TABLE IF NOT EXISTS LAST(_id INTEGER PRIMARY KEY, "
                + "BNUMBER INTEGER, "
                + "STOPID INTEGER);");
    }


}