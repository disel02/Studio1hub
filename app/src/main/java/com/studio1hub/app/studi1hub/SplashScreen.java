package com.studio1hub.app.studi1hub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static int SPLASH_TIME_OUT = 2000;
    String loginkey;
    ImageView ivlogo;
    TextView tvline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        ivlogo=(ImageView)findViewById(R.id.ivlogo);
        tvline=(TextView)findViewById(R.id.tvline);

        Animation animation= AnimationUtils.loadAnimation(this,R.anim.splash_transition);
        ivlogo.startAnimation(animation);
        tvline.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                loginkey = prefs.getString("loginkey", "0");

                if (loginkey.equals("0")) {  // login screen
                    Intent i = new Intent(SplashScreen.this, LoginScreen.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i = new Intent(SplashScreen.this, AdminMenu.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
