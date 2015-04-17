<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>五洲传播--用户注册</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<style>
</style>
<script type="text/javascript">
$(function() {
	var role = "<s:property value="roleList"/>";
	if(!role){
		document.getElementById("container").style.display = 'none';
	}
	$("#userName").blur(function() { //判断用户名是否已经被使用了
		var obj = $(this);
		var userName = obj.val();
		if(userName.length>0) {
			$.post("wzuser/checkUserName.action", {userName: userName}, function(data) {
				if(data=="0") {//可以正常使用
					$("#alert").html("");
					$("#alert").css("display", "none");
				} else if(data=="1") { //已经被使用了
					$("#alert").html("对不起，这个登录账号已经被使用了!");
					$("#alert").css("display", "block");
					obj.focus();
				} else if(data=="-1") { //查询出错了
					$("#alert").html("对不起，查询登录账号出错了!");
					$("#alert").css("display", "block");
					obj.focus();
				}
			});
		}
	});
	
	$(".form-control").each(function() {
		if($(this).val().length>0) {
			$("#alert").html("");
			$("#alert").css("display", "none");
		}
	});
});
function addUser() {
	var userName = $("#userName").val();
	var nickName = $("#nickName").val();
	var userPwd = $("#userPwd").val();
	var email = $("#email").val();
	if(userName.length>0&&nickName.length>0&&userPwd.length>0&&email.length>0) {
		if(userName.length>25) {
			alert("登录账号太长，请输入 25 个字符以内");
			return false;
		}
		if(nickName.length>25) {
			alert("用户姓名太长，请输入 25 个字符以内");
			return false;
		}
		if(userPwd.length>20) {
			alert("密码太长，请输入 20 个字符以内");
			return false;
		}
		return true;
	} else {
		$("#alert").html("用户信息填写不完整！");
		$("#alert").css("display", "block");
		return false;
	}
}

//文本框限制长度
function limitLength(arg, len) {
	if($(arg).val().replace(/(^\s*)|(\s*$)/g, "").length>parseInt(len)) {
		alert("超出限制的范围，请修改");
		$(arg).focus();
	}
}

function commitUser(){
	var userName = $("#userName").val();
	var nickName = $("#nickName").val();
	var userPwd = $("#userPwd").val();
	var email = $("#email").val();
}
</script>
</head>
<body>
	<div class="row" id='container'>
		<div class="col-md-4"></div>
		<div class="col-md-4">
			<p class="lead">用户注册</p>
			<form action="addUser.action" method="post" name="addForm" onSubmit="return addUser()">
				<input name="userEntity.user_name" placeholder="登录账号" id="userName" class="form-control" onblur="limitLength(this, 50)" />
				<br />
				<input name="userEntity.nick_name" placeholder="用户姓名" id="nickName" class="form-control" onblur="limitLength(this, 50)" />
				<br />
				<input name="userEntity.user_pwd" placeholder="密码" id="userPwd" type="password" class="form-control" onblur="limitLength(this, 20)" />
				<br />
				<select name="userRoleEntity.roleEntity.role_id" class="form-control ">
					<s:iterator value="roleList" id="roleList">
						<option value="<s:property value="#roleList.role_id" />">
							<s:property value="#roleList.role_name" />
						</option>
					</s:iterator>
				</select>
				<br />
				<input name="userEntity.email" placeholder="Email" id="email" class="form-control" />
				<br />
				<div class="alert alert-danger" id="alert" style="display:none"></div>
				<button class="btn btn-primary btn-block" type="submit">注册</button>
			</form>
		</div>
		<div class="col-md-4"></div>
	</div>
</body>
</html>