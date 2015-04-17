<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp"%>

<!DOCTYPE HTML>
<html>
<head>
<title>五洲传播--图书在线销售标注</title>
<link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
<style type="text/css">
th {
	font-size: 15px;
	text-align: center;
}
.row {
	margin-top: 1em;
}
td {
	font-size: 14px;
	vertical-align: middle !important;
}
.pageInput {
	border-radius: 4px;
	border: 1px solid #ccc;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
	box-shadow: inset 0px 1px 1px rgba(0, 0, 0, 0.075);
	-webkit-transition: border-color ease-in-out .15s, box-shadow
		ease-in-out .15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
	vertical-align: middle;
	padding: 0 10;
	color: #555;
	height: 32px;
	width: 3em;
}
#slider .slider-selection{
	background: #f0ad4e;
}
.osName{
	font-size:80%;
}
.osp{
	border:solid 1px #f0ad4e;
}
.progress{
	cursor:pointer;
}
.modal-lg {
	width:900px;
}
.progress-bar-daizhuanma{
	background-color:#CCCCCC;
}

.progress-bar-daishangxian {
	background-color:#FF9999;
}
.aa{align:center;}
.aa img {display:block; margin:auto;}
</style>
<script type="text/javascript">
//随机颜色
var classArr = new Array("label label-success", "label label-warning", "label label-info", "label label-danger", "label label-primary", "label label-primary");
//状态对应码位：0--转码中, 1--已上线, 2--转码完, 3--已停售, 4--待转码, 5--待上线
var statusName = new Array("转码中", "已上线", "转码完", "已停售", "待转码", "待上线");
//状态对应颜色
var statusClass = new Array("progress-bar progress-bar-success","progress-bar progress-bar-info","progress-bar progress-bar-warning","progress-bar progress-bar-danger","progress-bar progress-bar-daizhuanma","progress-bar progress-bar-default");
//当前页
	var currentPage = 1;
	//总页数
	var pageCount = 1;
	//每页显示记录数（默认）
	var pageRowCount = 15;
	//总记录数
	var rowCount = 1;
	//检索类型
	var searchType = "";
	//检索内容
	var searchContent = "";
	var column = "";
	var osXml="";//平台xml
	$(function() {
		
		column = "<s:property value='showColumn' />";
		column = column.replace(/&lt;/ig, "<").replace(/&gt;/ig, ">");
		if (!column) {
			document.getElementById("container").style.display = 'none';
		}
		osXml = "<s:property value='osXml' />";
		osXml = osXml.replace(/&lt;/ig, "<").replace(/&gt;/ig, ">");
		//初始化显示字段
		intShowColumn();
		//初始化检索字段
		initSearchColumn();
		//初始化图书信息
		getAllBookList(1, "", "");
		//初始化分页按钮
		initPageNum();
		//隐藏“出版时间”输入框
		
		$(".form_date").hide();
		$(".ptime_start").change(function() {
			var startDate = $(this).val();
			var endDate = $(".ptime_end").val();
			if (endDate == "") {
				$(".searchContent").val($(this).val());
			} else {
				if (startDate > endDate) {
					$(".ptime_end").val("");
					$(".searchContent").val(startDate);
				} else {
					$(".searchContent").val(startDate + " 到 " + endDate);
				}
			}
		});
		$(".ptime_end").change(
				function() {
					var startDate = $(".ptime_start").val();
					var endDate = $(this).val();
					if (endDate != "") {
						if (startDate == "") {
							alert("请选择起始日期");
							$(".ptime_end").val("");
						} else {
							if (startDate > endDate) {
								alert("起始日期不能超过结束日期");
								$(".ptime_end").val("");
								$(".searchContent")
										.val($(".ptime_start").val());
							} else {
								$(".searchContent").val(
										$(".ptime_start").val() + " 到 "
												+ endDate);
							}
						}
					}

				});
		initQuery1();
		$("#queryOs").change(function() {
			var osIdSel =$(this).val();
			if(osIdSel!=0) {
				$("#queryOsStatus").html("<option value='-1'>请选择</option><option value='4'>"+statusName[4]+"</option><option value='0'>"+statusName[0]+"</option><option value='2'>"+statusName[2]+"</option><option value='5'>"+statusName[5]+"</option><option value='1'>"+statusName[1]+"</option><option value='3'>"+statusName[3]+"</option>");
			}
		});
		$("#queryOsStatus").change(function() {
			$("#tempCount").html("");
			var statusIdSel = $(this).val();
			var osIdSel = $("#queryOs").val();
			if(statusIdSel=="-1") return false;
			$.ajax({
				url:"getOnlineStatusBook.action",
				data:{osIdSel:osIdSel, statusIdSel:statusIdSel},
				beforeSend:function(XMLHttpRequest){
		            $(".content table tbody").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
		       	},
			  	success:function(data) {
					if(data=="") {
						$(".content table tbody").html("<span style='color:red;font-size:8pt;'>没有找到图书！</span>");
						return;
					}
					var json = eval('(' + data + ')');
					var list = json.bookOnlineList;
					if(list!=null) {
						bookOnlineXml = " <online>";
						for(var m=0;m<list.length;m++) {
							bookOnlineXml+="<item onlineId='"+list[m].online_id+"' bookId='"+list[m].book_id+"' osId='"+list[m].os_id+"' isOnline='"+list[m].is_online+"' />";
						}
						bookOnlineXml+="</online> ";
					}
					if (json.bookList.length == 0) {
						$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到图书！</span>");
					} else {
						var items = $(column).find("item");
						var tableStr = "";
						for (var i = 0; i < json.bookList.length; i++) {
							tableStr = tableStr + "<tr id='tr_"+json.bookList[i].book_id+"'>";
							for (var j = 0; j < items.length; j++) {
								var ename = $(items[j]).attr("ename");
								if (ename == "book_paper_price"
										|| ename == "book_ebook_price") {
									tableStr = tableStr + "<td>" + json.bookList[i][ename] .toFixed(2) + "</td>";
								} else {
									tableStr = tableStr + "<td>" + json.bookList[i][ename] + "</td>";
								}
							}
							tableStr = tableStr + "<td style='width:180px'>";
							$(osXml).find("item").each(function() {
								if(json.bookOnlineList!=null) {
									for(var k=0;k<json.bookOnlineList.length;k++) {
										if(json.bookList[i].book_id==json.bookOnlineList[k].book_id && json.bookOnlineList[k].os_id==$(this).attr("osId").toString()) {
											var onlineStatus=json.bookOnlineList[k].is_online;
											//if(onlineStatus==null||typeof(onlineStatus)=="undefined") onlineStatus=4;
											tableStr = tableStr + "<p class=''><span class='osName "+classArr[onlineStatus]+"'>"+$(this).attr("osName") + statusName[onlineStatus] +"</span></p>";
											/*
											if(onlineStatus==1) { //上线
												tableStr = tableStr + "<p class=''><span class='osName "+classArr[2]+"'>"+$(this).attr("osName") + statusName[2] +"</span></p>";
											} else if(onlineStatus==2) { //转码完
												tableStr = tableStr + "<p class=''><span class='osName "+classArr[1]+"'>"+$(this).attr("osName") + statusName[1] +"</span></p>";
											} else if(onlineStatus==3) { //下线
												tableStr = tableStr + "<p class=''><span class='osName "+classArr[3]+"'>"+$(this).attr("osName") + statusName[3] +"</span></p>";
											} else if(onlineStatus==0) { //转码中
												tableStr = tableStr + "<p class=''><span class='osName "+classArr[0]+"'>"+$(this).attr("osName") + statusName[0] +"</span></p>";
											} else {
												
											}
											*/
											break;
										}
									}
								}
							});
							tableStr = tableStr + "<a href=\"javascript:updateBookOnlineState("+json.bookList[i].book_id+")\"><span class=\"badge\">更新</span></a>";
						}
						$("#currentPageSpan").html("1");
						$(".content table tbody").html(tableStr);
						pageCount = "1";
						pageRowCount = 15;
						$("#pageCountSpan").html("1");
						$("#pageRowCountSpan").html("15");
						$("#tempCount").html("<span class='text-danger'>共找到："+json.bookList.length+"本&nbsp;&nbsp;</span>");
					}
				}
			});
		});
	});
	
	

	//初始化显示的表头字段
	function intShowColumn() {
		try {
			var count = 0;
			var tHead = "<tr>";
			$(column).find("item").each(function() {
				tHead = tHead + "<th>" + $(this).attr("cname") + "</th>";
				count++;
			});
			tHead = tHead
					+ "<th align='center' style='width:80px'>状态</th></tr>";
			$(".content table thead").html(tHead);
			$("#colspanAttr").prop("colspan", count + 1);
		} catch (e) {
			alert(e.message);
		}
	}
	//初始化检索字段
	function initSearchColumn() {
		var searchLi = "";
		$(column).find("searchItem").each(
				function() {
					searchLi = searchLi
							+ "<li><a href='javascript:searchTypeBtn(\""
							+ $(this).attr("cname") + "\");'>"
							+ $(this).attr("cname") + "</a>";
				});
		$("#searchCondition").html(searchLi);
	}
	var bookOnlineXml="";
	/**
	 * 获取图书信息
	 */
	function getAllBookList(page, searchType, searchContent) {
		$("#tempCount").html("");
		bookOnlineXml="";
		$.ajax({
					url : 'getBookOnlineListByCondition.action',
					type : 'post',
					async : false,
					data : {
						page : page,
						searchType : searchType,
						searchContent : searchContent
					},
					success : function(data) {
						var json = eval('(' + data + ')');
						var list = json.bookOnlineList;
						if(list!=null) {
							bookOnlineXml = " <online>";
							for(var m=0;m<list.length;m++) {
								bookOnlineXml+="<item onlineId='"+list[m].online_id+"' bookId='"+list[m].book_id+"' osId='"+list[m].os_id+"' isOnline='"+list[m].is_online+"' />";
							}
							bookOnlineXml+="</online> ";
						}
						if (json.bookList.length == 0) {
							$(".row table tbody").html("<span style='color:red;font-size:8pt;'>没有找到图书！</span>");
						} else {
							var items = $(column).find("item");
							var tableStr = "";
							for (var i = 0; i < json.bookList.length; i++) {
								tableStr = tableStr + "<tr id='tr_"+json.bookList[i].book_id+"'>";
								for (var j = 0; j < items.length; j++) {
									var ename = $(items[j]).attr("ename");
									if (ename == "book_paper_price"
											|| ename == "book_ebook_price") {
										tableStr = tableStr + "<td>" + json.bookList[i][ename] .toFixed(2) + "</td>";
									} else {
										tableStr = tableStr + "<td>" + json.bookList[i][ename] + "</td>";
									}
								}
								tableStr = tableStr + "<td style='width:180px'>";
								$(osXml).find("item").each(function() {
									var checkStr="";
									if(json.bookOnlineList!=null) {
										for(var k=0;k<json.bookOnlineList.length;k++) {
											if(json.bookList[i].book_id==json.bookOnlineList[k].book_id && json.bookOnlineList[k].os_id==$(this).attr("osId").toString()) {
												//checkStr=" checked='checked' ";
												var onlineStatus=json.bookOnlineList[k].is_online;
												tableStr = tableStr + "<p class=''><span class='osName "+classArr[onlineStatus]+"'>"+$(this).attr("osName") + statusName[onlineStatus] +"</span></p>";
												/*
												if(onlineStatus==1) {//Math.floor(Math.random()*(5-1)+1) //1-5随机数 
													//上线
													tableStr = tableStr + "<p class=''><span class='osName "+classArr[2]+"'>"+$(this).attr("osName") + statusName[2] +"</span></p>";
												} else if(onlineStatus==2) { //转码完
													tableStr = tableStr + "<p class=''><span class='osName "+classArr[1]+"'>"+$(this).attr("osName") + statusName[1] +"</span></p>";
												} else if(onlineStatus==3) { //下线
													tableStr = tableStr + "<p class=''><span class='osName "+classArr[3]+"'>"+$(this).attr("osName") + statusName[3] +"</span></p>";
												} else if(onlineStatus==0) { //转码中
													tableStr = tableStr + "<p class=''><span class='osName "+classArr[0]+"'>"+$(this).attr("osName") + statusName[0] +"</span></p>";
												} else {
													
												}
												*/
												break;
											} else {
												checkStr="";
											}
										}
									}
									//tableStr = tableStr + " <input type='checkbox' " + checkStr + " value='"+$(this).attr("osId").toString()+"'> " + $(this).attr("osName").toString()+"<br />";
								});
								//tableStr = tableStr + "<button class=\"btn btn-success btn-block\" id=\"btn_"+json.bookList[i].book_id+"\" title=\"保存\" onclick=\"markOnline(this, '"+ json.bookList[i].book_id + "');\"><span class=\"glyphicon glyphicon-ok\"></span> 修改或保存</button></td><tr>";
								tableStr = tableStr + "<a href=\"javascript:updateBookOnlineState('"+json.bookList[i].book_id+"')\"><span class=\"badge\">更新</span></a>";
							}
							$("#currentPageSpan").html(page);
							$(".content table tbody").html(tableStr);

							pageCount = json.pageEntity.pageCount;
							pageRowCount = json.pageEntity.pageRowCount;
							//rowCount = json.pageEntity.rowCount;
							$("#pageCountSpan").html(pageCount);
							$("#pageRowCountSpan").html(pageRowCount);
							initPageNum();
						}
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert(XMLHttpRequest.readyState + XMLHttpRequest.status
								+ XMLHttpRequest.responseText);
					}
				});
	}
	
	function markOnline(arg1, bookId) {
		$("#btn_"+bookId).button("loading");
		var markStr="<mark>";
		var tdObj = $(arg1).parent();
		var cbObj = $(tdObj).children("input[type='checkbox']");
		for(var i=0;i<cbObj.length;i++) {
			markStr+="<item bookId='"+bookId+"' osId='"+cbObj[i].value+"' isOnline='"+cbObj[i].checked+"' />";
		}
		markStr+="</mark>";
		$.ajax({
			url:'updateBookOnline.action',
			type:'post',
			async : true,
			data:{markStr:markStr},
			success:function(data) {
				if(data=="1"){
					alert("保存成功");
				}else if(data=="-1") {
					alert("没有获取到图书是否上线信息");
				}else{
					alert("未做保存");
				}
			}
		});
		$("#btn_"+bookId).button("reset");
	}
	
	
	function getOnlineOsByBookId(bkid) {
		$.ajax({
			url:'getOnlineOsByBookId.action',
			type:'post',
			data:{bookId:bkid},
			success:function(data) {
				alert(data);
				if(data=="-1") {
					alert("未获取到图书ID");
				} else if(data!="") {
					$(data).find("item").each(function() {
						
					});
				}
			}
			
		});
	}

	//搜索方式
	function searchTypeBtn(searchType) {
		$(".searchTypeSpan").html(searchType);
		if (searchType == "出版时间") {
			$(".form_date").show();
			$(".searchContent").prop("disabled", true);
			$(".searchContent").val("");
		} else {
			$(".form_date").hide();
			$(".searchContent").prop("disabled", false);
			$(".ptime_start").val("");
			$(".ptime_end").val("");
		}
	}

	//检索
	function search() {
		currentPage = 1;
		pageCount = 1;
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
		if (searchType == "检索类别") {
			alert("请选择检索类别!");
		} else if (searchContent.replace(/ /ig, "").length < 1) {
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
		if (toPage == "") {
			alert("页码是空的哦，亲 !");
			return;
		}
		var re = /^[1-9]+[0-9]*$/;
		if (re.test(toPage)) {
			var pageInt = parseInt(toPage);
			if (pageInt >= 1 && pageInt <= pageCount) {
				currentPage = pageInt;
				getAllBookList(currentPage, searchType, searchContent);
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
		getAllBookList(1, searchType, searchContent);
	}

	function endPage() {
		getAllBookList(pageCount, searchType, searchContent);
	}
	//全局缓存图书id
	var updateBookId="0";
	//修改图书在线状态
	function updateBookOnlineState(book_id) {
		updateBookId=book_id;
		$("#content").empty();
		var content="";
		$(osXml).find("item").each(function() {
			var osName=$(this).attr("osName");
			if(bookOnlineXml==""||$(bookOnlineXml).find("item[bookId='"+book_id+"']").length==0){
				content+="<div class='row'><div class='col-sm-2'>"+osName+"</div><div class='col-sm-10'><div class='progress' cOsId='"+$(this).attr("osId")+"' onclick='changeStatus(this)'>";
				content+="<div class='"+statusClass[4]+"' style='width: 16.66%' >"+ statusName[4] +"</div>";
				content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 0' >"+ statusName[0] +"</div>";
				content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 0' >"+ statusName[2] +"</div>";
				content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 0'>"+ statusName[5] +"</div>";
				content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 0'>"+ statusName[1] +"</div>";
				content+="<div class='"+statusClass[3]+"' style='width: 0'>"+ statusName[3] +"</div>";
				content+="</div></div></div>";
			} else {
				var osId=$(this).attr("osId");
				var flag=false;
				$(bookOnlineXml).find("item[bookId='"+book_id+"']").each(function() {
					if(osId==$(this).attr("osId")) {
						flag=true;
						var isOnline=$(this).attr("isOnline");
						content+="<div class='row'><div class='col-sm-2'>"+osName+"</div><div class='col-sm-10'><div class='progress' cOsId='"+osId+"' onclick='changeStatus(this)'>";
						if(isOnline=="4") { //待转码
							content+="<div class='"+statusClass[4]+"' style='width: 16.66%'>"+ statusName[4] +"</div>";
							content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 0'>"+ statusName[0] +"</div>";
							content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 0'>"+ statusName[2] +"</div>";
							content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 0'>"+ statusName[5] +"</div>";
							content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 0'>"+ statusName[1] +"</div>";
							content+="<div class='"+statusClass[3]+"' style='width: 0'>"+ statusName[3] +"</div>";
						} else if(isOnline=="0") { //转码中
							content+="<div class='"+statusClass[4]+"' style='width: 16.66%'>"+ statusName[4] +"</div>";
							content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[0] +"</div>";
							content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 0'>"+ statusName[2] +"</div>";
							content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 0'>"+ statusName[5] +"</div>";
							content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 0'>"+ statusName[1] +"</div>";
							content+="<div class='"+statusClass[3]+"' style='width: 0'>"+ statusName[3] +"</div>";
						} else if(isOnline=="2") {//转码完
							content+="<div class='"+statusClass[4]+"' style='width: 16.66%'>"+ statusName[4] +"</div>";
							content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[0] +"</div>";
							content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[2] +"</div>";
							content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 0'>"+ statusName[5] +"</div>";
							content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 0'>"+ statusName[1] +"</div>";
							content+="<div class='"+statusClass[3]+"' style='width: 0'>"+ statusName[3] +"</div>";
						} else if(isOnline=="5") { //待上线
							content+="<div class='"+statusClass[4]+"' style='width: 16.66%'>"+ statusName[4] +"</div>";
							content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[0] +"</div>";
							content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[2] +"</div>";
							content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[5] +"</div>";
							content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 0'>"+ statusName[1] +"</div>";
							content+="<div class='"+statusClass[3]+"' style='width: 0'>"+ statusName[3] +"</div>";
						} else if(isOnline=="1") {//在线销售
							content+="<div class='"+statusClass[4]+"' style='width: 16.66%'>"+ statusName[4] +"</div>";
							content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[0] +"</div>";
							content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[2] +"</div>";
							content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[5] +"</div>";
							content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[1] +"</div>";
							content+="<div class='"+statusClass[3]+"' style='width: 0'>"+ statusName[3] +"</div>";
						} else if(isOnline=="3") {//下线停售
							content+="<div class='"+statusClass[4]+"' style='width: 16.66%'>"+ statusName[4] +"</div>";
							content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[0] +"</div>";
							content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[2] +"</div>";
							content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[5] +"</div>";
							content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 16.66%'>"+ statusName[1] +"</div>";
							content+="<div class='"+statusClass[3]+"' style='width: 16.66%'>"+ statusName[3] +"</div>";
						} else { //其它
							content+="<div class='"+statusClass[4]+"' style='width: 16.66%'>"+ statusName[4] +"</div>";
							content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 0'>"+ statusName[0] +"</div>";
							content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 0'>"+ statusName[2] +"</div>";
							content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 0'>"+ statusName[5] +"</div>";
							content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 0'>"+ statusName[1] +"</div>";
							content+="<div class='"+statusClass[3]+"' style='width: 0'>"+ statusName[3] +"</div>";
						}
						content+="</div></div></div>";
						return false;//相当于break
					} else {
						flag=false;
					}
				});
				if(!flag){
					content+="<div class='row'><div class='col-sm-2'>"+osName+"</div><div class='col-sm-10'><div class='progress' cOsId='"+osId+"' onclick='changeStatus(this)'>";
					content+="<div class='"+statusClass[4]+"' style='width: 16.66%'>"+ statusName[4] +"</div>";
					content+="<div class='"+statusClass[0]+" progress-bar-striped' style='width: 0'>"+ statusName[0] +"</div>";
					content+="<div class='"+statusClass[2]+" progress-bar-striped' style='width: 0'>"+ statusName[2] +"</div>";
					content+="<div class='"+statusClass[5]+" progress-bar-striped' style='width: 0'>"+ statusName[5] +"</div>";
					content+="<div class='"+statusClass[1]+" progress-bar-striped' style='width: 0'>"+ statusName[1] +"</div>";
					content+="<div class='"+statusClass[3]+"' style='width: 0'>"+ statusName[3] +"</div>";
					content+="</div></div></div>";
				}
				
			}
		});
		$("#content").html(content);
		$("#bookOnlineStateModal").modal("show");
	}
	function changeStatus(arg) {
		var count=0;//计算非空白个数
		$(arg).find(".progress-bar").each(function() {
			if($(this).css("width").replace("px","").replace("%","")=="0") {
				$(this).css("width", "16.66%");
				return false;
			} else {
				count++;
			}
		});
		if(count==6) {
			$(arg).find(".progress-bar").each(function() {
				$(this).css("width", "0");
			});
			//到最后一格后自动选择第一格
			$(arg).find(".progress-bar").each(function(i) {
				$(this).css("width", "16.66%");
				if(i==0) return false;
			});
			//.css("width", "16.66%");
		}
	}
	//
	function updateStatus() {
		var result="<xml bookId='"+updateBookId+"'>";
		$("#content").find(".progress").each(function() {
			var osId = $(this).attr("cOsId");
			var count=0;
			$(this).find(".progress-bar").each(function() {
				if($(this).css("width").replace("px","").replace("%","")=="0") {
					count++;
				}
			});
			count=6-count;//计算填充个数=4-空白
			if(count!=0) {
				count=count-1;
				result=result+"<item osId='"+osId+"' onlineStatus='"+count+"' />";
			}
		});
		result=result+"</xml>";
		
		$.post("updateOnlineStatus.action", {result:result}, function(data) {
			if(data=="-1"){
				alert("XML解析失败："+result);
			} else if(data=="1") {
				alert("保存成功");
				$("#tr_"+updateBookId).find("td:last").empty();
				var tempOut="";
				$(result).find("item").each(function() {
					var onlineStatus = $(this).attr("onlineStatus");
					var osId = $(this).attr("osId");
					if(onlineStatus!="0") {
						$(osXml).find("item").each(function() {
							if($(this).attr("osId")==osId) {
								if(onlineStatus==0) {
									onlineStatus=4;
								} else if(onlineStatus==1){
									onlineStatus=0;
								} else if(onlineStatus==3) {
									onlineStatus=5;
								} else if(onlineStatus==4) {
									onlineStatus=1;
								} else if(onlineStatus==5) {
									onlineStatus=3;
								}
								tempOut+="<p class=\"\"><span class=\"osName "+classArr[onlineStatus]+"\">"+$(this).attr("osName")+ statusName[onlineStatus]+"</span></p>";
							}
						});
					}
				});
				tempOut+="<a href=\"javascript:updateBookOnlineState('"+updateBookId+"')\"><span class=\"badge\">更新</span></a>";
				$("#tr_"+updateBookId).find("td:last").html(tempOut);
			} else if(data=="2") {
				alert("修改失败");
			}
		});
		$("#bookOnlineStateModal").modal("hide");
		
	}
	
	function initQuery1() {
		var query1="<option value='0'>请选择</option>";
		$(osXml).find("item").each(function() {
			query1+="<option value='"+$(this).attr("osId")+"'>"+$(this).attr("osName")+"</option>";			
		});
		$("#queryOs").html(query1);
		$("#queryOsStatus").html("<option value='-1'>请选择</option>");
	}
	
	</script>
</head>
<body>
	<input type="hidden" value="0" name="theBookId" id="theBookId" />
	<div class="container" id="container">
		<div class="row">
			<div class="col-sm-6">
				<div class="input-group">
					<div class="input-group-btn">
						<button type="button" class="btn btn-default dropdown-toggle" style="width: 9em;" data-toggle="dropdown">
							<span class="searchTypeSpan">检索类别</span> <span class="caret"></span>
						</button>
						<ul id="searchCondition" class="dropdown-menu">
						</ul>
					</div>
					<input type="text" class="form-control searchContent" value="" placeholder="输入关键词" />

					<div class="input-group-btn">
						<button class="btn btn-default dropdown-toggle" onclick="search()" type="button">
							<span class="glyphicon glyphicon-search"></span>&nbsp;检索
						</button>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="col-sm-6">
					<select class='form-control' id='queryOs'></select>
				</div>
				<div class="col-sm-6 ">
					<select class='form-control' id='queryOsStatus'></select>
				</div>
			</div>
		</div>
		<div class="row" id="sdate">
			<div class="col-sm-8">
				<div class="col-sm-5">
					<div class="input-group date form_date" data-date="" data-date-format="yyyy-MM">
						<input class="form-control ptime_start" size="16" placeholder="出版时间--起始" type="text" value="" readonly> <span class="input-group-addon"> <span class="glyphicon glyphicon-remove"></span>
						</span> <span class="input-group-addon"> <span class="glyphicon glyphicon-calendar"></span>
						</span>
					</div>
				</div>
				<div class="col-sm-5">
					<div class="input-group date form_date" data-date="" data-date-format="yyyy-MM">
						<input class="form-control ptime_end" size="16" placeholder="出版时间--结束" type="text" value="" readonly> <span class="input-group-addon"> <span class="glyphicon glyphicon-remove"></span>
						</span> <span class="input-group-addon"> <span class="glyphicon glyphicon-calendar"></span>
						</span>
					</div>
				</div>
			</div>
		</div>
		<div class="row content">
			<table class="table table-bordered table-hover table-condensed">
				<thead>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
					<tr>
						<td id="colspanAttr">
							<div class="pull-left">
								<span id="tempCount"></span>当前第[<span id="currentPageSpan"><s:property value="currentPage" /></span>]页，共[<span id="pageCountSpan"><s:property value="pageCount" /></span>]页，每页显示[<span id="pageRowCountSpan"><s:property value="pageRowCount" /></span>]条
							</div>
							<div class="pull-right">
								<!-- <a href="javascript:firstPage()" class="btn btn-info" id="firstPageTag" type="button">第一页</a> -->
								<a href="javascript:prePage()" class="btn btn-default" type="button" id="prePageTag">&larr; 上一页 </a><a href="javascript:nextPage()" class="btn btn-default" type="button" id="nextPageTag">下一页 &rarr;</a>&nbsp;&nbsp;
								<!-- <a href="javascript:endPage()" class="btn btn-info" id="endPageTag" type="button">最后一页</a>&nbsp;&nbsp; -->
								跳至<input type="text" class="pageInput" />页<a href="javascript:jumpPage();" class="btn btn-default" type="button">转</a>
							</div>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
	
	<div class="modal fade bs-example-modal-lg" id="bookOnlineStateModal" tabindex="-1" aria-labelledby="bookOnlineStateModalLabel" aria-hidden="true">
    	<div class="modal-dialog modal-lg">
    		<div class="modal-content">
    			<div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myOsModalLabel">图书状态修改</h4>
                </div>
	    		<div class="modal-body" id="content"></div>
	    		<div class="modal-footer">
	    			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-info" onclick="updateStatus()">保存修改</button>
	    		</div>
    		</div>
    	</div>
    </div>
    
    
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
