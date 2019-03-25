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
    <title>Добавить автора</title>
    <%@include file="/libs.jsp" %>
    <meta charset="UTF-8">
</head>
<body>
<jsp:include page="/menu.jsp"/>
<div class="container" style="margin-top: 40px;">
    <h1>Добавить автора</h1>

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

    <form action="" method="post" enctype="multipart/form-data">
        <div class="table">
            <div class="table-row">
                <div class="table-cell">
                    <b>Имя автора:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="authorName" style="width:300px;" value="test автор"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Дата рождения:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="birthDate" class="form-control" readonly style="width:300px;"
                           id="birthDate"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Дата смерти:</b>
                </div>
                <div class="table-cell">
                    <input type="text" name="deathDate" class="form-control" readonly style="width:300px;"
                           id="deathDate"/>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Об авторе:</b>
                </div>
                <div class="table-cell">
                    <textarea name="description">some description</textarea>
                </div>
            </div>
            <div class="table-row">
                <div class="table-cell">
                    <b>Постер:</b>
                </div>
                <div class="table-cell">
                    <input type="file" id="poster" name="poster"/>

                    <div style="margin-top:10px;font-size:10pt;font-style:italic;">
                        Если вы оставите это поле пустым, будет загружен постер по умолчанию.
                    </div>
                </div>
            </div>

            <div class="table-row">
                <div class="table-cell">
                </div>
                <div class="table-cell">
                    <br/>
                    <input type="submit" class="btn btn-dark" value="Добавить автора">
                </div>
            </div>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(document).ready(function () {
        $('#birthDate').datepicker({
            format: 'dd.mm.yyyy'
        });
        $('#deathDate').datepicker({
            format: 'dd.mm.yyyy'
        });
    });
</script>
</body>
</html>
