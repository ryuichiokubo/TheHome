package thehome;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;

public class EntryParser implements Runnable{
	
	private static final Logger log = Logger.getLogger(TheHomeServlet.class.getName());
	
	private SyndEntry entry;
	private HashMap<String, String> contents = new HashMap<String, String>(); // XXX TreeMap

	public EntryParser(SyndEntry entry) {
		this.entry = entry;
	}
	
	public void run() {
		contents.put("title", entry.getTitle());
		try {
			contents.put("link", findDetailURL(entry.getLink()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public HashMap<String, String> getContents() {
		return contents;
	}
		
	private String findDetailURL(String urlToParse) throws IOException {
		if (urlToParse.startsWith("http://rd.yahoo.co.jp")) {
			urlToParse = urlToParse.substring(urlToParse.indexOf("*")+1);
		}
		Document document = Jsoup.connect(urlToParse).timeout(1000*20).get(); // XXX constant
		Elements links = document.select("div#detailHeadline  a[href]");
		String href = "";
		ArrayList<String> tmpArr = new ArrayList<String>();
		for (Element link : links) {
			href = link.attr("href");
			if (href.startsWith("http://headlines.yahoo.co.jp")) { // XXX got http://backnumber.dailynews.yahoo.co.jp/?m=7700474&e=food_service_industry
				if (tmpArr.contains(href)) {
					break;
				} else {
					tmpArr.add(href);
				}
			}
		}
		return href;
	}
}
