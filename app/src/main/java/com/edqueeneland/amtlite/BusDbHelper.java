package com.edqueeneland.amtlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BusDbHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "bus";
    private static final int DB_VERSION = 4;

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
        //TODO riempire db
        ContentValues busValues = new ContentValues();
        busValues.put("NAME", "caricamento");
        busValues.put("BNUMBER", 23);
        busValues.put("STOPID", 4234);
        db.insert("STOPS", null, busValues);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS STOPS");
        db.execSQL("CREATE TABLE STOPS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "BNUMBER INTEGER, "
                + "STOPID INTEGER);");
        ContentValues busValues = new ContentValues();
        busValues.put("NAME", "caricamento");
        busValues.put("BNUMBER", 23);
        busValues.put("STOPID", 4234);
        db.insert("STOPS", null, busValues);
    }


}