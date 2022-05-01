package com.example.androidnotes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private static final int CLICK_EDIT = 222;
    private static final int NEW_EDIT = 111;
    private RecyclerView recyclerView;
    private final ArrayList<Notes> notelist = new ArrayList<>();
    private NoteAdapter noteAdapter;
    private Notes notes;
    private int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.noterecy);
        noteAdapter = new NoteAdapter(this, notelist);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notelist.addAll(loadfile());
        titleCount();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.info_menu) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.add_menu) {
            Intent intent = new Intent(this, EditActivity.class);
            startActivityForResult(intent,NEW_EDIT);
        } else {
            Toast.makeText(this,"Not a valid Option", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        p = recyclerView.getChildLayoutPosition(view);
        Notes n = notelist.get(p);

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("note",n);
        startActivityForResult(intent,CLICK_EDIT);
    }

    @Override
    public boolean onLongClick(View view) {
        p = recyclerView.getChildLayoutPosition(view);
        Notes n = notelist.get(p);
        String t = n.getTitle();
        if(t.length() > 80)
            t = t.substring(0,80)+" ...";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String finalT = t;
        builder.setPositiveButton("YES", (dialog, id) -> {
            notelist.remove(p);
            noteAdapter.notifyItemRemoved(p);
            saveNote();
            titleCount();

            Toast.makeText(this,"Note '"+ finalT +"' is Deleted",Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("NO", (dialog, id) -> {

        });
        builder.setMessage("Delete Note '"+ t +"'?");

        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    public void titleCount(){
        if(notelist.size() == 0){
            this.setTitle("Android Notes");
        }else{
            this.setTitle("Android Notes ("+ notelist.size()+")");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int reqcode,int rescode,Intent intent){
        super.onActivityResult(reqcode,rescode,intent);
        if((reqcode == NEW_EDIT) || (reqcode == CLICK_EDIT)){
            if(rescode == RESULT_OK){
                if(reqcode == CLICK_EDIT){
                    notelist.remove(p);
                    noteAdapter.notifyDataSetChanged();
                }
                if(intent.hasExtra("note")){
                    Notes n = (Notes) intent.getSerializableExtra("note");
                    notelist.add(0,n);
                    saveNote();
                    titleCount();
                    noteAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(this,"Error in data",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(this,"Error!!!",Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Notes> loadfile(){

        ArrayList<Notes> notes1 = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput("Notes.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title1 = jsonObject.getString("title");
                String data1 = jsonObject.getString("data");
                String time1 = jsonObject.getString("time-date");
                Notes n = new Notes(title1, data1,time1);
                notes1.add(n);
            }

            this.setTitle("Android Notes ("+ notelist.size()+")");
        } catch (FileNotFoundException e) {
//            Toast.makeText(this, "no json file", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notes1;
    }

    private void saveNote() {
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput("Notes.json", Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(notelist);
            printWriter.close();
            fos.close();

            //Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}