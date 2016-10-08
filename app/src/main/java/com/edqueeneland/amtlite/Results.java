package com.edqueeneland.amtlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Results extends AppCompatActivity {

    Cursor cursor;
    static String rip = "";
    List<String> a = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Bus in arrivo tra...");

        String[] ris = getIntent().getExtras().getStringArray("RIS");
        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(ris));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mlistview, lst);

        ListView lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_results);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SQLiteOpenHelper helper = new BusDbHelper(Results.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                cursor = db.query("LAST", new String[] {"BNUMBER", "STOPID", "RESULT"}, "_id = ?", new String[]{"0"}, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        String bNumber = String.valueOf(cursor.getInt(0));
                        String stopId = String.valueOf(cursor.getInt(1));
                        try {
                            a = new ParseAMT(Results.this).execute(stopId, bNumber, "true").get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        adapter.clear();
                        adapter.addAll(a);
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }
}
