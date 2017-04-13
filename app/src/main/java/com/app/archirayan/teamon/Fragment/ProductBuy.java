package com.app.archirayan.teamon.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.sevenheaven.iosswitch.ShSwitchView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import butterknife.ButterKnife;

import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.getFinalPrice;
import static java.lang.Integer.parseInt;

/**
 * Created by Ravi archi on 12/20/2016.
 */

public class ProductBuy extends Fragment implements View.OnClickListener {
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constant.PAYPALCLIENTID);
    public Button cancelBtn;
    public String orderId, orderKey, orderTotal;
    public TextView txtResult, txtProprice, txtTime, txtMaintitle, txtQuantity, soldByTv, txtUnitsales,
            txtMainproprice, useBalanceTv, readTermsTv;
    public ImageView imgPlus, imgMinus;
    public int qty;
    public int discountAmount;
    public boolean isSwitchOn;
    public LinearLayout paypalLinear;
    public Utils utils;
    public Double currentBalance, perUnitProductPrice, productPrice;
    public Handler handler;
    public Runnable runnable;
    public PayPalPayment payment;
    public EditText txtDiscount;
    public CheckBox checkBox;
    public ShSwitchView walletSwitch;
    public Double walletBalance, amountToPay;
    Double resultAmount, resultWalletCredit;
    private ProductStoreSaleDetail productFullDetails;
    private Long time;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_buy_now, container, false);

        qty = 1;
        cancelBtn = (Button) view.findViewById(R.id.fragment_buy_now_btncancel);
        walletSwitch = (ShSwitchView) view.findViewById(R.id.fragment_buy_now_wallet_switch);
        utils = new Utils(getActivity());
        useBalanceTv = (TextView) view.findViewById(R.id.fragment_but_now_use_balance);
        checkBox = (CheckBox) view.findViewById(R.id.fragment_buy_check);
        soldByTv = (TextView) view.findViewById(R.id.fragment_buy_now_txtstorename);
        paypalLinear = (LinearLayout) view.findViewById(R.id.fragment_buy_now_paypal);
        txtTime = (TextView) view.findViewById(R.id.fragment_buy_now_time);
        txtMaintitle = (TextView) view.findViewById(R.id.fragment_buy_now_pro_title_top);
        txtMainproprice = (TextView) view.findViewById(R.id.fragment_buy_now_pro_price_top);
        txtProprice = (TextView) view.findViewById(R.id.fragment_buy_now_pro_price);
        imgPlus = (ImageView) view.findViewById(R.id.fragment_buy_now_imgplus);
        imgMinus = (ImageView) view.findViewById(R.id.fragment_buy_now_imgminus);
        txtResult = (TextView) view.findViewById(R.id.fragment_buy_now_result);
        txtDiscount = (EditText) view.findViewById(R.id.fragment_buy_now_discount);
        txtQuantity = (TextView) view.findViewById(R.id.fragment_buy_now_txtqualtity);
        txtUnitsales = (TextView) view.findViewById(R.id.fragment_buy_now_txtunitsales);
        readTermsTv = (TextView) view.findViewById(R.id.fragment_buy_now_readterms);

        discountAmount = parseInt(ReadSharePrefrence(getActivity(), Constant.MYBALANCE));
        startService();

        return view;
    }

    public void init() {
        isSwitchOn = false;
        walletSwitch.setOn(false);

        walletBalance = Double.parseDouble(ReadSharePrefrence(getActivity(), Constant.MYBALANCE));
        productPrice = productFullDetails.getStepWinnerPrice();
        discountAmount = parseInt(ReadSharePrefrence(getActivity(), Constant.MYBALANCE));

        time = productFullDetails.getTime();
        txtMaintitle.setText(productFullDetails.getTitle());
        txtProprice.setText("$ " + productFullDetails.getPrice());
        txtMainproprice.setText("$ " + String.format("%.2f", productFullDetails.getStepWinnerPrice()));
        txtDiscount.setText(String.valueOf(discountAmount));
        txtUnitsales.setText("Unit sales - " + productFullDetails.getSaleQty());
//        price = Double.parseDouble(productFullDetails.getPrice());

        soldByTv.setText(productFullDetails.getStepWinnerName());
        txtProprice.setText("$ " + productFullDetails.getStepWinnerPrice());
        perUnitProductPrice = productFullDetails.getStepWinnerPrice();

        imgPlus.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
        paypalLinear.setOnClickListener(this);
        useBalanceTv.setText("Use Balance -" + discountAmount);
        readTermsTv.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        walletSwitch.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                isSwitchOn = isOn;
                setDiscountAndResult();
            }
        });
        setDiscountAndResult();
        runnable = new Runnable() {
            @Override
            public void run() {
                time = time - 1;
                productFullDetails.setTime(time);
                txtTime.setText(Utils.getTimeInCounter(time));
                handler.postDelayed(this, 1000);

                Gson gson = new Gson();
                Utils.WriteSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE, gson.toJson(productFullDetails));
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 1000);
    }

    public void setDiscountAndResult() {
        if (isSwitchOn) {
            amountToPay = (qty * productPrice);
            if (amountToPay >= walletBalance) {
                resultAmount = amountToPay - walletBalance;
                resultWalletCredit = walletBalance;
            } else {
                resultAmount = 0d;
                resultWalletCredit = amountToPay;
            }
        } else {

            resultWalletCredit = 0d;
            resultAmount = (qty * productPrice);

        }
        txtResult.setText("$ " + resultAmount);

    }

    private void getArgument() {
        Gson gsonbuynow = new Gson();
        String strObj = ReadSharePrefrence(getActivity(), Constant.PRODUCT_STORE_SALE);
        productFullDetails = gsonbuynow.fromJson(strObj, ProductStoreSaleDetail.class);
        init();
    }

    private void startService() {
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_buy_now_paypal:
                qty = parseInt(txtQuantity.getText().toString());
                if (checkBox.isChecked() && qty > 0) {
                    new CreateOrder().execute();
                }
                break;
            case R.id.fragment_buy_now_btncancel:
                getActivity().onBackPressed();
                break;
            case R.id.fragment_buy_now_imgminus:
                qty = parseInt(txtQuantity.getText().toString());
                if (qty > 1) {
                    qty = qty - 1;
                    txtQuantity.setText("" + qty);
                    txtResult.setText("$ " + String.format("%.2f", getFinalPrice(perUnitProductPrice, qty, discountAmount)));
                }
                break;

            case R.id.fragment_buy_now_imgplus:
                qty = parseInt(txtQuantity.getText().toString());
                qty = qty + 1;
                txtQuantity.setText("" + qty);
                txtResult.setText("$ " + String.format("%.2f", getFinalPrice(perUnitProductPrice, qty, discountAmount)));
                break;
            case R.id.fragment_buy_now_readterms:
                String storeImageUrl, aboutStoreStr, addressStoreStr;
                storeImageUrl = (productFullDetails.getUnitStep().get(productFullDetails.getSelectedStep()).getUserImage());
                aboutStoreStr = (productFullDetails.getUnitStep().get(productFullDetails.getSelectedStep()).getAboutStore());
                addressStoreStr = (productFullDetails.getUnitStep().get(productFullDetails.getSelectedStep()).getUserAddress());

                final Dialog dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_store_details);
                dialog.show();

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

                break;

        }

        txtQuantity.setText(String.valueOf(qty));
    }

    public void onBuyPressed() {

        // PAYMENT_INTENT_SALE will cause the payment to complete immediately.
        // Change PAYMENT_INTENT_SALE to
        //   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
        //   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
        //     later via calls from your server.
        payment = new PayPalPayment(new BigDecimal(productPrice), "USD", productFullDetails.getTitle(),
                PayPalPayment.PAYMENT_INTENT_SALE);

        String jsonPaypal = "{\"order_id\":" + orderId + ",\"order_key\":\"" + orderKey + "\"}";
        Log.d("OrderPaypal", jsonPaypal);
        payment.custom(jsonPaypal);
        Intent intent = new Intent(getActivity(), PaymentActivity.class);
        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {

                    String response = confirm.toJSONObject().toString(4);
                    JSONObject object = new JSONObject(response);
                    String transactionId = object.getJSONObject("response").getString("id");

                    new UpdateOrder(transactionId, qty, productFullDetails.getProId()).execute();

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
        }
    }

    private class UpdateOrder extends AsyncTask<String, String, String> {
        public String transactionId, proId;
        public int qty;
        public ProgressDialog pd;

        public UpdateOrder(String transactionId, int qty, String proId) {
            this.transactionId = transactionId;
            this.proId = proId;
            this.qty = qty;
        }

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
//            http://easydatasearch.com/easydata1/auction_wp/api/get_product.php?pid=1590&uid=69
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("pid", proId);
            hashMap.put("qty", String.valueOf(qty));
            hashMap.put("uid", ReadSharePrefrence(getActivity(), Constant.USERID));
            hashMap.put("transaction_id", transactionId);
            hashMap.put("order_id", orderId);
            return utils.getResponseofPost(Constant.BASE_URL + "complete_payment.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);

                if (object.getString("status").equalsIgnoreCase("true")) {

                    Fragment fragment = null;
                    Bundle bundle = new Bundle();
                    bundle.putLong("qty", qty);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fragment = new ProductThankyou();
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.addToBackStack(null);
                    ft.replace(R.id.fragment_product_details_frame, fragment).commit();

                } else {
                    Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), "" + s, Toast.LENGTH_SHORT).show();
        }
    }

    private class CreateOrder extends AsyncTask<String, String, String> {

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
            hashMap.put("pid", productFullDetails.getProId());
            hashMap.put("uid", ReadSharePrefrence(getActivity(), Constant.USERID));
            hashMap.put("qty", String.valueOf(qty));
            hashMap.put("credit", String.valueOf(resultWalletCredit));
            return utils.getResponseofPost(Constant.BASE_URL + "get_sale_quntity.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("Response", s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {

                    orderId = object.getJSONObject("data").getString("order_id");
                    orderKey = object.getJSONObject("data").getString("order_key");
                    orderTotal = object.getJSONObject("data").getString("order_total");
//                    int step = Utils.getSelectedStep(productFullDetails.getUnitStep(), saleQty);
//                    productPrice = Double.valueOf(productFullDetails.getUnitStep().get(step).getPerUnitPrice());
                    productPrice = Double.parseDouble(orderTotal);
                    if (productPrice == 0) {
                        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
                        StringBuilder sb = new StringBuilder();
                        Random random = new Random();
                        for (int i = 0; i < 15; i++) {
                            char c = chars[random.nextInt(chars.length)];
                            sb.append(c);
                        }
                        String output = sb.toString();

                        new UpdateOrder(output, qty, productFullDetails.getProId()).execute();

                    } else {
                        onBuyPressed();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
            }
        }
    }
}





