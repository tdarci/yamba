package com.frenchfry.yamba;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TweetActivity extends Activity implements OnClickListener, TextWatcher {
	
	public static final String TAG = TweetActivity.class.getSimpleName();
	public static final Integer MAX_TWEET_LEN = 140;

	private EditText editText;
	private Button goButton;
	private EditText remainingCharacters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		
		editText = (EditText) findViewById(R.id.editText);
		goButton = (Button) findViewById(R.id.buttonTweet);
		remainingCharacters = (EditText) findViewById(R.id.remainingCharacters);
		
		tweetTextChanged("");
		goButton.setOnClickListener(this);
		editText.addTextChangedListener(this);
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
		case R.id.itemSettings:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		case R.id.itemServiceStart:
			startService(new Intent(this, UpdaterService.class));
			break;
		case R.id.itemServiceStop:
			stopService(new Intent(this, UpdaterService.class));
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
		YambaApplication app = (YambaApplication) getApplication();
		TwitterPoster poster = new TwitterPoster(app.getTwitter(), getApplicationContext());
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

}
