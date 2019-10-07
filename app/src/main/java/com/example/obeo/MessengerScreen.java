package com.example.obeo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mesibo.api.Mesibo;
import com.mesibo.mediapicker.MediaPicker;
import com.mesibo.messaging.MesiboUI;

public class MessengerScreen extends AppCompatActivity implements Mesibo.MessageListener,
        Mesibo.ConnectionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_screen);


        MesiboUI.Config opt = MesiboUI.getConfig();
        opt.mToolbarColor = 0xfff0868b;
        MediaPicker.setToolbarColor(opt.mToolbarColor);


        MesiboUI.launch(MessengerScreen.this, 0, false, false);
    }

    @Override
    public void Mesibo_onConnectionStatus(int i) {
        Log.d("FragmentActivity", "on Mesibo Connection: " + i);

    }

    @Override
    public boolean Mesibo_onMessage(Mesibo.MessageParams messageParams, byte[] bytes) {
        System.out.println("On messge");
        return true;
    }

    @Override
    public void Mesibo_onMessageStatus(Mesibo.MessageParams messageParams) {
        System.out.println("On message status");

    }

    @Override
    public void Mesibo_onActivity(Mesibo.MessageParams messageParams, int i) {
        System.out.println("On messge activity");

    }

    @Override
    public void Mesibo_onLocation(Mesibo.MessageParams messageParams, Mesibo.Location location) {

    }

    @Override
    public void Mesibo_onFile(Mesibo.MessageParams messageParams, Mesibo.FileInfo fileInfo) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashboardScreen.class);
        startActivity(intent);
    }
}