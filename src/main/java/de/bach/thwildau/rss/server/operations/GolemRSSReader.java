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

public class GolemRSSReader extends RSSReader {

	private static final String golemRSSFeed = "https://rss.golem.de/rss.php?feed=RSS2.0";
	private static RSSReader uniqueInstance;

	private GolemRSSReader(String url) {
		super(url);
	}

	public static RSSReader getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new GolemRSSReader(golemRSSFeed);
		}
		return uniqueInstance;
	}

	@Override
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
			entries.forEach(feedEntry -> {
				SyndContentImpl content = (SyndContentImpl) feedEntry.getContents().get(0);
				String pictureUrl = content.getValue().split("\"")[1];
				feedList.add(
						new Feed(feedEntry.getTitle(),
								feedEntry.getDescription().getValue().substring(0,
										feedEntry.getDescription().getValue().indexOf("(")),
								feedEntry.getLink(), pictureUrl));
			});

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

	@Override
	public List<Feed> getFeedList() {
		if (!getFromCache()) {
			this.loadRSSFeeds();
		}
		return feedList;
	}

}
