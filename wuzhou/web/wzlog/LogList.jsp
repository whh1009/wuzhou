<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
    <title>五洲传播--日志管理</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <style>
    input{
    	margin-bottom: 1em;
    }
    #myModal .modal-dialog {
	  width: 60%;
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
    .mtsbh{width:25%}
    .myh{width:20%}
    .mczsj{width:25%}
    .mjtcz{width:15%}
    .cz{width:15%;}
    table, th{text-align:center}
    </style>
    <script type="text/javascript">
  //日志信息
    var logList;
    //当前页
    var currentPage = 1;
    //总页数
    var pageCount = 1;
    //每页显示记录数（默认）
    var pageRowCount = 15;
    //总记录数
    var rowCount = 1;
	var fromflag = "<s:property value="logFlag" />";
	$(function() {
		if(!fromflag){
			document.getElementById("container").style.display = 'none';
		}
    	getAlllogList();
    	initPageNum();
    	//默认显示第一页
    	showContentByPage(1);
    	
    })
    
    /**
     * 获取图书信息
     */
    function getAlllogList() {
    	$.ajax({
    		url:'getLogList.action',
    		type:'post',
    		async: false,
    		success: function(data) {
    			if(data=="0") {
    				$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到日志！</span>");
    			} else {
    				logList = eval('('+data+')');
    				if(logList.logList.length==0) {
    					$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到日志！</span>");
    				} else {
    					pageCount = logList.pageEntity.pageCount;
        				pageRowCount = logList.pageEntity.pageRowCount;
        				rowCount = logList.pageEntity.rowCount;
        				$("#pageCountSpan").html(pageCount);
        				$("#pageRowCountSpan").html(pageRowCount);
    				}
    			}
    		},
    		error: function(data) {
    			alert("error");
    		}
    	});
    }

    /**
     * 根据页码显示
     */
    function showContentByPage(cPage) {
    	$(".row table tbody").empty();
    	var startRow=(cPage-1)*pageRowCount;
    	var endRow=0;
    	if(pageCount==cPage) {
    		endRow = rowCount;
    	} else {
    		endRow = cPage*pageRowCount;
    	}
    	var tableStr="";
    	for(var i=startRow; i<endRow; i++) {
    		tableStr = tableStr + "<tr><td>"+logList.logList[i].book_serial_number+"</td><td>"+logList.logList[i].user_name+"</td><td>"+toDate(logList.logList[i].modify_time, 'yyyy-MM-dd hh:mm:ss')+"</td><td>"+logList.logList[i].modify_type+"</td>"+
    		"<td><a class=\"text-danger\" title=\"删除\" href=\"javascript:remove('"+logList.logList[i].modify_id+"');\"><span class=\"glyphicon glyphicon-remove\"></span>删除</a></td><tr>";
    	}
    	$("#currentPageSpan").html(cPage);
    	$(".row table tbody").html(tableStr);
    	initPageNum();
    }
	
    //格式化时间
    Date.prototype.format = function(format) {
        /*
          * format="yyyy-MM-dd hh:mm:ss";
          */
         var o = {
             "M+" : this.getMonth() + 1,
             "d+" : this.getDate(),
             "h+" : this.getHours(),
             "m+" : this.getMinutes(),
             "s+" : this.getSeconds(),
             "q+" : Math.floor((this.getMonth() + 3) / 3),
             "S" : this.getMilliseconds()
         };
         if (/(y+)/.test(format)) {
                 format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4- RegExp.$1.length));
             }
         for (var k in o) {
             if (new RegExp("(" + k + ")").test(format)){
                 format = format.replace(RegExp.$1, RegExp.$1.length == 1? o[k]:("00" + o[k]).substr(("" + o[k]).length));
             }
         }
         return format;
     };
     
     //转化JSON日期格式
    function toDate(objDate, format) {
         var date = new Date();
         date.setTime(objDate.time);
         date.setHours(objDate.hours);
         date.setMinutes(objDate.minutes);
         date.setSeconds(objDate.seconds);
         return date.format(format);

    }

    //搜索方式
    function searchType(searchType, columnName) {
    	$(".searchTypeSpan").html(searchType);
    }

    //检索
    function search() {
    	var searchType = $(".searchTypeSpan").html();
    	var searchContent = $(".searchContent").val();
    	if(searchType=="检索类别") {
    		alert("请选择检索类别!");
    	} else if(searchContent.replace(/ /ig,"").length<1){
    		alert("请输入关键词!");
    		$(".searchContent").val("");
    		$(".searchContent").focus();
    	} else {
    		$.ajax({
    			url:'getlogListByParm.action',
    			type:'post',
    			data: {searchType:searchType, searchContent:searchContent},
    			async: false,
    			success: function(data) {
    				if(data=="0") {
    					$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到图书！</span>");
    				} else {
    					logList = eval('('+data+')');
    					if(logList.logList.length==0) {
    						$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到图书！</span>");
    					} else {
    						pageCount = logList.pageEntity.pageCount;
        					pageRowCount = logList.pageEntity.pageRowCount;
        					rowCount = logList.pageEntity.rowCount;
        					$("#pageCountSpan").html(pageCount);
        					$("#pageRowCountSpan").html(pageRowCount);
        					showContentByPage(1);
    					}
    				}
    			},
    			error: function(data) {
    				alert("error");
    			}
    		});
    	}
    }
    
    //删除
    function remove(modifyId) {
    	if(window.confirm("确定要删除吗？")) {
    		$.post("deleteLog.action",{modifyId: modifyId}, function(data) {
    			if(data=="1") {//删除成功
    				//重新发起查询请求
    				alert("删除成功");
    				window.location.href="logList.action";
    			} else if(data=="2") {
    				alert("对不起，删除失败!")
    			} else if(data=="0") {
    				alert("对不起，为获取到日志ID.");
    			} else {
    				alert("删除失败！");
    			}
    		})
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
    	if(toPage=="") {
    		alert("页码是空的哦，亲 !");
    		return ;
    	}
    	var re = /^[1-9]+[0-9]*$/;
    	if(re.test(toPage)) {
    		var pageInt = parseInt(toPage);
    		if(pageInt>=1&&pageInt<=pageCount) {
    			showContentByPage(pageInt);
    			currentPage=pageInt;
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
    		<div class="col-md-6"></div>
    		<div class="col-md-6">
    			<div class="input-group">
    				<div class="input-group-btn">
			            <button type="button" class="btn btn-default dropdown-toggle" style="width: 9em;" data-toggle="dropdown">
		                   	<span class="searchTypeSpan">检索类别</span>
		                    <span class="caret"></span>
		                </button>
		                <ul class="dropdown-menu">
		                     <li>
		                         <a href="javascript:searchType('图书编号');">图书编号</a>
		                     </li>
		                     <li>
		                         <a href="javascript:;searchType('用户')">用户</a>
		                     </li>
		                 </ul>
			        </div>
				    <input type="text" class="form-control searchContent" value="" placeholder="输入关键词" />
				    <div class="input-group-btn">
				    	<button class="btn btn-default dropdown-toggle" onclick="search()" type="button"><span class="glyphicon glyphicon-search"></span>&nbsp;检索</button>
				    </div>
			    </div>
    		</div>
    	</div>
    	<br />
		<div class="row">
			<table class="table table-bordered table-hover table-condensed">
				<thead>
					<tr>
						<th class="mtsbh">图书编号</th><th class="myh">用户</th><th class="mczsj">操作时间</th><th class="mjtcz">操作类型</th><th class="cz">操作</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
					<td colspan="10">
						<div class="pull-left">当前第[<span id="currentPageSpan"><s:property value="currentPage" /></span>]页，共[<span id="pageCountSpan"><s:property value="pageCount" /></span>]页，每页显示[<span id="pageRowCountSpan"><s:property value="pageRowCount" /></span>]条</div>
						<div class="pull-right"><a href="javascript:prePage()" class="btn btn-default" type="button" id="prePageTag">&larr; 上一页 </a><a href="javascript:nextPage()"  class="btn btn-default" type="button" id="nextPageTag">下一页 &rarr;</a>&nbsp;&nbsp;跳至<input type="text" class="pageInput" />页<a href="javascript:jumpPage();" class="btn btn-default" type="button">转</a></div>
					</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
  </body>
</html>