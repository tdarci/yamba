package com.frenchfry.yamba;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import winterwell.jtwitter.Status;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import winterwell.jtwitter.URLConnectionHttpClient;
import winterwell.jtwitter.User;

public class TwitterWrapper extends Twitter {

	private static final long serialVersionUID = 1L;
	private static int idCounter = 0;

	public TwitterWrapper(String screenName, String password) {
		super(screenName, new URLConnectionHttpClient(screenName, password));
	}

	@SuppressWarnings("deprecation")
	@Override
	public synchronized List<Status> getHomeTimeline() throws TwitterException {
		final int MAX_COUNT = 10;
		List<Status> statuses = new ArrayList<Status>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, (-1 * MAX_COUNT) + 1);
		for (int counter = 0 ; counter < MAX_COUNT ; counter++) {
			User user = new User("wookie");
			int id = idCounter++;
			String message = String.format(Locale.US, "I like this number... %d", id);
			cal.add(Calendar.SECOND, 1);
			Status s = new Status(user, message, id, cal.getTime());
			statuses.add(s);
		}
		return statuses;
	}
}
