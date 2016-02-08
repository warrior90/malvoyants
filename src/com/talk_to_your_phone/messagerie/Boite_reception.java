package com.talk_to_your_phone.messagerie;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.TremblementDetection;
import com.talk_to_your_phone.outils.TremblementDetection.TremblementListener;

public class Boite_reception extends Activity implements OnGestureListener, TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener {

	private static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
	int compt = 0;
	private TextToSpeech tts;
	private Cursor C;
	private GestureDetector gd;
	private Vibrator vibration;
	private TextView tv0;
	private TextView tv1;
	private TextView tv2;
	private String emetteur;
	private String text;
	private String date;
	private Button btn_quitter;
	int idx;
	Intent intent;
	Date SMSTime;
	long id;
	int pos;
	
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	
	BroadcastReceiver mReceiver;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.boite_reception);
		tts = new TextToSpeech(this,this);

		intent = getIntent();

		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		idx=0;

		tv0 = (TextView)findViewById(R.id.tv_emetteur);
		tv1 = (TextView)findViewById(R.id.TextViewSMS);
		tv2 = (TextView)findViewById(R.id.tv_date);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		btn_quitter.setOnClickListener(this);
		btn_quitter.setOnLongClickListener(this);

		gd = new GestureDetector(this);

		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		C = getContentResolver().query(SMS_INBOX, null, null, null, null);
		

		gd.setOnDoubleTapListener(new OnDoubleTapListener()
		{

			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{


				//tts.setSpeechRate((float) 0.8);
				speakOut("émetteur, "+getEmetteur()+", date d'envoi, "+tv2.getText().toString()+", text de message, "+getText());
				return false;

			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {

				
				return false;
			}});

	}

	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		return gd.onTouchEvent(e);//return the double tap events
	}


	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}





	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		float dX = e2.getX()-e1.getX();

		float dY = e1.getY()-e2.getY();
		int pos=0;
		if(C.getCount() == 0){
			speakOut("pas de message");
			return true;
		}
		
		if (Math.abs(dY)<SWIPE_MAX_OFF_PATH &&

				Math.abs(velocityX)>=SWIPE_THRESHOLD_VELOCITY &&

				Math.abs(dX)>=SWIPE_MIN_DISTANCE ) {

			if (dX>0) {

				//right swipe
				
				
				if(C.moveToNext()){

					setEmetteur(C.getString(C.getColumnIndexOrThrow("address")));
					setText(C.getString(C.getColumnIndexOrThrow("body")));
					setDate(C.getString(C.getColumnIndexOrThrow("date")));
					SMSTime = new Date(Long.valueOf(getDate()));
					id = C.getInt(0);
					pos = C.getPosition();
					String SmsMessageId = C.getString(C.getColumnIndex("_id"));
                    ContentValues values = new ContentValues();
                    values.put("read", 1);
                    getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + SmsMessageId, null);
                    
					tv0.setText(getEmetteur().toString());
					tv1.setText(getText().toString());
					tv2.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.FRANCE)
	                          .format(SMSTime).toUpperCase());
					
					speakOut("SMS "+C.getPosition());
				}
				else{
					C.moveToPosition(0);
					setEmetteur(C.getString(C.getColumnIndexOrThrow("address")));
					setText(C.getString(C.getColumnIndexOrThrow("body")));
					setDate(C.getString(C.getColumnIndexOrThrow("date")));
					SMSTime = new Date(Long.valueOf(getDate()));
					id = C.getInt(0);
					pos = C.getPosition();
					
					String SmsMessageId = C.getString(C.getColumnIndex("_id"));
                    ContentValues values = new ContentValues();
                    values.put("read", 1);
                    getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + SmsMessageId, null);
					
					tv0.setText(getEmetteur().toString());
					tv1.setText(getText().toString());
					tv2.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.FRANCE)
	                          .format(SMSTime).toUpperCase());
					
					speakOut("SMS "+C.getPosition());
				}


			} else {

				//left swipe
				
				
				if(C.moveToPrevious()){
					
					setEmetteur(C.getString(C.getColumnIndexOrThrow("address")));
					setText(C.getString(C.getColumnIndexOrThrow("body")));
					setDate(C.getString(C.getColumnIndexOrThrow("date")));
					SMSTime = new Date(Long.valueOf(getDate()));
					id = C.getInt(0);
					pos = C.getPosition();
					String SmsMessageId = C.getString(C.getColumnIndex("_id"));
                    ContentValues values = new ContentValues();
                    values.put("read", 1);
                    getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + SmsMessageId, null);
					
					tv0.setText(getEmetteur().toString());
					tv1.setText(getText().toString());
					tv2.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.FRANCE)
	                          .format(SMSTime).toUpperCase());
					speakOut("SMS "+C.getPosition());
				}
				else{
					C.moveToPosition(C.getCount()-1);
					setEmetteur(C.getString(C.getColumnIndexOrThrow("address")));
					setText(C.getString(C.getColumnIndexOrThrow("body")));
					setDate(C.getString(C.getColumnIndexOrThrow("date")));
					SMSTime = new Date(Long.valueOf(getDate()));
					id = C.getInt(0);
					pos = C.getPosition();
					
					String SmsMessageId = C.getString(C.getColumnIndex("_id"));
                    ContentValues values = new ContentValues();
                    values.put("read", 1);
                    getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + SmsMessageId, null);
                    
					tv0.setText(getEmetteur().toString());
					tv1.setText(getText().toString());
					tv2.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.FRANCE)
	                          .format(SMSTime).toUpperCase());
					
					speakOut("SMS "+C.getPosition());
				}
				

			}

			return true;

		} 
		

		return false;



	}





	@Override
	public void onLongPress(MotionEvent e) {
		
		getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
		speakOut("supprimé avec succé");
		C.requery();
		C.moveToPosition(pos);
		setEmetteur(C.getString(C.getColumnIndexOrThrow("address")));
		setText(C.getString(C.getColumnIndexOrThrow("body")));
		setDate(C.getString(C.getColumnIndexOrThrow("date")));
		SMSTime = new Date(Long.valueOf(getDate()));
		id = C.getInt(0);
		pos = C.getPosition();
		
		tv0.setText(getEmetteur().toString());
		tv1.setText(getText().toString());
		tv2.setText(SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG, Locale.FRANCE)
                  .format(SMSTime).toUpperCase());
	}





	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}





	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}





	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}





	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
		
		speakOut("défiler pour parcourir, taper 2 fois pour lire");
		

	}


	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}

	public String getEmetteur() {
		return emetteur;
	}

	public void setEmetteur(String emetteur) {
		this.emetteur = emetteur;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	
	
	
	@Override
	protected void onDestroy() {
		
		C.close();
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){
		case R.id.btn_quitter:
			vibration.vibrate(100);
			finish();
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_quitter:
			speakOut("Retour");
			break;
		}
		
	}


	
	@Override
	protected void onPause() {
		// WHEN THE SCREEN IS ABOUT TO TURN OFF
		if (ScreenReceiver.wasScreenOn) {
			// THIS IS THE CASE WHEN ONPAUSE() IS CALLED BY THE SYSTEM DUE TO A SCREEN STATE CHANGE
			
			System.out.println("SCREEN TURNED OFF");
		} else {
			// THIS IS WHEN ONPAUSE() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// ONLY WHEN SCREEN TURNS ON
		if (!ScreenReceiver.wasScreenOn) {
			// THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
			speakOut("boite de réception, défiler pour parcourir, taper 2 fois pour lire");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			
			speakOut("boite de réception, défiler pour parcourir, taper 2 fois pour lire");
		}
		super.onResume();
	}
	

	



}


