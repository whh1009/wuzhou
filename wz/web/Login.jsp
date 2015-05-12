<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<link href="${ctx}/css/bootstrap.min.css" rel="stylesheet">
<link href="${ctx}/css/signin.css" rel="stylesheet">
<style>
.msg{
	color:red;
	clear:both;
}
.box{
	margin:0 auto;
}
</style>
<script type="text/javascript" src="${ctx}/js/jquery-1.10.2.min.js"></script>
<script>
	$(function() {
		$(".msg").hide();
		
		
	});

	//验证登录的form表单
	function validateForm() {
		var userName=$("input[name='user.user_name']").val().trim();
		var pwd=$("input[name='user.user_password']").val().trim();
		if(userName==""){
			$(".msg").html("请输入用户名");
			$("input[name='user.user_name']").focus();
			return false;
		}
		if(pwd=="") {
			$(".msg").html("请输入密码");
			$("input[name='user.user_password']").focus();
			return false;
		}
		return true;
	}
	
	function login() {
		var userName = $("#userName").val().trim();
		var pwd = $("#pwd").val().trim();
		if (userName == "") {
			$(".msg").html("请输入用户名");
			$(".msg").show();
			$("#userName").focus();
			return;
		} else {
			$(".msg").hide();
		}
		if (pwd == "") {
			$(".msg").html("请输入密码");
			$(".msg").show();
			$("#pwd").focus();
			return;
		} else {
			$(".msg").hide();
		}
		$.ajax({
			url : "${ctx }/user/login",
			type : "post",
			data : {
				userName : userName,
				userPassword : pwd
			},
			dataType : "json",
			success : function(data) {
				if (data != "1") {
					$(".msg").html("对不起，用户名或密码有误");
					$(".msg").show();
				} else {
					window.location.href = "${ctx}/";
				}
			}
		});
	}
	
	//监听回车
	function enterPress(event) {
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		if(keyCode == 13){ 
			login();
		} 
	}
</script>

</head>

<body class="login-bg">
	<div class="container">
		<div class="row">
			<div class="login-wrapper">
				<div class="box">
					<div class="content-wrap">
						<h3>工具书数据库检索</h3>
						<br />
						<div class="col-sm-12">
							<input type="text" name="user.user_name" id="userName" class="form-control" placeholder="用户名" maxlength="30" onkeydown="enterPress(event)" />
						</div>
						<div class="col-sm-12">
							<input type="password" name="user.user_password" id="pwd" class="form-control" placeholder="密&nbsp;&nbsp;码"  maxlength="30" onkeydown="enterPress(event)" />
						</div>
						<p class="msg"></p>
						<button class="btn btn-primary login" type="button" onclick="login()">登录</button>
					</div>
				</div>
				<div class="col-sm-12 no-account">
					<!-- <p>没有帐号 ?<a href="#">注册</a></p> -->
				</div>
			</div>
		</div>
	</div>
</body>
</html>
