<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>更新图书在线状态</title>

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
<style type="text/css">
.aa{align:center;}
.aa img {display:block; margin:auto;}
</style>
<script type="text/javascript">
$(function() {
	//初始化tooltip
	$('[data-toggle="tooltip"]').tooltip();
	$("#nextBtn").hide();
});
function loadExcel() {
	$.ajax({
		url:"getSheetsByOnlineStatus.action",
		type : 'POST',
		beforeSend:function(XMLHttpRequest){
            //alert('远程调用开始...');
            $("#excelSheet").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
       },
		success:function(data) {
			$("#excelSheet").html(data);
			$("#nextBtn").show();
		},
         error:function(XMLHttpRequest,textStatus,errorThrown){
           $("#excelSheet").html("<p class='text-danger'>"+textStatus+ "  " + errorThrown + "</p>");
        }
	});
}

function updateBookOnlineStatus() {
	var sheetIds="";
	$("[name='sheetName']:checkbox").each(function(){
		if($(this).is(":checked")) {
			sheetIds+=$(this).val()+",";
		}
	});
	if(sheetIds.replace(/,/g,"")=="") return;
	$.ajax({
		url:"updateBookOnlineStatusBySheetIds.action",
		data:{sheetIds:sheetIds},
		type:"post",
		beforeSend:function(XMLHttpRequest){
            //alert('远程调用开始...');
            $("#content").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
       },
		success:function(data) {
			$("#content").html(data);
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
		<center><h2>图书状态在线标注</h2></center>
		<br />
		<div class="row">
			<div class="col-sm-12">
				<button class="btn btn-lg btn-success btnFold" onclick="loadExcel()" data-toggle="tooltip" data-placement="top" title="加载工作簿"> &#9312; 加载EXCEL</button>
			</div>
		</div>
		<div class="row">
			<div id="excelSheet"></div>
		</div>
		<hr />
		<div class="row" id="nextBtn">
			<div class="col-sm-12">
				<button type="button" class="btn btn-lg btn-warning" onclick="updateBookOnlineStatus()" data-toggle="tooltip" data-placement="top" title="点击后将更新勾选的工作簿"> &#9313; 更新图书在线状态</button>
			</div>
		</div>
		
		<!-- <div class="row">
			<div class="col-sm-6">
				<a class="btn btn-lg btn-block btn-info" href="toLogin.action">返回登录</a>
			</div>
		</div> -->
		<div class="row">
			<div style="margin-top:2em;" id="content"></div>
		</div>
	</div>
</body>
</html>
