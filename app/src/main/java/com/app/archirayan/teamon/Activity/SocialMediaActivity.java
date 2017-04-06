package com.app.archirayan.teamon.Activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.archirayan.teamon.R;

public class SocialMediaActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView txtNothanks;
    public ImageView imgFacebook, imgTwitter, imgWhatsapp, imgEmail;
    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        if (getIntent().getExtras() != null) {
            user_id = getIntent().getExtras().getString("user_id");
            //email = getIntent().getExtras().getString("email");
        }

        txtNothanks = (TextView) findViewById(R.id.activity_socal_media_txtnothanks);
        imgFacebook = (ImageView) findViewById(R.id.activity_social_media_imgfb);
        imgTwitter = (ImageView) findViewById(R.id.activity_social_media_imgtwit);
        imgWhatsapp = (ImageView) findViewById(R.id.activity_social_media_imgwhtsapp);
        imgEmail = (ImageView) findViewById(R.id.activity_social_media_imgmail);

        txtNothanks.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);
        imgTwitter.setOnClickListener(this);
        imgWhatsapp.setOnClickListener(this);
        imgEmail.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        PackageManager pm = getPackageManager();

        switch (v.getId())

        {
            case R.id.activity_social_media_imgfb:
                try {
                    Intent ifb = new Intent(Intent.ACTION_SEND);
                    PackageInfo info = pm.getPackageInfo("com.facebook.katana", PackageManager.GET_META_DATA);
                    ifb.setPackage("com.facebook.katana");
                    startActivity(Intent.createChooser(ifb, "WELCOME"));
                } catch (PackageManager.NameNotFoundException ex) {
                    Toast.makeText(this, "Facebook have not been installed", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.activity_social_media_imgtwit:
                try {
                    Intent itwit = new Intent(Intent.ACTION_SEND);
                    PackageInfo info = pm.getPackageInfo("com.twitter.android", PackageManager.GET_META_DATA);
                    itwit.setPackage("com.twitter.android");
                    startActivity(Intent.createChooser(itwit, "WELCOME"));
                } catch (PackageManager.NameNotFoundException ex) {
                    Toast.makeText(this, "Twitter have not been installed", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.activity_social_media_imgwhtsapp:
                try {
                    Intent iwtsap = new Intent(Intent.ACTION_SEND);
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    iwtsap.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(iwtsap, "WELCOME"));
                } catch (PackageManager.NameNotFoundException ex) {
                    Toast.makeText(this, "Whatsapp have not been installed", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.activity_social_media_imgmail:
                try {
                    Intent imail = new Intent(Intent.ACTION_SEND);
                    // imail.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
                    PackageInfo info = pm.getPackageInfo("com.google.android.gm", PackageManager.GET_META_DATA);
                    imail.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(imail, "WELCOME"));
                } catch (PackageManager.NameNotFoundException ex) {
                    Toast.makeText(this, "Gmail have not been installed", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.activity_socal_media_txtnothanks:
                Intent iNothank = new Intent(SocialMediaActivity.this, MainActivity.class);
                startActivity(iNothank);
                break;

        }


    }


}


