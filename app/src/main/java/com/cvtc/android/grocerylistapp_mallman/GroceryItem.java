package com.cvtc.android.grocerylistapp_mallman;

/**
 * Created by mallman1 on 3/2/2015.
 */
public class GroceryItem {

    private String mstrItem;
    private long mnID;

    public GroceryItem(){
        this.mstrItem = "";
    }

    public GroceryItem(String strItem){
        this.mstrItem = strItem;
    }

    public GroceryItem(String strItem, long id){
        this.mstrItem = strItem;
        this.mnID = id;
    }

    public String getItem(){
        return mstrItem;
    }
    public void setItem(String item){
        this.mstrItem=item;
    }
    public long getID(){
        return mnID;
    }
    public void setID(long id){
        mnID = id;
    }

    public String toString(){return this.mstrItem;}

    public boolean equals(Object obj){return obj instanceof GroceryItem && ((GroceryItem) obj).getID() == mnID; }
}
