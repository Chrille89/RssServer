package de.bach.thwildau.rss.server.operations;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

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
