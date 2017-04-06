package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.retrofit.Model.Chat.MessageDetails;

import java.util.List;

/**
 * Created by archirayan on 31-Mar-17.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SENDERVIEW = 0;
    private static final int RECEIVERVIEW = 1;
    private Context context;
    private List<MessageDetails> chatArray;

    public ChatAdapter(List<MessageDetails> data) {
        this.chatArray = data;
    }

    @Override
    public int getItemCount() {
        return chatArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatArray.get(position).getSender().equalsIgnoreCase("0")) {
            return RECEIVERVIEW;
        } else {
            return SENDERVIEW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case SENDERVIEW:
                View sender = inflater.inflate(R.layout.adapter_senderview, parent, false);
                viewHolder = new SenderViewHolder(sender);
                break;
            case RECEIVERVIEW:
                View receiver = inflater.inflate(R.layout.adapter_receiverview, parent, false);
                viewHolder = new ReceiverViewHolder(receiver);
                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case SENDERVIEW:
                SenderViewHolder senderVH = (SenderViewHolder) holder;
                senderVH.msgTv.setText(chatArray.get(position).getText());

                break;
            case RECEIVERVIEW:
                ReceiverViewHolder receiverVH = (ReceiverViewHolder) holder;
                receiverVH.msgTv.setText(chatArray.get(position).getText());
                break;
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView msgTv;

        public SenderViewHolder(View itemView) {
            super(itemView);
            msgTv = (TextView) itemView.findViewById(R.id.adapter_sendview_text);
        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView msgTv;

        public ReceiverViewHolder(View itemView) {
            super(itemView);
            msgTv = (TextView) itemView.findViewById(R.id.adapter_receiverview_text);
        }
    }
}
