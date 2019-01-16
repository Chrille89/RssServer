package de.bach.thwildau.rss.server.operations;

import java.util.List;

import de.bach.thwildau.rss.server.model.Feed;

public class HeiseRSSReader extends RSSReader {

	private static final String heiseRSSFeed = "https://www.heise.de/developer/rss/news-atom.xml";
	private static RSSReader uniqueInstance;

	private HeiseRSSReader(String url) {
		super(url);
	}

	public static RSSReader getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new HeiseRSSReader(heiseRSSFeed);
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

