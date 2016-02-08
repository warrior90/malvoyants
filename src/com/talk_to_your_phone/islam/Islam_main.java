package com.talk_to_your_phone.islam;


import java.util.Locale;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.messagerie.Dicter_SMS;
import com.talk_to_your_phone.notifications.ScreenReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;


public class Islam_main extends Activity implements OnClickListener, OnLongClickListener, TextToSpeech.OnInitListener{
	
	private Button btn_quibla;
	private Button btn_heure_prieres;
	private Button btn_quitter;
	private TextToSpeech tts;
	private Vibrator vibration;
	Intent intent;
	MediaPlayer mp;
	BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.islam_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		intent = getIntent();
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		btn_quibla = (Button)findViewById(R.id.btn_quibla);
		btn_heure_prieres = (Button)findViewById(R.id.btn_heures_pri);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		btn_quibla.setOnClickListener(this);	btn_quibla.setOnLongClickListener(this);
		btn_heure_prieres.setOnClickListener(this);	btn_heure_prieres.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this);	btn_quitter.setOnLongClickListener(this);
		
		tts = new TextToSpeech(this,this);
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	}


	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.btn_quibla:
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.direction_qibla);
			mp.start();
			break;
			
		case R.id.btn_heures_pri:
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.heures_prieres);
			mp.start();
			break;
			
			
		case R.id.btn_quitter:
			speakOut("Quitter");
			break;
		}
	}
	
	@Override
	public boolean onLongClick(View v) {
		
		switch(v.getId()){
		
		case R.id.btn_quibla:
			if(!estOnligne()){
        		vibration.vibrate(100);
        		speakOut("nécessite une connexion internet");
        		try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        		break;
        	}
        	else{
        		vibration.vibrate(100);
    			Intent it_quibla = new Intent(this,Boussole.class);
    			startActivity(it_quibla);
    			break;
        	}
			
			
		case R.id.btn_heures_pri:
			if(!estOnligne()){
        		vibration.vibrate(100);
        		speakOut("nécessite une connexion internet");
        		try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        		break;
        	}
        	else{
        		vibration.vibrate(100);
    			Intent it_h_pri = new Intent(this,Heures_Prieres.class);
    			startActivity(it_h_pri);
    			break;
        	}
			
		
			
		case R.id.btn_quitter:
			vibration.vibrate(100);
			finish();
			break;
		}
		
		return true;
	}


	protected boolean estOnligne() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    } else {
	        return false;
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

			speakOut("Islam");

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
			try {
				speakOut("Islam");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			try {
				speakOut("Islam");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onResume();
	}
	

	

}
