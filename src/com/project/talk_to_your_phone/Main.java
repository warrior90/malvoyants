package com.project.talk_to_your_phone;

import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CallLog;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;

import com.talk_to_your_phone.aide.Menu_Demo1;
import com.talk_to_your_phone.clavier.Clavier;
import com.talk_to_your_phone.contacts.Contacts_Main;
import com.project.talk_to_your_phone.Urgence;
import com.talk_to_your_phone.horloge.Alarme_notif;
import com.talk_to_your_phone.horloge.Horloge_Main;
import com.talk_to_your_phone.islam.Islam_main;
import com.talk_to_your_phone.journal.Journal_main;
import com.talk_to_your_phone.messagerie.Messagerie;
import com.talk_to_your_phone.notifications.Batterie_notif;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.parametres.Parametres_Main;

public class Main extends Activity implements OnClickListener, OnLongClickListener, TextToSpeech.OnInitListener{

    private ImageView clavier;
    private ImageView journal;
    private ImageView sms;
    private ImageView contacts;
    private ImageView islam;
    private ImageView horloge;
    private ImageView parametres;
    private ImageView aide;
    private Button btn_urgence;
    private TextToSpeech tts;
    private Vibrator vibration;
    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        int scale = -1;
        int level = -1;
        int voltage = -1;
        int temp = -1;
     
        @Override
        public void onReceive(Context context, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            //Log.e("BatteryManager", "level is "+level+"/"+scale+", temp is "+temp+", voltage is "+voltage);
            double percent = (level*100)/scale;
            
            if(percent<=10 && percent>5){
                Intent i = new Intent(context, Batterie_notif.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("source", "batterie");
                i.putExtra("level", String.valueOf(percent));
                i.putExtra("couleur","rouge" );
                context.startActivity(i);
            }
            
        }
    };
    private Cursor c_appels;
    private Cursor c_SMS;
    int nbr_appels = 0;
    int nbr_SMS = 0;
    Intent intent;
    BroadcastReceiver mReceiver;
    String source;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        unlockScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        
        intent = getIntent();
        source = intent.getStringExtra("source");
         // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
        
