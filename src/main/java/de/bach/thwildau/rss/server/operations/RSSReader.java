package de.bach.thwildau.rss.server.operations;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndContentImpl;
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

			entries.forEach(feedEntry -> {
				String title = feedEntry.getTitle();
				String description = feedEntry.getDescription().getValue();
				String link = feedEntry.getLink();

				String pictureUrl = "";
				String contentValue = "";

				if (!feedEntry.getContents().isEmpty()) {
					SyndContentImpl content = (SyndContentImpl) feedEntry.getContents().get(0);
					contentValue = content.getValue();
				}

				if (description.contains("img")) {
					pictureUrl = getImageFromDescription(description);
					description = description.substring(description.indexOf("</a>") + 5);
					description = description.trim();
				} else if (contentValue.contains("img")) {
					pictureUrl = getImageFromContentValue(contentValue);

				}

				feedList.add(new Feed(title, description, link, pictureUrl));
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

	private String getImageFromContentValue(String contentValue) {
		String ending = "";
		if (contentValue.contains("jpeg")) {
			ending = "jpeg";
		} else if (contentValue.contains("jpg")) {
			ending = "jpg";
		} else if (contentValue.contains("png")) {
			ending = "png";
		} else {
			throw new IndexOutOfBoundsException("Could not found image ending for contentValue: " + contentValue);
		}
		return contentValue.substring(contentValue.indexOf("src=") + 5, contentValue.indexOf(ending) + ending.length());

	}

	private String getImageFromDescription(String description) {
		String ending = "";
		if (description.contains("jpeg")) {
			ending = "jpeg";
		} else if (description.contains("jpg")) {
			ending = "jpg";
		} else if (description.contains("png")) {
			ending = "png";
		} else {
			throw new IndexOutOfBoundsException("Could not found image ending for contentValue: " + description);
		}
		return description.substring(description.indexOf("src=") + 5, description.indexOf(ending) + ending.length());
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
