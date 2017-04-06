package com.app.archirayan.teamon.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.app.archirayan.teamon.Model.ProductStoreSaleDetail;
import com.app.archirayan.teamon.Model.UnitStep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by archirayan on 4/29/2016.
 */
public class Utils {

    Context context;
    SharedPreferences sp;
    Location getLocation;

    public Utils(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(Constant.Prefrence, Context.MODE_PRIVATE);
    }

    public static void WriteSharePrefrence(Context context, String key,
                                           String value) {
        @SuppressWarnings("static-access")
//        SharedPreferences write_Data = context.getSharedPreferences(
//                Constant.SHRED_PR.SHARE_PREF, context.MODE_PRIVATE);
//        Editor editor = write_Data.edit();
//        editor.putString(key, values);
//        editor.apply();

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String ReadSharePrefrence(Context context, String key) {

        String data;
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();
        data = preferences.getString(key, "");
        editor.commit();
        return data;
    }

    public static void ClearaSharePrefrence(Context context) {
//        SharedPreferences read_data = context.getSharedPreferences(
//                Constant.SHRED_PR.SHARE_PREF, context.MODE_PRIVATE);
//
//        return read_data.getString(key, "");

        String data;
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit().clear();

        editor.commit();

    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static long getDifferent(Date startDate, Date endDate) {


        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        return different / 1000;


//        Log.d("Difference", "startDate: " + startDate.toString() + " endDate: " + endDate + " Year : " + diffYear + " Months : " + diffMonth + " Days :" + elapsedDays + " Hours :" +
//                elapsedHours + " Mint :" + elapsedMinutes + " Seconds :" + elapsedSeconds);

    }

    public static String getTimeInCounter(long different) {

//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//        long monthInMili = daysInMilli * 30;
//
//        long elapsedMonths = different / monthInMili;
////        different = different % elapsedMonths;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;


//        $weeks = floor(($diff - $years * 365 * 60 * 60 * 24 - $months * 30 * 60 * 60 * 24) / (7 * 60 * 60 * 24));


//        long years = different / (365 * 60 * 60 * 24);
//        long months = (different - years * 365 * 60 * 60 * 24) / (30 * 60 * 60 * 24);
//        long weeks = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24) / (7 * 60 * 60 * 24);
        long days = (different) / (60 * 60 * 24);
        long hours = (different - days * 60 * 60 * 24) / (60 * 60);
        long minuts = (different - days * 60 * 60 * 24 - hours * 60 * 60) / 60;
        long seconds = (different - days * 60 * 60 * 24 - hours * 60 * 60 - minuts * 60);

        DecimalFormat df = new DecimalFormat("00");
        ArrayList<Long> array = new ArrayList<>();
        array.add(days);
        array.add(hours);
        array.add(minuts);
        array.add(seconds);
        String response = "";

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) != 0) {
                for (int k = 0; k < array.size(); k++) {
                    response = (k == 0) ? "" + df.format(array.get(k)) : response + ":" + df.format(array.get(k));
                }
                break;
            } else {
                array.remove(i);
            }
        }
//        int index = 0;
//        for (Long arrayLong : array) {
//            if (array.get(index) == 0) {
//                array.remove(index);
//            } else {
//                response = android.text.TextUtils.join(":", array);
//                break;
//            }
//            index++;
//        }

//        return " " + years + ":" + months + ":" + weeks + ":" + days + ":" + hours + ":" + minuts + ":" + seconds;
        return response;
    }

    public static String getTimeInCounter1(long different) {

//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//        long monthInMili = daysInMilli * 30;
//
//        long elapsedMonths = different / monthInMili;
////        different = different % elapsedMonths;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;


//        $weeks = floor(($diff - $years * 365 * 60 * 60 * 24 - $months * 30 * 60 * 60 * 24) / (7 * 60 * 60 * 24));


        long years = different / (365 * 60 * 60 * 24);
        long months = (different - years * 365 * 60 * 60 * 24) / (30 * 60 * 60 * 24);
        long weeks = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24) / (7 * 60 * 60 * 24);
        long days = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - weeks * 7 * 60 * 60 * 24) / (60 * 60 * 24);
        long hours = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - weeks * 7 * 60 * 60 * 24 - days * 60 * 60 * 24) / (60 * 60);
        long minuts = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - weeks * 7 * 60 * 60 * 24 - days * 60 * 60 * 24 - hours * 60 * 60) / 60;
//        long seconds = (different - years * 365 * 60 * 60 * 24 - months * 30 * 60 * 60 * 24 - weeks * 7 * 60 * 60 * 24 - days * 60 * 60 * 24 - hours * 60 * 60 - minuts * 60);
        long seconds = 9;

        ArrayList<Long> array = new ArrayList<>();
        array.add(Long.valueOf(new DecimalFormat("00").format(years)));
        array.add(months);
        array.add(weeks);
        array.add(days);
        array.add(hours);
        array.add(minuts);
        array.add(Long.valueOf(new DecimalFormat("##").format(seconds)));
        String response = "";

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) != 0) {
                for (int k = 0; k < array.size(); k++) {
                    response = (k == 0) ? "" + array.get(k) : response + ":" + array.get(k);
                }
                break;
            } else {
                array.remove(i);
            }
        }
