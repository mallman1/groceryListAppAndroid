package com.cvtc.android.grocerylistapp_mallman;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mallman1 on 3/2/2015.
 */
public class ItemDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "itemdatabase.db";

    public static final int DATABASE_VERSION = 1;
    public ItemDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){ItemTable.onCreate(database);}

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        ItemTable.onUpgrade(database, oldVersion, newVersion);
    }
}
