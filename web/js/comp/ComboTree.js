/**
 * Created by user-007 on 2015/8/15.
 */
/**
 *
 * @param param {target,[data|url],[value]}
 * @returns {*}
 * @constructor
 */
comp.ComboTree = function (param) {
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
        me.getTarget().combotree(me.getConfig(param));
        if (param.value) {
            me.getTarget().combotree("setValue", param.value);
        }
        if (param.text) {
            me.getTarget().combotree("setText", param.text);
        }
        me.initFlag = true;
    };

    me.getConfig = function (param) {
        return $.extend({
            required: true,
            multiple: false
        }, param);
    };

    me.getTree = function () {
        return me.getTarget().combotree('tree');
    };

    me.init(param);
    return me;
};