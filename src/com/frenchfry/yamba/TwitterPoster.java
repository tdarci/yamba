package com.frenchfry.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TwitterPoster extends AsyncTask<String, Integer, String> {
	
	private static final String TAG = "TwitterPoster";
	private final Twitter twitter;
	private final Context context;

	public TwitterPoster(Twitter twitter, Context context) {
		this.twitter = twitter;
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... params) {
		try {
			winterwell.jtwitter.Status status = twitter.updateStatus(params[0]);
			Log.d(TAG, "Tweeted: " + status.text);
			return status.text;
		} catch (TwitterException e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute( String result) {
		String toastMessage = "We tweeted: " + result;
		Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show();
	}

}
