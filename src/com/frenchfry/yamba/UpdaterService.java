package com.frenchfry.yamba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	private static final String TAG = UpdaterService.class.getSimpleName();
	private static final int DELAY_MS = 60000;
	public boolean runFlag = false;
	private Updater updater;
	private YambaApplication app;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		super.onCreate();
		this.app = (YambaApplication) getApplication();
		this.updater = new Updater(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d(TAG, "onStartCommand()");
		this.runFlag = true;
		this.updater.start();
		this.app.setServiceRunning(true);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		this.app.setServiceRunning(false);
		Log.d(TAG, "onDestroy()");
	}

	static private class Updater extends Thread {

		private static final String TAG = Updater.class.getSimpleName();
		private final UpdaterService updaterService;
		
		public Updater(UpdaterService updaterService) {
			super("UpdaterService-Updater");
			this.updaterService = updaterService;
		}

		@Override
		public void run() {
			while (this.updaterService.runFlag) {
				Log.d(TAG, "updater running...");
				try {
					int newTweetCount = this.updaterService.app.fetchTweetUpdates();
					if (newTweetCount > 0) {
						Log.d( TAG, String.format("We have %d new tweets.", newTweetCount));
					}
					Thread.sleep(DELAY_MS);
				} catch (InterruptedException e) {
					this.updaterService.runFlag = false;
				}
			}
		}
	}

}
