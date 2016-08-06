<%@ page language="java" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/mgr/layout/import.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>记录管理</title>
    <%@ include file="/WEB-INF/jsp/mgr/layout/header.jsp" %>
    <script type="text/javascript" src="${path}js/comp/EntityForm.js"></script>
    <script type="text/javascript" src="${path}js/account/Account.js"></script>
    <script type="text/javascript">
        var accountEdit = new account.AccountEdit();
        $(document).ready(accountEdit.init);
    </script>
</head>
<body>
<div class="easyui-layout">
    <form id="formAccountEdit" method="post">
        <input type="hidden" id="hdnId" name="id" value="${param.id}"/>

        <div class="div-row">
            <div class="label2">账号：</div>
            <div class="input2">
                <input class="easyui-textbox" type="text" id="txtAccountName" name="accountName"/>
            </div>
        </div>
        <div class="div-row">
            <div class="label2">QQ：</div>
            <div class="input2">
                <input class="easyui-textbox" type="text" id="txtQq" name="qq"/>
            </div>
        </div>
        <div class="div-row">
            <div class="label2">手机号：</div>
            <div class="input2">
                <input class="easyui-textbox" id="mobile" name="mobile"/>
            </div>
        </div>
        <div class="div-row">
            <div class="label2">彩金：</div>
            <div class="input2">
                <input class="easyui-numberbox" id="handsel" name="handsel"/>
            </div>
        </div>

        <div class="xx-btnBottom">
            <a id="btnSave" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
        </div>
    </form>
</div>
</body>
</html>
