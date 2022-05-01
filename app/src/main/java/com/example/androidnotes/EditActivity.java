package com.example.androidnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private EditText title;
    private EditText data;
    private String etitle = "";
    private String edata = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.edit_activity_title);
        data = findViewById(R.id.edit_activity_data);

        Intent intent = getIntent();
        if(intent.hasExtra("note")){
            Notes n = new Notes();
            n = (Notes) intent.getSerializableExtra("note");
            if (n !=null) {
                title.setText(n.getTitle());
                data.setText(n.getData());
                etitle = n.getTitle();
                edata = n.getData();
            }
        }
        title.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save_menu) {
            save();
        } else {
            Toast.makeText(this,"Error!!!",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(title.getText().toString().equals(etitle) && data.getText().toString().equals(edata)){
            finish();
        }

        else{
            String t = title.getText().toString();
            if(t.length() > 80)
                t = t.substring(0,80)+" ...";


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("YES", (dialog, id) -> save());
            builder.setNegativeButton("NO", (dialog, id) -> finish());
            builder.setMessage("Your Note is not saved! \nSave Note '"+t+"'?");
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void save(){

        if(title.getText().toString().equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("OK", (dialog, id) -> finish());
            builder.setNegativeButton("CANCEL", (dialog, id) -> title.requestFocus());
            builder.setMessage("Title is empty\nNote will not be Saved without Title");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else if (data.getText().toString().equals("")){
//                Toast.makeText(this,"You have Saved Note Without Note Text",Toast.LENGTH_SHORT).show();    //if you want to show toast
//                fun();
//                finish();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);                              //if you want to show alert dialog
            builder.setPositiveButton("OK", (dialog, id) -> fun());
            builder.setNegativeButton("CANCEL", (dialog, id) -> data.requestFocus());
            builder.setMessage("Note Text is empty\nDo you want to Save Note without text?");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else if(title.getText().toString().equals(etitle) && data.getText().toString().equals(edata)){
            finish();
        }

        else {
            fun();
        }
    }

    public void fun(){
        Date dt = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd',' hh:mm a");

        Notes n = new Notes();
        n.setTitle(title.getText().toString());
        n.setData(data.getText().toString());
        n.setTime(dateFormat.format(dt));

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("note", n);
        setResult(RESULT_OK, intent);
        finish();
    }
}