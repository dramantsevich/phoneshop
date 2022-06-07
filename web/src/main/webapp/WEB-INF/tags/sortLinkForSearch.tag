<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="keyword" required="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>


<a href="?keyword=${keyword}&sort=${sort}&order=${order}"
   style="${sort eq param.sort and order eq param.order ? 'font-weight: bold' : ''}">${order}</a>