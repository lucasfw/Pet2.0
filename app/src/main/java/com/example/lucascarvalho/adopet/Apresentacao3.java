package com.example.lucascarvalho.adopet;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.lucascarvalho.adopet.loginregister.activities.LoginActivity;

public class Apresentacao3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apresentacao3);

        //getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable(){
            public void run(){
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                finish();
            }
        },5000);

    }
}
