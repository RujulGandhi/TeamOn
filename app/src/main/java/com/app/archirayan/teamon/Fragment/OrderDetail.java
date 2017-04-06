package com.app.archirayan.teamon.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.Model.OrderDetails;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.archirayan.teamon.Utils.Constant.BASE_URL;
import static com.app.archirayan.teamon.Utils.Constant.MYBALANCE;
import static com.app.archirayan.teamon.Utils.Constant.NOTCOMPLETED;
import static com.app.archirayan.teamon.Utils.Constant.ORDERDETAILSKEY;
import static com.app.archirayan.teamon.Utils.Constant.ORDERID;
import static com.app.archirayan.teamon.Utils.Constant.ORDERSTATUS;
import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.getOrderStatus;

/**
 * Created by Ravi archi on 1/3/2017.
 */

public class OrderDetail extends Fragment {
    public String orderID, orderStatus;


    @BindView(R.id.fragment_order_detail_pro_iv)
    public ImageView productImage;
    @BindView(R.id.fragment_order_detail_proname_tv)
    public TextView productNameTv;
    @BindView(R.id.fragment_completed_orders_delivery_detail_problem)
    public LinearLayout deliveryDetailsLinear;
    @BindView(R.id.fragment_order_detail_pro_seller_name_tv)
    public TextView sellerTv;
    @BindView(R.id.fragment_order_detail_order_status_tv)
    public TextView statusTv;
    @BindView(R.id.fragment_order_detail_proprice_tv)
    public TextView priceTv;
    @BindView(R.id.fragment_completed_orders_delivery_detail_txtclosed)
    public TextView closeTv;
    @BindView(R.id.fragment_completed_orders_delivery_detail_txtpaymentmethod)
    public TextView paymentMethodTv;
    @BindView(R.id.fragment_completed_orders_delivery_detail_txtunit)
    public TextView qtyTv;
    @BindView(R.id.fragment_completed_orders_delivery_detail_txtpaid)
    public TextView paidAmountTv;
    @BindView(R.id.fragment_completed_orders_delivery_detail_txtgetback)
    public TextView cashBackTv;
    @BindView(R.id.fragment_completed_orders_delivery_detail_txtdeliverymethod)
    public TextView shoppingMethodTv;
    @BindView(R.id.fragment_order_details_status)
    public TextView statusOfOrderTv;
    @BindView(R.id.fragment_orders_detail_gotosale_tv)
    public TextView goToSaleTv;
    @BindView(R.id.fragment_order_details_menus)
    public LinearLayout menuLinearLayout;

    public OrderDetails details;
    public String storeImageUrl, aboutStoreStr, addressStoreStr;
    private Utils utils;
    private ArrayList<String> userArray;
    private Dialog withdrawDialog;

    @Override
    public void onResume() {
        super.onResume();
        orderID = ReadSharePrefrence(getActivity(), ORDERID);
        orderStatus = ReadSharePrefrence(getActivity(), ORDERSTATUS);
        setViewFromOrder();

        if (orderID != null && utils.isConnectingToInternet()) {

            new GetOrderDetails().execute();

        }

    }

    private void setViewFromOrder() {
        if (orderStatus.equals(NOTCOMPLETED)) {
            menuLinearLayout.setVisibility(View.GONE);
        } else {
            goToSaleTv.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.fragment_completed_orders_delivery_detail_storedetails)
    void submitButton(View view) {
        if (view.getId() == R.id.fragment_completed_orders_delivery_detail_storedetails) {
            final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_store_details);

            TextView aboutStoreTv = ButterKnife.findById(dialog, R.id.text_dialog_storedetails_aboutstore);
            aboutStoreTv.setText(aboutStoreStr);

            TextView addressStoreTv = ButterKnife.findById(dialog, R.id.text_dialog_storedetails_addressstore);
            addressStoreTv.setText(addressStoreStr);

            ImageView sellerIv = ButterKnife.findById(dialog, R.id.image_dialog_storedetails);
            if (storeImageUrl.length() > 0) {
                Picasso.with(getActivity()).load(storeImageUrl).placeholder(R.drawable.ic_placeholder).into(sellerIv);
            }
            ImageView backIv = ButterKnife.findById(dialog, R.id.ivBack_dialogStoreDetails);
            backIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    @OnClick(R.id.fragment_completed_orders_delivery_detail_problem)
    void orderDeliveryProblem(View view) {
        if (view.getId() == R.id.fragment_completed_orders_delivery_detail_problem) {
            Gson gson = new Gson();
            String orderDetailsObj = gson.toJson(details);
            Bundle bundle = new Bundle();
            bundle.putString(ORDERDETAILSKEY, orderDetailsObj);

            Fragment fragment = new OrderDeliveryDetail();
            fragment.setArguments(bundle);
            FragmentTransaction ft = (getActivity().getSupportFragmentManager()).beginTransaction();
            ft.addToBackStack("orderDeliveryDetails");
            ft.replace(R.id.activity_main_frame_layout, fragment).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        ButterKnife.bind(this, view);
        utils = new Utils(getActivity());
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_activity_navigation, menu);
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

    private class GetOrderDetails extends AsyncTask<String, String, String> {

        public ProgressDialog pd;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("order_id", orderID);
            hashMap.put("status", orderStatus);
            return utils.getResponseofPost(BASE_URL + "order_detail_by_id3.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            JSONObject object = null;
            try {
                object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    JSONObject orderDetail = object.getJSONObject("data");
                    JSONObject productDetail = orderDetail.getJSONObject("product");

                    closeTv.setText(productDetail.getString("product_close"));
                    statusTv.setText(getOrderStatus(orderDetail.getString("order_tracking_status")));
                    paidAmountTv.setText("You pay -" + orderDetail.getString("order_price"));
                    productNameTv.setText(productDetail.getString("post_title"));
                    qtyTv.setText("Unit -" + orderDetail.getString("order_qty"));
                    priceTv.setText("$ " + productDetail.getString("product_price"));
                    sellerTv.setText("Sale by " + productDetail.getString("seller_name"));
                    statusOfOrderTv.setText(getOrderStatus(orderDetail.getString("order_tracking_status")));
                    String imageUrl = productDetail.getString("product_image");
                    shoppingMethodTv.setText(orderDetail.getString("delivery_method"));
                    if (imageUrl.length() > 0) {
                        Picasso.with(getActivity()).load(imageUrl).placeholder(R.drawable.ic_placeholder).into(productImage);
                    }
                    storeImageUrl = productDetail.getString("seller_image");
                    addressStoreStr = productDetail.getString("seller_address");
                    aboutStoreStr = productDetail.getString("seller_info");

                    details = new OrderDetails();
                    details.setSellerName(productDetail.getString("seller_name"));
                    details.setSellerShopDescription(aboutStoreStr);
                    details.setSellerShopImage(storeImageUrl);
                    details.setSellerStoreAddress(addressStoreStr);
                    details.setOrderId(orderDetail.getString("order_id"));
                    details.setOrderDate(orderDetail.getString("order_date"));
                    details.setOrderImage(imageUrl);
                    details.setOrderPrice(productDetail.getString("product_price"));
                    details.setOrderQty(orderDetail.getString("order_qty"));
                    details.setShippingMethod(orderDetail.getString("delivery_method"));
                    details.setOrderStatus(getOrderStatus(orderDetail.getString("order_tracking_status")));
                    details.setOrderOriginalPrice(orderDetail.getString("order_price"));
                    details.setOrderTitle(productDetail.getString("post_title"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
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
