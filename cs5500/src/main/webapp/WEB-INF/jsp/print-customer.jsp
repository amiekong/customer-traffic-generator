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
<h2>Get a customer!</h2>
Time Slot During which the customer visited : ${sessionScope.customer.getPrintTimeSlot()}
<br/>
<br/>
Duration of Visit : ${sessionScope.customer.getPrintVisitDuration()}
<br/>
<br/>
Person is Senior : ${sessionScope.customer.getPrintIsSenior()}
</body>
</html>
