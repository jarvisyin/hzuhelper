<%@page import="java.util.ArrayList"%>
<%@page import="com.hzuhelper.Domain.TakeoutRestaurant2"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<jsp:include page="common/top.jsp" />
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<h2 class="sub-header">选择店铺</h2>
	<div>
		<%
			Object obj = request.getAttribute("list");
			ArrayList<TakeoutRestaurant2> uilist = (ArrayList<TakeoutRestaurant2>) obj;
			for (int i = 0, len = uilist.size(); i < len; i++) {
				TakeoutRestaurant2 model = uilist.get(i);
		%>
		<div style="width: 300px; float: left">
			<a href="takeoutmenu?restaurantId=<%=model.getId()%>"
				class="btn btn-primary" style="margin: 5px 5px 5px 5px"><%=model.getName()%></a>
		</div>
		<%
			}
		%>

	</div>
</div>
<jsp:include page="common/foot.jsp" />