/**
 * Created by user-007 on 2015/8/13.
 */
user.Login = function () {
    var me = this;
    me.init = function () {
        $("#aLogin").click(me.submit);
        $("#aLogout").click(me.regedit);
        $(document).on("keydown", function (event) {
            if (event.keyCode == "13") {
                me.submit();
            }
        });
    };

    me.submit = function () {
        if (me.beforeSubmit()) {
            $.ajax({
                url: AppHelper.Url.getManageUrl(user.MODULE_USER + "/login"),
                type: "post",
                dataType: "json",
                data: {
                    "userName": $("#txtUserName").val(),
                    "password": me.getPwd()
                },
                success: function (data, textStatus) {
                    if (AppHelper.Ajax.isSuccess(data, textStatus)) {
                        Constants.currentUser = {userName: $("#txtUserName").val()};
                        location.href = AppHelper.basePath + "main.shtml";
                    } else {
                        $.messager.alert('警告', AppHelper.Ajax.getErrorMsg(data));
                    }
                },
                error: function (data, textStatus, e) {
                    $.messager.alert('警告', e.message);
                }
            });
        }
    };

    me.beforeSubmit = function () {
        if (!$("#txtUserName").val()) {
            $.messager.alert('警告', "用户名不能为空!");
            return false;
        }
        if (!$("#password").val()) {
            $.messager.alert('警告', "密码不能为空!");
            return false;
        }
        return true;
    };

    me.getPwd = function () {
        var pwd = hex_sha1($("#password").val());
        return pwd.substring(0, _cp[1]) + _cp[0] + pwd.substr(_cp[1]);
    };
    return me;
};