package com.app.archirayan.teamon.retrofit;

import com.app.archirayan.teamon.Model.NotificationListDetails;
import com.app.archirayan.teamon.retrofit.Model.Chat.ChatDetails;
import com.app.archirayan.teamon.retrofit.Model.EditEmailDetails;
import com.app.archirayan.teamon.retrofit.Model.NotificationUpdate.UpdateSettingDetails;
import com.app.archirayan.teamon.retrofit.Model.PaypalBasicRes;
import com.app.archirayan.teamon.retrofit.Model.PaypalEmail;
import com.app.archirayan.teamon.retrofit.Model.SendMessageDetails;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {
    @Headers({
            "Accept: application/json",
            "Accept-Language: en_US"
    })
    @FormUrlEncoded
    @POST("v1/oauth2/token")
    Call<PaypalBasicRes> getLogin(@Field("grant_type") String grantType);

    @GET
    Call<PaypalEmail> getEmailId(@Url String Url, @Header("Content-Type") String token, @Header("Authorization") String token1);

    @FormUrlEncoded
    @POST("change_email.php")
    Call<EditEmailDetails> updateEmail(@FieldMap Map<String, String> hashMap);

    @FormUrlEncoded
    @POST("change_password.php")
    Call<EditEmailDetails> updatePassword(@FieldMap Map<String, String> hashMap);

    @FormUrlEncoded
    @POST("edit_address.php")
    Call<EditEmailDetails> updateAddress(@FieldMap Map<String, String> hashMap);

    @FormUrlEncoded
    @POST("get_notification.php")
    Call<NotificationListDetails> getNotification(@FieldMap Map<String, String> hashMap);

    @FormUrlEncoded
    @POST("get_chat_data.php")
    Call<ChatDetails> getChatDetails(@FieldMap Map<String, String> hashMap);

    @FormUrlEncoded
    @POST("insert_chat_msg.php")
    Call<SendMessageDetails> addChatData(@FieldMap Map<String, String> hashMap);

    @FormUrlEncoded
    @POST("enable_disable_notification.php")
    Call<UpdateSettingDetails> settingIsUpdated(@FieldMap Map<String, String> mMap);

}