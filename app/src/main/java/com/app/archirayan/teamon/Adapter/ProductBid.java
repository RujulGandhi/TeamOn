package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.archirayan.teamon.Fragment.ProductDetailsMain;
import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.Model.UnitStep;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by archirayan on 22-Nov-16.
 */

public class ProductBid extends BaseAdapter {
    private final ProductStoreSaleDetail productFullDetails;
    public Context context;
    public LayoutInflater inflater;
    public ArrayList<UnitStep> arrayList;
    public Utils utils;
    public FragmentManager fm;
    public Long saleQty;

    public ProductBid(Context context, ArrayList<UnitStep> arrayListUnitSteps, ProductStoreSaleDetail details, Long saleQty, FragmentManager supportFragmentManager) {
        this.context = context;
        this.saleQty = saleQty + 1;
        this.arrayList = arrayListUnitSteps;
        this.productFullDetails = details;
        this.utils = new Utils(context);
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

        View view = inflater.inflate(R.layout.adapter_bid, null);

        UnitStep details = arrayList.get(position);

        TextView textView = (TextView) view.findViewById(R.id.adapter_bid_bid_description);

        textView.setText(details.getFromUnitStep() + " - " + details.getToUnit() + " \n" + details.getName() + " - $" + details.getPerUnitPrice());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ProductDetailsMain();
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString("productstoresaledetails", gson.toJson(productFullDetails));

                if (fragment != null) {

                    fragment.setArguments(bundle);
                    FragmentTransaction ft = fm.beginTransaction();

                    ft.addToBackStack(null);
                    ft.replace(R.id.fragment_product_details_frame, fragment).commit();
                }

            }
        });

        long startUnit = Long.parseLong(arrayList.get(position).getFromUnitStep());
        long endUnit = Long.parseLong(arrayList.get(position).getToUnit());

        if (startUnit <= saleQty && endUnit >= saleQty) {

            textView.setTextColor(ContextCompat.getColor(context, R.color.toolBarPink));

        }
        return view;
    }

}

