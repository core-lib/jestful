<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Hello World!</h2>
<jsp:include page="/include/jsp" flush="true" />
<jsp:include page="/include/text" />
<jsp:include page="/include/json" />
<jsp:include page="/include/xml" />
</body>
</html>