(function ($) {
    /**
     * datagrid-toolbar
     * 使用: 在toolbar中加入permission属性
     */
    $.permission.methods.registerCheck({
        type: "toolbar",
        check: function (item) {
            return !item.permission || ($.inArray(item.permission, $.permission.defaults.permissionAry) != -1);
        },
        disable: function (item) {
            item.disabled = true;
        }
    });
    /**
     * form-linkbutton-options
     * 使用: 在options中加入permission属性
     */
    $.permission.methods.registerCheck({
        type: "linkbuttonopts",
        check: function (opts) {
            return !opts.permission || ($.inArray(opts.permission, $.permission.defaults.permissionAry) != -1);
        },
        disable: function (opts) {
            opts.disabled = true;
        }
    });
    /**
     * form-linkbutton
     * 使用: 在标签中加入data-permission属性
     */
    $.permission.methods.registerCheck({
        type: "linkbutton",
        disable: function (a) {
            $(a).linkbutton("disable");
            $(a).unbind("click").click(function (event) {
                event.stopPropagation();
                return false;
            });
        }
    });
    /**
     * 获取权限功能(需要AppHelper.js)
     * @param memberId
     * @returns {*}
     */
    (function () {
        if (!$.permission) {
            $.messager.alert("错误", "未引入jquery.permission.js");
            return;
        }
        $.ajax({
            url: AppHelper.Url.getManageUrl("/system/function/getFunctions", null),
            async: false,
            success: function (resultJson, textStatus) {
                if (AppHelper.Ajax.isSuccess(resultJson, textStatus)) {
                    $.permission.defaults.permissionAry = [];//先清空
                    var functions = AppHelper.Ajax.getData(resultJson);
                    for (var i = 0; i < functions.length; i++) {
                        $.permission.defaults.permissionAry.push(functions[i].code);
                    }
                } else {
                    $.messager.alert("错误", AppHelper.Ajax.getErrorMsg(resultJson));
                }
            },
            error: function (data, textStatus, e) {
                $.messager.alert('错误', e.message);
            }
        });
    })();
})
(jQuery);