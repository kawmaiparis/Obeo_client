package com.example.obeo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.*;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ClientServer.ClientRequests.ObeoClient;
import interests.UserInterest;

public class InterestsList extends AppCompatActivity {

    ExpandableListView expandableListView;

    List<String> categories;
    Map<String, List<String>> specifics;
    ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests_list);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        populateELV();

        listAdapter = new ELAdapter(this, categories, specifics);
        expandableListView.setAdapter(listAdapter);

    }

    public void populateELV() {

        categories = new ArrayList<>();
        specifics = new LinkedHashMap<>();

        categories.add("Sports");
        categories.add("Music");
        categories.add("Films and TV");
        categories.add("Literature");
        categories.add("Dancing");
        categories.add("Video Games");
        categories.add("Food");

        List<String> sports = new ArrayList<>();
        List<String> music = new ArrayList<>();
        List<String> fAndTV = new ArrayList<>();
//        List<String> food = new ArrayList<>();
        List<String> literature = new ArrayList<>();
        List<String> dancing = new ArrayList<>();
        List<String> videoGames = new ArrayList<>();
        List<String> food = new ArrayList<>();



        sports.add("Football");
        sports.add("Rugby");
        sports.add("Squash");
        sports.add("Badminton");
        sports.add("Basketball");
        sports.add("Tennis");
        sports.add("Cricket");
        sports.add("Netball");
        sports.add("Volleyball");


        music.add("Hip-hop");
        music.add("Jazz");
        music.add("Pop");
        music.add("Grime");
        music.add("Rap");
        music.add("Classical");
        music.add("Disco");


        fAndTV.add("Horror");
        fAndTV.add("Action");
        fAndTV.add("Mystery");
        fAndTV.add("Drama");
        fAndTV.add("Romance");
        fAndTV.add("Comedy");
        fAndTV.add("Thriller");
        fAndTV.add("Sci-Fi");


        literature.add("Poetry");
        literature.add("Non-Fiction");
        literature.add("Fantasy");
        literature.add("Murder-Mystery");
        literature.add("Manga");
        literature.add("Comics");
        literature.add("Romance");
        literature.add("Sci-Fi Novels");

        dancing.add("Salsa");
        dancing.add("Contemporary");
        dancing.add("Street Dance");
        dancing.add("Ballet");
        dancing.add("Ballroom");
        dancing.add("Break Dance");

        videoGames.add("Action-Adventure");
        videoGames.add("Racing");
        videoGames.add("Puzzle");
        videoGames.add("Role-Playing Games");
        videoGames.add("First Person Shooter");
        videoGames.add("Massively Multiplayer Online");
        videoGames.add("Arcade");

        food.add("Indian");
        food.add("Japanese");
        food.add("Chinese");
        food.add("French");
        food.add("American");
        food.add("Spanish");
        food.add("Thai");
        food.add("Korean");




        specifics.put(categories.get(0), sports);
        specifics.put(categories.get(1), music);
        specifics.put(categories.get(2), fAndTV);
        specifics.put(categories.get(3), literature);
        specifics.put(categories.get(4), dancing);
        specifics.put(categories.get(5), videoGames);
        specifics.put(categories.get(6), food);


    }



}

