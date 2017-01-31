package com.example.myfirstapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment {

    private TabHost tab;


    public FavouritesFragment() {
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
        //return inflater.inflate(R.layout.fragment_favourites, container, false);
        View view=inflater.inflate(R.layout.fragment_favourites, container, false);
        tab = (TabHost) view.findViewById(R.id.favtab);
        tab.setup();

        //tab1
        TabHost.TabSpec spec=tab.newTabSpec("Legislators");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Legislators");
        tab.addTab(spec);

        //tab2
        spec=tab.newTabSpec("Bills");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Bills");
        tab.addTab(spec);

        //tab3
        spec=tab.newTabSpec("Committees");
        spec.setContent(R.id.tab3);
        spec.setIndicator("committees");
        tab.addTab(spec);


        return view;
    }

}
