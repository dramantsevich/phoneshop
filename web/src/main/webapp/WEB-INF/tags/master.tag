<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>${pageTitle}</title>
    <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>

    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css" media="screen,projection"/>

    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <script type="text/javascript" src="js/materialize.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js">

    </script>
    <script type="text/javascript">
        function addToCart(id, totalQuantity, totalCost) {
            var quantity = $("#" + id).val();

            var Data = {
                "id": id,
                "quantity": quantity,
                "totalQuantity": totalQuantity,
                "totalCost": totalCost
            }
            console.log(Data);

            if(!Number.isFinite(quantity)){
                $('#feedback' + id).text('Enter number');
            }

            $.ajax({
                type: "POST",
                url: "/ajaxCart",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(Data),
                success: function (res) {
                    if (res.validated) {
                        $('#feedback' + id).empty();

                        $("#cartTotalQuantity").text("quantity: " + res.cart.totalQuantity);
                        $("#cartTotalCost").text("$" + res.cart.totalCost);
                    } else {
                        $.each(res.errorMessages, function (key, value) {
                            $('#feedback' + id).text(value);
                        });
                    }
                },
                error: function (e) {
                    console.log("ERROR : ", e);

                    const mes = $.parseJSON(e.responseText);
                    $('#feedback' + id).text(mes.message);
                }
            })
        }
    </script>
</head>
<body class="product-list">
<header>
    <div>
        <a href="../../index.jsp">
            <h2>Home</h2>
        </a>
    </div>
    <div>
        <h3><a href="${pageContext.request.contextPath}/cart">Cart</a></h3>
        <h4><span id="cartTotalQuantity">quantity: ${cart.totalQuantity}</span>, <span
                id="cartTotalCost">$${cart.totalCost}</span></h4>
    </div>
    <div>
        <c:choose>
            <c:when test="${pageContext.request.userPrincipal.name != null}">
                <form method="post" action="/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <p>Welcome <a href="/admin/orders">${pageContext.request.userPrincipal.name}</a>
                        |
                        <button type="submit">Logout</button>
                    </p>
                </form>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Login</a>
            </c:otherwise>
        </c:choose>
    </div>
</header>
<main>
    <jsp:doBody/>
</main>
<p>
    (c) Dzmitry Ramantsevich by Expert-Soft
</p>
</body>
</html>