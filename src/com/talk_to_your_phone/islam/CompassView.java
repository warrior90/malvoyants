package com.talk_to_your_phone.islam;

import com.project.talk_to_your_phone.R;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import android.os.Handler;
import android.os.SystemClock;

import android.util.AttributeSet;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

/**
 * Class description
 *
 *
 */

public class CompassView extends View {
	//~--- fields -------------------------------------------------------------

	
	private String pos;
	private float northOrientation = 0;
	public float qiblaOrientation = 0;

	private Paint circlePaint;
	private Paint northPaint;
	private Paint southPaint;
	private Paint qiblaPaint;

	private double Q_or;
	
	
	
	private Path trianglePath;

	//Délais entre chaque image
	private final int DELAY = 50;
	//Durée de l'animation
	private final int DURATION = 1000;

	private float startNorthOrientation;
	private float endNorthOrientation;
	private float startQiblaOrientation;
	private float endQiblaOrientation;

	//Heure de début de l’animation (ms)
	private long startTime;

	//Pourcentage d'évolution de l'animation
	private float perCent;
	//Temps courant
	private long curTime;
	//Temps total depuis le début de l'animation
	private long totalTime;
	public double rot;

	private Runnable animationTask = new Runnable() {
		public void run() {
			curTime   = SystemClock.uptimeMillis();
			totalTime = curTime - startTime;

			if (totalTime > DURATION) {
				northOrientation = endNorthOrientation % 360;
				qiblaOrientation = endQiblaOrientation %360;
				removeCallbacks(animationTask);
			} else {
				perCent = ((float) totalTime) / DURATION;

				// Animation plus réaliste de l'aiguille
				perCent          = (float) Math.sin(perCent * 1.5);
				perCent          = Math.min(perCent, 1);
				northOrientation = (float) (startNorthOrientation + perCent * (endNorthOrientation - startNorthOrientation));
				qiblaOrientation = (float) (startQiblaOrientation + perCent * (endQiblaOrientation - startQiblaOrientation));
				
				postDelayed(this, DELAY);
			}

			// on demande à notre vue de se redessiner
			invalidate();
		}
	};

	//~--- constructors -------------------------------------------------------

	// Constructeur par défaut de la vue
	public CompassView(Context context) {
		super(context);
		initView();
		

	}

	// Constructeur utilisé pour instancier la vue depuis sa
	// déclaration dans un fichier XML
	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	// idem au précédant
	public CompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	//~--- get methods --------------------------------------------------------

	// permet de récupérer l'orientation de la boussole
	public float getNorthOrientation() {
		return northOrientation;
	}

	//permet de récupérer l'orientation de la boussole
	public float getQiblaOrientation() {
		return qiblaOrientation;
	}

	//~--- set methods --------------------------------------------------------

	// permet de changer l'orientation de la boussole
	public void setNorthOrientation(float rotation) {

		// on met à jour l'orientation uniquement si elle a changé
		if (rotation != this.northOrientation) {
			//Arrêter l'ancienne animation
			removeCallbacks(animationTask);

			//Position courante
			this.startNorthOrientation = this.northOrientation;
			//Position désirée
			this.endNorthOrientation   = rotation;

			//Détermination du sens de rotation de l'aiguille
			if ( ((startNorthOrientation + 180) % 360) > endNorthOrientation)
			{
				//Rotation vers la gauche
				if ( (startNorthOrientation - endNorthOrientation) > 180 )
				{
					endNorthOrientation+=360;
					
				}
			} else {
				//Rotation vers la droite
				if ( (endNorthOrientation - startNorthOrientation) > 180 )
				{
					startNorthOrientation+=360;
					
				}
			}

			//Nouvelle animation
			startTime = SystemClock.uptimeMillis();
			postDelayed(animationTask, DELAY);
		}
	}


	//permet de changer l'orientation de la boussole
	public void setQiblaOrientation(float rotation) {

		// on met à jour l'orientation uniquement si elle a changé
		if (rotation != this.qiblaOrientation) {
			//Arrêter l'ancienne animation
			removeCallbacks(animationTask);

			//Position courante
			this.startQiblaOrientation = this.qiblaOrientation;
			//Position désirée
			this.endQiblaOrientation   = rotation;
			
			
			
			if(this.startQiblaOrientation-this.endQiblaOrientation >=89 || 
				this.startQiblaOrientation-	this.endQiblaOrientation <=91){
				setPos("qibla");
				
			}
			
			if(this.qiblaOrientation < 89){
				setPos("droite");
			}
			
			if(this.qiblaOrientation > 91){
				setPos("gauche");
			}
			
			Log.i("qibla",String.valueOf(getQiblaOrientation()));
			
			//Détermination du sens de rotation de l'aiguille
			if ( ((startQiblaOrientation + 180) % 360) > endQiblaOrientation)
			{
				//Rotation vers la gauche
				if ( (startQiblaOrientation - endQiblaOrientation) > Math.abs(getQ_or()) )
				{
					endQiblaOrientation+=360;
					rot = qiblaOrientation;
					//Log.i("qibla gauche", String.valueOf(qiblaOrientation));
				}
			} else {
				//Rotation vers la droite
				if ( (endQiblaOrientation - startQiblaOrientation) > Math.abs(getQ_or()) )
				{
					startQiblaOrientation+=360;
					rot = qiblaOrientation;
					//Log.i("qibla droite", String.valueOf(startQiblaOrientation));
				}
			}

			//Nouvelle animation
			startTime = SystemClock.uptimeMillis();
			postDelayed(animationTask, DELAY);
		}
	}

