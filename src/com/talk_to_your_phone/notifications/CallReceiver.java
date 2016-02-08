package com.talk_to_your_phone.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.sax.StartElementListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver{

	@Override 
	public void onReceive(final Context context, final Intent intent) {
	  
	    Intent i = new Intent(context,Appels_notif.class);
	    i.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
	    i.putExtra("source", "appel");
	    context.startActivity(i);
	    
	 
	}
}
