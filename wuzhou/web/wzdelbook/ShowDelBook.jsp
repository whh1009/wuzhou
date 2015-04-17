<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<%
	System.out.println(request.getParameter("showColumn"));
%>
<!DOCTYPE HTML>
<html>
<head>
<title>五洲传播--已删除图书查询</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<style>
input {
	margin-bottom: 1em;
}

#myModal .modal-dialog {
	width: 60%;
}

.pageInput {
	border-radius: 4px;
	border: 1px solid #ccc;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
	box-shadow: inset 0px 1px 1px rgba(0, 0, 0, 0.075);
	-webkit-transition: border-color ease-in-out .15s, box-shadow
		ease-in-out .15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
	padding: 0 10;
	color: #555;
	height: 32px;
	width: 3em;
}

th {
	text-align: center
}
</style>
<script type="text/javascript">
	//已删除图书list
	var delBookList;
	//当前页
	var currentPage = 1;
	//总页数
	var pageCount = 1;
	//每页显示记录数（默认）
	var pageRowCount = 15;
	//总记录数
	var rowCount = 1;

	var column = "";
	$(function() {
		column = "<s:property value="showColumn" />";
		if(column){
			column = column.replace(/&lt;/ig, "<").replace(/&gt;/ig, ">");
			//初始化显示字段
			intShowColumn();
	
			getDelBookList();
			initPageNum();
			//默认显示第一页
			showContentByPage(1);
		}else{
			document.getElementById("container").style.display = 'none';
		}

	})

	//初始化显示的表头字段
	function intShowColumn() {
		try {
			var count = 0;
			var tHead = "<tr>";
			$(column).find("item").each(function() {
				tHead = tHead + "<th>" + $(this).attr("cname") + "</th>";
				count++;
			});
			tHead = tHead + "<th>操作</th></tr>";
			$(".row table thead").html(tHead);
			$("#colspanAttr").prop("colspan", count+1);
		} catch (e) {
			alert(e.message);
		}
	}

	/**
	 * 获取图书信息
	 */
	function getDelBookList() {
		$.ajax({
			url : 'getDelBookList.action',
			type : 'post',
			async : false,
			success : function(data) {
				if (data == "0") {
					$(".row table tbody").html(
							"<span style='color:red'>未找到删除的图书</span>");
				} else {
					delBookList = eval('(' + data + ')');
					if (delBookList.delBookList.length == 0) {
						$(".row table tbody").html(
								"<span style='color:red'>未找到删除的图书</span>");
					} else {
						pageCount = delBookList.pageEntity.pageCount;
						pageRowCount = delBookList.pageEntity.pageRowCount;
						rowCount = delBookList.pageEntity.rowCount;
						$("#pageCountSpan").html(pageCount);
						$("#pageRowCountSpan").html(pageRowCount);
					}
				}
			},
			error : function(data) {
				alert("error");
			}
		});
	}

	/**
	 * 根据页码显示
	 */
	function showContentByPage(cPage) {
		$(".row table tbody").empty();
		var startRow = (cPage - 1) * pageRowCount;
		var endRow = 0;
		if (pageCount == cPage) {
			endRow = rowCount;
		} else {
			endRow = cPage * pageRowCount;
		}
		var tableStr = "";
		var items = $(column).find("item");
		for ( var i = startRow; i < endRow; i++) {
			tableStr = tableStr + "<tr>";
			for ( var j = 0; j < items.length; j++) {
				var ename = $(items[j]).attr("ename");
				tableStr = tableStr + "<td>"
						+ delBookList.delBookList[i][ename] + "</td>";
			}
			tableStr = tableStr
					+ "<td width='80px' align='center'><a class=\"text-info\" title=\"恢复\" href=\"javascript:recovery('" + delBookList.delBookList[i].book_id + "');\"><span class=\"glyphicon glyphicon-floppy-open\"></span></a>&nbsp;&nbsp;<a class=\"text-danger\" title=\"彻底删除\" href=\"javascript:delBook('" + delBookList.delBookList[i].book_id + "');\"><span class=\"glyphicon glyphicon-remove\"></span></a></td><tr>";
		}
		$("#currentPageSpan").html(cPage);
		$(".row table tbody").html(tableStr);
		initPageNum();
	}

	//恢复
	function recovery(modifyId) {
		if (window.confirm("确定要恢复吗？")) {
			$.post("recoveryDelBook.action", {
				bookId : modifyId
			}, function(data) {
				if (data == "1") {//恢复成功
					alert("恢复成功");
					//重新发起查询请求
					window.location.href = "showDelBook.action";
				} else {
					alert("对不起，恢复失败，请重新登录再试");
				}
			})
		}
	}
	
	function delBook(bookId) {
		if (window.confirm("确定要永久删除吗？")) {
			$.post("realDelBook.action", {
				bookId : bookId
			}, function(data) {
				if (data == "1") {//恢复成功
					alert("删除成功");
					//重新发起查询请求
					window.location.href = "showDelBook.action";
				} else {
					alert("对不起，删除失败，请重新登录再试");
				}
			})
		}
	}

	/**
	 * 初始化翻页
	 */
	function initPageNum() {
		if (currentPage <= 1) {
			$("#prePageTag").attr("disabled", true);
			$("#firstPageTag").attr("disabled", true);
		} else {
			$("#prePageTag").attr("disabled", false);
			$("#firstPageTag").attr("disabled", false);
		}

		if (currentPage >= pageCount) {
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
		showContentByPage(currentPage);
	}

	/**
	 * 下一页
	 */
	function nextPage() {
		currentPage = currentPage + 1;
		showContentByPage(currentPage);
	}

	/**
	 * 跳至XX页
	 */
	function jumpPage() {
		var toPage = $(".pageInput").val();
		if (toPage == "") {
			alert("页码是空的哦，亲 !");
			return;
		}
		var re = /^[1-9]+[0-9]*$/;
		if (re.test(toPage)) {
			var pageInt = parseInt(toPage);
			if (pageInt >= 1 && pageInt <= pageCount) {
				showContentByPage(pageInt);
				currentPage = pageInt;
				initPageNum();
			} else {
				alert("超出页码范围了，亲，请输入[1-" + pageCount + "]之间数字 !");
			}
		} else {
			alert("请输入正确的页码格式！");
			$(".pageInput").val("");
		}
	}

	function firstPage() {
		showContentByPage(1);
	}

	function endPage() {
		showContentByPage(pageCount);
	}
</script>
</head>
<body>
	<div class="container" id="container">
		<div class="row">
			<table class="table table-bordered table-hover table-condensed">
				<thead>

				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<td id="colspanAttr">
							<div class="pull-left">
								当前第[<span id="currentPageSpan"><s:property value="currentPage" /></span>]页，共[
								<span id="pageCountSpan"><s:property value="pageCount" /></span>]页，每页显示[
								<span id="pageRowCountSpan"><s:property value="pageRowCount" /></span>]条
							</div>
							<div class="pull-right">
								<a href="javascript:prePage()" class="btn btn-default" type="button" id="prePageTag">&larr; 上一页 </a>
								<a href="javascript:nextPage()" class="btn btn-default" type="button" id="nextPageTag">下一页 &rarr;</a>
								&nbsp;&nbsp;
								跳至<input type="text" class="pageInput" />页<a href="javascript:jumpPage();" class="btn btn-default" type="button">转</a>
							</div>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</body>
</html>