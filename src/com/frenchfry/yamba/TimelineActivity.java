package com.frenchfry.yamba;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.frenchfry.yamba.TweetRepository.DbHelper;

public class TimelineActivity extends YambaActivity {

	private DbHelper dbHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
//	private TextView textTimeline;
	private ListView listTimeline;
	private TimelineAdapter cursorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.timeline);
//		this.textTimeline = (TextView) findViewById(R.id.textTimeline);
		this.listTimeline = (ListView) findViewById(R.id.listTimeline);
		this.dbHelper = new DbHelper(this);
		this.db = dbHelper.getReadableDatabase();
		
		if (app.getPrefs().getString("username", null) == null) {
			this.startActivity(new Intent(this, PrefsActivity.class));
			Toast.makeText(this, R.string.msgSetupPrefs, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		this.cursor = this.db.query(TweetRepository.TABLE_NAME, null, null, null, null, null, TweetRepository.COL_CREATED_AT + " DESC");
		this.startManagingCursor(cursor);
		this.cursorAdapter = new TimelineAdapter(this, this.cursor);
		this.listTimeline.setAdapter(this.cursorAdapter);
		
//		String user;
//		String text;
//		String output;
//		
//		while (this.cursor.moveToNext()) {
//			user = this.cursor.getString(this.cursor.getColumnIndex(TweetRepository.COL_USER));
//			text = this.cursor.getString(this.cursor.getColumnIndex(TweetRepository.COL_TEXT));
//			output = String.format(Locale.US, "%s: %s\n", user, text);
//			this.textTimeline.append(output);
//		}
	}
	
}
