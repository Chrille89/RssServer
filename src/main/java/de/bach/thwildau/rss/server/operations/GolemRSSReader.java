package de.bach.thwildau.rss.server.operations;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import de.bach.thwildau.rss.server.model.Feed;

public class GolemRSSReader extends RSSReader {
	
	 public GolemRSSReader(String url) {
		super(url);
	}
	 
	@Override
	public List<Feed> loadRSSFeeds() {
		List<Feed> feedList = new ArrayList<>();	
		try {
		        InputStream is = new URL(this.feedUrlString).openConnection().getInputStream();
		        InputSource source = new InputSource(is);
		        
				SyndFeedInput input = new SyndFeedInput();
		        SyndFeed feed = input.build(source);
		        List<SyndEntryImpl> entries = feed.getEntries();
		        entries.forEach(feedEntry -> {
		        	SyndContentImpl content = (SyndContentImpl) feedEntry.getContents().get(0);
		        	String pictureUrl= content.getValue().split("\"")[1];
		        	feedList.add(new Feed(feedEntry.getTitle(),
		        			              feedEntry.getDescription().getValue().substring(0, feedEntry.getDescription().getValue().indexOf("(")),
	        		feedEntry.getLink(),
	        		pictureUrl));
		        });
		        		
		        System.out.println(feedList);
	            return feedList;
			} catch (MalformedURLException e) {
				//logger.log(LogLevel.WARN,"Cannot parse RSS-Document! Wrong URL! "+ExceptionUtils.exceptionStackTraceAsString(e));
			} catch (IllegalArgumentException e) {
			//	logger.log(LogLevel.WARN,"Cannot parse RSS-Document! Illegal Argument! "+ExceptionUtils.exceptionStackTraceAsString(e));
			} catch (FeedException e) {
			//	logger.log(LogLevel.WARN,"Cannot parse RSS-Document! "+ExceptionUtils.exceptionStackTraceAsString(e));
			} catch (IOException e) {
			//	logger.log(LogLevel.WARN,"I/O-Error! "+ExceptionUtils.exceptionStackTraceAsString(e));
			}
			return feedList;
	}

}
