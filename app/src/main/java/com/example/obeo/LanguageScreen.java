package com.example.obeo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import ClientServer.ClientRequests.ObeoClient;
import languages.UserLanguage;

public class LanguageScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_screen);
        final UserLanguage userLanguage = (UserLanguage) getIntent().getSerializableExtra("USER_LANGUAGE");
        int i = userLanguage.getLevel();
        String s = Integer.toString(i);

        Button cancelButton = findViewById(R.id.cancelLangUpdate);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToLanguage();
            }
        });

        TextView languageNameView = findViewById(R.id.nameOfLanguageView);
        languageNameView.setText(userLanguage.getLanguage());

        final EditText languageLevel = findViewById(R.id.levelNumberEditText);
        languageLevel.setText(s);

        Button confirmButton = findViewById(R.id.confirmUpdateButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObeoClient.getInstance().updateLanguageLevel(userLanguage, Integer.parseInt(languageLevel.getText().toString()));
                returnToLanguage();
            }
        });

        final Button deleteLangButton = findViewById(R.id.deleteLanguage);
        deleteLangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLangPopUp(userLanguage);
            }
        });



    }
    private void returnToLanguage() {
        Intent intent = new Intent(this, LanguagesLists.class);

        startActivity(intent);
    }

    private void deleteLangPopUp(final UserLanguage userLanguage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Changes")
                .setMessage("Are you sure you want to delete language?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ObeoClient.getInstance().deleteLanguage(userLanguage);
                        returnToLanguage();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }






}
