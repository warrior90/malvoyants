package com.talk_to_your_phone.outils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ContentProviderOperation.Builder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
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
	private Button btn_del;
	private Button btn_retour;
	private Button btn_ok;
	Vibrator vibration;
	Intent intent;
	String source;
	BroadcastReceiver mReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clavier);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		intent = getIntent();
		source = intent.getStringExtra("source");
		
		
		numeros = (TextView)findViewById(R.id.textView1);
		numeros.setText("");
		
		
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
		btn_del = (Button)findViewById(R.id.btn_del);
		btn_retour = (Button)findViewById(R.id.btn_retour);
		btn_plus = (Button)findViewById(R.id.btn_plus);
		btn_ok = (Button)findViewById(R.id.btn_ok);
		
		
		
		
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
		btn_del.setOnLongClickListener(this); btn_del.setOnClickListener(this);
		btn_retour.setOnLongClickListener(this); btn_retour.setOnClickListener(this);
		btn_ok.setOnLongClickListener(this); btn_ok.setOnClickListener(this);
		numeros.setOnTouchListener(this);
		
		
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
	
	}
	
	public void Envoi_SMS(String tel, String message) throws Exception{

	    SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage(tel, null, message, null, null);
	}

	protected void Ajout_contact(String nom, String tel){

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = ops.size();
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null)
				.build());
		ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, nom)
				.withValue(StructuredName.GIVEN_NAME, nom).build());
		
		ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, rawContactInsertIndex)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue(Phone.NUMBER, tel)
				.withValue(Phone.TYPE, Phone.TYPE_MOBILE)                
				.build());
		// For brevity, the try-catch statement is ignored.
		// Normally it's needed.
		try{
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		}catch(Exception e){
			e.printStackTrace();
		}
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

		case(R.id.btn_del):
			speakOut("éffacer");
		break;
		
		case(R.id.btn_plus):
			speakOut("+");
		break;
		
		case(R.id.button_dieze):
			speakOut("dièze");
		break;
		
		case(R.id.btn_retour):
			speakOut("Retour");
		break;
		
		case(R.id.btn_ok):
			speakOut("Valider");
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
			
		case(R.id.btn_plus):
			numeros.setText(numeros.getText().toString()+"+");
			speakOut("+");
			vibration.vibrate(100);
			break;
			
		case(R.id.btn_del):
			if(numeros.length()>0){
				numeros.setText("");
				speakOut("numéro éffacé");
				vibration.vibrate(100);
				break;
				}
			speakOut("pas de numéro saisie");
			break;
			
		case(R.id.btn_retour):

			vibration.vibrate(100);
			finish();
			break;
			
		case(R.id.btn_ok):
			
			vibration.vibrate(100);
			String tel = numeros.getText().toString();
			
			if(source.equals("choix num dest") && !tel.equals("")){
				String msg_text = intent.getStringExtra("msg");
				try{
					Envoi_SMS(tel, msg_text);
					speakOut("Message envoyé");
					Thread.sleep(3000);
					finish();
	
				}catch(Exception e){
					speakOut("Echec d'envoi");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					finish();
				}
				break;
			}
	
			if(source.equals("modifier tel") && !tel.equals("")){

				int id = Integer.parseInt(intent.getStringExtra("id"));
				String tel_contact = numeros.getText().toString();
				if(MAJ_tel(id, tel_contact)){
					speakOut("numéro modifié");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();
				}
				
				else{
					speakOut("numéro pas modifié");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					finish();
				}
				break;
			}
	
			if(source.equals("nouveau contact numero") && !tel.equals("")){
				String nom_contact = intent.getStringExtra("text");
				vibration.vibrate(100);
				Ajout_contact(nom_contact,tel);
				speakOut("Contact ajouté");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finish();
				break;
			}
	
			
			break;
	
		}

		return true;
	}



	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 
			
			speakOut("pavé numérique");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}
	}

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}


		
	@Override
	protected void onRestart() {
		speakOut("Pavé numérique");
		Log.i("état","onRestart()");
		super.onRestart();
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

	public boolean MAJ_tel(int Id, String numtel)
    {       
      

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

         
        Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);

        // Mise à jour du numero de tel du contact:
         
        builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
        
        builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " 
        + ContactsContract.Data.MIMETYPE + "=?"+ 
        " AND " + ContactsContract.CommonDataKinds.Organization.TYPE + "=?",
         new String[]{String.valueOf(Id), 
         ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, 
         String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)});
        builder.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, numtel);
        ops.add(builder.build());
        
        // Mise à jour du contact
        try
        {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
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
			speakOut("pavé numérique");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("pavé numérique");
		}
		super.onResume();
	}
	
}
