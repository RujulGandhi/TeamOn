package com.app.archirayan.teamon.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;
import com.app.archirayan.teamon.Widget.AsteriskPasswordTransformationMethod;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.app.archirayan.teamon.Utils.Constant.DEVICETYPE;

public class LoginActivity extends AppCompatActivity {


    public Utils utils;
    EditText username, password;
    TextView logintxt;
    ImageView unamcorrect, pwdcorrect, unamecross, pwdcross;
    ProgressDialog prgDialog;
    private Context context;
    private String uname, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.act_login_edituname);
        password = (EditText) findViewById(R.id.act_login_editpwd);
        password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        logintxt = (TextView) findViewById(R.id.activity_login_txtlogin);
        utils = new Utils(LoginActivity.this);

        logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Uname(username.getText().toString()) || isValidEmail(username.getText().toString())) {
                    if (!password.getText().toString().equalsIgnoreCase("")) {
                        uname = username.getText().toString();
                        pwd = password.getText().toString();
                        new Login(uname, pwd).execute();
                    } else {
                        password.setError("Password not Entered");
                        password.requestFocus();
                    }
                } else {
                    username.setError("Email not Valid");
                    username.requestFocus();
                }
            }

        });
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean Uname(String urname) {
        String re = "\\S+\\S+";
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(urname);
        return matcher.matches();
    }

    private class Login extends AsyncTask<String, String, String> {
        public String name, password;
        public ProgressDialog pd;

        public Login(String strName, String strPwd) {
            this.name = strName;
            this.password = strPwd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username_email", name);
            hashMap.put("password", password);
            hashMap.put("deviceType", DEVICETYPE);
            hashMap.put("device_id_android", FirebaseInstanceId.getInstance().getToken());
            return utils.getResponseofPost(Constant.BASE_URL + "login.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    JSONObject userObject = jsonObject.getJSONObject("data");
                    Utils.WriteSharePrefrence(LoginActivity.this, Constant.USERID, userObject.getString("ID"));

                    Intent i = new Intent(LoginActivity.this, LoadProductActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

}
