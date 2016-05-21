/**
 * Created by user-007 on 2016/1/6.
 */
if (typeof PermissionUtil == 'undefined') {
    var PermissionUtil = {};
}

(function ($) {
    PermissionUtil = $.extend(PermissionUtil, {
        /**
         * 获取权限功能
         * @param memberId
         * @returns {*}
         */
        getFunctions: function () {
            if (Constants.currentUser.functions) {
                return Constants.currentUser.functions;
            }
            $.ajax({
                url: AppHelper.Url.getManageUrl("/system/function/getFunctions", null),
                async: false,
                success: function (resultJson, textStatus) {
                    if (AppHelper.Ajax.isSuccess(resultJson, textStatus)) {
                        Constants.currentUser.functions = AppHelper.Ajax.getData(resultJson);
                    } else {
                        $.messager.alert("错误", AppHelper.Ajax.getErrorMsg(resultJson));
                    }
                },
                error: function (data, textStatus, e) {
                    $.messager.alert('错误', e.message);
                }
            });
            return Constants.currentUser.functions;
        },
        checkPermission: function (scope) {
            $(scope).find("*[data-auth]").each(function (obj, idx) {
                //alert(obj.innerHTML);
            });
        }
    });
})
(jQuery);