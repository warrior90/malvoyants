package com.talk_to_your_phone.journal;

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
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class Journal_main extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{
	private TextToSpeech tts;
	private Button btn_app_abs;
	private Button btn_app_rec;
	private Button btn_app_em;
	private Button btn_effacer;
	private Button btn_quitter;
	private Vibrator vibration;
	Intent intent;
	private Cursor managedCursor;
	BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.journal_main);
		tts = new TextToSpeech(this,this);

		intent = getIntent();
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		btn_app_abs = (Button)findViewById(R.id.btn_app_absc);
		btn_app_rec = (Button)findViewById(R.id.btn_app_rec);
		btn_app_em = (Button)findViewById(R.id.btn_app_em);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		btn_effacer = (Button)findViewById(R.id.btn_effacer);
		
		btn_app_abs.setOnClickListener(this);	btn_app_abs.setOnLongClickListener(this);
		btn_app_rec.setOnClickListener(this);	btn_app_rec.setOnLongClickListener(this);
		btn_app_em.setOnClickListener(this);	btn_app_em.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this);	btn_quitter.setOnLongClickListener(this);
		btn_effacer.setOnClickListener(this);	btn_effacer.setOnLongClickListener(this);

		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, null,null,null);
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

			speakOut("Journal d'appels");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
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
	public boolean onLongClick(View v) {

		switch(v.getId()){

		case (R.id.btn_app_absc):
			vibration.vibrate(100);
		Intent intent_abs = new Intent(this,Appels_abscences.class);
		startActivity(intent_abs);
		break;

		case (R.id.btn_app_rec):
			vibration.vibrate(100);
		Intent intent_rec = new Intent(this,Appels_recus.class);
		startActivity(intent_rec);
		break;

		case (R.id.btn_app_em):
			vibration.vibrate(100);
		Intent intent_em = new Intent(this,Appels_emis.class);
		startActivity(intent_em);
		break;
		
		case (R.id.btn_effacer):
			vibration.vibrate(100);
			if(managedCursor.getCount() == 0){
				speakOut("Journal vide");
				try {
					Thread.sleep(2000);
				} catch (Exception ex) {
					
					ex.printStackTrace();
				}
				break;
			}
			else{
				
				Intent intent_eff_jour = new Intent(this,Effacer_journal.class);
				startActivity(intent_eff_jour);
				break;
			}
		

		case (R.id.btn_quitter):
			vibration.vibrate(100);
			finish();
		break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){

		case (R.id.btn_app_absc):
			speakOut("Appels en abscences");
		break;

		case (R.id.btn_app_rec):
			speakOut("Appels reçus");
		break;

		case (R.id.btn_app_em):
			speakOut("Appels émis");
		break;
		
		case (R.id.btn_effacer):
			speakOut("éffacer le journal");
		break;

		case (R.id.btn_quitter):
			speakOut("Quitter");
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
			try {
				speakOut("Journal d'appels");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			try {
				speakOut("Journal d'appels");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		super.onResume();
	}



}
