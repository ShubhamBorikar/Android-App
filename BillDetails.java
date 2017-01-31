package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BillDetails extends AppCompatActivity {

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        String bill_id = intent.getStringExtra("bill_id");
        String title=intent.getStringExtra("title");
        String bill_type=intent.getStringExtra("bill_type").toUpperCase();
        String sponsorfull=intent.getStringExtra("sponsorfull");
        String chamber = intent.getStringExtra("chamber");
        chamber=chamber.substring(0,1).toUpperCase()+chamber.substring(1);
        String status=intent.getStringExtra("status");
        String intro_on = intent.getStringExtra("intro_on");
        String congress_url=intent.getStringExtra("congress_url");
        //String term = intent.getStringExtra("name");
        String version_status=intent.getStringExtra("version_status");
        String bill_url = intent.getStringExtra("bill_url");

        //int duration = Toast.LENGTH_LONG;
        //Toast toast = Toast.makeText(context, bill_id+" "+sponsorfull, duration);
        //toast.show();

        TextView t;
        t = (TextView)findViewById(R.id.tv12);
        t.setText(bill_id);
        t=(TextView)findViewById(R.id.tv13);
        t.setText(title);
        t = (TextView)findViewById(R.id.tv14);
        t.setText(bill_type);
        t=(TextView)findViewById(R.id.tv15);
        t.setText(sponsorfull);
        t = (TextView)findViewById(R.id.tv16);
        t.setText(chamber);
        t=(TextView)findViewById(R.id.tv17);
        t.setText(status);
        t = (TextView)findViewById(R.id.tv18);
        t.setText(intro_on);
        t=(TextView)findViewById(R.id.tv19);
        t.setText(congress_url);
        t = (TextView)findViewById(R.id.tv20);
        t.setText(version_status);
        t=(TextView)findViewById(R.id.tv21);
        t.setText(bill_url);



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
