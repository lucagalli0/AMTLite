package com.edqueeneland.amtlite;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class StopAdapter extends RecyclerView.Adapter<SetViewHolder>{


    private Activity activity;
    List<Stop> stops;

    public StopAdapter(Activity activity, List<Stop> stops) {
        this.activity = activity;
        this.stops = stops;
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_stop, parent, false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, final int position) {
        holder.name.setText(stops.get(position).getName());
        holder.bNumber.setText(String.valueOf(stops.get(position).getbNumber()));
        holder.stopId.setText(String.valueOf(stops.get(position).getStopId()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopActivity.bar.setVisibility(View.VISIBLE);
                ContentValues last = new ContentValues();
                last.put("_id", "0");
                last.put("BNUMBER", String.valueOf(stops.get(position).getbNumber()));
                last.put("STOPID", String.valueOf(stops.get(position).getStopId()));
                SQLiteOpenHelper helper = new BusDbHelper(v.getContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                db.insertWithOnConflict("LAST", null, last, SQLiteDatabase.CONFLICT_REPLACE);
//                Cursor cursor;
//                cursor = db.query("LAST",new String[] {"_id", "BNUMBER", "STOPID"}, null, null, null, null, null);
//                cursor.moveToFirst();
//                Log.d("Adapter", String.valueOf(cursor.getInt(0)) + " " + String.valueOf(cursor.getInt(1)) + " " + String.valueOf(cursor.getInt(2)));
                db.close();
                new ParseAMT(v.getContext()).execute(String.valueOf(stops.get(position).getStopId()), String.valueOf(stops.get(position).getbNumber()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    public void addItem(Stop stop) {
        stops.add(stop);
        notifyItemInserted(stops.size());
    }

    public void removeItem (int pos) {
        stops.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, stops.size());
    }

}
