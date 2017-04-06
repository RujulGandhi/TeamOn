package com.app.archirayan.teamon.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.app.archirayan.teamon.Fragment.splashA;
import com.app.archirayan.teamon.Fragment.splashB;
import com.app.archirayan.teamon.Fragment.splashC;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;

/**
 * Created by archirayan on 29-Nov-16.
 */

public class SplashActivity extends FragmentActivity implements View.OnClickListener {
    public Button skipBtn;
    public ViewPager viewPager;
    public FragmentManager fragmentManager;
    public Utils utils;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splush);

        skipBtn = (Button) findViewById(R.id.activity_splash_skip);
        viewPager = (ViewPager) findViewById(R.id.pager);
        utils = new Utils(SplashActivity.this);
        fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new MyAdapter(fragmentManager));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        skipBtn.setText("Skip");
                        skipBtn.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.splashskipbtn));
                        break;
                    case 1:
                        skipBtn.setText("Skip");
                        skipBtn.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.splashskipbtn));
                        break;
                    case 2:
                        skipBtn.setText("Start Now");
                        skipBtn.setBackgroundColor(ContextCompat.getColor(SplashActivity.this, R.color.toolBarPink));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_splush_tablyaout);
        tabLayout.setupWithViewPager(viewPager);
        skipBtn.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_splash_skip:

                utils.setSharedPrefrenceIsFirstTime("1");
                Intent in = new Intent(SplashActivity.this, FacebookLoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();


//                switch (viewPager.getCurrentItem()) {
//                    case 0:
//                        Intent in
//                        break;
//                    case 1:
//                        break;
//                    case 2:
//                        break;
//        }
                break;
        }
    }

    private class MyAdapter extends FragmentPagerAdapter {
        FragmentManager fragmentManager;

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            this.fragmentManager = fragmentManager;
        }


        /**
         * Return the Fragment associated with a specified position.
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new splashA();
                    break;
                case 1:
                    fragment = new splashB();
                    break;
                case 2:
                    fragment = new splashC();
                    break;
            }
//            if (position == 0) {
//                fragment = new SplashA();
//                skipBtn.setText("RujulA");
//            } else if (position == 1) {
//                fragment = new SplashB();
//                skipBtn.setText("RujulB");
//            } else if (position == 2) {
//                fragment = new SplashC();
//                skipBtn.setText("RujulC");
//            }

            return fragment;


        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            return 3;
        }
    }
}
