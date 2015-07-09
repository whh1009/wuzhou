<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>五洲传播--图书列表查询</title>
    <!-- Bootstrap framework -->
    <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn"t work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
    <style>
    th {
    	font-size: 15px;
    	text-align:center;
    }
    .row{
    	margin-top:1em;
    }
    td {
    	font-size:14px;
    	vertical-align: middle !important;
    }
    .pageInput {
    	border-radius: 4px;
    	border: 1px solid #ccc;
    	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
    	box-shadow: inset 0px 1px 1px rgba(0,0,0,0.075);
    	-webkit-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    	vertical-align: middle;
    	padding: 0 10;
    	color: #555;
    	height: 32px;
    	width: 3em;
    }
    
    </style>
<script>

//当前页
var currentPage = 1;
//总页数
var pageCount = 1;
//每页显示记录数（默认）
var pageRowCount = 15;
//总记录数
var rowCount = 1;
//检索类型
var searchType="";
//检索内容
var searchContent="";
//标记本社或其他社的复选框
var bookUse="";
//记录选中的用户id
var selUserId="";

var column="";
$(function () {
	column = "<s:property value="showColumn" />";
	column = column.replace(/&lt;/ig,"<").replace(/&gt;/ig,">");
	if(!column){
		document.getElementById("container").style.display='none';
	}
	//初始化用户
	initAllUserName();
	//初始化显示字段
	intShowColumn();
	//初始化检索字段
	initSearchColumn();
	//初始化图书信息
	getAllBookList("","", "BZWQ", "0");
	//初始化分页按钮
	initPageNum();
	//隐藏“出版时间”输入框
	$(".form_date").hide();
	$(".ptime_start").change(function() {
		var startDate = $(this).val();
		var endDate = $(".ptime_end").val();
		if(endDate=="") {
			$(".searchContent").val($(this).val());
		} else {
			if(startDate>endDate) {
				$(".ptime_end").val("");
				$(".searchContent").val(startDate);
			} else {
				$(".searchContent").val(startDate+" 到 "+endDate);
			}
		}
	});
	$(".ptime_end").change(function() {
		var startDate = $(".ptime_start").val();
		var endDate = $(this).val();
		if(endDate!="") {
			if(startDate=="") {
				alert("请选择起始日期");
				$(".ptime_end").val("");
			} else {
				if(startDate>endDate) {
					alert("起始日期不能超过结束日期");
					$(".ptime_end").val("");
					$(".searchContent").val($(".ptime_start").val());
				} else {
					$(".searchContent").val($(".ptime_start").val()+" 到 "+endDate);
				}
			}
		}
		
	});

	initBookUseCheckBox();
});

//初始化显示的表头字段
function intShowColumn() {
	try {
		var count=0;
		var tHead = "<tr>"; 
		$(column).find("item").each(function() {
			tHead = tHead + "<th>"+$(this).attr("cname")+"</th>";
			count++;
		});
		tHead = tHead + "</tr>";
		$(".row table thead").html(tHead);
		$("#colspanAttr").prop("colspan", count);
	}catch(e) {
		alert(e.message);
	}
}
//初始化检索字段
function initSearchColumn() {
	var searchLi = "";
	$(column).find("searchItem").each(function() {
		searchLi=searchLi+"<li><a href='javascript:searchTypeBtn(\""+$(this).attr("cname")+"\");'>"+$(this).attr("cname")+"</a>";
	});
	$("#searchCondition").html(searchLi);
}

/**
 * 获取图书信息
 */
