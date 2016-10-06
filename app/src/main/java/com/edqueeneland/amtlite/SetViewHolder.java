package com.edqueeneland.amtlite;

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
        bNumber = (TextView) itemView.findViewById(R.id.bnumber);
        stopId = (TextView) itemView.findViewById(R.id.stopid);
    }
}
