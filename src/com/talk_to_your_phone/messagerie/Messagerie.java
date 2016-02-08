package com.talk_to_your_phone.messagerie;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
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
import com.talk_to_your_phone.outils.TremblementDetection;
import com.talk_to_your_phone.outils.TremblementDetection.TremblementListener;




public class Messagerie extends Activity implements TextToSpeech.OnInitListener, OnClickListener, OnLongClickListener{
    
    private Button nv_msg;
    private Button boite_reception;
    private Button btn_quitter;
    private TextToSpeech tts;
    private Vibrator vibration;
    Intent i;
    BroadcastReceiver mReceiver;
   
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagerie);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
       
        i = getIntent();
        
        // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
        
        nv_msg = (Button)findViewById(R.id.btn_nv_msg);
        boite_reception = (Button) findViewById(R.id.btn_boite_reception);
        btn_quitter = (Button)findViewById(R.id.btn_quitter);
        
        tts = new TextToSpeech(this,this);
        
        nv_msg.setOnLongClickListener(this);    nv_msg.setOnClickListener(this);
        boite_reception.setOnLongClickListener(this);   boite_reception.setOnClickListener(this);
        btn_quitter.setOnLongClickListener(this);   btn_quitter.setOnClickListener(this);
        
   
        vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        
    }
    

   
    @Override
    public boolean onLongClick(View v) {
    	
    	switch(v.getId()){

        case(R.id.btn_nv_msg):
        	vibration.vibrate(100);
        	final Intent int_ecrire_message = new Intent(this,NV_message.class);
        	int_ecrire_message.putExtra("source", "main");
        	startActivity(int_ecrire_message);
            
        break;

        case(R.id.btn_boite_reception):
        	vibration.vibrate(100);
        	final Intent it = new Intent(this,Boite_reception.class);
        	it.putExtra("source", "main");
			startActivity(it);
        break;
        
        case(R.id.btn_quitter):
        	vibration.vibrate(100);
        	finish();
        break;
        }
    	
        
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        case(R.id.btn_nv_msg):
            speakOut("écrire un message");
        break;

        case(R.id.btn_boite_reception):
            speakOut("Boite de réception");
        break;
        
        case(R.id.btn_quitter):
            speakOut("Quitter");
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
            
            speakOut("Messagerie, éffectuer un choix");

        } else {
            Log.e("TTS", "Initilization Failed!");
           
        }
               
    }

    private void speakOut(String msg) {
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onRestart() {
    	speakOut("Messagerie");
    	super.onRestart();
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
			speakOut("Messagerie, éffectuer un choix");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Messagerie, éffectuer un choix");
		}
		super.onResume();
	}
	
    
}
