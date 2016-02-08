package com.talk_to_your_phone.messagerie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;
import com.talk_to_your_phone.outils.Brailler;
import com.talk_to_your_phone.outils.TremblementDetection;
import com.talk_to_your_phone.outils.TremblementDetection.TremblementListener;


public class Dicter_SMS extends Activity implements OnGestureListener, TextToSpeech.OnInitListener, 
OnClickListener, OnLongClickListener{

	public GestureDetector gd;
	private TextToSpeech tts;
	private TextView text;
	private Button btn_quitter;
	private Button btn_env;
	
	private static final int CODE_ACTIVITE = 111;
	Vibrator vibration;
	Intent intent;
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	BroadcastReceiver mReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dicter_sms);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		text = (TextView)findViewById(R.id.text);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		btn_quitter.setOnClickListener(this);
		btn_quitter.setOnLongClickListener(this);
		
		btn_env = (Button)findViewById(R.id.btn_dest);
		btn_env.setOnClickListener(this);
		btn_env.setOnLongClickListener(this);
		
		tts = new TextToSpeech(this,this);
		intent = getIntent();
		
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE
		
		PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if(activities.size()!= 0){
        	text.setText("");
    
        }else{
        	text.setText("Pas de Speech");
        }
		
		gd = new GestureDetector(this);
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
	
		

		gd.setOnDoubleTapListener(new OnDoubleTapListener()
		{

			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{
				intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech");
				speakOut("dicter");
				startActivityForResult(intent,CODE_ACTIVITE);
				return false;
			}


			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {

				
				return true;
			}});
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		return gd.onTouchEvent(e);//return the double tap events
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		float dX = e2.getX()-e1.getX();

		float dY = e1.getY()-e2.getY();

		if (Math.abs(dY)<SWIPE_MAX_OFF_PATH &&

				Math.abs(velocityX)>=SWIPE_THRESHOLD_VELOCITY &&

				Math.abs(dX)>=SWIPE_MIN_DISTANCE ) {

			if (dX>0) {
				if(!text.getText().toString().equals("")){
					String st =text.getText().toString();
					if(st.indexOf(" ") == -1)
					{
						text.setText("");
						speakOut("1 mot éffacé");
					
					}
					else{
		
						st = text.getText().toString().substring(0, st.lastIndexOf(" "));
						Log.i("st",st);
						text.setText(st);
						speakOut("1 mot éffacé");
					}
				}
				
			} else {

				if(!text.getText().toString().equals("")){
					String st =text.getText().toString();
					if(st.indexOf(" ") == -1)
					{
						text.setText("");
						speakOut("1 mot éffacé");
					
					}
					else{
		
						st = text.getText().toString().substring(0, st.lastIndexOf(" "));
						Log.i("st",st);
						text.setText(st);
						speakOut("1 mot éffacé");
					}
				}
				}
				

			

			return true;

		} else if (Math.abs(dX)<SWIPE_MAX_OFF_PATH &&

				Math.abs(velocityY)>=SWIPE_THRESHOLD_VELOCITY &&

				Math.abs(dY)>=SWIPE_MIN_DISTANCE ) {

			if (dY>0) {

				Toast.makeText(getApplicationContext(), "Up Swipe", Toast.LENGTH_SHORT).show();

			} else {

				Toast.makeText(getApplicationContext(), "Down Swipe", Toast.LENGTH_SHORT).show();

			}

			return true;

		}

		return false;



	}


	@Override
	public void onLongPress(MotionEvent e) {
		if(text.getText().toString().equals("")){
			speakOut("pas de text");
		}
		else{
			speakOut(text.getText().toString());
		}
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
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

			speakOut("taper 2 fois pour dicter le message, défiler pour éffacer 1 mot, long appui pour écouter");

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(requestCode == CODE_ACTIVITE){
			if(resultCode == RESULT_OK) {
				ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	
				text.setText(text.getText().toString()+" "+matches.get(0).toString());
				                        
			}
		}
	}


	@Override
	public boolean onLongClick(View v) {
		switch(v.getId()){

		case(R.id.btn_dest):
        	vibration.vibrate(100);
			if(!text.getText().toString().equals("")){
				final Intent it = new Intent(Dicter_SMS.this,Choix_Destinataire.class);
				it.putExtra("source","Dicter SMS");
				it.putExtra("text_msg",text.getText().toString());
				startActivity(it);
				finish();
				break;
			}
			else{
				speakOut("Text vide");
				break;
			}
        	
        case(R.id.btn_quitter):
        	vibration.vibrate(100);
            finish();
        	
        break;
		}
		return true;
	}


	@Override
	public void onClick(View v) {
		switch(v.getId()){

		case(R.id.btn_dest):
	            speakOut("Choix destinataire");
	    break;
        
		case(R.id.btn_quitter):
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
	protected void onResume() {
		// ONLY WHEN SCREEN TURNS ON
		if (!ScreenReceiver.wasScreenOn) {
			// THIS IS WHEN ONRESUME() IS CALLED DUE TO A SCREEN STATE CHANGE
			speakOut("taper 2 fois pour dicter le message, défiler pour éffacer 1 mot, long appui pour écouter");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			
		}
		super.onResume();
	}
	
	
}

