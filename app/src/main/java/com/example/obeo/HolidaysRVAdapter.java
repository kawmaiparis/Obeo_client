package com.example.obeo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Dash;

import java.util.HashMap;
import java.util.List;

import ClientServer.ClientRequests.ObeoClient;
import holidays_table.ObeoHoliday;

class HolidaysRVAdapter extends RecyclerView.Adapter<HolidaysRVAdapter.HolidayViewHolder> {

    private List<ObeoHoliday> holidays;
    private Context context;

    static final HashMap<String, Integer> landscapeMap = new HashMap<String, Integer>() {
        {
            put("Greater London", R.drawable.location_london_landscape);
            put("Agra", R.drawable.location_india_landscape);
            put("Paris", R.drawable.location_paris_landscape);
            put("Tokyo", R.drawable.location_tokyo_landscape);
            put("Honolulu", R.drawable.location_hawaii_landscape);
            put("Moscow", R.drawable.location_kremlin_landscape);
            put("Bangkok", R.drawable.location_bangkok_landscape);
        }
    };

    static final HashMap<String, Integer> portraitMap = new HashMap<String, Integer>() {
        {
            put("Greater London", R.drawable.background_london);
            put("Agra", R.drawable.location_india_portrait);
            put("Paris", R.drawable.location_paris_portrait);
            put("Tokyo", R.drawable.location_tokyo_portrait);
            put("Honolulu", R.drawable.location_hawaii_portrait);
            put("Moscow", R.drawable.location_kremlin_portrait);
            put("Bangkok", R.drawable.location_bangkok_portrait);

        }
    };



    public static class HolidayViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView location;
        TextView startDate;
        TextView endDate;
        ImageView location_picture;
        ObeoHoliday holiday;
        Button deleteView;

        HolidayViewHolder(View itemView, final Context context) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            location = itemView.findViewById(R.id.holidayLocationTextView);
            startDate = itemView.findViewById(R.id.startDateTextView);
            endDate = itemView.findViewById(R.id.endDateTextView);
            location_picture = itemView.findViewById(R.id.location_photo);
            deleteView = itemView.findViewById(R.id.delete_holiday);

            deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Holiday")
                            .setMessage("Are you sure you want to delete holiday?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                ObeoClient.getInstance().deleteHoliday(holiday);
                                Intent intent = new Intent(context, DashboardScreen.class);
                                context.startActivity(intent);


                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            cv.setOnClickListener(v -> {
                Intent intent = new Intent(context, DisplayLocalsScreen.class);
                intent.putExtra("HOLIDAY", holiday);
                context.startActivity(intent);
            });
        }

        public void setHoliday(ObeoHoliday holiday) {
            this.holiday = holiday;
        }
    }


    HolidaysRVAdapter(List<ObeoHoliday> holidays, Context context){
        this.holidays = holidays;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public HolidayViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holiday,
                viewGroup, false);

        return new HolidayViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(HolidayViewHolder holidayViewHolder, int i) {

        holidayViewHolder.setHoliday(holidays.get(i));

        holidayViewHolder.location.setText(holidays.get(i).getDestination_city());
        holidayViewHolder.startDate.setText(String.valueOf(holidays.get(i).getStart_date()));
        holidayViewHolder.endDate.setText("-> " + String.valueOf(holidays.get(i).getEnd_date()));

        String destination_city = holidays.get(i).getDestination_city();
        if (landscapeMap.containsKey(destination_city)) {
//            holidayViewHolder.location_picture.setImageAlpha((int) 0.2);
            holidayViewHolder.location_picture.setBackgroundResource(landscapeMap.get(destination_city));
        }

    }

    @Override
    public int getItemCount() {
        return holidays.size();
    }
}
