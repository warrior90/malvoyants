package com.talk_to_your_phone.clavier;

import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.TremblementDetection;
import com.talk_to_your_phone.outils.TremblementDetection.TremblementListener;

public class Clavier extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener, OnTouchListener{
	
	private TremblementDetection tremblementDetection;
	private TextToSpeech tts;
	private TextView numeros;
	private Button btn_1;
	private Button btn_2;
	private Button btn_3;
	private Button btn_4;
	private Button btn_5;
	private Button btn_6;
	private Button btn_7;
	private Button btn_8;
	private Button btn_9;
	private Button btn_0;
	private Button btn_etoile;
	private Button btn_dieze;
	private Button btn_plus;
	private Button btn_effacer;
	private Button btn_appeler;
	private static final int CODE_RETOUR = 101;
	Vibrator vibration;
	Intent it;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clavier_main);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		numeros = (TextView)findViewById(com.project.talk_to_your_phone.R.id.textView1);
		numeros.setText("");
		
		it = getIntent();
	
		 // INITIALIZE RECEIVER
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);
		
		btn_1 = (Button)findViewById(R.id.button1);
		btn_2 = (Button)findViewById(R.id.button2);
		btn_3 = (Button)findViewById(R.id.button3);
		btn_4 = (Button)findViewById(R.id.button4);
		btn_5 = (Button)findViewById(R.id.button5);
		btn_6 = (Button)findViewById(R.id.button6);
		btn_7 = (Button)findViewById(R.id.button7);
		btn_8 = (Button)findViewById(R.id.button8);
		btn_9 = (Button)findViewById(R.id.button9);
		btn_0 = (Button)findViewById(R.id.button11);
		btn_etoile = (Button)findViewById(R.id.button_etoile);
		btn_dieze = (Button)findViewById(R.id.button_dieze);
		btn_plus = (Button)findViewById(R.id.button_plus);
		btn_appeler = (Button)findViewById(R.id.button_appeler);
		btn_effacer = (Button)findViewById(R.id.button_effacer);
		
		
		tts = new TextToSpeech(this,this);

		btn_1.setOnLongClickListener(this);btn_1.setOnClickListener(this);
		btn_2.setOnLongClickListener(this); btn_2.setOnClickListener(this);
		btn_3.setOnLongClickListener(this); btn_3.setOnClickListener(this);
		btn_4.setOnLongClickListener(this); btn_4.setOnClickListener(this);
		btn_5.setOnLongClickListener(this); btn_5.setOnClickListener(this);
		btn_6.setOnLongClickListener(this); btn_6.setOnClickListener(this);
		btn_7.setOnLongClickListener(this); btn_7.setOnClickListener(this);
		btn_8.setOnLongClickListener(this); btn_8.setOnClickListener(this);
		btn_9.setOnLongClickListener(this); btn_9.setOnClickListener(this);
		btn_0.setOnLongClickListener(this); btn_0.setOnClickListener(this);
		btn_etoile.setOnLongClickListener(this); btn_etoile.setOnClickListener(this);
		btn_dieze.setOnLongClickListener(this); btn_dieze.setOnClickListener(this);
		btn_plus.setOnLongClickListener(this); btn_plus.setOnClickListener(this);
		btn_appeler.setOnLongClickListener(this); btn_appeler.setOnClickListener(this);
		btn_effacer.setOnLongClickListener(this); btn_effacer.setOnClickListener(this);
		numeros.setOnTouchListener(this);
		
		
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
        
		
		tremblementDetection = new TremblementDetection(getApplicationContext(), new TremblementListener(){
			public void onTremblementDetected() {
				if(!numeros.getText().toString().equals("")){
					final Intent it = new Intent(Clavier.this,Menu_choix.class);
					it.putExtra("num", numeros.getText().toString());
					startActivityForResult(it, CODE_RETOUR);
					
				}
				finish();
				
			}      
		});
		

		
	}

	public void onClick(View v){

		switch(v.getId()){

		case(R.id.button1):
			speakOut("1");
		break;

		case(R.id.button2):
			speakOut("2");
		break;

		case(R.id.button3):
			speakOut("3");
		break;

		case(R.id.button4):
			speakOut("4");
		break;
		
		case(R.id.button5):
			speakOut("5");
		break;

		case(R.id.button6):
			speakOut("6");
		break;

		case(R.id.button7):
			speakOut("7");
		break;

		case(R.id.button8):
			speakOut("8");
		break;

		case(R.id.button9):
			speakOut("9");
		break;

		case(R.id.button11):
			speakOut("0");
		break;

		case(R.id.button_etoile):
			speakOut("étoile");
		break;

		case(R.id.button_appeler):
			speakOut("appeler");
		break;
		
		case(R.id.button_plus):
			speakOut("+");
		break;
		
		case(R.id.button_dieze):
			speakOut("dièze");
		break;
		
		case(R.id.button_effacer):
			speakOut("éffacer");
		break;

		
		}
		

	}
	
	


	@Override
	public boolean onLongClick(View v) {

		switch(v.getId()){

		case(R.id.button1):
			
			numeros.setText(numeros.getText().toString()+"1");
			speakOut("1");
			vibration.vibrate(100);
		break;

		case(R.id.button2):
			numeros.setText(numeros.getText().toString()+"2");
			speakOut("2");
			vibration.vibrate(100);
		break;

		case(R.id.button3):
			numeros.setText(numeros.getText().toString()+"3");
		speakOut("3");
			vibration.vibrate(100);
			break;

		case(R.id.button4):
			numeros.setText(numeros.getText().toString()+"4");
		speakOut("4");
			vibration.vibrate(100);
			break;
			
		case(R.id.button5):
			numeros.setText(numeros.getText().toString()+"5");
		speakOut("5");
			vibration.vibrate(100);
			break;

		case(R.id.button6):
			numeros.setText(numeros.getText().toString()+"6");
		speakOut("6");
			vibration.vibrate(100);
			break;

		case(R.id.button7):
			numeros.setText(numeros.getText().toString()+"7");
		speakOut("7");
			vibration.vibrate(100);
			break;

		case(R.id.button8):
			numeros.setText(numeros.getText().toString()+"8");
		speakOut("8");
			vibration.vibrate(100);
			break;

		case(R.id.button9):
			numeros.setText(numeros.getText().toString()+"9");
		speakOut("9");
			vibration.vibrate(100);
			break;

		case(R.id.button11):
			numeros.setText(numeros.getText().toString()+"0");
		speakOut("0");
			vibration.vibrate(100);
			break;

		case(R.id.button_etoile):
			numeros.setText(numeros.getText().toString()+"*");
		speakOut("étoile");
			vibration.vibrate(100);
			break;

		case(R.id.button_dieze):
			numeros.setText(numeros.getText().toString()+"#");
		speakOut("dièze");
			vibration.vibrate(100);
			break;
			
		case(R.id.button_plus):
			numeros.setText(numeros.getText().toString()+"+");
		speakOut("+");
			vibration.vibrate(100);
			break;
			
		case(R.id.button_effacer):
			if(numeros.length()>0){
				numeros.setText("");
				speakOut("numéro éffacé");
				vibration.vibrate(100);
				break;
				}
			break;
			
		case(R.id.button_appeler):

			if(numeros.length()>0){
		
				String dialedNumber = "tel:" + numeros.getText().toString();
				speakOut("appel vers "+numeros.getText().toString());
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Appeler(dialedNumber);
				//setResult(1000);
				finish();
				break;
				}
			break;

		}

		return true;
	}

	public void Appeler(String telUri){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telUri));
		startActivity(intent);
	}

	public void shutdown(){
		tremblementDetection.shutdown();
	}
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 
			
			speakOut("clavier");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == CODE_RETOUR) {
			
			if(resultCode==1001){
				numeros.setText("");
			}
			
		}
			
	}
		

	
	@Override
	public void onDestroy() {
	
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		
		shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		switch (v.getId()){
		case R.id.textView1 :
			if(numeros.getText().toString().equals("")){
				speakOut("numéro vide");
				break;
			}
			speakOut(numeros.getText().toString());
			break;
		}
		return false;
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
			speakOut("Clavier");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Clavier");
		}
		super.onResume();
	}
	
	
}
