package com.talk_to_your_phone.journal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Appels_recus extends Activity implements OnGestureListener, TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{
	private TextToSpeech tts;
	private TextView appel_recu;
	private TextView titre;
	private Button retour;
	private GestureDetector gd;
	private Cursor managedCursor;
	private Cursor Contacts;
	private Vibrator vibration;
	String phNumber;
	String callDuration;
	Date callDayTime;
	Intent intent;
	String nom;
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	BroadcastReceiver mReceiver;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appels);
		tts = new TextToSpeech(this,this);
		
		intent = getIntent();
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
        
		gd = new GestureDetector(this);

		
		titre = (TextView) findViewById(R.id.text);
		titre.setText("Appels reçus");
		
		appel_recu = (TextView) findViewById(R.id.tv_app);
		appel_recu.setText("");
		
		retour = (Button)findViewById(R.id.btn_retour);
		
		retour.setOnClickListener(this);
		retour.setOnLongClickListener(this);

		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		Contacts =  getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, 
				null, 
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME + " ASC");

		String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
		managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null, strOrder);

		gd.setOnDoubleTapListener(new OnDoubleTapListener()
		{

			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{
				if(nom!=null){
					speakOut(nom+" , "+SimpleDateFormat.getDateInstance(
							SimpleDateFormat.LONG, Locale.FRANCE)
							.format(callDayTime).toUpperCase().toString()+" , durée , "+callDuration+" secondes");
				}
				else{
					speakOut(phNumber+" , "+SimpleDateFormat.getDateInstance(
							SimpleDateFormat.LONG, Locale.FRANCE)
							.format(callDayTime).toUpperCase().toString()+" , durée , "+callDuration+" secondes");
					
				}
				return false;

			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {

				return false;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {

				
				return false;
			}});



	}




	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

			speakOut("Appels reçus, défiler pour parcourir");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}



	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		return gd.onTouchEvent(e);//return the double tap events
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		tts.shutdown();
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

		boolean found = false;
		StringBuffer sb = new StringBuffer();
		int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER ); 
		int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
		int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
		int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
		
		if(managedCursor.getCount() == 0){
			speakOut("Journal vide");
			return true;
		}

		if (Math.abs(dY)<SWIPE_MAX_OFF_PATH &&

				Math.abs(velocityX)>=SWIPE_THRESHOLD_VELOCITY &&

				Math.abs(dX)>=SWIPE_MIN_DISTANCE ) {

			if (dX>0) {

				//right swipe


				if(managedCursor.moveToNext()){

					while(managedCursor.moveToNext() && !found){

						phNumber = managedCursor.getString( number );
						String callType = managedCursor.getString( type );
						String callDate = managedCursor.getString( date );
						callDayTime = new Date(Long.valueOf(callDate));
						int dircode = Integer.parseInt( callType );
						if(dircode == CallLog.Calls.INCOMING_TYPE){
							found = true;
							nom = managedCursor.getString(name);
							if(nom!=null){
								sb.append( "\nDe:"+nom
										+" \nDate: "+SimpleDateFormat.getDateInstance(
												SimpleDateFormat.LONG, Locale.FRANCE)
												.format(callDayTime).toUpperCase()
												+"\nHeure: "+SimpleDateFormat.getTimeInstance(
														SimpleDateFormat.SHORT, Locale.FRANCE)
														.format(callDayTime).toUpperCase());
								speakOut(nom);
							}
							else{
								sb.append( "\nDe:"+phNumber
										+" \nDate: "+SimpleDateFormat.getDateInstance(
												SimpleDateFormat.LONG, Locale.FRANCE)
												.format(callDayTime).toUpperCase()
												+"\nHeure: "+SimpleDateFormat.getTimeInstance(
														SimpleDateFormat.SHORT, Locale.FRANCE)
														.format(callDayTime).toUpperCase());
								speakOut(phNumber);
							}						
							appel_recu.setText(sb);

						}
					}
				}
				else{
					managedCursor.moveToPosition(0);
					while(managedCursor.moveToNext() && !found){

						phNumber = managedCursor.getString( number );
						String callType = managedCursor.getString( type );
						String callDate = managedCursor.getString( date );
						callDayTime = new Date(Long.valueOf(callDate));
						int dircode = Integer.parseInt( callType );
						if(dircode == CallLog.Calls.INCOMING_TYPE){
							found = true;
							nom = managedCursor.getString(name);
							if(nom!=null){
								sb.append( "\nDe:"+nom
										+" \nDate: "+SimpleDateFormat.getDateInstance(
												SimpleDateFormat.LONG, Locale.FRANCE)
												.format(callDayTime).toUpperCase()
												+"\nHeure: "+SimpleDateFormat.getTimeInstance(
														SimpleDateFormat.SHORT, Locale.FRANCE)
														.format(callDayTime).toUpperCase());
								speakOut(nom);
							}
							else{
								sb.append( "\nDe:"+phNumber
										+" \nDate: "+SimpleDateFormat.getDateInstance(
												SimpleDateFormat.LONG, Locale.FRANCE)
												.format(callDayTime).toUpperCase()
												+"\nHeure: "+SimpleDateFormat.getTimeInstance(
														SimpleDateFormat.SHORT, Locale.FRANCE)
														.format(callDayTime).toUpperCase());
								speakOut(phNumber);
							}						
							appel_recu.setText(sb);
						}
					}
				}


			} else {

				//left swipe


				if(managedCursor.moveToPrevious()){

					while(managedCursor.moveToPrevious() && !found){

						phNumber = managedCursor.getString( number );
						String callType = managedCursor.getString( type );
						String callDate = managedCursor.getString( date );
						callDayTime = new Date(Long.valueOf(callDate));
						int dircode = Integer.parseInt( callType );
						if(dircode == CallLog.Calls.INCOMING_TYPE){
							found = true;
							nom = managedCursor.getString(name);
							if(nom!=null){
								sb.append( "\nDe:"+nom
										+" \nDate: "+SimpleDateFormat.getDateInstance(
												SimpleDateFormat.LONG, Locale.FRANCE)
												.format(callDayTime).toUpperCase()
												+"\nHeure: "+SimpleDateFormat.getTimeInstance(
														SimpleDateFormat.SHORT, Locale.FRANCE)
														.format(callDayTime).toUpperCase());
								speakOut(nom);
							}
							else{
								sb.append( "\nDe:"+phNumber
										+" \nDate: "+SimpleDateFormat.getDateInstance(
												SimpleDateFormat.LONG, Locale.FRANCE)
												.format(callDayTime).toUpperCase()
												+"\nHeure: "+SimpleDateFormat.getTimeInstance(
														SimpleDateFormat.SHORT, Locale.FRANCE)
														.format(callDayTime).toUpperCase());
								speakOut(phNumber);
							}						
							appel_recu.setText(sb);
						}
					}
				}
				else{
					managedCursor.moveToPosition(managedCursor.getCount()-1);
					while(managedCursor.moveToPrevious() && !found){
						phNumber = managedCursor.getString( number );
						String callType = managedCursor.getString( type );
						String callDate = managedCursor.getString( date );
						callDayTime = new Date(Long.valueOf(callDate));
						int dircode = Integer.parseInt( callType );
						if(dircode == CallLog.Calls.INCOMING_TYPE){
							found = true;
							nom = managedCursor.getString(name);
							if(nom!=null){
								sb.append( "\nDe:"+nom
										+" \nDate: "+SimpleDateFormat.getDateInstance(
												SimpleDateFormat.LONG, Locale.FRANCE)
												.format(callDayTime).toUpperCase()
												+"\nHeure: "+SimpleDateFormat.getTimeInstance(
														SimpleDateFormat.SHORT, Locale.FRANCE)
														.format(callDayTime).toUpperCase());
								speakOut(nom);
							}
							else{
								sb.append( "\nDe:"+phNumber
										+" \nDate: "+SimpleDateFormat.getDateInstance(
												SimpleDateFormat.LONG, Locale.FRANCE)
												.format(callDayTime).toUpperCase()
												+"\nHeure: "+SimpleDateFormat.getTimeInstance(
														SimpleDateFormat.SHORT, Locale.FRANCE)
														.format(callDayTime).toUpperCase());
								speakOut(phNumber);
							}						
							appel_recu.setText(sb);
						}
					}
				}

			}

			return true;

		} 


		return false;


	}
	@Override
	public void onLongPress(MotionEvent arg0) {
		appel_recu.setText("");
		managedCursor.deactivate();
		Contacts.deactivate();
		Intent int_choix = new Intent(Appels_recus.this, Menu_choix_journal.class);
		int_choix.putExtra("num",phNumber);
		startActivity(int_choix);

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
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_retour:
			speakOut("Retour");
			break;
		}
		
	}




	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){
		case R.id.btn_retour:
			vibration.vibrate(100);
			finish();
			break;
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();		
		}
		unregisterReceiver(mReceiver);
		super.onDestroy();
		
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
			speakOut("Appels reçus, défiler pour parcourir");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Appels reçus, défiler pour parcourir");
		}
		super.onResume();
	}
	


}
