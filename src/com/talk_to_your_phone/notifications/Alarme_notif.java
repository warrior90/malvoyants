package com.talk_to_your_phone.notifications;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.widget.TextView;

import com.project.talk_to_your_phone.R;

public class Alarme_notif extends Activity implements TextToSpeech.OnInitListener, OnGestureListener{
	private TextView contentTxt;
	Intent intent;
	MediaPlayer mp;
	private static final int SWIPE_MIN_DISTANCE = 150;
	private static final int SWIPE_MAX_OFF_PATH = 100;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private GestureDetector gd;
	private TextToSpeech tts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery);
		contentTxt = (TextView)findViewById(R.id.txt);

		gd = new GestureDetector(this);
		tts = new TextToSpeech(this,this);
		intent = getIntent();

		contentTxt.setText(intent.getStringExtra("level"));
		String couleur = intent.getStringExtra("couleur");
		//getWindow().getDecorView().setBackgroundColor(Color.RED);

		setVolumeControlStream(AudioManager.STREAM_MUSIC); 
		mp = MediaPlayer.create(this, R.raw.galaxy_low_battery);
		mp.start();

		gd.setOnDoubleTapListener(new OnDoubleTapListener()
		{

			@Override
			public boolean onDoubleTap(MotionEvent e) 
			{

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

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 
			

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

		String source = intent.getStringExtra("source");
		if(source.equalsIgnoreCase("batterie")){
			
			speakOut("Batterie trés faible, "+intent.getStringExtra("level")+" pour cent");
	
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
