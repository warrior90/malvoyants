package com.talk_to_your_phone.messagerie;

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

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.Clavier;

public class Choix_Destinataire extends Activity implements OnLongClickListener, OnClickListener, TextToSpeech.OnInitListener{
	
	private Button btn_choix_contact;
	private Button btn_num_dest;
	private Button btn_quitter;
	private TextToSpeech tts;
	private Vibrator vibration;
	Intent intent;
	String source;
	BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choix_destinataire);
		
		tts = new TextToSpeech(this,this);
		
		btn_choix_contact = (Button)findViewById(R.id.btn_choix_contact);
		btn_num_dest = (Button)findViewById(R.id.btn_num_dest);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		btn_choix_contact.setOnClickListener(this);	btn_choix_contact.setOnLongClickListener(this);
		btn_num_dest.setOnClickListener(this);	btn_num_dest.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this);	btn_quitter.setOnLongClickListener(this);
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);	
		
		intent = getIntent();
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		source = intent.getStringExtra("source");
	}

	@Override
	public void onClick(View v) {
		
		 switch(v.getId()){

	        case(R.id.btn_choix_contact):
	            speakOut("Choisir un contact");
	        break;

	        case(R.id.btn_num_dest):
	            speakOut("Taper le numéro de destinataire");
	        break;
	        
	        case(R.id.btn_quitter):
	            speakOut("Quitter");
	        break;
	        }
		
	}

	@Override
	public boolean onLongClick(View v) {
		String text_msg = intent.getStringExtra("text_msg");
		
		 switch(v.getId()){

	        case(R.id.btn_choix_contact):
	            vibration.vibrate(100);
	        	
	        	Intent int_contact = new Intent(this,Choix_Contact.class);
	        	int_contact.putExtra("text", text_msg);
	        	startActivity(int_contact);
	        	finish();
	        	
	        break;

	        case(R.id.btn_num_dest):
	        	
	        if(source.equals("nv_message") || source.equals("Dicter SMS")){
	        	vibration.vibrate(100);
	        	Intent int_num_dest = new Intent(this,Clavier.class);
	        	int_num_dest.putExtra("source", "choix num dest");
	        	int_num_dest.putExtra("msg",text_msg);
	        	startActivity(int_num_dest);
	        	finish();
	        	break;
	        }
	           
	        break;
	        
	        case(R.id.btn_quitter):
	            vibration.vibrate(100);
	        	finish();
	        break;
	        }
		
		return false;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

			speakOut("Choix destinataire" );
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
			speakOut("Choix destinataire" );
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Choix destinataire" );
		}
		super.onResume();
	}
	

}
