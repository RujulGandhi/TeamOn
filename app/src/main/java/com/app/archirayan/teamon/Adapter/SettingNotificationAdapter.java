package com.app.archirayan.teamon.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.Model.NotificationSettingDetails;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.retrofit.ApiClient;
import com.app.archirayan.teamon.retrofit.ApiInterface;
import com.app.archirayan.teamon.retrofit.Model.NotificationUpdate.UpdateSettingDetails;
import com.google.gson.Gson;
import com.sevenheaven.iosswitch.ShSwitchView;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.archirayan.teamon.Utils.Constant.SETTING_NOTIFICATION;
import static com.app.archirayan.teamon.Utils.Constant.USERID;
import static com.app.archirayan.teamon.Utils.Utils.ReadSharePrefrence;
import static com.app.archirayan.teamon.Utils.Utils.WriteSharePrefrence;

/**
 * Created by archirayan on 06-Apr-17.
 */

public class SettingNotificationAdapter extends BaseAdapter {
    public String userId;
    ArrayList<NotificationSettingDetails> array;
    Context context;
    private ApiInterface apiInterface;


    public SettingNotificationAdapter(Context context, ArrayList<NotificationSettingDetails> array) {
        this.array = array;
        this.context = context;
        userId = ReadSharePrefrence(context, USERID);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View convertView = View.inflate(context, R.layout.adapter_setting_notification, null);

        ShSwitchView switchNotification = (ShSwitchView) convertView.findViewById(R.id.adapter_setting_noti_switch);
        TextView nameTv = (TextView) convertView.findViewById(R.id.adapter_setting_noti_name);
        if (array.get(i).getIsEnable().equalsIgnoreCase("true")) {

            switchNotification.setOn(true);

        } else {

            switchNotification.setOn(false);

        }

        nameTv.setText(array.get(i).getNotName());
        switchNotification.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                HashMap<String, String> mMap = new HashMap<>();
                mMap.put("uid", userId);
                mMap.put("type", array.get(i).getNotId());
                mMap.put("status", "" + isOn);
                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<UpdateSettingDetails> callApi = apiInterface.settingIsUpdated(mMap);
                callApi.enqueue(new Callback<UpdateSettingDetails>() {
                    @Override
                    public void onResponse(Call<UpdateSettingDetails> call, Response<UpdateSettingDetails> response) {
                        if (response.body() != null) {
                            if (response.body().getStatus().equalsIgnoreCase("true")) {
                                Gson gson = new Gson();
                                array.get(i).setIsEnable("true");
                                String allSettingNoti = gson.toJson(array);
                                WriteSharePrefrence(context, SETTING_NOTIFICATION, allSettingNoti);
                            } else {
                                Toast.makeText(context, R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateSettingDetails> call, Throwable t) {
                        Toast.makeText(context, R.string.somethingwentwrong, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        return convertView;
    }
}
