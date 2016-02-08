package com.project.talk_to_your_phone;

import java.util.Locale;

import com.project.talk_to_your_phone.Main;
import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.TremblementDetection;
import com.talk_to_your_phone.outils.TremblementDetection.TremblementListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class Urgence extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{
	
	private TextToSpeech tts;
	private Button btn_samu;
	private Button btn_police_sec;
	private Button btn_prot_civ;
	private Button btn_quitter;
	Vibrator vibration;
	Intent intent;
	BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.urgence_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		intent = getIntent();
		tts = new TextToSpeech(this,this);
		
		
		btn_samu = (Button)findViewById(R.id.btn_samu);
		btn_police_sec = (Button)findViewById(R.id.btn_police_sec);
		btn_prot_civ = (Button)findViewById(R.id.btn_prot_civ);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		btn_samu.setOnClickListener(this); btn_samu.setOnLongClickListener(this);
		btn_police_sec.setOnClickListener(this); btn_police_sec.setOnLongClickListener(this);
		btn_prot_civ.setOnClickListener(this); btn_prot_civ.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this);	btn_quitter.setOnLongClickListener(this);
		
		vibration = (Vibrator) getSystemService (Context.VIBRATOR_SERVICE);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE

	}

	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){
		
		case (R.id.btn_samu):
			vibration.vibrate(100);
			String samu = "tel:190";
			speakOut("appel vers samu");
			try {
				Thread.sleep(2000);
				} catch (InterruptedException e) {
				e.printStackTrace();
				}

			Appeler(samu);
			finish();
			break;
			
		case (R.id.btn_police_sec):
			vibration.vibrate(100);
			String police = "tel:197";
			speakOut("appel vers police de secours");
			try {
				Thread.sleep(2000);
				} catch (InterruptedException e) {
				e.printStackTrace();
				}
	
			Appeler(police);
			finish();
			break;
			
		case (R.id.btn_prot_civ):
			vibration.vibrate(100);
			String protection = "tel:198";
			
			try {
				speakOut("appel vers la protection civile");
				Thread.sleep(2000);
				} catch (InterruptedException e) {
				e.printStackTrace();
				}
	
			Appeler(protection);
			finish();
			break;
			
		case (R.id.btn_quitter):
			vibration.vibrate(100);
			finish();
			break;
	
		}
		return true;
	}

	public void Appeler(String telUri){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telUri));
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case (R.id.btn_samu):
			speakOut("samu");
			break;
		
		case (R.id.btn_police_sec):
			speakOut("Police de secours");
			break;
			
		case (R.id.btn_prot_civ):
			speakOut("protection civile");
			break;
		case (R.id.btn_quitter):
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
			
			speakOut("Urgence");

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
	
	
	
	

	@Override
	protected void onResume() {
		// ONLY WHEN SCREEN TURNS ON
		if (!ScreenReceiver.wasScreenOn) {
			// THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
			
			try {
				speakOut("Urgence");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			try {
				speakOut("Urgence");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.onResume();
	}
	

}


