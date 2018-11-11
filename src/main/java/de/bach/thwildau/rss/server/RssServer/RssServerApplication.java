package de.bach.thwildau.rss.server.RssServer;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.bach.thwildau.rss.server.model.Feed;
import de.bach.thwildau.rss.server.operations.GolemRSSReader;
import de.bach.thwildau.rss.server.operations.RSSReader;
import de.bach.thwildau.rss.server.operations.TagesschauNews;

@Controller
@EnableAutoConfiguration
@SpringBootConfiguration
public class RssServerApplication {
	
	private static final String golemRSSFeed = "https://rss.golem.de/rss.php?feed=RSS2.0";
	private static final String tagesschauRSSFeed = "http://www.tagesschau.de/xml/rss2";
	
	@RequestMapping("/")
	@ResponseBody
	public String test() {
		return "Hallo Welt";
	}
	
	@RequestMapping("/tagesschaunews")
	@ResponseBody
	public List<Feed> getTagesschauNews() {
		RSSReader news = new TagesschauNews(tagesschauRSSFeed);
		return news.loadRSSFeeds();
	}
	
	@RequestMapping("/golemnews")
	@ResponseBody
	public List<Feed> getGolemNews() {
		RSSReader news = new GolemRSSReader(golemRSSFeed);
		return news.loadRSSFeeds();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RssServerApplication.class, args);
	}
}
