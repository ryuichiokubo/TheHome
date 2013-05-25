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
    	//application.log(article);
    	    	
    	pageContext.setAttribute("title", article.get("title"));
    	String link = article.get("link");
    	%>
    	<a href="<%= link %>">${fn:escapeXml(title)}</a><br />
    	<%
	}
	//out.println("---- end ----");
%>

  </body>
</html>

