package com.app.archirayan.teamon.Fcm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.archirayan.teamon.Activity.LoadProductActivity;
import com.app.archirayan.teamon.Activity.Test;
import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.retrofit.Model.Chat.MessageDetails;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.app.archirayan.teamon.Activity.Test.listMsgDetails;

/**
 * Created by archirayan on 19-Jan-17.
 */
public class MyMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    public String title, message, image;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            String jsonString = remoteMessage.getData().toString().replace("=", ":\"").replace(",", "\",").replace("}", "\"}");
            String jsonString = remoteMessage.getData().toString().substring(6, remoteMessage.getData().toString().length() - 1);
            Log.d("JSONSTring", jsonString);
            try {
                JSONObject mainObject = new JSONObject(jsonString);
                title = mainObject.getString("title");
                message = mainObject.getString("message");
//                image = mainObject.getString("image");
                Log.d("title", title + " == " + message);
                MessageDetails details = new MessageDetails();
                details.setSender("1");
                details.setRecipient("5");
                details.setName("archi");
                details.setSubject("Archirayan");
                details.setText(message);

                listMsgDetails.add(details);
//                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("Error", e.toString());
            }
            sendNotification(remoteMessage.getData().toString());
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody().replace("=", ":"));
            String jsonString = remoteMessage.getNotification().getBody().replace("=", ":");
            Log.d("JSONSTring", jsonString);
            try {
                JSONObject mainObject = new JSONObject(jsonString);
                title = mainObject.getString("title");
                image = mainObject.getString("image");
                message = mainObject.getString("message");
                Log.d("title", title + " == " + message);
            } catch (JSONException e) {
                Log.d("Error", e.toString());
            }
//            new generatePictureStyleNotification("Title", "Message", "http://api.androidhive.info/images/sample.jpg").execute();
            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
//        Bitmap bmp = getBitmapFromURL(image);
        Intent intent = new Intent(this, Test.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
//                .setLargeIcon(bmp)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl;

        public generatePictureStyleNotification(String title, String message, String imageUrl) {
            super();
//            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Intent intent = new Intent(mContext, LoadProductActivity.class);
            intent.putExtra("key", "value");
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notif = new Notification.Builder(mContext)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(result)
                    .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                    .build();
            notif.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(1, notif);
        }

    }
}
