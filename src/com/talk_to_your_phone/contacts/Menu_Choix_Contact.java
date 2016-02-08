package com.talk_to_your_phone.contacts;

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

public class Menu_Choix_Contact extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{

	private Button btn_appeler;
	private Button btn_envoyer_sms;
	private Button btn_modifier;
	private Button btn_supprimer;
	private Button btn_quitter;
	private TextToSpeech tts;
	private Vibrator vibration;
	Intent i;
	String nom;
	String num;
	String contact_ID;
	BroadcastReceiver mReceiver;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_menu_choix);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		tts= new TextToSpeech(this,this);
		
		btn_appeler = (Button)findViewById(R.id.btn_appeler);
		btn_envoyer_sms = (Button)findViewById(R.id.btn_env_SMS);
		btn_modifier = (Button)findViewById(R.id.btn_modifier);
		btn_supprimer = (Button)findViewById(R.id.btn_supprimer);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		btn_appeler.setOnClickListener(this); btn_appeler.setOnLongClickListener(this);
		btn_envoyer_sms.setOnClickListener(this); btn_envoyer_sms.setOnLongClickListener(this);
		btn_modifier.setOnClickListener(this); btn_modifier.setOnLongClickListener(this);
		btn_supprimer.setOnClickListener(this); btn_supprimer.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this); btn_quitter.setOnLongClickListener(this);
		
		i = getIntent();
		contact_ID = i.getStringExtra("Contact_id");
		nom = i.getStringExtra("nom");
		num = i.getStringExtra("num");
	
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
		
		case (R.id.btn_appeler):
			
			String dialedNumber = "tel:" + num;
			speakOut("appel en cours vers "+nom);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Appeler(dialedNumber);
			finish();
			break;
			
		case (R.id.btn_modifier):
			vibration.vibrate(100);
			Intent it = new Intent (Menu_Choix_Contact.this,Modifier_Contact.class);
			it.putExtra("id", contact_ID);
			it.putExtra("nom", nom);
			it.putExtra("num", num);
			startActivity(it);
			
			break;
			
		case (R.id.btn_env_SMS):
			vibration.vibrate(100);
			Intent int_message = new Intent(Menu_Choix_Contact.this, Brailler.class);
			int_message.putExtra("source", "Envoi_SMS");
			int_message.putExtra("tel", num);
			startActivity(int_message);		
			break;
			
		case (R.id.btn_supprimer):
			vibration.vibrate(100);
			Log.i("id",contact_ID);
			if(delete(getApplicationContext(),contact_ID)){
				speakOut("Contact supprimé avec succé");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				
					e.printStackTrace();
				}
				finish();
				break;
			}
			
			else{
				speakOut("Contact non supprimé");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				finish();
				break;
			}
				
			
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
		case R.id.btn_appeler:
			speakOut("Appeler");
			break;
		
		case R.id.btn_env_SMS:
			speakOut("Envoyer un SMS");
			break;
			
		case R.id.btn_modifier:
			speakOut("Modifier");
			break;
			
		case R.id.btn_supprimer:
			speakOut("Supprimer");
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
			
			speakOut("Effectuer un choix pour "+nom);

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
		
	}
	
	public void Appeler(String telUri){
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telUri));
		startActivity(intent);
	}
	
	public static boolean Supprimer_Contact(Context ctx,String tel, String name) {
		Uri ContactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(tel));
		
		Cursor cur = ctx.getContentResolver().query(ContactUri, null, null, null, null);
		try {
			if (cur.moveToFirst()) {
				do {
					if (cur.getString(cur.getColumnIndex(PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
						String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
						Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
						System.out.println(uri);
						ctx.getContentResolver().delete(uri, null, null);
						return true;
					}
					
				} while (cur.moveToNext());
				
			}

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		return false;
	}
	

	public boolean delete(Context ctx, String id){
		ArrayList ops = new ArrayList(); String[] args = new String[] {id}; 
		ops.add(ContentProviderOperation.newDelete(RawContacts.CONTENT_URI).withSelection(RawContacts.CONTACT_ID + "=?", args) .build());
		try {
			ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		} catch (OperationApplicationException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public void update(int Id, String nom, String numtel)
	{       

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		// Mise à jour du nom du contact:

		Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);

		builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " 
				+ ContactsContract.Data.MIMETYPE + "=?", 
				new String[]{String.valueOf(Id), 
				ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE});

		builder.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, nom);
		ops.add(builder.build());

		// Mise à jour du numero de tel du contact:

		builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);

		builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " 
				+ ContactsContract.Data.MIMETYPE + "=?"+ 
				" AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?",
				new String[]{String.valueOf(Id), 
				ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, 
				String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME)});
		builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, numtel);
		ops.add(builder.build());

		// Mise à jour du contact
		try
		{
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}



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
			speakOut("Effectuer un choix pour "+nom);
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Effectuer un choix pour "+nom);
		}
		super.onResume();
	}


}
