<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>五洲传播--新书元数据信息采集</title>
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
</style>
<!-- 
class样式说明
pt 出版类型，分为本社和和其它两种，本社和其它切换显示
zzs 纸质书属性，用于电子书和纸质书之间切换显示
dzs 电子书属性，用于切换显示
 -->
<script type="text/javascript">
	//获取当前用户
	var userName = "<s:property value='#session.userEntity.user_name'></s:property>";
	var fromflag = "<s:property value="addBook" />";
	$(function() {
		if(!fromflag){
			document.getElementById("container").style.display = 'none';
		}
		
		//初始化出版社类型，本社或者其他
		publishType();
		//初始化图书类别事件，纸书或电子书
		initBookCheckBox();
		//图书编号第四部分设置为随机数
		$("#bsn4").val(getRandom());
		$("input[name='bookEntity.book_isbn']").change(function() {
			$("#bsn2").val($(this).val());
			//设置图书编号
			$("#book_serial_number").val($("#bsn0").val() + "_" + $("#bsn1").val() + "_" + $("#bsn2").val() + "_" + $("#bsn3").val() + "_" + $("#bsn4").val());
			//初始化FTP服务器路径
			initServerPath();
		});
		//标签页
		$("#myTab a").click(function(e) {
			e.preventDefault();
			$(this).tab("show");
		});
	});
	
	function myLimit(arg) {
		var userId = "<s:property value='#session.userEntity.user_id'></s:property>";
		var isbn = $(arg).val().trim();
		if(isbn.indexOf("_")!=-1) {
			alert("不能输入下划线");
			$(arg).focus();
			return;
		}
		if(userId==7) {
			if ($(arg).val().replace(/(^\s*)|(\s*$)/g, "").length > 40) {
				alert("超出限制的范围，请修改");
				$(arg).focus();
				return;
			}
		} else {
			if (!arg.value.match(/^\d*?$/)) {
				alert("请输入数字");
				arg.focus();
				return;
			}
			if ($(arg).val().replace(/(^\s*)|(\s*$)/g, "").length > 13) {
				alert("超出限制的范围，请修改");
				$(arg).focus();
				return;
			}
		}
	}

	//保存
	function save() {
		var pt = $("input[name='publishType']:checked").val();//出版类型
		var book_serial_number = $("#book_serial_number").val().replace(/(^\s*)|(\s*$)/g, "");
		if (book_serial_number.length < 1) {
			alert(" 编号（ID）不可为空");
			$("#book_serial_number").focus();
			return;
		}
		//if (pt == "1") { //本社
			var wz = $("#wenzhong").val().replace(/(^\s*)|(\s*$)/g, "");
			var bookNameCn = $("#bookNameCN").val().replace(/(^\s*)|(\s*$)/g, "");
			var isbn = $("#book_isbn").val().replace(/(^\s*)|(\s*$)/g, "");
			if (bookNameCn.length < 1) {
				alert("请输入中文书名");
				$("#bookNameCN").focus();
			} else if (isbn.length < 1) {
				alert("请输入ISBN/ISSN");
				$("#book_isbn").focus();
			} else if (wz.length < 1) {
				alert("请选择文种");
				$("#wenzhong").focus();
			} else {
				var array = book_serial_number.split("_");
				if(array[0].length==0) {
					alert("请重新选择“纸质书”或“电子书”");
					return;
				}
				if(array[1].length==0) {
					alert("请重新输入ISBN");
					return;
				}
				if(array[2].length==0) {
					alert("请重新选择文种");
					return;
				}
				if(array[3].length==0) {
					alert("随机码没有生成，请重新登录再试");
					return;
				}
				if (window.confirm("确定保存吗？")) {
					document.form.action = "addBook.action";
					document.form.submit();
				}
			}
		//} else { //其他
		//	$.ajax({
		//		url:"addOtherBook.action",
		//		data:{bsn:book_serial_number},
		//		type : 'POST',
		//		success:function(data) {
		//			if(data=="-1") {
		//				alert("session已过期，请重新登录");
		//			} else if(data=="0") {
		//				alert("未获取到图书编号");
		//			} else if(data=="2") {
		//				alert("添加失败");
		//			} else {
		//				alert("添加成功");
		//				//window.location.href="../wzmybook/myBookList.action";
		//			}
		//		}
		//	});
		//}
	}

	//出版社类型，本社图书或者其他
	//属性pt用于标识本社或其他
	function publishType() {
		$("input[name='publishType']").click(function() {
			var pt = $(this).val();
			if(pt=="2") { //其他
				$("#bsn0").val("Q");
				//设置图书编号
				$("#book_serial_number").val($("#bsn0").val() + "_" + $("#bsn1").val() + "_" + $("#bsn2").val() + "_" + $("#bsn3").val() + "_" + $("#bsn4").val());
				$(".bitian").each(function() {
					$(this).css("color", "#555555");
				});
			} else {
				$("#bsn0").val("B");
				//设置图书编号
				$("#book_serial_number").val($("#bsn0").val() + "_" + $("#bsn1").val() + "_" + $("#bsn2").val() + "_" + $("#bsn3").val() + "_" + $("#bsn4").val());
				//选择本社，修改“必填”颜色
				$(".bitian").each(function() {
					$(this).css("color", "#CC0033");
				});
				if($(".bilingual").val().indexOf("500")!=-1) { //双语对应
					$(".sydybt").css("color","#CC0033");
				} else {
					$(".sydybt").css("color","#555555");
				}
			}
		});
		/*
		不用
		$("input[name='publishType']").click(function() {
			var pt = $(this).val();
			if (pt == "2") {
				$(".pt").each(function() {
					$(this).hide();
				});
				$("#book_serial_number").prop("readonly", false);
				$("#refBtn").prop("disabled", true);
			} else {
				$(".pt").each(function() {
					$(this).show();
				});
				$("#book_serial_number").prop("readonly", true);
				$("#refBtn").prop("disabled", false);
			}
		});
		*/
	}

	//根据填充的编号获取isbn号
	//若添加的是纸书则获取电子书的信息，反之亦然
	function refByISBN() {
		var bookType = $("input[name='bookEntity.book_type']").val();//图书类型
		//var book_serial_number = $("#book_serial_number").val(); //图书编号
		//book_serial_number = book_serial_number.split('_')[1];
		var bookIsbn = $("#book_isbn").val().replace(/(^\s*)|(\s*$)/g, "");
		if (bookIsbn.length > 0) {
			if (confirm("部分数据将会被覆盖，确定吗？")) {
				$.ajax({
					url : 'refByISBN.action',
					type : 'POST',
					data : {
						bookType : bookType,
						bookIsbn : bookIsbn
					},
					success : function(data) {
						if (data == '0') {
							alert("没有找到相关联的书目信息");
						} else if (data == '-1') {
							alert("获取图书类型和图书编号失败");
						} else {
							var json = eval('(' + data + ')');
							for (name in json) {
								if (name == 'book_keyword_cn' || name == 'book_keyword_english' || name == 'book_keyword_foreign') { //关键字特殊处理
									$("*[name='bookEntity." + name + "']").importTags(json[name]);
								} else {
									if (name == "book_paper_price" || name == "book_ebook_price"||name=="book_paper_dollar_price"||name=="book_ebook_dollar_price") {
										$("*[name='bookEntity." + name + "']").val(json[name].toFixed(2));
									} else {
										$("*[name='bookEntity." + name + "']").val(json[name]);
									}
								}
							}
							var book_sn = $("#book_serial_number").val();
							$("#book_serial_number").val(book_sn.substring(0,book_sn.lastIndexOf("_"))+"_"+$("#bsn4").val());
							$("input[name$='_serverpath']").each(function() {
								var path = $(this).val();
								if(path.substring(0,1)=="/") {
									path = path.substring(1);
									path = "/"+userName+path.substring(path.indexOf("/"));
									$(this).val(path);
								}
							});
							//设置“数据文档--文件路径”
							$(".spath").each(function() {
								if($(this).val()=="有") {
									$(this).parent().parent().next(".col-sm-8").show();
								} else {
									$(this).parent().parent().next(".col-sm-8").hide();
								}
							});
							//初始化文种“英文”
							if($("*[name='bookEntity.book_language']").val()=="001--英文") {
								$(".ww").each(function() {
									$(this).children("input").val("");
									$(this).hide();//隐藏
								});
							}
							
							$("#bsn2").val(json.book_isbn);
							$("#bsn3").val(json.book_language.substring(0,3));
							//设置为当前用户的ID
							$("input[name='bookEntity.user_id']").val("<s:property value='#session.userEntity.user_id'></s:property>");
						}
					}
				});
			}
		} else {
			alert("请输入ISBN/ISSN号");
			$("#book_isbn").focus();
		}
	}

	//初始化图书类别事件,判断选中的是电子书还是纸书
	function initBookCheckBox() {
		$(".booktype").click(function() {
			var flag = 0;
			$(".booktype").each(function() {
				if ($(this).is(":checked")) {
					flag = flag + Number($(this).val());
				}
			});
			if (flag == 0) { //纸书和电子书都没有选中
				alert("纸质书和电子书必须选中一个！");
				//$(this).prop("checked",true);
				$(".booktype").each(function() {
					$(this).prop("checked", true);
				});
				flag = 3;
				$(".zzs").each(function() {
					$(this).show();
				});
				$(".dzs").each(function() {
					$(this).show();
				});
				$("#bsn1").val("EP"); //拼接图书编号,表示纸书和电子书
			} else if (flag == 1) { //纸书
				$(".zzs").each(function() {
					$(this).show();
				});
				$(".dzs").each(function() {
					$(this).hide();
				});
				$("#bsn1").val("P"); //拼接图书编号,表示纸书
			} else if (flag == 2) { //电子书
				$(".dzs").each(function() {
					$(this).show();
				});
				$(".zzs").each(function() {
					$(this).hide();
				});
				$("#bsn1").val("E"); //拼接图书编号,表示纸书
			} else {
				$(".zzs").each(function() {
					$(this).show();
				});
				$(".dzs").each(function() {
					$(this).show();
				});
				$("#bsn1").val("EP"); //拼接图书编号,表示纸书和电子书
			}
			//设置图书类型
			$("input[name='bookEntity.book_type']").each(function() {
				$(this).val(flag);
			});
			//设置图书编号
			$("#book_serial_number").val($("#bsn0").val() + "_" + $("#bsn1").val() + "_" + $("#bsn2").val() + "_" + $("#bsn3").val() + "_" + $("#bsn4").val());
			//初始化FTP服务器路径
			initServerPath();
		});
	}

	//从0-9，A-Z中获取5未随机数
	function getRandom() {
		var data = [ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
				"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
				"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
				"z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
				"L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
				"X", "Y", "Z" ];
		var result = "";
		for ( var i = 0; i < 5; i++) {
			var r = Math.floor(Math.random() * 62);
			result += data[r];
		}
		return result;
	}

	//初始化FTP服务器路径
	function initServerPath() {
		//$("#book_serial_number").change(function() {
		var bookName = $("#bookNameCN").val();
		if (bookName.length > 0) {
			bookName = "_" + bookName;
		}
		//var bsn=$("#book_serial_number").val();
		//var bsn1=$("#bsn1").val();
		var bsn = $("#book_serial_number").val();
		$("#neiwen_serverpath").val("/" + userName + "/" + bsn + "/排版");//内文存储路径
		$("#cover_serverpath").val("/" + userName + "/" + bsn + "/封面");//封面存储路径
		$("#font_serverpath").val("/" + userName + "/" + bsn + "/字体");//字体存储路径
		$("#pdf_publish_serverpath").val("/" + userName + "/" + bsn + "/分层PDF");//分层PDF存储路径
		$("#word_serverpath").val("/" + userName + "/" + bsn + "/WORD");
		$("#xml_serverpath").val("/" + userName + "/" + bsn + "/XML");//
		$("#epub_serverpath").val("/" + userName + "/" + bsn + "/EPUB");//
		$("#mobi_serverpath").val("/" + userName + "/" + bsn + "/MOBI");//
		$("#pdf_read_serverpath").val("/" + userName + "/" + bsn + "/阅读PDF");//
		$("#html_serverpath").val("/" + userName + "/" + bsn + "/HTML");//
		$("#contract_serverpath").val("/"+userName+"/"+bsn+"/合同");
		//});
	}
	
