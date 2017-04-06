package com.app.archirayan.teamon.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    public TextView txtSignin, txtagreetrems;
    public EditText txtname, txtemail, txtpwd, txtrepwd;
    public ImageView ivTerm, ivTermCorrect, ivNameCorrect, ivNameCross, ivEmailCorrect, ivEmailCross, ivPassworsCorrect, ivPasswordCross, ivRePasswordCorrect, ivRePasswordCross;
    public String strName, strEmail, strPwd, strRepsw;
    public boolean IsTermSelected = false;
    public Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        txtname = (EditText) findViewById(R.id.act_sign_et_sinin);
        txtemail = (EditText) findViewById(R.id.act_sign_et_email);
        txtpwd = (EditText) findViewById(R.id.act_sign_et_password);
        txtrepwd = (EditText) findViewById(R.id.act_sign_et_repassword);
        ivTerm = (ImageView) findViewById(R.id.act_sign_iv_term_conition);
        txtSignin = (TextView) findViewById(R.id.activity_sign_in_txtSignIn);
        ivTermCorrect = (ImageView) findViewById(R.id.act_sign_term_condition_correct);
        ivNameCorrect = (ImageView) findViewById(R.id.act_sign_true_user_name);
        ivNameCross = (ImageView) findViewById(R.id.act_sign_cross_user_name);
        ivEmailCorrect = (ImageView) findViewById(R.id.act_sign_true_email);
        ivEmailCross = (ImageView) findViewById(R.id.act_sign_cross_email);
        ivPassworsCorrect = (ImageView) findViewById(R.id.act_sign_true_pasword);
        ivPasswordCross = (ImageView) findViewById(R.id.act_sign_cross_password);
        ivRePasswordCross = (ImageView) findViewById(R.id.act_sign_cross_repassword);
        ivRePasswordCorrect = (ImageView) findViewById(R.id.act_sign_true_repasword);
        utils = new Utils(SignInActivity.this);
        init();


    }

    private void init() {

        ivTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivTermCorrect.setVisibility(View.VISIBLE);
                IsTermSelected = true;
            }
        });

        ivTermCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivTermCorrect.setVisibility(View.INVISIBLE);
                IsTermSelected = false;
            }
        });


        txtname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 3) {
                    ivNameCorrect.setVisibility(View.VISIBLE);
                    ivNameCross.setVisibility(View.INVISIBLE);
                } else {
                    ivNameCorrect.setVisibility(View.INVISIBLE);
                    ivNameCross.setVisibility(View.VISIBLE);
                }


            }
        });


        txtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    ivEmailCorrect.setVisibility(View.VISIBLE);
                    ivEmailCross.setVisibility(View.INVISIBLE);
                } else {
                    ivEmailCorrect.setVisibility(View.INVISIBLE);
                    ivEmailCross.setVisibility(View.VISIBLE);
                }


            }
        });


        txtpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                strPwd = String.valueOf(s);

                if (strPwd.length() > 3) {
                    ivPasswordCross.setVisibility(View.INVISIBLE);
                    ivPassworsCorrect.setVisibility(View.VISIBLE);
                    if (strPwd.equalsIgnoreCase(strRepsw)) {

                        ivPasswordCross.setVisibility(View.INVISIBLE);
                        ivPassworsCorrect.setVisibility(View.VISIBLE);
                        ivRePasswordCorrect.setVisibility(View.VISIBLE);
                        ivRePasswordCross.setVisibility(View.INVISIBLE);


                    } else {
                        ivPasswordCross.setVisibility(View.VISIBLE);
                        ivPassworsCorrect.setVisibility(View.INVISIBLE);

                        ivRePasswordCorrect.setVisibility(View.INVISIBLE);
                        ivRePasswordCross.setVisibility(View.VISIBLE);

                    }


                } else {
                    ivPasswordCross.setVisibility(View.VISIBLE);
                    ivPassworsCorrect.setVisibility(View.INVISIBLE);
                    ivRePasswordCorrect.setVisibility(View.INVISIBLE);
                    ivRePasswordCross.setVisibility(View.VISIBLE);
                }
            }
        });


        txtrepwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.equals(strPwd)) {
                    ivRePasswordCorrect.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                strRepsw = String.valueOf(s);

                if (strRepsw.length() > 3) {


                    if (strPwd.equalsIgnoreCase(strRepsw)) {
                        ivPasswordCross.setVisibility(View.INVISIBLE);
                        ivPassworsCorrect.setVisibility(View.VISIBLE);
                        ivRePasswordCorrect.setVisibility(View.VISIBLE);
                        ivRePasswordCross.setVisibility(View.INVISIBLE);

                    } else {
                        ivPasswordCross.setVisibility(View.VISIBLE);
                        ivPassworsCorrect.setVisibility(View.INVISIBLE);
                        ivRePasswordCorrect.setVisibility(View.INVISIBLE);
                        ivRePasswordCross.setVisibility(View.VISIBLE);

                    }
                } else {
                    ivPasswordCross.setVisibility(View.VISIBLE);
                    ivPassworsCorrect.setVisibility(View.INVISIBLE);
                    ivRePasswordCorrect.setVisibility(View.INVISIBLE);
                    ivRePasswordCross.setVisibility(View.VISIBLE);

                }
            }
        });


        txtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strName = txtname.getText().toString().trim();
                strEmail = txtemail.getText().toString().trim();
                strPwd = txtpwd.getText().toString().trim();
                strRepsw = txtrepwd.getText().toString().trim();
                if (strName.length() > 3) {
                    ivNameCross.setVisibility(View.INVISIBLE);
                    ivNameCorrect.setVisibility(View.VISIBLE);

                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                        ivEmailCross.setVisibility(View.INVISIBLE);
                        ivEmailCorrect.setVisibility(View.VISIBLE);

                        if (strPwd.length() > 3) {
                            ivPasswordCross.setVisibility(View.INVISIBLE);
                            ivPassworsCorrect.setVisibility(View.VISIBLE);
                            if (strRepsw.length() > 3) {

                                if (strPwd.equals(strRepsw)) {
                                    ivRePasswordCross.setVisibility(View.INVISIBLE);
                                    ivRePasswordCorrect.setVisibility(View.VISIBLE);
                                    if (IsTermSelected) {
                                        new SignIn(strName, strEmail, strPwd).execute();
                                    } else {
                                        Toast.makeText(SignInActivity.this, "please select the terms", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    ivRePasswordCross.setVisibility(View.VISIBLE);
                                    ivRePasswordCorrect.setVisibility(View.INVISIBLE);
                                }

                            } else {
                                ivRePasswordCorrect.setVisibility(View.INVISIBLE);
                                ivRePasswordCross.setVisibility(View.VISIBLE);
                            }


                        } else {
                            ivPassworsCorrect.setVisibility(View.INVISIBLE);
                            ivPasswordCross.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ivEmailCorrect.setVisibility(View.INVISIBLE);
                        ivEmailCross.setVisibility(View.VISIBLE);
                    }
                } else {
                    ivNameCorrect.setVisibility(View.INVISIBLE);
                    ivNameCross.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class SignIn extends AsyncTask<String, String, String> {
        String name, email, password;
        ProgressDialog pd;

        public SignIn(String strName, String strEmail, String strPwd) {
            this.name = strName;
            this.email = strEmail;
            this.password = strPwd;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(SignInActivity.this);
            pd.setMessage("please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://easydatasearch.com/easydata1/auction_wp/api/insert_user.php?username=samir&email=samir@gmail.com&password=samir
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", name);
            hashMap.put("email", strEmail);
            hashMap.put("password", password);
            return utils.getResponseofPost(Constant.BASE_URL + "insert_user.php", hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                Log.e("EROR", "response of the SignIn  :" + s);
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(SignInActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignInActivity.this, FacebookLoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();


        }
    }
}
