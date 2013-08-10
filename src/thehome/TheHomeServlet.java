package thehome;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.ThreadManager;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

@SuppressWarnings("serial")
public class TheHomeServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(TheHomeServlet.class.getName());
	
	@SuppressWarnings("null")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		// XXX exception handling
		// fetch data from URL
		URL url = new URL("http://rss.dailynews.yahoo.co.jp/fc/rss.xml");
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(20000); // XXX constant
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build(new XmlReader(conn));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// parse RSS		
		List<HashMap<String, String>> articles = new ArrayList<HashMap<String, String>>();
		List<EntryParser> parsers = new ArrayList<EntryParser>();
		List<Thread> threads = new ArrayList<Thread>();
		ThreadFactory threadFactory = ThreadManager.currentRequestThreadFactory();
		
		for (Object obj : feed.getEntries()) {
			SyndEntry entry = (SyndEntry) obj;
			EntryParser parser = new EntryParser(entry);
			Thread thread = threadFactory.newThread(parser);
			thread.start();
			threads.add(thread);
			parsers.add(parser);
		}
		for (Thread thread: threads) {
			try {
				thread.join(); // XXX replace with notify or something to wait for threads execution smartly
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (EntryParser parser: parsers) {
			articles.add(parser.getContents());
		}

		// XXX You should do this ONLY ONCE in the whole application life-cycle
		Configuration cfg = new Configuration();
		cfg.setTemplateLoader(new WebappTemplateLoader(this.getServletContext())); // !
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		
		Map root = new HashMap();
		root.put("articles", articles);
		
		Template temp = cfg.getTemplate("thehome.tmpl");
	    
		resp.setContentType("text/html");
	    resp.setCharacterEncoding("utf-8");
	    try {
	       temp.process(root, resp.getWriter());
	    } catch (TemplateException e) {
	       resp.getWriter().println(e.getMessage());
	    }
	}
}
