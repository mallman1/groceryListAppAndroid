package com.cvtc.android.grocerylistapp_mallman;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by mallman1 on 3/2/2015.
 */
public class ItemCursorAdapter extends CursorAdapter{

    public ItemCursorAdapter(Context context, Cursor itemCursor, int flags){
        super(context, itemCursor, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        ((ItemView) view).setItem(createItemFromCursor(cursor));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        ItemView itemView = new ItemView(context, createItemFromCursor(cursor));
        return itemView;
    }

    private GroceryItem createItemFromCursor(Cursor cursor){
        return new GroceryItem(cursor.getString(ItemTable.ITEM_COL_TEXT),
                cursor.getLong(ItemTable.ITEM_COL_ID));
    }
}
