package com.example.obeo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mesibo.api.Mesibo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ClientServer.ClientRequests.ObeoClient;
import matching_users.MatchedObeoUser;

public class HomeScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final EditText usernameEditText = findViewById(R.id.usernameEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);

        Button signIn = findViewById(R.id.signInButton);
        Button createAcc = findViewById(R.id.createAccountButton);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(HomeScreen.this,
                            "Enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }
                ObeoClient client = ObeoClient.getInstance();
                int success = client.createLogin(username, password);
                if (success == -1) {
                    Toast.makeText(HomeScreen.this, "Incorrect Username of Password",
                            Toast.LENGTH_SHORT).show();
                } else if (success == -2) {
                    Toast.makeText(HomeScreen.this, "Server Error", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("username", username);
                    editor.putString("password", password);
                    editor.commit();

                    openDashboardScreen();
                }
            }
        });
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreateAccountScreen();
            }
        });
    }

    private void openCreateAccountScreen() {
        Intent intent = new Intent(this, CreateAccountScreen.class);
        startActivity(intent);
    }

    private void openDashboardScreen() {
        Intent intent = new Intent(this, DashboardScreen.class);
        startActivity(intent);
    }
}
