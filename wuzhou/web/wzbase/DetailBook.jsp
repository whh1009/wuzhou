<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="Include.jsp"%>
<html lang="zh-cn">
<head>
<title>五洲传播--书目详细</title>
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
.yw{
	
}
.ww {
	
}
.input-group-addon,.input-group-myaddon {
	font-weight: bold;
}

.label {
	line-height: 1.5;
}
</style>
<script type="text/javascript">
    $(function() {
    	var result = "<s:property value="result" />";
    	if(result=='ok'){
    		alert("保存成功！");
    	}else if(result=='fail'){
    		alert("保存失败！");
    	}
    	initFormVal();
    	if($("#wenzhong").val()=="001--英文") {
    		initWenzhong('yw','001');
    	}
    	
    	$("input").each(function() {
    		$(this).prop("readonly", true);
    	});
    	$("button.dropdown-toggle").each(function() {
    		$(this).prop("disabled", "disabled");
    	});
    	$("textarea").each(function() {
    		$(this).prop("readonly", true);
    	});
        //标签页
        $("#myTab a").click(function (e) {
            e.preventDefault();
            $(this).tab("show");
        });
    });
    
  	//初始化表单的值
    function initFormVal() {
    	//var bookId = getURLPara("bookId");
    	var bookId = "<s:property value="theBookId" />";
    	$.ajax({
    		url:"wzbase/getBookEntityByBookId.action",
    		type : 'POST',
			data : {bookId:bookId},
			async: true,
			success : function(data) {
				if(data=="0") {
					document.getElementById("container").style.display='none';
					alert("服务器未获取到图书ID，请重新登录后再试");
				} else {
					var json = eval('(' + data + ')');
					for (name in json) {
						if (name=='book_keyword_cn'||name=='book_keyword_english'||name=='book_keyword_foreign'||name=="book_keyword_fa"||name=="book_keyword_xi"||name=="book_keyword_e") { //关键字特殊处理
							$("*[name='bookEntity." + name + "']").importTags(json[name]);
						} else {
							$("*[name='bookEntity." + name + "']").val(json[name]);
						}
					}
					//设置“数据文档--文件路径”
					$(".spath").each(function() {
						if($(this).val()=="有") {
							$(this).parent().parent().next(".col-sm-8").show();
						} else {
							$(this).parent().parent().next(".col-sm-8").hide();
						}
					});
				}
			}
    	});
    }
  	
    //获取url参数值,name 参数名
    function getURLPara(name) {
         var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
         var r = window.location.search.substr(1).match(reg);
         if(r!=null) {
        	 return  unescape(r[2]);
         } else {
        	 return "";
         }
    }
    
    function trim() {
    	

    }
    	
	//打印为docx
	function printDocx() {
		//var bId = getURLPara("bookId");
		var bId = "<s:property value="theBookId" />";
		var book_name_foreign = $("*[name='bookEntity.book_name_foreign']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_name_foreign=="") {
			alert("请填写 书名（外文）");
			$("*[name='bookEntity.book_name_foreign']").focus();
			return ;
		}
		var book_author_foreign = $("*[name='bookEntity.book_author_foreign']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_author_foreign=="") {
			alert("请填写 著者（外文）");
			$("*[name='bookEntity.book_author_foreign']").focus();
			return;
		}
		var book_translator_foreign = $("*[name='bookEntity.book_translator_foreign']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_translator_foreign=="") {
			alert("请填写 译者（外文）");
			$("*[name='bookEntity.book_translator_foreign']").focus();
			return;
		}
		var book_series_foreign = $("*[name='bookEntity.book_series_foreign']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_series_foreign=="") {
			alert("请填写 丛书名（外文）");
			$("*[name='bookEntity.book_series_foreign']").focus();
			return;
		}
		var book_cooperate_press = $("*[name='bookEntity.book_cooperate_press']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_cooperate_press=="") {
			alert("请填写 合作出版社");
			$("*[name='bookEntity.book_cooperate_press']").focus();
			return;
		}
		var book_keyword_foreign = $("*[name='bookEntity.book_keyword_foreign']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_keyword_foreign=="") {
			alert("请填写 外文关键词");
			$("*[name='bookEntity.book_keyword_foreign']").focus();
			return;
		}
		var book_content_intr_foreign = $("*[name='bookEntity.book_content_intr_foreign']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_content_intr_foreign=="") {
			alert("请填写 内容简介（外文）");
			$("*[name='bookEntity.book_content_intr_foreign']").focus();
			return;
		}
		var book_author_intr_foreign = $("*[name='bookEntity.book_author_intr_foreign']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_author_intr_foreign=="") {
			alert("请填写 作者简介（外文）");
			$("*[name='bookEntity.book_author_intr_foreign']").focus();
			return;
		}
		var book_editor_recommend_foreign = $("*[name='bookEntity.book_editor_recommend_foreign']").val().replace(/(^\s*)|(\s*$)/g, "");
		if(book_editor_recommend_foreign=="") {
			alert("请填写 编辑推荐（外文）");
			$("*[name='bookEntity.book_editor_recommend_foreign']").focus();
			return;
		}
		$.ajax({
			url:'createDocxByBookId.action',
			type:'post',
			async: false,
			data: {bookId: bId},
			success:function(data) {
				if(data=="-1") {
					alert("未获取到图书编号");
				} else if(data=="0")  {
					alert("未获取到图书信息");
				} else {
					window.location.href="downloadWord.action?wordDownloadName="+data;
				}
				
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.readyState==4&&XMLHttpRequest.status==200) {
					window.location.href="downloadWord.action?wordDownloadName="+data;
				} else {
					//alert("生成xml错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
					var ow = window.open("","newwindow");
					ow.document.write("生成WORD文档错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
					ow.document.close();
				}
			}
		});
	}
    </script>

</head>
<body>
	<div class="container" id="container">
		<form name="form" method="post">
			<input type="hidden" name="bookEntity.book_id" value="<s:property value="bookEntity.book_id" />">
			<input type="hidden" name="bookEntity.user_id" value="<s:property value='bookEntity.user_id'></s:property>" />
			<input type="hidden" name="bookEntity.book_flag" value="<s:property value='bookEntity.book_flag'></s:property>" />
			<input type="hidden" name="bookEntity.book_del_flag" value="<s:property value='bookEntity.book_del_flag'></s:property>" />
			<input type="hidden" name="bookEntity.book_type" id="book_type" value="<s:property value='bookEntity.book_type'></s:property>" />
			<div class="row">
				<div class="col-sm-4"></div>
				<div class="col-sm-4"></div>
	            <div class="col-sm-4">
	                <div class="btn-group pull-right">
					    <a class="btn btn-primary" href="javascript:printDocx();" title="word打印" name="printToDocx"><span class="glyphicon glyphicon-print"></span>&nbsp;打印</a>
	                </div>
	            </div>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<div class="input-group">
						<span class="input-group-addon">编号（ID）</span>
						<input class="form-control" id="bookId" name="bookEntity.book_serial_number" type="text"  value="<s:property value="bookEntity.book_serial_number" />" readonly />
					</div>
					<input type="hidden" id="bsn1" value="EP" />
					<input type="hidden" id="bsn2" value="" />
					<input type="hidden" id="bsn3" value="" />
					<input type="hidden" id="bsn4" value="" />
				</div>
			</div>
			<jsp:include page="BookBase.jsp"></jsp:include>
		</form>
	</div>
</body>
</html>