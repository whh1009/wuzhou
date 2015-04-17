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
</style>
<script>
$(function() {
	
	
	$("#EbookGoHomeBtn").on("click", function() {
		$(this).button("loading");
		$(".out").html("");
		$.ajax({
			url:"moveEBooks.action",
			type:"post",
			async : true,
			success:function(data) {
				$(".out").html(data);
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
			
			<button type="button" class="btn btn-info" id="EbookGoHomeBtn" data-loading-text="请稍等..." autocomplete="off">电子书归档</button>
			<p>&nbsp;</p>
			<div class="well">
				<div class="out"></div>
			</div>
		</div>
	</div>
</div>

	
</body>
</html>