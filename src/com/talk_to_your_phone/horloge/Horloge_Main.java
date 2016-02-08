package com.talk_to_your_phone.horloge;

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
import com.talk_to_your_phone.outils.TremblementDetection;
import com.talk_to_your_phone.outils.TremblementDetection.TremblementListener;

public class Horloge_Main extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{

	private Button btn_date_equiv;
	private Button btn_alarme;
	private Button btn_quitter;
	private TextToSpeech tts;
	private Vibrator vibration;
	Intent i;
	BroadcastReceiver mReceiver;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.horloge_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		tts= new TextToSpeech(this,this);

		btn_date_equiv = (Button)findViewById(R.id.btn_date_equiv);
		btn_alarme = (Button)findViewById(R.id.btn_alarme);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);


		btn_date_equiv.setOnClickListener(this); btn_date_equiv.setOnLongClickListener(this);
		btn_alarme.setOnClickListener(this); btn_alarme.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this); btn_quitter.setOnLongClickListener(this);

		i = getIntent();

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

		case (R.id.btn_date_equiv):
			vibration.vibrate(100);
			Intent int_equiv = new Intent(this,Equivalent_date.class);
			startActivity(int_equiv);
		break;

		case (R.id.btn_alarme):
			vibration.vibrate(100);
			Intent int_reveil = new Intent(this,Reveil.class);
			startActivity(int_reveil);
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
		case R.id.btn_date_equiv:
			speakOut("jour équivalent date");
			break;

		case R.id.btn_alarme:
			speakOut("Réveil");
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

			Calendar c = Calendar.getInstance();
			System.out.println("Current time => "+c.getTime());

			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = df.format(c.getTime());
			String fd = d.format(c.getTime());
			
			Integer jour = c.get(Calendar.DAY_OF_WEEK);
			switch(jour){
			case Calendar.MONDAY:
				speakOut("Lundi, le, "+fd+", Heure, "+formattedDate+" minutes");
				break;
				
			case Calendar.TUESDAY:
				speakOut("Mardi, le, "+fd+", Heure, "+formattedDate+" minutes");
				break;
				
				
			case Calendar.WEDNESDAY:
				speakOut("Mercredi, le, "+fd+", Heure, "+formattedDate+" minutes");
				break;
				
				
			case Calendar.THURSDAY:
				speakOut("Jeudi, le, "+fd+", Heure, "+formattedDate+" minutes");
				break;
				
				
			case Calendar.FRIDAY:
				speakOut("Vendredi, le, "+fd+", Heure, "+formattedDate+" minutes");
				break;
				
				
			case Calendar.SATURDAY:
				speakOut("Samedi, le, "+fd+", Heure, "+formattedDate+" minutes");
				break;
				
				
			case Calendar.SUNDAY:
				speakOut("Dimanche, le, "+fd+", Heure, "+formattedDate+" minutes");
				break;
				
				
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}



	@Override
	protected void onRestart() {
		speakOut("Menu Horloge");
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
			speakOut("menu horloge, Effectuer un choix");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("menu horloge, Effectuer un choix");
		}
		super.onResume();
	}


}
