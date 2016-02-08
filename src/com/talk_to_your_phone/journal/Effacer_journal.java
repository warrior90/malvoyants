package com.talk_to_your_phone.journal;

import java.util.Locale;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.CallLog;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.TextView;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;

public class Effacer_journal extends Activity implements TextToSpeech.OnInitListener, OnGestureListener{
	private TextView contentTxt;
	Intent intent;
	MediaPlayer mp;
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private GestureDetector gd;
	private TextToSpeech tts;
	BroadcastReceiver mReceiver;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eff_journal_notif);
		contentTxt = (TextView)findViewById(R.id.txt);

		gd = new GestureDetector(this);
		tts = new TextToSpeech(this,this);
		intent = getIntent();
	
		  // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        // YOUR CODE

		setVolumeControlStream(AudioManager.STREAM_MUSIC); 
		mp = MediaPlayer.create(this, R.raw.beep);
		mp.start();

		gd.setOnDoubleTapListener(new OnDoubleTapListener()
		{

			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{
				if (Effacer_callLog()){
					speakOut("Journal éffacé");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						
						e1.printStackTrace();
					}
					finish();
				}
				else{
					speakOut("Journal non éffacé");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						
						e1.printStackTrace();
					}
					finish();
					
				}
				
				return false;

			}

			@Override
			public boolean onDoubleTapEvent(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {

				return false;
			}});

	}
	
	public boolean Effacer_callLog(){
		try{
			
			this.getContentResolver().delete(CallLog.Calls.CONTENT_URI, null, null);
			return true;
			
		}catch(Exception e){
			
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
			
			speakOut("Taper 2 fois pour confirmer la suppression du journal, défiler pour ignorer");

		} else {
			Log.e("TTS", "Initilization Failed!");
		
		}
	}


	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e) 
	{
		return gd.onTouchEvent(e);//return the double tap events
	}


	@Override
	public boolean onDown(MotionEvent arg0) {
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
				
				finish();
				


			}


		} else {
			
			finish();
			
		}

		return true;

	}


	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
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
			speakOut("Taper 2 fois pour confirmer la suppression du journal, défiler pour ignorer");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("Taper 2 fois pour confirmer la suppression du journal, défiler pour ignorer");
		}
		super.onResume();
	}

	
}
