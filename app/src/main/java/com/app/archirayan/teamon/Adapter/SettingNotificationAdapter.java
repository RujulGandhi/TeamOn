package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.archirayan.teamon.Model.NotificationSettingDetails;
import com.app.archirayan.teamon.R;
import com.sevenheaven.iosswitch.ShSwitchView;

import java.util.ArrayList;

/**
 * Created by archirayan on 06-Apr-17.
 */

public class SettingNotificationAdapter extends BaseAdapter {
    ArrayList<NotificationSettingDetails> array;
    Context context;

    public SettingNotificationAdapter(Context context, ArrayList<NotificationSettingDetails> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = View.inflate(context, R.layout.adapter_setting_notification, null);

        ShSwitchView switchTv = (ShSwitchView) convertView.findViewById(R.id.adapter_setting_noti_switch);
        TextView nameTv = (TextView) convertView.findViewById(R.id.adapter_setting_noti_name);

        switchTv.setEnabled(true);
        nameTv.setText(array.get(i).getNotName());

        return convertView;
    }
}
