package com.talk_to_your_phone.notifications;

import com.project.talk_to_your_phone.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AppelReceiver extends BroadcastReceiver{

	@Override 
	public void onReceive(final Context context, final Intent intent) {
	  
	    Bundle extras = intent.getExtras();
	    String phoneNumber = null;
	    if (extras != null) {

	        String state = extras.getString(TelephonyManager.EXTRA_STATE);
	        

	        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){ 
	        	phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
	        	SystemClock.sleep(500 * 1);
	        	 
	        	Intent i = new Intent(context,Appels_notif.class);
	        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	        	i.putExtra("source", "CallReceiver");
	        	i.putExtra("tel", phoneNumber);
	        	context.startActivity(i);
	        }
	       
	    }
	}
}
