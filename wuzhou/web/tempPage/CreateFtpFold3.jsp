<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>TP建立"电子书封面"和"样章"文件夹</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">

<link rel="stylesheet" type="text/css" href="<%=basePath %>css/bootstrap.min.css">
<script type="text/javascript" src="<%=basePath %>js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/bootstrap.min.js"></script>
<script type="text/javascript">
.aa{align:center;}
.aa img {display:block; margin:auto;}
</script>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
function createFold() {
	$(".btnFold").prop("disabled", true);
	$(".btnFold").html("请耐心等待几秒钟");
	$.ajax({
		url:"createFtpFold3.action",
		type : 'POST',
		beforeSend:function(XMLHttpRequest){
            $("#content").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
       	},
		success:function(data) {
			$("#content").html("创建成功");
		},
        error:function(XMLHttpRequest,textStatus,errorThrown){
            $("#content").html("<p class='text-danger'>"+textStatus+ "  " + errorThrown + "</p>");
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
				<button class="btn btn-lg btn-block btn-success btnFold" onclick="createFold()" autocomplete="off">创建FTP服务目录</button>
			</div>
			<div class="col-sm-6">
				<a class="btn btn-lg btn-block btn-info" href="toLogin.action">返回登录</a>
			</div>
		</div>
		<br />
		<div class="row">
			<div id="content"></div>
		</div>
	</div>
</body>
</html>
