package com.app.archirayan.teamon.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.archirayan.teamon.Activity.MainActivity;
import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.Model.UnitStep;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.app.archirayan.teamon.Utils.Utils.WriteSharePrefrence;

/**
 * Created by archi on 12/12/2016.
 */

public class ProductStoreDetail extends Fragment implements View.OnClickListener {

    public Toolbar toolBar;
    public ImageView toolBackIv, storeImage;
    public FragmentManager fragmentManager;
    public Button buyNowBtn;
    public ProductStoreSaleDetail productFullDetails;
    public TextView aboutStoreTv, storeAddressTv, productNameTv, timeTv, regularPriceTv, salePriceTv, soldByTv, unitSaleTv;

    public ArrayList<UnitStep> array;
    public Utils utils;
    public Handler handler;
    public Runnable runnable;
    private ImageView ivBack;

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
        View view = inflater.inflate(R.layout.fragment_product_store_detail, container, false);

        utils = new Utils(getActivity());
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        unitSaleTv = (TextView) view.findViewById(R.id.fragment_product_store_unitsale);

        productNameTv = (TextView) view.findViewById(R.id.fragment_product_store_proname);
        timeTv = (TextView) view.findViewById(R.id.fragment_product_store_time);
        regularPriceTv = (TextView) view.findViewById(R.id.fragment_product_store_price);
        salePriceTv = (TextView) view.findViewById(R.id.fragment_product_store_saleprice);

        soldByTv = (TextView) view.findViewById(R.id.fragment_product_store_soldby);
        storeImage = (ImageView) view.findViewById(R.id.fragment_product_store_details_image);
        toolBackIv = (ImageView) getActivity().findViewById(R.id.imageView1);
        buyNowBtn = (Button) view.findViewById(R.id.fragment_product_store_buynow);
        toolBackIv.setVisibility(View.VISIBLE);
        aboutStoreTv = (TextView) view.findViewById(R.id.fragment_product_store_details_about_store);
        storeAddressTv = (TextView) view.findViewById(R.id.fragment_product_store_details_store_address);
        ivBack = (ImageView) view.findViewById(R.id.ivBack_ProductStoreDetails);

        return view;
    }

    private void init() {

        runnable = new Runnable() {
            @Override
            public void run() {

                Long time = productFullDetails.getTime();
                time -= 1;
                productFullDetails.setTime(time);

                timeTv.setText(Utils.getTimeInCounter(productFullDetails.getTime()));
                handler.postDelayed(this, 1000);

                Gson gson = new Gson();
                WriteSharePrefrence(getActivity().getApplicationContext(), Constant.PRODUCT_STORE_SALE, gson.toJson(productFullDetails));
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 1000);

        Gson gson = new Gson();
        WriteSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE, gson.toJson(productFullDetails));


        unitSaleTv.setText("Unit sales - " + productFullDetails.getSaleQty());
        regularPriceTv.setText("$ " + productFullDetails.getPrice());
        salePriceTv.setText("$ " + productFullDetails.getStepWinnerPrice());
        toolBackIv.setOnClickListener(this);
        buyNowBtn.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        setStoreDetails();

    }

    private void setStoreDetails() {

        productNameTv.setText(productFullDetails.getTitle());
        soldByTv.setText(productFullDetails.getStepWinnerName());
        regularPriceTv.setText("$" + productFullDetails.getPrice());
        unitSaleTv.setText("Unit sale : " + productFullDetails.getSaleQty());

        int currentStep = productFullDetails.getSelectedStep();
        salePriceTv.setText("$" + productFullDetails.getUnitStep().get(currentStep).getPerUnitPrice());

        long saleQty = productFullDetails.getSaleQty();
        saleQty += 1;
        for (int i = 0; i < array.size(); i++) {
            long startUnit = Long.parseLong(array.get(i).getFromUnitStep());
            long endUnit = Long.parseLong(array.get(i).getToUnit());

            if (startUnit <= saleQty && endUnit >= saleQty) {

                if (array.get(i).getUserImage().length() > 0) {
                    Picasso.with(getActivity()).load(array.get(i).getUserImage()).placeholder(R.drawable.ic_placeholder).into(storeImage);
                    storeAddressTv.setMovementMethod(new ScrollingMovementMethod());
                    storeAddressTv.setText(array.get(i).getUserAddress());
                    aboutStoreTv.setText(array.get(i).getAboutStore());
                }
            }
        }
    }


    private void getArgument() {

        Gson gson = new Gson();
        String strObj = Utils.ReadSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE);
        productFullDetails = gson.fromJson(strObj, ProductStoreSaleDetail.class);
        array = productFullDetails.getUnitStep();


        init();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView1:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.fragment_logo_store_buybtn:
                Fragment fragment = new ProductThankyou();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_product_details_frame, fragment);
                fragmentTransaction.commit();

                break;

            case R.id.ivBack_ProductStoreDetails:
                getActivity().onBackPressed();
                break;

            case R.id.fragment_product_store_buynow:

                Fragment buyNowFrag = new ProductThankyou();
                getFragmentManager().beginTransaction().replace(R.id.fragment_product_details_frame, buyNowFrag).commit();
                break;
        }
    }
}

