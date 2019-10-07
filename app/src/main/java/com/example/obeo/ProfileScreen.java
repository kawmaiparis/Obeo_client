package com.example.obeo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

import ClientServer.ClientRequests.ObeoClient;
import interests.UserInterest;
import languages.UserLanguage;
import matching_users.MatchedObeoUser;
import user.ObeoUser;

public class ProfileScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_screen);

        String username = getIntent().getStringExtra("USERNAME");
        final ObeoUser user = ObeoClient.getInstance().getUserFromUsername(username);


        TextView profileNameView = findViewById(R.id.profile_name);
//        TextView profileAgeView = findViewById(R.id.profile_age);
        TextView profileOccupationView = findViewById(R.id.profile_occupation);
        TextView profileBioView = findViewById(R.id.profile_bio);
        ImageView imageView = findViewById(R.id.imageView);

        Button sayHiButton = findViewById(R.id.sayHi);
        sayHiButton.setOnClickListener((v -> sayHi(user.getUser_name(), user.getId())));

        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener((v -> report(user.getUser_name(), user.getId())));

        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(user.getProfile_bits());
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        imageView.setImageBitmap(bitmap);

        ImageView background_image = findViewById(R.id.background_image);
        String home_city = user.getHome_city();
        if (HolidaysRVAdapter.portraitMap.containsKey(home_city)) {
//            holidayViewHolder.location_picture.setImageAlpha((int) 0.2);
            background_image.setBackgroundResource(HolidaysRVAdapter.portraitMap.get(home_city));
        }

        String profileName = user.getFirst_name() + " " + user.getSecond_name();
        String profileAge = Long.toString(user.getAge());
        String profileBio = user.getBio();


        // LANGUAGE
        List<UserLanguage> languages = user.getUserLanguages();
        TextView languages_view = findViewById(R.id.profile_languages);

        if (languages.size() == 0) {
            languages_view.setText("I wish I know how to speak");
        } else {
            StringBuilder language_builder = new StringBuilder();
            for (UserLanguage language : languages) {
                language_builder.append(language.getLanguage());
                language_builder.append(" - ");
                language_builder.append(LanguagesLists.languageLevel.get(language.getLevel()));
                language_builder.append("\n");
            }
            language_builder.setLength(language_builder.length() - 1);
            languages_view.setText(language_builder.toString());
        }

        // INTERESTS
        List<String> userOneInterests = user.getUserInterests().stream().map(UserInterest::getInterest).collect(Collectors.toList());
        List<String> userTwoInterests =  ObeoClient.getInstance().getClientUser().getUserInterests().stream().map(UserInterest::getInterest).collect(Collectors.toList());
        List<String> overLappingInterests = userOneInterests.stream().distinct().filter(userTwoInterests::contains).collect(Collectors.toList());

        TextView interests = findViewById(R.id.profile_interests);
        if (overLappingInterests.size() == 0) {
            interests.setText("I'm not interesting");
        }
        else {
            StringBuilder interest_builder = new StringBuilder();
            for (String interest : overLappingInterests) {
                interest_builder.append(interest);
                interest_builder.append(", ");
            }
            interest_builder.setLength(interest_builder.length() - 2);
            interests.setText(interest_builder.toString());
        }

        profileNameView.setText(profileName);
//        profileAgeView.setText(profileAge);
        profileBioView.setText(profileBio);
        profileOccupationView.setText(profileAge + ", " + home_city);

        TextView gender = findViewById(R.id.genderTextView);
        if (user.isMale()) {
            gender.setText("Male");

        } else {
            gender.setText("Female");

        }
    }

    private void report(String user_name, long user_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Report & block " + user_name)
                .setCancelable(true)
                .setMessage("Choose a reason for reporting this person")
                .setNeutralButton("Spam messages", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ObeoClient.getInstance().reportUsers(user_id);
                        finish();
                    }
                })
                .setPositiveButton("Pretending to be someone", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ObeoClient.getInstance().reportUsers(user_id);
                        finish();
                    }
                })
                .setNegativeButton("Inappropriate Behavior", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ObeoClient.getInstance().reportUsers(user_id);
                        finish();
                    }
                });

        builder.show();
    }

    private void setBackground(String city) {
//        RelativeLayout r = findViewById(R.id.block);
        ImageView r = findViewById(R.id.background_image);
//        if (city.equals("London"))
//        else {
//            r.setBackgroundResource(R.drawable.background_profile_dark);
//        }
//        r.getDrawable().setColorFilter(0xff555555, PorterDuff.Mode.MULTIPLY);
        r.setBackgroundResource(R.drawable.background_london);
    }

    private void sayHi(final String contactName, long userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SEND EM' YOUR FIRST MESSAGE!");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                ObeoClient.getInstance().sendTextMessage(contactName, text);
                ObeoClient.getInstance().matchTwoUsers(userId);
                openMessengerScreen();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void openMessengerScreen() {
        Intent intent = new Intent(this, DashboardScreen.class);
        intent.putExtra("UI", true);
        startActivity(intent);
    }

}