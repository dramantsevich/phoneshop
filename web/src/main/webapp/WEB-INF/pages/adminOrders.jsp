<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:master pageTitle="Admin Orders">
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form method="post" action="/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form>
    </c:if>
    <p>Welcome ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()">Logout</a></p>
    <p>message</p>
    <div id="content">
        <div class=”row”>
            <table>
                <caption>Orders</caption>
                <thead>
                <tr>
                    <td>Order Number</td>
                    <td>Customer</td>
                    <td>Phone</td>
                    <td>Address</td>
                    <td>Date</td>
                    <td>Total Price</td>
                    <td>Status</td>
                </tr>
                </thead>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>
                            <a href="/admin/orders/${order.id}">
                                    ${order.id}
                            </a>
                        </td>
                        <td>${order.firstName} ${order.lastName}</td>
                        <td>${order.contactPhoneNo}</td>
                        <td>${order.deliveryAddress}</td>
                        <td>${order.deliveryDate}</td>
                        <td>${order.totalPrice} $</td>
                        <td>${order.status}</td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</tags:master>