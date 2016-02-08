package com.talk_to_your_phone.notifications;

import java.lang.reflect.Method;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.speech.tts.TextToSpeech;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.android.internal.telephony.ITelephony;
import com.project.talk_to_your_phone.R;

public class Appels_notif extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener {

	Intent intent;
	String tel;

	private TextView tvnum;
	private GestureDetector gd;
	private TextToSpeech tts;
	private Button btn_answer;
	private Button btn_end;
	private Cursor Contacts;
	private Vibrator vibration;

	private BroadcastReceiver phonestate;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		unlockScreen();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appel_notif);

		
		
		intent = getIntent();
		tel = intent.getStringExtra("tel");
		Contacts =  getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, 
				null, 
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		
		
		tts = new TextToSpeech(this,this);
		tvnum = (TextView)findViewById(R.id.textView1);
		tvnum.setText(Contact(tel));
		btn_answer = (Button)findViewById(R.id.answer);
		btn_answer.setOnClickListener(this);
		btn_end = (Button)findViewById(R.id.end);
		btn_end.setOnClickListener(this);
		btn_answer.setOnLongClickListener(this);
		btn_end.setOnLongClickListener(this);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PHONE_STATE");
		
		phonestate = new BroadcastReceiver() {
		    @Override
		    public void onReceive(Context context, Intent intent) {
		    	Bundle extras = intent.getExtras();
			    String phoneNumber = null;
			    if (extras != null) {

			        String state = extras.getString(TelephonyManager.EXTRA_STATE);
			        

			        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){ 
			        	
			        	finish();
			        }
			       
			    }
			}
		  };

		  registerReceiver(phonestate, filter);
		
		  vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	public String Contact(String tel){
		boolean existe = false;

		while(Contacts.moveToNext() && existe == false){
			String Contact_ID = Contacts.getString(Contacts.getColumnIndex(ContactsContract.Contacts._ID));
			int name = Contacts.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String nom = Contacts.getString(name);

			int hasPhone = Contacts.getInt(Contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
			if (hasPhone == 1) {
				Cursor phoneCursor = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
						null, 
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + Contact_ID, 
						null, 
						null);

				while (phoneCursor.moveToNext()) {
					if(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).contains(tel)){
						existe = true;
						return nom;
					} 
				} 
				phoneCursor.close();
			}

		}

		Contacts.moveToFirst();
		return tel;
	}
	
	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	private void answerPhoneCall(Context context) throws Exception {
        // Set up communication with the telephony service (thanks to Tedd's Droid Tools!)
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        Class c = Class.forName(tm.getClass().getName());
        Method m = c.getDeclaredMethod("getITelephony");
        m.setAccessible(true);
        ITelephony telephonyService;
        telephonyService = (ITelephony)m.invoke(tm);

        // Silence the ringer and answer the call!
        telephonyService.silenceRinger();
        telephonyService.answerRingingCall();
}
	
	

	public void endCall(){
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		Class<TelephonyManager> c = TelephonyManager.class;  
		Method getITelephonyMethod = null;
		if(tm.getCallState() == TelephonyManager.CALL_STATE_RINGING ||
				tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK)
		try {  

			getITelephonyMethod = c.getDeclaredMethod("getITelephony",(Class[]) null);  
			getITelephonyMethod.setAccessible(true);  
			ITelephony iTelephony = (ITelephony) getITelephonyMethod.invoke(tm, (Object[]) null);  
			iTelephony.endCall();
			finish();
		} catch (Exception e) {  
			e.printStackTrace();
		}    
	}
	
	 private void answer(Context context) {
		 Intent headSetUnPluggedintent = new Intent(Intent.ACTION_HEADSET_PLUG);
		 headSetUnPluggedintent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
		 headSetUnPluggedintent.putExtra("state", 1); // 0 = unplugged  1 = Headset with microphone 2 = Headset without microphone
		 headSetUnPluggedintent.putExtra("name", "Headset");
		 // TODO: Should we require a permission?
		 sendOrderedBroadcast(headSetUnPluggedintent, null);
		 
         // Simulate a press of the headset button to pick up the call
         Intent buttonDown = new Intent(Intent.ACTION_MEDIA_BUTTON);             
         buttonDown.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
         context.sendOrderedBroadcast(buttonDown, "android.permission.CALL_PRIVILEGED");
         
         headSetUnPluggedintent.putExtra("state", 0); // 0 = unplugged  1 = Headset with microphone 2 = Headset without microphone
		 headSetUnPluggedintent.putExtra("name", "Headset");
		 // TODO: Should we require a permission?
		 sendOrderedBroadcast(headSetUnPluggedintent, null);

 }
	
	 @Override
	 public void onInit(int status) {
		 if (status == TextToSpeech.SUCCESS) {

			 int result = tts.setLanguage(Locale.FRENCH);

			 if (result == TextToSpeech.LANG_MISSING_DATA
					 || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				 Log.e("TTS", "This Language is not supported");
			 } 

			 String nom = Contact(tel);
			 Contacts.close();
			 speakOut("Appel en cours de la part de "+nom);
			 try {
				 Thread.sleep(1000);
			 } catch (InterruptedException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }


		 } else {
			 Log.e("TTS", "Initilization Failed!");
		 }
	 }


	private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
    }
	
	@Override
	public void onDestroy() {
	
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		unregisterReceiver(phonestate);
		Contacts.close();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.answer:
			speakOut("Répondre");
			break;
			
		case R.id.end:
			speakOut("raccrocher");
			break;
		}
		
	}

	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){
		case R.id.answer:
			vibration.vibrate(100);
			try {
				answer(this);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case R.id.end:
			vibration.vibrate(100);
			endCall();
			finish();
			break;
		}
		return true;
	}

}