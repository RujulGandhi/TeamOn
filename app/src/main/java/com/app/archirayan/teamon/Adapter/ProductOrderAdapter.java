package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.archirayan.teamon.Model.CategoryDetails;
import com.app.archirayan.teamon.Model.ProductOrdersDetails;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.app.archirayan.teamon.Utils.Utils.getOrderStatus;

/**
 * Created by archirayan on 27-Dec-16.
 */

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.MyViewHolder> {

    private final List<ProductOrdersDetails> arrayList;
    public String Id;
    public Utils utils;
    public Context context;
    public LayoutInflater inflater;
    public FragmentManager fm;
    public LinearLayout linearLayout;
    public String isCompletedOrder;

    public ProductOrderAdapter(Context context, ArrayList<ProductOrdersDetails> arrayList, FragmentManager fm, String isCompletedOrder) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
        this.context = context;
        this.utils = new Utils(context);
        this.fm = fm;
        this.isCompletedOrder = isCompletedOrder;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_orders_list, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ProductOrdersDetails details = arrayList.get(position);

        Id = details.getOrderId();
        holder.orderProName.setText(details.getOrderProName());
        holder.orderProPrice.setText("$ " + details.getOrderProPrice());
        holder.orderProSalerName.setText("Sale by " + details.getOrderProSalerName());
        holder.orderProStatus.setText(getOrderStatus(details.getOrderStatus()));
        if (getOrderStatus(details.getOrderStatus()).equalsIgnoreCase("Waiting to shipping")) {
            holder.orderProStatus.setTextColor(ContextCompat.getColor(context, R.color.toolBarPink));
        } else {
            holder.orderProStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

//        holder.orderProName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isCompletedOrder.equals(Constant.COMPLETED)) {
//
//                    WriteSharePrefrence(context, ORDERID, Id);
//                    Fragment fragment = new OrderDetail();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.addToBackStack("orderDeliveryDetails");
//                    ft.replace(R.id.activity_main_frame_layout, fragment).commit();
//
//                } else{
//
//                    WriteSharePrefrence(context, ORDERID, Id);
//                    Fragment fragment = new OrderDetail();
//                    FragmentTransaction ft = fm.beginTransaction();
//                    ft.addToBackStack("orderDeliveryDetails");
//                    ft.replace(R.id.activity_main_frame_layout, fragment).commit();
//
//                }
//            }
//        });

        if (details.getOrderProImage().length() > 0) {
            Picasso.with(context).load(details.getOrderProImage()).placeholder(R.drawable.ic_placeholder).into(holder.orderProImage);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderProName, orderProPrice, orderProSalerName, orderProStatus;
        public ImageView orderProImage;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            orderProName = (TextView) view.findViewById(R.id.adapter_product_orders_list_txtname);
            orderProPrice = (TextView) view.findViewById(R.id.adapter_product_orders_list_txtprice);
            orderProSalerName = (TextView) view.findViewById(R.id.adapter_product_orders_list_txtsalername);
            orderProStatus = (TextView) view.findViewById(R.id.adapter_product_orders_list_txtstatus);
            orderProImage = (ImageView) view.findViewById(R.id.adapter_product_orders_list_image);
            linearLayout = (LinearLayout) view.findViewById(R.id.adapter_product_orders_description);
        }

        public void bind(final CategoryDetails item, final CategoryAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
