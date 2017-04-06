package com.app.archirayan.teamon.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.archirayan.teamon.Fragment.Home;
import com.app.archirayan.teamon.Fragment.TabFrag;
import com.app.archirayan.teamon.Model.ProductDetail;
import com.app.archirayan.teamon.NavigationFragments.NavigationFullScreen;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.app.archirayan.teamon.Utils.Constant.ALLPRODUCT;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.WriteSharePrefrence;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public RelativeLayout drawerView;
    public Toolbar toolBar;
    public ImageView imageView;
    public TextView homeTv, orderTv, logoutTv, settingTv;
    public FragmentTransaction transaction;
    public Utils utils;
    public Fragment fragment = null;
    public FragmentManager fragmentManager;
    public LinearLayout navigationDrawerLinear;
    public FrameLayout navigationDrawerFrame;
    public ArrayList<ProductDetail> arrayList;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mainView;
    private ActionBarDrawerToggle mDrawerToggle;
    private Handler handler;
    private Runnable runnable;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawerLinear = (LinearLayout) findViewById(R.id.activity_main_navigation_linear);
        navigationDrawerFrame = (FrameLayout) findViewById(R.id.activity_main_navigation_frame);

        utils = new Utils(MainActivity.this);
        String isFirstTime = utils.getReadSharedPrefrenceIsFirstTime();

        // TODO: 24-Mar-17 if user is first time the will redirect to splash
        if (isFirstTime.equalsIgnoreCase("")) {

            Intent i = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(i);

        } else {

            String userId = Utils.ReadSharePrefrence(this, Constant.USERID);
            if (userId.equals("")) {

                Intent i = new Intent(MainActivity.this, FacebookLoginActivity.class);
                startActivity(i);

            } else {

                setContentView(R.layout.activity_main);

                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerView = (RelativeLayout) findViewById(R.id.drawerView);
                mainView = (RelativeLayout) findViewById(R.id.mainView);
                toolBar = (Toolbar) findViewById(R.id.activity_main_toolbar);
                setSupportActionBar(toolBar);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                imageView = (ImageView) findViewById(R.id.imageView);
                homeTv = (TextView) findViewById(R.id.activity_main_home);
                orderTv = (TextView) findViewById(R.id.activity_main_order);
                settingTv = (TextView) findViewById(R.id.activity_main_setting);
                logoutTv = (TextView) findViewById(R.id.activity_main_logout);

                imageView.setOnClickListener(this);
                homeTv.setOnClickListener(this);
                orderTv.setOnClickListener(this);
                logoutTv.setOnClickListener(this);
                settingTv.setOnClickListener(this);
                // For open home screen
                fragmentManager = getSupportFragmentManager();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolBar, R.string.app_name, R.string.app_name) {
                    public void onDrawerClosed(View view) {
                        supportInvalidateOptionsMenu();
                    }

                    public void onDrawerOpened(View drawerView) {
                        supportInvalidateOptionsMenu();
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        super.onDrawerSlide(drawerView, slideOffset);
                        mainView.setTranslationX(slideOffset * drawerView.getWidth());
                        mDrawerLayout.bringChildToFront(drawerView);
                        mDrawerLayout.requestLayout();
                        mDrawerLayout.setScrimColor(ContextCompat.getColor(MainActivity.this, android.R.color.transparent));
                    }
                };
                mDrawerLayout.setDrawerListener(mDrawerToggle);
                homeTv.performClick();
                startTimeUpdate();
            }
        }
    }

    private void startTimeUpdate() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ProductDetail>>() {
        }.getType();
        arrayList = gson.fromJson(ReadSharePrefrence(MainActivity.this, ALLPRODUCT), type);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                for (int i = 0; i < arrayList.size(); i++) {
                    long seconds = arrayList.get(i).getCounter();
                    if (seconds > 0) {
                        arrayList.get(i).setCounter(seconds - 1);
                    }
                }
                WriteSharePrefrence(MainActivity.this, ALLPRODUCT, gson.toJson(arrayList));
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        String stackName = "";
        switch (v.getId()) {
            case R.id.imageView:
                mDrawerLayout.openDrawer(Gravity.LEFT); //Edit Gravity.START need API 14
                break;
            case R.id.activity_main_home:
                fragment = new Home();
                stackName = "Home";
                setSelectedText(0);
                break;
            case R.id.activity_main_order:
                fragment = new TabFrag();
//                fragment = new TempOrder();
                stackName = "Order";
                setSelectedText(1);
                break;
            case R.id.activity_main_logout:

                if (ReadSharePrefrence(MainActivity.this, Constant.ISFACEBOOK).equalsIgnoreCase("1")) {
                    LoginManager.getInstance().logOut();
                }
//                setSelectedText(0);
                Utils.ClearaSharePrefrence(MainActivity.this);
                Intent in = new Intent(MainActivity.this, FacebookLoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
                break;

            case R.id.activity_main_setting:


                navigationDrawerLinear.setVisibility(View.GONE);
                navigationDrawerFrame.setVisibility(View.VISIBLE);

                navigationGone(View.GONE);
                transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
                Fragment navigationFragment = new NavigationFullScreen();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.activity_main_navigation_frame, navigationFragment);
                transaction.addToBackStack(stackName);
                transaction.commit();

                break;
        }

        if (fragment != null) {
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.activity_main_frame_layout, fragment);
            transaction.addToBackStack(stackName);
            transaction.commit();
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    private void navigationGone(int gone) {
        findViewById(R.id.activity_main_logout).setVisibility(gone);
        findViewById(R.id.activity_main_setting).setVisibility(gone);
        findViewById(R.id.activity_main_home).setVisibility(gone);
        findViewById(R.id.activity_main_order).setVisibility(gone);
    }

    private void setSelectedText(int i) {
        switch (i) {
            case 0:
                homeTv.setTextColor(ContextCompat.getColor(this, R.color.toolBarPink));
                orderTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                logoutTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                settingTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                break;
            case 1:
                homeTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                orderTv.setTextColor(ContextCompat.getColor(this, R.color.toolBarPink));
                logoutTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                settingTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                break;
            case 2:
                homeTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                orderTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                logoutTv.setTextColor(ContextCompat.getColor(this, R.color.toolBarPink));
                settingTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                break;
            case 3:
                homeTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                orderTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                logoutTv.setTextColor(ContextCompat.getColor(this, R.color.graytextcolor));
                settingTv.setTextColor(ContextCompat.getColor(this, R.color.toolBarPink));
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    // TODO: 02-Mar-17 Rujul On Restart reload product list
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        finish();
//        Intent in = new Intent(this, LoadProductActivity.class);
//        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(in);
//    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            super.onBackPressed();
            navigationDrawerLinear.setVisibility(View.VISIBLE);
            navigationDrawerFrame.setVisibility(View.GONE);
            navigationGone(View.VISIBLE);
        } else {
            super.onBackPressed();
            FragmentManager fm = getSupportFragmentManager();
            int lastFragmentCount = fm.getBackStackEntryCount() - 1;
            if (lastFragmentCount == -1) {
                this.finish();
            }
        }
    }

}
