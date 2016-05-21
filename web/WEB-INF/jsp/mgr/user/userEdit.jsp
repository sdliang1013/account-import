<%@ page language="java" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/mgr/layout/import.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>企业管理</title>
    <%@ include file="/WEB-INF/jsp/mgr/layout/header.jsp" %>
    <script type="text/javascript" src="${path}js/common/AESUtil.js"></script>
    <script type="text/javascript" src="${path}js/comp/EntityForm.js"></script>
    <script type="text/javascript" src="${path}js/user/User.js"></script>
    <script type="text/javascript">
        var userEdit = new user.UserEdit();
        $(document).ready(userEdit.init);
    </script>
</head>
<body>
<div class="easyui-layout">
    <form id="formUserEdit" method="post">
        <input type="hidden" id="hdnId" name="id" value="${param.id}"/>
        <input type="hidden" id="hdnPwd" name="password"/>

        <div class="div-row">
            <div class="label2">账号：</div>
            <div class="input2">
                <input class="easyui-textbox" type="text" id="txtUserName" name="userName"
                       data-options="{required:true}" readonly/>
            </div>
        </div>
        <div class="div-row">
            <div class="label2">姓名：</div>
            <div class="input2">
                <input class="easyui-textbox" type="text" id="txtRealName" name="realName"/>
            </div>
        </div>
        <div class="div-row">
            <div class="label2">密码：</div>
            <div class="input2">
                <input type="password" class="easyui-textbox" id="pwd" name="pwdClone"/>
            </div>
        </div>

        <div class="div-row">
            <div class="label">
                <a id="btnSave" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
            </div>
        </div>
    </form>
</div>
</body>
</html>
