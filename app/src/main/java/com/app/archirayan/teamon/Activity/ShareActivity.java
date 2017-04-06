package com.app.archirayan.teamon.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.app.archirayan.teamon.R;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView txtShare, txtNothanks;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if (getIntent().getExtras() != null) {
            user_id = getIntent().getExtras().getString("user_id");
            //email = getIntent().getExtras().getString("email");
        }

        txtShare = (TextView) findViewById(R.id.activity_share_txtshare);
        txtNothanks = (TextView) findViewById(R.id.activity_share_txtnothanks);
        txtShare.setOnClickListener(this);
        txtNothanks.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())

        {
            case R.id.activity_share_txtshare:
                Intent ishare = new Intent(ShareActivity.this, SocialMediaActivity.class);
                ishare.putExtra("user_id", user_id);
                startActivity(ishare);
                break;

            case R.id.activity_share_txtnothanks:
                Intent ino = new Intent(ShareActivity.this, MainActivity.class);
                ino.putExtra("user_id", user_id);
                startActivity(ino);
                break;

        }
    }
}

