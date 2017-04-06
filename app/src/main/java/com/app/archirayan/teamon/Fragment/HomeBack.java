package com.app.archirayan.teamon.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.Adapter.CategoryAdapter;
import com.app.archirayan.teamon.Adapter.ProductAdapter;
import com.app.archirayan.teamon.Model.CategoryDetails;
import com.app.archirayan.teamon.Model.ProductDetail;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;
import com.app.archirayan.teamon.Widget.MultiRange;
import com.app.archirayan.teamon.Widget.MultiRangeDollar;
import com.app.archirayan.teamon.Widget.SimpleDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.app.archirayan.teamon.R.id.dialog_filter_apply;
import static com.app.archirayan.teamon.R.id.dialog_filter_name;
import static com.app.archirayan.teamon.R.id.dialog_filter_reset;
import static com.app.archirayan.teamon.R.id.dialog_filter_sale;
import static com.app.archirayan.teamon.R.id.dialog_filter_time;
import static com.app.archirayan.teamon.Utils.Constant.ALLCATEGORY;
import static com.app.archirayan.teamon.Utils.Constant.ALLPRODUCT;
import static com.app.archirayan.teamon.Utils.Constant.ANYUPDATE;
import static com.app.archirayan.teamon.Utils.Constant.BASE_URL;
import static com.app.archirayan.teamon.Utils.Constant.ENDPRICE;
import static com.app.archirayan.teamon.Utils.Constant.ENDSALEQTY;
import static com.app.archirayan.teamon.Utils.Constant.FILTER_BY_PRODUCTNAME;
import static com.app.archirayan.teamon.Utils.Constant.FILTER_BY_PRODUCTSALE;
import static com.app.archirayan.teamon.Utils.Constant.FILTER_BY_PRODUCTTIME;
import static com.app.archirayan.teamon.Utils.Constant.MAXPRICE;
import static com.app.archirayan.teamon.Utils.Constant.MAXSALE;
import static com.app.archirayan.teamon.Utils.Constant.MYBALANCE;
import static com.app.archirayan.teamon.Utils.Constant.NOTIFICATIONCOUNT;
import static com.app.archirayan.teamon.Utils.Constant.SELECTEDCATID;
import static com.app.archirayan.teamon.Utils.Constant.SORTING_BY;
import static com.app.archirayan.teamon.Utils.Constant.STARTPRICE;
import static com.app.archirayan.teamon.Utils.Constant.STARTSALEQTY;
import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Constant.WALLETBALANCE;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.WriteSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.getTimeInCounter;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by archirayan on 22-Nov-16.
 */
public class HomeBack extends Fragment implements View.OnClickListener {
    public ListView listView;
    public ArrayList<ProductDetail> arrayList;
    public ProductAdapter adapter;
    public Utils utils;
    public EditText searchEdt;
    public String search;
    public Handler handler;
    public String uId, notificationCount;
    public ImageView imageView1;
    public Toolbar toolBar;
    public Long currentBalance;
    public LayoutInflater mInflater;
    public LinearLayout categoryFilterLinear, snackBarCategoryLinear, snackBarFilterLinear;
    public CoordinatorLayout coordinator;
    public Snackbar snackbar;
    public ArrayList<CategoryDetails> categoryArray;
    public ArrayList<String> userArray;
    public RecyclerView categoryRecyclerView;
    public Double result;
    public boolean isPagination = false;
    public Dialog filterDialog, categoryDialog, withdrawDialog;
    public TextView nameTv, saleTv, timeTv, resetTv, applyTv;
    public MultiRange multipleRangeBarSale;
    public MultiRangeDollar multiRangeBarPrice;
    public Long MaxValuePrice, MaxValueSale, walletBalance;
    public AVLoadingIndicatorView loader;
    public Runnable runnable;
    private Dialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_hiome_back, container, false);

        setHasOptionsMenu(true);
        showSnackBar(view);
