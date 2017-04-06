package com.app.archirayan.teamon.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.archirayan.teamon.Model.CategoryDetails;
import com.app.archirayan.teamon.Model.ProductDetail;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.app.archirayan.teamon.Utils.Constant.ALLCATEGORY;
import static com.app.archirayan.teamon.Utils.Constant.ALLPRODUCT;
import static com.app.archirayan.teamon.Utils.Constant.BASE_URL;
import static com.app.archirayan.teamon.Utils.Constant.ENDPRICE;
import static com.app.archirayan.teamon.Utils.Constant.ENDSALEQTY;
import static com.app.archirayan.teamon.Utils.Constant.MAXPRICE;
import static com.app.archirayan.teamon.Utils.Constant.MAXSALE;
import static com.app.archirayan.teamon.Utils.Constant.NOTIFICATIONCOUNT;
import static com.app.archirayan.teamon.Utils.Constant.SELECTEDCATID;
import static com.app.archirayan.teamon.Utils.Constant.SORTING_BY;
import static com.app.archirayan.teamon.Utils.Constant.STARTPRICE;
import static com.app.archirayan.teamon.Utils.Constant.STARTSALEQTY;
import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Constant.WALLETBALANCE;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.WriteSharePrefrence;

/**
 * Created by archirayan on 02-Feb-17.
 */

public class LoadProductActivity extends Activity {

    public Utils utils;
    public String uId;
    public ArrayList<ProductDetail> arrayList;
    public ArrayList<CategoryDetails> categoryArray;
    public AVLoadingIndicatorView loading;
    public String notification_count;
    private Long MaxValuePrice, MaxValueSale, currentBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_product);

        loading = (AVLoadingIndicatorView) findViewById(R.id.loading_product);
        utils = new Utils(LoadProductActivity.this);
        uId = ReadSharePrefrence(LoadProductActivity.this, USERID);

        String isFirstTime = utils.getReadSharedPrefrenceIsFirstTime();
        if (isFirstTime.equalsIgnoreCase("")) {
            Intent i = new Intent(LoadProductActivity.this, SplashActivity.class);
            startActivity(i);
        } else {
            if (utils.isConnectingToInternet()) {
                WriteSharePrefrence(LoadProductActivity.this, STARTPRICE, "");
                WriteSharePrefrence(LoadProductActivity.this, ENDPRICE, "");
                WriteSharePrefrence(LoadProductActivity.this, STARTSALEQTY, "");
                WriteSharePrefrence(LoadProductActivity.this, ENDSALEQTY, "");
                WriteSharePrefrence(LoadProductActivity.this, SELECTEDCATID, "");
                WriteSharePrefrence(LoadProductActivity.this, SORTING_BY, "product_name");
                new GetAllProduct().execute();
            } else {
                Toast.makeText(this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetAllProduct extends AsyncTask<String, String, String> {
        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
            categoryArray = new ArrayList<>();
            arrayList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("uid", uId);
            hashMap.put("sort_by", "product_name");
            String s = utils.getResponseofPost(BASE_URL + "get_product_list.php", hashMap);

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    JSONArray mainArray = object.getJSONArray("data");
                    JSONArray mainCategoryArray = object.getJSONArray("category");
                    MaxValuePrice = Long.valueOf(object.getString("max_price"));
                    MaxValueSale = Long.valueOf(object.getString("max_stock"));
                    currentBalance = Long.valueOf(object.getString("current_balance"));
                    notification_count = object.getString("notification_count");
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


                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("Exception", e.toString());
            }
//            loading.hide();


            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            loading.hide();

            Gson gson = new Gson();
            String allProduct = gson.toJson(arrayList);
            String allCategory = gson.toJson(categoryArray);

            WriteSharePrefrence(LoadProductActivity.this, ALLPRODUCT, allProduct);
            WriteSharePrefrence(LoadProductActivity.this, ALLCATEGORY, allCategory);
            WriteSharePrefrence(LoadProductActivity.this, MAXPRICE, String.valueOf(MaxValuePrice));
            WriteSharePrefrence(LoadProductActivity.this, MAXSALE, String.valueOf(MaxValueSale));
            WriteSharePrefrence(LoadProductActivity.this, WALLETBALANCE, String.valueOf(currentBalance));
            WriteSharePrefrence(LoadProductActivity.this, NOTIFICATIONCOUNT, String.valueOf(notification_count));

            Intent in = new Intent(LoadProductActivity.this, MainActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(in);
            finish();

        }
    }
}
