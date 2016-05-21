<%@ page language="java" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/mgr/layout/import.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>记录管理</title>
    <%@ include file="/WEB-INF/jsp/mgr/layout/header.jsp" %>
    <script type="text/javascript" src="${path}js/common/easyui.datagrid.extend.js"></script>
    <script type="text/javascript" src="${path}js/account/Account.js"></script>
    <script type="text/javascript">
        var accountMgr = new account.AccountMgr();
        $(document).ready(accountMgr.init);
    </script>
</head>
<body>
<div class="easyui-layout" style="height: 100%;width: 100%;">
    <div id="divAccountList" data-options="region:'center'" class="easyui-panel">
        <div id="listLayout" class="easyui-layout" style="width:100%; height: 100%">
            <div data-options="region:'north',title:'查询条件',split:false" style="height:80px;">
                <form id="formAccountList">
                    <div class="div-row">
                        <div class="label6">QQ</div>
                        <div class="input6">
                            <input class="easyui-textbox" name="qq" id="qq"/>
                        </div>
                        <div class="label6">手机号</div>
                        <div class="input6">
                            <input class="easyui-textbox" name="mobile" id="mobile"/>
                        </div>
                        <div class="label6">
                            <a id="btnSearch" href="#" class="easyui-linkbutton" data-options="{iconCls:'icon-search'}">查
                                询</a>
                        </div>
                    </div>
                </form>
            </div>
            <div data-options="region:'center',title:'查询结果'" style="background:#eee;">
                <table id="accountGrid"></table>
            </div>
        </div>
    </div>
    <%--窗体--%>
    <div id="detailWindow"></div>
</div>
</body>
</html>
