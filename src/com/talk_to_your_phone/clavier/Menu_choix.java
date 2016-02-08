package com.talk_to_your_phone.clavier;



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
import android.widget.TextView;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.Brailler;

public class Menu_choix extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener {

	private TextToSpeech tts;
	private Button btn_ajouter;
	private Button btn_retour;
	private TextView num;
	public String dialedNumber;
	public String numero;
	private static final int CODE_ACTIVITE = 102;
	Intent i;
	Vibrator vibration;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.clavier_menu);
		// Pour cacher la barre de statut et donc mettre votre application en plein écran
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		i = getIntent();
		numero = i.getStringExtra("num");
		dialedNumber = "tel:" + i.getStringExtra("num");
		Log.i("telephone", dialedNumber);

		num = (TextView)findViewById(R.id.num);
		num.setText(num.getText().toString()+" "+numero);
		
		btn_ajouter = (Button) findViewById(R.id.button_ajouter);
		btn_retour = (Button) findViewById(R.id.button_retour);

		tts = new TextToSpeech(this,this);

		num.setOnClickListener(this);
		btn_ajouter.setOnClickListener(this);	btn_ajouter.setOnLongClickListener(this);
		btn_retour.setOnClickListener(this);	btn_retour.setOnLongClickListener(this);

		 // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


	}


	@Override
	public boolean onLongClick(View view) {

		switch(view.getId()){

		case(R.id.button_ajouter):
			vibration.vibrate(100);
			Intent it_Braille = new Intent(this,Brailler.class);
			it_Braille.putExtra("source", "clavier");
			it_Braille.putExtra("num", numero);
			startActivity(it_Braille);
			finish();
			
		break;

		case(R.id.button_retour):
			setResult(1001);
			finish();
			break;
		}
		
		return true;
	}




	@Override
	public void onClick(View view) {

		switch(view.getId()){

		case(R.id.num):
			speakOut(numero);

		break;

		case(R.id.button_ajouter):
			speakOut("Ajouter au contacts");
		break;

		case(R.id.button_retour):
			speakOut("Retour au clavier");
		break;
		}

	}


	@Override
	public void onDestroy() {
		// Don't forget to shutdown tts!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 
			speakOut("Effectuer votre choix");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	private void speakOut(String text) {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}


	@Override
	protected void onPause() {
		// WHEN THE SCREEN IS ABOUT TO TURN OFF
		if (ScreenReceiver.wasScreenOn) {
			// THIS IS THE CASE WHEN ONPAUSE() IS CALLED BY THE SYSTEM DUE TO A SCREEN STATE CHANGE
			finish();
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
		}
		super.onResume();
	}

	

	
	

}
