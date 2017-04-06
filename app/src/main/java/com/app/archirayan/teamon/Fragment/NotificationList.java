package com.app.archirayan.teamon.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.Adapter.NotificationAdapter;
import com.app.archirayan.teamon.Model.NotificationDetail;
import com.app.archirayan.teamon.Model.NotificationListDetails;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;
import com.app.archirayan.teamon.retrofit.ApiClient;
import com.app.archirayan.teamon.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.archirayan.teamon.Utils.Constant.BASE_URL;
import static com.app.archirayan.teamon.Utils.Constant.MYBALANCE;
import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by archirayan on 16-Mar-17.
 */

public class NotificationList extends Fragment implements View.OnClickListener {


    @BindView(R.id.fragment_recyclerview_notification)
    public RecyclerView recyclerView;
    public EditText searchEdt;
    NotificationAdapter adapter;
    private ArrayList<String> userArray;
    private Dialog withdrawDialog;
    private Utils utils;
    private Dialog pd;
    private Toolbar toolBar;
    private ImageView toolBackIv;
    private List<NotificationDetail> listNotification, searchNotifications;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notification_list, container, false);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        searchEdt = (EditText) view.findViewById(R.id.fragment_notification_list_search);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        toolBackIv = (ImageView) getActivity().findViewById(R.id.imageView1);
        toolBackIv.setOnClickListener(this);
        toolBackIv.setVisibility(View.VISIBLE);
        utils = new Utils(getActivity());

        pd = new Dialog(getActivity());
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        pd.setContentView(R.layout.dialog_loading);
        pd.setCancelable(false);
        pd.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", ReadSharePrefrence(getActivity(), USERID));
        Call<NotificationListDetails> call = apiService.getNotification(hashMap);
        call.enqueue(new Callback<NotificationListDetails>() {
            @Override
            public void onResponse(Call<NotificationListDetails> call, Response<NotificationListDetails> response) {
                pd.dismiss();
                if (response.body().getData() != null) {
                    listNotification = response.body().getData();
                    adapter = new NotificationAdapter(getActivity(), listNotification);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                    recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), R.string.error_nodata, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationListDetails> call, Throwable t) {

            }

        });
        searchEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchEdt.setFocusableInTouchMode(true);
                return false;
            }
        });
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    searchNotifications = filter(listNotification, searchEdt.getText().toString());
                    adapter = new NotificationAdapter(getActivity(), searchNotifications);
                    adapter.notifyDataSetChanged();

                    return true;
                }
                return false;
            }
        });
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                searchNotifications = new ArrayList<NotificationDetail>();
                searchNotifications = filter(listNotification, editable.toString());
                adapter = new NotificationAdapter(getActivity(), searchNotifications);
                adapter.notifyDataSetChanged();


            }
        });

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView1:
                getActivity().onBackPressed();
                break;
        }
    }

    public List<NotificationDetail> filter(List<NotificationDetail> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<NotificationDetail> filteredModelList = new ArrayList<>();
        for (NotificationDetail model : models) {
            String title = model.getTitle().toLowerCase();
            String message = model.getTitle().toLowerCase();
            if (message.contains(lowerCaseQuery) || title.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
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
