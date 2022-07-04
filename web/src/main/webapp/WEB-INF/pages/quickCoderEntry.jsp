<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<c:set var="inputSize" value="${[0, 1, 2, 3]}"/>
<tags:master pageTitle="Quick coder entry">
    <%-- использовать теги spring вместо error div --%>
    <div id="content">
        <form method="post" action="${pageContext.request.contextPath}/quickCoderEntry">
            <table border="1px">
                <thead>
                <tr>
                    <td>Product code</td>
                    <td>QTY</td>
                </tr>
                <c:forEach var="size" items="${inputSize}" varStatus="status">
                    <tr>
                        <c:set var="errorCode" value="${errorsCode[size]}"/>
                        <td>
                            <input id="code${size}" name="code" value="${not empty errorCode ? paramValues['code'][status.index] : code[size]}"/>
                            <c:if test="${not empty errorCode}">
                                <div style="color: red" class="error">
                                        ${errorsCode[size]}
                                </div>
                            </c:if>
                        </td>
                        <td>
                            <c:set var="errorQuantity" value="${errorsQuantity[size]}"/>
                            <input id="quantity${size}" name="quantity" value="${not empty errorQuantity ? paramValues['quantity'][status.index] : quantity[size]}"/>
                            <c:if test="${not empty errorQuantity}">
                                <div style="color: red" class="error">
                                        ${errorsQuantity[size]}
                                </div>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </thead>
                <button type="submit" class="btn btn-info">Add 2 cart</button>
            </table>
        </form>
    </div>
</tags:master>