<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:master pageTitle="Order ${order.id}">
    <div id="content">
        <div class=”row”>
            <div class="col-md-6">
                <div>
                    Order number: ${order.id}
                </div>
            </div>
            <div class="col-md-6">
                <div>
                    Order status: ${order.status}
                </div>
            </div>
            <table>
                <thead>
                <tr>
                    <td>Brand</td>
                    <td>Model</td>
                    <td>Color</td>
                    <td>Display size</td>
                    <td>Quantity</td>
                    <td>Price</td>
                </tr>
                </thead>
                <c:forEach var="item" items="${order.items}">
                    <c:set var="phone" value="${item.stock.phone}"/>
                    <tr>
                        <td>${phone.brand}</td>
                        <td>${phone.model}</td>
                        <td>
                            <c:forEach var="color" items="${phone.color}">
                                ${color.code}
                            </c:forEach>
                        </td>
                        <td>${phone.displaySizeInches}"</td>
                        <td>${item.quantity}</td>
                        <td>${phone.price} $</td>
                    </tr>
                </c:forEach>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="quantity">SubTotal:</td>
                    <td class="quantity">${order.subtotal} $</td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="quantity">Delivery cost:</td>
                    <td class="quantity">${order.deliveryPrice} $</td>
                </tr>
                <tr>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td class="quantity">Total cost:</td>
                    <td class="quantity">${order.totalPrice} $</td>
                </tr>
            </table>
            <h2>Your details</h2>
            <tags:orderOverviewField name="firstName" label="First Name" order="${order}"></tags:orderOverviewField>
            <tags:orderOverviewField name="lastName" label="Last Name" order="${order}"></tags:orderOverviewField>
            <tags:orderOverviewField name="deliveryAddress" label="Address" order="${order}"></tags:orderOverviewField>
            <tags:orderOverviewField name="contactPhoneNo" label="Phone" order="${order}"></tags:orderOverviewField>
        </div>
        <a href="/admin/orders">
            <button>Back</button>
        </a>
        <c:set var="statusNew" value="NEW"/>
        <c:if test="${order.status == statusNew}">
            <a href="/admin/orders/delivered/${order.id}">
                <button>Delivered</button>
            </a>
            <a href="/admin/orders/rejected/${order.id}">
                <button>Rejected</button>
            </a>
        </c:if>
    </div>
</tags:master>