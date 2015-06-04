package com.cvtc.android.grocerylistapp_mallman;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by mallman1 on 3/2/2015.
 */
public class ItemListAdapter extends BaseAdapter {

    private Context context;

    private List<GroceryItem> itemList;

    public ItemListAdapter(Context context, List<GroceryItem> itemList){
        this.context = context;
        this.itemList = itemList;
    }

    public int getCount(){
        return this.itemList.size();
    }

    public Object getItem(int position){
        return this.itemList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ItemView itemView = null;

        if (convertView == null){
            itemView = new ItemView(this.context, this.itemList.get(position));
        }
        else {
            itemView = (ItemView)convertView;
        }
        itemView.setItem(this.itemList.get(position));
        return itemView;
    }
}
