package com.example.obeo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GuideScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_screen);

        TextView guideText = findViewById(R.id.guideText);

        String text = "--- GUIDE ---\n\n" +
                "\u2022  Call or video call to check your guide's language level is similar to yours\n\n\n " +
                "--- SAFETY ---\n\n" +
                "\u2022  Ensure that someone knows where you are and what you were planning to do\n" +
                "\u2022  Always in public with shops and people nearby\n" +
                "\u2022  Report button there if an interaction is inappropriate\n" +
                "\u2022  Make sure you have the emergency services’ number\n\n" +
                "\u2022  Enjoy :)"


                ;

        guideText.setText(text);





    }
}
