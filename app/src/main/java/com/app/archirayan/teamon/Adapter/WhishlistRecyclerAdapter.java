package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.archirayan.teamon.Model.ProductDetail;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.app.archirayan.teamon.Utils.Constant.ANYUPDATE;
import static com.app.archirayan.teamon.Utils.Utils.WriteSharePrefrence;

/**
 * Created by archirayan on 25-Mar-17.
 */

public class WhishlistRecyclerAdapter extends RecyclerView.Adapter<WhishlistRecyclerAdapter.ViewHolder> {
    public ArrayList<ProductDetail> arrayList;
    public Context context;
    public FragmentManager fm;
    private Utils utils;

    public WhishlistRecyclerAdapter(Context context, ArrayList<ProductDetail> arrayList, FragmentManager supportFragmentManager) {
        this.arrayList = new ArrayList<>();
        this.arrayList = arrayList;
        this.context = context;
        this.utils = new Utils(context);
        this.fm = supportFragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_list, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public ProductDetail getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ProductDetail details = arrayList.get(position);
        final int tempPos = position;

        holder.productNametv.setText(details.getProductName());
        holder.productPricetv.setText(details.getProductPrice());
        CountDownTimer timer = new CountDownTimer(details.getCounter(), 1000) {
            @Override
            public void onTick(long l) {
                details.setCounter(details.getCounter() - 1);
                holder.productSaletv.setText(details.getProductSales() + " Sold " + Utils.getTimeInCounter(details.getCounter()));
            }

            @Override
            public void onFinish() {
            }
        }.start();

        holder.productOldPricetv.setText("$" + details.getProductOldPrice());
        holder.productOldPricetv.setPaintFlags(holder.productOldPricetv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productSalerNametv.setText("Sale by " + details.getProductSalerName());
        if (details.getProductImage().length() > 0) {
            Picasso.with(context).load(details.getProductImage()).placeholder(R.drawable.ic_placeholder).into(holder.productImage);
        }

        if (details.getIsWhishList().equalsIgnoreCase("true")) {
            holder.productWishlisttv.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedbggreen));
            holder.productWishlisttv.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.productWishlisttv.setText(R.string.whistlist_green);
        } else {
            holder.productWishlisttv.setBackgroundColor(ContextCompat.getColor(context, R.color.unselectedbggray));
            holder.productWishlisttv.setTextColor(ContextCompat.getColor(context, R.color.whishlisttextgray));
            holder.productWishlisttv.setText(R.string.whistlist_gray);
        }

        holder.productWishlisttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = "";
                String isInWhishList = details.getIsWhishList();
                task = isInWhishList.equalsIgnoreCase("true") ? "remove" : "add";

                if (task.equalsIgnoreCase("add")) {
                    arrayList.get(tempPos).setIsWhishList("true");
                } else {
                    arrayList.get(tempPos).setIsWhishList("false");
                }
                notifyDataSetChanged();

                new UpdateWhishList(details.getProductId(), task, tempPos).execute();

            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNametv, productSaletv, productPricetv, productOldPricetv, productSalerNametv, productWishlisttv;
        ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productNametv = (TextView) itemView.findViewById(R.id.adapter_product_name);
            productSaletv = (TextView) itemView.findViewById(R.id.adapter_product_sale);
            productPricetv = (TextView) itemView.findViewById(R.id.adapter_product_price);
            productOldPricetv = (TextView) itemView.findViewById(R.id.adapter_product_old_price);
            productSalerNametv = (TextView) itemView.findViewById(R.id.adapter_product_saler_name);
            productWishlisttv = (TextView) itemView.findViewById(R.id.adapter_product_wishlist);
            productImage = (ImageView) itemView.findViewById(R.id.adapter_product_image);
        }
    }

    private class UpdateWhishList extends AsyncTask<String, String, String> {
        String pId, task;
        int pos;

        public UpdateWhishList(String productId, String task, int pos) {
            this.pos = pos;
            this.pId = productId;
            this.task = task;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("uid", Utils.ReadSharePrefrence(context, Constant.USERID));
            hashMap.put("product_id", pId);
            hashMap.put("task", task);
            return utils.getResponseofPost(Constant.BASE_URL + "wishlist.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {

                    arrayList.remove(pos);
                    notifyDataSetChanged();
                    WriteSharePrefrence(context, ANYUPDATE, "1");

                } else {
                    if (task.equalsIgnoreCase("add")) {
                        arrayList.get(pos).setIsWhishList("false");
                    } else {
                        arrayList.get(pos).setIsWhishList("true");
                    }
                    notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
