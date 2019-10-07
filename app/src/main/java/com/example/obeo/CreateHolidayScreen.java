package com.example.obeo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ClientServer.ClientRequests.ObeoClient;

public class CreateHolidayScreen extends AppCompatActivity {

    static Calendar startDate;
    static Calendar endDate;
    static Boolean startSet;
    static Boolean endSet;
    static Address address;
    static long startTime;

    public static void setAddress (Address newAddress) {
        address = newAddress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_holiday_screen);

        String caller = getIntent().getStringExtra("caller");

        final Calendar calendarStart = Calendar.getInstance();
        final Calendar calendarEnd = Calendar.getInstance();
        final EditText startDateEditText = findViewById(R.id.startDateEditText);
        final EditText endDateEditText = findViewById(R.id.endDateEditText);
        final EditText locationEditText = findViewById(R.id.locationEditText);
        final Button addHolidayButton = findViewById(R.id.addHolidayButton);

        if (caller.equals("SelectLocationScreen")) {
            if (startSet) {
                updateLabel(startDateEditText, startDate);
            }
            if (endSet) {
                updateLabel(endDateEditText, endDate);
            }
            String city = null;
            String locality = address.getLocality();
            String subAdmin = address.getSubAdminArea();
            String admin = address.getAdminArea();
            if (locality != null) {
                city = locality;
            } else if (subAdmin != null) {
                city = subAdmin;
            } else if (admin != null) {
                city = admin;
            }

            if (city.equals("東京都")) {
                city = "Tokyo";
            } else if (city.equals("กรุงเทพมหานคร")) {
                city = "Bangkok";
            } else if (city.equals("Москва")) {
                city = "Moscow";
            }

            locationEditText.setText(city);
        } else {
            startSet = false;
            endSet = false;
            startTime = SystemClock.uptimeMillis();
        }

        final DatePickerDialog.OnDateSetListener datePicker1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, monthOfYear);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(startDateEditText, calendarStart);
                startDate = calendarStart;
                startSet = true;
            }

        };

        final DatePickerDialog.OnDateSetListener datePicker2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, monthOfYear);
                calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(endDateEditText, calendarEnd);
                endDate = calendarEnd;
                endSet = true;
            }

        };

        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateHolidayScreen.this, datePicker1, calendarStart
                        .get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateHolidayScreen.this, datePicker2, calendarEnd
                        .get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectLocationScreen();
            }
        });

        addHolidayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long endTime = SystemClock.uptimeMillis();
                long elapsedTime = endTime - startTime;
                String locality = address.getLocality();
                String subAdmin = address.getSubAdminArea();
                String admin = address.getAdminArea();
                String city;
                if (locality != null) {
                    city = locality;
                } else if (subAdmin != null) {
                    city = subAdmin;
                } else if (admin != null) {
                    city = admin;
                } else {
                    Toast.makeText(CreateHolidayScreen.this,
                            "Address not supported", Toast.LENGTH_SHORT).show();
                    return;
                }
                ObeoClient client = ObeoClient.getInstance();
                client.createHoliday(startDate.getTime(), endDate.getTime(), city,
                        address.getLongitude(), address.getLatitude(), elapsedTime);

                openDashboardScreen();
//                Toast.makeText(CreateHolidayScreen.this, startDate.getTime().toString() + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openSelectLocationScreen() {
        Intent intent = new Intent(this, SelectLocationScreen.class);
        intent.putExtra("caller", "CreateHolidayScreen");
        startActivity(intent);
    }

    private void openDashboardScreen() {
        Intent intent = new Intent(this, DashboardScreen.class);
        startActivity(intent);
    }

    private void updateLabel(EditText editText, Calendar calendar) {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        editText.setText(sdf.format(calendar.getTime()));
    }
}