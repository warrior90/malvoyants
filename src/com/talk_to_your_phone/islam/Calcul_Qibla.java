package com.talk_to_your_phone.islam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class Calcul_Qibla{
	
	private int qibla_or;
	private double ma_latitude;
	private double ma_longitude;
	private final double mecca_lat = 21.4266667;
	private final double mecca_long = 39.8261111;
	private final double kaaba_lat = 21.422609;
	private final double kaaba_long = 39.826168;
	GpsTracker gps;
	
	public Calcul_Qibla(double ma_lat , double ma_long){
		
		ma_latitude = ma_lat;
		ma_longitude = ma_long;
		 
	}
	


	public double getMecca_lat() {
		return mecca_lat;
	}


	public double getMecca_long() {
		return mecca_long;
	}


	public double getMa_latitude() {
		return ma_latitude;
	}


	public void setMa_latitude(double ma_latitude) {
		this.ma_latitude = ma_latitude;
	}


	public double getMa_longitude() {
		return ma_longitude;
	}


	public void setMa_longitude(double ma_longitude) {
		this.ma_longitude = ma_longitude;
	}


	public int getQibla_or() {
		return qibla_or;
	}


	public void setQibla_or(int qibla_or) {
		this.qibla_or = qibla_or;
	}


	public double getKaaba_lat() {
		return kaaba_lat;
	}


	public double getKaaba_long() {
		return kaaba_long;
	}
	
	public double calcul(){
		
		double A = getMecca_long() - getMa_longitude();
		double B = getMa_latitude();
		double C = getKaaba_lat();
		
		/*Formule de Qibla:
		 * Q = atan2(cos(B)*tan(C)-sin(B)*cos(A) , sin(A))*/ 
		
		double Q = Math.atan2(Math.cos(B)*Math.tan(C)-Math.sin(B)*Math.cos(A), Math.sin(A));
		
		
		return Q*180/Math.PI;
		
	}

}
