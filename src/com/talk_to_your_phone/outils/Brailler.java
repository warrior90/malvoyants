package com.talk_to_your_phone.outils;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract.*;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Data.*;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.messagerie.Choix_Destinataire;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.TremblementDetection.TremblementListener;






public class Brailler extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{

	private TremblementDetection tremblementDetection;
	private TextToSpeech tts;
	private static final long[] PATTERN = {0, 1, 40, 41};
	private boolean dot1;
	private boolean dot2;
	private boolean dot3;
	private boolean dot4;
	private boolean dot5;
	private boolean dot6;
	private Vibrator vibe;
	private TextView tv;
	private String numero;
	private Button speaker;
	private Button effacer;
	private Button valider;
	Canvas cv;
	Vibrator vibration;
	Intent i;
	String source;
	BroadcastReceiver mReceiver;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_brailler);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		tts = new TextToSpeech(this,this);
		vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		dot1 = false;
		dot2 = false;
		dot3 = false;
		dot4 = false;
		dot5 = false;
		dot6 = false;
		
		tv =(TextView)findViewById(R.id.tvBraille);
		
		
		speaker = (Button)findViewById(R.id.btn_speak);
		valider = (Button)findViewById(R.id.btn_valider);
		effacer = (Button)findViewById(R.id.btn_effacer);
		
		speaker.setOnClickListener(this); speaker.setOnLongClickListener(this);
		effacer.setOnClickListener(this); effacer.setOnLongClickListener(this);
		valider.setOnClickListener(this); valider.setOnLongClickListener(this);
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		i=getIntent();
		source = i.getStringExtra("source");
		
		
		tremblementDetection = new TremblementDetection(getApplicationContext(), new TremblementListener(){
			public void onTremblementDetected() {
				
				
					if(!tv.getText().toString().equals("")){
						String st =tv.getText().toString();
						if(st.indexOf(" ") == -1)
						{
							tv.setText("");
							speakOut("1 mot éffacé");

						}
						else{

							st = tv.getText().toString().substring(0, st.lastIndexOf(" "));
							Log.i("st",st);
							tv.setText(st);
							speakOut("1 mot éffacé");
						}
					}
				}
			   
		});
		
		
	}
	
	
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		Display display = getWindowManager().getDefaultDisplay();
		// Ignore any events that involve the user going off the edge of the screen
		int edgeFlags = event.getEdgeFlags();
		
		if ((edgeFlags == MotionEvent.EDGE_BOTTOM) || (edgeFlags == MotionEvent.EDGE_LEFT)
				|| (edgeFlags == MotionEvent.EDGE_RIGHT) || (edgeFlags == MotionEvent.EDGE_TOP)) {
			vibe.vibrate(PATTERN, -1);
			return true;
		}
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			processInput();
			break;
		default:
			float x = event.getX();
			float y = event.getY();
			if (x < (display.getWidth() * .4)) {
				if (y < (display.getHeight() * .25)) {
					if (!dot1) {
						speakOut("");
						vibe.vibrate(PATTERN, -1);
					}
					
					dot1 = true;
				} else if (y < (display.getHeight() * .375)) {
					//Nothing - alley
				} else if (y < (display.getHeight() * .625)) {
					if (!dot2) {
						speakOut("");
						vibe.vibrate(PATTERN, -1);
					}
					
					dot2 = true;
				} else if (y < (display.getHeight() * .75)) {
					//Nothing - alley
				} else {
					if (!dot3) {
						speakOut("");
						vibe.vibrate(PATTERN, -1);
					}
					
					dot3 = true;
				}
			} else if (x < (display.getWidth() * .6)) {
				//Nothing - alley
			} else {
				if (y < (display.getHeight() * .25)) {
					if (!dot4) {
						speakOut("");
						vibe.vibrate(PATTERN, -1);
					}
					
					dot4 = true;
				} else if (y < (display.getHeight() * .375)) {
					//Nothing - alley
				} else if (y < (display.getHeight() * .625)) {
					if (!dot5) {
						speakOut("");
						vibe.vibrate(PATTERN, -1);
					}
				
					dot5 = true;
				} else if (y < (display.getHeight() * .75)) {
					//Nothing - alley
				} else {
					if (!dot6) {
						speakOut("");
						vibe.vibrate(PATTERN, -1);
					}
					dot6 = true;
				}
			}
			break;
		}
		return true;
	}


	private void processInput() { 
		if(letterFromBraille().equals(" ")){
			speakOut("éspace");
		}
		speakOut(letterFromBraille());
		tv.setText(tv.getText().toString()+""+letterFromBraille());
		dot1 = false;
		dot2 = false;
		dot3 = false;
		dot4 = false;
		dot5 = false;
		dot6 = false;
	}

	private String letterFromBraille() {
		if ( dot1 && !dot4 && 
				!dot2 && !dot5 && 
				!dot3 && !dot6 ) {
			return "a";
		}
		if (dot1 && !dot4 && dot2 && !dot5 && !dot3 && !dot6) {
			return "b";
		}
		if (dot1 && dot4 && !dot2 && !dot5 && !dot3 && !dot6) {
			return "c";
		}
		if (dot1 && dot4 && !dot2 && dot5 && !dot3 && !dot6) {
			return "d";
		}
		if (dot1 && !dot4 && !dot2 && dot5 && !dot3 && !dot6) {
			return "e";
		}
		if (dot1 && dot4 && dot2 && !dot5 && !dot3 && !dot6) {
			return "f";
		}
		if (dot1 && dot4 && dot2 && dot5 && !dot3 && !dot6) {
			return "g";
		}
		if (dot1 && !dot4 && dot2 && dot5 && !dot3 && !dot6) {
			return "h";
		}
		if (!dot1 && dot4 && dot2 && !dot5 && !dot3 && !dot6) {
			return "i";
		}
		if (!dot1 && dot4 && dot2 && dot5 && !dot3 && !dot6) {
			return "j";
		}
		if (dot1 && !dot4 && !dot2 && !dot5 && dot3 && !dot6) {
			return "k";
		}

		if ( dot1 && !dot4 && 
				dot2 && !dot5 && 
				dot3 && !dot6 ) {
			return "l";
		}
		if ( dot1 &&  dot4 && 
				!dot2 && !dot5 && 
				dot3 && !dot6 ) {
			return "m";
		}
		if ( dot1 &&  dot4 && 
				!dot2 &&  dot5 && 
				dot3 && !dot6 ) {
			return "n";
		}
		if ( dot1 && !dot4 && 
				!dot2 &&  dot5 && 
				dot3 && !dot6 ) {
			return "o";
		}
		if ( dot1 &&  dot4 && 
				dot2 && !dot5 && 
				dot3 && !dot6 ) {
			return "p";
		}    
		if ( dot1 &&  dot4 && 
				dot2 &&  dot5 && 
				dot3 && !dot6 ) {
			return "q";
		}
		if ( dot1 && !dot4 && 
				dot2 &&  dot5 && 
				dot3 && !dot6 ) {
			return "r";
		}
		if (!dot1 &&  dot4 && 
				dot2 && !dot5 && 
				dot3 && !dot6 ) {
			return "s";
		}
		if (!dot1 &&  dot4 && 
				dot2 &&  dot5 && 
				dot3 && !dot6 ) {
			return "t";
		}



		if ( dot1 && !dot4 && 
				!dot2 && !dot5 && 
				dot3 &&  dot6 ) {
			return "u";
		}    
		if ( dot1 && !dot4 && 
				dot2 && !dot5 && 
				dot3 &&  dot6 ) {
			return "v";
		}   
		if (!dot1 &&  dot4 && 
				dot2 &&  dot5 && 
				!dot3 &&  dot6 ) {
			return "w";
		}   
		if ( dot1 &&  dot4 && 
				!dot2 && !dot5 && 
				dot3 &&  dot6 ) {
			return "x";
		}   
		if ( dot1 &&  dot4 && 
				!dot2 &&  dot5 && 
				dot3 &&  dot6 ) {
			return "y";
		}   
		if ( dot1 && !dot4 && 
				!dot2 &&  dot5 && 
				dot3 &&  dot6 ) {
			return "z";
		}   
		
		if ( !dot1 && !dot4 && 
				!dot2 &&  !dot5 && 
				!dot3 &&  dot6 ) {
			return " ";
		}   

		return "";
	}




	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

			speakOut("clavier Braille");
			

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

		
	}
	

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}



	@Override
	public boolean onLongClick(View v) {
		String s = tv.getText().toString();

		switch(v.getId()){
		case (R.id.btn_speak):
			if(tv.getText().toString().equals("")){
				speakOut("pas de saisi");
				break;
			}
			
			vibration.vibrate(100);
			speakOut(s);

			break;
			
		case (R.id.btn_effacer):
			if(tv.length()==0){
				speakOut("pas de saisi");
				break;
			}
			tv.setText(tv.getText().toString().substring(0, tv.length() - 1));
			vibration.vibrate(100);
			break;
			
			
		case (R.id.btn_valider):
			vibration.vibrate(100);
			shutdown();
			
			if(source.equals("contact_filtre")){
				vibration.vibrate(100);
				Intent int_ret = new Intent();
				int_ret.putExtra("text", tv.getText().toString());
				setResult(RESULT_OK,int_ret);
				finish();
				break;
			
			}
			
			if(source.equals("nouveau msg text")){
				
				Intent int_dest = new Intent(Brailler.this,Choix_Destinataire.class);
				int_dest.putExtra("source", "nv_message");
				int_dest.putExtra("text_msg", s);
				startActivity(int_dest);
				finish();
				break;
			
			}
			
			if(source.equals("Envoi_SMS")){
				String tel = i.getStringExtra("tel");
				try{
					Envoi_SMS(tel, s);
					speakOut("Message envoyé");
					Thread.sleep(2000);

				}catch(Exception ex){
					speakOut("Erreur d'envoi");
				}
				finish();
				break;
			
			}
			
			if(source.equals("clavier")){
				numero = i.getStringExtra("num");
				Ajout_contact(s,numero);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				speakOut("Contact ajouté");
				finish();
				break;
			}
			
			
			if(source.equals("modifier nom")){
				
				int id = Integer.parseInt(i.getStringExtra("id"));
				String nom_contact = tv.getText().toString();
				if(MAJ_nom(id, nom_contact)){
					speakOut("modifié avec succé");
					finish();
					break;
				}
				else{
					speakOut("erreur");
					finish();
					break;
				}
			}
				
			if(source.equals("nouveau contact nom")){

				Intent int_clavier_chiffre = new Intent(this,Clavier.class);
				int_clavier_chiffre.putExtra("source","nouveau contact numero");
				int_clavier_chiffre.putExtra("text", s);
				startActivity(int_clavier_chiffre);
				finish();
				break;
			}
			
			
		}
	
		return true;
	}


	public void Envoi_SMS(String tel, String message) throws Exception{

	    SmsManager smsManager = SmsManager.getDefault();
	    smsManager.sendTextMessage(tel, null, message, null, null);
	}
	

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case (R.id.btn_speak):
			speakOut("Lire le text");
			break;
			
		case (R.id.btn_effacer):
				speakOut("éffacer");
				break;
				
		case (R.id.btn_valider):
			speakOut("Valider");
			break;
			
		}
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
	
	
	
	public boolean MAJ_nom(int Id, String nom)
    {       
        /*int id = 1;
        String firstname = "Contact's first name";
        String lastname = "Last name";
        String number = "000 000 000";
        */

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Mise à jour du nom du contact:
         
        Builder builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
        
        builder.withSelection(ContactsContract.Data.CONTACT_ID + "=?" + " AND " 
        + ContactsContract.Data.MIMETYPE + "=?", 
        new String[]{String.valueOf(Id), 
        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE});
        
        //builder.withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, lastname);
        builder.withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, nom);
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
 
	public void shutdown(){
		tremblementDetection.shutdown();
	}
	
	@Override
	public void onDestroy() {
	
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		unregisterReceiver(mReceiver);
		shutdown();
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
			speakOut("clavier Braille");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("clavier Braille");
		}
		super.onResume();
	}
	
	

}
