package de.bach.thwildau.rss.server.operations;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import de.bach.thwildau.rss.server.model.Feed;

public abstract class RSSReader {

	protected String feedUrlString;
	protected List<Feed> feedList;
	protected Date startDate;

	protected RSSReader(String feedUrlString) {
		this.feedUrlString = feedUrlString;
		this.feedList = new CopyOnWriteArrayList<>();
		this.startDate = new Date();
	}

	protected void loadRSSFeeds() {
		try {
			System.out.println("loadRSSFeeds()");
			startDate = new Date();
			feedList.clear();
			InputStream is = new URL(this.feedUrlString).openConnection().getInputStream();
			InputSource source = new InputSource(is);

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(source);
			List<SyndEntryImpl> entries = feed.getEntries();

			entries.forEach(feedEntry -> feedList.add(
					new Feed(feedEntry.getTitle(), feedEntry.getDescription().getValue(), feedEntry.getLink(), "")));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected boolean getFromCache() {
		if (!this.feedList.isEmpty() && new Date().getTime() - this.startDate.getTime() < 3600000) {
			System.out.println("Get From Cache...");
			return true;
		}
		System.out.println("Create Feedlist...");
		return false;
	}

	public List<Feed> getFeedList() {
		if (!getFromCache()) {
			loadRSSFeeds();
		}
		return this.feedList;
	}

}
