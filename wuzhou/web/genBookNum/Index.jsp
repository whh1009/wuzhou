<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="../css/bootstrap.min.css" rel="stylesheet"/>
    <link href="css/bootstrap-wizard.css" rel="stylesheet"/>
    <script src="../js/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="js/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/bootstrap-wizard.min.js" type="text/javascript"></script>
    <title></title>
    <script>
        $(function() {
            var wizard = $("#some-wizard").wizard({
                    keyboard: false,
                    contentHeight: 700,
                    contentWidth: 800,
                    backdrop: 'static'
            });
            wizard.show();

            $('#press').on('input', function() {
                if ($(this).val().length > 0) {
                    $('#press').parents('.form-group').removeClass('has-error has-success');
                }
            });
            //初始化
            //$("#press").val("五洲传播出版社");
            $("#benshe").on('click', function() {
                $("#press").val("五洲传播出版社");
            });
            $("#qita").on('click', function() {
                $("#press").val("");
            });


        });


        function validatePress(el) {
            var name = el.val();
            var retValue = {};
            if (name == "") {
                retValue.status = false;
                retValue.msg = "请输入出版社名称";
            } else {
                retValue.status = true;
            }
            return retValue;
        }
    </script>
</head>
<body>
<div class="wizard" id="some-wizard" data-title="书号创建">
    <div class="wizard-card" data-cardname="card1">
        <h3>出版社</h3>
        <div class="wizard-input-section">
            <div class="radio">
                <label>
                    <input type="radio" name="optionsRadios" id="benshe" value="bs"> 五洲
                </label>
            </div>
            <div class="radio disabled">
                <label>
                    <input type="radio" name="optionsRadios" id="qita" value="qt" checked> 其他社
                </label>
            </div>
        </div>
        <div class="wizard-input-section">
            <div class="form-group">
                <div class="col-sm-8">
                    <input type="text" class="form-control" id="press" name="press" placeholder="出版社名称" data-validate="validatePress" data-is-valid="0" data-lookup="0" />
                </div>
            </div>
        </div>
    </div>
    <div class="wizard-card" data-cardname="card2">
        <h3>出版时间</h3>
        <div class="wizard-input-section">
            <select id="publishTime" class="form-control">
                <option value="015" selected>2015</option>
                <option value="014">2014</option>
                <option value="013">2013</option>
                <option value="012">2012</option>
                <option value="011">2011</option>
                <option value="010">2010</option>
                <option value="009">2009</option>
            </select>
        </div>
    </div>
    <div class="wizard-card" data-cardname="card3">
        <h3>图书 & 期刊</h3>
        Some content
    </div>
    <div class="wizard-card" data-cardname="card4">
        <h3>语种</h3>
        <div class="col-sm-4">
            <div class="input-group">
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default dropdown-toggle input-group-myaddon twz" data-toggle="dropdown">
                        <span class="bitian zbitian wbitian">文种 <span class="caret"></span></span>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:closesydy();initWenzhong('yw','001');">001--英文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('xw','002');">002--西文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('zw','003');">003--中文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('fw','004');">004--法文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('dy','005');">005--德语</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('ay','006');">006--阿语</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('ey','007');">007--俄语</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('tw','008');">008--土文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('ry','009');">009--日语</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('hy','010');">010--韩语</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('ydly','011');">011--意大利语</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('ynw','012');">012--印尼文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('hskstw','013');">013--哈萨克斯坦文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('mw','014');">014--蒙文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('zw','015');">015--藏文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('bs','016');">016--波斯文</a></li>
                        <li><a href="javascript:closesydy();initWenzhong('bs','017');">017--柯尔克孜文</a></li>
                        <li><a href="javascript:opensydy();initWenzhong('sydy','500');">500--双语对应</a></li>
                    </ul>
                </div>
                <input type="text" class="form-control" name="bookEntity.book_language" id="wenzhong" placeholder="请选择" readonly>
            </div>
        </div>
    </div>
</div>
</body>
</html>
