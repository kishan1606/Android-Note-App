package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    TextView titleview;
    TextView timeview;
    TextView dataview;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        titleview = itemView.findViewById(R.id.titleview);
        timeview = itemView.findViewById(R.id.timeview);
        dataview = itemView.findViewById(R.id.dataview);
    }
}
