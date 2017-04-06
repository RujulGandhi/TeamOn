package com.app.archirayan.teamon.Fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.archirayan.teamon.Activity.MainActivity;
import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by archirayan on 14-Dec-16.
 */

public class ProductDetailsDecription extends Fragment implements View.OnClickListener {


    public TextView mainTitleTv, proTitleTv, productDecTv, timeTv, tvPriceMain, tvSubPrice, tvSalePerUnit, tvSaleBy;
    public Button backBtn, buynowBtn;
    public Utils utils;
    public ScrollView scrollView;
    public YouTubePlayerSupportFragment youTubePlayerFragment;
    public LinearLayout youtubeLinear;
    public Handler handler;
    public Runnable runnable;
    private ProductStoreSaleDetail productFullDetails;
    private Long time;
    private Toolbar toolBar;
    private ImageView toolBackIv;
    private YouTubePlayer YPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_product_detail_description, container, false);
        utils = new Utils(getActivity());
        youTubePlayerFragment = (YouTubePlayerSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.youtube_fragment);
        youtubeLinear = (LinearLayout) view.findViewById(R.id.fragment_product_details_description_youtube_linear);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        toolBackIv = (ImageView) getActivity().findViewById(R.id.imageView1);
        toolBackIv.setOnClickListener(this);

        scrollView = (ScrollView) view.findViewById(R.id.childScrollView);
        timeTv = (TextView) view.findViewById(R.id.fragment_product_details_time);
        mainTitleTv = (TextView) view.findViewById(R.id.fragment_product_details_pro_title_top);

        proTitleTv = (TextView) view.findViewById(R.id.fragment_product_details_pro_title);
        productDecTv = (TextView) view.findViewById(R.id.fragment_product_details_pro_desctiption);
        tvSubPrice = (TextView) view.findViewById(R.id.tvSubPrice_ProductDetailsDescription);
        tvPriceMain = (TextView) view.findViewById(R.id.tvPrice_ProductDetailsDescription);
        timeTv = (TextView) view.findViewById(R.id.fragment_product_details_time);
        mainTitleTv = (TextView) view.findViewById(R.id.fragment_product_details_pro_title_top);
        tvSalePerUnit = (TextView) view.findViewById(R.id.tvSalePerUnit_ProductDetailsDescription);
        tvSaleBy = (TextView) view.findViewById(R.id.tvSaleBy_ProductDetailsDescription);
        proTitleTv = (TextView) view.findViewById(R.id.fragment_product_details_pro_title);
        backBtn = (Button) view.findViewById(R.id.button3);
        backBtn.setOnClickListener(this);

//        getArgument();

        runnable = new Runnable() {
            @Override
            public void run() {

                time = time - 1;
                productFullDetails.setTime(time);
                timeTv.setText(Utils.getTimeInCounter(time));
                handler.postDelayed(this, 1000);

                Gson gson = new Gson();
                Utils.WriteSharePrefrence(getActivity().getApplicationContext(), Constant.PRODUCT_STORE_SALE, gson.toJson(productFullDetails));

            }
        };

        return view;
    }

    private void getArgument() {

        Gson gson = new Gson();
        String strObj = Utils.ReadSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE);
        productFullDetails = gson.fromJson(strObj, ProductStoreSaleDetail.class);

        handler = new Handler();
        handler.postDelayed(runnable, 1000);

//        productDecTv.setMovementMethod(new ScrollingMovementMethod());
        time = productFullDetails.getTime();
        mainTitleTv.setText(productFullDetails.getTitle());
        proTitleTv.setText(productFullDetails.getTitle());
        productDecTv.setText(productFullDetails.getProDescr());
        tvSubPrice.setText("$ " + productFullDetails.getPrice());
        tvSubPrice.setPaintFlags(tvSubPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvSaleBy.setText(productFullDetails.getStepWinnerName());

        tvPriceMain.setText("$ " + productFullDetails.getStepWinnerPrice());
        tvSalePerUnit.setText("Unit sale : " + productFullDetails.getSaleQty());
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;

            }
        });


        Pattern p = Pattern.compile("\\[embed\\].*?\\[\\/embed\\]");
        Matcher m = p.matcher(productFullDetails.getProDescr());


        if (m.find()) {

            String URL = String.valueOf(m.group().subSequence(7, m.group().length() - 8));
            final String id = getYouTubeId(URL);

            youTubePlayerFragment.initialize("AIzaSyBnHX8t29ZF5UcwY8XU1xPwuXkFfYIyv4c", new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                    if (!b) {
                        YPlayer = youTubePlayer;
                        YPlayer.setFullscreen(true);
                        YPlayer.loadVideo(id);
//                    YPlayer.play();
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                    // TODO Auto-generated method stub
                    youtubeLinear.setVisibility(View.GONE);
                }
            });
        } else {
            youtubeLinear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button3:
                getActivity().onBackPressed();
                break;

            case R.id.imageView1:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }

    public String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile(
                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

    private String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
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
