package com.app.archirayan.teamon.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.R;
import com.app.archirayan.teamon.Utils.Constant;
import com.app.archirayan.teamon.Utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class LetsGoActivity extends AppCompatActivity {

    public TextView txtLetsgo;
    public EditText editFullname, editCountry, editAddress, editZipcode, editPhone;
    public Utils utils;
    public String id, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lets_go);

        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getString("id");
            email = getIntent().getExtras().getString("email");
        }

        editFullname = (EditText) findViewById(R.id.activity_lets_go_editfullname);
        editCountry = (EditText) findViewById(R.id.activity_lets_go_editcountry);
        editAddress = (EditText) findViewById(R.id.activity_lets_go_editaddress);
        editZipcode = (EditText) findViewById(R.id.activity_lets_go_editzipcode);
        editPhone = (EditText) findViewById(R.id.activity_lets_go_editpono);
        txtLetsgo = (TextView) findViewById(R.id.activity_lets_go_editletsgo);

        utils = new Utils(LetsGoActivity.this);

        txtLetsgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String full_name = editFullname.getText().toString().trim();
                String country = editCountry.getText().toString().trim();
                String address = editAddress.getText().toString().trim();
                String zip_code = editZipcode.getText().toString().trim();
                String phone_number = editPhone.getText().toString().trim();

                new LetsGo(full_name, country, address, zip_code, phone_number).execute();

            }

        });
    }

    private class LetsGo extends AsyncTask<String, String, String> {
        public String full_name, country, address, zip_code, phone_number;
        ProgressDialog pd;

        public LetsGo(String strfull_name, String strcountry, String straddress, String strzip_code, String strphone_number) {
            this.full_name = strfull_name;
            this.country = strcountry;
            this.address = straddress;
            this.zip_code = strzip_code;
            this.phone_number = strphone_number;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LetsGoActivity.this);
            pd.setMessage("please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return utils.getResponseofGet(Constant.BASE_URL + "add_edit_shipping_billing_address.php?user_id=" + id + "&full_name=" + full_name + "&country=" + country
                    + "&address=" + address + "&zip_code=" + zip_code + "&phone_number=" + phone_number + "&email=" + email);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(LetsGoActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
//                    Utils.WriteSharePrefrence(LetsGoActivity.this, Constant.UserId, jsonObject.getString("ID"));
                    Intent i = new Intent(LetsGoActivity.this, ShareActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LetsGoActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }

    }
}