//        init(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        uId = ReadSharePrefrence(getActivity(), USERID);
        categoryFilterLinear = (LinearLayout) view.findViewById(R.id.fragment_home_cateandfilter);
        listView = (ListView) view.findViewById(R.id.fragment_home_recycler_view);
        searchEdt = (EditText) view.findViewById(R.id.fragment_home_search);
        utils = new Utils(getActivity());
        arrayList = new ArrayList<>();

        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        imageView1 = (ImageView) toolBar.findViewById(R.id.imageView1);
        imageView1.setVisibility(View.GONE);

        adapter = new ProductAdapter(getActivity(), arrayList, getActivity().getSupportFragmentManager());
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    search = searchEdt.getText().toString();
                    arrayList.clear();
                    new GetProductList().execute();

                    return true;
                }
                return false;
            }
        });


        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                if (s.length() == 0 && getActivity() != null) {
//
//                    arrayList = new ArrayList<ProductDetail>();
//                    new GetProductList().execute();
//
//                }
            }
        });

        listView.setOnScrollListener(new ListView.OnScrollListener() {
            public int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mLastFirstVisibleItem < firstVisibleItem) {
                    snackbar.dismiss();
                }
                if (mLastFirstVisibleItem > firstVisibleItem) {
                    snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }
                mLastFirstVisibleItem = firstVisibleItem;
            }
        });


        searchEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchEdt.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    public void startTimerUpdate() {
        adapter = new ProductAdapter(getActivity(), arrayList, getActivity().getSupportFragmentManager());
        listView.setAdapter(adapter);
        arrayList = new ArrayList<>();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<ProductDetail>>() {
                }.getType();
                arrayList = gson.fromJson(ReadSharePrefrence(getActivity(), ALLPRODUCT), type);
                Log.d("UpdateTime", getTimeInCounter(arrayList.get(0).getCounter()));
                adapter = new ProductAdapter(getActivity(), arrayList, getActivity().getSupportFragmentManager());
//                if (adapter.getCount() <= 0) {
                listView.setAdapter(adapter);
//                }
                adapter.notifyDataSetChanged();
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void showSnackBar(View view) {
        coordinator = (CoordinatorLayout) view.findViewById(R.id.coordinator);

        mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create the Snackbar
        snackbar = Snackbar.make(coordinator, "", Snackbar.LENGTH_LONG);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.toolBarPink));
        // Hide the text
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = mInflater.inflate(R.layout.snackbar, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        snackBarCategoryLinear = (LinearLayout) snackView.findViewById(R.id.snackbar_category);
        snackBarCategoryLinear.setOnClickListener(this);
        snackBarFilterLinear = (LinearLayout) snackView.findViewById(R.id.snackbar_filter);
        snackBarFilterLinear.setOnClickListener(this);

        // Add the view to the Snackbar's layout
        layout.addView(snackView, params);

        // Show the Snackbar
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.snackbar_category:
                if (categoryArray.size() > 0) {
                    openCategoryDialogWithCategoryList();
                } else {
                    Toast.makeText(getActivity(), "No Category Found, Please Try Again.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.snackbar_filter:
                createFilterDialog();
                break;
            case dialog_filter_name:
                WriteSharePrefrence(getActivity(), SORTING_BY, FILTER_BY_PRODUCTNAME);

                nameTv.setBackgroundResource(R.drawable.selected_button_pink);
                saleTv.setBackgroundResource(R.drawable.unselected_button_grey);
                timeTv.setBackgroundResource(R.drawable.unselected_button_grey);

                nameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                saleTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
                timeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));

                break;
            case dialog_filter_sale:

                WriteSharePrefrence(getActivity(), SORTING_BY, FILTER_BY_PRODUCTSALE);
                nameTv.setBackgroundResource(R.drawable.unselected_button_grey);
                saleTv.setBackgroundResource(R.drawable.selected_button_pink);
                timeTv.setBackgroundResource(R.drawable.unselected_button_grey);

                nameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
                saleTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                timeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));

                break;
            case dialog_filter_time:

                WriteSharePrefrence(getActivity(), SORTING_BY, FILTER_BY_PRODUCTTIME);

                nameTv.setBackgroundResource(R.drawable.unselected_button_grey);
                saleTv.setBackgroundResource(R.drawable.unselected_button_grey);
                timeTv.setBackgroundResource(R.drawable.selected_button_pink);

                nameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
                saleTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
                timeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

                break;
            case dialog_filter_reset:

                WriteSharePrefrence(getActivity(), STARTPRICE, "");
                WriteSharePrefrence(getActivity(), ENDPRICE, "");
                WriteSharePrefrence(getActivity(), STARTSALEQTY, "");
                WriteSharePrefrence(getActivity(), ENDSALEQTY, "");
                WriteSharePrefrence(getActivity(), SELECTEDCATID, "");
                WriteSharePrefrence(getActivity(), SORTING_BY, FILTER_BY_PRODUCTNAME);
                arrayList.clear();
                filterDialog.dismiss();

                new GetProductList().execute();

                break;

            case dialog_filter_apply:

                WriteSharePrefrence(getActivity(), STARTPRICE, String.valueOf(multiRangeBarPrice.getSelectedMinValue()));
                WriteSharePrefrence(getActivity(), ENDPRICE, String.valueOf(multiRangeBarPrice.getSelectedMaxValue()));
                WriteSharePrefrence(getActivity(), STARTSALEQTY, String.valueOf(multipleRangeBarSale.getSelectedMinValue()));
                WriteSharePrefrence(getActivity(), ENDSALEQTY, String.valueOf(multipleRangeBarSale.getSelectedMaxValue()));

                filterDialog.dismiss();

                new GetProductList().execute();

                break;
        }
    }

    private void createFilterDialog() {

        filterDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = filterDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        filterDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        filterDialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_right_down_arrow);
