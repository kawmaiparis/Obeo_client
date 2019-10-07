package com.example.obeo;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ClientServer.ClientRequests.ObeoClient;

public class UpdateProfileScreen extends AppCompatActivity {

    public static final int REQUEST_CODE = 20;
    public static final int IMAGE_GALLERY_REQUEST = REQUEST_CODE;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_screen);

        profilePicture = findViewById(R.id.profilePicture);

        final EditText forenameEditText = findViewById(R.id.forenameEditText);
        forenameEditText.setText(ObeoClient.getInstance().getClientUser().getFirst_name());

        final EditText surnameEditText = findViewById(R.id.surnameEditText);
        surnameEditText.setText(ObeoClient.getInstance().getClientUser().getSecond_name());

        final RadioGroup genderRadioGroup = findViewById(R.id.selectGender);
        if (genderRadioGroup == null)
            System.out.println("RADIOGROUP IS NULL ---------------------");
        boolean isMale = ObeoClient.getInstance().getClientUser().isMale();
        if (isMale) {
            genderRadioGroup.check(findViewById(R.id.male).getId());
        } else {
            genderRadioGroup.check(findViewById(R.id.female).getId());
        }

        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        final EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ObeoClient.getInstance().getClientUser().getDate_of_birth());
        DecimalFormat formatter = new DecimalFormat("00");

        final EditText dobDayEditText = findViewById(R.id.dobDayEditText);
        String day = formatter.format(calendar.get(Calendar.DAY_OF_MONTH));
        dobDayEditText.setText(day);

        final EditText dobMonthEditText = findViewById(R.id.dobMonthEditText);
        String month = formatter.format(calendar.get(Calendar.MONTH) + 1);
        dobMonthEditText.setText(month);

        final EditText dobYearEditText = findViewById(R.id.dobYearEditText);
        String year = formatter.format(calendar.get(Calendar.YEAR));
        dobYearEditText.setText(year);

        final EditText bioEditText = findViewById(R.id.profile_bioTextEdit);
        bioEditText.setText(ObeoClient.getInstance().getClientUser().getBio());

        final ImageView imageView = findViewById(R.id.profilePicture);
        imageView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGalleryClick(v);
            }
        }));
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(ObeoClient.getInstance().getClientUser().getProfile_bits(), 0,ObeoClient.getInstance().getClientUser().getProfile_bits().length ));
        profilePicture.setTag("");

        final Button logout = findViewById(R.id.logOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.
                        getDefaultSharedPreferences(UpdateProfileScreen.this);
                sharedPreferences.edit().clear().apply();
                openSplashScreen();
            }
        });



        Button setNewLocation = findViewById(R.id.setLocation);
        setNewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openMapsScreen(new setUpListener(forenameEditText, surnameEditText, passwordEditText,
                        confirmPasswordEditText, dobDayEditText, dobMonthEditText,
                        dobYearEditText, bioEditText, profilePicture, genderRadioGroup, UpdateProfileScreen.this));
            }
        });

        Button confirmButton =  findViewById(R.id.confirmUpdateButton);
        confirmButton.setOnClickListener(
                        new setUpListener(forenameEditText, surnameEditText, passwordEditText,
                        confirmPasswordEditText, dobDayEditText, dobMonthEditText,
                        dobYearEditText, bioEditText, profilePicture, genderRadioGroup, UpdateProfileScreen.this));


        Button cancelButton = findViewById(R.id.cancelUpdate);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToDashboard();
            }
        });

        Button interestButton = findViewById(R.id.setInterest);
        interestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInterestScreen();
            }
        });

        Button languageButton = findViewById(R.id.setLanguages);
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLanguageScreen();
            }
        });

        String destination_city = ObeoClient.getInstance().getClientUser().getHome_city();
        if (HolidaysRVAdapter.portraitMap.containsKey(destination_city)) {
            ImageView location_picture = findViewById(R.id.location_photo);
            location_picture.setBackgroundResource(HolidaysRVAdapter.portraitMap.get(destination_city));
        }

    }

    private void openSplashScreen() {
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
    }

    protected void returnToDashboard() {
        Intent intent = new Intent(this, DashboardScreen.class);
        startActivity(intent);
    }

    protected void openSelectLocationScreen() {
        Intent intent = new Intent(this, SelectLocationScreen.class);
        intent.putExtra("caller", "UpdateProfileScreen");
        startActivity(intent);
    }

    private void getInterestScreen() {
        Intent intent = new Intent(this, InterestsList.class);
        startActivity(intent);
    }

    private void getLanguageScreen() {
        Intent intent = new Intent(this, LanguagesLists.class);
        startActivity(intent);
    }

    public void onGalleryClick(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        photoPickerIntent.putExtra("URL", "URL");

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri imageUri = data.getData();

                InputStream inputStream;

                try {
                    inputStream = getContentResolver().openInputStream(imageUri);

                    Bitmap image = BitmapFactory.decodeStream(inputStream);

                    profilePicture.setImageBitmap(image);

                    profilePicture.setTag("bmp");


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();

                }

            }
        }
    }

    private void openMapsScreen(final setUpListener listener) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Changes")
                    .setMessage("Are you sure you want to confirm changes?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, listener)

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

    }


}


 class setUpListener implements View.OnClickListener, DialogInterface.OnClickListener {

     private final RadioGroup genderRadioGroup;
     private EditText forenameEditText;
    private EditText surnameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText dobDayEditText;
    private EditText dobMonthEditText;
    private EditText dobYearEditText;
    private EditText bioEditText;
    private ImageView imageView;
    private UpdateProfileScreen appCompatActivity;

    public setUpListener(EditText forenameEditText, EditText surnameEditText, EditText passwordEditText,
                         EditText confirmPasswordEditText, EditText dobDayEditText, EditText dobMonthEditText,
                         EditText dobYearEditText, EditText bioEditText, ImageView imageView,
                         RadioGroup genderRadioGroup, UpdateProfileScreen appCompatActivity) {
        this.forenameEditText = forenameEditText;
        this.surnameEditText = surnameEditText;
        this.passwordEditText = passwordEditText;
        this.confirmPasswordEditText = confirmPasswordEditText;
        this.dobDayEditText = dobDayEditText;
        this.dobMonthEditText = dobMonthEditText;
        this.dobYearEditText = dobYearEditText;
        this.bioEditText = bioEditText;
        this.imageView = imageView;
        this.genderRadioGroup = genderRadioGroup;
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    public void onClick(View v) { //Listener for confirm button
        String forename = forenameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();

        String password = passwordEditText.getText().toString();
        String confirmPssword = confirmPasswordEditText.getText().toString();

        String dobDay = dobDayEditText.getText().toString();
        String dobMonth = dobMonthEditText.getText().toString();
        String dobYear = dobYearEditText.getText().toString();
        String date = dobDay.toString() + dobMonth.toString() + dobYear.toString();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        java.util.Date dob = null;

        boolean isMale = genderRadioGroup.getCheckedRadioButtonId() == R.id.male;
        String bio = bioEditText.getText().toString();

        ObeoClient client = ObeoClient.getInstance();
        boolean newPassword = !password.matches("");

        String filepath = imageView.getTag().toString();
        Bitmap file;
        if (filepath.equals("")) {
            System.out.println("Null");
            file = null;
        } else{
            System.out.println("Not null with path");
            file = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }



        if (!password.equals(confirmPssword)) {
            Toast.makeText(appCompatActivity,
                    "Confirm password does not match password", Toast.LENGTH_SHORT).show();

        } else {

            try {
                dob = df.parse(date);
                client.updateUser(forename, surname, client.getClientUser().getHome_city(), dob, client.getClientUser().getLongitude(),
                        client.getClientUser().getLatitude(), password, file, isMale, bio, newPassword);
            } catch (ParseException e) {
                Toast.makeText(appCompatActivity,
                        "Invalid date", Toast.LENGTH_SHORT).show();
                return;
            }
            appCompatActivity.returnToDashboard();

        }

    }



    @Override
    public void onClick(DialogInterface dialog, int which) { //Listener for dialog before choosing location
        String forename = forenameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();

        String password = passwordEditText.getText().toString();
        String confirmPssword = confirmPasswordEditText.getText().toString();

        String dobDay = dobDayEditText.getText().toString();
        String dobMonth = dobMonthEditText.getText().toString();
        String dobYear = dobYearEditText.getText().toString();
        String date = dobDay.toString() + dobMonth.toString() + dobYear.toString();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        java.util.Date dob = null;


        String filepath = imageView.getTag().toString();
        Bitmap file;
        if (filepath.equals("")) {
            System.out.println("Null");
            file = null;
        } else{
            System.out.println("Not null with path" + imageView.getTag().toString());
            file = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }

        String bio = bioEditText.getText().toString();

        ObeoClient client = ObeoClient.getInstance();
        boolean newPassword = !password.matches("");

        boolean isMale = genderRadioGroup.getCheckedRadioButtonId() == R.id.male;


        if (!password.equals(confirmPssword)) {
            Toast.makeText(appCompatActivity,
                    "Confirm password does not match password", Toast.LENGTH_SHORT).show();
            dialog.cancel();

        } else {

            try {
                dob = df.parse(date);
                client.updateUser(forename, surname, client.getClientUser().getHome_city(), dob, client.getClientUser().getLongitude(),
                        client.getClientUser().getLatitude(), password, file, isMale, bio, newPassword);
            } catch (ParseException e) {
                Toast.makeText(appCompatActivity,
                        "Invalid date", Toast.LENGTH_SHORT).show();
                return;
            }
            appCompatActivity.openSelectLocationScreen();
        }
    }

}
