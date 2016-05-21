<%@ page language="java" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/mgr/layout/import.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>记录导入和清理</title>
    <%@ include file="/WEB-INF/jsp/mgr/layout/header.jsp" %>
    <script type="text/javascript" src="${path}js/common/easyui.datagrid.extend.js"></script>
    <script type="text/javascript" src="${path}js/account/Account.js"></script>
    <script type="text/javascript">
        var accountImport = new account.AccountImport();
        var accountClear = new account.AccountClear();
        $(document).ready(function () {
            accountImport.init();
            accountClear.init();
        });
    </script>
</head>
<body>
<div class="easyui-layout" style="height: 100%;width: 100%;">
    <div id="divAccountList" data-options="region:'center'" class="easyui-panel">
        <div id="listLayout" class="easyui-layout" style="width:100%; height: 100%">
            <div data-options="region:'north',title:'导入',split:false" style="height:80px;">
                <form id="formAccountImport" method="post" enctype="multipart/form-data">
                    <div class="div-row">
                        <div class="label4">选择文件</div>
                        <div class="input4">
                            <input class="easyui-filebox" required="required" style="width:300px"
                                   name="file" id="file" data-options="buttonText:'请选择...'"/>
                        </div>
                        <div class="input4">
                            <a id="btnImport" href="#" class="easyui-linkbutton"
                               data-options="{iconCls:'icon-add'}">导 入</a>
                            　　　　　　　　　　
                            <a id="btnTemplate" href="${path}resources/import-template.xls" class="easyui-linkbutton"
                               data-options="{iconCls:'icon-save'}">模板下载</a>
                        </div>
                    </div>
                </form>
                <iframe id="ifmImport" style="display: none;"></iframe>
            </div>
            <div data-options="region:'center',title:'清理'">
                <form id="formAccountClear" method="post">
                    <div class="div-row">
                        <div class="label4">选择日期</div>
                        <div class="input4">
                            <input type="text" class="easyui-datebox" required="required" name="dateStr" id="dateStr"/>
                            (该日期之前的记录将清除)
                        </div>
                        <div class="input4">
                            <a id="btnClear" href="#" class="easyui-linkbutton"
                               data-options="{iconCls:'icon-remove'}">清 理</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <%--窗体--%>
    <div id="detailWindow"></div>
</div>
</body>
</html>
