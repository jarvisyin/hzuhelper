function itemDelete1(id) {
	$("#warningDialog_message").html("本操作将会删除该店铺以其菜单，<br/>您确定吗？");
	$("#btnSure").html("确定");
	$("#storage").val(id);
}
function itemDelete2() {
	var id = $("#storage").val();
	$.ajax({
		url : window.location.pathname,
		type : "POST",
		data : "cmd=delete&id=" + id,
		dataType : "json",
		success : function(data, status) {
			if (status != "success" || data.statu != "true") {
				$("#edit_res_Label").html("添加失败. </br>" + data.errorMsg);
				$("#btn_editRes").val("重新添加");
				return;
			}
			$('#warningDialog').modal('hide');
			$("#tr_" + id).hide(1000);
		}
	});
}
$("#showEditRes").click(function() {
	$("#storage").val("-1");
	$("#btn_editRes").val("确定");
	$('#edit_res_Label').html("添加外卖店铺");
	var txt_name = $("#txt_name");
	var txt_phone = $("#txt_phone");
	var txt_intro = $("#txt_intro");
	txt_name.val("");
	txt_phone.val("");
	txt_intro.val("");
	$('#edit_res').modal('show');
});
$("#btn_editRes").click(
		function() {
			var id = $("#storage").val();
			if (id < 0) {
				$.ajax({
					url : window.location.pathname,
					type : "POST",
					data : "cmd=add&name=" + $("#txt_name").val() + "&intro="
							+ $("#txt_intro").val() + "&phone="
							+ $("#txt_phone").val(),
					dataType : "json",
					success : function(data, status) {
						if (status != "success" || data.statu != "true") {
							$("#edit_res_Label").html(
									"添加失败. </br>" + data.errorMsg);
							$("#btn_editRes").html("重新删除");
							return;
						}
						window.location.reload();
					}
				});
			} else {
				var name = $("#tr_" + id + " td:eq(1)");
				var phone = $("#tr_" + id + " td:eq(6)");
				var intro = $("#tr_" + id + " td:eq(7)");
				var txt_name = $("#txt_name");
				var txt_phone = $("#txt_phone");
				var txt_intro = $("#txt_intro");
				$.ajax({
					url : window.location.pathname,
					type : "POST",
					data : "cmd=update&id=" + id + "&name=" + txt_name.val()
							+ "&intro=" + txt_intro.val() + "&phone="
							+ txt_phone.val(),
					dataType : "json",
					success : function(data, status) {
						if (status != "success" || data.statu != "true") {
							$("#edit_res_Label").html(
									"修改失败. </br>" + data.errorMsg);
							$("#btn_editRes").val("重新修改");
							return;
						}
						name.html(txt_name.val());
						phone.html(txt_phone.val());
						intro.html(txt_intro.val());
						$('#edit_res').modal('hide');
					}
				});
			}
		});
function itemUpdate(id) {
	$("#storage").val(id);
	var name = $("#tr_" + id + " td:eq(1)");
	var phone = $("#tr_" + id + " td:eq(6)");
	var intro = $("#tr_" + id + " td:eq(7)");
	var txt_name = $("#txt_name");
	var txt_phone = $("#txt_phone");
	var txt_intro = $("#txt_intro");
	txt_name.val(name.html());
	txt_phone.val(phone.html());
	txt_intro.val(intro.html());
	$('#edit_res_Label').html("修改" + name.html());
	$("#btn_editRes").val("修改");
	$('#edit_res').modal('show');
}