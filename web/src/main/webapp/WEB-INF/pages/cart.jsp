<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Cart">
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
                                        <input id="${item.stock.phone.id}" class="quantity" name="quantity${item.stock.phone.id}" value="${item.quantity}"/>
                                        <input type="hidden" name="hiddenProductId" class="hiddenProductId" value='${item.stock.phone.id}'/>
                                        <div style="color: red" id="feedback${item.stock.phone.id}"></div>
                                    </td>
                                    <td>
                                        <button form="deleteCartItem" id="btn-submit1" class="btn btn-primary delete"
                                                name="button"
                                                formaction="${pageContext.request.contextPath}/cart/${item.stock.phone.id}">
                                            Delete
                                        </button>
                                        <input id="btn-submit" type="submit" class="btn btn-primary addToCart" name="button"
                                               value="Update"
                                               onclick="updateCart(${item.stock.phone.id}, ${cart.totalQuantity}, ${cart.totalCost})"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
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
