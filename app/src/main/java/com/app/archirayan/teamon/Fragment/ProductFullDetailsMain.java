package com.app.archirayan.teamon.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.Model.UnitStep;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.app.archirayan.teamon.Utils.Constant.BASE_URL;
import static com.app.archirayan.teamon.Utils.Constant.MYBALANCE;
import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;

/**
 * Created by archirayan on 07-Dec-16.
 */

public class ProductFullDetailsMain extends Fragment implements View.OnClickListener {

    public String pId;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    public FrameLayout frameLayout;
    public ArrayList<String> galleryImage;
    public Utils utils;
    public TextView whishlistBtn, shareitBtn;
    public ProductStoreSaleDetail details;
    public ImageView imageView1;
    public Toolbar toolBar;
    public MenuItem balanceItem;
    public ArrayList<String> userArray;
    private String notificationCount;
    private Dialog withdrawDialog;
    private MenuItem notificationItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_product_full_details_main, container, false);

        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        toolbar.getMenu().clear();

        whishlistBtn = (TextView) view.findViewById(R.id.fragment_product_details_whislist);
        shareitBtn = (TextView) view.findViewById(R.id.fragment_product_details_share);
        viewPager = (ViewPager) view.findViewById(R.id.fragment_product_details_pager);
        utils = new Utils(getActivity());
        tabLayout = (TabLayout) view.findViewById(R.id.fragment_product_details_tablayout);
        frameLayout = (FrameLayout) view.findViewById(R.id.fragment_product_details_frame);
        toolBar = (Toolbar) getActivity().findViewById(R.id.activity_main_toolbar);
        imageView1 = (ImageView) toolBar.findViewById(R.id.imageView1);
        imageView1.setVisibility(View.VISIBLE);

        getBundles();
        init();

        return view;
    }

    private void getBundles() {

        if (getArguments() != null) {

            pId = getArguments().getString("pId");

        }

    }

    private void init() {

        // GET PRODUCT DETAILS FROM PRODUCT ID
        if (utils.isConnectingToInternet()) {
            new GetProductDetails().execute();
        } else {
            Toast.makeText(getActivity(), getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }

        // ADD TO WHISHLIST
        whishlistBtn.setOnClickListener(this);

        //SHARE THE PRODUCT DETAILS
        shareitBtn.setOnClickListener(this);

        // back button click
        imageView1.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fragment_product_details_whislist:
                String task = "";

                String isInWhishList = details.getIsWhishList();
                if (isInWhishList.equalsIgnoreCase("true")) {
                    whishlistBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.unselectedbggray));
                } else {
                    whishlistBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selectedbggreen));
                }
                task = isInWhishList.equalsIgnoreCase("true") ? "remove" : "add";

                new UpdateWhishList(details.getProId(), task).execute();

                break;
            case R.id.fragment_product_details_share:

                Intent intent2 = new Intent(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.app.archirayan.teamon");
                startActivity(Intent.createChooser(intent2, getResources().getString(R.string.SendTo)));

                break;

            case R.id.imageView1:
                Fragment fragment = new Home();
                if (fragment != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.activity_main_frame_layout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                break;
        }

    }

    // setting action bar icon & click
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_activity_navigation, menu);


        notificationItem = menu.findItem(R.id.main_navigation_notification);
        if (notificationCount != null) {
            if (notificationCount.equalsIgnoreCase("0") || notificationCount.equalsIgnoreCase("")) {
//                menuItem2.setIcon(buildCounterDrawable(String.valueOf(notificationCount), android.R.color.transparent));
            } else {
                notificationItem.setIcon(buildCounterDrawable(String.valueOf(notificationCount), android.R.color.transparent));
            }
        }
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


    // VIEW PAGER IMAGES CLASS
    public class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return galleryImage.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            imageView.setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(galleryImage.get(position)).placeholder(R.drawable.ic_placeholder).into(imageView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    //GET PRODUCT DETAILS FROM PRODUCT ID.
    private class GetProductDetails extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.show();
            galleryImage = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("URL", BASE_URL + "get_product.php?pid=" + pId + "&uid=" + ReadSharePrefrence(getActivity(), USERID));
            return utils.getResponseofGet(BASE_URL + "get_product.php?pid=" + pId + "&uid=" + ReadSharePrefrence(getActivity(), USERID));
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    Utils.WriteSharePrefrence(getActivity(), MYBALANCE, object.getString("current_balance"));
                    notificationCount = object.getString("notification_count");
                    if (notificationCount != null) {
                        if (notificationCount.equalsIgnoreCase("0") || notificationCount.equalsIgnoreCase("")) {
                        } else {
                            notificationItem.setIcon(buildCounterDrawable(String.valueOf(notificationCount), android.R.color.transparent));
                        }
                    }
                    JSONObject productObject = object.getJSONObject("data");

                    JSONArray galleryImages = productObject.getJSONArray("gallery");
                    for (int i = 0; i < galleryImages.length(); i++) {
                        galleryImage.add(galleryImages.getJSONObject(i).getString("original"));
                    }

                    viewPager.setAdapter(new CustomPagerAdapter(getActivity()));
                    tabLayout.setupWithViewPager(viewPager);

                    details = new ProductStoreSaleDetail();
                    details.setProId(pId);
                    details.setTime(productObject.getLong("auction_duration"));
                    details.setTitle(productObject.getString("post_title"));
                    details.setPrice(productObject.getString("regular_price"));
                    details.setGalleryImages(galleryImage);
                    details.setIsWhishList(productObject.getString("wishlist"));
                    details.setSaleQty(productObject.getLong("sales_qty"));
                    details.setProDescr(productObject.getString("post_content"));
                    details.setSoldBy(productObject.getString("seller_name"));
                    details.setRating(productObject.getString("rating"));
                    ArrayList<UnitStep> array = new ArrayList<>();
                    JSONArray unitStepArray = productObject.getJSONArray("_auction_steps");

                    for (int j = 0; j < unitStepArray.length(); j++) {

                        UnitStep unitStep = new UnitStep();
                        JSONObject unitObject = unitStepArray.getJSONObject(j);
                        unitStep.setName(unitObject.getString("name"));
                        unitStep.setFromUnitStep(unitObject.getString("fromUnit"));
                        unitStep.setToUnit(unitObject.getString("toUnit"));
                        unitStep.setPerUnitPrice(unitObject.getString("pricePerUnit"));
                        unitStep.setUserId(unitObject.getString("user_id"));
                        unitStep.setUserImage(unitObject.getString("user_user_image"));
                        unitStep.setName(unitObject.getString("user_username"));
                        unitStep.setUserAddress(unitObject.getString("store_address"));
                        unitStep.setAboutStore(unitObject.getString("store_detail"));
                        array.add(unitStep);

                    }
                    details.setUnitStep(array);
                    int selectedStep = Utils.getSelectedStep(details.getUnitStep(), details.getSaleQty());
                    if (selectedStep >= 0) {
                        details.setSelectedStep(selectedStep);
                        details.setStepWinnerName(array.get(selectedStep).getName());
                        details.setStepWinnerPrice(Double.parseDouble(array.get(selectedStep).getPerUnitPrice()));
                    } else {
                        details.setSelectedStep(selectedStep);
                        details.setStepWinnerName("");
                        details.setStepWinnerPrice(0d);
                    }
                    if (productObject.getString("wishlist").equalsIgnoreCase("true")) {

                        whishlistBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.selectedbggreen));

                    } else {

                        whishlistBtn.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.whishlisttextgray));

                    }


                    Gson gson = new Gson();

                    Utils.WriteSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE, gson.toJson(details));

                    Fragment fragment = new ProductDetailsMain();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_product_details_frame, fragment).commit();

                } else {
                    Toast.makeText(getActivity(), getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    private class UpdateWhishList extends AsyncTask<String, String, String> {
        String pId, task;

        public UpdateWhishList(String productId, String task) {

            this.pId = productId;
            this.task = task;

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("uid", ReadSharePrefrence(getActivity(), USERID));
            hashMap.put("product_id", pId);
            hashMap.put("task", task);
            return utils.getResponseofPost(BASE_URL + "wishlist.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                    boolean isSucess = !task.equalsIgnoreCase("remove");
                    if (isSucess) {
                        details.setIsWhishList("false");
                    } else {
                        details.setIsWhishList("true");
                    }
                } else {
                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
