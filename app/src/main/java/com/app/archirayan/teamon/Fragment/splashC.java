package com.app.archirayan.teamon.Fragment;

import android.content.Context;
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
public class splashC extends Fragment {
    public Button button;
    public Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_splash_3, container, false);
        return view;

    }
}
