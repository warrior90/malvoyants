package com.talk_to_your_phone.parametres;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import android.view.WindowManager;
import android.widget.Button;
import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;


public class Parametres_Main extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{

	private Button btn_info_batt;
	private Button btn_mode;
	private Button btn_quitter;
	private TextToSpeech tts;
	private Vibrator vibration;
	Intent i;
	BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parametres_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		tts= new TextToSpeech(this,this);

		btn_info_batt = (Button)findViewById(R.id.btn_inf_batt);
		btn_mode = (Button)findViewById(R.id.btn_mode);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);


		btn_info_batt.setOnClickListener(this); btn_info_batt.setOnLongClickListener(this);
		btn_mode.setOnClickListener(this); btn_mode.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this); btn_quitter.setOnLongClickListener(this);

		i = getIntent();

	}

	@Override
	public boolean onLongClick(View v) {

		switch(v.getId()){

		case (R.id.btn_inf_batt):
			vibration.vibrate(100);
			Intent int_equiv = new Intent(this,Etat_Batterie.class);
			startActivity(int_equiv);
		break;

		case (R.id.btn_mode):
			vibration.vibrate(100);
			Intent int_mode = new Intent(this,Mode.class);
			startActivity(int_mode);
			break;

		case (R.id.btn_quitter):
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
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.btn_inf_batt:
			speakOut("Etat de batterie");
			break;

		case R.id.btn_mode:
			speakOut("Changer le mode");
			break;

		case R.id.btn_quitter:
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

			speakOut("menu paramètres");
			
		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}



	@Override
	protected void onRestart() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		speakOut("Menu paramètres");
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
			speakOut("menu paramètre");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("menu paramètre");
		}
		super.onResume();
	}
	

}
