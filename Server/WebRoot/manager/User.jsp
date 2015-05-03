<%@page import="com.hzuhelper.Domain.User"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<jsp:include page="common/top.jsp" />
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<table style="width: 100%">
		<tr>
			<td><h2 class="sub-header">用户管理</h2></td>
			<td style="text-align: right">
				<%
					int userCount = (Integer) request.getAttribute("userCount");
				%> 注册用户：<%=userCount%></td>
		</tr>
	</table>
	<div class="table-responsive">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Id</th>
					<th>Email</th>
					<th>Student_id</th>
					<th>Register_date</th>
				</tr>
			</thead>
			<tbody>
				<%
					Object obj = request.getAttribute("uilist");
							ArrayList<User> uilist = (ArrayList<User>) obj;
							for (int i = 0, len = uilist.size(); i < len; i++) {
								User model = uilist.get(i);
				%>
				<tr>
					<td><%=model.getId()%></td>
					<td><%=model.getEmail()%></td>
					<td><%=model.getStudentId()%></td>
					<td><%=model.getRegisterDatetime()%></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
	<div>
		<%
			Integer webPage = (Integer) request.getAttribute("page");
		%>
		页次：<%=webPage%>/<%=(userCount / 20) + 1%>
		每页20 总数<%=userCount%>
		<a href="user">首页</a> <a href="/mana/user?page=<%=webPage - 1%>">上一页</a>
		<a href="user?page=<%=webPage + 1%>">下一页</a> <a
			href="user?page=<%=(userCount / 20 + 1)%>">尾页</a>

	</div>
	<div>
		<form style="padding-top: 10px" action="/mana/user" method="get">
			跳转到:<input name="page" style="width: 35px" type="text"> 页 <input
				type="submit" class="btn btn-success btn-xs" value="确定" />
		</form>
	</div>
</div>
<jsp:include page="common/foot.jsp" />