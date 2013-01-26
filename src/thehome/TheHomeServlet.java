package thehome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class TheHomeServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		// XXX exception handling
		
		// fetch data from URL
		URL url = new URL("http://rss.dailynews.yahoo.co.jp/fc/rss.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		String line = "";
		String res = "";
		while ((line = reader.readLine()) != null) {
			res += line;
		}
		
		// response
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().println("Yahoo!トピックス");
		resp.getWriter().println(res);
	}
}
