<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>五洲传播--责编</title>
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
	.jinggaofail{
		color:red;
	}
	.jinggaosuc{
		color:darkgreen;
	}
	.jinggao {
		margin-right: 1em;
	}
	.aa{align:center;}
	.aa img {display:block; margin:auto;}
    </style>
<script>
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
	//初始化用户
	//initAllUserName();
	//初始化显示字段
	intShowColumn();
	//初始化检索字段
	initSearchColumn();
	//初始化图书信息
	getAllBookList("","");

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

//初始化显示的表头字段
function intShowColumn() {
	try {
		var tHead = "<tr>"; 
		$(column).find("item").each(function() {
			tHead = tHead + "<th>"+$(this).attr("cname")+"</th>";
		});
		tHead = tHead + "<th align='center' style='width:150px'>警告</th></tr>";
		tHead = tHead + "</tr>";
		$(".row table thead").html(tHead);
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
function getAllBookList(searchType, searchContent) {
	$.ajax({
		url:'getBookListByConditionAndFileSize.action',
		type:'post',
		async: true,
		data:{searchType:searchType,searchContent:searchContent},
		beforeSend: function (XMLHttpRequest) {
			//alert('远程调用开始...');
			$(".row table tbody").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
		},
		success: function(data) {
			var json = eval('('+data+')');
			if(json.length==0||json=="") {
				$(".row table tbody").html("<span style='color:red'>没有找到图书！</span>");
			} else {
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
					tableStr = tableStr + "<td>";
					if(json[i].fengmian) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>封面</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>封面</span>";
					}
					if(json[i].neiwen) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>内文</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>内文</span><br />";
					}
					if(json[i].fencengpdf) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>分层PDF</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>分层PDF</span>";
					}
					if(json[i].contract) {
						tableStr+="<span class='jinggao jinggaosuc'><span class='glyphicon glyphicon-tree-deciduous'></span>合同</span>";
					} else {
						tableStr+="<span class='jinggao jinggaofail'><span class='glyphicon glyphicon-tree-deciduous'></span>合同</span>";
					}

					tableStr = tableStr + "</td></tr>";
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




	searchType = $(".searchTypeSpan").html();
	searchContent = $(".searchContent").val();
	if(searchType=="检索类别") {
		alert("请选择检索类别!");
	} else if(searchContent.replace(/ /ig,"").length<1){
		alert("请输入关键词!");
		$(".searchContent").val("");
		$(".searchContent").focus();
	} else {
		getAllBookList(searchType, searchContent);
	}

}


//导出excel
function exportExcel(){
	$.ajax({
		url:'createExcelByFileSize.action',
		type:'post',
		async: false,
		data: {searchType:searchType,searchContent:searchContent},
		beforeSend: function (XMLHttpRequest) {
			$("body").showLoading();
		},
		success: function(data) {
			$("body").hideLoading();
			if(data!="0") {
				window.location.href="excelDownload.action?fileName="+data;
			} else {
				alert("生成excel错误");
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown) {
			$("body").hideLoading();
			if(XMLHttpRequest.readyState==4&&XMLHttpRequest.status==200) {
				window.location.href="excelDownload.action?fileName="+data;
			} else {
				alert("生成excel错误:"+XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
			}
		}
	});
}

</script>
  </head>
  
  <body>
  <form name="form" method="post">
    <div class="container" id='container'>
    	<div class="row">

    		<div class="col-sm-5">
    			<div class="input-group">
    				<div class="input-group-btn">
			            <button type="button" class="btn btn-default dropdown-toggle" style="width: 9em;" data-toggle="dropdown">
		                   	<span class="searchTypeSpan">检索类别</span>
		                    <span class="caret"></span>
		                </button>
		                <ul class="dropdown-menu" id="searchCondition"></ul>

			        </div>
				    <input type="text" class="form-control searchContent" value="" placeholder="输入关键词"/>

				    <div class="input-group-btn">
				    	<button class="btn btn-default dropdown-toggle" onclick="search()" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;检索</button>
				    </div>
				   
			    </div>
    		</div>
    		<div class="col-sm-3">
    			<div class="pull-right">
	    			<div class="input-group-btn">
						<button type="button" class="btn btn-default dropdown-toggle"
							data-toggle="dropdown">
							导出 <span class="caret"></span>
						</button>
						<ul class="dropdown-menu">
							<li><a href="javascript:exportExcel();"
								data-loading-text="正在导出..." id="exportExcel">导出图书列表</a></li>
							<!-- <li><a href="javascript:exportXml();" data-loading-text="正在导出..." id="exportXml" data-toggle="modal" data-target="#myModal">导出资源文件</a></li> -->
						</ul>
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
					
				</thead>
				<tbody>
					
				</tbody>

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
        })
        
    </script>
  </body>
</html>
