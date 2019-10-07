package com.example.obeo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import ClientServer.ClientRequests.ObeoClient;
import languages.UserLanguage;

public class LanguagesLists extends AppCompatActivity {

    private static String[] spinnerLanguages =
            {"Select Language", "English", "French", "German", "Hindi", "Japanese",
                    "Spanish", "Swedish", "Portuguese", "Russian", "Thai"};
    private static String[] spinnerLanguageLevels = {"Select Skill Level", "1", "2", "3", "4", "5"};

    static final HashMap<Integer, String> languageLevel = new HashMap<Integer, String>() {
        {
            put(1, "literally just started");
            put(2, "novice");
            put(3, "intermediate");
            put(4, "fluent");
            put(5, "native speaker");
        }
    };
    RecyclerView rv;
    List<UserLanguage> userLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages_lists);

        final Spinner languageSpinner = findViewById(R.id.languageSpinner);
        final Spinner languageLevelSpinner = findViewById(R.id.languageLevelSpinner);
        final Button confirmButton = findViewById(R.id.confirmButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerLanguageLevels);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageLevelSpinner.setAdapter(adapter2);

        confirmButton.setOnClickListener(v -> {
            String languageString = languageSpinner.getSelectedItem().toString();
            if (languageString.equals("Select Language")) {
                Toast.makeText(this, "Select Language", Toast.LENGTH_SHORT).show();
                return;
            }
            String languageLevel = languageLevelSpinner.getSelectedItem().toString();
            if (languageLevel.equals("Select Skill Level")) {
                Toast.makeText(this, "Select Skill level", Toast.LENGTH_SHORT).show();
                return;
            }
            ObeoClient.getInstance().createLanguage(languageString,
                    Integer.valueOf(languageLevel));
            userLanguages = ObeoClient.getInstance().getClientUser().getUserLanguages();
            languageSpinner.setSelection(0);
            languageLevelSpinner.setSelection(0);
            initializeAdapter();
        });

        rv = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        userLanguages = ObeoClient.getInstance().getClientUser().getUserLanguages();

        initializeAdapter();

//        for (final UserLanguage u : userLanguages) {
//            Button specificLanguage = new Button(this);
//            specificLanguage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openLanguageScreen(u);
//                }
//            });
//            String buttonString = u.getLanguage() + " - Level: " + u.getLevel();
//            specificLanguage.setText(buttonString);
//            LinearLayout ll = (LinearLayout) findViewById(R.id.languageButtonLayout);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            ll.addView(specificLanguage, lp);
//        }
//
//        Button addButton = findViewById(R.id.addNewLanguage);
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addNewLanguageScreen();
//            }
//        });

    }

    private void initializeAdapter(){
        LanguagesRVAdapter adapter = new LanguagesRVAdapter(userLanguages, this);
        rv.setAdapter(adapter);
    }

    private void openLanguageScreen(UserLanguage userLanguage) {
        Intent intent = new Intent(this, LanguageScreen.class);
        intent.putExtra("USER_LANGUAGE", userLanguage);
        startActivity(intent);

    }

    private void addNewLanguageScreen() {
        Intent intent = new Intent(this, NewLanguageScreen.class);
        startActivity(intent);
    }
}
