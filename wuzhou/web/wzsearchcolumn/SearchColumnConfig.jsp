<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../wzbase/Include.jsp" %>
<!DOCTYPE html>
<html>
<head>
<s:head />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<title>五洲传播--图书信息检索条件配置</title>
</head>
<body>
	<div class="container">
		<div class="row">
			<s:form action="updateSearchColumnConfig.action" method="post">
				<s:optiontransferselect list="leftSearchList" label=""
					doubleList="rightSearchList" name="leftSearchString"
					doubleName="rightSearchString" addToLeftLabel=" <<< "
					addToRightLabel=" >>> " rightDownLabel="向下" rightUpLabel="向上"
					multiple="false" doubleMultiple="true" allowAddAllToLeft="false"
					allowAddAllToRight="false" allowSelectAll="false"
					allowUpDownOnLeft="false" 
					cssClass="form-control" doubleCssClass="form-control"
					buttonCssClass="btn btn-primary" cssErrorClass="btn btn-danger"
					cssStyle="height:478px; width:300px;"
					doubleCssStyle="height:450px; width:300px;" leftTitle="未配置的检索字段"
					rightTitle="已配置的检索字段">
				</s:optiontransferselect>
				<s:submit value="保存" cssClass="btn btn-primary"></s:submit>
			</s:form>

		</div>
	</div>
</body>
</html>