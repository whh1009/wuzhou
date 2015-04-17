<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	if(request.getSession().getAttribute("userEntity") == null) {
		System.out.println("SESSION 中用户信息已经过期");
		response.sendRedirect("toLogin.action");
		return;
	}
	if(request.getSession().getAttribute("navigation") == null) {
		System.out.println("SESSION 中缓存的导航菜单已过期");
		response.sendRedirect("toLogin.action");
		return;
	}
%>
<link rel="shortcut icon" href="<%=basePath %>images/favicon.ico" />
<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" />
<style>
li:hover{ background:#DDDDDD;}
.navbar-brand{padding:10px 15px; width:150px;}
</style>

<script src="<%=basePath %>js/jquery-1.10.2.min.js"></script>
<script src="<%=basePath %>js/bootstrap.min.js"></script>
<script>
//初始化导航条
$(function() {
	var navigation = "<%=request.getSession().getAttribute("navigation") %>";
	if(navigation.length>4) {
		$("body").prepend(navigation.replace(/&lt;/ig,'<').replace(/&gt;/ig,'>').replace(/&quot;/ig,'"'));
	}
});

//退出
function logout() {
	location.href="toLogin.action";
}
//密码修改
function modifyPwd() {
	$("#modifyPwdAlert").html("");
	$("#modifyPwdAlert").css("display", "none");
	$("#modifyPwdModal").modal("show");
	
}

function savePwd() {
	$("#modifyPwdAlert").html("");
	$("#modifyPwdAlert").css("display", "none");
	var ooldPwd = "<s:property value="#session.userEntity.user_pwd"></s:property>";
	var oldPwd = $("#oldPwd").val();
	var newPwd = $("#newPwd").val();
	if(oldPwd.length<1||newPwd.length<1) {
		$("#modifyPwdAlert").html("请输入密码！");
		$("#modifyPwdAlert").css("display", "block");
	} else {
		if(ooldPwd!=oldPwd) {
			$("#modifyPwdAlert").html("对不起，密码不对，请输入登录密码");
			$("#oldPwd").focus();
			$("#modifyPwdAlert").css("display", "block");
		} else {
			$("#modifyPwdAlert").html("");
			$("#modifyPwdAlert").css("display", "none");
			$.ajax({
				url:'modifyPwd.action',
				type:'post',
				async: false,
				data:{userPwd: newPwd},
				success: function(data) {
					if(data=="1") {
						alert("密码修改成功，下次登录请使用新密码");
						window.location.href="toLogin.action";
					} else {
						alert("密码修改失败，请刷新后重试");
					}
				}
			});
		}
	}
}


</script>
<!-- 模态框 -->
<div class="modal fade in" id="modifyPwdModal" tabindex="-1" role="dialog" aria-labelledby="modifyPwdModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="modifyPwdModalLabel">密码修改</h4>
            </div>
 			<div class="modal-body">
	 			<form name="editForm" >
	 				<input type="hidden" value="<s:property value="#session.userEntity.user_pwd"></s:property>" />
					<label>旧密码：</label><input id="oldPwd" class="form-control" />
					<label>新密码：</label><input id="newPwd" class="form-control" />
					<br />
					<div class="alert alert-danger" id="modifyPwdAlert" style="display:none"></div>
					<br />
					<div class="text-right">
						<a class="btn btn-primary" onclick="savePwd()">修改</a>
					</div>
				</form>
	 		</div>
		</div>
	</div>
</div>
