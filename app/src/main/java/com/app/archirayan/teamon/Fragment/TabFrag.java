package com.app.archirayan.teamon.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.app.archirayan.teamon.Utils.Constant.BASE_URL;
import static com.app.archirayan.teamon.Utils.Constant.MYBALANCE;
import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;

/**
 * Created by welcome on 21-03-2017.
 */

public class TabFrag extends Fragment implements View.OnClickListener {

    TabCompleteOrders onefrag;
    TabNotOver twoFrag;
    TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<String> userArray;
    private Dialog withdrawDialog;
    private Utils utils;
    private Toolbar toolBar;
    private ImageView toolBackIv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabbiew, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        toolBackIv = (ImageView) getActivity().findViewById(R.id.imageView1);
        toolBackIv.setOnClickListener(this);
        toolBackIv.setVisibility(View.VISIBLE);

        setHasOptionsMenu(true);
        setupPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(ContextCompat.getColor(getActivity(), R.color.graytextcolor),
                ContextCompat.getColor(getActivity(), R.color.toolBarPink));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.getTabAt(0).setText("Completed");
        tabLayout.getTabAt(1).setText("Not Over");
        utils = new Utils(getActivity());

    }

    private void setupPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        if (onefrag == null) {
            onefrag = new TabCompleteOrders();
        }
        if (twoFrag == null) {
            twoFrag = new TabNotOver();
        }

        adapter.addFragment(onefrag, "");
        adapter.addFragment(twoFrag, "");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView1:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity_navigation, menu);
    }

    private Drawable buildCounterDrawable(String count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.noitification_count, null);
        view.setBackgroundResource(backgroundImageId);


        TextView textView = (TextView) view.findViewById(R.id.badge_notification_1);
        textView.setText("" + count);

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.main_navigation_balance:

                new GetEmailId().execute();

                break;
            case R.id.main_navigation_order:

                Fragment fragment = new TabFrag();

                if (fragment != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.activity_main_frame_layout, fragment);
                    transaction.addToBackStack("order");
                    transaction.commit();
                }

                break;
            case R.id.main_navigation_whishlist:


                Fragment whishListFragment = new WhishList();

                if (whishListFragment != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.activity_main_frame_layout, whishListFragment);
                    transaction.addToBackStack("whishList");
                    transaction.commit();
                }
                break;
            case R.id.main_navigation_notification:

                Fragment notificationListFragment = new NotificationList();

                if (notificationListFragment != null) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.activity_main_frame_layout, notificationListFragment);
                    transaction.addToBackStack("notification");
                    transaction.commit();
                }

                break;

        }
        return false;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
        }
    }

    private class GetEmailId extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            userArray = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return utils.getResponseofGet(BASE_URL + "get_order_PayPal_email_by_customer.php?uid=" + ReadSharePrefrence(getActivity(), USERID));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObejct = new JSONObject(s);
                if (jsonObejct.getString("status").equalsIgnoreCase("true")) {
                    JSONArray array = jsonObejct.getJSONArray("payer_email");
                    for (int i = 0; i < array.length(); i++) {
                        userArray.add(array.get(i).toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            withdrawDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
            withdrawDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            withdrawDialog.setContentView(R.layout.dialog_add_payment);
            withdrawDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            final EditText amountEdt = (EditText) withdrawDialog.findViewById(R.id.dialog_add_payment_amount);
            final TextView submitAmountTv = (TextView) withdrawDialog.findViewById(R.id.dialog_add_payment_txtsend);
            final TextView currentbalanceTv = (TextView) withdrawDialog.findViewById(R.id.dialog_add_payment_current_balance);
            final Spinner emailSpinner = (Spinner) withdrawDialog.findViewById(R.id.dialog_add_payment_email);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, userArray);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            emailSpinner.setAdapter(dataAdapter);
            currentbalanceTv.setText(ReadSharePrefrence(getActivity(), MYBALANCE));

            submitAmountTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (amountEdt.length() > 0) {
                        Double amount = Double.valueOf(amountEdt.getText().toString());
                        if (Double.parseDouble(ReadSharePrefrence(getActivity(), MYBALANCE)) > amount) {
                            new WithDrawPayment(emailSpinner.getSelectedItem().toString(), amount).execute();
                        }
                    }

                }
            });

            withdrawDialog.show();


        }

    }

    private class WithDrawPayment extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String email;
        Double amount;

        public WithDrawPayment(String s, Double amount) {
            this.email = s;
            this.amount = amount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            userArray = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("uid", "69");
            hashMap.put("receiverEmail", email);
            hashMap.put("amount", String.valueOf(amount));

            return utils.getResponseofPost(BASE_URL + "withdraw_amount.php", hashMap);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();

            try {

                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
                withdrawDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

