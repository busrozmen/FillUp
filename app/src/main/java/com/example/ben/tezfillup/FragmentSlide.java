package com.example.ben.tezfillup;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Pc on 31.03.2018.
 */

public class FragmentSlide extends Fragment  {
    private static final String SEARCH_ENABLED_PARAM_NAME = "searchEnabledParam";
    private static final String TAG = "FragmentSlide";
    String ActiveUserEmail;
    private static ViewPager mPager;
    private boolean searchEnabled;

    private static final Integer[] sliderImagesId = {
            R.drawable.image1, R.drawable.image2, R.drawable.image3
    };
    private ArrayList<Integer> sliderImages = new ArrayList<Integer>();

    public FragmentSlide() {
        // Required empty public constructor
    }
    public static FragmentSlide newInstance(Boolean searchEnabled) {
        FragmentSlide fragment = new FragmentSlide();
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
        View view = inflater.inflate(R.layout.activity_slide, container, false);
        SharedPreferences SP = this.getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE);
        ActiveUserEmail = SP.getString("ActiveUserEmail", null);

        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("MYPREFS", MODE_PRIVATE).edit();
        editor.putString("PageName", "FragmentSlide");
        editor.commit();
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        SliderAdapter adapterView = new SliderAdapter(this.getActivity(), sliderImages,ActiveUserEmail);
        mViewPager.setAdapter(adapterView);

        for(int i = 0; i < sliderImagesId.length; i++)
            sliderImages.add(sliderImagesId[i]);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new SliderAdapter(this.getActivity(),sliderImages,ActiveUserEmail));
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