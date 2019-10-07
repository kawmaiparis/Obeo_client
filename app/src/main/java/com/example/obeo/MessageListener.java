package com.example.obeo;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.mesibo.api.Mesibo;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageListener implements Mesibo.UIHelperListner, Mesibo.FileTransferListener {

    public MessageListener() {
    }
    private static MessageListener _instance = null;
    public static MessageListener getInstance() {
        if(null==_instance)
            synchronized(MessageListener.class) {
                if(null == _instance) {
                    _instance = new MessageListener();
                }
            }

        return _instance;
    }

    @Override
    public void Mesibo_onForeground(Context context, int i, boolean b) {

    }

    @Override
    public void Mesibo_onShowProfile(Context context, Mesibo.UserProfile userProfile) {


    }

    @Override
    public void Mesibo_onDeleteProfile(Context context, Mesibo.UserProfile userProfile, Handler handler) {

    }

    @Override
    public int Mesibo_onGetMenuResourceId(Context context, int i, Mesibo.MessageParams messageParams, Menu menu) {
        int id = 0;



        HashMap<String, Mesibo.UserProfile> ms = Mesibo.getUserProfiles();

        for (String s: ms.keySet()) {
            System.out.println(ms.get(s).picturePath);
            ms.get(s).picturePath = "/Users/rewajshrestha/Pictures/Wallpapers/kNShvXi.png";
            System.out.println(ms.get(s).picturePath);


        }

        if (i == 0) // Setting menu in userlist
            id = R.menu.messaging_activity_menu;

        else {

            id = R.menu.menu_messaging;

        }

        ((Activity)context).getMenuInflater().inflate(id, menu);
        if(1 == i && null != messageParams && messageParams.groupid > 0) {
            MenuItem menuItem = menu.findItem(R.id.action_call);
            menuItem.setVisible(true);
            MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_NEVER);

            menuItem = menu.findItem(R.id.action_videocall);
            menuItem.setVisible(true);
            MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_NEVER);
        }

        return 0;
    }


    @Override
    public boolean Mesibo_onMenuItemSelected(Context context, int i, Mesibo.MessageParams messageParams, int i1) {
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
    public boolean Mesibo_onFileTransferProgress(Mesibo.FileInfo fileInfo) {
        return true;

    }
}