//
        filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        filterDialog.setCancelable(true);
        filterDialog.setContentView(R.layout.dialog_filter);

        multiRangeBarPrice = (MultiRangeDollar) filterDialog.findViewById(R.id.dialog_filter_price_range);
        multipleRangeBarSale = (MultiRange) filterDialog.findViewById(R.id.dialog_filter_sale_qty_range);
//        multiRangeBarPrice.setRangeToDefaultValues(0d, Ma);
//        multipleRangeBarSale.setRangeToDefaultValues(0, 500);

//        RangeSeekBar<Integer> rangeSeekBar = new RangeSeekBar<>(getActivity());
        // Set the range
        multiRangeBarPrice.setRangeValues(0, MaxValuePrice);
        multipleRangeBarSale.setRangeValues(0, MaxValueSale);

        String startPrice, endPrice, startSale, endSale;

        startPrice = ReadSharePrefrence(getActivity(), STARTPRICE);
        endPrice = ReadSharePrefrence(getActivity(), ENDPRICE);
        startSale = ReadSharePrefrence(getActivity(), STARTSALEQTY);
        endSale = ReadSharePrefrence(getActivity(), ENDSALEQTY);

        int minPriceValue = startPrice.length() > 0 ? Integer.parseInt(startPrice) : 0;
        int maxPriceValue = endPrice.length() > 0 ? Integer.parseInt(endPrice) : MaxValuePrice.intValue();
        int minSaleValue = startSale.length() > 0 ? Integer.parseInt(startSale) : 0;
        int maxSaleValue = endSale.length() > 0 ? Integer.parseInt(endSale) : MaxValueSale.intValue();


        multiRangeBarPrice.setSelectedMinValue(minPriceValue);
        multiRangeBarPrice.setSelectedMaxValue(maxPriceValue);
        multipleRangeBarSale.setSelectedMinValue(minSaleValue);
        multipleRangeBarSale.setSelectedMaxValue(maxSaleValue);

        nameTv = (TextView) filterDialog.findViewById(dialog_filter_name);
        saleTv = (TextView) filterDialog.findViewById(dialog_filter_sale);
        timeTv = (TextView) filterDialog.findViewById(dialog_filter_time);
        resetTv = (TextView) filterDialog.findViewById(dialog_filter_reset);
        applyTv = (TextView) filterDialog.findViewById(dialog_filter_apply);

        nameTv.setOnClickListener(this);
        saleTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        resetTv.setOnClickListener(this);
        applyTv.setOnClickListener(this);


        String sortBy = ReadSharePrefrence(getActivity(), SORTING_BY);

        if (sortBy.equalsIgnoreCase(FILTER_BY_PRODUCTSALE)) {

            nameTv.setBackgroundResource(R.drawable.unselected_button_grey);
            saleTv.setBackgroundResource(R.drawable.selected_button_pink);
            timeTv.setBackgroundResource(R.drawable.unselected_button_grey);

            nameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
            saleTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            timeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));

        } else if (sortBy.equalsIgnoreCase(FILTER_BY_PRODUCTTIME)) {

            nameTv.setBackgroundResource(R.drawable.unselected_button_grey);
            saleTv.setBackgroundResource(R.drawable.unselected_button_grey);
            timeTv.setBackgroundResource(R.drawable.selected_button_pink);

            nameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
            saleTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
            timeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));

        } else {

            nameTv.setBackgroundResource(R.drawable.selected_button_pink);
            saleTv.setBackgroundResource(R.drawable.unselected_button_grey);
            timeTv.setBackgroundResource(R.drawable.unselected_button_grey);

            nameTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            saleTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));
            timeTv.setTextColor(ContextCompat.getColor(getActivity(), R.color.graytextcolor));

        }

        filterDialog.show();
    }

    private void openCategoryDialogWithCategoryList() {
        categoryDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = categoryDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        categoryDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_left_down_arrow_);
        categoryDialog.getWindow().setBackgroundDrawableResource(R.drawable.ic_left_down_arrow);
        categoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        categoryDialog.setContentView(R.layout.dialog_category);
        categoryDialog.setCancelable(true);
        categoryRecyclerView = (RecyclerView) categoryDialog.findViewById(R.id.dialog_category_recycler);
