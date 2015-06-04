package com.cvtc.android.grocerylistapp_mallman;


import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.ActionMode.Callback;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;


public class AdvancedGroceryList extends SherlockFragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    protected ArrayList<GroceryItem> marrItemList;

    protected ItemCursorAdapter mItemAdapter;

    protected ListView mvwItemLayout;
    protected EditText mvwItemEditText;
    protected Menu mvwMenu;
    protected Button mvwItemButton;

    protected static final String SAVED_EDIT_TEXT = "mvwItemEditText";

    private Callback mActionModeCallback;
    private ActionMode mActionMode;

    private int selected_position;

    private static final int LOADER_ID = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.mItemAdapter = new ItemCursorAdapter(this, null, 0);

        initLayout();
        initAddItemListeners();

        this.getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        mvwItemEditText.setText(getPreferences(MODE_PRIVATE).getString(SAVED_EDIT_TEXT, ""));

    }


    protected void initLayout(){
        this.setContentView(R.layout.advanced);
        this.mvwItemLayout = (ListView)this.findViewById(R.id.itemListViewGroup);
        this.mvwItemLayout.setAdapter(mItemAdapter);

        this.mvwItemLayout.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mActionMode != null){
                    return false;
                }
                mActionMode = getSherlock().startActionMode(mActionModeCallback);
                selected_position = position;
                return true;
            }
        });

        this.mvwItemEditText = (EditText)this.findViewById(R.id.newitemEditText);
        this.mvwItemButton = (Button)this.findViewById(R.id.addItemButton);

        mActionModeCallback = new Callback(){
            public boolean onCreateActionMode(ActionMode mode, Menu menu){
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.actionmenu, menu);
                return true;
            }

            public boolean onPrepareActionMode(ActionMode mode, Menu menu){ return false;}

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_remove:
                        removeItem(((ItemView) mvwItemLayout.getChildAt(selected_position)).getItem());
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

                public void onDestroyActionMode(ActionMode mode){mActionMode= null;}
            };

    }

    protected void initAddItemListeners(){
        this.mvwItemButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemText = mvwItemEditText.getText().toString();
                if (itemText != null && !itemText.equals("")) {
                    addItem(new GroceryItem(itemText));
                    mvwItemEditText.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mvwItemEditText.getWindowToken(), 0);
                }
            }
        });

        this.mvwItemEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String itemText = mvwItemEditText.getText().toString();
                    if (itemText != null && !itemText.equals("")) {
                        addItem(new GroceryItem(itemText));
                        mvwItemEditText.setText("");
                        return true;
                    }
                }
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mvwItemEditText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
    }

    protected void addItem(GroceryItem item){
        Uri uri = Uri.parse(ItemContentProvider.CONTENT_URI + "/items/" + item.getID());
        uri = getContentResolver().insert(uri, getContentValuesForItem(item));
        int id=Integer.parseInt(uri.getLastPathSegment());
        item.setID(id);

        refreshView();
    }

    @Override
    public void onPause(){
        super.onPause();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        preferences.edit().putString(SAVED_EDIT_TEXT, mvwItemEditText.getText().toString()).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(SAVED_EDIT_TEXT, this.mvwItemEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if(null != savedInstanceState && savedInstanceState.containsKey(SAVED_EDIT_TEXT)){
            this.mvwItemEditText.setText(savedInstanceState.getString(SAVED_EDIT_TEXT));
        }
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        String[] projection = {ItemTable.ITEM_KEY_ID, ItemTable.ITEM_KEY_TEXT};
        Uri uri = Uri.parse(ItemContentProvider.CONTENT_URI + "");
        CursorLoader cursorLoader = new CursorLoader(this, uri, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){
        this.mItemAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){this.mItemAdapter.swapCursor(null);}

        public void onItemChanged(ItemView view, GroceryItem item){
        Uri uri = Uri.parse(ItemContentProvider.CONTENT_URI + "/items/" + item.getID());
        getContentResolver().update(uri, getContentValuesForItem(item), null, null);

        refreshView();
    }

    private ContentValues getContentValuesForItem(GroceryItem item){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemTable.ITEM_KEY_TEXT, item.getItem());
        return contentValues;
    }

    protected void removeItem(GroceryItem item){
        Uri uri = Uri.parse(ItemContentProvider.CONTENT_URI + "/items/" + item.getID());
        getContentResolver().delete(uri, null, null);
        refreshView();
    }

    private void refreshView(){
        this.getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        this.mvwItemLayout.setAdapter(mItemAdapter);
    }





}
