package com.example.obeo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ClientServer.ClientRequests.ObeoClient;

public class NewLanguageScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_language_screen);

        final EditText languageNameEdit = findViewById(R.id.nameOfInterestEdit);


        final EditText languageLevelEdit = findViewById(R.id.levelNumberEditText);

        Button confirmButton = findViewById(R.id.confirmUpdateButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int langLevel = Integer.parseInt(languageLevelEdit.getText().toString());
                ObeoClient.getInstance().createLanguage(languageNameEdit.getText().toString(), langLevel);
                returnToLanguage();
            }
        });

        Button cancelButton = findViewById(R.id.cancelLangUpdate);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToLanguage();
            }
        });
    }

    private void returnToLanguage() {
        Intent intent = new Intent(this, LanguagesLists.class);
        startActivity(intent);
    }

}
