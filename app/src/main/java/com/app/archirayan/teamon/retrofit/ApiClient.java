package com.app.archirayan.teamon.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.archirayan.teamon.Utils.Constant.BASE_URL;

public class ApiClient {

    public static final String BASE_URL_PAYPAL = "https://api.sandbox.paypal.com/";
    private static Retrofit retrofit = null;


    public static Retrofit getClientPaypal() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}