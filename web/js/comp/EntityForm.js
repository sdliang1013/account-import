/**
 * Created by user-007 on 2015/8/17.
 */
comp.EntityForm = function () {
    var me = this;

    var getTargetForm = function (targetId) {
        var targetForm;
        if (typeof targetId == "string") {
            targetForm = $("#" + targetId);
        } else {
            targetForm = $(targetId);
        }
        return targetForm;
    }
    /**
     * 加载实体
     * @param url
     * @param targetFormId
     */
    me.loadEntity = function (url, targetFormId) {
        $.ajax({
            "url": url,
            success: function (data, textStatus) {
                if (AppHelper.Ajax.isSuccess(data, textStatus)) {
                    var entity = AppHelper.Ajax.getData(data);
                    me.onLoadEntity(entity)
                    getTargetForm(targetFormId).form('load', entity);
                    me.onSetEntity(entity);
                } else {
                    $.messager.alert("警告", AppHelper.Ajax.getErrorMsg(data));
                    me.onError();
                }
            },
            error: function (data, textStatus, e) {
                $.messager.alert("警告", e.message);
                me.onError();
            }

        });
    };
    /**
     * 获取数据后
     * @param entity
     */
    me.onLoadEntity = function (entity) {

    };
    /**
     * 设置完字段后
     * @param entity
     */
    me.onSetEntity = function (entity) {

    };
    /**
     * 错误处理
     */
    me.onError = function () {

    };
    return me;
};
