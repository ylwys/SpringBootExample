<%--
  Created by IntelliJ IDEA.
  User: yanliang
  Date: 2016/12/17
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>$Title$</title>
</head>
<body>


<%
    String test = (String) request.getAttribute("test");
    System.out.println(test);
%>



${test}
$END$

</body>
</html>
