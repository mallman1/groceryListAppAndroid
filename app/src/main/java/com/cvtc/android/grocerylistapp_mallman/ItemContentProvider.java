package com.cvtc.android.grocerylistapp_mallman;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;



/**
 * Created by mallman1 on 3/2/2015.
 */
public class ItemContentProvider extends ContentProvider {

    private ItemDatabaseHelper database;

    private static final int ITEM_ID = 1;

    private static final String AUTHORITY = "com.cvtc.android.contentprovider";
    private static final String BASE_PATH = "item_table";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/items/#", ITEM_ID);
    }

    @Override
    public boolean onCreate(){
        this.database = new ItemDatabaseHelper(getContext(),
                ItemDatabaseHelper.DATABASE_NAME, null,
                ItemDatabaseHelper.DATABASE_VERSION);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder){
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(ItemTable.DATABASE_TABLE_ITEM);

        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase database = this.database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, null, null, null, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri){return null;}

    @Override
    public Uri insert(Uri uri, ContentValues values){
        SQLiteDatabase sqlDB = this.database.getWritableDatabase();
        long id=0;
        int uriType = sURIMatcher.match(uri);

        switch(uriType){
            case ITEM_ID:
                id = sqlDB.insert(ItemTable.DATABASE_TABLE_ITEM, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.parse(BASE_PATH + "/" + id);


    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs ){
        SQLiteDatabase database = this.database.getWritableDatabase();

        int rowsDeleted = 0;

        int uriType = sURIMatcher.match(uri);
        switch(uriType){
            case ITEM_ID:
                String id = uri.getLastPathSegment();
                rowsDeleted = database.delete(ItemTable.DATABASE_TABLE_ITEM, ItemTable.ITEM_KEY_ID + "=" + id, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }

        if (rowsDeleted > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs ){
        SQLiteDatabase database = this.database.getWritableDatabase();

        int rowsUpdated = 0;

        int uriType = sURIMatcher.match(uri);
       switch(uriType){
           case ITEM_ID:
           String id = uri.getLastPathSegment();
               if (!TextUtils.isEmpty(selection)){
                   rowsUpdated = database.update(ItemTable.DATABASE_TABLE_ITEM, values, ItemTable.ITEM_KEY_ID + "=" + id + "AND" + selection, null);

               }else{
                   rowsUpdated = database.update(ItemTable.DATABASE_TABLE_ITEM, values, ItemTable.ITEM_KEY_ID + "=" + id, null);
               }
               break;
           default:
               throw new IllegalArgumentException("Unknown URI: " + uri);
       }
       if (rowsUpdated>0){
           getContext().getContentResolver().notifyChange(uri, null);
       }
       return rowsUpdated;
    }

    private void checkColumns(String[] projection){
        String[] available = {ItemTable.ITEM_KEY_ID, ItemTable.ITEM_KEY_TEXT};

        if (projection != null){
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            if(!availableColumns.containsAll(requestedColumns)){
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
