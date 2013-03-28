package com.frenchfry.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener {

	private static final String TAG = YambaApplication.class.getSimpleName();
	// note: tom's account is td/doofus
	public static final String DEFAULT_USERNAME = "student";
	public static final String DEFAULT_PWD = "password";
	public static final String DEFAULT_API_ROOT = "http://yamba.marakana.com/api";

	private Twitter twitter;
	private SharedPreferences prefs;
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.d(TAG, "Options changed.");
		this.twitter = null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.prefs.registerOnSharedPreferenceChangeListener(this);
		Log.i(TAG, "onCreate()");
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminate()");
	}

	@SuppressWarnings("deprecation")
	public synchronized Twitter getTwitter() {
		if (this.twitter == null) {
			String username, password, apiRoot;
			username = prefs.getString("username", DEFAULT_USERNAME);
			password = prefs.getString("password", DEFAULT_PWD);
			apiRoot = prefs.getString("apiRoot", DEFAULT_API_ROOT);
			
			Log.i(TAG, "Logging in as '" + username + "'.");
			this.twitter = new Twitter(username, password);
			this.twitter.setAPIRootUrl(apiRoot);
		}
		return this.twitter;
	}

	
	
}
