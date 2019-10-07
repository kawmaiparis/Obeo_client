package com.example.obeo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Map;

import ClientServer.ClientRequests.ObeoClient;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
            String username = sharedPreferences.getString("username","DEFAULT");
            String password = sharedPreferences.getString("password","DEFAULT");
            if (ObeoClient.getInstance().createLogin(username, password) == 0){
                openDashboardScreen();
                return;
            }
        }
        scheduleSplashScreen();

    }

    private void scheduleSplashScreen() {
        final int splashScreenDuration = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openHomeScreen();
                finish();

            }
        }, splashScreenDuration);
    }

    private void openHomeScreen() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
    private void openDashboardScreen() {
        final int splashScreenDuration = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, DashboardScreen.class);
                startActivity(intent);
                finish();

            }
        }, splashScreenDuration);

    }
}
