<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- Bootstrap -->
<link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
<link rel="stylesheet" href="../css/jquery.tagsinput.css" />

    
<style type="text/css">
.row {
	margin-top: 1em;
}

.input-group-addon,.input-group-myaddon {
	font-weight: bold;
}

.label {
	line-height: 1.5;
}
.bitian {
	color: #CC0033;
}

input[name='bookEntity.book_name_e'], input[name='bookEntity.book_author_e'],input[name='bookEntity.book_translator_e'],input[name='bookEntity.book_series_e'],
textarea[name='bookEntity.book_content_intr_e'],textarea[name='bookEntity.book_author_intr_e'],textarea[name='bookEntity.book_editor_recommend_e']
{
	direction:rtl;
	unicode-bidi:embed;
}

</style>
<!-- 
class样式说明
pt 出版类型，分为本社和和其它两种，本社和其它切换显示
zzs 纸质书属性，用于电子书和纸质书之间切换显示
dzs 电子书属性，用于切换显示
 -->
<script type="text/javascript">
	//获取当前用户
	//var userName = "<s:property value='#session.userEntity.user_name'></s:property>";
	$(function() {
		//修正readOnly退格不后退
		$("*[readOnly]").keydown(function(e) {
			var theEvent = window.event || e;
			var code = theEvent.keyCode || theEvent.which;
			if(code=="8") { //退格键
				if($(this).prop("readonly")){
					theEvent.preventDefault();
				}
			}
			//
		});
		
		//初始化标签--“关键词”
		$(".tags").each(function() {
			$(this).tagsInput({
				width : 'auto',
				height : 'auto'
			});
		});
		//标签页
		$("#myTab a").click(function(e) {
			e.preventDefault();
			$(this).tab("show");
		});

		//"点击下拉框"给文本框赋值
		$(".dropdown-menu a").click(function() {
			var selectedVal = $(this).html().replace(/&nbsp;/g, " ").replace(/&amp;/g, "&");
			$(this).parent().parent().parent().parent().children("input").val(selectedVal);
		});
		$("#view1").click(function() {
			//$(this).parent().remove();
			$("br").remove();
			$("#modalBody").empty();
			//$("#modalBody").html($("#fagao").html());
			$("#myModal").modal("show");
		});
		$("#view2").click(function() {
			//$(this).parent().remove();
			$("br").remove();
			$("#modalBody").empty();
			//$("#modalBody").html($("#sheji").html());
			$("#myModal").modal("show");
		});
		$("#view3").click(function() {
			//$(this).parent().remove();
			$("br").remove();
			$("#modalBody").empty();
			//$("#modalBody").html($("#yinzhi").html());
			$("#myModal").modal("show");
		});
		sumZYS();
		
		initFTPServerPath();
		roleFormConfig();
		
		//默认关闭双语对应
		$(".bilingual").attr("readonly", "");
		//初始化设置双语对应为黑色
		$(".sydybt").css("color","#555555");
		
	});
	
	//级联菜单
	//arg1 铜版纸/哑粉纸...
	//arg2 克重
	//tagId 标签id
	//parentBtnId 父按钮id
	//parentBtnId2 二级联动的id
	//inputTagId 要设置值的input id
	function jilian(arg1, arg2, tagId, parentBtnId1, parentBtnId2, inputTagId) {
		$("#"+tagId).empty();
		var theHtml = "";
		var arr = arg2.split("/");
		for(var i=0; i<arr.length; i++) {
			theHtml=theHtml+"<li><a href=\"javascript:jilian2('"+arg1+"', '"+arr[i]+"','"+parentBtnId2+"','"+inputTagId+"');\">"+arr[i]+"</a></li>";
		}
		$("#"+tagId).html(theHtml);
		$("#"+parentBtnId2).html("用纸克重");
		$("#"+inputTagId).val("");
		$("#"+parentBtnId1).html(arg1);
	}
	
	//级联菜单
	function jilian2(arg1, arg2, arg3, arg4) {
		$("#"+arg3).html(arg2);
		$("#"+arg4).val(arg1+" "+arg2);
	}

	//打开双语对应
	function opensydy() {
		$(".bilingual").removeAttr("readonly");
		$(".sydybt").css("color","#CC0033");
		//$(".bilingual").removeProp("readonly");
	}
	//禁用双语对应
	function closesydy() {
		$(".bilingual").val("");//清空双语内容
		$(".bilingual").attr("readonly", "");
		$(".sydybt").css("color","#555555");
	}

	//初始化文种
	function initWenzhong(args1, args2) {
		if (args1 == "yw") {
			$(".ww").each(function() {
				//$(this).children("input").prop("readonly",true);//不可编辑？
				$(this).children("input").val("");
				$(this).hide();//隐藏
			});
		} else {
			$(".ww").each(function() {
				$(this).show();
			});
		}
		if(args1=="ay"||args1=="bs") { //阿文，波斯语，右排序direction: rtl; unicode-bidi: bidi-override;
			$("textarea[name='bookEntity.book_content_intr_foreign']").css({"direction":"rtl","unicode-bidi":"ebmed"});			
			$("textarea[name='bookEntity.book_author_intr_foreign']").css({"direction":"rtl","unicode-bidi":"ebmed"});
			$("textarea[name='bookEntity.book_editor_recommend_foreign']").css({"direction":"rtl","unicode-bidi":"ebmed"});
		}else {
			$("textarea[name='bookEntity.book_content_intr_foreign']").css({"direction":"inherit"});			
			$("textarea[name='bookEntity.book_author_intr_foreign']").css({"direction":"inherit"});
			$("textarea[name='bookEntity.book_editor_recommend_foreign']").css({"direction":"inherit"});
		}
		
		$("#bsn3").val(args2);
		//设置图书编号
		$("#book_serial_number").val($("#bsn0").val() + "_" + $("#bsn1").val() + "_" + $("#bsn2").val() + "_" + $("#bsn3").val() + "_" + $("#bsn4").val());
		//初始化FTP服务器路径
		initServerPath();
	}

	//总印数
	function sumZYS() {
		var xwbysVal = $("#xwbys").val();
		var fxysVal = $("#fxys").val();
		$("#zys").val(parseInt(xwbysVal) + parseInt(fxysVal));
	}
	//数字
	function keyUp(ob) {
		//if (ob.value.length == 1) {
		//	ob.value = ob.value.replace(/[^1-9]/g, '');
		//} else {
			ob.value = ob.value.replace(/\D/g, '');
		//}
	}

	//浮点数
	function inFloat(arg) {
		if ('' != $(arg).val().replace(/\d{1,}\.{0,1}\d{0,}/, '')) {
			$(arg).val($(arg).val().match(/\d{1,}\.{0,1}\d{0,}/) == null ? '' : $(arg).val().match(/\d{1,}\.{0,1}\d{0,}/));
		}
	}

	function onBlur(ob) {
		if (!ob.value.match(/^\d*?$/)) {
			alert("请输入数字");
			ob.focus();
			ob.value = "";
		} else {
			var xwbysVal = $("#xwbys").val() == "" ? "0" : $("#xwbys").val();
			var fxysVal = $("#fxys").val() == "" ? "0" : $("#fxys").val();
			$("#zys").val(parseInt(xwbysVal) + parseInt(fxysVal));
		}
	}

	function onBlur2(ob) {
		if (!ob.value.match(/^\d*?$/)) {
			alert("请输入数字");
			ob.focus();
			ob.value = "";
		}
	}

	//文本框限制长度
	function limitLength(arg, len) {
		if ($(arg).val().replace(/(^\s*)|(\s*$)/g, "").length > parseInt(len)) {
			alert("超出限制的范围，请修改");
			$(arg).focus();
		}
	}

	//设置数据文档中的资源文件文本框的显示和隐藏
	function formatPath(arg) {
		var colDiv = $(arg).parent().parent().parent().parent().parent();
		var isin = $(arg).parent().text();
		if (isin == "无") {
			colDiv.next(".col-sm-8").hide();
		} else if(isin=="有"){
			colDiv.next(".col-sm-8").show();
		}
	}
	
	//初始化ftp路径的显示和隐藏
	function initFTPServerPath() {
		$(".spath").each(function() {
			if($(this).val()=="无"||$(this).val()=="") {
				$(this).parent().parent().next(".col-sm-8").hide();
				//$(this).parent().parent().next(".col-sm-8 input").val("");
			} else {
				$(this).parent().parent().next(".col-sm-8").show();
			}
		});
	}
	
	//根据角色配置隐藏表单内容
	function roleFormConfig() {
		var configXml = '<s:property value="#session.roleFormConfig" />'.replace(/&lt;/g,"<").replace(/&gt;/g,">").replace(/&quot;/g,"\"");
		var roleId = '<s:property value="#session.roleEntity.role_id" />';
		$(configXml).find("item[roleId='"+roleId+"']").each(function() {
			var names = $(this).attr("name").split(",");
			for(var i=0; i<names.length; i++){
				$("*[name='"+names[i]+"']").parent().parent().parent().hide();
			}
		});
	}
	
	//flag=true右排序 
	function changeWordDirect(flag, obj) {
		if(flag) {
			
		}
	}
	
