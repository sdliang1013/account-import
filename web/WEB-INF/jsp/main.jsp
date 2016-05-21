<%@ page language="java" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/mgr/layout/import.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="pragma" content="no-cache">
    <title>账号导入查询系统</title>
    <%@ include file="/WEB-INF/jsp/mgr/layout/header.jsp" %>
    <script type="text/javascript" src="${path}js/sys/Index.js"></script>
    <script type="text/javascript">
        var index = new sys.Index();
        $(document).ready(index.init);
    </script>
</head>
<body>
<div class="easyui-layout" style="height: 100%;width: 100%;">
    <div id="divTop" data-options="region:'north'" style="height:50px; background-color: lightslategrey;">
        <span style="font-size: large;color: white;margin: 10px 30px 0 10px; float: left;">
            欢迎您【${currentUser.user.userName}】
        </span>
    </div>
    <div id="divMenu" data-options="region:'west',split:true" title="菜单"
         style="width:200px;">
        <ul id="ulSysMenu" class="easyui-tree"></ul>
    </div>
    <div id="divTab" data-options="region:'center'">
        <div id="divTabs" class="easyui-tabs">
            <div id="divWelcome" title="欢迎页">
                <h1 style="font-size: 36px;text-align: center;line-height: 550px;">欢迎使用账号导入查询系统</h1>
            </div>
        </div>
    </div>
    <div id="popWindow"></div>
</div>
</body>
</html>
