package com.edqueeneland.amtlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Results extends AppCompatActivity {

    Cursor cursor;
    List<String> lista = new ArrayList<>();
    ArrayAdapter<String> adapter;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Bus in arrivo tra...");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        String[] ris = getIntent().getExtras().getStringArray("RIS");
        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(ris));
        adapter = new ArrayAdapter<>(this, R.layout.mlistview, lst);

        ListView lv = (ListView) findViewById(R.id.listview);
        lv.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_results);
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
                        new Update().execute(stopId, bNumber);
                    }
                }
            }
        });
    }


    private class Update extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... params) {
            try {
                Connection.Response response = Jsoup.connect("http://www.amt.genova.it/amt/servizi/passaggi_tel.php")
                        .timeout(7000)
                        .method(Connection.Method.POST)
                        .data("CodiceFermata", params[0])
                        .data("conferma", "Conferma")
                        .execute();

                String nBus = String.format(Locale.ITALY, "%03d", Integer.valueOf(params[1]));
                Elements orario;
                orario = response.parse().select(":contains(" + nBus + ") + td +td +td");
                Log.d("DBG", "orario: " + orario.toString());
                if (orario.hasText())
                    lista = new ArrayList<>(Arrays.asList(orario.text().split(" ")));
                else
                    lista.add("Nessun bus con quel numero in arrivo :(");
            } catch (IOException e) {
                lista.add("Non sono riuscito a connettermi :(");
            }
            return lista;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            adapter.clear();
            adapter.addAll(strings);
            adapter.notifyDataSetChanged();
            refreshLayout.setRefreshing(false);
        }


    }
}
