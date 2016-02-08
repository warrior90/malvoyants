package com.talk_to_your_phone.islam;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
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


public class Boussole extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{
	Intent intent;
	private TextToSpeech tts;
	//La vue de notre boussole
	CompassView compassView;
	MediaPlayer mp;
	
	//Le gestionnaire des capteurs
	private SensorManager sensorManager;
	//Notre capteur de la boussole numérique
	private Sensor sensor;
	
	boolean Qibla_OK;
	boolean Droite;
	boolean Gauche;
	private Button btn_quitter;
	private TextView tv;
	
	private Vibrator vibration;
	
	GpsTracker gps;
	Calcul_Qibla C;
	String city;
	double latitude;
	double longitude;
	
	BroadcastReceiver mReceiver;
	
	//Notre listener sur le capteur de la boussole numérique
	private final SensorEventListener sensorListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			updateOrientation(event.values[SensorManager.DATA_X]);
			
			if(Qibla_OK){
				setVolumeControlStream(AudioManager.STREAM_MUSIC); 
				mp = MediaPlayer.create(Boussole.this, R.raw.qibla);
				mp.start();
	        	vibration.vibrate(200);
	        	sensorManager.unregisterListener(sensorListener);
	        }
	       
	
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quibla);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        intent = getIntent();
        
        // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
        
        tts = new TextToSpeech(this,this);
        vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        compassView = (CompassView)findViewById(R.id.compassView);
        btn_quitter = (Button)findViewById(R.id.btn_retour);
        
        gps = new GpsTracker(this);

        // check if GPS enabled     
        if(gps.canGetLocation()){
        	Log.i("GPS","true");
        	latitude = gps.getLatitude();
        	longitude = gps.getLongitude();
        	Log.i("lat", String.valueOf(latitude));
        	Log.i("long", String.valueOf(longitude));

        	//Log.i("city", String.valueOf(gps.My_city()));
        	//city = gps.My_city();

        }else{
        	Log.i("GPS","false");
        	compassView.setQ_or(159);
        	
        }
        tv = (TextView)findViewById(R.id.tv);
        tv.setText("recherche quibla");
        
        btn_quitter.setOnClickListener(this);	btn_quitter.setOnLongClickListener(this);
        
        //Récupération du gestionnaire de capteurs
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        
        //Demander au gestionnaire de capteur de nous retourner les capteurs de type boussole
        List<Sensor> sensors =sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        
        //s’il y a plusieurs capteurs de ce type on garde uniquement le premier
        if (sensors.size() > 0) {
        	sensor = sensors.get(0);
        }
        
        
        
    }
    
	//Mettre à jour l'orientation
    protected void updateOrientation(float rotation) {
		compassView.setNorthOrientation(rotation);	
		
		compassView.setQiblaOrientation(rotation);	
		
	
		tv.setText(String .valueOf(compassView.getQiblaOrientation()));
		
		if(compassView.getPos().equals("qibla")){
			Qibla_OK = true; 
			tv.setText("Qibla OK");
			
		}
		

		
	}

	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}

	
	
	@Override
	protected void onStop(){
		super.onStop();
		//Retirer le lien entre le listener et les évènements de la boussole numérique
		sensorManager.unregisterListener(sensorListener);
		//tts.shutdown();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 
			try {
				C = new Calcul_Qibla(latitude, longitude);
				compassView.setQ_or(C.calcul());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			setVolumeControlStream(AudioManager.STREAM_MUSIC); 
			mp = MediaPlayer.create(Boussole.this, R.raw.qibla_instr);
			mp.start();
			

		} else {
			Log.e("TTS", "Initilization Failed!");
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
	public boolean onLongClick(View v) {
		
		switch(v.getId()){
		
		case R.id.btn_retour:
			vibration.vibrate(100);
			finish();
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.btn_retour:
			speakOut("Retour");
			break;
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
    protected void onResume(){
    	super.onResume();
    	//Lier les évènements de la boussole numérique au listener
    	sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
}
