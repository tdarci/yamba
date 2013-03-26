package com.frenchfry.yamba;

import winterwell.jtwitter.Twitter;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TweetActivity extends Activity implements OnClickListener {
	
	public static final String TAG = "tweet";
	EditText editText;
	Button goButton;
	Twitter twitter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);
		
		editText = (EditText) findViewById(R.id.editText);
		goButton = (Button) findViewById(R.id.buttonTweet);
		goButton.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		String tweet = editText.getText().toString();
		TwitterPoster poster = new TwitterPoster(twitter, getApplicationContext());
		poster.execute(tweet);
		Log.d(TAG, "Submitted tweet request: " + tweet);
	}

}
