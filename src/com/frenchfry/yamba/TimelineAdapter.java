package com.frenchfry.yamba;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TimelineAdapter extends SimpleCursorAdapter {

	private static final String[] FROM_ARRAY = { TweetRepository.COL_CREATED_AT, TweetRepository.COL_USER, TweetRepository.COL_TEXT};
	private static final int[] TO_ARRAY = { R.id.textCreatedAt, R.id.textUser, R.id.textText};

	@SuppressWarnings("deprecation")
	public TimelineAdapter( Context context, Cursor cursor) {
		super( context, R.layout.row, cursor, FROM_ARRAY, TO_ARRAY);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		long timestamp = cursor.getLong(cursor.getColumnIndex(TweetRepository.COL_CREATED_AT));
		TextView textCreatedAt = (TextView) view.findViewById(R.id.textCreatedAt);
		textCreatedAt.setText(DateUtils.getRelativeTimeSpanString(timestamp));
	}
	
}
