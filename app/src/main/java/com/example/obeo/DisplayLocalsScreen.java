package com.example.obeo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ClientServer.ClientRequests.ObeoClient;
import holidays_table.ObeoHoliday;
import matching_users.MatchedObeoUser;

public class DisplayLocalsScreen extends AppCompatActivity {

    private List<MatchedObeoUser> people;
    RecyclerView rv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_locals_screen);
        rv = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        ObeoHoliday holiday = (ObeoHoliday) getIntent().getSerializableExtra("HOLIDAY");

        String destination_city = holiday.getDestination_city();
        TextView header = findViewById(R.id.InterestHeader);
        header.setText(destination_city);

        if (HolidaysRVAdapter.portraitMap.containsKey(destination_city)) {
//            holidayViewHolder.location_picture.setImageAlpha((int) 0.2);
            ImageView location_picture = findViewById(R.id.location_photo);
            location_picture.setBackgroundResource(HolidaysRVAdapter.portraitMap.get(destination_city));
        }

        people = ObeoClient.getInstance().lookingForLocals(holiday);
        initializeAdapter();

    }

    private void initializeAdapter(){
        LocalsTouristsRVAdapter adapter = new LocalsTouristsRVAdapter(people, true, this);
        rv.setAdapter(adapter);
    }

}
