package com.example.obeo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ClientServer.ClientRequests.ObeoClient;

public class CreateInterestScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_interest_screen);
        final EditText languageNameEdit = findViewById(R.id.nameOfInterestEdit);



        Button confirmButton = findViewById(R.id.confirmUpdateButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObeoClient.getInstance().createInterest(languageNameEdit.getText().toString());
                returnToInterest();
            }
        });

        Button cancelButton = findViewById(R.id.cancelLangUpdate);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToInterest();
            }
        });
    }

    private void returnToInterest() {
        Intent intent = new Intent(this, InterestsList.class);
        startActivity(intent);
    }
}
