package com.example.obeo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mesibo.api.Mesibo;
import com.mesibo.calls.MesiboCall;
import com.mesibo.mediapicker.MediaPicker;
import com.mesibo.messaging.MesiboUI;

import java.util.ArrayList;
import java.util.List;

import ClientServer.ClientRequests.ObeoClient;
import holidays_table.ObeoHoliday;
import matching_users.MatchedObeoUser;

public class DashboardScreen extends AppCompatActivity implements Mesibo.MessageListener, Mesibo.ConnectionListener, Mesibo.UIHelperListner{

    public static final String CHANNEL_1_ID = "channel1";
    private NotificationManagerCompat notificationManager;
    static boolean isSetup = false;
    private RecyclerView rv;
    private List<ObeoHoliday> holidayList;
    private List<MatchedObeoUser> tourists;
    private Boolean isHoliday = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_screen);

        if (getIntent().getBooleanExtra("UI", false)) {


            MesiboUI.Config opt = MesiboUI.getConfig();
            opt.mToolbarColor = 0xfff0868b;
            MediaPicker.setToolbarColor(opt.mToolbarColor);
            MesiboUI.launch(DashboardScreen.this, 0, false, false);

        }

        if (!isSetup) {

            setUpMesibo();
        }

        rv = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);


        holidayList = ObeoClient.getInstance().getClientUser().getHolidayList();

        tourists = ObeoClient.getInstance().lookingforTourist();

        ImageButton touristButton = findViewById(R.id.cancelUpdate);
        Button createHolidayButton = findViewById(R.id.confirmUpdateButton);
        createHolidayButton.setOnClickListener(v -> createHolidayScreen());
        TextView touristTextView = findViewById(R.id.Touristtextview);
        touristTextView.setText("Meet someone coming to " +
                ObeoClient.getInstance().getClientUser().getHome_city());

        final TextView title = findViewById(R.id.InterestHeader);

        ImageButton updateProfileButton = findViewById(R.id.updateProfileButton);
        updateProfileButton.setOnClickListener((v -> updateProfileScreen()));

        initializeHolidayAdapter();

        touristButton.setOnClickListener(v -> {
            if (isHoliday) {
                title.setText("Tourists", TextView.BufferType.EDITABLE);
                createHolidayButton.setVisibility(View.INVISIBLE);
                touristTextView.setVisibility(View.VISIBLE);
                initializeTouristAdapter();
            } else {
                title.setText("Holidays", TextView.BufferType.EDITABLE);
                createHolidayButton.setVisibility(View.VISIBLE);
                touristTextView.setVisibility(View.INVISIBLE);
                initializeHolidayAdapter();
            }
            isHoliday = !isHoliday;
        });

        ImageButton messengerScreen = findViewById(R.id.openMessengerButton);
        messengerScreen.setOnClickListener((v -> openMessenger()));

        Button guideButton = findViewById(R.id.guideButton);
        guideButton.setOnClickListener(v -> openGuideScreen());

        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();


