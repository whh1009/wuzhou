<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ include file="Include.jsp" %>
<html>
<head>
    <title>五洲传播--首页</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <link rel="shortcut icon" href="../images/favicon.ico"/>
    <script src="<%=basePath%>/js/Chart.js"></script>
<script>

    function changeYear(arg){
        var year = $(arg).val();
        initBookPublishTimePic(year);
    }
</script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6">
            <h4>文种汇总图：</h4>
            <div style="width: 80%">
                <canvas id="bookLanPic" height="450" width="500"></canvas>
            </div>
        </div>
        <div class="col-sm-6">
            <h4 class="col-sm-3">出版时间： </h4>
            <div class="col-sm-9">
                <select class=" form-control" onchange="changeYear(this)">
                    <option value="2015">2015年</option>
                    <option value="2014">2014年</option>
                    <option value="2013">2013年</option>
                    <option value="2012">2012年</option>
                    <option value="2011">2011年</option>
                    <option value="2010">2010年</option>
                    <option value="2009">2009年</option>
                    <option value="2008">2008年</option>
                    <option value="2007">2007年</option>
                    <option value="2006">2006年</option>
                    <option value="2005">2005年</option>
                    <option value="2004">2004年</option>
                    <option value="2003">2003年</option>
                    <option value="2002">2002年</option>
                </select>
            </div>
            <div style="width: 100%" id="temp">
                <canvas id="bookPublishTimePic" height="450" width="600"></canvas>
            </div>
        </div>
    </div>
    <div id="test"></div>

    <script>
        $(function () {
            initBookLanPic();
            initBookPublishTimePic();
        });

        //初始化图书语种图 -- 饼状图
        function initBookLanPic() {
            $.ajax({
                url: "bookLanDoughnutChart.action",
                method: "post",
                beforeSend: function (XMLHttpRequest) {
                    $("#bookLanPic").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
                },
                success: function (data) {
                    $("#bookLanPic").empty();
                    var pieData = eval('(' + data + ')'); //json字符串转json对象
                    //var ctx = document.getElementById("bookLanPic").getContext("2d");
                    //window.myDoughnut = new Chart(ctx).Doughnut(doughnutData, {responsive: true});
                    var ctx = document.getElementById("bookLanPic").getContext("2d");
                    window.myPie = new Chart(ctx).Pie(pieData);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("#bookLanPic").html("<p class='text-danger'>" + textStatus + "  " + errorThrown + "</p>");
                }
            });
        }

        //图书出版时间图 -- 柱形图
        function initBookPublishTimePic(year) {
            $.ajax({
                url: "bookPublishTimeChart.action",
                method: "post",
                data:{bookPublishTime:year},
                beforeSend: function (XMLHttpRequest) {
                    $("#bookPublishTimePic").html("<div class='aa'><img src='<%=basePath %>images/loading.gif' /></div>");
                },
                success: function (data) {
                    //删除再添加，否则会跳出之前已经加载的柱状图
                    $("#bookPublishTimePic").remove();
                    $("#temp").append("<canvas id=\"bookPublishTimePic\" height=\"450\" width=\"600\"></canvas>");
                    var barChartData = eval('(' + data + ')'); //json字符串转json对象
                    var ctx = document.getElementById("bookPublishTimePic").getContext("2d");
                    window.myBar = new Chart(ctx).Bar(barChartData, {responsive : true});
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    $("#bookPublishTimePic").html("<p class='text-danger'>" + textStatus + "  " + errorThrown + "</p>");
                }
            });
        }
    </script>
</div>


</body>
</html>