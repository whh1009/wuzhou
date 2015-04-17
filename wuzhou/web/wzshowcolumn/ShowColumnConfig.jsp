<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html>
<head>
<s:head />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>五洲传播--图书信息显示字段配置</title>
<script type="text/javascript">

</script>
</head>
<body>
	<div class="container" id='container'>
		<div class="row">
			<div class="alert alert-danger fade in">
				<!-- <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> -->
           		<strong>建议不要超过 10 个显示字段</strong></span>
           	</div>
		</div>
		<div class="row">
			<s:form action="updateShowColumnconfig.action" method="post">
				<s:optiontransferselect list="leftShowList" label=""
					doubleList="rightShowList" name="leftShowString"
					doubleName="rightShowString" addToLeftLabel=" <<< "
					addToRightLabel=" >>> " rightDownLabel="向下" rightUpLabel="向上"
					multiple="false" doubleMultiple="true" allowAddAllToLeft="false"
					allowAddAllToRight="false" allowSelectAll="false"
					allowUpDownOnLeft="false" doubleCssClass="form-control"
					cssClass="form-control" 
					buttonCssClass="btn btn-primary" cssErrorClass="btn btn-danger"
					cssStyle="height:478px; width:300px;"
					doubleCssStyle="height:450px; width:300px;" leftTitle="未配置的显示字段"
					rightTitle="已配置的显示字段">
				</s:optiontransferselect>
				<s:submit value="保存" cssClass="btn btn-primary"></s:submit>
			</s:form>
		</div>
	</div>
</body>
</html>