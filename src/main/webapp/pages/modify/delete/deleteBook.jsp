<%--
  Created by IntelliJ IDEA.
  User: JayLim
  Date: 12.03.2019
  Time: 21:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Service Delete Page</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 30px">
    <%
        Object error = request.getAttribute("error");
        if (error != null) {
    %>
    <div class="message-box error">
        <% out.println(error); %>
    </div>
    <%
        }
    %>

    <%
        Object info = request.getAttribute("info");
        if (info != null) {
    %>
    <div class="message-box info">
        <% out.println(info); %>
    </div>
    <%
        }
    %>
</div>
</body>
</html>
