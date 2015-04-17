<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">

    <title>自定义更新图书信息</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

    <link rel="stylesheet" type="text/css" href="<%=basePath%>css/bootstrap.min.css">
    <script type="text/javascript" src="<%=basePath%>js/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/bootstrap.min.js"></script>

    <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
    <style type="text/css">
        .aa {
            align: center;
        }

        .aa img {
            display: block;
            margin: auto;
        }
        .modal-lg {
            width:900px;
        }
    </style>
    <script type="text/javascript">
        var tableColumnMap = "";
        var splitStr="→";
        $(function () {
            //初始化tooltip
            $('[data-toggle="tooltip"]').tooltip();
            $("#nextBtn").hide();
            //获取数据库中图书字段信息
            $.ajax({
                url: "getBookInfoXml.action",
                async: true,
                success: function (data) {
                    var radioStr = "";
                    var i = 0;
                    $(data).find("item").each(function () {
                        if (i % 5 == 0) {
                            if (i > 0) {
                                radioStr += "</p><p>";
                            } else {
                                radioStr += "<p>";
                            }
                        }
                        radioStr += "<label class='radio-inline'><input type='radio' name='radioOptions' value='" + $(this).attr("ename") + "' > <span>" + $(this).attr("cname") + "</span></label>";
                        i++;
                    });
                    radioStr += "</p>";
                    tableColumnMap = radioStr;
                }
            });
        });

        var currentBtnObj;//缓存当前按钮
        function showTableColumn(arg) {
            currentBtnObj = arg;
            $("#tableMap").empty();
            $("#tableMap").html(tableColumnMap);
            if(typeof($(arg).attr("cusData"))!="undefined"){
                $("input[name='radioOptions'][value="+$(arg).attr('cusData')+"]").attr("checked",true);
            }
            $("#myModal").modal("show");
        }

        function setSheetTableMap() {
            $("#myModal").modal("hide");
            var ckv = $('input:radio[name=radioOptions]:checked').val();
            var ckh = $('input:radio[name=radioOptions]:checked + span').text();
            $(currentBtnObj).html($(currentBtnObj).html()+splitStr+ckh);
            $(currentBtnObj).attr("cusData", ckv);
        }
        function clearSheetTableMap() {
            var btnVal = $(currentBtnObj).html();
            $(currentBtnObj).html(btnVal.substr(0, btnVal.indexOf(splitStr)));
            $(currentBtnObj).attr("cusData", "");
        }

        function loadExcel() {
            $.ajax({
                url: "getSheetsByBookInfoExcel.action",
                type: 'POST',
                beforeSend: function (XMLHttpRequest) {
                    //alert('远程调用开始...');
                    $("#excelSheet").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
                },
                success: function (data) {
                    $("#excelSheet").html(data);
                    $("#nextBtn").show();
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("#excelSheet").html("<p class='text-danger'>" + textStatus + "  " + errorThrown + "</p>");
                }
            });
        }

        function updateBookInfo() {
            var myXml="<root>";
            var sheetIds = "";
            $("button[name='excelSheet']").each(function () {
                var cusData = $(this).attr("cusData");
                if(cusData!="") {
                    sheetIds+=$(this).attr("cusData2")+",";
                    myXml+="<item updateColumnName='"+cusData+"' sheetId='"+$(this).attr("cusData2")+"' />";
                }
            });
            myXml+="</root>";
            if (sheetIds.replace(/,/g, "") == "") return;
            $.ajax({
                url: "updateBookInfoByExcel.action",
                data: {myXml: myXml},
                type: "post",
                beforeSend: function (XMLHttpRequest) {
                    //alert('远程调用开始...');
                    $("#content").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
                },
                success: function (data) {
                    $("#content").html(data);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("#content").html("<p class='text-danger'>" + textStatus + "  " + errorThrown + "</p>");
                }
            });
        }

    </script>
</head>

<body>
<div class="container">
    <br/>
    <br/>
    <center><h2>自定义图书信息更新</h2></center>
    <br/>

    <div class="row">
        <div class="col-sm-12">
            <button class="btn btn-lg btn-success btnFold" onclick="loadExcel()" data-toggle="tooltip"
                    data-placement="top" title="加载工作簿"> &#9312; 加载EXCEL
            </button>
        </div>
    </div>
    <div class="row">
        <p>&nbsp;</p>

        <div id="excelSheet"></div>
    </div>
    <hr/>
    <div class="row" id="nextBtn">
        <div class="col-sm-12">
            <button type="button" class="btn btn-lg btn-warning" onclick="updateBookInfo()"
                    data-toggle="tooltip" data-placement="top" title="请勾选的工作簿要更新的对应字段"> &#9313; 更新图书信息
            </button>
        </div>
    </div>

    <div class="row">
        <div style="margin-top:2em;" id="content"></div>
    </div>

    <div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog  modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">选择要更新的字段</h4>
                </div>
                <div class="modal-body" id="tableMap">

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal" onclick="clearSheetTableMap()">清空</button>
                    <button type="button" class="btn btn-primary" onclick="setSheetTableMap()">确定</button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
