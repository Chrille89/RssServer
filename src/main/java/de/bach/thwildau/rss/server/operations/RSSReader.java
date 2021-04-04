package de.bach.thwildau.rss.server.operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

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

        // fetch data from URL
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        BufferedReader reader = getResponseRSS(httpClient, this.feedUrlString);
        feed = input.build(reader);

        reader.close();
        httpClient.close();

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

    } catch(MalformedURLException e) {
        e.printStackTrace();
    } catch(IllegalArgumentException e) {
        e.printStackTrace();
    } catch(FeedException e) {
        e.printStackTrace();
    } catch(IOException e) {
        e.printStackTrace();
    }
}

    private BufferedReader getResponseRSS(CloseableHttpClient httpClient, String dataSource) throws IOException {
        HttpGet httpGet = new HttpGet(dataSource);
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        BufferedReader reader;
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        } else {
            reader = null;
        }
        return reader;
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
