package com.talk_to_your_phone.notifications;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

import com.project.talk_to_your_phone.Main;


public class SMSReceiver extends BroadcastReceiver{

	 @Override
	    public void onReceive(Context context, Intent intent) 
	    {
	        //---get the SMS message passed in---
	        Bundle bundle = intent.getExtras();        
	        SmsMessage[] msgs = null;
	        String str = "";  
	        String de="";
	        if (bundle != null)
	        {
	            //---retrieve the SMS message received---
	            Object[] pdus = (Object[]) bundle.get("pdus");
	            msgs = new SmsMessage[pdus.length];
	            
	            for (int i=0; i<msgs.length; i++){
	                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
	                de = msgs[i].getOriginatingAddress();
	                str += msgs[i].getMessageBody().toString();
	                str += "\n";        
	            }
	            //---display the new SMS message---
	           
	            Intent i = new Intent(context, SMS_notif.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra("source", "smsReceiver");
                i.putExtra("incomingsms", str);
                i.putExtra("num",de );
                context.startActivity(i);
	        }                         
	    }

}
