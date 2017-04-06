package com.app.archirayan.teamon.Fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.archirayan.teamon.Activity.MainActivity;
import com.app.archirayan.teamon.Adapter.ProductBid;
import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.gson.Gson;

/**
 * Created by archirayan on 20-Dec-16.
 */

public class ProductSaleDetails extends Fragment implements View.OnClickListener {


    public String productName, productDetailsJson;
    public Long time;
    public TextView timeTv, productNameTv, saleQtyTv, soldByTv, unitSaleTv, tvSubPrice, tvPriceMain;
    public ListView listView;
    public Button buyNowBtn;
    public Utils utils;
    public RelativeLayout relative;
    public Handler handler;
    public Runnable runnable;
    private ProductStoreSaleDetail productFullDetails;
    private Toolbar toolBar;
    private ImageView imageView1;
    private FrameLayout flBack;
    private Button btBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sales_detail, container, false);

        utils = new Utils(getActivity());

        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        imageView1 = (ImageView) toolBar.findViewById(R.id.imageView1);
        imageView1.setVisibility(View.VISIBLE);
        imageView1.setOnClickListener(this);
        relative = (RelativeLayout) view.findViewById(R.id.relative);
//        flBack = (FrameLayout) view.findViewById(R.id.flBack_SalesDetailsFragment);
//        flBack.setOnClickListener(this);

        btBack = (Button) view.findViewById(R.id.btBack_SalesDetailsFragment);
        btBack.setOnClickListener(this);
        tvSubPrice = (TextView) view.findViewById(R.id.fragment_product_store_price);
        tvPriceMain = (TextView) view.findViewById(R.id.fragment_product_store_saleprice);
        buyNowBtn = (Button) view.findViewById(R.id.fragment_sale_buybtn);
        listView = (ListView) view.findViewById(R.id.fragment_sale_bid_list);
        productNameTv = (TextView) view.findViewById(R.id.fragment_sale_pro_name);
        soldByTv = (TextView) view.findViewById(R.id.fragment_sale_pro_saler_name);
        timeTv = (TextView) view.findViewById(R.id.fragment_sale_pro_time);
        unitSaleTv = (TextView) view.findViewById(R.id.fragment_sale_pro_unit_sale);


//        li=new ArrayList<String>();
//        for(int i=0; i<50; i++){
//            li.add("List "+i);
//        }
//        ArrayAdapter<String> adp=new ArrayAdapter<String>
//                (getActivity(),R.layout.list,li);
//        listView.setAdapter(adp);

        return view;
    }

    private void init() {
        Gson gson = new Gson();
        String strObj = getArguments().getString("productstoresaledetails");
        productFullDetails = gson.fromJson(strObj, ProductStoreSaleDetail.class);

        timeTv.setText(Utils.getTimeInCounter(time));
        productNameTv.setText(productName);
        unitSaleTv.setText("Unit sales - " + productFullDetails.getSaleQty());
        tvSubPrice.setText("$ " + productFullDetails.getPrice());
        tvSubPrice.setPaintFlags(tvSubPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tvPriceMain.setText("$ " + productFullDetails.getStepWinnerPrice());
        soldByTv.setText(productFullDetails.getStepWinnerName());

        listView.setAdapter(new ProductBid(getActivity(), productFullDetails.getUnitStep(), productFullDetails, productFullDetails.getSaleQty(), getActivity().getSupportFragmentManager()));

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


        relative.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;

            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;

            }
        });


        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ProductBuy();
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString("productstoresaledetails", gson.toJson(productFullDetails));

                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.addToBackStack("buynow");
                ft.replace(R.id.fragment_product_details_frame, fragment).commit();

            }
        });

    }

    private void getArgument() {
        Gson gson = new Gson();
        String strObj = Utils.ReadSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE);
        productFullDetails = gson.fromJson(strObj, ProductStoreSaleDetail.class);

        time = productFullDetails.getTime();
        productName = productFullDetails.getTitle();
        productDetailsJson = productFullDetails.getProDescr();

        init();
    }


    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        Intent intent;
        switch (view.getId()) {
            case R.id.btBack_SalesDetailsFragment:
//                Toast.makeText(getActivity(), "Go To Home Fragment", Toast.LENGTH_SHORT).show();
//                intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//                getActivity().finish();
                getActivity().onBackPressed();
                break;

            case R.id.imageView1:
//                Toast.makeText(getActivity(), "Go To Home Main", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;

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
