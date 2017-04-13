package com.app.archirayan.teamon.Fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.gson.Gson;

import static com.app.archirayan.teamon.Utils.Constant.ANYUPDATE;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.WriteSharePrefrence;

/**
 * Created by archi on 12/13/2016.
 */

public class ProductThankyou extends Fragment implements View.OnClickListener {


    public Toolbar toolBar;
    public ImageView toolback;
    public FragmentManager fragmentManager;
    public Button btnCancel;
    public TextView txtTime/*, txtSaleqty,*/, txtTitle, txtProprice, txtStorename, txtOldprice, txtUnitsales;
    public Utils utils;
    public Handler handler;
    public Runnable runnable;
    private Long time;
    private ProductStoreSaleDetail productFullDetails;

    @Override
    public void onResume() {
        super.onResume();
        getArgument();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_product_thank_you, container, false);

        // txtSaleqty = (TextView) view.findViewById(R.id.fragment_product_main_detais_sale_qty);
        utils = new Utils(getActivity());
        txtTime = (TextView) view.findViewById(R.id.fragment_product_thank_you_time);
        txtTitle = (TextView) view.findViewById(R.id.fragment_product_thank_you_protitle_top);
        txtProprice = (TextView) view.findViewById(R.id.fragment_product_thank_you_proprice_top);
        txtStorename = (TextView) view.findViewById(R.id.fragment_product_thank_you_txtstorename);
        txtOldprice = (TextView) view.findViewById(R.id.fragment_product_thank_you_txtoldprice);
        txtUnitsales = (TextView) view.findViewById(R.id.fragment_product_thank_you_txtunitsales);
        btnCancel = (Button) view.findViewById(R.id.fragment_product_thank_you_btncancel);
        btnCancel.setOnClickListener(this);


        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        toolback = (ImageView) getActivity().findViewById(R.id.imageView1);
        toolback.setVisibility(View.VISIBLE);
        toolback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {

                time = time - 1;
                productFullDetails.setTime(time);
                txtTime.setText(Utils.getTimeInCounter(time));
                handler.postDelayed(this, 1000);

                Gson gson = new Gson();
                WriteSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE, gson.toJson(productFullDetails));
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 1000);
        return view;

    }


    private void getArgument() {
        Gson gsonbuynow = new Gson();
        String strObj = ReadSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE);
        productFullDetails = gsonbuynow.fromJson(strObj, ProductStoreSaleDetail.class);
        Bundle bundle = this.getArguments();
        Long addedQty = 0L;
        if (bundle != null) {
            addedQty = bundle.getLong("qty", 0);
        }
        time = productFullDetails.getTime();
        // txtSaleqty.setText(String.valueOf(productFullDetails.getSaleQty()));
        txtTitle.setText(productFullDetails.getTitle());
        txtProprice.setText("$ " + productFullDetails.getStepWinnerPrice());
        txtOldprice.setText("$ " + productFullDetails.getPrice());
        txtOldprice.setPaintFlags(txtOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtStorename.setText(productFullDetails.getStepWinnerName());
        Long qty = productFullDetails.getSaleQty() + addedQty;
        txtUnitsales.setText("Unit sales " + qty);
        WriteSharePrefrence(getActivity(), ANYUPDATE, "1");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_product_thank_you_btncancel:
                Fragment fragment = new Home();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_product_details_frame, fragment);
                fragmentTransaction.commit();
                break;
        }
    }
}