</script>

</head>
<body>
	<div class="container">
		<form name="form" method="post">
			<%-- <input type="hidden" name="bookEntity.user_id" value="<s:property value='#session.userEntity.user_id'></s:property>" />
			<input type="hidden" name="bookEntity.book_flag" value=0 />
			<input type="hidden" name="bookEntity.book_del_flag" value=0 /> --%>
			
			<div class="row pt">
				<ul class="nav nav-tabs nav-justified" id="myTab">
					<li></li>
					<li class="active pt"><a href="#fagao" data-toggle="tab">新书发稿</a></li>
					<li class="pt"><a href="#tuijie" data-toggle="tab">新书推介</a></li>
					<li class="zzs pt"><a href="#sheji" data-toggle="tab">装帧设计</a></li>
					<li class="zzs pt"><a href="#yinzhi" data-toggle="tab">印制发行</a></li>
					<li class="pt"><a href="#wendang" data-toggle="tab">数据文档</a></li>
					<li class=""><a href="#others" data-toggle="tab">其它</a></li>
					<li></li>
				</ul>
			</div>
			<div class="tab-content">
				<div class="tab-pane fade in active" id="fagao">
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian zbitian wbitian">ISBN/ISSN</span></span> <input class="form-control" id="book_isbn" name="bookEntity.book_isbn" onblur="myLimit(this)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon twz" data-toggle="dropdown">
										<span class="bitian zbitian wbitian">文种 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:closesydy();initWenzhong('yw','001');">001--英文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('xw','002');">002--西文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('zw','003');">003--中文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('fw','004');">004--法文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('dy','005');">005--德语</a></li>
										<li><a href="javascript:closesydy();initWenzhong('ay','006');">006--阿语</a></li>
										<li><a href="javascript:closesydy();initWenzhong('ey','007');">007--俄语</a></li>
										<li><a href="javascript:closesydy();initWenzhong('tw','008');">008--土文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('ry','009');">009--日语</a></li>
										<li><a href="javascript:closesydy();initWenzhong('hy','010');">010--韩语</a></li>
										<li><a href="javascript:closesydy();initWenzhong('ydly','011');">011--意大利语</a></li>
										<li><a href="javascript:closesydy();initWenzhong('ynw','012');">012--印尼文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('hskstw','013');">013--哈萨克斯坦文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('mw','014');">014--蒙文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('zw','015');">015--藏文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('bs','016');">016--波斯文</a></li>
										<li><a href="javascript:closesydy();initWenzhong('bs','017');">017--柯尔克孜文</a></li>
										<li><a href="javascript:opensydy();initWenzhong('sydy','500');">500--双语对应</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_language" id="wenzhong" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian sydybt">双语对应</span></span>
								<input class="form-control bilingual" name="bookEntity.book_bilingual" onblur="limitLength(this, 200)" type="text" readonly>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group date form_date" data-date="" data-date-format="yyyy-MM">
								<input class="form-control" size="16" name="bookEntity.book_publish_time" placeholder="出版时间" type="text" value="" readonly> <span class="input-group-addon">
									<span class="bitian zbitian wbitian"><span class="glyphicon glyphicon-remove"></span></span>
								</span> <span class="input-group-addon"> <span class="bitian zbitian wbitian"><span class="glyphicon glyphicon-calendar"></span></span>
								</span>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian zbitian">中图分类号</span></span>
								<input class="form-control" name="bookEntity.book_clcc" onblur="limitLength(this, 20)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">美图分类号</span>
								<input class="form-control" name="bookEntity.book_alcc" onblur="limitLength(this, 20)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group zw">
								<span class="input-group-addon"><span class="bitian zbitian wbitian">责编</span></span>
								<input class="form-control formch" name="bookEntity.book_editor" onblur="limitLength(this, 50)" type="text">
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian wbitian">合作出版社</span></span>
								<input class="form-control" name="bookEntity.book_cooperate_press" onblur="limitLength(this, 200)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group zw">
								<span class="input-group-addon"><span class="bitian zbitian wbitian">书名（中文）</span></span>
								<input class="form-control formch" name="bookEntity.book_name_cn" id="bookNameCN" onblur="limitLength(this, 200)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group yw has-success">
								<span class="input-group-addon"><span class="bitian zbitian">书名（英文）</span></span>
								<input class="form-control formen" name="bookEntity.book_name_english" onblur="limitLength(this, 200)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group ww has-warning">
								<span class="input-group-addon"><span class="bitian wbitian">书名（外文）</span></span>
								<input class="form-control formfo" name="bookEntity.book_name_foreign" onblur="limitLength(this, 200)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group zw">
								<span class="input-group-addon"><span class="bitian zbitian wbitian">著者（中文）</span></span>
								<input class="form-control formch" name="bookEntity.book_author" onblur="limitLength(this, 50)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group yw has-success">
								<span class="input-group-addon"><span class="bitian  zbitian">著者（英文）</span></span>
								<input class="form-control formen" name="bookEntity.book_author_english" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group ww has-warning">
								<span class="input-group-addon"><span class="bitian wbitian">著者（外文）</span></span>
								<input class="form-control formfo" name="bookEntity.book_author_foreign" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
					</div>

					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group zw">
								<span class="input-group-addon"><span class="bitian">译者（中文）</span></span>
								<input class="form-control formch" name="bookEntity.book_translator" onblur="limitLength(this, 50)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group yw has-success">
								<span class="input-group-addon"><span class="bitian">译者（英文）</span></span>
								<input class="form-control formen" name="bookEntity.book_translator_english" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group ww has-warning">
								<span class="input-group-addon"><span class="bitian">译者（外文）</span></span>
								<input class="form-control formfo" name="bookEntity.book_translator_foreign" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group zw">
								<span class="input-group-addon"><span class="bitian">丛书名（中文）</span></span>
								<input class="form-control formch" name="bookEntity.book_series_cn" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group yw has-success">
								<span class="input-group-addon"><span class="bitian">丛书名（英文）</span></span>
								<input class="form-control formen" name="bookEntity.book_series_english" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group ww has-warning">
								<span class="input-group-addon"><span class="bitian">丛书名（外文）</span></span>
								<input class="form-control formfo" name="bookEntity.book_series_foreign" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">书名（法文）</span></span>
								<input class="form-control" name="bookEntity.book_name_fa" onblur="limitLength(this, 200)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">书名（西文）</span></span>
								<input class="form-control" name="bookEntity.book_name_xi" onblur="limitLength(this, 200)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">书名（阿文）</span></span>
								<input class="form-control" name="bookEntity.book_name_e" onblur="limitLength(this, 200)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">著者（法文）</span></span>
								<input class="form-control" name="bookEntity.book_author_fa" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">著者（西文）</span></span>
								<input class="form-control" name="bookEntity.book_author_xi" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">著者（阿文）</span></span>
								<input class="form-control" name="bookEntity.book_author_e" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">译者（法文）</span>
								<input class="form-control" name="bookEntity.book_translator_fa" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">译者（西文）</span>
								<input class="form-control" name="bookEntity.book_translator_xi" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">译者（阿文）</span>
								<input class="form-control" name="bookEntity.book_translator_e" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">丛书名（法文）</span>
								<input class="form-control" name="bookEntity.book_series_fa" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">丛书名（西文）</span>
								<input class="form-control" name="bookEntity.book_series_xi" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">丛书名（阿文）</span>
								<input class="form-control" name="bookEntity.book_series_e" onblur="limitLength(this, 100)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon" data-toggle="dropdown">
										<span class="bitian zbitian wbitian">建议分类 1 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">01-艺术(ART)</a></li>
										<li><a href="javascript:;">02-传记/自传(BIOGRAPHY & AUTOBIOGRAPHY)</a></li>
										<li><a href="javascript:;">03-经济(BUSINESS & ECONOMICS)</a></li>
										<li><a href="javascript:;">04-卡通/绘本(CARTOONS)</a></li>
										<li><a href="javascript:;">05-童书(CHILDREN'S BOOKS)</a></li>
										<li><a href="javascript:;">06-美食(COOKING)</a></li>
										<li><a href="javascript:;">07-文化(CULTURE)</a></li>
										<li><a href="javascript:;">08-休闲养生(HEALTH & FITNESS)</a></li>
										<li><a href="javascript:;">09-历史(HISTORY)</a></li>
										<li><a href="javascript:;">10-文学(LITERATURE & FICTION)</a></li>
										<li><a href="javascript:;">11-大众传播(MASS COMMUNICATION)</a></li>
										<li><a href="javascript:;">12-军事(MILITARY)</a></li>
										<li><a href="javascript:;">13-哲学(PHILOSOPHY)</a></li>
										<li><a href="javascript:;">14-诗歌(POETRY)</a></li>
										<li><a href="javascript:;">15-政治(POLITICAL SCIENCE)</a></li>
										<li><a href="javascript:;">16-指南(REFERENCE)</a></li>
										<li><a href="javascript:;">17-宗教(RELIGION)</a></li>
										<li><a href="javascript:;">18-社科(SOCIAL SCIENCE)</a></li>
										<li><a href="javascript:;">19-旅游(TRAVEL)</a></li>
										<li><a href="javascript:;">99-其他(OTHERS)</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_category1" onblur="limitLength(this, 50)" placeholder="填写或请选择">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon" data-toggle="dropdown">
										建议分类 2 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">01-艺术(ART)</a></li>
										<li><a href="javascript:;">02-传记/自传(BIOGRAPHY & AUTOBIOGRAPHY)</a></li>
										<li><a href="javascript:;">03-经济(BUSINESS & ECONOMICS)</a></li>
										<li><a href="javascript:;">04-卡通/绘本(CARTOONS)</a></li>
										<li><a href="javascript:;">05-童书(CHILDREN’S BOOKS)</a></li>
										<li><a href="javascript:;">06-美食(COOKING)</a></li>
										<li><a href="javascript:;">07-文化(CULTURE)</a></li>
										<li><a href="javascript:;">08-休闲养生(HEALTH & FITNESS)</a></li>
										<li><a href="javascript:;">09-历史(HISTORY)</a></li>
										<li><a href="javascript:;">10-文学(LITERATURE & FICTION)</a></li>
										<li><a href="javascript:;">11-大众传播(MASS COMMUNICATION)</a></li>
										<li><a href="javascript:;">12-军事(MILITARY)</a></li>
										<li><a href="javascript:;">13-哲学(PHILOSOPHY)</a></li>
										<li><a href="javascript:;">14-诗歌(POETRY)</a></li>
										<li><a href="javascript:;">15-政治(POLITICAL SCIENCE)</a></li>
										<li><a href="javascript:;">16-指南(REFERENCE)</a></li>
										<li><a href="javascript:;">17-宗教(RELIGION)</a></li>
										<li><a href="javascript:;">18-社科(SOCIAL SCIENCE)</a></li>
										<li><a href="javascript:;">19-旅游(TRAVEL)</a></li>
										<li><a href="javascript:;">99-其他(OTHERS)</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_category2" onblur="limitLength(this, 50)" placeholder="填写或请选择">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon" data-toggle="dropdown">
										建议分类 3 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">01-艺术(ART)</a></li>
										<li><a href="javascript:;">02-传记/自传(BIOGRAPHY & AUTOBIOGRAPHY)</a></li>
										<li><a href="javascript:;">03-经济(BUSINESS & ECONOMICS)</a></li>
										<li><a href="javascript:;">04-卡通/绘本(CARTOONS)</a></li>
										<li><a href="javascript:;">05-童书(CHILDREN’S BOOKS)</a></li>
										<li><a href="javascript:;">06-美食(COOKING)</a></li>
										<li><a href="javascript:;">07-文化(CULTURE)</a></li>
										<li><a href="javascript:;">08-休闲养生(HEALTH & FITNESS)</a></li>
										<li><a href="javascript:;">09-历史(HISTORY)</a></li>
										<li><a href="javascript:;">10-文学(LITERATURE & FICTION)</a></li>
										<li><a href="javascript:;">11-大众传播(MASS COMMUNICATION)</a></li>
										<li><a href="javascript:;">12-军事(MILITARY)</a></li>
										<li><a href="javascript:;">13-哲学(PHILOSOPHY)</a></li>
										<li><a href="javascript:;">14-诗歌(POETRY)</a></li>
										<li><a href="javascript:;">15-政治(POLITICAL SCIENCE)</a></li>
										<li><a href="javascript:;">16-指南(REFERENCE)</a></li>
										<li><a href="javascript:;">17-宗教(RELIGION)</a></li>
										<li><a href="javascript:;">18-社科(SOCIAL SCIENCE)</a></li>
										<li><a href="javascript:;">19-旅游(TRAVEL)</a></li>
										<li><a href="javascript:;">99-其他(OTHERS)</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_category3" onblur="limitLength(this, 50)" placeholder="填写或请选择">
							</div>
						</div>
					</div>

					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon" data-toggle="dropdown">
										<span class="bitian">版次 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">1</a></li>
										<li><a href="javascript:;">2</a></li>
										<li><a href="javascript:;">3</a></li>
										<li><a href="javascript:;">4</a></li>
										<li><a href="javascript:;">5</a></li>
										<li><a href="javascript:;">6</a></li>
										<li><a href="javascript:;">7</a></li>
										<li><a href="javascript:;">8</a></li>
										<li><a href="javascript:;">9</a></li>
										<li><a href="javascript:;">10</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_publish_count" placeholder="请选择" value="1" readonly>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">版权字数</span></span>
								<input class="form-control" name="bookEntity.book_copyright_word_count" onkeyup="inFloat(this)" onblur="limitLength(this, 8)" type="text">
								<span class="input-group-addon"><span class="bitian">千字</span></span>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group date form_date" data-date="" data-date-format="yyyy-MM-dd">
								<input class="form-control" size="16" name="bookEntity.book_copyright_expires" placeholder="版权到期时间" type="text" value="" readonly> <span class="input-group-addon">
									<span class="bitian"><span class="glyphicon glyphicon-remove"></span></span>
								</span> <span class="input-group-addon"> <span class="bitian"><span class="glyphicon glyphicon-calendar"></span></span>
								</span>
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon" data-toggle="dropdown">
										<span class="bitian">印次 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">1</a></li>
										<li><a href="javascript:;">2</a></li>
										<li><a href="javascript:;">3</a></li>
										<li><a href="javascript:;">4</a></li>
										<li><a href="javascript:;">5</a></li>
										<li><a href="javascript:;">6</a></li>
										<li><a href="javascript:;">7</a></li>
										<li><a href="javascript:;">8</a></li>
										<li><a href="javascript:;">9</a></li>
										<li><a href="javascript:;">10</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_print_count" placeholder="请选择" value="1" readonly>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										介质 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">CD</a></li>
										<li><a href="javascript:;">MP3</a></li>
										<li><a href="javascript:;">AT</a></li>
										<li><a href="javascript:;">DVD</a></li>
										<li><a href="javascript:;">CDROM</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_disk_type" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel2" data-toggle="dropdown">
										光盘配送方式 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">随书赠送</a></li>
										<li><a href="javascript:;">单独销售</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_disk_send" placeholder="请选择" readonly>
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">用纸尺寸 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">A-(787mm×1092mm)</a></li>
										<li><a href="javascript:;">B-(889mm×1194mm)</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_size" onblur="limitLength(this, 30)" placeholder="选择或输入">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">开本尺寸 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">A-4K(530mm×375mm)</a></li>
										<li><a href="javascript:;">A-8K(375mm×260mm)</a></li>
										<li><a href="javascript:;">A-12K(260mm×250mm)</a></li>
										<li><a href="javascript:;">A-16K(260mm×185mm)</a></li>
										<li><a href="javascript:;">A-20K(260mm×140mm)</a></li>
										<li><a href="javascript:;">A-24K(185mm×170mm)</a></li>
										<li><a href="javascript:;">A-32K(185mm×130mm)</a></li>
										<li><a href="javascript:;">A-48K(180mm×90mm)</a></li>
										<li><a href="javascript:;">A-64K(120mm×80mm)</a></li>
										<li><a href="javascript:;">B-4K(580mm×430mm)</a></li>
										<li><a href="javascript:;">B-8K(430mm×285mm)</a></li>
										<li><a href="javascript:;">B-12K(285mm×280mm)</a></li>
										<li><a href="javascript:;">B-16K(285mm×210mm)</a></li>
										<li><a href="javascript:;">B-20K(270mm×160mm)</a></li>
										<li><a href="javascript:;">B-24K(210mm×185mm)</a></li>
										<li><a href="javascript:;">B-32K(210mm×140mm)</a></li>
										<li><a href="javascript:;">B-48K(185mm×100mm)</a></li>
										<li><a href="javascript:;">B-64K(130mm×100mm)</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_kaiben" onblur="limitLength(this, 30)" placeholder="选择或输入">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">书脊厚度</span></span>
								<input class="form-control" name="bookEntity.book_heigh" onkeyup="inFloat(this)" onblur="limitLength(this, 10)" type="text">
								<span class="input-group-addon"><span class="bitian">mm</span></span>
							</div>
						</div>
					</div>

					<div class="row pt">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">插图数量</span>
								<input class="form-control" name="bookEntity.book_image_count" onkeyup="keyUp(this)" onblur="limitLength(this, 8)" type="text">
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon" data-toggle="dropdown">
										插图分布 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">文间插图</a></li>
										<li><a href="javascript:;">单页插图</a></li>
										<li><a href="javascript:;">集合插图</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_image_spread" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon">插表数量</span>
								<input class="form-control" name="bookEntity.book_table_count" onblur="limitLength(this, 8)" onkeyup="keyUp(this)" type="text">
							</div>
						</div>
					</div>
					<div class="row pt zzs">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon" title="人民币"><span class="bitian">纸质书定价</span></span>
								<input class="form-control" name="bookEntity.book_paper_price" onkeyup="inFloat(this)" onblur="limitLength(this, 8)" type="text">
								<span class="input-group-addon"><span class="bitian">RMB</span></span>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-4 zzs">
							<div class="input-group">
								<span class="input-group-addon" title="美元">纸质书定价</span>
								<input class="form-control" name="bookEntity.book_paper_dollar_price" onkeyup="inFloat(this)" onblur="limitLength(this, 8)" type="text">
								<span class="input-group-addon">USD</span>
							</div>
						</div>
						<div class="col-sm-4 dzs">
							<div class="input-group">
								<span class="input-group-addon" title="人民币"><span class="zbitian">电子书定价</span></span>
								<input class="form-control" name="bookEntity.book_ebook_price" onkeyup="inFloat(this)" onblur="limitLength(this, 8)" type="text">
								<span class="input-group-addon"><span class="zbitian">RMB</span></span>
							</div>
						</div>
						<div class="col-sm-4 dzs">
							<div class="input-group">
								<span class="input-group-addon" title="美元"><span class="zbitian wbitian">电子书定价</span></span>
								<input class="form-control" name="bookEntity.book_ebook_dollar_price" onkeyup="inFloat(this)" onblur="limitLength(this, 8)" type="text">
								<span class="input-group-addon"><span class="zbitian wbitian">USD</span></span>
							</div>
						</div>
					</div>
				</div>
				<div class="tab-pane fade" id="tuijie">
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group zw">
								<span class="input-group-addon"><span class="bitian">中文关键词（非书名及内容简介内的文字）</span></span>
								<input type="text" class="form-control formch tags" id="book_keyword_cn" name="bookEntity.book_keyword_cn" />
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group yw has-success">
								<span class="input-group-addon"><span class="bitian">英文关键词（非书名及内容简介内的文字）</span></span>
								<input type="text" class="form-control formen tags" id="book_keyword_english" name="bookEntity.book_keyword_english" />
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group ww has-warning">
								<span class="input-group-addon"><span class="bitian">外文关键词（非书名及内容简介内的文字）</span></span>
								<input type="text" class="form-control formfo tags" id="book_keyword_foreign" name="bookEntity.book_keyword_foreign" />
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group zw">
								<span class="input-group-addon"><span class="bitian zbitian wbitian">内容简介（中文）</span></span>
								<textarea class="form-control formch" name="bookEntity.book_content_intr_cn" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group yw has-success" title="内容简介（英文）">
								<span class="input-group-addon"><span class="bitian zbitian">内容简介（英文）</span></span>
								<textarea class="form-control formen" name="bookEntity.book_content_intr_english" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group ww has-warning" title="内容简介（外文）">
								<span class="input-group-addon"><span class="bitian">内容简介（外文）</span></span>
								<textarea class="form-control formfo" name="bookEntity.book_content_intr_foreign" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group zw" title="作者简介（中文）">
								<span class="input-group-addon"><span class="bitian zbitian wbitian">作者简介（中文）</span></span>
								<textarea class="form-control formch" name="bookEntity.book_author_intr_cn" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group yw has-success">
								<span class="input-group-addon"><span class="bitian zbitian">作者简介（英文）</span></span>
								<textarea class="form-control formen" name="bookEntity.book_author_intr_english" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group ww has-warning">
								<span class="input-group-addon"><span class="bitian">作者简介（外文）</span></span>
								<textarea class="form-control formfo" name="bookEntity.book_author_intr_foreign" rows=4></textarea>
							</div>
						</div>
					</div>
					
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group zw">
								<span class="input-group-addon"><span class="bitian zbitian wbitian">编辑推荐（中文）</span></span>
								<textarea class="form-control formch" name="bookEntity.book_editor_recommend_cn" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group yw has-success">
								<span class="input-group-addon"><span class="bitian zbitian">编辑推荐（英文）</span></span>
								<textarea class="form-control formen" name="bookEntity.book_editor_recommend_english" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group ww has-warning">
								<span class="input-group-addon"><span class="bitian">编辑推荐（外文）</span></span>
								<textarea class="form-control formfo" name="bookEntity.book_editor_recommend_foreign" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon">法文关键词（非书名及内容简介内的文字）</span>
								<input type="text" class="form-control tags" id="book_keyword_fa" name="bookEntity.book_keyword_fa" />
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon">西文关键词（非书名及内容简介内的文字）</span>
								<input type="text" class="form-control tags" id="book_keyword_xi" name="bookEntity.book_keyword_xi" />
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon">阿文关键词（非书名及内容简介内的文字）</span>
								<input type="text" class="form-control tags" id="book_keyword_e" name="bookEntity.book_keyword_e" />
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group" title="内容简介（法文）">
								<span class="input-group-addon"><span class="zbitian">内容简介（法文）</span></span>
								<textarea class="form-control" name="bookEntity.book_content_intr_fa" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group" title="内容简介（西文）">
								<span class="input-group-addon"><span class="zbitian">内容简介（西文）</span></span>
								<textarea class="form-control" name="bookEntity.book_content_intr_xi" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group" title="内容简介（阿文）">
								<span class="input-group-addon"><span class="zbitian">内容简介（阿文）</span></span>
								<textarea class="form-control" name="bookEntity.book_content_intr_e" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">作者简介（法文）</span></span>
								<textarea class="form-control" name="bookEntity.book_author_intr_fa" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">作者简介（西文）</span></span>
								<textarea class="form-control" name="bookEntity.book_author_intr_xi" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">作者简介（阿文）</span></span>
								<textarea class="form-control" name="bookEntity.book_author_intr_e" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">编辑推荐（法文）</span></span>
								<textarea class="form-control" name="bookEntity.book_editor_recommend_fa" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">编辑推荐（西文）</span></span>
								<textarea class="form-control" name="bookEntity.book_editor_recommend_xi" rows=4></textarea>
							</div>
						</div>
					</div>
					<div class="row pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon"><span class="zbitian">编辑推荐（阿文）</span></span>
								<textarea class="form-control" name="bookEntity.book_editor_recommend_e" rows=4></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="tab-pane fade" id="sheji">
					<div class="row zzs pt">
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">封面-用纸</span></span>
								<input class="form-control" name="bookEntity.book_cover_paper" type="text" onblur="limitLength(this, 45)" id="setValInput" placeholder="请点右侧选择" readonly>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<div class="" style="float:left">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
											<span id="parentBtnId1">用纸类型</span> <span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li><a href="javascript:jilian('铜版纸','200g/230g/250g/300g','book_cover_paper_ul','parentBtnId1','parentBtnId2','setValInput');">铜版纸</a></li>
											<li><a href="javascript:jilian('哑粉纸','200g/230g/250g/300g','book_cover_paper_ul','parentBtnId1','parentBtnId2','setValInput');">哑粉纸</a></li>
											<li><a href="javascript:jilian('特种纸','200g/210g/230g/250g','book_cover_paper_ul','parentBtnId1','parentBtnId2','setValInput');">特种纸</a></li>
										</ul>
									</div>
								</div>
								<div class="" style="float:left">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
											<span id="parentBtnId2">用纸克重</span> <span class="caret"></span>
										</button>
										<ul class="dropdown-menu" id="book_cover_paper_ul">
										</ul>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">封面-印刷颜色 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">单色</a></li>
										<li><a href="javascript:;">双色</a></li>
										<li><a href="javascript:;">四色</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_cover_publish_color" onblur="limitLength(this, 30)" placeholder="填写或请选择">
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<%-- <div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										插页-用纸 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">胶版纸(60,70,80,90,100,120g)</a></li>
										<li><a href="javascript:;">轻型纸(60,70,80,90,100,120g)</a></li>
										<li><a href="javascript:;">纯质纸(60,70,80,90,100,120g)</a></li>
										<li><a href="javascript:;">铜版纸(80,90,105,120,128,157g)</a></li>
										<li><a href="javascript:;">哑粉纸(80,90,105,120,128,157g)</a></li>
										<li><a href="javascript:;">特种纸(120,140,200,210,230,250g)</a></li>
										<li><a href="javascript:;">铜版纸(200,230,250,300g)</a></li>
										<li><a href="javascript:;">哑粉纸(200,230,250,300g)</a></li>
									</ul>
								</div>
								<input class="form-control" name="bookEntity.book_image_paper" type="text" onblur="limitLength(this, 45)" placeholder="填写或请选择">
							</div>
						</div> --%>
						
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">插页-用纸</span></span>
								<input class="form-control" name="bookEntity.book_image_paper" type="text" onblur="limitLength(this, 45)" id="setImageValInput" placeholder="请点右侧选择" readonly>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<div class="" style="float:left">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
											<span id="imageBtnId1">用纸类型</span> <span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li><a href="javascript:jilian('胶版纸','60g/70g/80g/90g/100g/120g','book_image_paper_ul','imageBtnId1','imageBtnId2','setImageValInput');">胶版纸</a></li>
											<li><a href="javascript:jilian('轻型纸','60g/70g/80g/90g/100g/120g','book_image_paper_ul','imageBtnId1','imageBtnId2','setImageValInput');">轻型纸</a></li>
											<li><a href="javascript:jilian('纯质纸','60g/70g/80g/90g/100g/120g','book_image_paper_ul','imageBtnId1','imageBtnId2','setImageValInput');">纯质纸</a></li>
											<li><a href="javascript:jilian('铜版纸','80g/90g/105g/120g/128g/157g/200g/230g/250g/300g','book_image_paper_ul','imageBtnId1','imageBtnId2','setImageValInput');">铜版纸</a></li>
											<li><a href="javascript:jilian('哑粉纸','80g/90g/105g/120g/128g/157g/200g/230g/250g/300g','book_image_paper_ul','imageBtnId1','imageBtnId2','setImageValInput');">哑粉纸</a></li>
											<li><a href="javascript:jilian('特种纸','120g/140g/200g/210g/230g/250g','book_image_paper_ul','imageBtnId1','imageBtnId2','setImageValInput');">特种纸</a></li>
										</ul>
									</div>
								</div>
								<div class="" style="float:left">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
											<span id="imageBtnId2">用纸克重</span> <span class="caret"></span>
										</button>
										<ul class="dropdown-menu" id="book_image_paper_ul">
										</ul>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">插页-印刷颜色 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">单色</a></li>
										<li><a href="javascript:;">双色</a></li>
										<li><a href="javascript:;">四色</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_image_publish_color" onblur="limitLength(this, 30)" placeholder="填写或请选择">
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">内文-用纸</span></span>
								<input class="form-control" name="bookEntity.book_neiwen_paper" type="text" onblur="limitLength(this, 45)" id="setneiwenValInput" placeholder="请点右侧选择" readonly>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<div class="" style="float:left">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
											<span id="neiwenBtnId1">用纸类型</span> <span class="caret"></span>
										</button>
										<ul class="dropdown-menu">
											<li><a href="javascript:jilian('胶版纸','60g/70g/80g/90g/100g/120g','book_neiwen_paper_ul','neiwenBtnId1','neiwenBtnId2','setneiwenValInput');">胶版纸</a></li>
											<li><a href="javascript:jilian('轻型纸','60g/70g/80g/90g/100g/120g','book_neiwen_paper_ul','neiwenBtnId1','neiwenBtnId2','setneiwenValInput');">轻型纸</a></li>
											<li><a href="javascript:jilian('纯质纸','60g/70g/80g/90g/100g/120g','book_neiwen_paper_ul','neiwenBtnId1','neiwenBtnId2','setneiwenValInput');">纯质纸</a></li>
											<li><a href="javascript:jilian('铜版纸','80g/90g/105g/120g/128g/157g','book_neiwen_paper_ul','neiwenBtnId1','neiwenBtnId2','setneiwenValInput');">铜版纸</a></li>
											<li><a href="javascript:jilian('哑粉纸','80g/90g/105g/120g/128g/157g','book_neiwen_paper_ul','neiwenBtnId1','neiwenBtnId2','setneiwenValInput');">哑粉纸</a></li>
											<li><a href="javascript:jilian('特种纸','120g/140g/200g/210g/230g/250g','book_neiwen_paper_ul','neiwenBtnId1','neiwenBtnId2','setneiwenValInput');">特种纸</a></li>
										</ul>
									</div>
								</div>
								<div class="" style="float:left">
									<div class="input-group-btn">
										<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
											<span id="neiwenBtnId2">用纸克重</span> <span class="caret"></span>
										</button>
										<ul class="dropdown-menu" id="book_neiwen_paper_ul">
										</ul>
									</div>
								</div>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">内文-印刷颜色 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">单色</a></li>
										<li><a href="javascript:;">双色</a></li>
										<li><a href="javascript:;">四色</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_neiwen_publish_color" onblur="limitLength(this, 30)" placeholder="填写或请选择">
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-6">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon" data-toggle="dropdown">
										<span class="bitian">装帧-类型 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">平装</a></li>
										<li><a href="javascript:;">准精装</a></li>
										<li><a href="javascript:;">精装</a></li>
										<li><a href="javascript:;">卷轴装</a></li>
										<li><a href="javascript:;">经折装</a></li>
										<li><a href="javascript:;">旋风装</a></li>
										<li><a href="javascript:;">蝴蝶装</a></li>
										<li><a href="javascript:;">包背装</a></li>
										<li><a href="javascript:;">线装</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_zhuangzhen_class" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-6">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon" data-toggle="dropdown">
										<span class="bitian">装帧-方式 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">锁线胶订</a></li>
										<li><a href="javascript:;">胶订（无线） </a></li>
										<li><a href="javascript:;">骑马订 </a></li>
										<li><a href="javascript:;">其他 </a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_zhuangzhen_type" placeholder="请选择" readonly>
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-3">
							<div class="input-group">
								<div class="input-group-btn has-success">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										后工艺-覆膜 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">无膜</a></li>
										<li><a href="javascript:;">亮膜</a></li>
										<li><a href="javascript:;">亚膜</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_end_fumo" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										后工艺- UV <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">有</a></li>
										<li><a href="javascript:;">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_end_uv" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon">后工艺-勒口</span>
								<input class="form-control" name="bookEntity.book_end_lekou" type="text"  onkeyup="inFloat(this)" onblur="limitLength(this, 8)" />
								<span class="input-group-addon">mm</span>
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										后工艺-封装 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">无封装</a></li>
										<li><a href="javascript:;">塑封</a></li>
										<li><a href="javascript:;">函套</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_end_sufeng" placeholder="请选择" readonly>
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon">后工艺-其他 </span>
								<input class="form-control" name="bookEntity.book_end_other" onblur="limitLength(this, 150)" type="text" />
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">内文页数</span></span>
								<input class="form-control" name="bookEntity.book_neiwen_page_count" onkeyup="keyUp(this)" onblur="onBlur2(this);limitLength(this, 6)" type="text" id="nwys" />
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">插页页数</span></span>
								<input class="form-control" name="bookEntity.book_image_page_count" onkeyup="keyUp(this)" onblur="onBlur2(this);limitLength(this, 6)" type="text" id="cyys" />
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">总印张数</span></span>
								<input class="form-control" name="bookEntity.book_publish_page_count" onblur="limitLength(this, 6)" type="text" />
							</div>
						</div>
						<div class="col-sm-3">
							<div class="input-group">
								<span class="input-group-addon">成书克重</span>
								<input class="form-control" name="bookEntity.book_weight" onkeyup="inFloat(this)" onblur="limitLength(this, 8)" type="text" />
								<span class="input-group-addon">克/本</span>
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-6">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">封面设计公司</span></span>
								<input class="form-control" name="bookEntity.book_design_cover_company" onblur="limitLength(this, 100)" type="text" />
							</div>
						</div>
						<div class="col-sm-6">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">版式制作公司</span></span>
								<input class="form-control" name="bookEntity.book_design_style_company" onblur="limitLength(this, 100)" type="text" />
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon">印装说明</span>
								<textarea class="form-control" name="bookEntity.book_publish_explain" rows=6></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="tab-pane fade" id="yinzhi">
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">新闻办印数</span></span>
								<input class="form-control" name="bookEntity.book_news_publish_count" type="text" onkeyup="keyUp(this)" onblur="onBlur(this)" value="0" id="xwbys" />
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">发行印数</span></span>
								<input class="form-control" name="bookEntity.book_news_distribute_count" type="text" onkeyup="keyUp(this)" onblur="onBlur(this)" value="0" id="fxys" />
							</div>
						</div>
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">总印数</span></span>
								<input class="form-control" name="bookEntity.book_news_count" type="text" id="zys" readonly />
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<span class="input-group-addon"><span class="bitian">上缴样书册数</span></span>
								<input class="form-control" name="bookEntity.book_news_shangjiao_count" onkeyup="keyUp(this)" value="0" type="text" />
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-12">
							<div class="input-group">
								<span class="input-group-addon"> 其他送货地址、联系人 <br /> <br /> 及备注送货册数</span>
								<textarea class="form-control" name="bookEntity.book_send_info" rows=6></textarea>
							</div>
						</div>
					</div>
				</div>
				<div class="tab-pane fade" id="wendang">
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">排版软件及版本 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;">Indesign</a></li>
										<li><a href="javascript:;">PageMaker</a></li>
									</ul>
								</div>
								<input type="text" class="form-control" name="bookEntity.book_publish_soft" onblur="limitLength(this, 100)" placeholder="填写或请选择">
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">内文排版文件 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_paper_neiwen_style_file" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">内文--存储路径</span>
								<input class="form-control" id="neiwen_serverpath" name="bookEntity.book_neiwen_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">封面版式文件 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_paper_cover_style_file" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">封面--存储路径</span> <input class="form-control" id="cover_serverpath" name="bookEntity.book_cover_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										内文使用字体 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_paper_font" placeholder="请选择" value="无" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">字体--存储路径</span> <input class="form-control" id="font_serverpath" name="bookEntity.book_font_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">分层PDF <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_paper_publish_pdf" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">PDF--存储路径</span> <input class="form-control" id="pdf_publish_serverpath" name="bookEntity.book_pdf_publish_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row zzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										原WORD文档 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_paper_word" placeholder="请选择" value="无" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">WORD存储路径</span> <input class="form-control" id="word_serverpath" name="bookEntity.book_word_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row dzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										XML ---- 文档 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_e_xml" placeholder="请选择" value="无" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">XML--存储路径</span> <input class="form-control" id="xml_serverpath" name="bookEntity.book_xml_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row dzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										EPUB -- 文件 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_e_epub" placeholder="请选择" value="无" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">EPUB存储路径</span> <input class="form-control" id="epub_serverpath" name="bookEntity.book_epub_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row dzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										MOBI -- 文件 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_e_mobi" placeholder="请选择" value="无" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">MOBI存储路径</span> <input class="form-control" id="mobi_serverpath" name="bookEntity.book_mobi_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row dzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										阅览PDF文件 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_e_pdf" placeholder="请选择" value="无" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">PDF--存储路径</span> <input class="form-control" id="pdf_read_serverpath" name="bookEntity.book_pdf_read_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row dzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										HTML -- 文档 <span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_e_html" placeholder="请选择" value="无" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">HTML存储路径</span> <input class="form-control" id="html_serverpath" name="bookEntity.book_html_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<div class="row dzs pt">
						<div class="col-sm-4">
							<div class="input-group">
								<div class="input-group-btn">
									<button type="button" class="btn btn-default dropdown-toggle input-group-myaddon peipanSel1" data-toggle="dropdown">
										<span class="bitian">图书合同归档 <span class="caret"></span></span>
									</button>
									<ul class="dropdown-menu">
										<li><a href="javascript:;" onclick="formatPath(this)">有</a></li>
										<li><a href="javascript:;" onclick="formatPath(this)">无</a></li>
									</ul>
								</div>
								<input type="text" class="form-control spath" name="bookEntity.book_contract" placeholder="请选择" readonly>
							</div>
						</div>
						<div class="col-sm-8">
							<div class="input-group">
								<span class="input-group-addon">合同存储路径</span> <input class="form-control" id="contract_serverpath" name="bookEntity.book_contract_serverpath" type="text" readonly />
							</div>
						</div>
					</div>
					<!-- <div class="row pt">
						<div class="col-sm-12">
							<button class="btn btn-info btn-block" type="button" onClick="downloadTaskXml()">下载任务列表</button>
						</div>
						<br /> <br />
					</div> -->
				</div>
				<div class="tab-pane fade" id="others">
					<div class="row">
							<div class="col-sm-12">
								<div class="input-group" title="备注">
									<span class="input-group-addon"><span class="">备注</span></span>
									<textarea class="form-control" name="bookEntity.book_mark" rows=4></textarea>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<script src="../js/jquery.tagsinput.min.js"></script>
</body>
</html>