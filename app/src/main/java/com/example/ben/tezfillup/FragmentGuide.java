package com.example.ben.tezfillup;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

import static android.content.Context.MODE_PRIVATE;


public class FragmentGuide extends Fragment  {
    private static final String SEARCH_ENABLED_PARAM_NAME = "searchEnabledParam";
    private static final String TAG = "SlideShow";
    private boolean searchEnabled;


    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] ScreenShot= {
            R.drawable.mavilogin,R.drawable.yesilform, R.drawable.kirmizipdf,
            R.drawable.moraccount};
    private ArrayList<Integer> ScreenShots = new ArrayList<Integer>();

    public FragmentGuide() {
        // Required empty public constructor
    }
    public static FragmentGuide newInstance(Boolean searchEnabled) {
        FragmentGuide fragment = new FragmentGuide();
        Bundle args = new Bundle();
        args.putString(SEARCH_ENABLED_PARAM_NAME,Boolean.toString(searchEnabled));
        fragment.setArguments(args);
        if(searchEnabled)
            fragment.setHasOptionsMenu(true);
        else
            fragment.setHasOptionsMenu(false);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.searchEnabled = Boolean.parseBoolean(getArguments().getString(SEARCH_ENABLED_PARAM_NAME));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.activity_slidewhite, container, false);
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE).edit();
        editor.putString("PageName", "FragmentGuide");
        editor.commit();
        view.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
                return true;
            }
        });

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        SliderAdapter adapterView = new SliderAdapter(this.getActivity(), ScreenShots,"");
        mViewPager.setAdapter(adapterView);

        for(int i = 0; i < ScreenShot.length; i++)
            ScreenShots.add(ScreenShot[i]);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new SliderAdapter(this.getActivity(),ScreenShots,""));
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        return view;
    }

    public boolean onBackPressed() {
        Intent intent=new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
        return true;
    }
}