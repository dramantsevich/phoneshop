<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:master pageTitle="Login">
    <div id="content">
        <div class=”row”>
            <h1>Welcome to Admin</h1>

          <c:set var="error" value="${errors}"/>
          <c:if test="${not empty error}">
            <div style="color: red">
                ${error}
            </div>
          </c:if>
            <form method="POST" action="${pageContext.request.contextPath}/login">
                User Name : <input type="text" name="username" value="admin"/><br><br>
                Password : <input type="password" name="password" value="password"/><br><br>
                <input type="submit" name="submit"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            </form>
        </div>
    </div>
</tags:master>