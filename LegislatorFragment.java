package com.example.myfirstapp;


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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static com.example.myfirstapp.R.id.legimage;
import static com.example.myfirstapp.R.id.legname;


/**
 * A simple {@link Fragment} subclass.
 */
public class LegislatorFragment extends Fragment {
    Map<String, Integer> mapIndex;

    private String TAG = LegislatorFragment.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv,lv2,lv3;

    // URL to get contacts JSON
    private static String url = "http://cis-env.us-west-2.elasticbeanstalk.com/index.php/androidssb.php?type=leg";
    static final String URL="img";

    ArrayList<HashMap<String, String>> contactList,contactListHouse,contactListSenate;


    private TabHost tab;

    public LegislatorFragment() {
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
        View view=inflater.inflate(R.layout.fragment_legislator, container, false);
        tab = (TabHost) view.findViewById(R.id.legtab);
        tab.setup();

        contactList = new ArrayList<>();
        contactListHouse = new ArrayList<>();
        contactListSenate = new ArrayList<>();

        lv = (ListView) view.findViewById(R.id.list);
        lv2 = (ListView) view.findViewById(R.id.listhouse);
        lv3 = (ListView) view.findViewById(R.id.listsenate);

        new GetContacts().execute();

        //tab1
        TabHost.TabSpec spec=tab.newTabSpec("By State");
        spec.setContent(R.id.tab1);
        spec.setIndicator("By State");
        tab.addTab(spec);

        //tab2
        spec=tab.newTabSpec("House");
        spec.setContent(R.id.tab2);
        spec.setIndicator("House");
        tab.addTab(spec);

        //tab3
        spec=tab.newTabSpec("Senate");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Senate");
        tab.addTab(spec);




        return view;


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {



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

                        String id = c.getString("bioguide_id");
                        String name = c.getString("last_name")+", "+c.getString("first_name");
                        String state=c.getString("state_name");
                           String lower;
                        if(c.getString("district").equals("null"))
                        {
                            lower = "("+c.getString("party")+")"+c.getString("state_name")+" - District N.A.";
                        }
                        else
                        {
                            lower = "("+c.getString("party")+")"+c.getString("state_name")+" - District "+c.getString("district");
                        }


                        String chamber = c.getString("chamber");
                        String img="http://theunitedstates.io/images/congress/original/"+id+".jpg";
                        String party=c.getString("party");
                        String title=c.getString("title");
                        String email=c.getString("oc_email");
                        String phone=c.getString("phone");
                        String term_start=c.getString("term_start");
                        String term_end=c.getString("term_end");
                        String office=c.getString("office");
                        String fax=c.getString("fax");
                        String birthday=c.getString("birthday");
                        String fbid,twid,website;
                        /*if(c.getString("facebook_id").equals("null"))
                        {
                            fbid="NA";
                        }
                        else{fbid=c.getString("facebook_id");}
                        if(c.getString("twitter_id").equals("null")){twid="NA";} else{twid=c.getString("twitter_id");}
                        if(c.getString("website").equals("null")){website="NA";} else{website=c.getString("website");}
*/
                        fbid=c.optString("facebook_id");
                        twid=c.optString("twitter_id");
                        website=c.optString("website");

                        // Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject("phone");
                        //String mobile = phone.getString("mobile");
                        //String home = phone.getString("home");
                        //String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("name", name);
                        contact.put("lower", lower);
                        contact.put("img",img);
                        contact.put("state",state);
                        contact.put("party",party);
                        contact.put("email",email);
                        contact.put("chamber",chamber);
                        contact.put("phone",phone);
                        contact.put("term_start",term_start);
                        contact.put("term_end",term_end);
                        contact.put("office",office);
                        contact.put("fax",fax);
                        contact.put("birthday",birthday);
                        contact.put("fbid",fbid);
                        contact.put("twid",twid);
                        contact.put("website",website);


                        // adding contact to contact list
                        contactList.add(contact);
                        if(chamber.equals("house"))
                        {
                            contactListHouse.add(contact);
                        }
                        else
                        {
                            contactListSenate.add(contact);
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
                    new MyAdapter(
                            getActivity(),
                            contactList,
                            R.layout.legrow,
                            new String[]{"name","lower"},
                            new int[]{R.id.legname, R.id.legstate});



            Collections.sort(contactList, new Comparator<HashMap< String,String >>() {

                @Override
                public int compare(HashMap<String, String> m1,
                                   HashMap<String, String> m2) {
                    // Do your comparison logic here and retrn accordingly.
                    //if (manufacturer.compareToIgnoreCase(o.manufacturer) == 0)
                      //  return model.compareToIgnoreCase(o.model);
                    //else
                      //  return manufacturer.compareToIgnoreCase(o.manufacturer);
                    if(m1.get("state").compareTo(m2.get("state"))==0)
                        return m1.get("name").compareTo(m2.get("name"));
                    else
                    return m1.get("state").compareTo(m2.get("state"));
                }
            });

            lv.setAdapter(adapter);

            /*AdapterView.OnItemClickListener onListClick=new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> parent,View view, int position, long id)
                {
                    Intent i=new Intent(LegislatorFragment.this.getActivity(),LegislatorDetails.class);
                    i.putExtra("name", contactList.get(position));
                    startActivity(i);

                }
            };*/
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {



                    // Launching new Activity on selecting single List Item
                    Intent i = new Intent(LegislatorFragment.this.getActivity(), LegislatorDetails.class);
                    // sending data to new activity
                    i.putExtra("name", contactList.get(position).get("name"));
                    i.putExtra("url", contactList.get(position).get("img"));
                    i.putExtra("party",contactList.get(position).get("party"));
                    i.putExtra("email",contactList.get(position).get("email"));
                    i.putExtra("chamber",contactList.get(position).get("chamber"));
                    i.putExtra("phone",contactList.get(position).get("phone"));
                    i.putExtra("term_start",contactList.get(position).get("term_start"));
                    i.putExtra("term_end",contactList.get(position).get("term_end"));
                    i.putExtra("office",contactList.get(position).get("office"));
                    i.putExtra("state",contactList.get(position).get("state"));
                    i.putExtra("fax",contactList.get(position).get("fax"));
                    i.putExtra("birthday",contactList.get(position).get("birthday"));
                    i.putExtra("fbid",contactList.get(position).get("fbid"));
                    i.putExtra("twid",contactList.get(position).get("twid"));
                    i.putExtra("website",contactList.get(position).get("website"));
                    startActivity(i);

                }
            });

            ListAdapter adapter2 = new MyAdapter(
                    getActivity(), contactListHouse,
                    R.layout.legrow, new String[]{"name",
                    "lower"}, new int[]{R.id.legname,
                    R.id.legstate});

            Collections.sort(contactListHouse, new Comparator<HashMap< String,String >>() {

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
                    Intent i = new Intent(LegislatorFragment.this.getActivity(), LegislatorDetails.class);
                    // sending data to new activity
                    i.putExtra("name", contactListHouse.get(position).get("name"));
                    i.putExtra("url", contactListHouse.get(position).get("img"));
                    i.putExtra("party",contactListHouse.get(position).get("party"));
                    i.putExtra("email",contactListHouse.get(position).get("email"));
                    i.putExtra("chamber",contactListHouse.get(position).get("chamber"));
                    i.putExtra("phone",contactListHouse.get(position).get("phone"));
                    i.putExtra("term_start",contactListHouse.get(position).get("term_start"));
                    i.putExtra("term_end",contactListHouse.get(position).get("term_end"));
                    i.putExtra("office",contactListHouse.get(position).get("office"));
                    i.putExtra("state",contactListHouse.get(position).get("state"));
                    i.putExtra("fax",contactListHouse.get(position).get("fax"));
                    i.putExtra("birthday",contactListHouse.get(position).get("birthday"));
                    i.putExtra("fbid",contactListHouse.get(position).get("fbid"));
                    i.putExtra("twid",contactListHouse.get(position).get("twid"));
                    i.putExtra("website",contactListHouse.get(position).get("website"));
                    startActivity(i);

                }
            });


