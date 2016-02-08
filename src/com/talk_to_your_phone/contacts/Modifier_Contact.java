package com.talk_to_your_phone.contacts;

import java.util.Locale;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.Brailler;
import com.talk_to_your_phone.outils.Clavier;

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
import android.widget.Button;

public class Modifier_Contact extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{
	
	Intent i;
	String nom;
	String num;
	String id;
	private Button btn_modif_nom;
	private Button btn_modif_tel;
	private Button btn_retour;
	private Vibrator vibration;
	private TextToSpeech tts;
	BroadcastReceiver mReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_modif_contact);
		
		i = getIntent(); 
		nom = i.getStringExtra("nom");
		num = i.getStringExtra("num");
		id = i.getStringExtra("id");
		
		btn_modif_nom = (Button)findViewById(R.id.btn_modif_nom);
		btn_modif_tel = (Button)findViewById(R.id.btn_modif_tel);
		btn_retour = (Button)findViewById(R.id.btn_retour);
		
		btn_modif_nom.setOnClickListener(this); btn_modif_nom.setOnLongClickListener(this);
		btn_modif_tel.setOnClickListener(this); btn_modif_tel.setOnLongClickListener(this);
		btn_retour.setOnClickListener(this); btn_retour.setOnLongClickListener(this);
		
		tts = new TextToSpeech(this,this);
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()){
		
		case (R.id.btn_modif_nom):
			vibration.vibrate(100);
			Intent intent_nom = new Intent(Modifier_Contact.this, Brailler.class);
			intent_nom.putExtra("source", "modifier nom");
			intent_nom.putExtra("id", id);
			startActivity(intent_nom);
			tts.shutdown();
			break;
		
		case (R.id.btn_modif_tel):
			vibration.vibrate(100);
			Intent intent_tel = new Intent(Modifier_Contact.this, Clavier.class);
			intent_tel.putExtra("source", "modifier tel");
			intent_tel.putExtra("id", id);
			startActivity(intent_tel);
			tts.shutdown();
			break;
		
		case (R.id.btn_retour):
			vibration.vibrate(100);
			finish();
			break;
		}
				
		return false;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()){
		
		case (R.id.btn_modif_nom):
			speakOut("Modifier le nom");
			break;
		
		case (R.id.btn_modif_tel):
			speakOut("Modifier le numéro de téléphone");
			break;
		
		case (R.id.btn_retour):
			speakOut("Retour");
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
			
			speakOut("Effectuer un choix");

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
			speakOut("Modifier contact, Effectuer un choix");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Modifier contact, effectuer un choix");
		}
		super.onResume();
	}
	

}
