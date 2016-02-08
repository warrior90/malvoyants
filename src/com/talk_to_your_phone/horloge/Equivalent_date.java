package com.talk_to_your_phone.horloge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Equivalent_date extends Activity implements OnLongClickListener, OnClickListener, TextToSpeech.OnInitListener{
	
	private TextView tv;
	private Button btn_0;
	private Button btn_1;
	private Button btn_2;
	private Button btn_3;
	private Button btn_4;
	private Button btn_5;
	private Button btn_6;
	private Button btn_7;
	private Button btn_8;
	private Button btn_9;
	private Button btn_del;
	private Button btn_ok;
	private Button btn_quitter;
	private TextToSpeech tts;
	private Vibrator vibration;
	Intent intent;
	BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.equivalent_date);
		
		intent= getIntent();
		tts = new TextToSpeech(this,this);
		tv = (TextView)findViewById(R.id.textView1);
		btn_0 = (Button)findViewById(R.id.btn_0);
		btn_1 =	(Button)findViewById(R.id.btn_1);
		btn_2 = (Button)findViewById(R.id.btn_2);
		btn_3 = (Button)findViewById(R.id.btn_3);
		btn_4 = (Button)findViewById(R.id.btn_4);
		btn_5 = (Button)findViewById(R.id.btn_5);
		btn_6 = (Button)findViewById(R.id.btn_6);
		btn_7 = (Button)findViewById(R.id.btn_7);
		btn_8 = (Button)findViewById(R.id.btn_8);
		btn_9 = (Button)findViewById(R.id.btn_9);
		btn_del = (Button)findViewById(R.id.btn_eff);
		btn_ok = (Button)findViewById(R.id.btn_ok);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		
		btn_0.setOnClickListener(this);	btn_0.setOnLongClickListener(this);
		btn_1.setOnClickListener(this);	btn_1.setOnLongClickListener(this);
		btn_2.setOnClickListener(this);	btn_2.setOnLongClickListener(this);
		btn_3.setOnClickListener(this);	btn_3.setOnLongClickListener(this);
		btn_4.setOnClickListener(this);	btn_4.setOnLongClickListener(this);
		btn_5.setOnClickListener(this);	btn_5.setOnLongClickListener(this);
		btn_6.setOnClickListener(this);	btn_6.setOnLongClickListener(this);
		btn_7.setOnClickListener(this);	btn_7.setOnLongClickListener(this);
		btn_8.setOnClickListener(this);	btn_8.setOnLongClickListener(this);
		btn_9.setOnClickListener(this);	btn_9.setOnLongClickListener(this);
		btn_del.setOnClickListener(this);	btn_del.setOnLongClickListener(this);
		btn_ok.setOnClickListener(this);	btn_ok.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this);	btn_quitter.setOnLongClickListener(this);
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.btn_0:
			speakOut("0");
			break;
		case R.id.btn_1:
			speakOut("1");
			break;
		case R.id.btn_2:
			speakOut("2");
			break;
		case R.id.btn_3:
			speakOut("3");
			break;
		case R.id.btn_4:
			speakOut("4");
			break;
		case R.id.btn_5:
			speakOut("5");
			break;
		case R.id.btn_6:
			speakOut("6");
			break;
		case R.id.btn_7:
			speakOut("7");
			break;
		case R.id.btn_8:
			speakOut("8");
			break;
		case R.id.btn_9:
			speakOut("9");
			break;
		case R.id.btn_eff:
			speakOut("éffacer");
			break;
		case R.id.btn_ok:
			speakOut("valider");
			break;
		case R.id.btn_quitter:
			speakOut("quitter");
			break;
		}
		
	}

	@Override
	public boolean onLongClick(View v) {
		
		String s = tv.getText().toString();
		switch(v.getId()){
		
		case R.id.btn_0:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"0");
				break;
			}
		case R.id.btn_1:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"1");
				break;
			}
		case R.id.btn_2:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"2");
				break;
			}
		case R.id.btn_3:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"3");
				break;
			}
		case R.id.btn_4:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"4");
				break;
			}
		case R.id.btn_5:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"5");
				break;
			}
		case R.id.btn_6:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"6");
				break;
			}
		case R.id.btn_7:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"7");
				break;
			}
		case R.id.btn_8:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"8");
				break;
			}
		case R.id.btn_9:
			if(s.length()<8){
				vibration.vibrate(100);
				tv.setText(s+"9");
				break;
			}
		case R.id.btn_eff:
			vibration.vibrate(100);
			tv.setText("");
			break;
			
		case R.id.btn_ok:
			vibration.vibrate(100);
			if(s.length()< 8){
				speakOut("erreur de saisi");
				break;
			}
			else{
				try {
					String jour = Date_equiv(s);
					SystemClock.sleep(1000);
					speakOut(jour+", est le jour équivalent");
					} catch (ParseException e) {

					e.printStackTrace();
				}
				break;
			}
		case R.id.btn_quitter:
			vibration.vibrate(100);
			finish();
			break;
		}
		return true;
	}

	private String Date_equiv(String s) throws ParseException {
		String j = s.substring(0, 2) ;
		String m = s.substring(2, 4);
		String a = s.substring(4, 8);

		String str = a+"/"+m+"/"+j;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date dte = sdf.parse(str);
		sdf = new SimpleDateFormat("EEEE");
	
		return sdf.format(dte);
	}

	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

			speakOut("Entrer la date sous la forme jours, mois, année");

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
			speakOut("Entrer la date sous la forme jours, mois, année");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Entrer la date sous la forme jours, mois, année");
		}
		super.onResume();
	}


}
