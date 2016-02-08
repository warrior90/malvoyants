package com.talk_to_your_phone.contacts;


import java.util.Locale;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.Brailler;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class Contacts_Main extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener {
	
	private TextToSpeech tts;
	private Button btn_contacts;
	private Button btn_quitter;
	private Button btn_nvcontact;
	Vibrator vibration;
	Intent intent;
	BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		intent = getIntent();
		tts = new TextToSpeech(this,this);
		
		btn_nvcontact = (Button)findViewById(R.id.btn__nvcontact);
		btn_contacts = (Button)findViewById(R.id.btn_contacts);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		btn_nvcontact.setOnClickListener(this); btn_nvcontact.setOnLongClickListener(this);
		btn_contacts.setOnClickListener(this); btn_contacts.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this); btn_quitter.setOnLongClickListener(this);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		vibration = (Vibrator) getSystemService (Context.VIBRATOR_SERVICE);
	}

	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){
		
		case (R.id.btn__nvcontact):
			vibration.vibrate(100);
			Intent it_nvcontact = new Intent(this,Brailler.class);
			it_nvcontact.putExtra("source","nouveau contact nom");
			startActivity(it_nvcontact);
			break;
			
		case (R.id.btn_contacts):
			vibration.vibrate(100);
			Intent it_contacts = new Intent(this,Contacts.class);
			startActivity(it_contacts);
			break;
			
			
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
		
		case (R.id.btn__nvcontact):
			speakOut("Nouveau contact");
			break;
		
		case (R.id.btn_contacts):
			speakOut("Contacts");
			break;
			
			
		case (R.id.btn_quitter):
			speakOut("Quitter");
			break;
		}
		
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 
			
			speakOut("Répertoire");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
		
	}

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}
    
	@Override
	public void onDestroy() {
	
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	
	protected void onRestart() {
		speakOut("Contacts");
		super.onRestart();
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
			speakOut("Répertoire");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Répertoire");
		}
		super.onResume();
	}
	

}
