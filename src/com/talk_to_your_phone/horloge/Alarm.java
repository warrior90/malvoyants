package com.talk_to_your_phone.horloge;

import java.io.Serializable;

import android.content.Context;
import android.text.format.Time;

public class Alarm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//private Time heure;
	private String hh;
	private String mn;
	private boolean active;
	
	/*public Time getHeure() {
		return heure;
	}
	public void setHeure(Time heure) {
		this.heure = heure;
	}*/
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getheures(){
		return hh;
	}
	public String getminutes(){
		return mn;
	}
	
	public void setheure(String heure){
		this.hh = heure;
	}
	public void setminute(String minute){
		this.mn = minute;
	}
}
