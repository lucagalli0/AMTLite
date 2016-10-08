package com.edqueeneland.amtlite;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SetViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView bNumber;
    public TextView stopId;

    public SetViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        Typeface font = Typeface.createFromAsset(itemView.getContext().getAssets(), "Luminator6X9.TTF");
        bNumber = (TextView) itemView.findViewById(R.id.bnumber);
        bNumber.setTypeface(font);
        stopId = (TextView) itemView.findViewById(R.id.stopid);
    }
}
