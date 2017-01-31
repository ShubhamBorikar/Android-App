package com.example.myfirstapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TabHost;
import android.app.ProgressDialog;


import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class BillsFragment extends Fragment {

    private Context context;
    private String TAG = BillsFragment.class.getSimpleName();

    //private ProgressDialog pDialog;
    private ListView lv,lv2;

    // URL to get contacts JSON
    private static String url = "http://cis-env.us-west-2.elasticbeanstalk.com/index.php/androidssb.php?type=billsnew";
    private static String urlactive = "http://cis-env.us-west-2.elasticbeanstalk.com/index.php/androidssb.php?type=billsactive";

    ArrayList<HashMap<String, String>> contactList,contactListActive;

    private TabHost tab;

    public BillsFragment() {
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
        //return inflater.inflate(R.layout.fragment_bills, container, false);


        View view=inflater.inflate(R.layout.fragment_bills, container, false);
        tab = (TabHost) view.findViewById(R.id.billstab);
        tab.setup();

        contactList = new ArrayList<>();
        contactListActive = new ArrayList<>();

        lv = (ListView) view.findViewById(R.id.listnewbills);
        lv2 = (ListView) view.findViewById(R.id.listactivebills);


        new GetContactsBills().execute();


        //tab1
        TabHost.TabSpec spec=tab.newTabSpec("Active Bills");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Active Bills");
        tab.addTab(spec);

        //tab2
        spec=tab.newTabSpec("New Bills");
        spec.setContent(R.id.tab2);
        spec.setIndicator("New Bills");
        tab.addTab(spec);

        return view;
    }

    private class GetContactsBills extends AsyncTask<Void, Void, Void> {



        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
//new bills
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String bill_id = c.optString("bill_id").toUpperCase();
                        String title = c.optString("official_title");

                       String date = c.optString("introduced_on");

                        String bill_type=c.optString("bill_type").toUpperCase();
                        JSONObject sponsor = c.optJSONObject("sponsor");
                        String sponsorfull=sponsor.optString("title")+" "+sponsor.optString("last_name")+", "+sponsor.optString("first_name");
                        String chamber=c.optString("chamber");
                        //JSONObject history=c.optJSONObject("history");
                        String status="New";
                        String intro_on=c.optString("introduced_on");
                        JSONObject urls=c.optJSONObject("urls");
                        String congress_url=urls.optString("congress");

                        JSONObject last_version=c.optJSONObject("last_version");
                        String version_name="NA";
                        if(last_version!=null)
                        {version_name=last_version.optString("version_name");}

                        JSONObject pdfurls;
                        String bill_url="NA";
                        if(last_version!=null)
                        {
                            pdfurls=last_version.optJSONObject("urls");
                            if(pdfurls!=null)
                            {bill_url=pdfurls.optString("pdf");}
                        }


                        /*String bill_url=c.optJSONObject("last_version").optJSONObject("urls").optString("pdf");*/

                        // Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject("phone");
                        //String mobile = phone.getString("mobile");
                        //String home = phone.getString("home");
                        //String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("bill_id", bill_id);
                        contact.put("title", title);
                        contact.put("date",date);
                        contact.put("bill_type",bill_type);
                        contact.put("sponsorfull",sponsorfull);
                        contact.put("chamber",chamber);
                        contact.put("status",status);
                        contact.put("intro_on",intro_on);
                        contact.put("congress_url",congress_url);
                        contact.put("version_status",version_name);
                        contact.put("bill_url",bill_url);


                        // adding contact to contact list
                        contactList.add(contact);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String jsonStrActive = sh.makeServiceCall(urlactive);

            Log.e(TAG, "Response from url: " + jsonStrActive);

            if (jsonStrActive != null) {
                try {
                    JSONObject jsonObjActive = new JSONObject(jsonStrActive);

                    // Getting JSON Array node
                    JSONArray contactsactive = jsonObjActive.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < contactsactive.length(); i++) {
                        JSONObject c = contactsactive.getJSONObject(i);

                        String bill_id = c.optString("bill_id").toUpperCase();
                        String title = c.optString("official_title");

                        String date = c.optString("introduced_on");
                        String bill_type=c.optString("bill_type");
                        JSONObject sponsor = c.optJSONObject("sponsor");
                        String sponsorfull=sponsor.optString("title")+" "+sponsor.optString("last_name")+", "+sponsor.optString("first_name");
                        String chamber=c.optString("chamber");
                        //JSONObject history=c.getJSONObject("history");
                       String status="Active";
                        String intro_on=c.optString("introduced_on");
                        JSONObject urls=c.optJSONObject("urls");
                        String congress_url=urls.optString("congress");
                        JSONObject last_version=c.optJSONObject("last_version");
                        String version_name="NA";
                        if(last_version!=null)
                        {version_name=last_version.optString("version_name");}

                        JSONObject pdfurls;
                        String bill_url="NA";
                        if(last_version!=null)
                        {
                            pdfurls=last_version.optJSONObject("urls");
                            if(pdfurls!=null)
                            {bill_url=pdfurls.optString("pdf");}
                        }



                        // Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject("phone");
                        //String mobile = phone.getString("mobile");
                        //String home = phone.getString("home");
                        //String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contactactive = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contactactive.put("bill_id", bill_id);
                        contactactive.put("title", title);
                        contactactive.put("date",date);

                        contactactive.put("bill_type",bill_type);
                        contactactive.put("sponsorfull",sponsorfull);
                        contactactive.put("chamber",chamber);
                        contactactive.put("status",status);
                        contactactive.put("intro_on",intro_on);
                        contactactive.put("congress_url",congress_url);
                        contactactive.put("version_status",version_name);
                        contactactive.put("bill_url",bill_url);


                        // adding contact to contact list
                        contactListActive.add(contactactive);

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

            //Log.e(TAG,contactList.toString());

            //New bills
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), contactList,
                    R.layout.billsrow, new String[]{"bill_id",
                    "title","date"}, new int[]{R.id.billid,
                    R.id.billtitle,R.id.billdate});

            Collections.sort(contactList, new Comparator<HashMap< String,String >>() {

                @Override
                public int compare(HashMap<String, String> m1,
                                   HashMap<String, String> m2) {
                    // Do your comparison logic here and retrn accordingly.

                    return m2.get("date").compareTo(m1.get("date"));
                }
            });

            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(BillsFragment.this.getActivity(), BillDetails.class);
                    // sending data to new activity
                    i.putExtra("bill_id", contactList.get(position).get("bill_id"));
                    i.putExtra("title", contactList.get(position).get("title"));
                    i.putExtra("bill_type",contactList.get(position).get("bill_type"));
                    i.putExtra("sponsorfull",contactList.get(position).get("sponsorfull"));
                    i.putExtra("chamber",contactList.get(position).get("chamber"));
                    i.putExtra("status",contactList.get(position).get("status"));
                    i.putExtra("intro_on",contactList.get(position).get("intro_on"));
                    i.putExtra("congress_url",contactList.get(position).get("congress_url"));
                    i.putExtra("version_status",contactList.get(position).get("version_status"));
                    i.putExtra("bill_url",contactList.get(position).get("bill_url"));

                    startActivity(i);

                }
            });


            //Active bills
            ListAdapter adapter2 = new SimpleAdapter(
                    getActivity(), contactListActive,
                    R.layout.billsrow, new String[]{"bill_id",
                    "title", "date"}, new int[]{R.id.billid,
                    R.id.billtitle,R.id.billdate});

            Collections.sort(contactListActive, new Comparator<HashMap< String,String >>() {

                @Override
                public int compare(HashMap<String, String> m1,
                                   HashMap<String, String> m2) {
                    // Do your comparison logic here and retrn accordingly.

                    return m2.get("date").compareTo(m1.get("date"));
                }
            });

            lv2.setAdapter(adapter2);

            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(BillsFragment.this.getActivity(), BillDetails.class);
                    // sending data to new activity
                    i.putExtra("bill_id", contactListActive.get(position).get("bill_id"));
                    i.putExtra("title", contactListActive.get(position).get("title"));
                    i.putExtra("bill_type",contactListActive.get(position).get("bill_type"));
                    i.putExtra("sponsorfull",contactListActive.get(position).get("sponsorfull"));
                    i.putExtra("chamber",contactListActive.get(position).get("chamber"));
                    i.putExtra("status",contactListActive.get(position).get("status"));
                    i.putExtra("intro_on",contactListActive.get(position).get("intro_on"));
                    i.putExtra("congress_url",contactListActive.get(position).get("congress_url"));
                    i.putExtra("version_status",contactListActive.get(position).get("version_status"));
                    i.putExtra("bill_url",contactListActive.get(position).get("bill_url"));

                    startActivity(i);

                }
            });



        }

    }

}
