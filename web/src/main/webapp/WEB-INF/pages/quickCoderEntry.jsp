<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="inputSize" value="${[0, 1, 2, 3]}"/>
<tags:master pageTitle="Quick coder entry">
    <%-- использовать теги spring вместо error div --%>
    <div id="content">
        <c:if test="${not empty successMessage}">
            <div style="color: green">${successMessage}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div style="color: red">${errorMessage}</div>
        </c:if>
        <form:form modelAttribute="quickCoderEntryList" method="post" action="${pageContext.request.contextPath}/quickCoderEntry">
            <table border="1px">
                <thead>
                <tr>
                    <td>Product code</td>
                    <td>QTY</td>
                </tr>
                <c:forEach var="size" items="${inputSize}" varStatus="status">
                    <tr>
                        <td>
                            <form:input path="list[${status.index}].id"/>
                            <form:errors path="list[${status.index}].id" cssStyle="color:red"/>
                        </td>
                        <td>
                            <form:input path="list[${status.index}].quantity"/>
                            <form:errors path="list[${status.index}].quantity" cssStyle="color:red"/>
                        </td>
                    </tr>
                </c:forEach>
                </thead>
            </table>
            <form:button name="Add" type="submit" class="btn btn-info">Add 2 cart</form:button>
        </form:form>
    </div>
</tags:master>