package de.bach.thwildau.rss.server.operations;

import java.util.List;

import de.bach.thwildau.rss.server.model.Feed;

public class TagesschauNews extends RSSReader {

	private static final String tagesschauRSSFeed = "http://www.tagesschau.de/xml/rss2";
	private static RSSReader uniqueInstance;

	private TagesschauNews(String url) {
		super(url);
	}

	public static RSSReader getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new TagesschauNews(tagesschauRSSFeed);
		}
		return uniqueInstance;
	}

	@Override
	protected void loadRSSFeeds() {
		super.loadRSSFeeds();
	}

	@Override
	public List<Feed> getFeedList() {
		if (!getFromCache()) {
			this.loadRSSFeeds();
		}
		return feedList;
	}
}
