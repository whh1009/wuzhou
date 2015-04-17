<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html>
<head>
<s:head />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>五洲传播--图书信息导出字段配置</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<!-- allowUpDownOnLeft="true" -->
			<s:form action="updateExportColumnConfig.action" method="post">
				<s:optiontransferselect list="leftExportList" label=""
					doubleList="rightExportList" name="leftExportString"
					doubleName="rightExportString" addToLeftLabel=" <<< "
					addToRightLabel=" >>> " rightDownLabel="向下" rightUpLabel="向上"
					allowUpDownOnLeft="false"
					multiple="false" doubleMultiple="true" allowAddAllToLeft="false"
					allowAddAllToRight="false" allowSelectAll="false"
					cssClass="form-control" doubleCssClass="form-control"
					buttonCssClass="btn btn-primary" cssErrorClass="btn btn-danger"
					cssStyle="height:478px; width:300px;"
					doubleCssStyle="height:450px; width:300px;" leftTitle="未配置的导出字段"
					rightTitle="已配置的导出字段">
				</s:optiontransferselect>
				<s:submit value="保存" cssClass="btn btn-primary"></s:submit>
			</s:form>
		</div>
	</div>
</body>
</html>