</script>

</head>
<body>
	<div class="container" id="container">
		<form name="form" method="post"  target='submitframe'>
			<input type="hidden" name="bookEntity.user_id" value="<s:property value='#session.userEntity.user_id'></s:property>" />
			<input type="hidden" name="bookEntity.book_flag" value=0 />
			<input type="hidden" name="bookEntity.book_del_flag" value=0 />
			<div class="row">
				<div class="col-sm-12">
					<div class="btn-group pull-right">
						<!-- <button class="btn btn-primary" onclick="add()" disabled><span class="glyphicon glyphicon-plus-sign"></span>&nbsp;新增</button>&nbsp;&nbsp; -->
						<a class="btn btn-primary" href="javascript:save();"><span class="glyphicon glyphicon-saved"></span>&nbsp;保存数据</a>
						<!-- <a class="btn btn-primary" href="javascript:printDocx();" title=""><span class="glyphicon glyphicon-print"></span>&nbsp;后台打印</a> -->
						<!-- <button class="btn btn-primary" disabled><span class="glyphicon glyphicon-list"></span>&nbsp;浏览</button> -->
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<div class="">
						<label class="radio-inline"> <input type="radio" name="publishType" value="1" checked="checked"> 本社
						</label> <label class="radio-inline"> <input type="radio" name="publishType" value="2"> 其他
						</label>
					</div>
				</div>
				<div class="col-sm-8">
					<div class="col-sm-4 pt">
						<label class="checkbox-inline"> <input type="checkbox" class="booktype" value="1" checked="checked"> 纸质书
						</label> <label class="checkbox-inline"> <input type="checkbox" class="booktype" value="2" checked="checked"> 电子书
						</label> <input type="hidden" name="bookEntity.book_type" id="book_type" value="3" />
					</div>
					<div class="col-sm-8">
						<div class="input-group">
							<span class="input-group-addon">编号（ID）</span> <input class="form-control" id="book_serial_number" name="bookEntity.book_serial_number" type="text" value="" readonly />
							<div class="input-group-btn">
								<button type="button" class="btn btn-default" id="refBtn" onclick="refByISBN()" title="引用相同ISBN号的书目信息，格式如下：纸质书/电子书_ISBN_文种_5位随机数">
									&nbsp;<span class="glyphicon glyphicon-transfer"></span>
								</button>
							</div>
						</div>
						<input type="hidden" id="bsn0" value="B" />
						<input type="hidden" id="bsn1" value="EP" />
						<input type="hidden" id="bsn2" value="" /><!-- isbn -->
						<input type="hidden" id="bsn3" value="" /><!-- 文种 -->
						<input type="hidden" id="bsn4" value="" /><!-- 随机码 -->
					</div>
				</div>
			</div>
			<jsp:include page="../wzbase/BookBase.jsp"></jsp:include>
		</form>
	</div>
	
	<script src="../js/bootstrap-datetimepicker.min.js"></script>
	<script src="../js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript">
		$(function() {
			$(".form_date").datetimepicker({
				language : "zh-CN",
				weekStart : 1,
				todayBtn : 1,
				autoclose : 1,
				todayHighlight : 1,
				startView : 2,
				minView : 2,
				forceParse : 0,
				pickerPosition : "bottom-left"
			});
		});
	</script>
	
	<iframe name='submitframe' id='submitframe' style="display:none" />
</body>
</html>