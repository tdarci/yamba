package com.frenchfry.yamba;

import java.util.List;
import java.util.Locale;

import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.ContentValues;
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
	private boolean serviceRunning;
	private TweetRepository tweetRepository;
	
	public TweetRepository getTweetRepository() {
		if (tweetRepository == null) {
			this.tweetRepository = new TweetRepository(this);
		}
		return tweetRepository;
	}
	
	public synchronized int fetchTweetUpdates() {
		Log.d( TAG, "fetching tweets");
		Twitter twitter = this.getTwitter();
		if (twitter == null) {
			Log.d( TAG, "twitter busted");
			return 0;
		}
		try {
			List<Status> timeline = twitter.getHomeTimeline();
			long latestTweetTime = this.getTweetRepository().getLatestTweetCreatedAtTime();
			int tweetsInserted = 0;
			
			ContentValues rowVals = new ContentValues();
			
			for (Status s : timeline) {
				
				rowVals.clear();
				int curId = s.id.intValue();
				rowVals.put( TweetRepository.COL_ID, curId);
				long createdAt = s.createdAt.getTime();
				rowVals.put( TweetRepository.COL_CREATED_AT, createdAt);
				rowVals.put( TweetRepository.COL_TEXT, s.text);
				rowVals.put( TweetRepository.COL_USER, s.user.screenName);
				if (latestTweetTime < createdAt) {
					Log.d(TAG, String.format(Locale.US, "%s: %s", s.user.screenName, s.text));
					this.getTweetRepository().insertOrIgnore(rowVals);
					tweetsInserted++;
				}
			}
			return tweetsInserted;
		} catch (RuntimeException e) {
			Log.e( TAG, "Failed to fetch tweet updates.", e);
			return 0;
		}
	}
	
	public boolean isServiceRunning() {
		return this.serviceRunning;
	}
	
	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}
	
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

	public synchronized Twitter getTwitter() {
		if (this.twitter == null) {
			String username, password, apiRoot;
			username = prefs.getString("username", DEFAULT_USERNAME);
			password = prefs.getString("password", DEFAULT_PWD);
			apiRoot = prefs.getString("apiRoot", DEFAULT_API_ROOT);
			
			Log.i(TAG, "Logging in as '" + username + "'.");
			Twitter.WORRIED_ABOUT_TWITTER = true;
			this.twitter = new TwitterWrapper(username, password);
			this.twitter.setAPIRootUrl(apiRoot);
		}
		return this.twitter;
	}

	
	
}
