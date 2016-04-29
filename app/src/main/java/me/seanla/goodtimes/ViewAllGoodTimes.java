package me.seanla.goodtimes;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ViewAllGoodTimes extends ListActivity {
    private DbAdapter dbHelperRead;
    private DbAdapter dbHelperWrite;
    private ListView list;
    private int currentListItemIndex;
    private ActionMode currentActionMode;


    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Action");
            mode.getMenuInflater().inflate(R.menu.actions_textview, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch(item.getItemId()) {
                case R.id.menu_delete:
                    Cursor cursor = (Cursor) list.getItemAtPosition(currentListItemIndex);
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

                    dbHelperWrite.openForWriting();
                    dbHelperWrite.deleteGoodTime(id);
                    dbHelperWrite.close();

                    mode.finish();
                    displayListView();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            currentActionMode = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_good_times);

        dbHelperWrite = new DbAdapter(this);
        dbHelperRead = new DbAdapter(this);
        dbHelperRead.openForReading();

        displayListView();

    }

    private void displayListView() {
        Cursor cursor = dbHelperRead.fetchAllGoodTimes();

        String[] columns = new String[] {
                DbAdapter.KEY_DATE,
                DbAdapter.KEY_EVENT
        };

        int[] to = new int[] {
                R.id.date,
                R.id.details
        };

        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(
                this, R.layout.goodtime_info,
                cursor,
                columns,
                to,
                0);

        list = (ListView) findViewById(android.R.id.list);

        list.setAdapter(dataAdapter);

        list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (currentActionMode != null) {
                    return;
                }
                currentListItemIndex = position;
                currentActionMode = startActionMode(modeCallBack);
                view.setSelected(true);
                return;
            }
        });
    }
}
