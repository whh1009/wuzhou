<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<html>
<head>
<title>五洲传播--上线平台管理</title>
<meta charset="utf-8">
<style type="text/css">
table{
	font-size:100%;
}
</style>
<script type="text/javascript">
	//限制长度
	function limitLength(arg, len) {
		if ($(arg).val().replace(/(^\s*)|(\s*$)/g, "").length > parseInt(len)) {
			alert("太长了，请修改");
			$(arg).focus();
		}
	}
	
	//删除
	function deleteOS(osId) {
		$.ajax({
			url:"deleteEBookOs.action",
			method:"post",
			data:{osId:osId},
			success:function(data){
				alert(data);
				location.href="eBookOSMan.action";
			}
		});
	}
	function checkAddBookOs() {
		var osName = $("#addOsName").val().toString();
		if(osName.length<1||osName.length>20) {
			alert("平台名没有输入或长度太长");
			return false;
		}
	}
	
	function updateOs() {
		var osId = $("#os_id").val().toString();
		var osName = $("#os_name").val().toString();
		var osDesc = $("#os_desc").val().toString();
		var osFlag = $("#os_flag").val().toString();
		if(osName.length<1||osName.length>20) {
			$("#editAlert").html("平台名没有输入或长度太长");
			$("#editAlert").show();
			return;
		}
		$.ajax({
			url:"updateEBookOS.action",
			method:"post",
			data:{osId:osId, osName:osName, osDesc:osDesc, osFlag:osFlag},
			success:function(data){
				alert(data);
				location.href="eBookOSMan.action";
			}
		});
	}
	//修改
	function editOS(osId, osName, osDesc, osFlag) {
		$("#editAlert").html("");
		$("#editAlert").hide();
		$("#os_id").val(osId);
		$("#os_name").val(osName);
		$("#os_desc").val(osDesc);
		$("#os_flag").val(osFlag);
		$("#myOsModal").modal({show:true});
	}
</script>
</head>
<body>
<div class="container">
	<div class="row">
		
		<div class="col-sm-8">
			<table class="table table-hover table-bordered">
				<caption class="lead">电子书上线平台列表</caption>
				<thead>
					<tr>
						<th>序号</th><th>名称</th><th>描述</th><th>操作</th>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="osList" id="osList">
						<tr>
							<td width="8%"><s:property value="#osList.os_id"/></td>
							<td width="22%"><s:property value="#osList.os_name"/></td>
							<td width="60%"><s:property value="#osList.os_desc"/></td>
							<td width="10%">
								<a href="javascript:editOS('<s:property value="#osList.os_id"/>', '<s:property value="#osList.os_name"/>', '<s:property value="#osList.os_desc"/>', '<s:property value="#osList.os_flag"/>')"><span class="glyphicon glyphicon-edit"></span></a>&nbsp;&nbsp;
								<a href="javascript:deleteOS('<s:property value="#osList.os_id"/>')"><span class="glyphicon glyphicon-remove" style="color:red;"></span></a>
							</td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</div>
		<div class="col-sm-4">
			<p class="lead text-center">添加电子书上线平台</p>
			<form name="from" method="post" action="addEBookOS.action" onsubmit="return checkAddBookOs()">
				<input class="form-control" type="text" id="addOsName" name="bookOnlineOSEntity.os_name" placeholder="平台名称" onblur="limitLength(this, 20)">
				<br />
				<textarea class="form-control" name="bookOnlineOSEntity.os_desc" placeholder="平台描述" rows="3"></textarea>
				<br />
				<input type="hidden" name="bookOnlineOSEntity.os_flag" value="0">
				<button type="submit" class="btn btn-info"><span class="glyphicon glyphicon-plus"></span>添加</button>
			</form>
		</div>
	</div>
</div>

<!-- 模态框 -->
    <div class="modal fade in" id="myOsModal" tabindex="-1" role="dialog" aria-labelledby="myOsModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
    		<div class="modal-content">
    			<div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myOsModalLabel">用户信息修改</h4>
                </div>
	    		<div class="modal-body">
	    			<form method="post">
	    				<input type="hidden" id="os_id" value="" />
	    				<input type="hidden" id="os_flag" value="0" />
						<label>平台名称：</label><input id="os_name" class="form-control" onblur="limitLength(this, 20)" />
						<label>平台描述：</label><textarea id="os_desc" class="form-control" rows="3"></textarea>
						<br />
						<div class="alert alert-danger" id="editAlert" style="display:none"></div>
						<br />
						<div class="text-right">
							<button class="btn btn-primary" type="button" onclick="updateOs()">修改</button>
						</div>
					</form>
	    		</div>
    		</div>
    	</div>
    </div>
</body>
</html>