/**
 * jQuery EasyUI 1.4.3
 *
 * Copyright (c) 2009-2015 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt
 * To use it on other terms please contact us at info@jeasyui.com
 *
 */
/**
 * permission - jQuery EasyUI
 *
 */
(function ($) {

    function disableA(a) {
        $(a).attr({
            href: "javascript:void(0);"
        }).css({
            color: "#808080"
        }).unbind("click").click(function (event) {
            event.stopPropagation();
            return false;
        });
    }

    function disableButton(btn) {
        $(btn).attr({
            disabled: true
        }).css({
            color: "#808080"
        });
    }

    $.permission = {};

    $.permission.methods = {
        /**
         * 注册检验方法{type,replace,check,disable}
         * @param param
         */
        registerCheck: function (param) {
            if (!param.type) {
                alert("请指定type!");
                return;
            }
            if (!$.permission.lib[param.type] || param.replace) {
                $.permission.lib[param.type] =
                    $.extend({}, $.permission.defaults.check, {
                        check: param.check,
                        disable: param.disable
                    });
            }
        },
        /**
         * 检查权限,并控制
         * @param target
         * @param type
         */
        check: function (target, type) {
            if (!type) {
                alert("请指定type参数!");
                return;
            }
            var checkObj = $.permission.lib[type];
            if (!checkObj) {
                return;
            }
            var ary = $.isArray(target) ? target : [target];
            $.each(ary, function (idx, obj) {
                if (!checkObj.check(obj)) {
                    checkObj.disable(obj);
                }
            });
        }
    };

    $.permission.defaults = {
        permissionAry: [],//权限code集合
        check: {
            check: function (el) {
                return !$(el).attr("data-permission") || $.inArray($(el).attr("data-permission"), $.permission.defaults.permissionAry) != -1;
            },
            disable: function (obj) {
                $(obj).attr("disabled", true);
                if ($(obj).disable) {
                    $(obj).disable();
                }
            }
        }
    };

    $.permission.lib = {
        "a": $.extend({}, $.permission.defaults.check, {
            disable: disableA
        }),
        "button": $.extend({}, $.permission.defaults.check, {
            disable: disableButton
        })
    };
})
(jQuery);
