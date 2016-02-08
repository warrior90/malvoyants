package com.talk_to_your_phone.notifications;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.project.talk_to_your_phone.R;

public class SMS_notif extends Activity implements TextToSpeech.OnInitListener, OnGestureListener{
	private TextView contentTxt;
	Intent intent;
	MediaPlayer mp;
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private GestureDetector gd;
	private TextToSpeech tts;
	String de;
	String sms_text;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		unlockScreen();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_recu);
		
		
		
		contentTxt = (TextView)findViewById(R.id.txt);

		gd = new GestureDetector(this);
		tts = new TextToSpeech(this,this);
		intent = getIntent();
		de = intent.getStringExtra("num");
		sms_text = intent.getStringExtra("incomingsms");
	
		contentTxt.setText(de);

		setVolumeControlStream(AudioManager.STREAM_MUSIC); 
		mp = MediaPlayer.create(this, R.raw.new_sms);
		mp.start();

		gd.setOnDoubleTapListener(new OnDoubleTapListener()
		{

			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{
				speakOut(sms_text);
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

		NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		
		final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                finish(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 20000);
	}
	
	private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
    }

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 
			
			speakOut("SMS reçu de la part de, "+de+", taper 2 fois pour lire, défiler pour fermer");

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
		super.onDestroy();
	}

}
