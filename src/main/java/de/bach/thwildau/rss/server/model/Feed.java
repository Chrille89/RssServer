package de.bach.thwildau.rss.server.model;

public class Feed {

	private String title;
	private String description;
	private String link;
	private String pictureUrl;
	
	public Feed(String title, String description, String link, String pictureUrl) {
		this.title = title;
		this.description = description;
		this.link = link;
		this.pictureUrl = pictureUrl;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	@Override
	public String toString() {
		return "Title: "+getTitle()+"; Description: "+getDescription()+"; PictureUrl: "+getPictureUrl()+"; Link: "+getLink();
	}
	
	
}
