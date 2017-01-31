package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

import static android.R.id.edit;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.myfirstapp.R.id.checkBox;
import static com.example.myfirstapp.R.id.fb;
import static com.example.myfirstapp.R.id.imageView;
import static com.example.myfirstapp.R.id.legdetail;
import static com.example.myfirstapp.R.id.legname;
import static com.example.myfirstapp.R.id.search_go_btn;
import static com.example.myfirstapp.R.id.web;

public class LegislatorDetails extends AppCompatActivity {

    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislator_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email=intent.getStringExtra("email");
        String url=intent.getStringExtra("url");
        String party=intent.getStringExtra("party");
        String chamber = intent.getStringExtra("chamber");
        chamber=chamber.substring(0,1).toUpperCase()+chamber.substring(1);
        String phone=intent.getStringExtra("phone");
        String term_start = intent.getStringExtra("term_start");
        String term_end=intent.getStringExtra("term_end");
        //String term = intent.getStringExtra("name");
        String office=intent.getStringExtra("office");
        String state = intent.getStringExtra("state");
        String fax=intent.getStringExtra("fax");
        String birthday = intent.getStringExtra("birthday");
        final String fbid = intent.getStringExtra("fbid");
        final String twid=intent.getStringExtra("twid");
        final String website = intent.getStringExtra("website");





        ImageView imageView=(ImageView)findViewById(R.id.legdetail);
        Picasso.with(context).load(url).into(imageView);

        ImageView imageparty=(ImageView)findViewById(R.id.imageparty);
        TextView tvparty=(TextView) findViewById(R.id.partyname);
        if(party.equals("R"))
        {

            imageparty.setImageResource(R.drawable.r);
            tvparty.setText("Republicans");
        }
        else
        {

            imageparty.setImageResource(R.drawable.d);
            tvparty.setText("Democrats");
        }

        /*Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, website, duration);
        toast.show();*/




        ImageView img = (ImageView)findViewById(R.id.fb);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(fbid.equals("null"))
                {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,"No Facebook URL", duration);
                    toast.show();
                }
                else
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://facebook.com/"+fbid));
                    startActivity(intent);
                }

            }
        });

        //twitter
        ImageView imgt = (ImageView)findViewById(R.id.tw);
        imgt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(twid.equals("null"))
                {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,"No Twitter URL", duration);
                    toast.show();
                }
                else
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://twitter.com/"+twid));
                    startActivity(intent);
                }

            }
        });

        //website
        ImageView imgw = (ImageView)findViewById(R.id.web);
        imgw.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(website.equals("null"))
                {
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context,"No Website URL", duration);
                    toast.show();
                }
                else
                {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(website));
                    startActivity(intent);
                }

            }
        });

        TextView t;
        t = (TextView)findViewById(R.id.tv12);
        t.setText(name);
        t=(TextView)findViewById(R.id.tv13);
        t.setText(email);
        t = (TextView)findViewById(R.id.tv14);
        t.setText(chamber);
        t=(TextView)findViewById(R.id.tv15);
        t.setText(phone);
        t = (TextView)findViewById(R.id.tv16);
        t.setText(term_start);
        t=(TextView)findViewById(R.id.tv17);
        t.setText(term_end);
        //t = (TextView)findViewById(R.id.tv18);
        //t.setText("Progress Bar Here");
        t=(TextView)findViewById(R.id.tv19);
        t.setText(office);
        t = (TextView)findViewById(R.id.tv20);
        t.setText(state);
        t=(TextView)findViewById(R.id.tv21);
        t.setText(fax);
        t = (TextView)findViewById(R.id.tv22);
        t.setText(birthday);




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
