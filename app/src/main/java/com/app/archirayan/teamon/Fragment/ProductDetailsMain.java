package com.app.archirayan.teamon.Fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.archirayan.teamon.Activity.MainActivity;
import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.Model.UnitStep;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.app.archirayan.teamon.Widget.MySeekBar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by archirayan on 08-Dec-16.
 */

public class ProductDetailsMain extends Fragment implements View.OnClickListener {
    public LinearLayout productLinear, saleLinear, storeLinear;
    public ImageView productLinearImg, saleLinearImg, storeLinearImg;
    public ProductStoreSaleDetail productFullDetails;
    //    public RatingBar ratingBar;
    public MySeekBar seekBar;
    public TextView startUnitTv, endUnitTv, unitPriceTv, nextUnitPriceTv;
    public ImageView storeImage1, storeImage2;
    public TextView timeTv, productNameTv, soldByTv, saleQtyTv, tvPriceMain, tvPriceSub, tvSoldBy;
    public String productName, productDetailsJson, priceStr, soldByStr;
    public Long time, salesQntLong;
    public Utils utils;
    public Handler handler;
    public Runnable runnable;
    private Button btnBuynow;
    private Toolbar toolBar;
    private ImageView toolBackIv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_product_detail_main, container, false);

        utils = new Utils(getActivity());
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        toolBackIv = (ImageView) getActivity().findViewById(R.id.imageView1);
        toolBackIv.setOnClickListener(this);

        tvPriceMain = (TextView) view.findViewById(R.id.fragment_product_detail_main_price);
        tvSoldBy = (TextView) view.findViewById(R.id.fragment_product_main_detais_sold_by);
        tvPriceSub = (TextView) view.findViewById(R.id.fragment_product_detail_main_subprice);

        seekBar = (MySeekBar) view.findViewById(R.id.seekbar_ProductDetailMainFragment);
        storeImage1 = (ImageView) view.findViewById(R.id.fragment_product_main_detais_pro_store_image);
        storeImage2 = (ImageView) view.findViewById(R.id.fragment_product_main_detais_pro_store_image_2);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
//        ratingBar = (RatingBar) view.findViewById(R.id.ratingbar);

        productLinear = (LinearLayout) view.findViewById(R.id.fragment_product_main_detail_product);
        saleLinear = (LinearLayout) view.findViewById(R.id.fragment_product_main_detail_sale);
        storeLinear = (LinearLayout) view.findViewById(R.id.fragment_product_main_detail_store);
        timeTv = (TextView) view.findViewById(R.id.fragment_product_main_detais_time);
        productNameTv = (TextView) view.findViewById(R.id.fragment_product_main_detais_pro_name);
        productLinearImg = (ImageView) view.findViewById(R.id.fragment_product_main_detail_product_img);
        saleLinearImg = (ImageView) view.findViewById(R.id.fragment_product_main_detail_sale_img);
        storeLinearImg = (ImageView) view.findViewById(R.id.fragment_product_main_detail_store_img);
        saleQtyTv = (TextView) view.findViewById(R.id.fragment_product_main_detais_sale_qty);
        soldByTv = (TextView) view.findViewById(R.id.fragment_product_main_detais_pro_soldby);
        startUnitTv = (TextView) view.findViewById(R.id.fragment_product_main_detais_pro_start_unit);
        endUnitTv = (TextView) view.findViewById(R.id.fragment_product_main_detais_end_unit);
        unitPriceTv = (TextView) view.findViewById(R.id.fragment_product_main_detais_unit_price);
        nextUnitPriceTv = (TextView) view.findViewById(R.id.fragment_product_main_detais_next_unit_price);
        btnBuynow = (Button) view.findViewById(R.id.fragment_product_detail_main_btnbuynow);

        return view;
    }

    private void getArgument() {

        Gson gson = new Gson();
        String strObj = Utils.ReadSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE);
        productFullDetails = gson.fromJson(strObj, ProductStoreSaleDetail.class);

        soldByStr = productFullDetails.getStepWinnerName();
        salesQntLong = productFullDetails.getSaleQty();
        time = productFullDetails.getTime();
        productName = productFullDetails.getTitle();
        productDetailsJson = productFullDetails.getProDescr();

        ArrayList<UnitStep> array = productFullDetails.getUnitStep();
