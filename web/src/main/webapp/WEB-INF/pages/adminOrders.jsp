<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:master pageTitle="Admin Orders">
    <div id="content">
        <div class=”row”>
            <table>
                <caption>Orders</caption>
                <thead>
                <tr>
                    <th>Order Number</th>
                    <th>Customer</th>
                    <th>Phone</th>
                    <th>Address</th>
                    <th>Date</th>
                    <th>Total Price</th>
                    <th>Status</th>
                </tr>
                </thead>
                <c:forEach var="order" items="${orders.values()}">
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