            ListAdapter adapter3 = new MyAdapter(
                    getActivity(), contactListSenate,
                    R.layout.legrow, new String[]{"name",
                    "lower"}, new int[]{R.id.legname,
                    R.id.legstate});

            Collections.sort(contactListSenate, new Comparator<HashMap< String,String >>() {

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
                    Intent i = new Intent(LegislatorFragment.this.getActivity(), LegislatorDetails.class);
                    // sending data to new activity
                    i.putExtra("name", contactListSenate.get(position).get("name"));
                    i.putExtra("url", contactListSenate.get(position).get("img"));
                    i.putExtra("party",contactListSenate.get(position).get("party"));
                    i.putExtra("email",contactListSenate.get(position).get("email"));
                    i.putExtra("chamber",contactListSenate.get(position).get("chamber"));
                    i.putExtra("phone",contactListSenate.get(position).get("phone"));
                    i.putExtra("term_start",contactListSenate.get(position).get("term_start"));
                    i.putExtra("term_end",contactListSenate.get(position).get("term_end"));
                    i.putExtra("office",contactListSenate.get(position).get("office"));
                    i.putExtra("state",contactListSenate.get(position).get("state"));
                    i.putExtra("fax",contactListSenate.get(position).get("fax"));
                    i.putExtra("birthday",contactListSenate.get(position).get("birthday"));
                    i.putExtra("fbid",contactListSenate.get(position).get("fbid"));
                    i.putExtra("twid",contactListSenate.get(position).get("twid"));
                    i.putExtra("website",contactListSenate.get(position).get("website"));
                    startActivity(i);

                }
            });

        }

    }

    public class MyAdapter extends SimpleAdapter {
        private Context context;

        public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){
            super(context, data, resource, from, to);
            this.context=context;
        }

        public View getView(final int position, View convertView, ViewGroup parent){
            // here you let SimpleAdapter built the view normally.
            View v = super.getView(position, convertView, parent);

            // Then we get reference for Picasso
            ImageView img = (ImageView) v.getTag();
            if(img == null){
                img = (ImageView) v.findViewById(legimage);
                v.setTag(img); // <<< THIS LINE !!!!
            }
            // get the url from the data you passed to the `Map`
            String url = ((Map<String,String>)getItem(position)).get("img");
            // do Picasso
            Picasso.with(v.getContext()).load(url).into(img);

            // return the view
            return v;
        }

    }







}
