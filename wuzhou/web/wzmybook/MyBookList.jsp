<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>五洲传播--我的书目信息</title>
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
	.jinggaofail{
		color:red;
	}
	.jinggaosuc{
		color:darkgreen;
	}
	.jinggao {
		margin-right: 1em;
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
var column="";
$(function () {
	column = "<s:property value="showColumn" />";
	column = column.replace(/&lt;/ig,"<").replace(/&gt;/ig,">");
	if(!column){
		document.getElementById("container").style.display='none';
	}
	//初始化显示字段
	intShowColumn();
	//初始化检索字段
	initSearchColumn();
	//初始化图书信息
	getAllBookList(1,"","");
	//初始化分页按钮
	initPageNum();
	//默认显示第一页
	//showContentByPage(1);
	//隐藏“出版时间”输入框
	$(".form_date").hide();
	
	/*
	$('#myModal').on('hidden.bs.modal', function (e) {
		exportXml();
	});
	*/
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
		tHead = tHead + "<th align='center' style='width:80px'>操作</th>" +
						"<th align='center' style='width:80px'>电子文件</th>" +
						"<th align='center' style='width:80px'>元数据</th>" +
						"<th align='center' style='width:100px'>打印时间</th>" +
						"</tr>";
		$(".row table thead").html(tHead);
		$("#colspanAttr").prop("colspan", count+4);
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
 function getAllBookList(page, searchType, searchContent) {
	var userId = "<s:property value="#session.userEntity.user_id" />";
	$.ajax({
		url:'getBookListByCondition.action',
		type:'post',
		async: false,
		data:{page:page,searchType:searchType,searchContent:searchContent,userId:userId},
		success: function(data) {
			var json = eval('('+data+')');
			if(json.bookList==null||json.bookList.length==0) {
				$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到图书！</span>");
				$("#currentPageSpan").html("1");
				$("#pageCountSpan").html("1");
				$("#pageRowCountSpan").html("1");
				initPageNum();
			} else {
				var items = $(column).find("item");
				var tableStr="";
				for(var i = 0; i < json.bookList.length; i++) {
					tableStr = tableStr + "<tr>";
					for(var j=0; j<items.length; j++) {
						var ename = $(items[j]).attr("ename");
						if (ename == "book_paper_price" || ename == "book_ebook_price"||ename=="book_paper_dollar_price"||ename=="book_ebook_dollar_price") {
							tableStr = tableStr + "<td>"+json.bookList[i].be[ename].toFixed(2)+"</td>";
						} else {
							tableStr = tableStr + "<td>"+json.bookList[i].be[ename]+"</td>";
						}
					}
					tableStr = tableStr + "<td align='center' style='width:80px'><a class=\"text-success\" title=\"编辑\" href=\"javascript:edit('"+json.bookList[i].be.book_id+"');\"><span class=\"glyphicon glyphicon-edit\"></span></a>&nbsp;&nbsp;<a class=\"text-danger\" title=\"删除\" href=\"javascript:remove('"+json.bookList[i].be.book_id+"');\"><span class=\"glyphicon glyphicon-remove\"></span></a></td><td>";
					if(json.bookList[i].fengmian) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>封面</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>封面</span>";
					}
					if(json.bookList[i].neiwen) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>内文</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>内文</span><br />";
					}
					if(json.bookList[i].fencengpdf) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>分层PDF</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>分层PDF</span>";
					}
					if(json.bookList[i].contract) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>合同</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>合同</span>";
					}
					tableStr = tableStr + "</td><td align='center'>";
					if(json.bookList[i].bookInfo) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>完整</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>缺少</span>";
					}
					tableStr = tableStr + "</td>";

					if(json.bookList[i].printTime==null){
						tableStr+="<td></td>";
					} else {
						tableStr+="<td>"+ timeStamp2String(json.bookList[i].printTime.time, true) +"</td>";
					}

					tableStr+="</tr>";

				}
				$("#currentPageSpan").html(page);
				$(".row table tbody").html(tableStr);
				pageCount = json.pageEntity.pageCount;
				pageRowCount = json.pageEntity.pageRowCount;
				//rowCount = json.pageEntity.rowCount;
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
//格式化时间
function timeStamp2String(time){
	var datetime = new Date();
	datetime.setTime(time);
	var year = datetime.getFullYear();
	var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
	var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
	var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
	var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
	var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
	return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}

//搜索方式
function searchTypeBtn(searchType, columnName) {
	$(".searchTypeSpan").html(searchType);
	if(searchType=="出版时间") {
		$(".form_date").show();
		$(".searchContent").hide();
	} else {
		$(".form_date").hide();
		$(".searchContent").show();
	}
}

//检索
function search() {
	currentPage=1;
	pageCount=1;
	searchType = $(".searchTypeSpan").html();
	if(searchType=="出版时间"){
		$(".searchContent").val("");
		$(".searchContent").val($(".ptime").val());
	} else {
		$(".ptime").val("");
	}
	searchContent = $(".searchContent").val();
	if(searchType=="检索类别") {
		alert("请选择检索类别!");
	} else if(searchContent.replace(/ /ig,"").length<1){
		alert("请输入关键词!");
		$(".searchContent").val("");
		$(".searchContent").focus();
	} else {
		getAllBookList(1, searchType, searchContent);
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
	getAllBookList(currentPage, searchType, searchContent);
}

/**
 * 下一页
 */
function nextPage() {
	currentPage = currentPage + 1;
	getAllBookList(currentPage, searchType, searchContent);
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
			getAllBookList(currentPage, searchType, searchContent);
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
	getAllBookList(1, searchType, searchContent);
}

function endPage() {
	getAllBookList(pageCount, searchType, searchContent);
}

//获取书目详细信息
function detail(bookId) {
	//document.form.action="../wzbase/detailBook.action?bookId="+bookId;
	$("#theBookId").val(bookId);
	document.form.action="../wzbase/detailBook.action";
	document.form.submit();
}
/*
function detail(bookId) {
	$.post("getBookEntityByBookId.action",{bookId: bookId}, function(data) {
		if(data=="0") {
			alert("未获取到服务器数据，请重新登录!");
		} else {
			var json = eval('('+data+')');
			var bookInfo = "<p>"+json.book_name_cn+"</p>";
			$("#modalBody").html(bookInfo);
			$('#myModal').modal({show:true});
		}
	})
}*/

function edit(bookId) {
	$("#theBookId").val(bookId);
	//document.form.action="../wzbase/editBook.action?bookId="+bookId;
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
				//重新发起查询请求
				//document.form.action="myBookList.action";
				//document.form.submit();
				alert("删除成功");
				window.location.href = window.location.href;
			} else if(data=="2") {
				alert("对不起，删除失败!");
			} else if(data=="0") {
				alert("对不起，为获取到bookID.");
			} else {
				alert("删除失败！");
			}
		});
	}
}