function getAllBookListBak(page, searchType, searchContent, bookUse, uId) {
	$.ajax({
		url:'getBookListByCheckRes.action',
		type:'post',
		async: false,
		data:{page:page,searchType:searchType,searchContent:searchContent, bookUse:bookUse, selUserId:uId},
		success: function(data) {
			var json = eval('('+data+')');
			if(json.bookList.length==0) {
				$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到图书！</span>");
			} else {
				var items = $(column).find("item");
				var tableStr="";
				for(var i = 0; i < json.bookList.length; i++) {
					tableStr = tableStr + "<tr>";
					for(var j=0; j<items.length; j++) {
						var ename = $(items[j]).attr("ename");
						if (ename == "book_paper_price" || ename == "book_ebook_price"||ename=="book_paper_dollar_price"||ename=="book_ebook_dollar_price") {
							tableStr = tableStr + "<td>"+json.bookList[i][ename].toFixed(2)+"</td>";
						} else {
							tableStr = tableStr + "<td>"+json.bookList[i][ename]+"</td>";
						}
					}
					tableStr = tableStr + "<td align='center' style='width:80px'><a class=\"text-success\" title=\"编辑\" href=\"javascript:edit('"+json.bookList[i].book_id+"');\"><span class=\"glyphicon glyphicon-edit\"></span></a>&nbsp;&nbsp;<a class=\"text-danger\" title=\"删除\" href=\"javascript:remove('"+json.bookList[i].book_id+"');\"><span class=\"glyphicon glyphicon-remove\"></span></a></td><tr>";
				}
				$("#currentPageSpan").html(page);
				$(".row table tbody").html(tableStr);
				pageCount = json.pageEntity.pageCount;
				pageRowCount = json.pageEntity.pageRowCount;
				$("#pageCountSpan").html(pageCount);
				$("#pageRowCountSpan").html(pageRowCount);
				initPageNum();
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
		}
	});
}

function getAllBookList(searchType, searchContent, bookUse, uId) {
	$.ajax({
		url:'getBookListByCheckRes.action',
		type:'post',
		async: false,
		data:{searchType:searchType,searchContent:searchContent, bookUse:bookUse, selUserId:uId},
		success: function(data) {
			var json = eval('('+data+')');
			if(json==""||json.length==0) {
				$(".row table tbody").html("<tr><td><span style='color:red;font-size:8pt;'>没有找到图书！</span></td></tr>");
			} else {
				$("#tableMark").html("共 "+json.length+" 条");
				var items = $(column).find("item");
				var tableStr="";
				for(var i = 0; i < json.length; i++) {
					tableStr = tableStr + "<tr>";
					for(var j=0; j<items.length; j++) {
						var ename = $(items[j]).attr("ename");
						if (ename == "book_paper_price" || ename == "book_ebook_price"||ename=="book_paper_dollar_price"||ename=="book_ebook_dollar_price") {
							tableStr = tableStr + "<td>"+json[i].be[ename].toFixed(2)+"</td>";
						} else {
							tableStr = tableStr + "<td>"+json[i].be[ename]+"</td>";
						}
					}
					tableStr = tableStr + "</tr>";
				}
				$(".row table tbody").html(tableStr);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			alert(XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
		}
	});
}


//搜索方式
function searchTypeBtn(searchType) {
	$(".searchTypeSpan").html(searchType);
	if(searchType=="出版时间") {
		$(".form_date").show();
		$(".searchContent").prop("disabled",true);
		$(".searchContent").val("");
	} else {
		$(".form_date").hide();
		$(".searchContent").prop("disabled",false);
		$(".ptime_start").val("");
		$(".ptime_end").val("");
	}
}

//检索
function search() {
//	currentPage=1;
//	pageCount=1;
	searchType = $(".searchTypeSpan").html();
	searchContent = $(".searchContent").val();
	if(searchType=="检索类别") {
		alert("请选择检索类别!");
	} else if(searchContent.replace(/ /ig,"").length<1){
		alert("请输入关键词!");
		$(".searchContent").val("");
		$(".searchContent").focus();
	} else {
		bookUse="";
		$("input[name='bookuseCb']").each(function() {
			if($(this).is(":checked")) bookUse += $(this).val();
		});
//		currentPage = 1;
//		pageCount = 1;
//		getAllBookList(1, searchType, searchContent, bookUse, selUserId);
		getAllBookList(searchType, searchContent, bookUse, selUserId);
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
	getAllBookList(currentPage, searchType, searchContent, bookUse, selUserId);
}

/**
 * 下一页
 */
function nextPage() {
	currentPage = currentPage + 1;
	getAllBookList(currentPage, searchType, searchContent, bookUse, selUserId);
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
			getAllBookList(currentPage, searchType, searchContent, bookUse, selUserId);
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
	getAllBookList(1, searchType, searchContent, bookUse, selUserId);
}

function endPage() {
	getAllBookList(pageCount, searchType, searchContent, bookUse, selUserId);
}

//获取书目详细信息
function detail(bookId) {
	$("#theBookId").val(bookId);
	document.form.action="../wzbase/detailBook.action";
	document.form.submit();
}


function edit(bookId) {
	$("#theBookId").val(bookId);
	document.form.action="../wzbase/editBook.action";
	document.form.submit();
}

/**
 * 删除
 */
function remove(bookId) {
	if(window.confirm("你确定要删除吗？")){
		$.post("deleteBook.action",{bookId: bookId}, function(data) {
			if(data=="1") {//删除成功
				alert("删除成功");
				window.location.href = window.location.href;
			} else if(data=="2") {
				alert("对不起，删除失败!");
			} else if(data=="0") {
				alert("对不起，为获取到bookID.");
			} else {
				alert("删除失败！");
			}
		})
	}
}

//导出excel
function exportExcel(){
	$.ajax({
		url:'createExcelByCheckRes.action',
		type:'post',
		async: false,
		data: {searchType:searchType,searchContent:searchContent, bookUse:bookUse, selUserId:selUserId},
		success: function(data) {
			if(data!="0") {
				window.location.href="excelDownload.action?fileName="+data;
			} else {
				alert("生成excel错误");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			if(XMLHttpRequest.readyState==4&&XMLHttpRequest.status==200) {
				window.location.href="excelDownload.action?fileName="+data;
			} else {
				alert("生成excel错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
			}
		}
	});
}

//导出下载列表，生成xml文件
function downFtpXmlBak() {
	var exportXmlCol="";
	$("input[name='exportXmlConfig']:checked").each(function() {
		exportXmlCol=exportXmlCol+","+$(this).val();
	});
	var localpath=$("#localpath").val().replace(/(^\s*)|(\s*$)/g, "");
	if(exportXmlCol=="") {
		alert("至少选择一个作为导出项！");
		$("#myModal").modal('show');
	} else if(localpath==""){
		alert("请输入资源文件存放地址");
		$("#myModal").modal('show');
	} else {
		$("#myModal").modal('hide');
		$.ajax({
			url:'getFtpFiles.action',
			type:'post',
			async: false,
			data: {exportXmlCol:exportXmlCol, localpath:localpath, searchType:searchType, searchContent:searchContent, },
			dataType: 'json',
			success: function(data) {
				alert(data);
				if(data!="-1") {
					window.location.href="downFtpFileXml.action?ftpFileName="+data;
				} else {
					alert("生成ftp文件错误");
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.readyState==4&&XMLHttpRequest.status==200) {
					window.location.href="downFtpFileXml.action?ftpFileName="+XMLHttpRequest.responseText;
				} else {
					alert("生成xml错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
				}
				
			}
		});
	}
}

function downFtpXml() {
	var exportXmlCol="";
	$("input[name='exportXmlConfig']:checked").each(function() {
		exportXmlCol=exportXmlCol+","+$(this).val();
	});
	var localpath=$("#localpath").val().replace(/(^\s*)|(\s*$)/g, "");
	if(exportXmlCol=="") {
		alert("至少选择一个作为导出项！");
		$("#myModal").modal('show');
	} else if(localpath==""){
		alert("请输入资源文件存放地址");
		$("#myModal").modal('show');
	} else {
		$("#myModal").modal('hide');
		$.ajax({
			url:'getFtpFilesByConditions.action',
			type:'post',
			async: false,
			data: {exportXmlCol:exportXmlCol, localpath:localpath, searchType:searchType, searchContent:searchContent},
			dataType: 'json',
			success: function(data) {
				alert(data);
				if(data!="-1") {
					window.location.href="downFtpFileXml.action?ftpFileName="+data;
				} else {
					alert("生成ftp文件错误");
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.readyState==4&&XMLHttpRequest.status==200) {
					window.location.href="downFtpFileXml.action?ftpFileName="+XMLHttpRequest.responseText;
				} else {
					alert("生成xml错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
				}

			}
		});
	}
}


//初始化本社和其他社的复选框，默认全选，至少选一个
function initBookUseCheckBox() {
	$("input[name='bookuseCb']").click(function() {
		$(".searchContent").val("");
		searchType = "";
		searchContent = "";
		$("#userSel").val("0");
		var tem = "";
		var flag = false;
		$("input[name='bookuseCb']").each(function() {
			flag = flag | $(this).is(":checked");
			if($(this).is(":checked")) tem += $(this).val();
		});
		if(!flag){
			alert("请至少选择一个");
			$("input[name='bookuseCb']").each(function() {
				$(this).prop("checked", true);
			});
			bookUse = "BZWQ";
		} else {
			bookUse = tem;
		}
//		currentPage = 1;
//		pageCount = 1;
//		getAllBookList(1, "", "", bookUse, "0");
		getAllBookList("", "", bookUse, "0");
	});
}

//获取所有的用户，并组装一个select
function initAllUserName() {
	$.ajax({
		url:'getAllUser.action',
		type:'post',
		beforeSend:function(XMLHttpRequest){
			$("#userSel").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
		},
		success: function(data) {
			$("#userSel").empty();
			var userSel = "<option value='0'>全部录入者</option>";
			var userInfo = eval("("+data+")");
			for(var i=0; i<userInfo.length; i++) {
				userSel+="<option value='"+userInfo[i].user_id+"'>"+userInfo[i].user_id +"--"+userInfo[i].nick_name+"</option>";
			}
			$("#userSel").append(userSel);
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			$("#userSel").empty();
			alert(XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
		}
	});
}
//根据责编检索
function userChange() {
	var userId = $("#userSel").val();
	selUserId = userId;
	$(".searchContent").val("");
//	currentPage = 1;
//	pageCount = 1;
//	getAllBookList(1, "", "", bookUse, userId);
	getAllBookList("", "", bookUse, userId);
}

</script>
  </head>
  
  <body>
  <form name="form" method="post">
  	<input type="hidden" value="0" name="theBookId" id="theBookId" />
    <div class="container" id="container">
    	<div class="row">
			<div class="col-sm-4">
				<div>
					<!-- 图书用处 -->
					<label class="checkbox-inline"><input type="checkbox" name="bookuseCb" value="B" checked> 五洲</label>
					<label class="checkbox-inline"><input type="checkbox" name="bookuseCb" value="Z" checked> 国内</label>
					<label class="checkbox-inline"><input type="checkbox" name="bookuseCb" value="W" checked> 国际</label>
					<label class="checkbox-inline"><input type="checkbox" name="bookuseCb" value="Q" checked> 合作出版社</label>
				</div>
			</div>
			<div class="col-sm-2">
				<select id="userSel" class="form-control" onchange='userChange()'>
					<option value="0">全部录入者</option>
				</select>
			</div>
    		<div class="col-sm-5">
    			<div class="input-group">
    				<div class="input-group-btn">
			            <button type="button" class="btn btn-default dropdown-toggle" style="width: 9em;" data-toggle="dropdown">
		                   	<span class="searchTypeSpan">检索类别</span>
		                    <span class="caret"></span>
		                </button>
		                <ul id="searchCondition" class="dropdown-menu">
		                 </ul>
			        </div>
				    <input type="text" class="form-control searchContent" value="" placeholder="输入关键词"/>
				    <div class="input-group-btn">
				    	<button class="btn btn-default dropdown-toggle" onclick="search()" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;检索</button>
				    </div>
				   
			    </div>
    		</div>
    		<div class="col-sm-1">
    			<!-- <div class="pull-right"> -->
	    			<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle"
							data-toggle="dropdown">
							导出 <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a href="javascript:exportExcel();"
								data-loading-text="正在导出..." id="exportExcel">导出图书列表</a></li>

						</ul>
					</div>
				<!-- </div> -->
    		</div>
    	</div>
    	
    	<div class="row" id="sdate">
    		<div class="col-sm-8">
    			<div class="col-sm-5">
    			<div class="input-group date form_date" data-date="" data-date-format="yyyy-MM">
                	<input class="form-control ptime_start" size="16" placeholder="出版时间--起始" type="text" value="" readonly>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-remove"></span>
                    </span>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
                </div>
                <div class="col-sm-5">
                <div class="input-group date form_date" data-date="" data-date-format="yyyy-MM">
                    <input class="form-control ptime_end" size="16" placeholder="出版时间--结束" type="text" value="" readonly>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-remove"></span>
                    </span>
                    <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                </div>
                </div>
    		</div>
    	</div>
		<div id="tableMark"></div>
    	<div class="row">
			<table class="table table-bordered table-hover table-condensed">
				<thead>
					
				</thead>
				<tbody>
					
				</tbody>
				<tfoot style="display: none">
					<tr>
					<td id="colspanAttr">
						<div class="pull-left">当前第[<span id="currentPageSpan"><s:property value="currentPage" /></span>]页，共[<span id="pageCountSpan"><s:property value="pageCount" /></span>]页，每页显示[<span id="pageRowCountSpan"><s:property value="pageRowCount" /></span>]条</div>
						<div class="pull-right"><!-- <a href="javascript:firstPage()" class="btn btn-info" id="firstPageTag" type="button">第一页</a> --><a href="javascript:prePage()" class="btn btn-default" type="button" id="prePageTag">&larr; 上一页 </a><a href="javascript:nextPage()"  class="btn btn-default" type="button" id="nextPageTag">下一页 &rarr;</a>&nbsp;&nbsp;<!-- <a href="javascript:endPage()" class="btn btn-info" id="endPageTag" type="button">最后一页</a>&nbsp;&nbsp; -->跳至<input type="text" class="pageInput" />页<a href="javascript:jumpPage();" class="btn btn-default" type="button">转</a></div>
					</td>
					</tr>
				</tfoot>
			</table>
		</div>
    </div>
    
    
    <!-- 模态框 -->
    <div class="modal fade in" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    	<div class="modal-dialog">
    		<div class="modal-content">
    			<div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">资源下载字段配置</h4>
                </div>
	    		<div class="modal-body">
	    			<div class="alert alert-danger">至少选择一个！</div>
	    			<!-- <p class="text-danger pull-right"><small>至少选择一个下载项！</small></p> -->
	    			<table class="table table-bordered">
	    				<tbody>
	    					<tr>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_cover_serverpath" checked> 封面</td>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_neiwen_serverpath" checked> 内文</td>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_font_serverpath" checked> 字体</td>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_pdf_publish_serverpath" checked> 分层PDF</td>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_word_serverpath"> WORD</td>	
	    					</tr>
	    					<tr>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_xml_serverpath"> XML</td>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_epub_serverpath"> EPUB</td>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_mobi_serverpath"> MOBI</td>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_pdf_read_serverpath"> PDF阅读</td>
	    						<td><input type="checkbox" name="exportXmlConfig" value="book_html_serverpath"> HTML</td>
	    					</tr>
	    				</tbody>
	    			</table>
	    			<br />
	    			<div class="form-group">
	    				<div class="col-sm-12">
	    					<input class="form-control" type="text" id="localpath" placeholder="资源文件存放地址，如，D:\mybook，请保证磁盘足够大" />
	    				</div>
	    			</div>
	    		</div>
	    		<div class="modal-footer">
	    			<button type="button" class="btn btn-success" onclick="downFtpXml()">确定</button>
	                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
    		</div>
    	</div>
    </div>
    </form>
    <script src="../js/json2.js"></script>
    <script src="../js/button.js"></script>
    <script src="../js/bootstrap-datetimepicker.min.js"></script>
	<script src="../js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
	<script type="text/javascript">
        
        $(function() {
            $(".form_date").datetimepicker({
                language:  "zh-CN",
                weekStart: 1,
                todayBtn:  1,
                autoclose: 1,
                todayHighlight: 1,
                startView: 2,
                minView: 2,
                forceParse: 0,
                pickerPosition: "bottom-left"
            });
        });
        
    </script>
  </body>
</html>
