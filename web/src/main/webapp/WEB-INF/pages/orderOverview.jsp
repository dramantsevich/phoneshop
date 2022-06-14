<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:master pageTitle="Order overview">
    <div id="content">
        <h2>Order number: ${order.id}</h2>
        <div class=”row”>
            <div class="col-md-6">
                <div class="row">
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
                            <tr>
                                <td>${item.stock.phone.brand}</td>
                                <td>${item.stock.phone.model}</td>
                                <td>
                                    <c:forEach var="color" items="${item.stock.phone.color}">
                                        ${color.code}
                                    </c:forEach>
                                </td>
                                <td>${item.stock.phone.displaySizeInches}"</td>
                                <td>${item.quantity}</td>
                                <td>$${item.stock.phone.price}</td>
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
                </div><!-- end row -->
            </div>
        </div>

        <h2>Your details</h2>
        <tags:orderOverviewField name="firstName" label="First Name" order="${order}"></tags:orderOverviewField>
        <tags:orderOverviewField name="lastName" label="Last Name" order="${order}"></tags:orderOverviewField>
        <tags:orderOverviewField name="deliveryAddress" label="Address" order="${order}"></tags:orderOverviewField>
        <tags:orderOverviewField name="contactPhoneNo" label="Phone" order="${order}"></tags:orderOverviewField>
    </div>
</tags:master>