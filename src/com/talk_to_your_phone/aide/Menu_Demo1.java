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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

public class Menu_Demo1 extends Activity implements OnClickListener, OnLongClickListener, 
TextToSpeech.OnInitListener{
	
	private TextToSpeech tts;
	private Button btn_clavier;
	private Button btn_journal;
	private Button btn_contacts;
	private Button btn_sms;
	private Button btn_suivant;
	private Button btn_quitter;
	private Vibrator vibration;
	Intent intent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		unlockScreen();
		setContentView(R.layout.demo_menu_1);
		
		intent = getIntent();
		 // INITIALIZE RECEIVER
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
		vibration = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		tts = new TextToSpeech(this,this);

		btn_clavier = (Button)findViewById(R.id.btn_clavier);
		btn_journal = (Button)findViewById(R.id.btn_journal);
		btn_contacts = (Button)findViewById(R.id.btn_contacts);
		btn_sms = (Button)findViewById(R.id.btn_sms);
		btn_suivant = (Button)findViewById(R.id.btn_suivant);
		btn_quitter = (Button)findViewById(R.id.btn_quitter);
		
		
		
		btn_clavier.setOnClickListener(this);	btn_clavier.setOnLongClickListener(this);
		btn_contacts.setOnClickListener(this);	btn_contacts.setOnLongClickListener(this);
		btn_journal.setOnClickListener(this);	btn_journal.setOnLongClickListener(this);
		btn_sms.setOnClickListener(this);	    btn_sms.setOnLongClickListener(this);
		btn_suivant.setOnClickListener(this);	btn_suivant.setOnLongClickListener(this);
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

			speakOut("d�monstration de tous les services de l'application, premier menu");

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}


	@Override
	public boolean onLongClick(View v) {

		switch(v.getId()){

		case R.id.btn_clavier:
			vibration.vibrate(100);
			speakOut("service clavier, dans ce service vous avez une zone de texte toute en haut, et tout le reste c'est votre clavier num�rique, " +
					"Pour ecouter taper une fois sur le bouton que vous voulez, par exemple, pour ecouter le numero 1, taper une fois sur le bouton 1, et ainsi de suite" +
					" pour selectionner un numero, rester appuyer sur le num�ro desir�, par exemple pour selectionner le num�ro 1, fa�tes un appui long sur le bouton 1, et ainsi de suite " +
					"pour appeler un numero saisie, il ya le bouton d'appeler en bas a droite, Fa�tes un long appui sur le bouton appeler et votre appel sera pass� " +
					"pour �ffacer un num�ro saisie, il faut faire un long appui sur le bouton �ffacer, noter bien, on ne peu pas �ffacer un seul num�ro, par exemple si vous avez taper 123 et " +
					"vous avez appuyer sur �ffacer tout le num�ro sera �ffac�, et vous aller �tre besoin de retaper tous le num�ro a nouveau " +
					"pour passer au menu de choix, il suffi de trembler le t�l�phonele, le menu choix est construi de 3 zones " +
					"premier zone pour r��couter le num�ro saisie, il suffi de taper une fois en haut, " +
					"Deuxieme zone et destin� pour ajouter le num�ro saisie � partir du clavier, appuyer une fois pour �couter et il suffi d'appuyer longuement sur le bouton ajouter,  " +
					"apr�s un long appui vous tenter maintenant d'associer un nom � votre num�ro saisie � l'aide du clavier braille, " +
					"si vous avez tromper du nom, vous pouvez effacer par caract�re le champ que vous venez d'�crire � l'aide d'un bouton �ffacer situ� en bas a gauche, sinon vous pouvez �ffacer par mot en tremblant le smartphone, " +
					"pour r��couter le texte saisie et verifi� s'il est correct ou non, taper une fois pour savoir que vous �tes sur le bouton qui vous dit lire le texte, apr�s il suffit d'appuyer longuement sur ce bouton pour valider la lecture, " +
					"et pour valider l'ajout, taper une fois en bas de l'�cran jusqu'� ce que vous �tes sur le bouton valider, faite un long appui pour valider");
			break;

		case R.id.btn_journal:
			vibration.vibrate(100);
			speakOut("service journal d'appels, ce service fais apparaitre 5 boutons, un bouton pour les appels en absences, un bouton pour les appels re�us, un bouton pour les appels �mis, un bouton pour �ffacer tous le journal et un bouton pour quitter, " +
					" pour savoir vous �tes dans quel endroi, il suffit de taper une fois, " +
					"pour selectionner un des sous services, il suffit d'appuyer longuement sur un bouton, par exemple pour s�l�ctionner les appels en absence, il suffit de faire un appui long sur le bouton d'appels en absence, " +					
					"si le bouton d'appels en absence est selectionn�, un autre interface apparait, dans cet interface, on trouve un seul bouton retour en bas qui permet de revenir en arri�re si vous voulez annuler, " +
					"sinon, d�filer pour parcourir les appels en absence, taper deux fois pour �couter le nom ou num�ro et la date de l'appel, faire un appui long pour passer au menu choix, " +
					"dans ce menu choix, il y a 3 boutons, bouton pour appeler, bouton pour �ffacer cet appel du journal, et un bouton de retour, " +
					"pour �couter, vous �tes o� taper une fois, fa�tes un long appui pour selectionner l'action que vous voulez faire, " +					
					"c'est la meme chose pour les appels re�us et les appels �mis, "+
					
					"pour �ffacer le journal d'appels il suffit d'appuyer longuement sur le bouton �ffacer le journal, une notification sera affich�, taper deux fois pour confirmer sinon d�filer pour revenir au journal d'appels," +
					
					"le dernier bouton dans le journal est le bouton quitter, ce bouton suite a un long appui il se charge de quitter le journal d'appels et revenir au menu principal");
			
			
			break;
			
		case R.id.btn_contacts:
			vibration.vibrate(100);
			speakOut("service contacts, dans ce service on a 3 boutons, un bouton pour ajouter un nouveau contact, un bouton pour la consultation de la liste des contact, et en fin un bouton pour quitter le service contact" +
					"pour �couter les selection, il suffit de taper une fois sur un des boutons, pour valider un des boutons, fa�tes un long appui sur lui, " +
					
					"pour ajouter un nouveau contact, il vous demande apr�s d'�crire le nom de votre contact avec le clavier braille, " +
					"en bas vous avez 3 boutons, a gauche vous avez le bouton �ffacer. ce bouton vous permet d'effacer un caract�re si vous avez tromper du caract�re, sinon fa�tes trembler votre t�l�phone pour �ffacer par mot, " +
					"pour relire ce que vous avez �crit, il ya le bouton en bas et � droite qui vous permet de relire le texte �crit, " +
					"pour la validation du nom il ya le bouton en bas au milieux, " +
					
					"juste apr�s la validation du nom, un pav� num�rique appara�t en vous demandant de saisir le num�ro du contact, taper longuement sur les num�ros pour les saisir sinon taper une fois pour �couter le num�ro � saisir, " +
					
					"pour valider l'ajout, il vous suffit tout simplement d'appuier longuement sur le bouton valider citu� dans le bas d'�cran, et votre contact sera ajouter, " +
					
					"pour consulter la liste des contact, appuyer longuement sur le bouton cantact, dans l'interface qui suit vous avez 2 boutons, un pour faire une recherche � un contact d�sir� par le nom, et le deuxi�me bouton pour retourner au menu contacts, " +
					"pour parcourir la liste des contacts il faut d�filer, un appui long sur un des contact vous donne le choix entre appeler ce conact; envoyer un message;modifier le contact ou le supprimer de la liste des contacts" +
					"pour appeler le contact; il faut faire appui long sur le bouton appeler et l'appel sera transmi." +
					"pour envoyer un message; appuyer longuement sur le bouton, envoyer un SMS . ecrivez ce que vous voulez envoyer avec le clavier braille; et appuyer sur valider en bas du l'�cran.pour �ffacer caract�re par caract�re appuier longuement sur le bouton �ffacer sinon trembler le t�l�phone pour �ffacer mot par mot.pour r��couter le texte appuyer sur le bouton de relecture" +
					"pour modifier le contact appuyer sur modifier un autre menu apparait contient 3 boutons.un bouton pour modifier le nom ;un bouton pour modifier le num�ro et un bouton de retour qui permet de reculer au menu pr�c�dente." +
					"pour supprimer il vous suffit juste de faire un appui long sur le bouton supprimer." +
					
					"pour quitter le service contact appuyer longuement sur le bouton quitter");
			break;
			
		case R.id.btn_sms:
			vibration.vibrate(100);
			speakOut("service de messagerie, dans ce service on a un menu qui contient 3 bouton, un bouton pour �crire un nouveau message, " +
					"un bouton pour consulter la boite de r�ception, et un bouton pour quitter, " +
					"pour �couter taper une fois sur le bouton d�sir�, " +
					"pour envoyer un nouveau message, il  faut tout d'abord de taper longuement sur le bouton, nouveau message, " +
					"un autre menu s'affiche contenant 3 bouton pour choisir la m�thode de saisie du texte, " +
					"le premier bouton pour �crire le texte en language braille le deuxi�me bouton pour dicter le message vocalement, " +
					"cette option n�cessite une connexion internet, " +
					"si vous avez choisi d'�crire le texte manuellement appuyer longuement sur le bouton, �crire un SMS avec la language braille, " +
					"ainsi �crivez ce que vous voulez envoyer avec le clavier braille, et appuyer sur valider en bas au milieux du l'�cran, " +
					"si vous avez appuyer sur valider, un autre menu apparait demandant le destinataire, ce menu vous donne le choix 3 boutons" +
					" con�u pour �a, un bouton pour taper le num�ro de destinataire avec le pav� num�rique, " +
					"un autre bouton pour choisir le destinataire a partir de votre liste de contact," +
					" et un autre bouton qui permet de revenir au menu choix de la mani�re d'�criture, " +
					"si vous avez passer par �crire le num�ro par clavier vous saisissez le num�ro du destinataire en faisant un long appui" +
					" sur les num�ro, apr�s vous appuyez longuement sur valider pour envoyer, " +
					"si vous avez choisi de passer par choisir un contact vous pouvez parcourir votre liste de contact" +
					" et envoyer le message au contact d�sir�" +					
					"si vou avez choisi la maniere de dicter un message, un long appui se fait tout d'abor sur le bouton de dicter un message, " +
					"une petite v�rification se fait sur la connexion, " +
					"si vous avez une connexion, vous commencez a dicter, d�filer pour supprimer un mot si vous avez tromp�, " +
					"Sinon vous appuyer longuement sur le bouton choix destinataire ou bien le bouton quitter pour retourner vers le menu pr�c�dent" +
					
					"pour la consultation de la boite de r�c�ption il suffit d'appyer longuement sur le bouton, boite de r�ception, " +
					"ainsi vous d�filer pour parcourir la liste des messages enregistr�s sur votre appareil, " +
					"pour le retour il ya en bas le bouton retour, " +
					"et en fin pour quitter le service de messagerie, faites un long appui sur le bouton quitter");
			
			break;
			
		case R.id.btn_suivant:
			vibration.vibrate(100);
			Intent intent_menu = new Intent(this,Menu_Demo2.class);
			startActivity(intent_menu);
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
		
		case R.id.btn_clavier:
			speakOut("d�monstration du service clavier.");
			break;
			
		case R.id.btn_journal:
			speakOut("d�monstration du service journal.");
			break;
			
		case R.id.btn_contacts:
			speakOut("d�monstration du service contacts.");
			break;
			
		case R.id.btn_sms:
			speakOut("d�monstration du service messagerie.");
			break;
			
		case R.id.btn_suivant:
			speakOut("passer � la page suivante pour continuer");
			
			break;
			
		case R.id.btn_quitter:
			speakOut("quitter le service d�mo");
			break;
		}
		
	}


	private void unlockScreen() {
        Window window = this.getWindow();
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
    }
	
	@Override
	protected void onRestart() {
		unlockScreen();
		super.onRestart();
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
			speakOut("d�mo, premier menu");
			System.out.println("SCREEN TURNED ON");
		} else {
			// THIS IS WHEN ONRESUME() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
			speakOut("d�mo, premier menu");
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


