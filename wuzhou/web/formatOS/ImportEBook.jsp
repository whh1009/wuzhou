<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<html>
<head>
<title>五洲传播--导入存量资源</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<link rel="shortcut icon" href="../images/favicon.ico" />
<script src="../js/button.js"></script>
<style>
.out{
	height:600px;
	overflow:auto;
}
.aa{align:center;}
.aa img {display:block; margin:auto;}
</style>
<script>
$(function() {
	//初始化tooltip
	$('[data-toggle="tooltip"]').tooltip();

	$("#EbookGoHomeBtn").on("click", function() {
		$(this).button("loading");
		$(".out").html("");
		$.ajax({
			url:"moveEBooks.action",
			type:"post",
			async : true,
			beforeSend:function(XMLHttpRequest){
				$(".out").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
			},
			success:function(data) {
				$(".out").html(data);
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
				$(".out").html("<p class='text-danger'>"+textStatus+ "  " + errorThrown + "</p>");
			}
		});
		$(this).button("reset");
	});

	$("#yzBtn").on("click", function() {
		$(this).button("loading");
		$(".out").html("");
		$.ajax({
			url:"moveYZEBooks.action",
			type:"post",
			async : true,
			beforeSend:function(XMLHttpRequest){
				$(".out").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
			},
			success:function(data) {
				$(".out").html(data);
			},
			error:function(XMLHttpRequest,textStatus,errorThrown){
				$(".out").html("<p class='text-danger'>"+textStatus+ "  " + errorThrown + "</p>");
			}
		});
		$(this).button("reset");
	});
});
</script>
</head>
<body>
<div class="container">
	<div class="row">
		<div class="col-sm-12">
			<button type="button" class="btn btn-info" id="EbookGoHomeBtn" data-loading-text="请稍等..." autocomplete="off" data-toggle="tooltip" data-placement="bottom" title="电子书归档，包括EPUB，MOBI，阅读PDF，封面(非电子书封面)">电子书归档</button>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<button type="button" class="btn btn-success" id="yzBtn" data-loading-text="请稍等..." autocomplete="off" data-toggle="tooltip" data-placement="bottom" title="样章归档">样章归档</button>
			<p>&nbsp;</p>
			<div class="well">
				<div class="out"></div>
			</div>
		</div>
	</div>
</div>

	
</body>
</html>