<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>中文数据更新</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css" href="<%=basePath %>css/bootstrap.min.css">
<script type="text/javascript" src="<%=basePath %>js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/bootstrap.min.js"></script>

<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
function updateBookInfo() {
	$(".btnFold").prop("disabled", true);
	$(".btnFold").html("请耐心等待几秒钟");
	$.ajax({
		url:"updateBookInfo1.action",
		type : 'POST',
		success:function(data) {
			$("#content").html(data);
		}
	});
}

</script>
</head>

<body>
	<div class="container">
		<br />
		<br />
		<br />
		<div class="row">
			<div class="col-sm-6">
				<button class="btn btn-lg btn-block btn-success btnFold" onclick="updateBookInfo()" autocomplete="off">中文数据更新</button>
			</div>
			<div class="col-sm-6">
				<a class="btn btn-lg btn-block btn-info" href="toLogin.action">返回登录</a>
			</div>
		</div>
		<div class="row">
			<div style="margin-top:2em;" id="content"></div>
		</div>
	</div>
</body>
</html>