//        for (final ObeoHoliday holiday : holidayList) {
//            Button specificHolidayButton = new Button(this);
//            String buttonText = holiday.getDestination_city() + "\n" + holiday.getStart_date()  + " - " + holiday.getEnd_date();
//            specificHolidayButton.setText(buttonText);
//            specificHolidayButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openHolidayScreen(holiday);
//                }
//            });
//            LinearLayout ll = (LinearLayout) findViewById(R.id.buttonlayout);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            ll.addView(specificHolidayButton, lp);
//
//        }

    }

    private void openGuideScreen() {
        Intent intent = new Intent(this, GuideScreen.class);
        startActivity(intent);
    }

    public void initializeHolidayAdapter(){
        HolidaysRVAdapter adapter = new HolidaysRVAdapter(holidayList, this);
        rv.setAdapter(adapter);
    }

    private void initializeTouristAdapter(){
        LocalsTouristsRVAdapter adapter = new LocalsTouristsRVAdapter(tourists,
                false, this);
        rv.setAdapter(adapter);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
    }

    private void sendOnChannel1(String user, String message) {
        Intent intent = new Intent(this, DashboardScreen.class);
        intent.putExtra("UI", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.obeo_logo_without_text)
                .setContentTitle(user)
                .setContentText(message)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setVibrate(new long[] {1000, 1000, 1000, 1000, 1000})
                .setLights(Color.BLUE, 3000, 3000)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        notificationManager.notify(1, notification);

    }

    private void openHolidayScreen(ObeoHoliday holiday) {
        Intent intent = new Intent(this, DisplayLocalsScreen.class);

//        intent.putExtra("USERS", ObeoClient.getInstance().lookingForLocals(holiday));
        intent.putExtra("HOLIDAY", holiday);
        startActivity(intent);
    }



    private void openTouristScreen() {
        Intent intent  = new Intent(this, TouristsScreen.class);
        startActivity(intent);
    }

    private void createHolidayScreen() {
        Intent intent = new Intent(this, CreateHolidayScreen.class);
        intent.putExtra("caller", "DashboardScreen");
        startActivity(intent);
    }

    private void updateProfileScreen() {
        Intent intent = new Intent(this, UpdateProfileScreen.class);
        startActivity(intent);
    }

    @Override
    public boolean Mesibo_onMessage(Mesibo.MessageParams messageParams, byte[] bytes) {
        sendOnChannel1(messageParams.profile.name, new String(bytes));
        return true;
    }

    @Override
    public void Mesibo_onMessageStatus(Mesibo.MessageParams messageParams) {

    }

    @Override
    public void Mesibo_onActivity(Mesibo.MessageParams messageParams, int i) {

    }

    @Override
    public void Mesibo_onLocation(Mesibo.MessageParams messageParams, Mesibo.Location location) {

    }

    @Override
    public void Mesibo_onFile(Mesibo.MessageParams messageParams, Mesibo.FileInfo fileInfo) {

    }

    private void openMessenger() {
//        Intent intent = new Intent(this, DashboardScreen.class);
//        intent.putExtra("UI", true);
//        startActivity(intent);


        MesiboUI.Config opt = MesiboUI.getConfig();
        opt.mToolbarColor = 0xfff0868b;
        MediaPicker.setToolbarColor(opt.mToolbarColor);

        MesiboUI.launch(DashboardScreen.this, 0, false, false);

    }


    private void setUpMesibo() {
        Mesibo api = Mesibo.getInstance();
        api.init(this);


        MesiboCall mCall = MesiboCall.getInstance();
        mCall.init(this);

        api.addListener(this);



        // set user authentication token obtained by creating user
        api.setAccessToken(ObeoClient.getInstance().getClientUser().getMessenger_token());
        api.setDatabase("mesibo.db", 0);

        api.start();

        isSetup = true;


    }

    @Override
    public void Mesibo_onConnectionStatus(int i) {
        Log.d("FragmentActivity", "on Mesibo Connection: " + i);

    }

    @Override
    public void Mesibo_onForeground(Context context, int i, boolean b) {

    }

    @Override
    public void Mesibo_onShowProfile(Context context, Mesibo.UserProfile userProfile) {


        Intent intent = new Intent(context, ProfileScreen.class);
        intent.putExtra("USERNAME", userProfile.address);
        context.startActivity(intent);


    }

    @Override
    public void Mesibo_onDeleteProfile(Context context, Mesibo.UserProfile userProfile, Handler handler) {

    }

    @Override
    public int Mesibo_onGetMenuResourceId(Context context, int i, Mesibo.MessageParams messageParams, Menu menu) {
        int id = 0;
        if (i == 0) // Setting menu in userlist
            id = R.menu.messaging_activity_menu;
        else // from User chatbox
        {
            System.out.println("Opening chat");
            id = R.menu.menu_messaging;
        }

        ((Activity)context).getMenuInflater().inflate(id, menu);

        if(1 == i && null != messageParams && messageParams.groupid > 0) {
            MenuItem menuItem = menu.findItem(R.id.action_call);
            menuItem.setVisible(false);
            MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_NEVER);

            menuItem = menu.findItem(R.id.action_videocall);
            menuItem.setVisible(false);
            MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_NEVER);
        }

        return 0;
    }

    @Override
    public boolean Mesibo_onMenuItemSelected(Context context, int i, Mesibo.MessageParams messageParams, int i1) {
        if (i != 0) {
            if(R.id.action_call == i1 && 0 == messageParams.groupid) {
//                UIManager.launchCallActivity(MainApplication.getAppContext(), params.peer, true);
                MesiboCall.getInstance().call(context, Mesibo.random(), messageParams.profile, false);
            }
            else if(R.id.action_videocall == i1 && 0 == messageParams.groupid) {
                //UIManager.launchCallActivity(MainApplication.getAppContext(), params.peer, true);
                MesiboCall.getInstance().call(context, Mesibo.random(), messageParams.profile, true);
            }
        }

        return false;
    }

    @Override
    public void Mesibo_onSetGroup(Context context, long l, String s, int i, String s1, String s2, String[] strings, Handler handler) {

    }

    @Override
    public void Mesibo_onGetGroup(Context context, long l, Handler handler) {

    }

    @Override
    public ArrayList<Mesibo.UserProfile> Mesibo_onGetGroupMembers(Context context, long l) {
        return null;
    }

    @Override
    public void onBackPressed() {

    }
}
