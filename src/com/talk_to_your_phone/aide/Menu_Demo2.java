package com.talk_to_your_phone.aide;

import java.util.Locale;

import com.project.talk_to_your_phone.R;
import com.talk_to_your_phone.notifications.ScreenReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;

public class Menu_Demo2 extends Activity implements OnClickListener, OnLongClickListener, 
TextToSpeech.OnInitListener{

	private TextToSpeech tts;
	private Button btn_islam;
	private Button btn_horloge;
	private Button btn_param;
	private Button btn_autre;
	private Button btn_retour;
	private Button btn_quitter;
	private Vibrator vibration;
	Intent intent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo_menu_2);

		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

		tts = new TextToSpeech(this,this);

		// INITIALIZE RECEIVER
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		BroadcastReceiver mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);

		btn_islam = (Button)findViewById(R.id.btn_islam);
		btn_horloge = (Button)findViewById(R.id.btn_horloge);
		btn_param = (Button)findViewById(R.id.btn_param);
		btn_autre = (Button)findViewById(R.id.btn_autre);
		btn_retour = (Button)findViewById(R.id.btn_retour);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);



		btn_islam.setOnClickListener(this);	    btn_islam.setOnLongClickListener(this);
		btn_horloge.setOnClickListener(this);	btn_horloge.setOnLongClickListener(this);
		btn_param.setOnClickListener(this);	    btn_param.setOnLongClickListener(this);
		btn_autre.setOnClickListener(this);	    btn_autre.setOnLongClickListener(this);
		btn_retour.setOnClickListener(this);	btn_retour.setOnLongClickListener(this);
		btn_quitter.setOnClickListener(this);	btn_quitter.setOnLongClickListener(this);

	}


	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.FRENCH);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} 

			speakOut("d�mo, Deuxi�me menu");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}


	@Override
	public boolean onLongClick(View v) {

		switch(v.getId()){

		case R.id.btn_islam:
			vibration.vibrate(100);
			speakOut("");
			break;

		case R.id.btn_horloge:
			vibration.vibrate(100);
			speakOut("service horloge, faite un appui long sur l'icone du horloge dans le menu principale et vous aller �couter la date d'aujourd'hui et l'heure" +

						"le menu horloge contient 3 boutons, un bouton s'appelle �quivalent date, un bouton r�veil, et un boutoon pour quitter, " +

						"le premier bouton permet de de donner le jour �quivalent a une date entr�, un long appui sur le bouton �quivalent date" +
						" vous donne un clavier pour rentrer le jour, le mois et l'ann�e, ainsi vous aller �couter cette date tombe dans quel jours, " +

						"le deuxi�me bouton est la r�veil, faite un appui long sur r�veil, un autre interface appara�t, dans cet interface vous avez une " +
						"date et un bouton de retour en arri�re en bas de l'ecran, taper sur l'�cran �a vous permet d'activer la r�veil, " +
						"taper une deuxi�me fois �a vous permet de la d�sactiv�, " +

						"pour le r�glage du r�veil, taper une fois pour activer la r�veil, une fois taper un autre interface se lance " +
						"et elle contient les heures de la journ�e, dans cet interface il y a 3 bouton, un bouton + un bouton moins et un bouton valider " +
						"et au milieux vous avez les heures, taper sur plus pour incr�menter d'une heure la r�veil et sur moins pour d�cr�menter, " +
						"pour valider les heure il ya un bouton valider en bas, " +
						"rester appuyer sur ce bouton et vous aller �tre transf�rer vers l'interface des minutes, " +
						"fa�te la m�me chose que les heure ainsi faire un appui long sur valider, " +
						"maintenant vous avez bien r�gler votre r�veil, " +

						"pour sortir du service d'horloge dans le menu il ya le bouton quitter, " +
					"faites un appui long et vous aller �tre redir�cter vers le menu principale");
			break;

		case R.id.btn_param:
			vibration.vibrate(100);
			speakOut("service param�tre, dans ce service vous avez 3 bouton, un bouton pour v�rifier l'�tat du batterie, " +
					"un bouton pour le r�glage du mode, et un boutton pour quitter, " +

						"pour connaitre l'�tat de votre batterie, il suffit de faire un long appui sur le bouton �tat batterie, " +
						"ensuite vous aller �couter le niveau de votre batterie, " +

						"pour changer le mode de votre t�l�phone, appuyer longuement sur le bouton, changer le mode, un autre menu " +
						"s'affiche contenant 4 autre boutons, " +

						"un appui long sur le premier bouton permet d'activer le mode silencieux, " +
						"un appui sur le deuxi�me bouton permet d'activer le mode g�n�ral, " +
						"un appui sur le troisi�me bouton permet d'activer le mode vibreur, " +

						"pour revenir en arri�re au menu de param�tre appuyer sur le bouton quitter, " +

					"pour quitter le service param�tre, fa�tes un long appui sur le bouton quitter");

			break;

		case R.id.btn_autre:
			vibration.vibrate(100);
			speakOut("autres astuces, pour fermer toutes les notification il suffit de d�filer, " +
					"si vous avez re�u un appel un interface va apparaitre contien deux bouton, un bouton pour r�pondre " +
					"et un bouton pour raccrocher, " +
					"ces deux boutons sont en bas de l'�cran, pour �couter vous �tes sur quel bouton tapaer une fois sur l'un des bouton, " +
					"si vous avez re�us un message, taper deux fois pour �couter le message sinon d�filer pour quitter, " +

						"si la batterie est en niveau critique vous aller etre notifier autaumatiquement, " +
					"pour annuler la notification de r�veil de d�filer");

			break;

		case R.id.btn_retour:
			vibration.vibrate(100);
			Intent intent = new Intent(this,Menu_Demo1.class);
			startActivity(intent);
			finish();
			break;

		case R.id.btn_quitter:
			vibration.vibrate(100);
			finish();
			break;
		}
		return true;
	}


	private void speakOut(String msg) {
		tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);
	} 


	@Override
	public void onClick(View v) {

		switch(v.getId()){

		case R.id.btn_islam:

			speakOut("d�mo du service islam");
			break;

		case R.id.btn_horloge:
			speakOut("d�mo du service horloge");
			break;

		case R.id.btn_param:
			speakOut("d�mo du service param�tres");
			break;

		case R.id.btn_autre:
			speakOut("autres astuces dans votre application");
			break;



		case R.id.btn_retour:
			speakOut("retour � la page pr�c�dente");
			break;

		case R.id.btn_quitter:
			speakOut("quitter le service d�mo");
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
			speakOut("d�mo, Deuxi�me menu");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("d�mo, Deuxi�me menu");
		}
		super.onResume();
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





