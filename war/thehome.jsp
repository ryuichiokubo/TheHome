<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.logging.Logger" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

  <body>
	  <p>${fn:escapeXml(title)}</p>
	  
<%
	//out.println("---- start ----");

	List<String> articles = (List<String>) request.getAttribute("articles");
    for (String article : articles) {
    	//application.log(article);
    	pageContext.setAttribute("article", article);
    	%>
    	${fn:escapeXml(article)}<br />
    	<%
	}
	//out.println("---- end ----");
%>

  </body>
</html>

