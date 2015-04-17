<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>五洲传播--用户管理</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <style>
    input{
    	margin-bottom: 1em;
    }
    #myModal .modal-dialog {
	  width: 60%;
	}
    .pageInput {
    	border-radius: 4px;
    	border: 1px solid #ccc;
    	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
    	box-shadow: inset 0px 1px 1px rgba(0,0,0,0.075);
    	-webkit-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    	padding: 0 10;
    	color: #555;
    	height: 32px;
    	width: 3em;
    }
    
    </style>
    <script type="text/javascript">
  	//当前页
    var currentPage = parseInt("<s:property value="pageEntity.currentPage" />");
    //总页数
    var pageCount = parseInt("<s:property value="pageEntity.pageCount" />");
    //每页显示总记录数
    var pageRowCount = parseInt("<s:property value="pageEntity.pageRowCount" />");
    $(function() {
    	//初始化页码
    	initPageNum();
    	var user = "<s:property value="urList"/>";
    	if(user){
	    	$(".form-control").each(function() {
	    		if($(this).val().length>0) {
	    			$("#alert").html("");
	    			$("#alert").css("display", "none");
	    		}
	    	})
    	}else{
    		document.getElementById("container").style.display = 'none';
    	}
    	
    })
    
    function editUpdate() {
    	var userId = $("#editUserId").val()
    	var roleIdHide = $("#roleIdHide").val();
    	var userName = $("#editUserNameInput").val();
    	var nickName = $("#editNickName").val();
    	var userPwd = $("#editUserPwd").val();
    	var email = $("#editEmail").val();
    	var roleId = $("#editRole").val();
    	if(roleIdHide==roleId) { //没有修改角色
    		roleId = -1;
    	}
    	if(userId.length>0&&userName.length>0&&nickName.length>0&&userPwd.length>0&&email.length>0) {
    		if(userName.length>30) {
    			alert("用户名太长，建议在 20 个字符以内");
    			return;
    		}
    		if(nickName.length>30) {
    			alert("昵称太长，建议在 20 个字符以内");
    			return;
    		}
    		if(userPwd.length>20) {
    			alert("密码太长，建议在 20 个字符以内");
    			return;
    		}
    		$.ajax({
    			url:"updateUser.action",
    			data:{userId:userId, userName:userName, nickName:nickName, userPwd:userPwd,email:email, roleId:roleId},
    			async : false,
    			type:'post',
    			success: function(data){
    				if(data=="0") {
        				alert("修改失败，请刷新页面重试");
        			} else {
                		window.location.href="userList.action";
                		$("#myModal").modal('hide');
        			}
    			}
    		});
    	} else {
    		$("#editAlert").html("用户信息填写不完整！");
    		$("#editAlert").css("display", "block");
    	}
    	
    }
    
    function editUser(userId, userName, nickName, userPwd, email, roleId) {
    	$("#editUserId").val(userId);
    	$("#editUserNameInput").val(userName);
    	$("#editUserNameHidden").val(userName);
    	$("#editNickName").val(nickName);
    	$("#editUserPwd").val(userPwd);
    	$("#editEmail").val(email);
    	$("#roleIdHide").val(roleId); //修改前的roleId
    	$("#editRole").val(roleId);
    	$("#myModal").modal({show:true});
    }
    
    function deleteUser(userId) {
    	if(window.confirm("确定要删除吗？")) {
    		$.post("deleteUser.action", {userId:userId}, function(data) {
        		if(data=="1") {
        			alert("删除成功!");
        			//document.forms['editForm'].action="userList.action";
        			//document.forms['editForm'].submit();
        			window.location.href="userList.action";
        		} else { 
        			alert("对不起，删除失败!");
        		}
        	})
    	}
    }
    
    /**
     * 初始化翻页
     */
    function initPageNum() {
    	if(currentPage<=1) {
    		$("#prePageTag").attr("disabled", true);
    		$("#firstPageTag").attr("disabled", true);
    	} else {
    		$("#prePageTag").attr("disabled", false);
    		$("#firstPageTag").attr("disabled", false);
    	}
    	if(currentPage >= pageCount) {
    		$("#nextPageTag").attr("disabled", true);
    		$("#endPageTag").attr("disabled", true);
    	} else {
    		$("#nextPageTag").attr("disabled", false);
    		$("#endPageTag").attr("disabled", false);
    	}
    }

    /**
     * 上一页
     */
    function prePage() {
    	currentPage = currentPage - 1;
    	page(currentPage);
    }

    /**
     * 下一页
     */
    function nextPage() {
    	currentPage = currentPage + 1;
    	page(currentPage);
    }

    /**
     * 跳至XX页
     */
    function jumpPage() {
    	var toPage = $(".pageInput").val();
    	if(toPage=="") {
    		alert("页码是空的哦，亲 !");
    		return ;
    	}
    	var re = /^[1-9]+[0-9]*$/;
    	if(re.test(toPage)) {
    		var pageInt = parseInt(toPage);
    		if(pageInt>=1&&pageInt<=pageCount) {
    			page(pageInt);
    			currentPage=pageInt;
    			initPageNum();
    		} else {
    			alert("超出页码范围了，亲，请输入[1-"+pageCount+"]之间数字 !");
    		}
    	} else {
    		alert("请输入正确的页码格式！")
    		$(".pageInput").val("");
    	}
    }

    function firstPage() {
    	page(1);
    }

    function endPage() {
    	page(pageCount);
    }

    /**
     * 根据页码查询
     */
    function page(currentPage) {
    	$(".row table tbody").empty();
    	$.ajax({
    		url:'getUserListByPage.action',
    		data:{currentPage: currentPage},
    		type:'post',
    		async: false,
    		success:function(data) {
    			var json = eval("("+data+")");
        		var tableStr = "";
        		for(var i=0; i<json.urList.length;i++) {
        			tableStr = tableStr + "<tr>";
        			tableStr = tableStr + "<td>"+json.urList[i].user_name+"</td><td>"+json.urList[i].nick_name+"</td><td>"+json.urList[i].email+"</td><td>"+json.urList[i].role_name+"</td>"+
        			//tableStr = tableStr + "<td>"+json.urList[i].user_name+"</td><td>"+json.urList[i].nick_name+"</td><td>"+json.urList[i].user_pwd+"</td><td>"+json.urList[i].email+"</td><td>"+json.urList[i].role_name+"</td>"+
        			"<td style=\"text-align:center\"><a href=\"javascript:editUser('"+json.urList[i].user_id+"','"+json.urList[i].user_name+"','"+json.urList[i].nick_name+"','"+json.urList[i].user_pwd+"','"+json.urList[i].email+"','"+json.urList[i].role_id+"');\" title=\"编辑\"><span class=\"glyphicon glyphicon-edit\"></span></a>&nbsp;&nbsp;&nbsp;<a href=\"javascript:deleteUser("+json.urList[i].user_id+");\" title=\"删除\"><span class=\"glyphicon glyphicon-remove\"></span></a></td>";
        			tableStr = tableStr + "</tr>";
        		}
        		$("#currentPageSpan").html(json.pageEntity.currentPage);
        		$("#pageCountSpan").html(pageCount);
        		$("#pageRowCountSpan").html(pageRowCount);
        		$(".row table tbody").html(tableStr);
    		}
    	});
    	initPageNum();
    }
  //文本框限制长度
    function limitLength(arg, len) {
    	if($(arg).val().replace(/(^\s*)|(\s*$)/g, "").length>parseInt(len)) {
    		alert("超出限制的范围，请修改");
    		$(arg).focus();
    	}
    }
    </script>
  </head>
  <body>
  	<div class="container" id='container'>
  		<div class="row">
			<div class="col-sm-12">
			<table class="table table-bordered table-hover">
				<caption class="lead">用户信息<div style='text-align:right;font-size:8pt;'><a href='addUserPage.action'>注册新用户</a></div></caption>
				<thead>
					<tr>
						<th>登录账号</th>
						<th>用户姓名</th>
						<!-- <th>密 码</th> -->
						<th>邮箱</th>
						<th>角 色</th>
						<th style="text-align:center">操作</th>
					</tr>
				</thead>
				<tbody>		
					<s:iterator value="urList" id="urList">
						<tr>
							<td><s:property value="#urList.user_name"/></td>
							<td><s:property value="#urList.nick_name"/></td>
							<!-- <td>********</td>-->
							<!--<td><s:property value="#urList.user_pwd"/></td>-->
							<td><s:property value="#urList.email"/></td>
							<td><s:property value="#urList.role_name" /></td>
							<td style="text-align:center">
								<a href="javascript:editUser('<s:property value="#urList.user_id" />','<s:property value="#urList.user_name" />','<s:property value="#urList.nick_name" />','<s:property value="#urList.user_pwd" />','<s:property value="#urList.email" />','<s:property value="#urList.role_id" />');" title="编辑"><span class="glyphicon glyphicon-edit"></span></a>
								&nbsp;&nbsp;&nbsp;
								<a href="javascript:deleteUser('<s:property value="#urList.user_id" />');" title="删除"><span class="glyphicon glyphicon-remove"></span></a>
							</td>
						</tr>
					</s:iterator>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="6">
							<div class="pull-left">当前第[<span id="currentPageSpan"><s:property value="pageEntity.currentPage" /></span>]页，共[<span id="pageCountSpan"><s:property value="pageEntity.pageCount" /></span>]页，每页显示[<span id="pageRowCount"><s:property value="pageEntity.pageRowCount" /></span>]条</div>
							<div class="pull-right"><a href="javascript:prePage()" class="btn btn-default" type="button" id="prePageTag">&larr; 上一页 </a><a href="javascript:nextPage()"  class="btn btn-default" type="button" id="nextPageTag">下一页 &rarr;</a>&nbsp;&nbsp;跳至<input type="text" class="pageInput" />页<a href="javascript:jumpPage();" class="btn btn-default" type="button">转</a></div>
						</td>
					</tr>
				</tfoot>
			</table>
			</div>
		</div>
		<div class="row">
			
		</div>
	</div>
	
	<!-- 模态框 -->
    <div class="modal fade in" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
    		<div class="modal-content">
    			<div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">用户信息修改</h4>
                </div>
	    		<div class="modal-body">
	    			<form name="editForm" >
	    				<input type="hidden" id="editUserId" value="" />
	    				<input type="hidden" id="roleIdHide" value="" />
						<label>登录账号：</label><input id="editUserNameInput" class="form-control" disabled />
						<label>用户姓名：</label><input id="editNickName" class="form-control" onblur="limitLength(this, 50)" />
						<label>密  码：</label><input type='password' id="editUserPwd" class="form-control" onblur="limitLength(this, 50)" />
						<label>Email：</label><input id="editEmail" class="form-control" onblur="limitLength(this, 30)" />
						<label>角  色：</label>
						<select id="editRole" class="form-control">
							<s:iterator value="roleList" id="roleList">
								<option value="<s:property value="#roleList.role_id" />"><s:property value="#roleList.role_name" /></option>
							</s:iterator>
						</select>
						<br />
						<div class="alert alert-danger" id="editAlert" style="display:none"></div>
						<br />
						<div class="text-right">
							<a class="btn btn-primary" onclick="editUpdate()">修改</a>
						</div>
					</form>
	    		</div>
    		</div>
    	</div>
    </div>
  </body>
</html>