        //register Batterie broadcast 
        this.registerReceiver(this.batteryReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        
        vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        
        tts = new TextToSpeech(this,this);
        
        clavier = (ImageView)findViewById(R.id.clavier);
        journal = (ImageView)findViewById(R.id.journal);
        contacts = (ImageView)findViewById(R.id.contacts);
        sms = (ImageView)findViewById(R.id.sms);
        islam = (ImageView)findViewById(R.id.islam);
        horloge = (ImageView)findViewById(R.id.horloge);
        parametres = (ImageView)findViewById(R.id.params);
        aide = (ImageView)findViewById(R.id.aide);
        btn_urgence = (Button)findViewById(R.id.btn_urgence);

        clavier.setOnClickListener(this);   clavier.setOnLongClickListener(this);
        contacts.setOnClickListener(this);  contacts.setOnLongClickListener(this);
        journal.setOnClickListener(this);   journal.setOnLongClickListener(this);
        sms.setOnClickListener(this);   sms.setOnLongClickListener(this);
        islam.setOnClickListener(this); islam.setOnLongClickListener(this);
        horloge.setOnClickListener(this);   horloge.setOnLongClickListener(this);
        parametres.setOnClickListener(this);    parametres.setOnLongClickListener(this);
        aide.setOnClickListener(this);  aide.setOnLongClickListener(this);
        btn_urgence.setOnClickListener(this);   btn_urgence.setOnLongClickListener(this);
        
        nbr_appels = Appels_abscences(this);
        nbr_SMS = SMS_non_lus(this);

    }

  
    @Override
    public boolean onLongClick(View v) {

        switch(v.getId()){
        
        case R.id.clavier:
            vibration.vibrate(100);
            Intent i_clavier = new Intent(this,Clavier.class);
            startActivity(i_clavier);
            break;
            
        case R.id.contacts:
            vibration.vibrate(100);
            Intent i_contacts = new Intent(this,Contacts_Main.class);
            startActivity(i_contacts);
            break;
            
        case R.id.journal:
            vibration.vibrate(100);
            Intent i_journal = new Intent(this,Journal_main.class);
            startActivity(i_journal);
            break;
            
        case R.id.sms:
            vibration.vibrate(100);
            Intent i_sms = new Intent(this,Messagerie.class);
            startActivity(i_sms);
            break;
            
        case R.id.islam:
            vibration.vibrate(100);
            Intent i_islam = new Intent(this,Islam_main.class);
            startActivity(i_islam);
            break;
            
        case R.id.params:
            vibration.vibrate(100);
            Intent i_parametres = new Intent(this,Parametres_Main.class);
            startActivity(i_parametres);
            break;
            
        case R.id.horloge:
            vibration.vibrate(100);
            Intent i_horloge = new Intent(this,Horloge_Main.class);
            startActivity(i_horloge);
            break;
            
        case R.id.aide:
            vibration.vibrate(100);
            Intent i_aide = new Intent(this,Menu_Demo1.class);
            startActivity(i_aide);
            break;
            
        case R.id.btn_urgence:
            vibration.vibrate(100);
            Intent i_urgence = new Intent(this,Urgence.class);
            startActivity(i_urgence);
            break;
        }

        
        return true;
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
        
        case R.id.clavier:
            speakOut("Clavier");
            break;
            
        case R.id.contacts:
            speakOut("Contacts");
            break;
            
        case R.id.journal:
            speakOut("Journal d'appels");
            break;
            
        case R.id.sms:
            speakOut("Messagerie");
            break;
            
        case R.id.islam:
            speakOut("islam");
            break;
            
        case R.id.params:
            speakOut("Paramètres");
            break;
            
        case R.id.horloge:
            speakOut("horloge");
            break;
            
        case R.id.aide:
            speakOut("Aide");
            break;
        
        case R.id.btn_urgence:
            speakOut("Urgence");
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

            // sms non lus et appels en abscences
            if(nbr_appels>0 && nbr_SMS>0){
            	speakOut("Bienvenue, vous avez "+nbr_appels+" appels en abscences et "+nbr_SMS+" SMS non lus");
            }
            if(nbr_appels > 0 && nbr_SMS==0 ){
            	speakOut("Bienvenue, vous avez "+nbr_appels+" appels en abscences");
            }
            if(nbr_appels == 0 && nbr_SMS>0 ){
            	speakOut("Bienvenue, vous avez "+nbr_SMS+" SMS non lus");
            }
            if(nbr_appels == 0 && nbr_SMS==0){
            	speakOut("Bienvenue");

            }

        
        } else {
            Log.e("TTS", "Initilization Failed!");

        }
        
       

    }

    private void speakOut(String msg) {
        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
    }
    
    

    public int Appels_abscences(Context ctx){
        
        int nbr_app = 0;
        final String[] projection = null;
        final String selection = null;
        final String[] selectionArgs = null;
        final String sortOrder = android.provider.CallLog.Calls.DATE + " DESC";
        Cursor cursor = null;
        try{
            cursor = ctx.getContentResolver().query(
                    Uri.parse("content://call_log/calls"),
                    projection,
                    selection,
                    selectionArgs,
                    sortOrder);
            while (cursor.moveToNext()) { 
                String callLogID = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
                String callNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String callDate = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                String callType = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String isCallNew = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NEW));
                if(Integer.parseInt(callType) == CallLog.Calls.MISSED_TYPE && Integer.parseInt(isCallNew) > 0){
                    nbr_app++;
                }
            }
            
        }catch(Exception ex){
           ex.printStackTrace();
        }finally{
            cursor.close();
        }
        return nbr_app;
    }
    
    public int SMS_non_lus(Context ctx){
        int nbr = 0;
        
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        c_SMS = getContentResolver().query(SMS_INBOX, null, "read = 0", null, null);
        nbr = c_SMS.getCount();
        c_SMS.deactivate();
        
        return nbr;
    }

   
   
  /*  @Override
    public void onAttachedToWindow()
    {  
           this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);     
           super.onAttachedToWindow();  
    }*/
    
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_HOME) {
        	tts.setLanguage(Locale.ENGLISH);
        	speakOut("Home");
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	
            return true;
        }
        return super.onKeyDown(keyCode, event);   
    }
    
    private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
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
            // sms non lus et appels en abscences
        	nbr_appels = Appels_abscences(this);
            nbr_SMS = SMS_non_lus(this);
            if(nbr_appels>0 && nbr_SMS>0){
            	try {
            		speakOut("Menu principal, vous avez "+nbr_appels+" appels en abscences et "+nbr_SMS+" SMS non lus");
    				Thread.sleep(4000);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                
            }
            if(nbr_appels > 0 && nbr_SMS==0 ){
            	try {
            		speakOut("Menu principal, vous avez "+nbr_appels+" appels en abscences");
    				Thread.sleep(3000);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                
            }
            if(nbr_appels == 0 && nbr_SMS>0 ){
            	try {
            		speakOut("Menu principal, vous avez "+nbr_SMS+" SMS non lus");
    				Thread.sleep(2000);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                
            }
            if(nbr_appels == 0 && nbr_SMS==0){
            	try {
            		speakOut("Menu principal");
    				Thread.sleep(1000);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                
            }
        } else {
        	// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
        	// sms non lus et appels en abscences

        	try {
        		speakOut("Menu principal");
        		Thread.sleep(1000);
        	} catch (InterruptedException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}


        	
        }
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        unregisterReceiver(batteryReceiver);
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
    


}