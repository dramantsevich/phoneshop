<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<tags:master pageTitle="search">
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
                        <tags:sortLinkForSearch keyword="${keyword}" sort="brand" order="asc"/>
                        <tags:sortLinkForSearch keyword="${keyword}" sort="brand" order="desc"/>
                    </td>
                    <td>
                        Model
                        <tags:sortLinkForSearch keyword="${keyword}" sort="model" order="asc"/>
                        <tags:sortLinkForSearch keyword="${keyword}" sort="model" order="desc"/>
                    </td>
                    <td>Color</td>
                    <td>
                        Display size
                        <tags:sortLinkForSearch keyword="${keyword}" sort="displaysizeinches" order="asc"/>
                        <tags:sortLinkForSearch keyword="${keyword}" sort="displaysizeinches" order="desc"/>
                    </td>
                    <td>
                        Price
                        <tags:sortLinkForSearch keyword="${keyword}" sort="price" order="asc"/>
                        <tags:sortLinkForSearch keyword="${keyword}" sort="price" order="desc"/>
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
                    <td>${ph.phone.model}</td>
                    <td>
                        <c:forEach var="color" items="${ph.phone.color}">
                            ${color.code}
                        </c:forEach>
                    </td>
                    <td>${ph.phone.displaySizeInches}"</td>
                    <td>$ ${ph.phone.price}</td>
                    <td>
                        <input id="${ph.phone.id}" type="text" class="quantity" name="quantity"/>
                        <input type="hidden" name="hiddenProductId" class="hiddenProductId" value='${ph.phone.id}'/>
                        <div style="color: red" id="feedback${ph.phone.id}"></div>
                    </td>
                    <td>
                        <input id="btn-submit" type="submit" class="btn btn-primary addToCart" name="button"
                               value="Add to cart"
                               onclick="addToCart(${ph.phone.id})"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <div class="row col-sm-10">
                <div class="col-sm-1 page-item">
                    <div class="col-sm-1 page-item">
                        <c:if test="${currentPage==1}">
                            <a><i class="material-icons">chevron_left</i></a>
                        </c:if>
                        <c:if test="${currentPage!=1}">
                            <a href="/search/ + ${currentPage - 1}?keyword=${keyword}&sort=${sort}&order=${order}"><i
                                    class="material-icons">chevron_left</i></a>
                        </c:if>
                    </div>
                </div>
                <div class="col-sm-1">
                    <c:forEach var="i" items="${numbers}">
                  <span class="page-item">
                      <c:choose>
                          <c:when test="${currentPage!=i}">
                              <a href="/search/ + ${i}?keyword=${keyword}&sort=${sort}&order=${order}">${i}</a>
                          </c:when>
                          <c:otherwise>
                              <span>${i}</span>
                          </c:otherwise>
                      </c:choose>
                  </span>
                    </c:forEach>
                </div>
                <div class="col-sm-1 page-item">
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a href="/search/ + ${currentPage + 1}?keyword=${keyword}&sort=${sort}&order=${order}"><i
                                    class="material-icons">chevron_right</i></a>
                        </c:when>
                        <c:otherwise>
                            <span class="page-item"><i class="material-icons">chevron_right</i></span>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="col-sm-1">
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <a href="/search/ + ${totalPages}?keyword=${keyword}&sort=${sort}&order=${order}">Last</a>
                        </c:when>
                        <c:otherwise>
                            <span class="page-item">Last</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </c:if>
    </p>

    <script type="text/javascript">
        function addToCart(id) {
            //получаю айдишник и беру по айдишнику quantity
            var quantity = $("#" + id).val();

            var Data = {
                "id": id,
                "quantity": quantity
            }
            console.log(Data);

            $.ajax({
                type: "POST",
                url: "/ajaxCart",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(Data),
                success: function (data) {
                    $('#feedback' + id).text(JSON.stringify(data, null, 4))

                    console.log("SUCCESS : ", data);

                },
                error: function (e) {
                    $('#feedback' + id).text(e.responseText)

                    console.log("ERROR : ", e);

                }
            })
        }
    </script>
</tags:master>