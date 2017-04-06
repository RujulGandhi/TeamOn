package com.app.archirayan.teamon.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.archirayan.teamon.R;

/**
 * Created by archirayan on 29-Nov-16.
 */
public class splashA extends Fragment {
    //    public ViewPager viewPager;
    public Button btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_splash_1, container, false);

//        (view.findViewById(R.id.imageView)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                Fragment frag = new SplashB();
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.beginTransaction().replace(R.id.pager,frag).commit();
//
//
//
//
//            }
//        });


        return view;

    }
}
