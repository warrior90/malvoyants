package com.talk_to_your_phone.parametres;

import java.util.Locale;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;

public class Etat_Batterie extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{
	
	int lev;
	
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context c, Intent i) {
			int level = i.getIntExtra("level", 0);
			ProgressBar pb = (ProgressBar) findViewById(R.id.progressbar);
			pb.setProgress(level);
			TextView tv = (TextView )findViewById(R.id.textfield);
			tv.setText("Battery Level: "+Integer.toString(level)+"%");
			lev = level;
		}
	};
	
	private TextToSpeech tts;
	private TextView etat;
	private Button quitter;
	private Vibrator vibration;
	BroadcastReceiver mReceiver;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.etat_batterie);
		registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		etat = (TextView)findViewById(R.id.textfield);
		quitter = (Button)findViewById(R.id.btn_quitter);
		quitter.setOnClickListener(this);	quitter.setOnLongClickListener(this);
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
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

			speakOut("Niveau de batterie, "+lev+" %");
			
		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}
	
	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);

	}
	
	
	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){
		
		case R.id.btn_quitter:
			vibration.vibrate(100);
			finish();
			break;
		}
		return true;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.btn_quitter:
			speakOut("retour");
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
			speakOut("Niveau de batterie, "+lev+" %");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Niveau de batterie, "+lev+" %");
		}
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		unregisterReceiver(mReceiver);
		unregisterReceiver(mBatInfoReceiver);
		super.onDestroy();

	}


	
}
