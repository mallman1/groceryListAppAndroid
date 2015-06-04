package com.cvtc.android.grocerylistapp_mallman;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mallman1 on 3/2/2015.
 */
public class ItemView extends LinearLayout {
    private TextView mvwItemText;

    private GroceryItem mItem;

    public ItemView(Context context, GroceryItem item){
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_view, this, true);
        this.mvwItemText = (TextView)findViewById(R.id.itemTextView);
        this.setItem(item);
    }

    public GroceryItem getItem(){return this.mItem;}

    public void setItem(GroceryItem item){
        this.mItem = item;
        this.mvwItemText.setText(mItem.getItem());
        requestLayout();
    }
}
