package com.talk_to_your_phone.horloge;

import com.project.talk_to_your_phone.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			//Toast.makeText(context, "C'est l'heure !!!",Toast.LENGTH_LONG).show();
			
			Intent i2 = new Intent(context,Alarme_notif.class);
        	i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        	context.startActivity(i2);
        
			
		} catch (Exception r) {
			Toast.makeText(context, "Erreur.",Toast.LENGTH_SHORT).show();
			r.printStackTrace();
		}		
	}
}
