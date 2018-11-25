package de.bach.thwildau.rss.server.operations;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;

import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

import de.bach.thwildau.rss.server.model.Feed;

public abstract class RSSReader {
	
	protected String feedUrlString;
	
	public RSSReader(String feedUrlString) {
		this.feedUrlString = feedUrlString;
	}	

	public List<Feed> loadRSSFeeds(){
		List<Feed> feedList = new ArrayList<>();	
		try {
		        InputStream is = new URL(this.feedUrlString).openConnection().getInputStream();
		        InputSource source = new InputSource(is);
		        
				SyndFeedInput input = new SyndFeedInput();
		        SyndFeed feed = input.build(source);
		        List<SyndEntryImpl> entries = feed.getEntries();
		        
		        entries.forEach(feedEntry -> feedList.add(new Feed(feedEntry.getTitle(),
		        		feedEntry.getDescription().getValue(),
		        		feedEntry.getLink(),
		        		"")));	  
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
