package com.edqueeneland.amtlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    Button salva;
    EditText editDesc;
    EditText editNumero;
    EditText editCodice;
    ContentValues busValues;
    Cursor cursor;
    Integer pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Aggiungi una fermata preferita");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        editDesc = (EditText) findViewById(R.id.editDesc);
        editNumero = (EditText) findViewById(R.id.editNumero);
        editCodice = (EditText) findViewById(R.id.editCodice);
        busValues = new ContentValues();
        salva = (Button) findViewById(R.id.salva);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pos = extras.getInt("ID");
            Log.d("Edit", "pos: "+pos);
            try {
                SQLiteOpenHelper helper = new BusDbHelper(EditActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                cursor = db.query("STOPS", new String[]{"_id", "NAME", "BNUMBER", "STOPID"}, "_id = ?", new String[]{Integer.toString(pos)}, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        editDesc.setText(cursor.getString(1));
                        editNumero.setText(String.valueOf(cursor.getInt(2)));
                        editCodice.setText(String.valueOf(cursor.getInt(3)));
                    }
                }
            } catch (SQLException e) {
                Toast.makeText(EditActivity.this, "Db irraggiungibile", Toast.LENGTH_LONG);
            }

            salva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("AZZZZ", "try "+pos);
                        busValues.put("NAME", editDesc.getText().toString());
                        busValues.put("BNUMBER", Integer.valueOf(editNumero.getText().toString()));
                        busValues.put("STOPID", Integer.valueOf(editCodice.getText().toString()));
                        SQLiteOpenHelper helper = new BusDbHelper(EditActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        if (getIntent().getExtras().getBoolean("EDIT"))
                            db.update("STOPS", busValues, "_id = ?", new String[]{Integer.toString(pos)});
                        else
                            db.insert("STOPS", null, busValues);
                        Toast.makeText(EditActivity.this, "inserito", Toast.LENGTH_LONG);
                        db.close();
                    } catch (SQLException e) {
                        Toast.makeText(EditActivity.this, "Db irraggiungibile", Toast.LENGTH_LONG);
                    }
                    Intent intent = new Intent(EditActivity.this, StopActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
