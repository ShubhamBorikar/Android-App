package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class CommitteeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_committee_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent intent = getIntent();
        String committee_id=intent.getStringExtra("committee_id");
        String name = intent.getStringExtra("name");
        String chamber = intent.getStringExtra("chamber");
        String parent_committee_id=intent.getStringExtra("parent_committee_id");
        String phone=intent.getStringExtra("phone");
        String office=intent.getStringExtra("office");
        if(intent.getStringExtra("name").equals(""))
        {
            name="NA";
        }
        if(intent.getStringExtra("parent_committee_id").equals(""))
        {
            parent_committee_id="NA";
        }
        if(intent.getStringExtra("phone").equals(""))
        {
            phone="NA";
        }
        if(intent.getStringExtra("office").equals(""))
        {
            office="NA";
        }


        TextView t;
        t = (TextView)findViewById(R.id.c1);
        t.setText(committee_id);
        t=(TextView)findViewById(R.id.c2);
        t.setText(name);
        t = (TextView)findViewById(R.id.c3);
        t.setText(chamber);
        t=(TextView)findViewById(R.id.c4);
        t.setText(parent_committee_id);
        t = (TextView)findViewById(R.id.c5);
        t.setText(phone);
        t=(TextView)findViewById(R.id.c6);
        t.setText(office);



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
