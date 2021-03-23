package de.bach.thwildau.rss.server.RssServer;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import de.bach.thwildau.rss.server.model.Feed;
import de.bach.thwildau.rss.server.operations.GameStarRSSReader;
import de.bach.thwildau.rss.server.operations.GolemRSSReader;
import de.bach.thwildau.rss.server.operations.HeiseRSSReader;
import de.bach.thwildau.rss.server.operations.RSSReader;
import de.bach.thwildau.rss.server.operations.TagesschauNews;

@Controller
@EnableAutoConfiguration
@SpringBootConfiguration
@SpringBootApplication
public class RssServerApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(RssServerApplication.class);
	}

	@CrossOrigin
	@RequestMapping("/")
	@ResponseBody
	public String test() {
		return "The RssServerApplication is now running.";
	}

	@CrossOrigin
	@RequestMapping("/golem")
	@ResponseBody
	public List<Feed> getGolemNews() {
		System.out.println("Call /golem");
		RSSReader news = GolemRSSReader.getInstance();
		return news.getFeedList();
	}

	@CrossOrigin
	@RequestMapping("/heise")
	@ResponseBody
	public List<Feed> getHeiseNews() {
		System.out.println("Call /heise");
		RSSReader news = HeiseRSSReader.getInstance();
		return news.getFeedList();
	}

	@CrossOrigin
	@RequestMapping("/gamestar")
	@ResponseBody
	public List<Feed> getGameStarNews() {
		System.out.println("Call /ct");
		RSSReader news = GameStarRSSReader.getInstance();
		return news.getFeedList();
	}

	@CrossOrigin
	@RequestMapping("/tagesschau")
	@ResponseBody
	public List<Feed> getTagesschauNews() {
		System.out.println("Call /tagesschau");
		RSSReader news = TagesschauNews.getInstance();
		return news.getFeedList();
	}

	public static void main(String[] args) {
		SpringApplication.run(RssServerApplication.class, args);
	}
}