//        CategoryAdapter adapter = new CategoryAdapter(categoryArray, getContext(), listener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        categoryRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        categoryRecyclerView.setLayoutManager(mLayoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        categoryRecyclerView.setAdapter(adapter);
        categoryRecyclerView.setAdapter(new CategoryAdapter(categoryArray, getContext(), new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CategoryDetails item) {
                categoryDialog.dismiss();
                arrayList.clear();
                WriteSharePrefrence(getActivity(), SELECTEDCATID, item.catId);
                new GetProductList().execute();
            }
        }));
        categoryDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity_navigation, menu);

        MenuItem notificationItem = menu.findItem(R.id.main_navigation_notification);
        if (notificationCount != null) {
            if (notificationCount.equalsIgnoreCase("0") || notificationCount.equalsIgnoreCase("")) {

            } else {
                notificationItem.setIcon(buildCounterDrawable(String.valueOf(notificationCount), android.R.color.transparent));
            }
        }
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
    public void onResume() {
        super.onResume();

        Gson gson = new Gson();
        arrayList = new ArrayList<>();
        Type type = new TypeToken<ArrayList<ProductDetail>>() {
        }.getType();
        arrayList = gson.fromJson(ReadSharePrefrence(getActivity(), ALLPRODUCT), type);

        Type typeCat = new TypeToken<ArrayList<CategoryDetails>>() {
        }.getType();
        categoryArray = gson.fromJson(ReadSharePrefrence(getActivity(), ALLCATEGORY), typeCat);

        walletBalance = Long.valueOf(ReadSharePrefrence(getActivity(), WALLETBALANCE));
        MaxValuePrice = Long.valueOf(ReadSharePrefrence(getActivity(), MAXPRICE));
        MaxValueSale = Long.valueOf(ReadSharePrefrence(getActivity(), MAXSALE));
        notificationCount = ReadSharePrefrence(getActivity(), NOTIFICATIONCOUNT);

        adapter = new ProductAdapter(getActivity(), arrayList, getActivity().getSupportFragmentManager());
        listView.setAdapter(adapter);
        startTimerUpdate();
        if (ReadSharePrefrence(getActivity(), ANYUPDATE).equalsIgnoreCase("1")) {
            WriteSharePrefrence(getActivity(), ANYUPDATE, "0");
            new GetProductList().execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private class GetProductList extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new Dialog(getActivity());
            pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pd.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            pd.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            pd.setContentView(R.layout.dialog_loading);
            pd.setCancelable(false);
            pd.show();

            search = searchEdt.getText().toString();
            arrayList.clear();
            categoryArray.clear();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("uid", uId);
            String sortBy = ReadSharePrefrence(getActivity(), SORTING_BY);
            if (sortBy.equalsIgnoreCase("")) {
                hashMap.put("sort_by", "product_name");
            } else {
                hashMap.put("sort_by", sortBy);
            }
            hashMap.put("s", search);
            hashMap.put("start_price", ReadSharePrefrence(getActivity(), STARTPRICE));
            hashMap.put("end_price", ReadSharePrefrence(getActivity(), ENDPRICE));
            hashMap.put("start_sale_quantity", ReadSharePrefrence(getActivity(), STARTSALEQTY));
            hashMap.put("end_sale_quantity", ReadSharePrefrence(getActivity(), ENDSALEQTY));
            hashMap.put("category_id", ReadSharePrefrence(getActivity(), SELECTEDCATID));

            for (Map.Entry<String, String> hashMapObj : hashMap.entrySet()) {
                Log.d("KEY-VALUE", hashMapObj.getKey() + "=" + hashMapObj.getValue());
            }
            return utils.getResponseofPost(BASE_URL + "get_product_list.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                pd.dismiss();
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    String amount = object.getString("current_balance");
                    WriteSharePrefrence(getActivity(), MYBALANCE, amount);
                    MaxValuePrice = Long.valueOf(object.getString("max_price"));
                    MaxValueSale = Long.valueOf(object.getString("max_stock"));
                    currentBalance = Long.valueOf(object.getString("current_balance"));
                    notificationCount = object.getString("notification_count");
//                    if (balanceMenu != null)
//                        balanceMenu.setIcon(buildCounterDrawable(amount, android.R.color.transparent));

                    JSONArray mainArray = object.getJSONArray("data");
                    JSONArray mainCategoryArray = object.getJSONArray("category");
                    listView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mainArray.length(); i++) {
                        JSONObject productObject = mainArray.getJSONObject(i);

                        ProductDetail details = new ProductDetail();
                        details.setProductId(productObject.getString("product_id"));
                        details.setProductName(productObject.getString("product_name"));
                        details.setProductPrice(productObject.getString("pricePerUnit"));
                        details.setProductOldPrice(productObject.getString("product_regular_price"));
                        details.setProductSalerName(productObject.getString("seller_name"));
                        if (productObject.has("product_image")) {
                            details.setProductImage(productObject.getString("product_image"));
                        } else {
                            details.setProductImage("");
                        }
                        details.setCounter(Long.parseLong(productObject.getString("_auction_duration")));
                        details.setIsWhishList(productObject.getString("wishlist"));
                        details.setProductSales(productObject.getString("sales_qty"));

                        arrayList.add(details);
                    }
                    for (int j = 0; j < mainCategoryArray.length(); j++) {
                        JSONObject categoryObject = mainCategoryArray.getJSONObject(j);
                        CategoryDetails details = new CategoryDetails();
                        details.setCatCount(categoryObject.getString("count"));
                        details.setCatId(categoryObject.getString("term_id"));
                        details.setCatTitle(categoryObject.getString("name"));
                        categoryArray.add(details);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
            }

            Gson gson = new Gson();
            String allProduct = gson.toJson(arrayList);
            String allCategory = gson.toJson(categoryArray);

            WriteSharePrefrence(getActivity(), ALLPRODUCT, allProduct);
            WriteSharePrefrence(getActivity(), ALLCATEGORY, allCategory);
            WriteSharePrefrence(getActivity(), MAXPRICE, String.valueOf(MaxValuePrice));
            WriteSharePrefrence(getActivity(), MAXSALE, String.valueOf(MaxValueSale));
            WriteSharePrefrence(getActivity(), WALLETBALANCE, String.valueOf(currentBalance));
            WriteSharePrefrence(getActivity(), NOTIFICATIONCOUNT, String.valueOf(notificationCount));

            adapter.notifyDataSetChanged();
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

