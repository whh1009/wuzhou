<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
<head>
<title>五洲传播--上线平台管理</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<link rel="shortcut icon" href="<%=basePath %>images/favicon.ico" />
<link rel="stylesheet" href="<%=basePath %>css/bootstrap.min.css" />

<script src="<%=basePath %>js/jquery-1.10.2.min.js"></script>
<script src="<%=basePath %>js/bootstrap.min.js"></script>
<style type="text/css">

</style>

<script type="text/javascript">
$(function() {
});

</script>
</head>
<body>
<div class="container">
	<p>&nbsp;</p>
	<center>
		<h1><span class="glyphicon glyphicon-flag"></span> 临时页面 <span class="glyphicon glyphicon-flag"></span></h1>
		<h3> <small>用于处理突发需求且不在菜单中显示</small> </h3>
	</center>
	
	<p>&nbsp;</p>
	<div class="row">
		<div class="col-sm-12">
			<ul>
				<li><p><a href="<%=basePath %>tempPage/CreateFtpFold.jsp">存量资源创建FTP目录</a></p></li>
				<li><p><a href="<%=basePath %>tempPage/UpdateBookPrice.jsp">根据EXCEL更新电子书价格</a></p></li>
				<li><p><a href="tempPage/DeleteBookById.jsp">删除FTP目录中重复的书目<small>已有book_id列表</small></a></p></li>
				<li><p><a href="<%=basePath %>tempPage/RenamePDFFold.jsp">PDF改名（印刷PDF=>>分层PDF）</a></p></li>
				<li><p><a href="<%=basePath %>tempPage/UpdateBookInfo1.jsp">中文数据导入</a></p></li>
				<li><p><a href="<%=basePath %>tempPage/UpdateBookInfo2.jsp">阿文数据导入</a></p></li>
				<li><p><a href="<%=basePath %>tempPage/HistoryBookArchive.jsp">历史资源数据归档</a></p></li>
				<li><p><a href="<%=basePath %>tempPage/UpdateBookOnlineStatus.jsp"><span class="glyphicon glyphicon-star"></span> 更新图书在线状态</a></p></li>
				<li><p><a href="<%=basePath %>tempPage/UpdateBookPrice2.jsp">电子书价格更新</a></p></li>
				<li><p><a href="<%=basePath %>tempPage/CreateFtpFold2.jsp">补目录（采集系统中有这本书，FTP没有这个目录的全部补上）</a></p></li>
				<li><p><a href="<%=basePath %>tempPage/CreateFtpFold3.jsp">FTP建立"电子书封面"和"样章"文件夹<small>2015-04-15</small></a></p></li>
				<li><p><a href="<%=basePath %>tempPage/UpdateBookInfo.jsp"><span class="glyphicon glyphicon-star"></span> 自定义更新图书</a></p></li>
			</ul>
		</div>
	</div>
</div>
</body>
</html>