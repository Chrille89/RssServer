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

	@RequestMapping("/")
	@ResponseBody
	public String test() {
		return "Hallo Welt";
	}

	@RequestMapping("/tagesschaunews")
	@ResponseBody
	public List<Feed> getTagesschauNews() {
		System.out.println("Call /tagesschaunews");
		RSSReader news = TagesschauNews.getInstance();
		return news.getFeedList();
	}

	@RequestMapping("/golemnews")
	@ResponseBody
	public List<Feed> getGolemNews() {
		System.out.println("Call /golemnews");
		RSSReader news = GolemRSSReader.getInstance();
		return news.getFeedList();
	}

	public static void main(String[] args) {
		SpringApplication.run(RssServerApplication.class, args);
	}
}
