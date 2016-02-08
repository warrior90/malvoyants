package com.talk_to_your_phone.messagerie;

import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.talk_to_your_phone.outils.Brailler;

public class NV_message extends Activity implements OnLongClickListener, OnClickListener, TextToSpeech.OnInitListener{
	
	private Button btn_text_message;
	private Button btn_sound_message;
	private Button btn_quitter;
	private TextToSpeech tts;
	private Vibrator vibration;
	Intent intent;
	BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nv_message);
		
		tts = new TextToSpeech(this,this);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		btn_text_message = (Button)findViewById(R.id.btn_tx_msg);
		btn_sound_message = (Button)findViewById(R.id.btn_sound_message);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		btn_text_message.setOnClickListener(this);	btn_text_message.setOnLongClickListener(this);
		btn_sound_message.setOnClickListener(this);	btn_sound_message.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this);	btn_quitter.setOnLongClickListener(this);
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		intent = getIntent();
	}

	@Override
	public void onClick(View v) {
		
		 switch(v.getId()){

	        case(R.id.btn_tx_msg):
	            speakOut("Taper un message braille");
	        break;

	        case(R.id.btn_sound_message):
	            speakOut("Dicter un message");
	        break;
	        
	        case(R.id.btn_quitter):
	            speakOut("Quitter");
	        break;
	        }
		
	}

	@Override
	public boolean onLongClick(View v) {
		
		 switch(v.getId()){

	        case(R.id.btn_tx_msg):
	        	vibration.vibrate(100);
	            Intent intent_txt_msg = new Intent(this,Brailler.class);
	        	intent_txt_msg.putExtra("source", "nouveau msg text");
	        	//intent_txt_msg.putExtra("", "");
	        	startActivity(intent_txt_msg);
	        	
	        break;

	        case(R.id.btn_sound_message):
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
	        		Intent intent_sound_msg = new Intent(this,Dicter_SMS.class);
	        		startActivity(intent_sound_msg);
	        		break;
	        	}
	
	        
	        case(R.id.btn_quitter):
	        	vibration.vibrate(100);
	        	finish();
	        break;
	        }
		
		return false;
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
			
			speakOut("Choisir une méthode d'écriture");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
		
	}


	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}

	
	@Override
    protected void onRestart() {
    	speakOut("menu choix méthode d'écriture");
    	super.onRestart();
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
			speakOut("Choisir une méthode d'écriture");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Choisir une méthode d'écriture");
		}
		super.onResume();
	}
	

}
