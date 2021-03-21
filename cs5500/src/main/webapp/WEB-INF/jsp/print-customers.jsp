<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: akriti
  Date: 7/27/20
  Time: 1:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Group 11</title>
</head>
<body>
<h2>Get all customers!</h2>

Total number of customers : ${sessionScope.numCustomers}
<table border="1">
    <tr>
        <td>
            <u>Time Slot</u>
        </td>
        <td>
            <u>Visit Duration(minutes)</u>
        </td>
        <td>
            <u>isSenior (Boolean)</u>
        </td>
    </tr>

    <c:forEach items="${sessionScope.customers}" var="customer" >
        <tr>
            <td><c:out value="${customer.getPrintTimeSlot()}" /></td>
            <td><c:out value="${customer.getPrintVisitDuration()}" /></td>
            <td><c:out value="${customer.getPrintIsSenior()}" /></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
