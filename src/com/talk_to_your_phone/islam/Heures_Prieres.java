package com.talk_to_your_phone.islam;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
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


public class Heures_Prieres extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener {

	// All static variables
	static final String URL = "http://www.islamicfinder.org/prayer_service.php?country=tunisia&city=sousse&state=&zipcode=&latitude=35.8256&longitude=10.6411&timezone=1&HanfiShafi=1&pmethod=1&fajrTwilight1=10&fajrTwilight2=10&ishaTwilight=10&ishaInterval=30&dhuhrInterval=1&maghribInterval=1&dayLight=0&simpleFormat=xml";
	// XML node keys
	
	static final String KEY_PRAYER = "prayer"; // parent node
	static final String KEY_FAJR = "fajr";
	static final String KEY_SUNRISE = "sunrise";
	static final String KEY_DHUHR = "dhuhr";
	static final String KEY_ASR = "asr";
	static final String KEY_MAGHRIB = "maghrib";
	static final String KEY_ISHA = "isha";
	static final String KEY_DATE = "date";
	static final String KEY_HIJRI = "hijri";
	static final String KEY_CITY = "city";
	static final String KEY_COUNTRY = "country";
	static final String KEY_WEBSITE = "website";

	private Button fajr;
	private Button dhuhr;
	private Button asr;
	private Button maghrib;
	private Button isha;
	private String hijri;
	private Button btn_retour;
	String city;
	String country;
	String date;
	String sunrise;
	String website;
	String h_fajr;
	String h_dhuhr;
	String h_asr;
	String h_maghrib;
	String h_isha;
	private TextToSpeech tts;
	private Vibrator vibration;
	MediaPlayer mp;
	Intent intent;
	BroadcastReceiver mReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heures_prieres);

		intent = getIntent(); 
		tts = new TextToSpeech(this,this);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		fajr = (Button)findViewById(R.id.btn_fajr);
		dhuhr= (Button)findViewById(R.id.btn_dhuhr);
		asr = (Button)findViewById(R.id.btn_asr);
		maghrib = (Button)findViewById(R.id.btn_maghrib);
		isha = (Button)findViewById(R.id.btn_isha);
		btn_retour = (Button)findViewById(R.id.btn_quitter);
		
		fajr.setOnClickListener(this);	fajr.setOnLongClickListener(this);
		dhuhr.setOnClickListener(this);	dhuhr.setOnLongClickListener(this);
		asr.setOnClickListener(this);	asr.setOnLongClickListener(this);
		maghrib.setOnClickListener(this);	maghrib.setOnLongClickListener(this);
		isha.setOnClickListener(this);	isha.setOnLongClickListener(this);
		btn_retour.setOnClickListener(this);	btn_retour.setOnLongClickListener(this);
		
		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromUrl(URL); // getting XML
		Document doc = parser.getDomElement(xml); // getting DOM element

		NodeList nl = doc.getElementsByTagName(KEY_PRAYER);
		// looping through all item nodes <prayer>
		for (int i = 0; i < nl.getLength(); i++) {
			
			Element e = (Element) nl.item(i);
			fajr.setText(fajr.getText().toString()+" "+parser.getValue(e, KEY_FAJR));
			h_fajr = parser.getValue(e, KEY_FAJR);
			sunrise = parser.getValue(e, KEY_SUNRISE);
			dhuhr.setText(dhuhr.getText().toString()+" "+parser.getValue(e, KEY_DHUHR));
			h_dhuhr = parser.getValue(e, KEY_DHUHR);
			asr.setText(asr.getText().toString()+" "+parser.getValue(e, KEY_ASR));
			h_asr = parser.getValue(e, KEY_ASR);
			maghrib.setText(maghrib.getText().toString()+" "+parser.getValue(e, KEY_MAGHRIB));
			h_maghrib = parser.getValue(e, KEY_MAGHRIB);
			isha.setText(isha.getText().toString()+" "+parser.getValue(e, KEY_ISHA));
			h_isha = parser.getValue(e, KEY_ISHA);
			date = parser.getValue(e, KEY_DATE);
			hijri = parser.getValue(e, KEY_HIJRI);
			city = parser.getValue(e, KEY_CITY);
			country = parser.getValue(e, KEY_COUNTRY);
			website = parser.getValue(e, KEY_WEBSITE);
				
		}

		
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fajr:
			vibration.vibrate(100);
			speakOut(h_fajr+" minutes");
			break;

		case R.id.btn_dhuhr:
			vibration.vibrate(100);
			speakOut(h_dhuhr+" minutes");
			break;
			
		case R.id.btn_asr:
			vibration.vibrate(100);
			speakOut(h_asr+" minutes");
			break;
			
		case R.id.btn_maghrib:
			vibration.vibrate(100);
			speakOut(h_maghrib+" minutes");
			break;
			
		case R.id.btn_isha:
			vibration.vibrate(100);
			speakOut(h_isha+" minutes");
			break;
			
		case R.id.btn_quitter:
			vibration.vibrate(100);
			finish();
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fajr:
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.fajr);
			mp.start();
			break;

		case R.id.btn_dhuhr:
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.dhuhr);
			mp.start();
			break;
			
		case R.id.btn_asr:
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.asr);
			mp.start();
			break;
			
		case R.id.btn_maghrib:
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.maghrib);
			mp.start();
			break;
			
		case R.id.btn_isha:
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.isha);
			mp.start();
			break;
			
		case R.id.btn_quitter:
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
			//speakOut("Les heures des prières pour "+city+", date, "+hijri);
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.heures_prieres);
			mp.start();
		
		} else {
			Log.e("TTS", "Initilization Failed!");

		}
		

	}

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
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
			//speakOut("Les heures des prières pour "+city+", date, "+hijri);
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.heures_prieres);
			mp.start();
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(this, R.raw.heures_prieres);
			mp.start();
		}
		super.onResume();
	}
}