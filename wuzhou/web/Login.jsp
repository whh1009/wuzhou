<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>图书数据采集系统--登录</title>
    	<link rel="shortcut icon" href="images/favicon.ico" />
        <!-- Bootstrap framework -->
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <!-- main styles -->
        <link rel="stylesheet" href="css/login.css" />
        <!--[if lte IE 8]>
            <script src="js/ie/html5.js"></script>
			<script src="js/ie/respond.min.js"></script>
        <![endif]-->
        <style type="text/css">
        	.reg-alert, .login-alert{color:#A94442;}
        </style>
        
        <script src="js/jquery-1.10.2.min.js"></script>
		<script type="text/javascript">
			$(function() {
				//监听回车登录
				$("#password").keydown(function(e) {
					var e = e||event;
					var keycode = e.which||e.keyCode;
					if(keycode==13) {
						login();
					}
				})
			})
			
			//删除登录提示信息
			function removeAlertInfo() {
				var userName = $("#username").val();
            	var password = $("#password").val();
            	if(userName!=""||password!="") {
            		$(".login-alert").html("");
            	}
			}
			
			//登录
            function login() {
            	var userName = $("#username").val();
            	var password = $("#password").val();
            	if(userName!=""&&password!="") {
            		$("#loginBtn").button('loading');
            		$.ajax({
            			url:"login.action",
            			type:"POST",
            			data:{userName: userName, password: password},
            			success:function(data) {
            				if(data=="1") {
                				location.href="wzbase/Index.jsp";
                				$("#loginBtn").button('reset');
                			} else if(data=="-1") {
               					$("#loginBtn").button('reset');
               					$("#username").focus();
               					$(".login-alert").html("用户名或密码不正确!");
                			} else if(data=="0") {
                				$(".login-alert").html("用户角色获取失败!");
                			}
            			},
            			error: function(XMLHttpRequest, textStatus, errorThrown) {
            				alert("登录请求错误！")
            			}
            		});
            		/*
            		$.post("login.action", {userName: userName, password: password}, function(data){
            			if(data=="1") {
            				document.forms['login_form'].action="bookList.action";
            				document.forms['login_form'].submit();
            				$("#loginBtn").button('reset');
            			} else {
            				if(data=="-1") {
            					$("#loginBtn").button('reset');
            					$("#username").focus();
            					$(".login-alert").html("用户名或密码不正确!");
            				}
            			}
	            	});
            		*/
            	} else {
            		$("#loginBtn").button('reset');
            		$("#username").focus();
            		$(".login-alert").html("用户名或密码不能为空!");
            	}
            }
			
			//注册
			function register() {
				var userName = $("#reg_username").val();
				var nickName = $("#reg_nickname").val();
				var pwd = $("#reg_pwd").val();
				var email = $("#reg_email").val();
				if(userName==""||nickName==""||pwd==""||email=="") {
					$(".reg-alert").html("注册信息填写不完整!")
					return false;
				} else {
					alert("注册成功，请登录!");
					return true;
				}
			}
    	</script>
    </head>
    <body class="login_page">
		
		<div class="login_box">
			
			<form method="post" name="login_form" id="login_form">
				<div class="top_b"><img alt="" src="images/logo-new.png" style="margin-left:35px;height:40px"><span style="margin-left:15px;">图书数据采集系统</span></div>
				<div class="cnt_b">
					<div class="row">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span><input class="form-control" type="text" id="username" name="userEntity.user_name" placeholder="用户名" />
						</div>
					</div>
					<div class="row">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span><input  class="form-control" type="password" id="password" name="userEntity.user_pwd" placeholder="密 码"  />
						</div>
					</div>
					<div class="row">
						<span class="login-alert"></span>
					</div>
				</div>
				<div class="btm_b clearfix">
					<a class="btn btn-primary pull-right" href="javascript:login()" type="button" data-loading-text="加载中……" id="loginBtn">登录</a>
					<!-- <span class="link_reg"><a href="#reg_form">没有账号 ? 点击注册</a></span> -->
				</div>  
			</form>
			
			<form id="softDown" style="display:none">
				<div class="top_b">软件下载</div>
				<div class="">
					<br />
					<!-- <div class="row">
						<div class="col-sm-1"></div>
						<div class="col-sm-3"><strong>文件名</strong></div>
						<div class="col-sm-5"><strong>备注</strong></div>
						<div class="col-sm-3"><strong>下载</strong></div>
					</div>
					<hr /> -->
					<div class="row">
						<div class="col-sm-1"></div>
						<div class="col-sm-3"><small>FileZilla win7</small></div>
						<div class="col-sm-5"><small>FTP文件上传下载客户端</small></div>
						<div class="col-sm-3"><a href="soft/FileZilla for win7.rar"><small>下载</small></a></div>
					</div>
					<br />
					<div class="row">
						<div class="col-sm-1"></div>
						<div class="col-sm-3"><small>FileZilla xp</small></div>
						<div class="col-sm-5"><small>FTP文件上传下载客户端</small></div>
						<div class="col-sm-3"><a href="soft/FileZilla for xp.rar"><small>下载</small></a></div>
					</div>
					<br />
					<div class="row">
						<div class="col-sm-1"></div>
						<div class="col-sm-3"><small>Chrome</small></div>
						<div class="col-sm-5"><small>使用IE11的可以不用下载</small></div>
						<div class="col-sm-3"><a href="soft/Chrome.rar"><small>下载</small></a></div>
					</div>
					<br />
					<!-- <table class="table" style="width:300px">
						<thead>
							<tr><th>文件名</th><th>备注</th><th>操作</th></tr>
						</thead>
						<tbody>
							<tr>
								<td>FileZilla</td>
								<td>FTP文件上传下载客户端</td>
								<td><a href="soft/FileZilla.rar">下载</a></td>
							</tr>
							<tr>
								<td>Chrome</td>
								<td>浏览器使用IE11可以不用下载</td>
								<td><a href="soft/Chrome.rar">下载</a></td>
							</tr>
						</tbody>
					</table> -->
				</div>
			</form>
			
			<form action="" method="post" id="pass_form" style="display:none">
				<div class="top_b">忘记密码 ?</div>
				<div class="cnt_b">
					<div class="row">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span><input class="form-control" type="text" name="userEntity.email" placeholder="Email" />
						</div>
					</div>
					<div class="row"><small>&nbsp;&nbsp;登录邮箱后打开链接修改您的密码!</small></div>
				</div>
				<div class="btm_b clearfix">
					<button class="btn btn-primary pull-right" type="submit">确定</button>
				</div>  
			</form>
			
			<form action="addUser.action" method="post" onSubmit="return register()" id="reg_form" style="display:none">
				<div class="top_b">我要注册</div>
				<div class="cnt_b">
					<div class="row">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span><input class="form-control" name="userEntity.user_name" id="reg_username" type="text" placeholder="用户名" />
						</div>
					</div>
					<div class="row">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span><input class="form-control" name="userEntity.nick_name" id="reg_nickname" type="text" placeholder="昵 称" />
						</div>
					</div>
					<div class="row">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span><input class="form-control" name="userEntity.user_pwd" id="reg_pwd" type="password" placeholder="密 码" />
						</div>
					</div>
					<div class="row">
						<div class="input-group">
							<span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span><input class="form-control" type="text" name="userEntity.email" id="reg_email" placeholder="Email" />
						</div>
					</div>
					<div class="row">
						<span class="reg-alert"></span>
					</div>
				</div>
				<div class="btm_b clearfix">
					<button class="btn btn-primary pull-right" type="submit">注册</button>
				</div>  
			</form>
			
		</div>
		
		<div class="links_b links_btm clearfix">
			<!-- <span class="linkform"><a href="#pass_form">忘记密码 ?</a></span>
			<span class="linkform" style="display:none">已有账号 , <a href="#login_form">返回登录</a></span> -->
			<span class="linkform"><a href="#softDown">软件下载</a></span>
			<span class="linkform" style="display:none"><a href="#login_form">返回登录</a></span>
		</div>
        
        <script src="js/jquery.actual.min.js"></script>
        <script src="js/jquery.validate.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/button.js"></script>
        <script>
            $(document).ready(function(){
				//初始化页面动画效果
				var form_wrapper = $('.login_box');
                $('.linkform a,.link_reg a').on('click',function(e){
                	//删除登录注册提示的错误信息
                	$(".login-alert").html("");
                	$(".reg-alert").html("");
                	
					var target	= $(this).attr('href'),
						target_height = $(target).actual('height');
					$(form_wrapper).css({
						'height': form_wrapper.height()
					});	
					$(form_wrapper.find('form:visible')).fadeOut(400,function(){
						form_wrapper.stop().animate({
                            height	: target_height
                        },500,function(){
                            $(target).fadeIn(400);
                            $('.links_btm .linkform').toggle();
							$(form_wrapper).css({
								'height': ''
							});	
                        });
					});
					e.preventDefault();
				});
				
				
            });
        </script>
    </body>
</html>