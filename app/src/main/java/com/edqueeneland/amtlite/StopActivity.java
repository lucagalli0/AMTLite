package com.edqueeneland.amtlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StopActivity extends AppCompatActivity{

    private RecyclerView rv;
    private BusDbHelper helper;
    private SQLiteDatabase db;
    private List<Stop> stops = new ArrayList<Stop>();
    private Cursor cursor;
    private StopAdapter adapter;
    private Paint p = new Paint();
    static ProgressBar bar;

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("Fermate preferite");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.INVISIBLE);
        rv = (RecyclerView) findViewById(R.id.recview);
        adapter = new StopAdapter(this, stops);
        try {
            helper = new BusDbHelper(this);
            db = helper.getWritableDatabase();
            cursor = db.query("STOPS", new String[] {"_id", "NAME", "BNUMBER", "STOPID"}, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        Stop stop = new Stop(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
                        Log.d("AZZZZZ", "cursor:" + cursor.getString(1));
                        stops.add(stop);
                    } while (cursor.moveToNext());
                    adapter.notifyDataSetChanged();
                }

            }
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Database morto", Toast.LENGTH_LONG);
            toast.show();
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new SimpleDividerItemDecoration(getBaseContext(), getResources()));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StopActivity.this, EditActivity.class);
                intent.putExtra("EDIT", false);
                startActivity(intent);
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2 .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StopActivity.this, Insert.class);
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Stop saved = stops.get(position);
                adapter.removeItem(position);
                if (direction == ItemTouchHelper.LEFT){
                    Snackbar.make(coordinatorLayout, "La fermata verrà cancellata", Snackbar.LENGTH_LONG)
                            .setAction("Annulla", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    adapter.addItem(saved);
                                }
                            }).setCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar snackbar, int event) {
                            super.onDismissed(snackbar, event);
                            if (event != DISMISS_EVENT_ACTION) {
                                try {
                                    SQLiteOpenHelper helper = new BusDbHelper(StopActivity.this);
                                    SQLiteDatabase db = helper.getWritableDatabase();
                                    //Log.d("ID", Integer.toString(stops.get(position).getId()));
                                    db.delete("STOPS", "_id = ?", new String[]{Integer.toString(saved.getId())});
                                } catch (SQLException e) {
                                    Toast.makeText(StopActivity.this, "Db irraggiungibile", Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }).show();

                } else {
                    Intent intent = new Intent(StopActivity.this, EditActivity.class);
                    //Log.d("SWIPE", "pos: "+position);
                    intent.putExtra("ID", saved.getId());
                    intent.putExtra("EDIT", true);
                    startActivity(intent);
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }
}
