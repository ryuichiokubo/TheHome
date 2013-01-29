package thehome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.*;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@SuppressWarnings("serial")
public class TheHomeServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// XXX exception handling
		
		// fetch data from URL
		URL url = new URL("http://rss.dailynews.yahoo.co.jp/fc/rss.xml");
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build(new XmlReader(url));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// parse RSS
		String title = feed.getTitle();
		String res = "";
		for (Object obj : feed.getEntries()) {
			SyndEntry entry = (SyndEntry) obj;
			res += entry.getTitle();
			res += "\n";
		}
		/*
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		String line = "";
		while ((line = reader.readLine()) != null) {
			res += line;
		}
		*/
		
		// response
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().println(title);
		resp.getWriter().println(res);
	}
}
