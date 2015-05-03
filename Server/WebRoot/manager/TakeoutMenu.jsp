<%@page import="com.hzuhelper.Domain.TakeoutMenu"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="common/top.jsp" />
<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
	<table style="width: 100%">
		<tr>
			<td><h2 class="sub-header">
					${restaurantInfo.name } <span style="font-size: 20px"> 菜单管理</span>
				</h2></td>
			<td style="text-align: right"><input type="button"
				class="btn btn-lg btn-success" id="showEditRes" value="添加菜单" /></td>
		</tr>
	</table>
	<div style="clear: both" class="table-responsive">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>id</th>
					<th>name</th>
					<th>price</th>
					<th>intro</th>
					<th>删除</th>
					<th>修改</th>
				</tr>
			</thead>
			<tbody>
				<%
					Object obj = request.getAttribute("list");
							ArrayList<TakeoutMenu> uilist = (ArrayList<TakeoutMenu>) obj;
							for (int i = 0, len = uilist.size(); i < len; i++) {
								TakeoutMenu model = uilist.get(i);
				%>
				<tr id="tr_<%=model.getId()%>">
					<td><%=model.getId()%></td>
					<td><%=model.getName()%></td>
					<td><%=model.getPrice()%></td>
					<td><%=model.getIntro()%></td>
					<td><input type="button" class="btn btn-xs btn-danger"
						value="删除" data-toggle="modal" data-target="#warningDialog"
						onclick="itemDelete1(<%=model.getId()%>)" /></td>
					<td><input type="button" class="btn btn-xs btn-warning"
						value="修改" onclick="itemUpdate(<%=model.getId()%>)" /></td>
				</tr>
				<%
					}
				%>
			</tbody>
		</table>
	</div>
</div>
<input type="text" style="display: none" value="${restaurantInfo.id }"
	id="restaurantId">
<input type="text" style="display: none" id="storage">
<div class="modal fade" id="warningDialog" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">警告</h4>
			</div>
			<div id="warningDialog_message" class="modal-body"></div>
			<div class="modal-footer">
				<input type="button" class="btn btn-lg btn-default"
					data-dismiss="modal" value="取消" /> <input id="btnSure"
					type="button" class="btn btn-lg btn-danger" onclick="itemDelete2()"
					value="确定" />
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="edit_res" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="edit_res_Label"></h4>
			</div>
			<div class="modal-body">
				<form role="form">
					<div class="form-group">
						<label>名称</label> <input type="text" class="form-control"
							id="txt_name" />
					</div>
					<div class="form-group">
						<label>价格 （单位：元）</label> <input type="text" class="form-control"
							id="txt_price" />
					</div>
					<div class="form-group">
						<label>简介</label>
						<textarea class="form-control" id="txt_intro" rows="3"></textarea>
					</div>
					<div style="text-align: right">
						<input type="button" id="btn_editRes" class="btn btn-primary"
							value="确定" />
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<jsp:include page="common/foot.jsp" />
<script src="js/TakeoutMenu.js" type="text/javascript"></script>


