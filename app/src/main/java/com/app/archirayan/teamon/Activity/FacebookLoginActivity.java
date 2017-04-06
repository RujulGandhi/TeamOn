package com.app.archirayan.teamon.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.app.archirayan.teamon.Utils.Constant.DEVICETYPE;

//facebook archirayan15@gmail.com archirayan15
//Google analytic


public class FacebookLoginActivity extends AppCompatActivity {

    public TextView txtFblogin, tvSignIn;
    public String FbName, FbEmail, FbId;
    public Utils utils;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView tvSignInfb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_facebook_login);

        tvSignInfb = (TextView) findViewById(R.id.activity_facebook_login_txtsignup);
        tvSignInfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(FacebookLoginActivity.this, LoginActivity.class);
                startActivity(in);
            }
        });
        txtFblogin = (TextView) findViewById(R.id.activity_facebook_login_txtfblogin);
        tvSignIn = (TextView) findViewById(R.id.activity_fb_signing);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        utils = new Utils(FacebookLoginActivity.this);

        init();
    }

    private void init() {
        txtFblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.performClick();

            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
//http://easydatasearch.com/easydata1/auction_wp/api/login_with_facebook.php?email=archirayan15@gmail.com&token_id=1786384274954482&full_name=samir
                                Log.v("LoginActivity Response ", response.toString() + " -- " + object.toString());
                                try {
                                    FbName = object.getString("name");
                                    FbEmail = object.getString("email");
                                    FbId = object.getString("id");

                                    new GetLogin().execute();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(FacebookLoginActivity.this, "Dat " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FacebookLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class GetLogin extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(FacebookLoginActivity.this);
            pd.setMessage("please wait...");
            pd.show();
        }


        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> map = new HashMap();
            map.put("email", FbEmail);
            map.put("token_id", FbId);
            map.put("full_name", FbName);
            map.put("deviceType", DEVICETYPE);
            map.put("device_id_android", FirebaseInstanceId.getInstance().getToken());

            for (Map.Entry<String, String> hashMapObj : map.entrySet()) {
                Log.d("KEY-VALUE", hashMapObj.getKey() + "=" + hashMapObj.getValue());
            }

            return utils.getResponseofPost(Constant.BASE_URL + "login_with_facebook.php", map);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("Response", s);
            JSONObject object = null;
            try {
                object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {

                    JSONObject userObject = object.getJSONObject("data");
                    Utils.WriteSharePrefrence(FacebookLoginActivity.this, Constant.USERID, userObject.getString("user_id"));
                    Utils.WriteSharePrefrence(FacebookLoginActivity.this, Constant.ISFACEBOOK, "1");
                    Intent in = new Intent(FacebookLoginActivity.this, MainActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();

                } else {
                    Toast.makeText(FacebookLoginActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
