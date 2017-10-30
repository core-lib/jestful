<%--
  Created by IntelliJ IDEA.
  User: yangchangpei
  Date: 17/10/30
  Time: 上午10:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
Hello Jestful
<a href="/sample">测试</a>

<form action="/sample" method="post">
    <input type="text" name="test"/>
    <button type="submit">提交</button>
</form>
</body>
</html>