//        ratingBar.setStar(Float.parseFloat(productFullDetails.getRating()));

        long saleQty = productFullDetails.getSaleQty();
        saleQty += 1;
        for (int i = 0; i < array.size(); i++) {
            long startUnit = Long.parseLong(array.get(i).getFromUnitStep());
            long endUnit = Long.parseLong(array.get(i).getToUnit());

            if (startUnit <= saleQty && endUnit >= saleQty) {
                startUnitTv.setText(String.valueOf(startUnit) + " Units");
                endUnitTv.setText(String.valueOf(endUnit) + " Units");
                if (array.get(i).getUserImage().length() > 0) {
                    Picasso.with(getActivity()).load(array.get(i).getUserImage()).placeholder(R.drawable.ic_placeholder).into(storeImage1);
                    Picasso.with(getActivity()).load(array.get(i).getUserImage()).placeholder(R.drawable.ic_placeholder).into(storeImage2);
                }
                unitPriceTv.setText("$" + array.get(i).getPerUnitPrice());

                if (array.size() > (i + 1)) {
                    nextUnitPriceTv.setText("$" + array.get(i + 1).getPerUnitPrice());
                } else {
                    nextUnitPriceTv.setText("This is last step");
                }
            }
        }

        init();
    }

    private void init() {

        productNameTv.setText(productName);
        tvPriceSub.setPaintFlags(tvPriceSub.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvPriceSub.setText("$" + productFullDetails.getPrice());

        int currentStep = productFullDetails.getSelectedStep();

        tvPriceMain.setText("$ " + productFullDetails.getStepWinnerPrice());
        saleQtyTv.setText("Unit sale - " + String.valueOf(salesQntLong));
        soldByTv.setText("" + salesQntLong + " Sale by \n" + soldByStr);
        tvSoldBy.setText(soldByStr);

        String fromUnit = productFullDetails.getUnitStep().get(currentStep).getFromUnitStep();
        String toUnit = productFullDetails.getUnitStep().get(currentStep).getToUnit();
        int diff = Integer.parseInt(toUnit) - Integer.parseInt(fromUnit);
        int max = diff + 1;
        int progress = Integer.parseInt(String.valueOf(salesQntLong)) - Integer.parseInt(fromUnit);
        seekBar.setMax(Math.abs(max));
        seekBar.setProgress(progress);

        startUnitTv.setText(String.valueOf(fromUnit) + " Units");
        endUnitTv.setText(String.valueOf(toUnit) + " Units");
        runnable = new Runnable() {
            @Override
            public void run() {

                time = time - 1;
                productFullDetails.setTime(time);
                timeTv.setText(Utils.getTimeInCounter(time));
                handler.postDelayed(this, 1000);

                Gson gson = new Gson();
                Utils.WriteSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE, gson.toJson(productFullDetails));

            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 1000);

        productLinear.setOnClickListener(this);
        saleLinear.setOnClickListener(this);
        storeLinear.setOnClickListener(this);
        btnBuynow.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Fragment fragment = null;
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Gson gson = new Gson();

        Bundle bundle = new Bundle();
        bundle.putString("productstoresaledetails", gson.toJson(productFullDetails));
        String backStackName = "";
        switch (view.getId()) {

            case R.id.fragment_product_main_detail_product:

                productLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_selected_pro_details));
                saleLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_sale_details));
                storeLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_store_details));

                fragment = new ProductDetailsDecription();
                fragment.setArguments(bundle);
                backStackName = "productdescription";
                break;
            case R.id.fragment_product_main_detail_sale:

                productLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pro_details));
                saleLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_selected_saler_details));
                storeLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_store_details));

                fragment = new ProductSaleDetails();
                fragment.setArguments(bundle);
                backStackName = "saledetails";
                break;
            case R.id.fragment_product_main_detail_store:

                productLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_pro_details));
                saleLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_sale_details));
                storeLinearImg.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_selected_store_details));

                fragment = new ProductStoreDetail();
                fragment.setArguments(bundle);

                backStackName = "storedetails";

                break;
            case R.id.fragment_product_detail_main_btnbuynow:

                //btnBuynow.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
                fragment = new ProductBuy();
                fragment.setArguments(bundle);
                backStackName = "buynow";

                break;

            case R.id.imageView1:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(backStackName);
            ft.replace(R.id.fragment_product_details_frame, fragment).commit();
        }
    }


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
}
