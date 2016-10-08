package com.edqueeneland.amtlite;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class Insert extends AppCompatActivity {

    static ProgressBar bar;
    EditText bus;
    EditText fermata;
    Button button;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Inserisci i dati manualmente");
        button = (Button) findViewById(R.id.button);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.INVISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus = (EditText) findViewById(R.id.nBus);
                fermata = (EditText) findViewById(R.id.nFermata);
                bar.setVisibility(View.VISIBLE);
                hideKeyboard(Insert.this);
                String nFermata = fermata.getText().toString();
                String nBus = bus.getText().toString();
                if (!nFermata.equals("") && !nBus.equals("")) {
                    ContentValues last = new ContentValues();
                    last.put("BNUMBER", nBus);
                    last.put("STOPID", nFermata);
                    SQLiteOpenHelper helper = new BusDbHelper(Insert.this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.update("LAST", last, "_id = ?", new String[]{"0"});
                    new ParseAMT(Insert.this).execute(nFermata, nBus, "false");
                }
                else Toast.makeText(Insert.this, "Inserisci numeri bus e fermata", Toast.LENGTH_LONG);
            }
        });
    }
}




