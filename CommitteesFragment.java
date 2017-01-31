package com.example.myfirstapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;



/**
 * A simple {@link Fragment} subclass.
 */
public class CommitteesFragment extends Fragment {

    private String TAG = CommitteesFragment.class.getSimpleName();
    private ListView lv,lv2,lv3;

    // URL to get contacts JSON
    private static String url = "http://cis-env.us-west-2.elasticbeanstalk.com/index.php/androidssb.php?type=comm";


    ArrayList<HashMap<String, String>> contactList,contactListSenate,contactListJoint;
    private TabHost tab;


    public CommitteesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_committees, container, false);
        View view=inflater.inflate(R.layout.fragment_committees, container, false);
        tab = (TabHost) view.findViewById(R.id.commtab);
        tab.setup();

        contactList = new ArrayList<>();
        contactListSenate = new ArrayList<>();
        contactListJoint = new ArrayList<>();

        lv = (ListView) view.findViewById(R.id.listviewchouse);
        lv2 = (ListView) view.findViewById(R.id.listviewcsenate);
        lv3 = (ListView) view.findViewById(R.id.listviewcjoint);

        new GetContactsCom().execute();

        //tab1
        TabHost.TabSpec spec=tab.newTabSpec("House");
        spec.setContent(R.id.tab1);
        spec.setIndicator("House");
        tab.addTab(spec);

        //tab2
        spec=tab.newTabSpec("Senate");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Senate");
        tab.addTab(spec);

        //tab3
        spec=tab.newTabSpec("Joint");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Joint");
        tab.addTab(spec);


        return view;
    }

    private class GetContactsCom extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String committee_id = c.getString("committee_id");
                        String name = c.getString("name");
                        String chamber=c.getString("chamber").substring(0,1).toUpperCase()+c.getString("chamber").substring(1);


                        String parent_committee_id=c.optString("parent_committee_id");
                        String phone=c.optString("phone");
                        String office=c.optString("office");



                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("committee_id", committee_id);
                        contact.put("name", name);
                        contact.put("chamber",chamber);
                        contact.put("parent_committee_id",parent_committee_id);
                        contact.put("phone",phone);
                        contact.put("office",office);





                        // adding contact to contact list
                        if(chamber.toLowerCase().equals("house"))
                        {
                            contactList.add(contact);
                        }

                        else if(chamber.toLowerCase().equals("senate"))
                        {
                            contactListSenate.add(contact);
                        }
                        else if(chamber.toLowerCase().equals("joint"))
                        {
                            contactListJoint.add(contact);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            /**
             * Updating parsed JSON data into ListView
             * */
            /*
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.legrow, new String[]{"name",
                    "lower"}, new int[]{R.id.legname,
                    R.id.legstate});

            lv.setAdapter(adapter);
            */
            ListAdapter adapter =
                    new SimpleAdapter(
                            getActivity(),
                            contactList,
                            R.layout.comrow,
                            new String[]{"committee_id","name","chamber"},
                            new int[]{R.id.cid, R.id.ctitle, R.id.cchamber});



            Collections.sort(contactList, new Comparator<HashMap< String,String >>() {

                @Override
                public int compare(HashMap<String, String> m1,
                                   HashMap<String, String> m2) {


                        return m1.get("name").compareTo(m2.get("name"));

                }
            });

            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(CommitteesFragment.this.getActivity(), CommitteeDetails.class);
                    // sending data to new activity
                    i.putExtra("committee_id", contactList.get(position).get("committee_id"));
                    i.putExtra("name", contactList.get(position).get("name"));
                    i.putExtra("chamber",contactList.get(position).get("chamber"));
                    i.putExtra("parent_committee_id",contactList.get(position).get("parent_committee_id"));
                    i.putExtra("phone",contactList.get(position).get("phone"));
                    i.putExtra("office",contactList.get(position).get("office"));

                    startActivity(i);

                }
            });

            ListAdapter adapter2 = new SimpleAdapter(
                    getActivity(), contactListSenate,
                    R.layout.comrow, new String[]{"committee_id","name","chamber"},
                    new int[]{R.id.cid, R.id.ctitle, R.id.cchamber});

            Collections.sort(contactListSenate, new Comparator<HashMap< String,String >>() {

                @Override
                public int compare(HashMap<String, String> m1,
                                   HashMap<String, String> m2) {
                    // Do your comparison logic here and retrn accordingly.

                    return m1.get("name").compareTo(m2.get("name"));
                }
            });

            lv2.setAdapter(adapter2);

            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(CommitteesFragment.this.getActivity(), CommitteeDetails.class);

                    i.putExtra("committee_id", contactListSenate.get(position).get("committee_id"));
                    i.putExtra("name", contactListSenate.get(position).get("name"));
                    i.putExtra("chamber",contactListSenate.get(position).get("chamber"));
                    i.putExtra("parent_committee_id",contactListSenate.get(position).get("parent_committee_id"));
                    i.putExtra("phone",contactListSenate.get(position).get("phone"));
                    i.putExtra("office",contactListSenate.get(position).get("office"));
                    startActivity(i);

                }
            });


            ListAdapter adapter3 = new SimpleAdapter(
                    getActivity(), contactListJoint,
                    R.layout.comrow, new String[]{"committee_id","name","chamber"},
                    new int[]{R.id.cid, R.id.ctitle, R.id.cchamber});

            Collections.sort(contactListJoint, new Comparator<HashMap< String,String >>() {

                @Override
                public int compare(HashMap<String, String> m1,
                                   HashMap<String, String> m2) {
                    // Do your comparison logic here and retrn accordingly.

                    return m1.get("name").compareTo(m2.get("name"));
                }
            });

            lv3.setAdapter(adapter3);

            lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(CommitteesFragment.this.getActivity(), CommitteeDetails.class);
                    // sending data to new activity
                    i.putExtra("committee_id", contactListJoint.get(position).get("committee_id"));
                    i.putExtra("name", contactListJoint.get(position).get("name"));
                    i.putExtra("chamber",contactListJoint.get(position).get("chamber"));
                    i.putExtra("parent_committee_id",contactListJoint.get(position).get("parent_committee_id"));
                    i.putExtra("phone",contactListJoint.get(position).get("phone"));
                    i.putExtra("office",contactListJoint.get(position).get("office"));
                    startActivity(i);

                }
            });

        }

    }




}
