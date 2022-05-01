package com.example.androidnotes;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{

    private MainActivity mainact;
    private ArrayList<Notes> notelist = new ArrayList<>();

    public NoteAdapter(MainActivity mainact, ArrayList<Notes> notelist){
        this.mainact = mainact;
        this.notelist = notelist;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_layout, parent, false);

        itemView.setOnClickListener(mainact);
        itemView.setOnLongClickListener(mainact);

        return new NoteViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Notes n = notelist.get(position);

        String data1 = n.getTitle();
        if(data1.length() > 80)
            holder.titleview.setText(data1.substring(0,80)+" ...");
        else
            holder.titleview.setText(data1);

        String data2 = n.getData();
        if(data2.length() > 80)
            holder.dataview.setText(data2.substring(0,80)+" ...");
        else
            holder.dataview.setText(data2);

        holder.timeview.setText(n.getTime());

    }

    @Override
    public int getItemCount() {
        return notelist.size();
    }
}
