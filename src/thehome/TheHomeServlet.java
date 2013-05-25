package thehome;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@SuppressWarnings("serial")
public class TheHomeServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(TheHomeServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

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
		String feedTitle = feed.getTitle();
		List<HashMap<String, String>> articles = new ArrayList<HashMap<String, String>>();
		for (Object obj : feed.getEntries()) {
			SyndEntry entry = (SyndEntry) obj;
			HashMap<String, String> content = new HashMap<String, String>(); // XXX TreeMap
			content.put("title", entry.getTitle());
			content.put("link", entry.getLink());
			articles.add(content);
		}
		
		String tmpl = "/thehome.jsp";
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher(tmpl);
		req.setAttribute("feedTitle", feedTitle);
		req.setAttribute("articles", articles);
		//req.setAttribute("links", links );
		rd.forward(req, resp);
	}
}
