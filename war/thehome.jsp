<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.logging.Logger" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

  <body>
	  <p>${fn:escapeXml(feedTitle)}</p>
	  
<%
	//out.println("---- start ----");

	List<HashMap<String, String>> articles = (List<HashMap<String, String>>) request.getAttribute("articles");
    for (HashMap<String, String> article : articles) {  
    	String title = article.get("title");  	    	
    	pageContext.setAttribute("title", title);
    	String link = article.get("link");
    	String summary = article.get("summary");
    	String hash = article.get("hash");
    	pageContext.setAttribute("title", title);
    	%>
    	<a href="<%= link %>">${fn:escapeXml(title)}</a><br />
    	<%= summary %><br /><%= hash %><br />
    	<%
	}
	//out.println("---- end ----");
%>

  </body>
  <script>
  	var hash = "<%=request.getAttribute("hash") %>";
  	var title = "<%=request.getAttribute("title") %>";
	localStorage.setItem(hash, title); // XXX {null: "null"}
  </script>
</html>

