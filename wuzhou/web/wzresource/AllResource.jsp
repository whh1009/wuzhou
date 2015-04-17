<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>五洲传播--资源上传列表</title>
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
var column="";
$(function () {
	column = "<s:property value="showColumn" />";
	column = column.replace(/&lt;/ig,"<").replace(/&gt;/ig,">");
	if(!column){
		document.getElementById("container").style.display='none';
	}
	//初始化检索字段
	initSearchColumn();
	//初始化图书信息
	getAllBookList(1,"","");
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
});


function getAllBookList(page, searchType, searchContent) {
	$.ajax({
		url:'getAllResourceByCondition.action',
		type:'post',
		async: true,
		data:{page:page,searchType:searchType,searchContent:searchContent},
		success: function(data) {
			var json = eval('('+data+')');
			if(json.brList.length==0) {
				$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到！</span>");
			} else {
				var tableStr="";
				for(var i = 0; i < json.brList.length; i++) {
					tableStr = tableStr + "<tr>";
					tableStr = tableStr + "<td>"+json.brList[i].book_serial_number+"</td>";
					tableStr = tableStr + "<td>"+json.brList[i].book_name_cn+"</td>";
					//tableStr = tableStr + "<td>"+json.brList[i].book_language+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].neiwen_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].cover_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].font_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].pdf_publish_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].word_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].xml_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].epub_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].mobi_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].pdf_read_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].html_size.toFixed(2)+"</td>";
					tableStr = tableStr + "<td class='mysize'>"+json.brList[i].contract_size.toFixed(2)+"</td>";
					tableStr = tableStr + "</tr>";
				}
				$("#currentPageSpan").html(page);
				$(".row table tbody").html(tableStr);
				//变色
				changeColor();
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
//变色，小于1M的用danger，否则用info
function changeColor(){
	$(".mysize").each(function() {
		var size = parseFloat($(this).html());
		if(size<1) {
			$(this).addClass("danger");
		}else{
			$(this).addClass("success");
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
	currentPage=1;
	pageCount=1;
	searchType = $(".searchTypeSpan").html();
	/*
	if(searchType=="出版时间"){
		$(".searchContent").val("");
		$(".searchContent").val($(".ptime_start").val());
	} else {
		$(".ptime_start").val("");
		$(".ptime_end").val("");
	}
	*/
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
//初始化检索字段
function initSearchColumn() {
	var searchLi = "";
	$(column).find("searchItem").each(function() {
		searchLi=searchLi+"<li><a href='javascript:searchTypeBtn(\""+$(this).attr("cname")+"\");'>"+$(this).attr("cname")+"</a>";
	});
	$("#searchCondition").html(searchLi);
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


</script>
  </head>
  
  <body>
  <form name="form" method="post">
  	<input type="hidden" value="0" name="theBookId" id="theBookId" />
    <div class="container" id="container">
    	<div class="row">
    		<div class="col-sm-6">
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
    	
    	<div class="row">
			<table class="table table-bordered table-hover table-condensed">
				<thead>
					<tr>
						<th>图书编号</th><th>书名</th><th>排版文件</th><th>封面</th><th>字体</th><th>分层PDF</th>
						<th>WORD</th><th>XML</th><th>EPUB</th><th>MOBI</th><th>阅读PDF</th><th>HTML</th><th>合同</th>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
				<tfoot>
					<tr>
					<td colspan="13" id="colspanAttr">
						<div class="pull-left">当前第[<span id="currentPageSpan"><s:property value="currentPage" /></span>]页，共[<span id="pageCountSpan"><s:property value="pageCount" /></span>]页，每页显示[<span id="pageRowCountSpan"><s:property value="pageRowCount" /></span>]条</div>
						<div class="pull-right"><!-- <a href="javascript:firstPage()" class="btn btn-info" id="firstPageTag" type="button">第一页</a> --><a href="javascript:prePage()" class="btn btn-default" type="button" id="prePageTag">&larr; 上一页 </a><a href="javascript:nextPage()"  class="btn btn-default" type="button" id="nextPageTag">下一页 &rarr;</a>&nbsp;&nbsp;<!-- <a href="javascript:endPage()" class="btn btn-info" id="endPageTag" type="button">最后一页</a>&nbsp;&nbsp; -->跳至<input type="text" class="pageInput" />页<a href="javascript:jumpPage();" class="btn btn-default" type="button">转</a></div>
					</td>
					</tr>
				</tfoot>
			</table>
			
			
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
