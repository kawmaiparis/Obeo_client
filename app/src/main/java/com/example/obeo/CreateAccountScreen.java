package com.example.obeo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ClientServer.ClientRequests.ObeoClient;
import user.ObeoUser;

public class CreateAccountScreen extends AppCompatActivity {

    private String[] spinnerLanguages = {"English", "French", "German", "Hindi", "Japanese",
            "Spanish", "Swedish", "Portuguese", "Russian", "Thai"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_screen);

        final EditText forenameEditText = findViewById(R.id.forenameEditText);
        final EditText surnameEditText = findViewById(R.id.surnameEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        final EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        final EditText emailEditText = findViewById(R.id.emailEditText);
        final EditText dobDayEditText = findViewById(R.id.dobDayEditText);
        final EditText dobMonthEditText = findViewById(R.id.dobMonthEditText);
        final EditText dobYearEditText = findViewById(R.id.dobYearEditText);
        final Button createAccountButton = findViewById(R.id.createAccountButton);
        final RadioGroup radioGroup = findViewById(R.id.genderRadio);
        final Spinner selectLanguageSpinner = findViewById(R.id.selectLanguageSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerLanguages);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        selectLanguageSpinner.setAdapter(adapter);

        createAccountButton.setOnClickListener(v -> {
            String forename = forenameEditText.getText().toString();
            String surname = surnameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPssword = confirmPasswordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String dobDay = dobDayEditText.getText().toString();
            String dobMonth = dobMonthEditText.getText().toString();
            String dobYear = dobYearEditText.getText().toString();
            ObeoClient client = ObeoClient.getInstance();
            String date = dobDay.toString() + dobMonth.toString() + dobYear.toString();
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
            Date dob = null;
            Boolean isMale = radioGroup.getCheckedRadioButtonId() == R.id.male;
            String language = selectLanguageSpinner.getSelectedItem().toString();

            if (!password.equals(confirmPssword)) {
                Toast.makeText(CreateAccountScreen.this,
                        "Confirm password does not match password", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    dob = df.parse(date);
                    client.setupUser(email, forename, surname, new java.sql.Date(dob.getTime()),
                            password, null, false, isMale, "");
                } catch (ParseException e) {
                    Toast.makeText(CreateAccountScreen.this,
                            "Invalid date", Toast.LENGTH_SHORT).show();
                    return;
                }
                openSelectLocationScreen(language);
            }
        });


//        final Calendar calendar = Calendar.getInstance();

//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, monthOfYear);
//                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel(dobEditText, calendar);
//            }
//
//        };
//
//        dobEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(CreateAccountScreen.this, date, calendar
//                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });


    }

    private void openSelectLocationScreen(String language) {
        Intent intent = new Intent(this, SelectLocationScreen.class);
        intent.putExtra("language", language);
        intent.putExtra("caller", "CreateAccountScreen");
        startActivity(intent);
    }

//    private void updateLabel(EditText dobEditText, Calendar calendar) {
//        String myFormat = "dd/MM/yy";
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
//
//        dobEditText.setText(sdf.format(calendar.getTime()));
//    }
}