//        return " " + years + ":" + months + ":" + weeks + ":" + days + ":" + hours + ":" + minuts + ":" + seconds;
        return response;
    }

    public static void animateViewVisibility(final View view, final int visibility) {
        // cancel runnning animations and remove and listeners
        view.animate().cancel();
        view.animate().setListener(null);

        // animate making view visible
        if (visibility == View.VISIBLE) {
            view.animate().alpha(1f).start();
            view.setVisibility(View.VISIBLE);
        }
        // animate making view hidden (HIDDEN or INVISIBLE)
        else {
            view.animate().setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(visibility);
                }
            }).alpha(0f).start();
        }
    }

    public static int getSelectedStep(ArrayList<UnitStep> array, Long saleQty) {
        int step = -1;

        saleQty += 1;
        for (int i = 0; i < array.size(); i++) {

            long startUnit = Long.parseLong(array.get(i).getFromUnitStep());
            long endUnit = Long.parseLong(array.get(i).getToUnit());

            if (startUnit <= saleQty && endUnit >= saleQty) {

                step = i;

            }
        }
        return step;
    }

//    public boolean connectiondetect() {
//
//        if (isConnectingToInternet()) {
//            return true;
//        } else {
//            final Dialog dialog = new Dialog(context);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.dialog_connectionfailed);
//            Button ok = (Button) dialog.findViewById(R.id.dialog_ok);
//            Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
//            dialog.show();
//            dialog.setCancelable(false);
//
//            ok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                    connectiondetect();
//                }
//            });
//            cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                    context.startActivity(new Intent(Settings.ACTION_SETTINGS));
//
//                }
//            });
//            return false;
//        }
//
//    }

    public static Double getFinalPrice(Double price, int qty, int balance) {
        Double result = 0d;
        if (qty != 0) {
            Double totlaPrice = price * qty;
            result = totlaPrice - balance;
        }
        return result;
    }

    public static String getOrderStatus(String status) {
        String orderStatus = "";
        switch (status) {
            case "wc-pending":
                orderStatus = "Pending";
                break;
            case "wc-processing":
                orderStatus = "Processing";
                break;
            case "wc-on-hold":
                orderStatus = "On Hold";
                break;
            case "wc-completed":
                orderStatus = "Completed";
                break;
            case "wc-cancelled":
                orderStatus = "Cancelled";
                break;
            case "wc-refunded":
                orderStatus = "Refunded";
                break;
            case "wc-failed":
                orderStatus = "Failed";
                break;
            case "Waiting to shipping":
                orderStatus = "Waiting to shipping";
                break;
            case "Shipping":
                orderStatus = "Shipping";
                break;
        }

        return orderStatus;
    }

    public static String getOrderIsCompleted(String status) {
        String orderStatus = "";
        switch (status) {
            case "wc-pending":
                orderStatus = "Not Complete";
                break;
            case "wc-processing":
                orderStatus = "Not Complete";
                break;
            case "wc-on-hold":
                orderStatus = "Not Complete";
                break;
            case "wc-completed":
                orderStatus = "Completed";
                break;
            case "wc-cancelled":
                orderStatus = "Not Complete";
                break;
            case "wc-refunded":
                orderStatus = "Not Complete";
                break;
            case "wc-failed":
                orderStatus = "Not Complete";
                break;
        }

        return orderStatus;
    }

    public void setSharedPrefrenceIsFirstTime(String value) {
        sp.edit().putString(Constant.ISFIRSTTIME, value).commit();
    }

    public String getReadSharedPrefrenceIsFirstTime() {
        String defValue = "";
        String resposne = sp.getString(Constant.ISFIRSTTIME, defValue);
        return resposne;
    }

    public void clearSharedPreferenceData() {
        sp.edit().clear().commit();

    }

    public String getResponseofPost(String URL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getResponseofGet(String URL) {
        URL url;
        String response = "";
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.d("URL - ResponseCode", URL + " - " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws
            UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public String MakeServiceCall(String URLSTR) {
        StringBuilder response = null;
        try {
            response = new StringBuilder();
            URL url = new URL(URLSTR);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                String strLine = null;
                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }
                input.close();
            }
        } catch (Exception e) {

        }
        return response.toString();

    }

    private boolean appInstalledOrNot(String packageName) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            Log.d("Network",
                                    "NETWORKNAME: " + anInfo.getTypeName());
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getSelectedStepForPaypal(ProductStoreSaleDetail productFullDetails, long saleQty) {
        int step = 0;
        ArrayList<UnitStep> array = productFullDetails.getUnitStep();
        saleQty += 1;
        for (int i = 0; i < array.size(); i++) {

            long startUnit = Long.parseLong(array.get(i).getFromUnitStep());
            long endUnit = Long.parseLong(array.get(i).getToUnit());

            if (startUnit <= saleQty && endUnit >= saleQty) {

                step = i;

            }
        }
        return step;

    }


}

