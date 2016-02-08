package com.talk_to_your_phone.horloge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;

public class Reveil extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{
	static final int ALARM_ID = 1234567;
	private static final int CODE_RETOUR = 101;
	static Alarm alarm;
	private TextToSpeech tts;
	private CheckBox heure;
	private Button btn_quitter;
	String heures;
	String minutes;
	Time t;
	private Vibrator vibration;
	BroadcastReceiver mReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Chargement des informations du reveil
		charger();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.reveil);

		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		btn_quitter.setOnClickListener(this);
		btn_quitter.setOnLongClickListener(this);
		
		//Affichage 
		affichage();

		//Planification
		planifierAlarm();

		tts = new TextToSpeech(this,this);

		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

	}
	
	/*
	 * Chargement des informations du reveil.
	 * Ici pour la sauvegarde on a simplement déserialiser l'objet Alarm.
	 */
	public void charger(){
		alarm = null;
		try {
			ObjectInputStream alarmOIS= new ObjectInputStream(openFileInput("alarm.serial"));
			alarm = (Alarm) alarmOIS.readObject(); 
			alarmOIS.close();
		}
		catch(FileNotFoundException fnfe){
			
			alarm = new Alarm();
			alarm.setActive(true);
			alarm.setheure("7");
			alarm.setminute("30");
			heures = "7";
			minutes = "30";
			
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		catch(ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}   

	
	private void affichage() {
		//Ici on a juste voulu créer un affichage de l'heure qui soit au format hh:mm.
		String heureReveil = "";
		
		heureReveil += Integer.parseInt(alarm.getheures()) >10 ? Integer.parseInt(alarm.getheures()) : "0" + Integer.parseInt(alarm.getheures());
		heureReveil +=":";
		heureReveil += Integer.parseInt(alarm.getminutes()) >10 ? Integer.parseInt(alarm.getminutes()) : "0" + Integer.parseInt(alarm.getminutes());
		
		CheckBox ck_alarm = (CheckBox)findViewById(R.id.heure);
		ck_alarm.setText(heureReveil);
		ck_alarm.setChecked(alarm.isActive());
	}
	
	/*
	 * changeHeure se déclenche automatiquement au click sur l'heure ou la CheckBox.
	 * Active ou désactive le reveil.
	 * Affiche un dialog pour choisir l'heure de reveil
	 */
	public void changeHeure(View target){
		CheckBox ck_alarm = (CheckBox)findViewById(R.id.heure);

		//Si on active l'alarm alors on veut choisir l'heure.
		if(ck_alarm.isChecked()){
			speakOut("Activer la réveil");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			Intent int_time_picker = new Intent (this, Time_Picker_HH.class);
			int_time_picker.putExtra("hh",alarm.getheures());
			int_time_picker.putExtra("mn", alarm.getminutes());
			startActivityForResult(int_time_picker, CODE_RETOUR);
		}
		else{
			ck_alarm.setChecked(false);
			alarm.setActive(false);
		}

	}

	
	

	/*
	 * Sauvegarde des informations du reveil
	 */
	public void sauver(){
		try {
			ObjectOutputStream alarmOOS= new ObjectOutputStream(openFileOutput("alarm.serial",MODE_WORLD_WRITEABLE));
			alarmOOS.writeObject(alarm);
			alarmOOS.flush();
			alarmOOS.close();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/*
	 * Job de planification du reveil
	 */
	private void planifierAlarm() {
		//Récupération de l'instance du service AlarmManager.
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		//On instancie l'Intent qui va être appelé au moment du reveil.
		Intent intent = new Intent(this, AlarmReceiver.class);

		//On créer le pending Intent qui identifie l'Intent de reveil avec un ID et un/des flag(s)
		PendingIntent pendingintent = PendingIntent.getBroadcast(this, ALARM_ID, intent, 0);

		//On annule l'alarm pour replanifier si besoin
		am.cancel(pendingintent);

		//La on va déclencher un calcul pour connaitre le temps qui nous sépare du prochain reveil.
		Calendar reveil  = Calendar.getInstance();
		reveil.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarm.getheures()));
		reveil.set(Calendar.MINUTE, Integer.parseInt(alarm.getminutes()));
		
		if(reveil.compareTo(Calendar.getInstance()) == -1)
			reveil.add(Calendar.DAY_OF_YEAR, 1);

		Calendar cal = Calendar.getInstance();
		reveil.set(Calendar.SECOND, 0);
		cal.set(Calendar.SECOND, 0);
		long diff = reveil.getTimeInMillis() - cal.getTimeInMillis();

		//On ajoute le reveil au service de l'AlarmManager
		am.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis() + diff, pendingintent);
		Toast.makeText(this, "Alarme programmé; le " + 
				reveil.get(Calendar.DAY_OF_MONTH) + " à " + 
				reveil.get(Calendar.HOUR_OF_DAY) + ":" + + 
				reveil.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
	}

	
	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 
			if(alarm.isActive()){
				speakOut("Réveil activé à, "+alarm.getheures()+" heures et, "+alarm.getminutes()+" minutes");
			}
			else{
				speakOut("Taper pour activer la réveil");
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}


	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);

	}

	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.btn_quitter:
			speakOut("Retour");
			break;
		}

	}

	@Override
	public boolean onLongClick(View v) {

		switch(v.getId()){
		case R.id.btn_quitter:
			vibration.vibrate(100);
			sauver();
			finish();
			break;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == CODE_RETOUR){
			if(resultCode == RESULT_OK){
				heures = data.getStringExtra("heures");
				minutes = data.getStringExtra("minutes");
				alarm.setheure(heures);
				alarm.setminute(minutes);
				alarm.setActive(true);
				CheckBox ck_heures = (CheckBox)findViewById(R.id.heure);
				ck_heures.setChecked(true);
				affichage();
				planifierAlarm();
				
			}
		}
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
		speakOut("Réveil activé à,"+heures+" heures, et, "+minutes+" minutes");
		super.onResume();
	}
	

	@Override
	protected void onDestroy() {
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		sauver();
		unregisterReceiver(mReceiver);
		super.onDestroy();

	}
	
}
