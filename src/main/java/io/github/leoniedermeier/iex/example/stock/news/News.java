package io.github.leoniedermeier.iex.example.stock.news;

import java.net.URL;
import java.time.ZonedDateTime;

public class News {

	private ZonedDateTime datetime;
	private String headline;
	private String source;
	private URL url;
	private String summary;
	private String related;

	public News() {
		super();
	}

	public ZonedDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(ZonedDateTime datetime) {
		this.datetime = datetime;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getRelated() {
		return related;
	}

	public void setRelated(String related) {
		this.related = related;
	}

	@Override
	public String toString() {
		return "News [datetime=" + datetime + ", headline=" + headline + ", source=" + source + ", url=" + url
				+ ", summary=" + summary + ", related=" + related + "]";
	}

}
