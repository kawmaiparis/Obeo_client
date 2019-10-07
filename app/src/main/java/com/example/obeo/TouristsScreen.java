package com.example.obeo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ClientServer.ClientRequests.ObeoClient;
import matching_users.MatchedObeoUser;

public class TouristsScreen extends AppCompatActivity {

    private List<MatchedObeoUser> tourists;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourists_screen);
        rv = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        System.out.println("Getting tourists");

        String destination_city = ObeoClient.getInstance().getClientUser().getHome_city();
        if (HolidaysRVAdapter.portraitMap.containsKey(destination_city)) {
//            holidayViewHolder.location_picture.setImageAlpha((int) 0.2);
            ImageView location_picture = findViewById(R.id.location_photo);
            location_picture.setBackgroundResource(HolidaysRVAdapter.portraitMap.get(destination_city));
        }
        tourists = ObeoClient.getInstance().lookingforTourist();
        initializeAdapter();

//        for (final MatchedObeoUser m : tourists) {
//            Button myButton = new Button(this);
//            myButton.setText(m.getTourist().getFirst_name());
//
//            myButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openLocalProfileScreen(m);
//                }
//            });
//
//
//            LinearLayout ll = (LinearLayout) findViewById(R.id.buttonlayout);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            ll.addView(myButton, lp);
//        }

    }

//    private void openLocalProfileScreen(MatchedObeoUser matchedObeoUser) {
//        // opens up local profile screen -> i.e. link to ProfileScreen
//        Intent intent = new Intent(this, ProfileScreen.class );
//
//        intent.putExtra("PROFILE", matchedObeoUser);
//        intent.putExtra("IS_LOCAL", false);
//        startActivity(intent);
//    }

    private void initializeAdapter(){
        LocalsTouristsRVAdapter adapter = new LocalsTouristsRVAdapter(tourists, false, this);
        rv.setAdapter(adapter);
    }

}
