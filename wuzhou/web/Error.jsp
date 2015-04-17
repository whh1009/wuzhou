<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>错误页</title>
    <!-- Bootstrap framework -->
    <link rel="stylesheet" href="css/bootstrap.min.css" />
	<script src="js/jquery-1.10.2.min.js"></script>

  </head>
  
  <body>
  
    <div class="container">
    	<br />
    	<br />
    	<div class="row">
    		<div class="col-md-12">
    			<div class="alert alert-danger">
    				<h3>错误：</h3>
    				<p class="lead"><s:property value="exception"></s:property></p>
    			</div>
    		</div>
    	</div>
    	<div class="row">
    		<div class="col-md-12">
    			<p class="lead">您可以尝试以下办法：</p>
    			<a class="btn btn-primary btn-lg btn-block" href="toLogin.action">重新登录</a>
    		</div>
    	</div>
    </div>
    
    <script src="js/bootstrap.min.js"></script>
  </body>
</html>
