package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.Fragment.ProductFullDetailsMain;
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
 * Created by archirayan on 22-Nov-16.
 */

public class ProductWhishListAdapter extends BaseAdapter {
    public Context context;
    public LayoutInflater inflater;
    public ArrayList<ProductDetail> arrayList;
    public Utils utils;
    public FragmentManager fm;

    public ProductWhishListAdapter(Context context, ArrayList<ProductDetail> arrayListCommentds, FragmentManager supportFragmentManager) {
        this.context = context;
        this.utils = new Utils(context);
        this.arrayList = arrayListCommentds;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fm = supportFragmentManager;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return arrayList.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.adapter_product_list, null);
        final ProductDetail details = arrayList.get(position);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.adapter_product_linear);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ProductFullDetailsMain();
                Bundle bundle = new Bundle();
                bundle.putString("pId", details.getProductId());
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.activity_main_frame_layout, fragment);
                    transaction.addToBackStack("home");
                    transaction.commit();
                }
            }
        });

        final TextView productNametv = (TextView) view.findViewById(R.id.adapter_product_name);
        TextView productPricetv = (TextView) view.findViewById(R.id.adapter_product_price);
        final TextView productSaletv = (TextView) view.findViewById(R.id.adapter_product_sale);
        TextView productOldPricetv = (TextView) view.findViewById(R.id.adapter_product_old_price);
        TextView productSalerNametv = (TextView) view.findViewById(R.id.adapter_product_saler_name);
        final TextView productWishlisttv = (TextView) view.findViewById(R.id.adapter_product_wishlist);
        ImageView productImage = (ImageView) view.findViewById(R.id.adapter_product_image);


        productOldPricetv.setText("$" + details.getProductOldPrice());
        productOldPricetv.setPaintFlags(productOldPricetv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        productNametv.setText(details.getProductName());
        productPricetv.setText("$" + details.getProductPrice());

        productSalerNametv.setText("Sale by " + details.getProductSalerName());

        if (details.getProductImage().length() > 0) {
            Picasso.with(context).load(details.getProductImage()).placeholder(R.drawable.ic_placeholder).into(productImage);
        }
        productSaletv.setText(details.getProductSales() + " Sold " + Utils.getTimeInCounter(details.getCounter()));

        if (details.getIsWhishList().equalsIgnoreCase("true")) {

            productWishlisttv.setBackgroundColor(ContextCompat.getColor(context, R.color.selectedbggreen));
            productWishlisttv.setTextColor(ContextCompat.getColor(context, R.color.white));
            productWishlisttv.setText(R.string.whistlist_green);

        } else {

            productWishlisttv.setBackgroundColor(ContextCompat.getColor(context, R.color.unselectedbggray));
            productWishlisttv.setTextColor(ContextCompat.getColor(context, R.color.whishlisttextgray));
            productWishlisttv.setText(R.string.whistlist_gray);

        }

        productWishlisttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String task = "";
                String isInWhishList = details.getIsWhishList();
                task = isInWhishList.equalsIgnoreCase("true") ? "remove" : "add";

                if (task.equalsIgnoreCase("add")) {
                    arrayList.get(position).setIsWhishList("true");
                } else {
                    arrayList.get(position).setIsWhishList("false");
                }
                notifyDataSetChanged();
                new UpdateWhishList(details.getProductId(), task, position).execute();

            }
        });
        return view;
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

                    Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    arrayList.remove(pos);
                    notifyDataSetChanged();
                    WriteSharePrefrence(context, ANYUPDATE, "1");

                } else {
                    Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
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

