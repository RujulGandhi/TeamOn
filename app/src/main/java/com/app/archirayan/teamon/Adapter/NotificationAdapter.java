package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.archirayan.teamon.Model.NotificationDetail;
import com.app.archirayan.teamon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by archirayan on 16-Mar-17.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    public List<NotificationDetail> list;
    public Context context;

    public NotificationAdapter(Context context, List<NotificationDetail> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_notification, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleMsgTv.setText(list.get(position).getTitle());
        holder.msgTv.setText(list.get(position).getMessage());
        if (list.get(position).getProductImage().length() > 0) {
            Picasso.with(context).load(list.get(position).getProductImage()).placeholder(R.drawable.ic_placeholder).into(holder.productImageIv);
        } else {
            Picasso.with(context).load(R.drawable.ic_placeholder).placeholder(R.drawable.ic_placeholder).into(holder.productImageIv);
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleMsgTv, msgTv;
        public ImageView productImageIv;

        public ViewHolder(View itemView) {
            super(itemView);
            titleMsgTv = (TextView) itemView.findViewById(R.id.adapter_notification_title);
            msgTv = (TextView) itemView.findViewById(R.id.adapter_notification_msg);
            productImageIv = (ImageView) itemView.findViewById(R.id.adapter_notification_image);
        }
    }


}
