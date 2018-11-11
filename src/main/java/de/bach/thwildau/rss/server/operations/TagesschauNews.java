package de.bach.thwildau.rss.server.operations;

import java.util.List;

import de.bach.thwildau.rss.server.model.Feed;

public class TagesschauNews extends RSSReader {
	
	 public TagesschauNews(String url) {
		super(url);
	}
	 
	@Override
	public List<Feed> loadRSSFeeds() {
		// TODO Auto-generated method stub
		return super.loadRSSFeeds();
	}
}
