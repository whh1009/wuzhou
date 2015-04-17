<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
<title>五洲传播--版权到期</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<style>
th {
	text-align: center;
}
td {font-size:14px;}
.row {
	margin-top: 1em;
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
	var currentPage = 1;
	//总页数
	var pageCount = 1;
	//每页显示总记录数
	var pageRowCount = 15;
	//总记录数
	var rowCount = 1;
	$(function() {
		//初始化页码
    	initPageNum();
		getBookByBiaoji(0);
		initBiaoji();
	})
	
	function initBiaoji() {
		$("#biaoji").change(function() {
			currentPage = 1;
			pageCount = 1;
			pageRowCount = 15;
			rowCount = 1;
			var biaoji = $(this).val();
			getBookByBiaoji(biaoji);
		})
	}
	
	/**
     * 上一页
     */
    function prePage() {
    	currentPage = currentPage - 1;
    	var biaoji = $("#biaoji").val();
    	page(currentPage, biaoji);
    }

    /**
     * 下一页
     */
    function nextPage() {
    	currentPage = currentPage + 1;
    	var biaoji = $("#biaoji").val();
    	page(currentPage, biaoji);
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
    			currentPage=pageInt;
    			var biaoji = $("#biaoji").val();
    	    	page(currentPage, biaoji);
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
    	var biaoji = $("#biaoji").val();
    	page(1, biaoji);
    }


    /**
     * 根据页码查询
     */
    function page(currentPage, biaoji) {
    	$(".row table tbody").empty();
    	$(".row talbe thead").empty();
    	var titleTag = "";
    	var biaojiClass = "";
    	if(biaoji=="0") {
    		titleTag = "标记";
    		$(".row table thead").html("<tr><th>图书编号</th><th>书名</th><th>著者</th><th>责编</th><th>译者</th><th>版权到期时间</th><th>操作</th></tr>");
    		biaojiClass = "class='danger'";
    	} else {
    		titleTag = "";
    		$(".row table thead").html("<tr><th>图书编号</th><th>书名</th><th>著者</th><th>责编</th><th>译者</th><th>版权到期时间</th><th>状态</th></tr>");
    	}
    	$.ajax({
    		url:"getBookByBiaoji.action",
    		type:"post",
    		data:{currentPage:currentPage, biaoji:biaoji},
    		async: false,
    		success:function(data) {
    			if(data=="-1") {
    				document.getElementById("container").style.display='none';
    				alert("获取参数失败，请登录后重试");
    			} else {
	    			var json = eval("("+data+")");
	        		var tableStr = "";
	        		for(var i=0; i<json.bookList.length;i++) {
	        			tableStr = tableStr + "<tr "+biaojiClass+" >";
	        			tableStr = tableStr + "<td>"+json.bookList[i].book_serial_number+"</td><td>"+json.bookList[i].book_name_cn+"</td><td>"+json.bookList[i].book_author+"</td><td>"+json.bookList[i].book_editor+"</td><td>"+json.bookList[i].book_translator+"</td><td>"+json.bookList[i].book_copyright_expires+"</td>";
	        			if(biaoji=="0") {
	        				tableStr = tableStr + "<td style=\"text-align:center\"><a href=\"javascript:handleExpires('"+json.bookList[i].book_id+"');\" title=\""+titleTag+"\"><span class=\"glyphicon glyphicon-map-marker\"></span> &nbsp;标记</a></td>";
	        			} else {
	        				tableStr = tableStr + "<td style=\"text-align:center\"><span class=\"glyphicon glyphicon-ok-sign\"></span> &nbsp;已处理</td>";
	        			}
	        			tableStr = tableStr + "</tr>";
	    			}
	        		pageCount = json.pageEntity.pageCount;
	        		pageRowCount = json.pageEntity.pageRowCount;
	        		$("#currentPageSpan").html(currentPage);
	        		$("#pageCountSpan").html(pageCount);
	        		$("#pageRowCountSpan").html(pageRowCount);
	        		$(".row table tbody").html(tableStr);
    			}
    		}
    	});
    	initPageNum();
    }

	//根据是否标记得到图书
	function getBookByBiaoji(biaoji) {
		page(1, biaoji);
	}

	//处理过期图书
	function handleExpires(bookId) {
		if(confirm("确定标记为已处理？")) {
			$.ajax({
				url : 'handleCopyrightExpires.action',
				type : 'post',
				data : {
					bookId : bookId
				},
				success : function(data) {
					if (data == "1") {
						alert("标记成功");
						window.location.href = "copyrightExpires.action";
					} else {
						alert("修改失败，请重新登录后再试");
					}
				}
			});
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
</script>
</head>
<body>
	<div class="container" id="container">
		<div class="row">
			<div class="col-md-4">
				<select class="form-control" id="biaoji">
					<option value="0" selected>未标记</option>
					<option value="1">已标记</option>
				</select>
			</div>
		</div>
		<div class="row">
			<table class="table table-bordered table-hover">
				<thead>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="7">
							<div class="pull-left">当前第[<span id="currentPageSpan"></span>]页，共[<span id="pageCountSpan"></span>]页，每页显示[<span id="pageRowCountSpan"></span>]条</div>
							<div class="pull-right"><a href="javascript:prePage()" class="btn btn-default" type="button" id="prePageTag">&larr; 上一页 </a><a href="javascript:nextPage()"  class="btn btn-default" type="button" id="nextPageTag">下一页 &rarr;</a>&nbsp;&nbsp;跳至<input type="text" class="pageInput" />页<a href="javascript:jumpPage();" class="btn btn-default" type="button">转</a></div>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</body>
</html>