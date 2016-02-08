package com.talk_to_your_phone.parametres;

import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;

public class Mode extends Activity implements OnLongClickListener,TextToSpeech.OnInitListener, OnClickListener
{
	private TextToSpeech tts;
	private Vibrator vibration ;
	private Button general;
	private Button silent;
	private Button vibreur;
	private Button retour;
	Intent intent;
	private AudioManager audio_mngr;
	BroadcastReceiver mReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mode);

		tts = new TextToSpeech(this,this);
		intent = getIntent();
		
		silent = (Button) findViewById(R.id.btn_silencieux);
		general = (Button) findViewById(R.id.btn_general);
		vibreur = (Button) findViewById(R.id.btn_vibreur);
		retour = (Button) findViewById(R.id.btn_quitter);

		silent.setOnClickListener(this);	silent.setOnLongClickListener(this);
		general.setOnClickListener(this);	general.setOnLongClickListener(this);
		vibreur.setOnClickListener(this);	vibreur.setOnLongClickListener(this);
		retour.setOnClickListener(this);	retour.setOnLongClickListener(this);

		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		audio_mngr = (AudioManager) getBaseContext().getSystemService(Context.AUDIO_SERVICE);


	}
	
	@Override
	public boolean onLongClick(View v) {

		switch(v.getId()){

		case R.id.btn_general:
			vibration.vibrate(100);
			audio_mngr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			speakOut("mode général activé");
			break;

		case R.id.btn_silencieux:
			vibration.vibrate(100);
			audio_mngr.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			speakOut("mode silencieux activé");
			break;

		case R.id.btn_vibreur:
			vibration.vibrate(100);
			audio_mngr.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
			speakOut("mode vibreur activé");
			break;
			
		case R.id.btn_quitter:
			vibration.vibrate(100);
			finish();
			break;
		}
		return true;
	}



	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);

	}

	@Override
	public void onInit(int status) {


		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

			
			int mode = audio_mngr.getRingerMode();
			
			switch(mode){
			
			case AudioManager.RINGER_MODE_NORMAL:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				speakOut("le téléphone est en mode normal");
				break;
			case AudioManager.RINGER_MODE_SILENT:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				speakOut("le téléphone est en mode silencieux");
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				speakOut("le téléphone est en mode vibreur");
				break;
			}
			
		} else {
			Log.e("TTS", "Initilization Failed!");
		}



	}
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.btn_silencieux:
			speakOut("mode silencieux");
			break;

		case R.id.btn_general:
			speakOut("mode général");
			break;
			
		case R.id.btn_vibreur:
			speakOut("mode vibreur");
			break;

		case R.id.btn_quitter:
			speakOut("retour");
			break;
		}

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
			int mode = audio_mngr.getRingerMode();
			
			switch(mode){
			
			case AudioManager.RINGER_MODE_NORMAL:
				speakOut("le téléphone est en mode normal");
				break;
			case AudioManager.RINGER_MODE_SILENT:
				speakOut("le téléphone est en mode silencieux");
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				speakOut("le téléphone est en mode vibreur");
				break;
			}
			
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			int mode = audio_mngr.getRingerMode();
			
			switch(mode){
			
			case AudioManager.RINGER_MODE_NORMAL:
				speakOut("le téléphone est en mode normal");
				break;
			case AudioManager.RINGER_MODE_SILENT:
				speakOut("le téléphone est en mode silencieux");
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				speakOut("le téléphone est en mode vibreur");
				break;
			}
			
		}
		super.onResume();
	}
	
	
		
}