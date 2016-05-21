/**
 * Created by yuanyouz on 2016/3/17.
 */
/**
 *
 * @param param 属性继承自comp.ComboGrid
 *   {
 *   target,      目标控件
 *   [needPlatform], 是否需要在列表中出现平台
 *   [needAllPlatform], 是否需要在列表中出现全平台
 *   [required],  是否必填
 *   [multiple]   是否支持多选
 *   }
 * @returns {*}
 * @constructor
 */
comp.OrgComboGrid = function (param) {
    var needPlatform = AppHelper.isNull(param.needPlatform) ? 'false' : param.needPlatform;
    var needAllPlatform = AppHelper.isNull(param.needAllPlatform) ? 'false' : param.needAllPlatform;
    var required = AppHelper.isNull(param.required) ? 'false' : param.required;
    var multiple = AppHelper.isNull(param.multiple) ? 'false' : param.multiple;

    var me = new comp.ComboGrid({
        target: param.target,
        url: AppHelper.Url.manage + "org/org/list-for-select.shtml?needPlatform=" + needPlatform + "&needAllPlatform=" + needAllPlatform,
        required: required,
        multiple: multiple,
        pagination: true,
        panelHeight: 360,
        columns: {
            "id": "id",
            "name": "企业名称"
        },
        queryColumn: "name"
    });

    me.target = param.target;

    /**
     * 当multiple=true时调用
     * 获取选中的企业id，多个企业id以,号分隔
     */
    me.getSelectedOrgIds = function () {
        return AppHelper.Array.toString(me.target.combogrid("getValues"), ',');
    };

    /**
     * 当multiple=false时调用
     * 获取选中的单个企业id
     */
    me.getSelectedOrgId = function () {
        return AppHelper.Array.toString(me.target.combogrid("getValue"), ',');
    };

    return me;
};

