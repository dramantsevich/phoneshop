<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product List">
    <sf:form method="get" action="/search/1">
        <label>
            <input type="text" name="keyword" placeholder="Search" size="50"/>
        </label>
        <button type="submit" class="btn btn-info">Search</button>
    </sf:form>
    <div id="content">
        <table border="1px">
            <thead>
            <sf:form method="get" action="/productList/sort/1">
                <tr>
                    <td>Image</td>
                    <td>
                        Brand
                        <tags:sortLink sort="brand" order="asc"/>
                        <tags:sortLink sort="brand" order="desc"/>
                    </td>
                    <td>
                        Model
                        <tags:sortLink sort="model" order="asc"/>
                        <tags:sortLink sort="model" order="desc"/>
                    </td>
                    <td>Color</td>
                    <td>
                        Display size
                        <tags:sortLink sort="displaysizeinches" order="asc"/>
                        <tags:sortLink sort="displaysizeinches" order="desc"/>
                    </td>
                    <td>
                        Price
                        <tags:sortLink sort="price" order="asc"/>
                        <tags:sortLink sort="price" order="desc"/>
                    </td>
                    <td>Quantity</td>
                    <td>Action</td>
                </tr>
            </sf:form>
            </thead>
            <c:forEach var="ph" items="${phones.content}">
                <tr>
                    <td>
                        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${ph.phone.imageUrl}">
                    </td>
                    <td>${ph.phone.brand}</td>
                    <td>
                        <a href="/productDetails/${ph.phone.id}">
                                ${ph.phone.model}
                        </a>
                    </td>
                    <td>
                        <c:forEach var="color" items="${ph.phone.color}">
                            ${color.code}
                        </c:forEach>
                    </td>
                    <td>${ph.phone.displaySizeInches}"</td>
                    <td>$ ${ph.phone.price}</td>
                    <td>
                        <input id="${ph.phone.id}" class="quantity" name="quantity"/>
                        <input type="hidden" name="hiddenProductId" class="hiddenProductId" value='${ph.phone.id}'/>
                        <div style="color: red" id="feedback${ph.phone.id}"></div>
                    </td>
                    <td>
                        <input id="btn-submit" type="submit" class="btn btn-primary addToCart" name="button"
                               value="Add to cart"
                               onclick="addToCart(${ph.phone.id}, ${cart.totalQuantity}, ${cart.totalCost})"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <c:set var="p" value="${currentPage}"/> <%-- current page (1-based) --%>
    <c:set var="l" value="10"/> <%-- amount of page links to be displayed --%>
    <c:set var="r" value="${l / 2}"/> <%-- minimum link range ahead/behind --%>
    <c:set var="t" value="${totalPages}"/> <%-- total amount of pages --%>

    <c:set var="begin" value="${((p - r) > 0 ? ((p - r) < (t - l + 1) ? (p - r) : (t - l)) : 0) + 1}"/>
    <c:set var="end" value="${(p + r) < t ? ((p + r) > l ? (p + r) : l) : t}"/>

    <c:choose>
        <c:when test="${p != 1}">
            <a href="/productList/ + ${1}?sort=${sort}&order=${order}">First</a>
        </c:when>
        <c:otherwise>
            <span class="page-item">First</span>
        </c:otherwise>
    </c:choose>
    <c:if test="${p==1}">
        <a><i class="material-icons">chevron_left</i></a>
    </c:if>
    <c:if test="${p!=1}">
        <a href="/productList/ + ${p - 1}?sort=${sort}&order=${order}"><i
                class="material-icons">chevron_left</i></a>
    </c:if>
    <c:forEach begin="${begin}" end="${end}" var="i">
        <a href="/productList/ + ${i}?sort=${sort}&order=${order}">${i}</a>
    </c:forEach>
    <c:choose>
        <c:when test="${p < t}">
            <a href="/productList/ + ${p + 1}?sort=${sort}&order=&${order}"><i
                    class="material-icons">chevron_right</i></a>
        </c:when>
        <c:otherwise>
            <span class="page-item"><i class="material-icons">chevron_right</i></span>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${p < t}">
            <a href="/productList/ + ${t}?sort=${sort}&order=${order}">Last</a>
        </c:when>
        <c:otherwise>
            <span class="page-item">Last</span>
        </c:otherwise>
    </c:choose>

</tags:master>