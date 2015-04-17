<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<title>五洲传播--更新图书价格</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<link rel="shortcut icon" href="<%=basePath %>images/favicon.ico" />
<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" />

<script src="<%=basePath %>js/jquery-1.10.2.min.js"></script>
<script src="<%=basePath %>js/bootstrap.min.js"></script>
<style>
#out{
	height:600px;
	overflow:auto;
}
.aa{align:center;}
.aa img {display:block; margin:auto;}
</style>
<script>
$(function() {
	$('#updatePriceBtn').on('click', function () {
		$(this).button('loading');
		$('.out').html('');
		$.ajax({
			url:'updateBookPrice2.action',
			type:'post',
			beforeSend:function(XMLHttpRequest){
	            $("#out").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
	       },
			success:function(data) {
				$("#out").html(data);
			},
	         error:function(XMLHttpRequest,textStatus,errorThrown){
	             $("#out").html("<p class='text-danger'>"+textStatus+ "  " + errorThrown + "</p>");
	          }
		});
		$(this).button('reset');
	});
	
});
</script>
</head>
<body>
<div class="container">
<p>&nbsp;</p><p>&nbsp;</p><p>&nbsp;</p>
	<div class="row">
		<div class="col-sm-6">
			<button type="button" id="updatePriceBtn" data-loading-text="请稍等..." class="btn btn-info btn-block btn-lg"  autocomplete="off">更新电子书价格</button>
		</div>
		<div class="col-sm-6">
			<a class="btn btn-success btn-block btn-lg" href="toLogin.action">返回登录</a>
		</div>
	</div>
	<div class="row">
		<div class="col-sm-12">
			<p>&nbsp;</p>
			<div class="well">
				<div id="out"></div>
			</div>
		</div>
	</div>
	
</div>

	
</body>
</html>