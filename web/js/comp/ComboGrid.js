/**
 * Created by sdliang on 2015/8/15.
 */
/**
 *
 * @param param {target,columns,[pagination],[data|url],[queryColumn],[idField],[textField],[value],[initText]}
 * @returns {*}
 * @constructor
 */
comp.ComboGrid = function (param) {
    if (!param.target) {
        $.messager.alert("警告", "请指定target属性!");
        return false;
    }
    if (!param.columns) {
        $.messager.alert("警告", "请指定columns属性!");
        return false;
    }
    var me = this;
    me.initFlag = false;
    me.target = param.target;
    me.queryInputDiv = "div_" + param.queryColumn;
    me.queryInputId = "txt_" + param.queryColumn;
    me.configFunc = {};

    me.init = function (initParam) {
        if (!AppHelper.String.isEmpty(param.value)) {//初始化值
            $(me.target).val(param.value);
        }

        me.target.combogrid(me.getConfig(initParam));
        me.initFlag = true;
    };

    /**
     * 设置初始化参数
     * @param configParam
     * @returns {{required: boolean, multiple: (*|config.multiple|jQuery.fn.accordion.parseOptions.multiple|jQuery.fn.accordion.defaults.multiple|boolean|jQuery.fn.combo.parseOptions.multiple)}}
     */
    me.getConfig = function (configParam) {
        var config = $.extend({
            required: true,
            striped: true
        }, configParam);
        if (configParam.width) {
            config.panelWidth = configParam.width;
        }
        //设置数据源
        if (configParam.url && configParam.mode != "remote") {
            config.filter = configParam.filter || function (q, row) {
            };
        }
        //查询条件
        if (configParam.queryColumn) {
            var div = $('<div id="' + me.queryInputDiv + '"><input type="text" id="' + me.queryInputId + '"/></div>');
            var btn = $('<input type="button" class="icon-search" value="     "/>');
            btn.click(function () {
                var filterParam = {};
                filterParam[configParam.queryColumn] = $("#" + me.queryInputId).val();
                me.getGrid().datagrid('load', filterParam);
            });
            div.append(btn);
            $(document.body).append(div);
            config.toolbar = "#" + me.queryInputDiv;
        }
        //设置列名
        if (configParam.columns && !$.isArray(configParam.columns)) {
            var columnsAry = [];
            var column;
            for (var col in configParam.columns) {
                column = {field: col, title: configParam.columns[col]};
                if (col.toString() == "id") {//
                    column.hidden = true;
                    config.idField = col;
                }
                columnsAry.push(column);
            }
            config.columns = [columnsAry];
        }
        var columnModel = config.columns[0];
        config.idField = configParam.idField || config.idField || columnModel[0].field;
        config.textField = configParam.textField || columnModel[1].field;
        //分页
        if (configParam.pagination) {
            config.panelWidth = Math.max(400, config.panelWidth || 0);
            config.pagination = true;
            config.pageSize = 10;
        }
        //加入事件
        for (var p in configParam) {
            if (p.toString().startsWith("on")) {
                config[p] = configParam[p];
            }
        }
        //初始值
        if ((!AppHelper.String.isEmpty(configParam.value) || configParam.initText)
            && configParam.pagination) {//分页
            config.textInited = false;
            if (typeof configParam.onLoadSuccess == "function") {
                me.configFunc.onLoadSuccess = configParam.onLoadSuccess;
                config.onLoadSuccess = function (data) {
                    try {
                        me.initText(data);
                    } catch (e) {
                    }
                    me.configFunc.onLoadSuccess(data);
                }
            } else {
                config.onLoadSuccess = me.initText;
            }
        }
        return config;
    };
    /**
     * 初始化文本
     * @param data
     */
    me.initText = function (data) {
        var comboOpts = $(me.target).combogrid("options");
        if (!comboOpts.textInited) {
            var val = $(me.target).combogrid("getValue");
            var dataLen = data.rows.length;
            if (dataLen == 0 || !val) {
                return;
            }
            var grid = $(me.target).combogrid('grid');
            var idField = comboOpts.idField;
            for (var i = 0; i < dataLen; i++) {
                if (data.rows[i][idField] == val) {
                    comboOpts.textInited = true;
                    return;
                }
            }
            var paper = grid.datagrid("getPager");
            var paperOpts = paper.pagination("options");
            var pageNumber = paperOpts.pageNumber + 1;
            if (Math.ceil(paperOpts.total / paperOpts.pageSize) >= pageNumber) {
                paper.pagination('select', pageNumber);
            }
        }
    };

    me.getGrid = function () {
        return me.target.combogrid('grid');
    };

    me.init(param);
    return me;
};