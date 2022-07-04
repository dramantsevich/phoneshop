<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Order">
    <div id="content">
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

        <form:form modelAttribute="orderDTO" method="post" action="${pageContext.request.contextPath}/order" >
            <h2>Your details</h2>
            <tr>
                <td>First name</td>
                <td>
                    <form:input path="firstName" id="firstName"/>
                </td>
                <form:errors path="firstName" cssStyle="color:red"/>
            </tr>
            <tr>
                <td>Last name</td>
                <td>
                    <form:input path="lastName" id="lastName"/>
                </td>
                <form:errors path="lastName" cssStyle="color:red"/>
            </tr>
            <tr>
                <td>Address</td>
                <td>
                    <form:input path="deliveryAddress" id="deliveryAddress"/>
                </td>
                <form:errors path="deliveryAddress" cssStyle="color:red"/>
            </tr>
            <tr>
                <td>Phone</td>
                <td>
                    <form:input path="contactPhoneNo" id="contactPhoneNo"/>
                </td>
                <form:errors path="contactPhoneNo" cssStyle="color:red"/>
            </tr>
            <form:button name="Place order" type="submit" class="btn btn-info">Place order</form:button>
        </form:form>
    </div>
</tags:master>