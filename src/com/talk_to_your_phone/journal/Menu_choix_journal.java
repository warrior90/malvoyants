package com.talk_to_your_phone.journal;

import java.util.ArrayList;
import java.util.Locale;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.Brailler;
import com.talk_to_your_phone.outils.TremblementDetection;
import com.talk_to_your_phone.outils.TremblementDetection.TremblementListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.Vibrator;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.speech.tts.TextToSpeech;
import android.text.GetChars;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;

public class Menu_choix_journal extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{

	private Button btn_appeler;
	private Button btn_supp;
	private Button btn_retour;
	private TextToSpeech tts;
	private Vibrator vibration;
	Intent i;
	String num;
	BroadcastReceiver mReceiver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_choix_journal);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		tts= new TextToSpeech(this,this);

		btn_appeler = (Button)findViewById(R.id.btn_appeler);
		btn_supp = (Button)findViewById(R.id.btn_supp);
		btn_retour = (Button)findViewById(R.id.btn_retour);


		btn_appeler.setOnClickListener(this); btn_appeler.setOnLongClickListener(this);
		btn_supp.setOnClickListener(this); btn_supp.setOnLongClickListener(this);
		btn_retour.setOnClickListener(this); btn_retour.setOnLongClickListener(this);


		i = getIntent();
		num = i.getStringExtra("num");


	}

	@Override
	public boolean onLongClick(View v) {

		switch(v.getId()){

		case (R.id.btn_appeler):
			vibration.vibrate(100);
			String dialedNumber = "tel:" + num;
			speakOut("appel en cours");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Appeler(dialedNumber);
			finish();
			break;

		case (R.id.btn_supp):
			vibration.vibrate(100);
			if (DeleteCallLogByNumber(num)){
				speakOut("numéro supprimé du journal");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
				break;
			}
			else{
				speakOut("erreur");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finish();
				break;
			}


		case (R.id.btn_retour):
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
		case R.id.btn_appeler:
			speakOut("Appeler");
			break;

		case R.id.btn_supp:
			speakOut("Supprimer du journal");
			break;

		case R.id.btn_retour:
			speakOut("retour");
			break;
		}

	}

	public boolean DeleteCallLogByNumber(String number) {   
		String queryString = "NUMBER='"+number+"'";
		
		try{
			
			this.getContentResolver().delete(CallLog.Calls.CONTENT_URI,queryString,null);
			return true;
			
		}catch (Exception e){
			
			e.printStackTrace();
			return false;
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

			speakOut("Effectuer un choix");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	public void Appeler(String telUri){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telUri));
		startActivity(intent);
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
			speakOut("Effectuer un choix");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Effectuer un choix");
		}
		super.onResume();
	}
	

}