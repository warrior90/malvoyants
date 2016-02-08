package com.talk_to_your_phone.horloge;

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
import android.widget.TextView;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;

public class Time_Picker_MN extends Activity implements OnLongClickListener, OnClickListener,
TextToSpeech.OnInitListener{
	
	private TextToSpeech tts;
	private TextView tv_heures;
	private TextView tv_minutes;
	private Button plus_heures;
	private Button plus_minutes;
	private Button moins_heures;
	private Button moins_minutes;
	private Button btn_valider;

	private Vibrator vibration;
	
	int cpt_hh=0;
	int cpt_mn=0;
	String hh;
	String mn;
	Intent intent;
	BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_picker_mn);
		
		intent = getIntent();
		mn = intent.getStringExtra("minutes");
		
		tts = new TextToSpeech(this,this);
		
		tv_minutes = (TextView)findViewById(R.id.mn);
		tv_minutes.setText(mn);
		
		cpt_mn = Integer.parseInt(mn);
		
		plus_minutes =(Button)findViewById(R.id.plus_minute);
		moins_minutes =(Button)findViewById(R.id.moins_minutes);
		btn_valider = (Button)findViewById(R.id.btn_valider);
			
		plus_minutes.setOnClickListener(this);		
		moins_minutes.setOnClickListener(this);	
		btn_valider.setOnClickListener(this);	btn_valider.setOnLongClickListener(this);
		
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
		
		switch (v.getId()) {

		case R.id.btn_valider:
			vibration.vibrate(100);
			Intent int_ret = new Intent();
			int_ret.putExtra("minutes", tv_minutes.getText().toString());
			setResult(RESULT_OK,int_ret);
			finish();
			break;

	 
		}
		return false;
	}




	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
	
		case R.id.plus_minute:
			if(cpt_mn < 59){
				cpt_mn++;
				tv_minutes.setText(String.valueOf(cpt_mn).toString());
				speakOut(tv_minutes.getText().toString()+" minutes");
				break;
			}
			else{
				cpt_mn = 0;
				tv_minutes.setText(String.valueOf(cpt_mn).toString());
				speakOut(tv_minutes.getText().toString()+" minutes");
				break;
			}
		
		case R.id.moins_minutes:
			if(cpt_mn > 0){
				cpt_mn--;
				tv_minutes.setText(String.valueOf(cpt_mn).toString());	
				speakOut(tv_minutes.getText().toString()+" minutes");
				break;
			}
			else{
				cpt_mn = 59;
				tv_minutes.setText(String.valueOf(cpt_mn).toString());	
				speakOut(tv_minutes.getText().toString()+" minutes");
				break;
			}
			

		case R.id.btn_valider:
			speakOut("Valider");
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

			speakOut("Régler les minutes du réveil");

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
			speakOut("Régler les minutes du réveil");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Régler les minutes du réveil");
		}
		super.onResume();
	}
}
