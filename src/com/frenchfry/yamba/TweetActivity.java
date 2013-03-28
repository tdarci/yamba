package com.frenchfry.yamba;

import winterwell.jtwitter.Twitter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TweetActivity extends Activity implements OnClickListener, TextWatcher, OnSharedPreferenceChangeListener {
	
	public static final String TAG = TweetActivity.class.getSimpleName();
	public static final Integer MAX_TWEET_LEN = 140;
	public static final String DEFAULT_API_ROOT = "http://yamba.marakana.com/api";
//	public static final String DEFAULT_USERNAME = "td";
//	public static final String DEFAULT_PWD = "2%H&Fy15u#uf";
//	public static final String DEFAULT_PWD = "doofus";
	public static final String DEFAULT_USERNAME = "student";
	public static final String DEFAULT_PWD = "password";

	private EditText editText;
	private Button goButton;
	private Twitter twitter;
	private EditText remainingCharacters;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		
		editText = (EditText) findViewById(R.id.editText);
		goButton = (Button) findViewById(R.id.buttonTweet);
		remainingCharacters = (EditText) findViewById(R.id.remainingCharacters);
		
		tweetTextChanged("");
		goButton.setOnClickListener(this);
		editText.addTextChangedListener(this);
	}
	
	@SuppressWarnings("deprecation")
	private Twitter getTwitter() {
		if (twitter == null) {
			String username, password, apiRoot;
			username = prefs.getString("username", DEFAULT_USERNAME);
			password = prefs.getString("password", DEFAULT_PWD);
			apiRoot = prefs.getString("apiRoot", DEFAULT_API_ROOT);
			Log.d(TAG, "********** Logging in as '" + username + "' with pwd: " + password);
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(apiRoot);
		}
		return twitter;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		}
		return true;
	}

	public void tweetTextChanged(String newText) {
		int count = MAX_TWEET_LEN - newText.length();
		remainingCharacters.setText(Integer.toString(count));
		int textColor = Color.GREEN;
		if (count < 0) {
			textColor = Color.RED;
		} else if (count < 10) {
			textColor = Color.YELLOW;
		}
		remainingCharacters.setTextColor(textColor);
	}

	@Override
	public void onClick(View v) {
		String tweet = editText.getText().toString();
		TwitterPoster poster = new TwitterPoster(getTwitter(), getApplicationContext());
		poster.execute(tweet);
		editText.setText(null);
		Log.d(TAG, "Submitted tweet request: " + tweet);
	}

	@Override
	public void afterTextChanged(Editable s) {
		tweetTextChanged(s.toString());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// do nothing
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// do nothing
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Log.d(TAG, "*** OPTIONS CHANGED");
		twitter = null;
	}

}
