<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>五洲传播--角色权限配置</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<style>
.leval1{margin-left:0em;}
.leval2{margin-left:1.5em;}
.leval3{margin-left:3em;}
</style>
<script type="text/javascript">
var menuXml="<s:property value="menuXml" />".replace(/&lt;/ig, "<").replace(/&gt;/ig, ">");
$(function() {
	var role = "<s:property value="roleList"/>";
	if(!role){
		document.getElementById("container").style.display = 'none';
	}
	$("#alert").hide();
	$("#alert2").hide();
	
	//解析menuXml
	menuXml2CheckBox();
	//checkbox选中事件
	checkBoxClick();
	//初始化角色和菜单复选框
	initRole();
});

//添加角色
function addRole() {
	if($("#roleName").val().length>45) {
		alert("角色名太长，请重新输入");
		$("#roleName").focus();
		return;
	}
	var flag = false;
	var roleName = $("#roleName").val();
	if(roleName.length<1) {
		$("#alert").empty();
		$("#alert").html("请输入角色");
		$("#alert").show(1000);
		$("#roleName").focus();
		flag = false;
	}
	if(roleName.length>20) { //角色名的长度限制
		$("#alert").empty();
		$("#alert").html("角色名太长了，建议在 20 个字符以内");
		$("#alert").show(100);
		$("#roleName").focus();
		flag = false;
	}
	$.ajax({
		url:'checkRoleName.action',
		type:'post',
		async: false,
		data:{roleName: roleName},
		success: function(data) {
			if(data=="0") { //角色不存在，可以添加
				flag = true;
			} else if(data=="-1"){ //未获取到角色名
				$("#alert").empty();
				$("#alert").html("未获取到角色名，请刷新重试");
				$("#alert").show();
				flag = false;
			} else { //已存在
				$("#alert").empty();
				$("#alert").html("该角色已经存在");
				$("#alert").show();
				$("#roleName").focus();
				flag = false;
			}
		},
		error: function(data) {
			alert("检测角色名 error");
			flag = false;
		}
	});
	return flag;
}
var checkBoxStr="";
//菜单xml生成checkBox
function menuXml2CheckBox() {
	if(menuXml.length==0) { //没有菜单
		$(".menuTree").empty();
		$(".menuTree").html("没有菜单信息");
	} else {
		parserMenuXml("0");
		$(".menuTree").html(checkBoxStr);
	}
}

//解析menuXml
function parserMenuXml(menuPid) {
	var items=$(menuXml).find("item[menuPid='"+menuPid+"']");
	for(var i=0; i<items.length; i++) {
		var mid=$(items[i]).attr("menuId");
		var pid=$(items[i]).attr("menuPid");
		var leval=$(items[i]).attr("menuLeval");
		var menuName=$(items[i]).attr("menuName");
		checkBoxStr=checkBoxStr+"<label class='checkbox leval"+leval+"'><input type='checkbox' name='checkbox' class='pid"+pid+"_mid"+mid+"'>"+menuName+"</label>";
		parserMenuXml(mid);
	}
}

//选中一级checkbox，二级自动选中
function checkBoxClick() {
	$("input[name='checkbox']").click(function() {
		var classAttr=$(this).prop("class"); //pid0_mid1
		var classArray=classAttr.split("_");
		var parentId=classArray[0];
		var menuId=classArray[1];
		if($(this).is(":checked")) { //选中
			//1. 让父节点选中
			$(".checkbox").find("input[class$='"+parentId.replace("p","m")+"']").each(function() {
				$(this).prop("checked",true);
			});
			//2. 子节点全部选中
			$(".checkbox").find("input[class^='"+menuId.replace("m","p")+"']").each(function() {
				$(this).prop("checked",true);
			});
		} else { //取消选中
			//1. 子节点全部取消选中
			$(".checkbox").find("input[class^='"+menuId.replace("m","p")+"']").each(function() {
				$(this).prop("checked",false);
			});
			//2. 判断父节点是否有其他节点选中，有则不变，没有则取消父节点选中状态
			var flag=false;
			$(".checkbox").find("input[class^='"+parentId+"']").each(function() {
				if($(this).is(":checked")) { //被选中
					flag=true;
					return false;
				} else {
					flag=false;
				}
			});
			if(!flag) {
				$(".checkbox").find("input[class$='"+parentId.replace("p","m")+"']").each(function() {
					$(this).prop("checked",false);
					return false;
				});
			}
		}
	});
}

//选择角色时，重置复选框
function initRole() {
	$("#role").change(function() {
		$("input[name='checkbox']").each(function() {
			$(this).prop("checked", false);
		})
		
		var roleId=$(this).val();
		if(roleId=="0") {
			return false;
		} else {
			if(menuXml.length==0) { //没有菜单
				$(".menuTree").empty();
				$(".menuTree").html("没有菜单信息");
			} else {
				//alert(roleId);
				$(menuXml).find("roleItem[roleId='"+roleId+"']").each(function() {
					$(".checkbox").find("input[class$='mid"+$(this).attr("menuId")+"']").each(function() {
						$(this).prop("checked",true);
					});
				});
			}
		}
	});
}

//保存菜单,角色
function saveRoleMenu() {
	$("#alert2").hide();
	var roleId=$("#role").val();
	var menuId="";
	$("input[name='checkbox']:checked").each(function() {
		menuId = menuId + $(this).prop("class").split("_")[1].replace("mid","")+",";
	})
	if(roleId>0&&menuId.length>0) {
		$.ajax({
			url:'saveRoleMenu.action',
			type:'post',
			async: false,
			data:{roleId: roleId, menuId: menuId},
			success: function(data) {
				if(data=="1") {
					alert("保存成功");
					location.href="addRolePage.action";
				} else if(data=="-1"){
					alert("获取参数失败");
				} else {
					alert("修改失败");
				}
			}
		});
	} else {
		$("#alert2").html("菜单和角色都要选择哦");
		$("#alert2").show();
	}
	
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
		<div class="col-xs-5">
			<p class="lead">添加角色</p>
			<form action="addRole.action" method="post" name="addForm" onsubmit="return addRole()">
				<input name="roleEntity.role_name" placeholder="角色名" id="roleName" class="form-control" onblur="limitLength(this, 50)" />
				<br />
				<div class="alert alert-danger" id="alert"></div>
				<button class="btn btn-primary btn-block" type="submit">添加</button>
			</form>
		</div>
		<div class="col-xs-7">
			<p class="lead">权限分配</p>
			<div class="alert alert-danger" id="alert2"></div>
			<div class="col-xs-4">
				<p class="text-success">1. 选择的角色</p>
				<select class="form-control" id="role">
					<option value="0">请选择</option>
					<s:iterator value="roleList" id="roleList">
						<option value="<s:property value="#roleList.role_id" />">
							<s:property value="#roleList.role_name" />
						</option>
					</s:iterator>
				</select>
			</div>
			<div class="col-xs-4">
				<p class="text-success">2. 选择菜单 <br /><small><span class="text-danger">"新书数据采集"，"我的图书操作"配套勾选</span></small></p>
				<div class="menuTree"></div>
			</div>
			
			<div class="col-xs-4">
				<p class="text-success">3. 保存</p>
				<button class="btn btn-primary" onclick="saveRoleMenu()">保存</button>
			</div>
		</div>
	</div>
	</div>
</body>
</html>