//导出excel
function exportExcel(){
	var userId="<s:property value="#session.userEntity.user_id" />";
	$.ajax({
		url:'createExcelByBookListByCondition.action',
		type:'post',
		async: false,
		data: {searchType:searchType,searchContent:searchContent, userId:userId},
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
		//var myjson = JSON.stringify(bookList);
		var userId="<s:property value="#session.userEntity.user_id" />";
		$.ajax({
			url:'getFtpFiles.action',
			type:'post',
			async: false,
			data: {userId:userId, exportXmlCol:exportXmlCol,localpath:localpath,searchType:searchType,searchContent:searchContent},
			//dataType: 'json',
			success: function(data) {
				if(data!="-1") {
					window.location.href="downFtpFileXml.action?ftpFileName="+data;
				} else {
					alert("生成ftp文件错误");
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown) {
				if(XMLHttpRequest.readyState==4&&XMLHttpRequest.status==200) {
					//alert(XMLHttpRequest.responseText);
					window.location.href="downFtpFileXml.action?ftpFileName="+XMLHttpRequest.responseText;
				} else {
					alert("生成xml错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
				}
				
			}
		});
	}
}

</script>
  </head>
  
  <body>
  <form name="form" method="post">
  	<input type="hidden" value="0" name="theBookId" id="theBookId" />
    <div class="container" id='container'>
    	<div class="row">
    		<div class="col-sm-6">
    			<div class="input-group">
    				<div class="input-group-btn">
			            <button type="button" class="btn btn-default dropdown-toggle" style="width: 9em;" data-toggle="dropdown">
		                   	<span class="searchTypeSpan">检索类别</span>
		                    <span class="caret"></span>
		                </button>
		                <ul id="searchCondition" class="dropdown-menu">
		                <!-- 
		                     <li>
		                         <a href="javascript:searchType('图书编号');">图书编号</a>
		                     </li>
		                     <li>
		                         <a href="javascript:;searchType('书名（中文）')">书名（中文）</a>
		                     </li>
		                     <li>
		                         <a href="javascript:;searchType('著者（中文）')">著者（中文）</a>
		                     </li>
		                     <li>
		                         <a href="javascript:;searchType('责编（中文）')">责编（中文）</a>
		                     </li>
		                     <li>
		                         <a href="javascript:;searchType('出版时间')">出版时间</a>
		                     </li>
		                     -->
		                 </ul>
			        </div>
				    <input type="text" class="form-control searchContent" value="" placeholder="输入关键词"/>
				    
				    <div class="input-group date form_date" data-date="" data-date-format="yyyy-MM">
                        <input class="form-control ptime" size="16" placeholder="出版时间" type="text" value="" readonly>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-remove"></span>
                        </span>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                    </div>
				    <div class="input-group-btn">
				    	<button class="btn btn-default dropdown-toggle" onclick="search()" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;检索</button>
				    </div>
				   
			    </div>
    		</div>
    		<div class="col-sm-6">
    			<div class="pull-right">
	    			<div class="input-group-btn">
						<button type="button" class="btn btn-default dropdown-toggle"
							data-toggle="dropdown">
							导出 <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a href="javascript:exportExcel();" data-loading-text="正在导出..." id="exportExcel">导出图书列表</a></li>
							<li><a href="javascript:exportXml();" data-loading-text="正在导出..." id="exportXml" data-toggle="modal" data-target="#myModal">导出资源文件</a></li>
						</ul>
					</div>
				</div>
    		</div>
    	</div>
    	<div class="row">
			<table class="table table-bordered table-hover table-condensed">
				<thead>
					
				</thead>
				<tbody>
					
				</tbody>
				<tfoot>
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
        })
        
    </script>
  </body>
</html>
