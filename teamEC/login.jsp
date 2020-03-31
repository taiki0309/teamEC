<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/galaxy.css">
<link rel="stylesheet" href="./css/style.css">
<link rel="stylesheet" href="./css/table.css">
<link rel="stylesheet" type="text/css" href="./css/header.css">
<title>Login画面</title>
</head>
<body>
<jsp:include page="header.jsp" />
<div id="contents">
<h1>ログイン画面</h1>

<s:if test="userIdErrorMessageList!=null && userIdErrorMessageList.size()>0">
		<div class="error">
				<div class="error-message">
						<s:iterator value="userIdErrorMessageList"><s:property /><br></s:iterator>
				</div>
		</div>
</s:if>
<s:if test="passwordErrorMessageList!=null && passwordErrorMessageList.size()>0">
		<div class="error">
				<div class="error-message">
						<s:iterator value="passwordErrorMessageList"><s:property/><br></s:iterator>
				</div>
		</div>
</s:if>

<s:if test="isNotUserInfoMessage!=null && !isNotUserInfoMessage.isEmpty()">
		<div class="error">
				<div class="error-message">
						<s:property  value="isNotUserInfoMessage"/>
				</div>
		</div>
</s:if>

<s:form action="LoginAction">
		<table class="box-table">
				<tr>
						<th scope="row"><s:label value="ユーザーID"/></th>
						<s:if test="#session.idFlg==true">
						<td><s:textfield name="userId" class="txt" placeholder="ユーザーID" value='%{#session.userId}' autocomplete="off"/></td>
						</s:if>
						<s:else>
						<td><s:textfield name="userId" class="txt" placeholder="ユーザーID" value='%{userId}' autocomplete="off"/></td>
						</s:else>
				</tr>
				<tr>
						<th scope="row"><s:label value="パスワード"/></th>
						<td><s:password name="password" class="txt" placeholder="パスワード" autocomplete="off"/></td>
				</tr>
		</table>
		<div class="box">
				<s:if test="(#session.idFlg==true && #session.userId!=null && !#session.userId.isEmpty()) || idFlg==true">
						<s:checkbox name="idFlg" checked="checked"/>
				</s:if>
				<s:else>
						<s:checkbox name="idFlg"/>
				</s:else>
				<s:label value="ユーザーID保存"/><br>
		</div>
		<div class="submit_btn_box">
						<s:submit value="ログイン" class="submit_btn"/>
		</div>
</s:form>

<s:form action="CreateUserAction">
<div class="submit_btn_box">
		<div id="contents-btn-set">
				<s:submit value="新規ユーザー登録" class="submit_btn" />
		</div>
</div>
</s:form>
<s:form action="ResetPasswordAction">
<div class="submit_btn_box">
<div id="contents-btn-set">
				<s:submit value="パスワード再設定" class="submit_btn" />
</div>
</div>
</s:form>
</div>

</body>
</html>