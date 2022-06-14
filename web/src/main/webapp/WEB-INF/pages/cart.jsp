<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                        <c:forEach var="item" items="${cart.items}">
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
                                    <input id="${item.stock.phone.id}" type="number" class="quantity" name="quantity"
                                           value="${item.quantity}"/>
                                    <input type="hidden" name="hiddenProductId" class="hiddenProductId"
                                           value='${item.stock.phone.id}'/>
                                    <input type="submit" class="btn btn-primary update"
                                           name="button"
                                           value="Update"
                                           onclick="updateCart(${item.stock.phone.id}, ${cart.totalQuantity}, ${cart.totalCost})"/>
                                    <div style="color: red" id="feedback${item.stock.phone.id}"></div>
                                </td>
                                <td>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/cart/${item.stock.phone.id}">
                                        <input id="btn-submit" type="submit" class="btn btn-primary delete"
                                               name="button"
                                               value="Delete"/>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                    <c:choose>
                        <c:when test="${cart.items.size() > 0}">
                            <a href="/order">
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
    </script><script type="text/javascript">
    function updateCart(id, totalQuantity, totalCost) {
    //получаю айдишник и беру по айдишнику quantity
        var quantity = $("#" + id).val();

        var Data = {
        "id": id,
        "quantity": quantity,
        "totalQuantity": totalQuantity,
        "totalCost" : totalCost
        }
        console.log(Data);

        $.ajax({
            type: "PUT",
            url: "/ajaxCart/update",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            data: JSON.stringify(Data),
            success: function (data) {
                $('#feedback' + id).empty();

                console.log("SUCCESS : ", data);

                $("#cartTotalQuantity").text("quantity: " + data.totalQuantity);
                $("#cartTotalCost").text("$" + data.totalCost);
            },
            error: function (e) {
                $('#feedback' + id).text(e.responseText)

                console.log("ERROR : ", e);
            }
        })
    }
    </script>
</tags:master>
