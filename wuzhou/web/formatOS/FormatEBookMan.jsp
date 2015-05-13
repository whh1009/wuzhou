<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../css/bootstrap-datetimepicker.min.css">
    <style>
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
            -webkit-transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
            transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
            vertical-align: middle;
            padding: 0 10;
            color: #555;
            height: 32px;
            width: 3em;
        }

        #myBookListModal .modal-dialog {
            width: 80%;
        }
    </style>
    <script type="text/javascript">
        var bookIds = ""; //记录添加到收藏夹的图书id
        var selectedBookCount = 0; //记录选择添加到收藏夹的图书数量
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
        $(function () {
            $("#tempBookList").html(selectedBookCount);
            //column = "<root><item cname='图书编号' ename='book_serial_number' /><item cname='书名（中文）' ename='book_name_cn' /><item cname='文种' ename='book_language' /><item cname='丛书名（中文）' ename='book_series_cn' /></root>";
            column = "<s:property value='showColumn' />";
            column = column.replace(/&lt;/ig, "<").replace(/&gt;/ig, ">");
            if (!column) {
                document.getElementById("container").style.display = 'none';
            }
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
            $(".ptime_start").change(function () {
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
                    function () {
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
        });

        //初始化显示的表头字段
        function intShowColumn() {
            try {
                var count = 0;
                var tHead = "<tr>";
                $(column).find("item").each(function () {
                    tHead = tHead + "<th>" + $(this).attr("cname") + "</th>";
                    count++;
                });
                tHead = tHead
                + "<th align='center' style='width:80px'>操作</th></tr>";
                $(".row table thead").html(tHead);
                $("#colspanAttr").prop("colspan", count + 1);
            } catch (e) {
                alert(e.message);
            }
        }
        //初始化检索字段
        function initSearchColumn() {
            var searchLi = "";
            $(column).find("searchItem").each(
                    function () {
                        searchLi = searchLi
                        + "<li><a href='javascript:searchTypeBtn(\""
                        + $(this).attr("cname") + "\");'>"
                        + $(this).attr("cname") + "</a>";
                    });
            $("#searchCondition").html(searchLi);
        }

        /**
         * 获取图书信息
         */
        function getAllBookList(page, searchType, searchContent) {
            $
                    .ajax({
                        url: 'getBookListByCondition.action',
                        type: 'post',
                        async: false,
                        data: {
                            page: page,
                            searchType: searchType,
                            searchContent: searchContent
                        },
                        success: function (data) {
                            var json = eval('(' + data + ')');
                            if (json.bookList.length == 0) {
                                $(".row table tbody")
                                        .html(
                                        "<span style='color:red;font-size:8pt;'>没有找到图书！</span>");
                            } else {
                                var items = $(column).find("item");
                                var tableStr = "";
                                for (var i = 0; i < json.bookList.length; i++) {
                                    tableStr = tableStr + "<tr id='tr_" + json.bookList[i].book_id + "'>";
                                    for (var j = 0; j < items.length; j++) {
                                        var ename = $(items[j]).attr("ename");
                                        if (ename == "book_paper_price" || ename == "book_ebook_price"||ename=="book_paper_dollar_price"||ename=="book_ebook_dollar_price") {
                                            tableStr = tableStr + "<td>" + json.bookList[i][ename].toFixed(2) + "</td>";
                                        } else {
                                            tableStr = tableStr + "<td>"
                                            + json.bookList[i][ename]
                                            + "</td>";
                                        }
                                    }
                                    var array = bookIds.split(",");
                                    var flag = false;
                                    for (var j = 0; j < array.length; j++) {
                                        if (array[j] == json.bookList[i].book_id) {
                                            flag = true;
                                            break;
                                        } else {
                                            flag = false;
                                        }
                                    }
                                    if (flag) {
                                        tableStr = tableStr + "<td align='center' style='width:40px'></td><tr>";
                                    } else {
                                        tableStr = tableStr + "<td align='center' style='width:40px'><a class=\"text-info\" id=\"a_" + json.bookList[i].book_id + "\" title=\"添加至收藏夹中\" href=\"javascript:addBookToFormatOS('" + json.bookList[i].book_id + "');\"><span class=\"glyphicon glyphicon-shopping-cart\" id='span_" + json.bookList[i].book_id + "'></span></a></td><tr>";
                                    }
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
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            alert(XMLHttpRequest.readyState + XMLHttpRequest.status
                            + XMLHttpRequest.responseText);
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
        /////////////////////////////////////////////////////////////////////////////////////////////////
        //把图书添加到格式化列表中
        function addBookToFormatOS(bookId) {
            var shopOffset = $("#tempBookList").offset();
            var cloneDiv = $("#span_" + bookId).clone();
            var proOffset = $("#span_" + bookId).offset();
            cloneDiv.css({"position": "absolute", "top": proOffset.top, "left": proOffset.left});
            $("#span_" + bookId).parent().append(cloneDiv);

            cloneDiv.animate({
                left: shopOffset.left,
                top: shopOffset.top
            }, "slow", function (data) {
                $("#a_" + bookId).hide();
                cloneDiv.css("display", "none");
                initBookCount();
                bookIds += bookId + ",";
            });
        }

        function initBookCount() {
            //加入收藏夹 图书计数+1
            selectedBookCount++;
            $("#tempBookList").html(selectedBookCount);
        }

        //转换车
        function zhuanhuanche() {
            if (bookIds == "") {
                alert("请先添加图书");
            } else {
                $("#bookListContent").html("");
                $.ajax({
                    url: "getBookListByBookIds.action",
                    type: "post",
                    dataType: "json",
                    async: true,
                    data: {bookIds: bookIds},
                    success: function (data) {
                        if (data == "0") {
                            alert("没有获取到图书编号");
                        } else {
                            var tableContent = "<table class='table table-bordered table-hover'>";
                            tableContent += "<thead><tr><th>图书编号</th><th>书名（中文）</th><th>著者（中文）</th><th>文种</th><th>丛书（中文）</th><th>建议分类1</th></tr></thead><tbody>";
                            for (var i = 0; i < data.length; i++) {
                                tableContent += "<tr><td>" + data[i].book_serial_number + "</td><td>" + data[i].book_name_cn + "</td><td>" + data[i].book_author + "</td><td>" + data[i].book_language + "</td><td>" + data[i].book_series_cn + "</td><td>" + data[i].book_category1 + "</td></tr>";
                            }
                            tableContent += "</tbody></table>";
                            tableContent += "<button class='btn btn-success btn-block btn-lg' id='formatBookBtn' data-loading-text='转换中...' onclick='formatBookById()'>生成各平台数据格式规范</button>";
                            $("#bookListContent").html(tableContent);
                            $("#myBookListModal").modal({show: true});
                        }
                    }
                });
            }
        }

        // 转换
        function formatBookById() {
            $("#formatBookBtn").button("loading");
            $.ajax({
                url: "formatEBookByBookId.action",
                type: "post",
                async: true,
                data: {bookIds: bookIds},
                beforeSend:function(){
                    $("body").showLoading();
                },
                success: function (data) {
                    $("body").hideLoading();
                    if (data == "0") {
                        alert("没有获取到图书ID");
                    } else if (data == "1") {
                        alert("生成文件成功");
                        $("#myBookListModal").modal('hide');
                    } else {
                        alert("生成文件失败");
                    }
                    $("#formatBookBtn").button("reset");
                },
                error:function(XMLHttpRequest, textStatus, errorThrown) {
                    $("body").hideLoading();
                    alert("转换失败："+textStatus+" "+errorThrown);
                }
            });
        }

    </script>

</head>

<body>


<input type="hidden" value="0" name="theBookId" id="theBookId"/>

<div class="container" id="container">
    <div class="row">
        <div class="col-sm-3">
            <button type="button" class="btn btn-warning btn-lg" onclick="zhuanhuanche()">转换车 <sup><span class="badge"
                                                                                                         id="tempBookList">0</span></sup>
            </button>
        </div>
        <div class="col-sm-9">
            <div class="input-group">
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default dropdown-toggle" style="width: 9em;"
                            data-toggle="dropdown">
                        <span class="searchTypeSpan">检索类别</span> <span class="caret"></span>
                    </button>
                    <ul id="searchCondition" class="dropdown-menu">
                    </ul>
                </div>
                <input type="text" class="form-control searchContent" value="" placeholder="输入关键词"/>

                <div class="input-group-btn">
                    <button class="btn btn-default dropdown-toggle" onclick="search()" type="button">
                        <span class="glyphicon glyphicon-search"></span>&nbsp;检索
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="row" id="sdate">
        <div class="col-sm-8 col-sm-offset-4">
            <div class="col-sm-5">
                <div class="input-group date form_date" data-date="" data-date-format="yyyy-MM">
                    <input class="form-control ptime_start" size="16" placeholder="出版时间--起始" type="text" value=""
                           readonly> <span class="input-group-addon"> <span class="glyphicon glyphicon-remove"></span>
						</span> <span class="input-group-addon"> <span class="glyphicon glyphicon-calendar"></span>
						</span>
                </div>
            </div>
            <div class="col-sm-5">
                <div class="input-group date form_date" data-date="" data-date-format="yyyy-MM">
                    <input class="form-control ptime_end" size="16" placeholder="出版时间--结束" type="text" value=""
                           readonly> <span class="input-group-addon"> <span class="glyphicon glyphicon-remove"></span>
						</span> <span class="input-group-addon"> <span class="glyphicon glyphicon-calendar"></span>
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
            <tfoot>
            <tr>
                <td id="colspanAttr">
                    <div class="pull-left">
                        当前第[<span id="currentPageSpan"><s:property value="currentPage"/></span>]页，共[<span
                            id="pageCountSpan"><s:property value="pageCount"/></span>]页，每页显示[<span
                            id="pageRowCountSpan"><s:property value="pageRowCount"/></span>]条
                    </div>
                    <div class="pull-right">
                        <!-- <a href="javascript:firstPage()" class="btn btn-info" id="firstPageTag" type="button">第一页</a> -->
                        <a href="javascript:prePage()" class="btn btn-default" type="button" id="prePageTag">&larr;
                            上一页 </a><a href="javascript:nextPage()" class="btn btn-default" type="button"
                                       id="nextPageTag">下一页 &rarr;</a>&nbsp;&nbsp;
                        <!-- <a href="javascript:endPage()" class="btn btn-info" id="endPageTag" type="button">最后一页</a>&nbsp;&nbsp; -->
                        跳至<input type="text" class="pageInput"/>页<a href="javascript:jumpPage();"
                                                                    class="btn btn-default" type="button">转</a>
                    </div>
                </td>
            </tr>
            </tfoot>
        </table>
    </div>
</div>

<br/>
<!-- 模态框 -->
<div class="modal fade in" id="myBookListModal" tabindex="-1" role="dialog" aria-labelledby="myBookListModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myBookListModalLabel">转换书目列表</h4>
            </div>
            <div class="modal-body" id="bookListContent">

            </div>
        </div>
    </div>
</div>
<script src="../js/bootstrap-datetimepicker.min.js"></script>
<script src="../js/bootstrap-datetimepicker.zh-CN.js" charset="UTF-8"></script>
<script type="text/javascript">

    $(function () {
        $(".form_date").datetimepicker({
            language: "zh-CN",
            weekStart: 1,
            todayBtn: 1,
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
