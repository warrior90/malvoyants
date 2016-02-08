package com.talk_to_your_phone.notifications;

import com.project.talk_to_your_phone.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver{
	
    public static boolean wasScreenOn = true;
 
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // DO WHATEVER YOU NEED TO DO HERE
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        	
            wasScreenOn = true;
        }
        else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
        	
        }
    }
 
}