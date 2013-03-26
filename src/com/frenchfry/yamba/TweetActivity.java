package com.frenchfry.yamba;

import winterwell.jtwitter.Twitter;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TweetActivity extends Activity implements OnClickListener, TextWatcher {
	
	public static final String TAG = "tweet";
	public static final Integer MAX_TWEET_LEN = 140;
	
	EditText editText;
	Button goButton;
	Twitter twitter;
	EditText remainingCharacters;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		
		editText = (EditText) findViewById(R.id.editText);
		goButton = (Button) findViewById(R.id.buttonTweet);
		remainingCharacters = (EditText) findViewById(R.id.remainingCharacters);
		
		remainingCharacters.setBackgroundColor(Color.BLACK);
		
		tweetTextChanged("");
		goButton.setOnClickListener(this);
		editText.addTextChangedListener(this);
//		twitter = new Twitter("student", "password");
		twitter = new Twitter("td", "2%H&Fy15u#uf");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet, menu);
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
		TwitterPoster poster = new TwitterPoster(twitter, getApplicationContext());
		poster.execute(tweet);
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
