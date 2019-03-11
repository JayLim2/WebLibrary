<%--
  Created by IntelliJ IDEA.
  User: JayLim
  Date: 11.03.2019
  Time: 21:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Item</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Добавить издателя</h1>
    <form action="" method="post">
        <label>
            Input param 1:
            <input type="text" name="param1" value="test"/>
        </label>
        <label>
            Input test param 2:
            <input type="text" name="testParam2" value="test2"/>
        </label>
        <input type="submit" value="SUBMIT">
    </form>
</div>
</body>
</html>
