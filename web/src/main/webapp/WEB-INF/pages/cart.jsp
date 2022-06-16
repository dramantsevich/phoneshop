<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:master pageTitle="Cart">
    <div id="content">
        <div class=”row”>
            <div class="col-md-6">
                <div class="row">
                    <form method="post" action="${pageContext.request.contextPath}/cart/update">
                        <table>
                            <thead>
                            <tr>
                                <td>Brand</td>
                                <td>Model</td>
                                <td>Color</td>
                                <td>Display size</td>
                                <td>Price</td>
                                <td>quantity</td>
                                <td>Action</td>
                            </tr>
                            </thead>
                            <c:forEach var="item" items="${cart.items}" varStatus="status">
                                <tr>
                                    <td>${item.stock.phone.brand}</td>
                                    <td>${item.stock.phone.model}</td>
                                    <td>
                                        <c:forEach var="color" items="${item.stock.phone.color}">
                                            ${color.code}
                                        </c:forEach>
                                    </td>
                                    <td>${item.stock.phone.displaySizeInches}"</td>
                                    <td>$${item.stock.phone.price}</td>
                                    <td>
                                        <c:set var="error" value="${errors[item.stock.phone.id]}"/>
                                        <input class="quantity" name="quantity" type="number"
                                               value="${not empty error ? paramValues['quantity'][status.index] : item.quantity}"/>
                                        <c:if test="${not empty error}">
                                            <div style="color: red" class="error">
                                                    ${errors[item.stock.phone.id]}
                                            </div>
                                        </c:if>
                                        <input type="hidden" name="productId" value="${item.stock.phone.id}"/>
                                    </td>
                                    <td>
                                        <button form="deleteCartItem" id="btn-submit" class="btn btn-primary delete"
                                                name="button"
                                                formaction="${pageContext.request.contextPath}/cart/${item.stock.phone.id}">
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <button type="submit" class="btn btn-info">Update</button>
                    </form>
                    <form id="deleteCartItem" method="post"></form>
                    <c:choose>
                        <c:when test="${cart.items.size() > 0}">
                            <a href="${pageContext.request.contextPath}/order">
                                <button class="btn btn-info">Order</button>
                            </a>
                        </c:when>
                        <c:otherwise>
                            <h3>Your cart is empty</h3>
                        </c:otherwise>
                    </c:choose>
                </div><!-- end row -->
            </div>
        </div>
    </div>
</tags:master>
