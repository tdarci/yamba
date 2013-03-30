package com.frenchfry.yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class YambaActivity extends Activity {
	
	public static final String TAG = YambaActivity.class.getSimpleName();
	protected YambaApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.app = (YambaApplication) this.getApplication();
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
			this.startActivity(new Intent(this, PrefsActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.itemPurge:
			this.app.getTweetRepository().delete();
			Toast.makeText(this, R.string.msgAllDataPurged, Toast.LENGTH_LONG).show();
			break;
		case R.id.itemTimeline:
			this.startActivity(new Intent(this, TimelineActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.itemTweet:
			this.startActivity(new Intent(this, TweetActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
			break;
		case R.id.itemServiceStart:
			this.startService(new Intent(this, UpdaterService.class));
			break;
		case R.id.itemServiceStop:
			this.stopService(new Intent(this, UpdaterService.class));
			break;
		}
		return true;
	}

}
