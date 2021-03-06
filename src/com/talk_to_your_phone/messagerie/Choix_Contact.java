package com.talk_to_your_phone.messagerie;

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
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.Brailler;
import com.talk_to_your_phone.outils.TremblementDetection;

public class Choix_Contact extends Activity implements OnGestureListener, TextToSpeech.OnInitListener, 
OnClickListener, OnLongClickListener{

	public GestureDetector gd;
	private TextToSpeech tts;
	private TextView tv1;
	private TextView tv2;
	private Button btn_quitter;
	//private TremblementDetection tremblementDetection;
	private static final int CODE_ACTIVITE = 101;
	Vibrator vibration;
	int nbrcontacts;
	Intent intent;
	Cursor people;
	Cursor people_filter;
	String Contact_ID;
	private static final int SWIPE_MIN_DISTANCE = 150;

	private static final int SWIPE_MAX_OFF_PATH = 100;

	private static final int SWIPE_THRESHOLD_VELOCITY = 100;

	BroadcastReceiver mReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choix_contacts);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		tts = new TextToSpeech(this,this);
		intent = getIntent();
		tv1 = (TextView)findViewById(R.id.tv_nom);	
		tv2 = (TextView)findViewById(R.id.tv_num);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		btn_quitter.setOnClickListener(this);
		btn_quitter.setOnLongClickListener(this);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		gd = new GestureDetector(this);
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	
		people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, 
				null, 
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		
		
		nbrcontacts = people.getCount();

		gd.setOnDoubleTapListener(new OnDoubleTapListener()
		{

			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{
				String msg_text = intent.getStringExtra("text");
				String num = tv2.getText().toString();
				try{
					Envoi_SMS(num, msg_text);
					speakOut("Message envoy�");
					Thread.sleep(2000);
					finish();

				}catch(Exception ex){
					speakOut("Erreur d'envoi");
					finish();
				}
				
				return false;

			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {

				
				return true;
			}});


	}

	
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		return gd.onTouchEvent(e);//return the double tap events
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		float dX = e2.getX()-e1.getX();

		float dY = e1.getY()-e2.getY();

		if(people.getCount() == 0){
			speakOut("pas de contacts");
			return true;
		}
		
		if (Math.abs(dY)<SWIPE_MAX_OFF_PATH &&

				Math.abs(velocityX)>=SWIPE_THRESHOLD_VELOCITY &&

				Math.abs(dX)>=SWIPE_MIN_DISTANCE ) {

			if (dX>0) {

				//right swipe
				Log.i("pos", String.valueOf(people.getPosition()));
				
				if(people.moveToNext()){
					Contact_ID = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
					int name = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
					String nom = people.getString(name);
					String numb = "";
					int hasPhone = people.getInt(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					if (hasPhone == 1) {
						Cursor phoneCursor = getContentResolver().query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
								null, 
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + Contact_ID, 
								null, 
								null);
						
						while (phoneCursor.moveToNext()) {
							if(!numb.contains(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))){
								numb =numb+"\n"+ phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							} 
						} 
						phoneCursor.close();
					}


					tv1.setText(nom.toString());
					speakOut(nom.toString());
					tv2.setText(numb.toString());
				}
				else{
					people.moveToPosition(0);
					Contact_ID = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
					int name = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
					String nom = people.getString(name);
					String numb = "";
					int hasPhone = people.getInt(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					if (hasPhone == 1) {
						Cursor phoneCursor = getContentResolver().query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
								null, 
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + Contact_ID, 
								null, 
								null);
						
						while (phoneCursor.moveToNext()) {
							if(!numb.contains(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))){
								numb =numb+"\n"+ phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							} 
						} 
						phoneCursor.close();
					}


					tv1.setText(nom.toString());
					speakOut(nom.toString());
					tv2.setText(numb.toString());
				}


			} else {

				//left swipe
				Log.i("pos", String.valueOf(people.getPosition()));
				
				if(people.moveToPrevious()){
					Contact_ID = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
					int name = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
					String nom = people.getString(name);
					String numb = "";
					int hasPhone = people.getInt(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					if (hasPhone == 1) {
						Cursor phoneCursor = getContentResolver().query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
								null, 
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + Contact_ID, 
								null, 
								null);
						
						while (phoneCursor.moveToNext()) {
							if(!numb.contains(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))){
								numb =numb+"\n"+ phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							} 
						} 
						phoneCursor.close();
					}


					tv1.setText(nom.toString());
					speakOut(nom.toString());
					tv2.setText(numb.toString());
				}
				else{
					people.moveToPosition(people.getCount()-1);
								
					Contact_ID = people.getString(people.getColumnIndex(ContactsContract.Contacts._ID));
					int name = people.getColumnIndex(PhoneLookup.DISPLAY_NAME);
					String nom = people.getString(name);
					String numb = "";
					int hasPhone = people.getInt(people.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
					if (hasPhone == 1) {
						Cursor phoneCursor = getContentResolver().query(
								ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
								null, 
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + Contact_ID, 
								null, 
								null);
						
						while (phoneCursor.moveToNext()) {
							if(!numb.contains(phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))){
								numb =numb+"\n"+ phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							} 
						} 
						phoneCursor.close();
					}


					tv1.setText(nom.toString());
					speakOut(nom.toString());
					tv2.setText(numb.toString());
				}
				

			}

			return true;

		} else if (Math.abs(dX)<SWIPE_MAX_OFF_PATH &&

				Math.abs(velocityY)>=SWIPE_THRESHOLD_VELOCITY &&

				Math.abs(dY)>=SWIPE_MIN_DISTANCE ) {

			if (dY>0) {

				Toast.makeText(getApplicationContext(), "Up Swipe", Toast.LENGTH_SHORT).show();

			} else {

				Toast.makeText(getApplicationContext(), "Down Swipe", Toast.LENGTH_SHORT).show();

			}

			return true;

		}

		return false;



	}


	@Override
	public void onLongPress(MotionEvent e) {
		tv1.setText("");
		tv2.setText("");
		people.deactivate();
		vibration.vibrate(100);
		Intent i = new Intent(this,Brailler.class);
		i.putExtra("source", "contact_filtre");
		startActivityForResult(i, CODE_ACTIVITE);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
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

			speakOut("d�filer pour parcourir, taper 2 fois pour envoyer le message,long appui pour filtrer");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}


	}

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);

	}

	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		people.close();

		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		people.close();
		unregisterReceiver(mReceiver);
		super.onDestroy();

	}

	
	
	public void Envoi_SMS(String tel, String message) throws Exception{

	    SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage(tel, null, message, null, null);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		//super.onActivityResult(requestCode, resultCode, data);

		if(requestCode== CODE_ACTIVITE){

			if(resultCode == RESULT_OK){

				String filtre = data.getStringExtra("text");

				people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, 
						null, 
						ContactsContract.Contacts.DISPLAY_NAME + " LIKE '"+filtre+"%'", 
						null, 
						ContactsContract.Contacts.DISPLAY_NAME + " ASC");

				people.moveToFirst();
				people.requery();

				int cpt = people.getCount();

				speakOut("il y a "+cpt+" r�sultats");
			}
		
		}
	}


	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){

        case(R.id.btn_quitter):
        	vibration.vibrate(100);
            finish();
        	
        break;
		}
		return false;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){

        case(R.id.btn_quitter):
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
			speakOut("d�filer pour parcourir, taper 2 fois pour envoyer le message,long click pour filtrer");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			int cpt = people.getCount();
			speakOut("il y a, "+cpt+" contact trouv�");
		}
		super.onResume();
	}
	
	
}