	//~--- methods ------------------------------------------------------------

	// Initialisation de la vue
	private void initView() {
		Resources r = this.getResources();

		// Paint pour l'arrière plan de la boussole
		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(r.getColor(R.color.compassCircle));

		// Paint pour les 2 aiguilles, Nord et Sud
		northPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		northPaint.setColor(r.getColor(R.color.northPointer));
		southPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		southPaint.setColor(r.getColor(R.color.southPointer));

		// Paint pour l'aiguilles Qibla
		qiblaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		qiblaPaint.setColor(r.getColor(R.color.northPointer));

		
		
		// Path pour dessiner les aiguilles
		trianglePath = new Path();
	}

	// Permet de définir la taille de notre vue
	// /!\ par défaut un cadre de 100x100 si non redéfini
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredWidth  = measure(widthMeasureSpec);
		int measuredHeight = measure(heightMeasureSpec);

		// Notre vue sera un carré, on garde donc le minimum
		int d = Math.min(measuredWidth, measuredHeight);

		setMeasuredDimension(d, d);
	}

	// Déterminer la taille de notre vue
	private int measure(int measureSpec) {
		int result   = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {

			// Taille par défaut
			result = 200;
		} else {

			// On va prendre la taille de la vue parente
			result = specSize;
		}

		return result;
	}

	// Appelée pour redessiner la vue
	@Override
	protected void onDraw(Canvas canvas) {
		int centerX = getMeasuredWidth() / 2;
		int centerY = getMeasuredHeight() / 2;

		// On détermine le diamètre du cercle (arrière plan de la boussole)
		int radius = Math.min(centerX, centerY);

		canvas.drawCircle(centerX, centerY, radius, circlePaint);
	

		// On sauvegarde la position initiale du canvas
		canvas.save();

		// On tourne le canvas pour que le nord pointe vers le haut
		canvas.rotate(-northOrientation, centerX, centerY);

		// on créer une forme triangulaire qui part du centre du cercle et
		// pointe vers le haut
		trianglePath.reset();    // RAZ du path (une seule instance)
		trianglePath.moveTo(centerX, 10);
		trianglePath.lineTo(centerX - 10, centerY);
		trianglePath.lineTo(centerX + 10, centerY);

		// On désigne l'aiguille Nord
		//canvas.drawPath(trianglePath, northPaint);

		// On tourne notre vue de 180° pour désigner l'auguille Sud
		//canvas.rotate(180, centerX, centerY);
		//canvas.drawPath(trianglePath, southPaint);

		// On tourne notre vue de 159.09° pour désigner l'auguille qibla
		canvas.rotate(90, centerX, centerY);
		canvas.drawPath(trianglePath, qiblaPaint);

		// On restaure la position initiale (inutile, mais prévoyant)
		canvas.restore();
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public double getQ_or() {
		return Q_or;
	}

	public void setQ_or(double q_or) {
		Q_or = q_or;
	}
	
	/*
	 * Formule de Qibla:
	 * Q = atan2(cos(B)*tan(C)-sin(B)*cos(A) , sin(A)) 
	 * 
	 * Si le dénominateur est négatif :
		-	Ajouter 180° à Q si le numérateur est positif
		-	Retranchez 180° à Q si le numérateur est négatif
	 * 
	 *	A = Différence de longitudes entre La Mecque et le lieu de la prière ;
	 	B = Latitude du lieu de la prière ;
	 	C = Latitude de la Kaaba.
	 	
	 	La latitude doit être comptée positive au Nord et négative au Sud.
	 	La longitude doit être comptée positive à l'Est et négative à l'Ouest
	 	
	 	Résultat = 
	 	
	 	Q = Direction de la qibla
		Q > 0 angle partant de la direction du Nord et compté dans le sens rétrograde 
			(sens des aiguilles d'une montre).
		Q < 0 angle partant de la direction du Nord et compté dans le sens direct 
			(sens inverse des aiguilles d'une montre, sens trigonométrique).
			
		Mecca: 	lat = 21.4266667
				Long = 39.8261111
				
		Kaaba:	Lat = 21.422609
				long = 93.826168
		
		variables utilisées:
			long(maPosition)
			lat(maPosition)
			
		A = long(Mecca) - long(maPosition)
		B = lat(maPosition)
		C = lat(kaaba)
	 */	
}

