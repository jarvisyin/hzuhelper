function itemDelete1(id) {
	$("#warningDialog_message").html("您确定删除吗吗？");
	$("#btnSure").html("确定");
	$("#storage").val(id);
}
function itemDelete2() {
	var id = $("#storage").val();
	$
			.ajax({
				url : "takeoutmenu",
				type : "POST",
				data : "cmd=delete&id=" + id,
				dataType : "json",
				success : function(data, status) {
					if (status != "success" || data.statu != "true") {
						$("#warningDialog_message").html(
								"删除失败. </br>" + data.errorMsg);
						$("#btnSure").val("重新删除");
						return;
					}
					$('#warningDialog').modal('hide');
					$("#tr_" + id).hide(1000);
				}
			});
}
function itemUpdate(id) {
	$("#storage").val(id);
	var name = $("#tr_" + id + " td:eq(1)");
	var phone = $("#tr_" + id + " td:eq(2)");
	var intro = $("#tr_" + id + " td:eq(3)");
	var txt_name = $("#txt_name");
	var txt_price = $("#txt_price");
	var txt_intro = $("#txt_intro");
	txt_name.val(name.html());
	txt_price.val(phone.html());
	txt_intro.val(intro.html());
	$('#edit_res_Label').html("修改菜单");
	$("#btn_editRes").val("修改");
	$('#edit_res').modal('show');
}
$("#showEditRes").click(function() {
	$("#storage").val("-1");
	$("#btn_editRes").val("确定");
	$('#edit_res_Label').html("添加菜单");
	var txt_name = $("#txt_name");
	var txt_price = $("#txt_price");
	var txt_intro = $("#txt_intro");
	txt_name.val("");
	txt_price.val("");
	txt_intro.val("");
	$('#edit_res').modal('show');
});
$("#btn_editRes").click(
		function() {
			var id = $("#storage").val();
			if (id < 0) {
				$.ajax({
					url : "takeoutmenu",
					type : "POST",
					data : "cmd=add&restaurantId=" + $("#restaurantId").val()
							+ "&name=" + $("#txt_name").val() + "&intro="
							+ $("#txt_intro").val() + "&price="
							+ $("#txt_price").val(),
					dataType : "json",
					success : function(data, status) {
						if (status != "success" || data.statu != "true") {
							$("#edit_res_Label").html(
									"添加失败. </br>" + data.errorMsg);
							$("#btn_editRes").html("重新添加");
							return;
						}
						window.location.reload();
					}
				});
			} else {
				var name = $("#tr_" + id + " td:eq(1)");
				var phone = $("#tr_" + id + " td:eq(2)");
				var intro = $("#tr_" + id + " td:eq(3)");
				var txt_name = $("#txt_name");
				var txt_price = $("#txt_price");
				var txt_intro = $("#txt_intro");
				$.ajax({
					url : "takeoutmenu",
					type : "POST",
					data : "cmd=update&id=" + id + "&name=" + txt_name.val()
							+ "&intro=" + txt_intro.val() + "&price="
							+ txt_price.val(),
					dataType : "json",
					success : function(data, status) {
						if (status != "success" || data.statu != "true") {
							$("#edit_res_Label").html(
									"修改失败. </br>" + data.errorMsg);
							$("#btn_editRes").val("重新修改");
							return;
						}
						name.html(txt_name.val());
						phone.html(txt_price.val());
						intro.html(txt_intro.val());
						$('#edit_res').modal('hide');
					}
				});
			}
		});
