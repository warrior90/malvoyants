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

public class Time_Picker_HH extends Activity implements OnLongClickListener, OnClickListener,
TextToSpeech.OnInitListener{
	
	private TextToSpeech tts;
	private TextView tv_heures;
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
		setContentView(R.layout.time_picker_hh);
		
		intent = getIntent();
		hh = intent.getStringExtra("hh");
		mn = intent.getStringExtra("mn");
		
		tts = new TextToSpeech(this,this);
		
		tv_heures = (TextView)findViewById(R.id.hh);
		tv_heures.setText(hh);
		
		cpt_hh = Integer.parseInt(hh);
		cpt_mn = Integer.parseInt(mn);
		
		plus_heures = (Button)findViewById(R.id.plus_heure);
		moins_heures = (Button)findViewById(R.id.moins_heures);
		btn_valider = (Button)findViewById(R.id.btn_valider);
		
		plus_heures.setOnClickListener(this);	
		moins_heures.setOnClickListener(this);		
		btn_valider.setOnClickListener(this);	btn_valider.setOnLongClickListener(this);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
	}




	@Override
	public boolean onLongClick(View v) {
		
		switch (v.getId()) {

		case R.id.btn_valider:
			vibration.vibrate(100);
			Intent int_mn = new Intent(this,Time_Picker_MN.class);
			int_mn.putExtra("minutes", mn);
			startActivityForResult(int_mn,201);
			break;

		}
		return true;
	}




	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.plus_heure:
			if(cpt_hh < 23){
				cpt_hh++;
				tv_heures.setText(String.valueOf(cpt_hh).toString());
				speakOut(tv_heures.getText().toString()+" heures");
				break;
			}
			else{
				cpt_hh = 0;
				tv_heures.setText(String.valueOf(cpt_hh).toString());
				speakOut(tv_heures.getText().toString()+" heures");
				break;
			}
		

		case R.id.moins_heures:
			if(cpt_hh > 0){
				cpt_hh--;
				tv_heures.setText(String.valueOf(cpt_hh).toString());
				speakOut(tv_heures.getText().toString()+" heures");
				break;
			}
			else{
				cpt_hh = 23;
				tv_heures.setText(String.valueOf(cpt_hh).toString());
				speakOut(tv_heures.getText().toString()+" heures");
				break;
			}
			

		
		case R.id.btn_valider:
			speakOut("Valider");
			break;

	 
		}
		
	}


	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 201){
			if(resultCode == RESULT_OK){
				String mn = data.getStringExtra("minutes");
				Intent int_ret = new Intent();
				int_ret.putExtra("heures", tv_heures.getText().toString());
				int_ret.putExtra("minutes", mn);
				setResult(RESULT_OK,int_ret);
				finish();
			}
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

			speakOut("Régler les heures du réveil");

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
			speakOut("Régler les heures du réveil");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Régler les heures du réveil");
		}
		super.onResume();
	}

}
