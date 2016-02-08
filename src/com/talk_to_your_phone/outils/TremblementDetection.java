package com.talk_to_your_phone.outils;

import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;


public class TremblementDetection {

	public interface TremblementListener {
		public void onTremblementDetected();
	}


	private SensorListener mListener;
	private TremblementListener cb;
	private SensorManager sensorManager;

	public TremblementDetection(Context context, TremblementListener callback) {
		cb = callback;
		mListener = new SensorListener() {

			private final double tremblementForce = .8;
			private final int Count = 2;
			int shakeCount = 0;
			boolean lastShakePositive = false;
			private int shakeCountTimeout = 500;

			public void onSensorChanged(int sensor, float[] values) {
				if ((values[1] > tremblementForce) && !lastShakePositive) {
					(new Thread(new resetShakeCount())).start();
					shakeCount++;
					lastShakePositive = true;
				} else if ((values[1] < -tremblementForce) && lastShakePositive) {
					(new Thread(new resetShakeCount())).start();
					shakeCount++;
					lastShakePositive = false;
				}
				if (shakeCount > Count) {
					shakeCount = 0;
					cb.onTremblementDetected();
					
				}
			}

			public void onAccuracyChanged(int arg0, int arg1) {
			}

			class resetShakeCount implements Runnable {
				public void run() {
					try {
						Thread.sleep(shakeCountTimeout);
						shakeCount = 0;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};


		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(mListener, SensorManager.SENSOR_ACCELEROMETER,SensorManager.SENSOR_DELAY_FASTEST);
	}

	public void shutdown() {
		sensorManager.unregisterListener(mListener);
	}
	
	

}
