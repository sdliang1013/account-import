<%@ page language="java" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/jsp/mgr/layout/import.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="description" content="运用CSS3和CSS滤镜做的精美的登录界面，无用到图片的喔！">
    <title>系统登录</title>
    <%@ include file="/WEB-INF/jsp/mgr/layout/header.jsp" %>
    <link rel="stylesheet" type="text/css" href="${path}skin/${skin}/login.css"/>
    <script type="text/javascript" src="${path}js/common/AESUtil.js"></script>
    <script type="text/javascript" src="${path}js/user/Login.js"></script>
    <script type="text/javascript">
        var login = new user.Login();
        var _cp = ["${encryptCode}", ${encryptPosition}];
        $(document).ready(login.init);
    </script>
</head>

<body>
<div class="wrap">
    <form id="loginForm" method="post">
        <section class="loginForm">
            <header>
                <h1>账号导入查询系统</h1>
            </header>
            <div class="loginForm_content">
                <fieldset>
                    <div class="inputWrap" style="background: #eee;padding-left: 10px;">
                        <span style="font-size: 15px">用 户</span>
                        <input style="width: 78%;" id="txtUserName" type="text" name="userName"
                               placeholder="请输入用户名"
                               autofocus required>
                    </div>
                    <div class="inputWrap" style="background: #eee;padding-left: 10px;">
                        <span style="font-size: 15px">密 码</span>
                        <input style="width: 78%;" id="password" type="password" name="password"
                               placeholder="请输入密码"
                               required>
                    </div>
                </fieldset>
                <fieldset>
                    <div style="text-align:right;padding:15px">
                        <a id="aLogin" href="javascript:void(0)" class="easyui-linkbutton">
                            &nbsp;登&nbsp;&nbsp;录&nbsp;</a>
                    </div>
                </fieldset>
            </div>
        </section>
    </form>
</div>
</body>
<script>
</script>
</html>
