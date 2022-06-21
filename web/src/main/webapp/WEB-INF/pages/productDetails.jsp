<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<link
        href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
        rel="stylesheet"
        integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
        crossorigin="anonymous"
>
<script
        src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"
        integrity="sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS"
        crossorigin="anonymous"></script>
<link href='https://fonts.googleapis.com/css?family=Ubuntu+Mono' rel='stylesheet' type='text/css'>


<tags:master pageTitle="Product Details">
    <c:set var="phone" value="${stock.phone}"/>
    <div id="content">
        <div class=”row”>
            <div class="col-md-6">
                <img
                        src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}"
                        class="image-responsive"
                />
                <p>${phone.description}</p>
                <div>
                    <div class="col-md-12 bottom-rule">
                        <h5>Stock - ${stock.stock}</h5>
                    </div>
                    <div class="col-md-12 bottom-rule">
                        <h5>Reserved - ${stock.reserved}</h5>
                    </div>
                    <div class="row">
                        <div class="col-md-12 bottom-rule">
                            <h2 class="product-price">$${phone.price}</h2>
                        </div>
                    </div>
                    <input id="${phone.id}" class="quantity" name="quantity"/>
                    <input type="hidden" name="hiddenProductId" class="hiddenProductId" value='${phone.id}'/>
                    <div style="color: red" id="feedback${phone.id}"></div>
                    <input id="btn-submit" type="submit" class="btn btn-primary addToCart" name="button"
                           value="Add to cart"
                           onclick="addToCart(${phone.id})"/>
                </div>

            </div>
            <div class="col-md-6">
                <div class="row">
                    <div class="col-md-12">
                        <h1>${phone.brand}, ${phone.model}</h1>
                    </div>
                </div><!-- end row-->
                <div class="row">
                    <table>
                        <caption>Display</caption>
                        <tr>
                            <td>Size</td>
                            <td>${phone.displaySizeInches}"</td>
                        </tr>
                        <tr>
                            <td>Resolution</td>
                            <td>${phone.displayResolution}</td>
                        </tr>
                        <tr>
                            <td>Technology</td>
                            <td>${phone.displayTechnology}</td>
                        </tr>
                        <tr>
                            <td>Pixel density</td>
                            <td>${phone.pixelDensity}</td>
                        </tr>
                    </table>
                </div><!-- end row -->
                <div class="row">
                    <table>
                        <caption>Dimensions & weight</caption>
                        <tr>
                            <td>Length</td>
                            <td>${phone.lengthMm}</td>
                        </tr>
                        <tr>
                            <td>Width</td>
                            <td>${phone.widthMm}</td>
                        </tr>
                        <tr>
                            <td>Color</td>
                            <td>
                                <c:forEach var="color" items="${phone.color}">
                                    <%--                                    сделать ссылку на телефон с таким цветом, либо же выбор цвета--%>
                                    ${color.code}
                                </c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td>Weight</td>
                            <td>${phone.weightGr}</td>
                        </tr>
                    </table>
                </div><!-- end row -->
                <div class="row">
                    <table>
                        <caption>Camera</caption>
                        <tr>
                            <td>Front</td>
                            <td>${phone.frontCameraMegapixels}</td>
                        </tr>
                        <tr>
                            <td>Back</td>
                            <td>${phone.backCameraMegapixels}</td>
                        </tr>
                    </table>
                </div><!-- end row -->
                <div class="row">
                    <table>
                        <caption>Battery</caption>
                        <tr>
                            <td>Talk time</td>
                            <td>${phone.talkTimeHours}</td>
                        </tr>
                        <tr>
                            <td>Stand by time</td>
                            <td>${phone.standByTimeHours}</td>
                        </tr>
                        <tr>
                            <td>Battery capacity</td>
                            <td>${phone.batteryCapacityMah}</td>
                        </tr>
                    </table>
                </div><!-- end row -->
                <div class="row">
                    <table>
                        <caption>Other</caption>
                        <tr>
                            <td>Colors</td>
                            <td>
                                <c:forEach var="color" items="${phone.color}">
                                    <%--                                    сделать ссылку на телефон с таким цветом, либо же выбор цвета--%>
                                    ${color.code}
                                </c:forEach>
                            </td>
                        </tr>
                        <tr>
                            <td>Device type</td>
                            <td>${phone.deviceType}</td>
                        </tr>
                        <tr>
                            <td>Bluetooth</td>
                            <td>${phone.bluetooth}</td>
                        </tr>
                    </table>
                </div><!-- end row -->
            </div>
        </div>
    </div>
</tags:master>
