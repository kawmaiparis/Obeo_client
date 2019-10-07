package com.example.obeo;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import matching_users.MatchedObeoUser;

class LocalsTouristsRVAdapter extends RecyclerView.Adapter<LocalsTouristsRVAdapter.UserViewHolder> {

    List<MatchedObeoUser> people;
    boolean isTourist;
    Context context;

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;
        ImageView locationPhoto;
        MatchedObeoUser user;

        UserViewHolder(View itemView, boolean isTourist, Context context,
                       MatchedObeoUser user) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            personName = itemView.findViewById(R.id.person_name);
            personAge = itemView.findViewById(R.id.person_age);
            personPhoto = itemView.findViewById(R.id.person_photo);
            if (!isTourist) {
                locationPhoto = itemView.findViewById(R.id.card_background);
            }
            cv.setOnClickListener(v -> {
                if (isTourist) {



                    Intent intent = new Intent(context, ProfileScreen.class);
                    intent.putExtra("USERNAME", user.getLocal().getUser_name());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ProfileScreen.class);
                    intent.putExtra("USERNAME", user.getTourist().getUser_name());
                    context.startActivity(intent);
                }
            });
        }

        public void setUser(MatchedObeoUser user) {
            this.user = user;
        }
    }

    LocalsTouristsRVAdapter(List<MatchedObeoUser> persons, boolean isTourist, Context context){
        this.people = persons;
        this.isTourist = isTourist;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v;
        if (isTourist){
            v = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.card_person, viewGroup, false);
        } else {
            v = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.card_template, viewGroup, false);
        }

        UserViewHolder pvh = new UserViewHolder(v, isTourist, context, people.get(i));

        return pvh;
    }

    @Override
    public void onBindViewHolder(UserViewHolder personViewHolder, int i) {

        personViewHolder.setUser(people.get(i));
        String isMale;

        if (isTourist) {
            if (people.get(i).getLocal().isMale())
                isMale = "Male";
            else
                isMale = "Female";

            personViewHolder.personName.setText(people.get(i).getLocal().getFirst_name());
            personViewHolder.personAge.setText(isMale + ", " + String.valueOf(people.get(i).getLocal().getAge()));
            personViewHolder.personPhoto.setImageBitmap(BitmapFactory.decodeByteArray(
                    people.get(i).getLocal().getProfile_bits(), 0,
                    people.get(i).getLocal().getProfile_bits().length));
        } else {
            if (people.get(i).getTourist().isMale())
                isMale = "Male";
            else
                isMale = "Female";

            personViewHolder.personName.setText(people.get(i).getTourist().getFirst_name());
            personViewHolder.personAge.setText(isMale + ", " + String.valueOf(people.get(i).getTourist().getAge()));
            personViewHolder.personPhoto.setImageBitmap(BitmapFactory.decodeByteArray(
                    people.get(i).getTourist().getProfile_bits(), 0,
                    people.get(i).getTourist().getProfile_bits().length));

            String home_city = people.get(i).getLocal().getHome_city();
            if (HolidaysRVAdapter.landscapeMap.containsKey(home_city)) {
//            holidayViewHolder.location_picture.setImageAlpha((int) 0.2);
                personViewHolder.locationPhoto.setBackgroundResource(HolidaysRVAdapter.landscapeMap.get(home_city));
            }
        }
    }

    @Override
    public int getItemCount() {

        return people.size();
    }
}
