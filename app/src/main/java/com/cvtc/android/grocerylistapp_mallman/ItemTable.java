package com.cvtc.android.grocerylistapp_mallman;

import android.database.sqlite.SQLiteDatabase;
/**
 * Created by mallman1 on 3/2/2015.
 */
public class ItemTable {
    public static final String DATABASE_TABLE_ITEM = "item_table";
    public static final String ITEM_KEY_ID = "_id";
    public static final int ITEM_COL_ID = 0;

    public static final String ITEM_KEY_TEXT = "item_text";
    public static final int ITEM_COL_TEXT = ITEM_COL_ID + 1;

    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE_ITEM + " (" +
            ITEM_KEY_ID + " integer primary key autoincrement, " +
            ITEM_KEY_TEXT + " text not null);";
    public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE_ITEM;
    public static void onCreate(SQLiteDatabase database) {database.execSQL(DATABASE_CREATE);}
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL(DATABASE_DROP);
        onCreate(database);
    }
}
