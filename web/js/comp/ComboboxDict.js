/**
 * Created by user-007 on 2015/8/15.
 */
/**
 *
 * @param param {target,[type|data|url],[valueField],[textField],[value]}
 * @returns {*}
 * @constructor
 */
comp.ComboBoxDict = function (param) {
    if (!param.target) {
        $.messager.alert("警告", "请指定target属性!");
        return false;
    }
    var me = this;
    me.initFlag = false;
    var target = param.target;
    me.getTarget = function () {
        return target;
    };

    me.init = function (param) {
        var comboParam = me.getConfig(param);
        if (comboParam.url) {
            $.getJSON(comboParam.url, function (result) {
                comboParam.data = result;
                if (typeof comboParam.filter == "function") {
                    comboParam.data = comboParam.filter(comboParam.data);
                }
                comboParam.url = null;
                comboParam.filter = null;
                me.getTarget().combobox(comboParam);
                me.initFlag = true;
            });
        } else if (comboParam.data) {
            me.getTarget().combobox(comboParam);
            me.initFlag = true;
        }
    };
    /**
     * 设置初始化参数
     * @param param
     * @returns {{multiple: (*|config.multiple|jQuery.fn.accordion.parseOptions.multiple|jQuery.fn.accordion.defaults.multiple|boolean|jQuery.fn.combo.parseOptions.multiple)}}
     */
    me.getConfig = function (param) {
        var config = $.extend({
            valueField: "code",
            textField: "text"
        }, param);
        if (param.type) {
            config.url = AppHelper.Url.getManageUrl("/sys/dict/load", {
                type: param.type,
                code: param.code || ""
            });
        }
        if (!AppHelper.String.isEmpty(param.value)) {
            config.onLoadSuccess = function () {
                $(this).combobox("setValue", param.value);
            };
        }
        return config;
    };

    me.init(param);
    return me;
};