<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Cart">
    <div id="content">
        <div class=”row”>
            <div class="col-md-6">
                <div class="row">
                    <c:if test="${not empty errorMessage}">
                        <h4 style="color: red">${errorMessage}</h4>
                    </c:if>
                    <form:form method="post" modelAttribute="cartDTOList" action="/cart/update">
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
                            <c:forEach var="item" items="${cartDTOList.list}" varStatus="status">
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
                                        <form:input path="list[${status.index}].quantity"/>
                                        <form:errors path="list[${status.index}].quantity" cssStyle="color:red"/>
                                        <form:input path="list[${status.index}].itemId" type="hidden"/>
                                    </td>
                                    <td>
                                        <button form="deleteCartItem" id="btn-submit1" class="btn btn-primary delete"
                                                name="button"
                                                formaction="${pageContext.request.contextPath}/cart/${item.stock.phone.id}">
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <form:button name="Update" type="submit" class="btn btn-info">Update</form:button>
                    </form:form>
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
