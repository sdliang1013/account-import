/**
 * Created by user-007 on 2016/3/11.
 */
user.UserMgr = function () {
    var me = this;

    me.init = function () {
        me.initUserList();
        $("#btnSearch").click(me.search);
    };
    /**
     * 初始化右键菜单
     */
    me.initUserList = function () {
        $('#userGrid').datagrid({
            url: AppHelper.Url.getManageUrl(user.MODULE_USER + "/list", {deleted: 0}),
            loadMsg: '数据加载中，请稍后...',
            striped: true,
            pagination: true,
            rownumbers: true,
            singleSelect: true,
            height: '100%',
            pageSize: 20,

            toolbar: [{
                text: '增加',
                iconCls: 'icon-add',
                handler: me.toAdd
            }, "-", {
                text: '编辑',
                iconCls: 'icon-edit',
                handler: me.toEdit
            }, "-", {
                text: '删除',
                iconCls: 'icon-remove',
                handler: me.delete
            }],
            columns: [[

                {field: 'id', title: 'id', hidden: true},
                {
                    field: 'userName', title: '账号', width: 200
                },
                {field: 'realName', title: '姓名', width: 200},
                {
                    field: 'userType', title: '类型', width: 200,
                    formatter: easyuiExt.DataGrid.enumFormatter,
                    enumName: Constants.enum.UserType
                }
            ]],
            onLoadSuccess: function (data) {
                //内容过长处理
                $('#userGrid').datagrid('doCellTip', {
                    delay: 500,
                    maxWidth: 400,
                    onlyShowInterrupt: true
                });
            }
        });
    };
    /**
     * 查询按钮
     */
    me.search = function () {
        $("#userGrid").datagrid("reload",
            AppHelper.Form.serialize("#formUserList"));
    }

    me.toAdd = function () {
        var url = AppHelper.Url.getManageUrl(user.MODULE_USER + "/to-add");
        $("#detailWindow").window({
            title: "增加操作员",
            width: 500,
            height: 300,
            content: AppHelper.Iframe.create(url),
            onClose: me.search
        });
    };

    me.toEdit = function () {
        var record = easyuiExt.DataGrid.getSelected($('#userGrid'));
        if (!record) {
            return false;
        }
        //if (record.userType == Constants.UserType.Manage) {
        //    easyuiExt.Msg.show("警告", "不能编辑管理员!", true);
        //    return false;
        //}
        var url = AppHelper.Url.getManageUrl(user.MODULE_USER + "/to-edit", {id: record.id});
        $("#detailWindow").window({
            title: "修改操作员",
            width: 500,
            height: 300,
            content: AppHelper.Iframe.create(url),
            onClose: me.search
        });
    };

    me.delete = function () {
        var record = easyuiExt.DataGrid.getSelected($('#userGrid'));
        if (!record || !window.confirm("确定删除吗?")) {
            return false;
        }
        if (record.userType == Constants.UserType.Manage) {
            easyuiExt.Msg.show("警告", "不能删除管理员!", true);
            return false;
        }
        $.ajax({
            url: AppHelper.Url.getManageUrl(user.MODULE_USER + "/delete", {id: record.id}),
            type: "post",
            success: function (data, textStatus) {
                AppHelper.Ajax.defaultResult(data, textStatus);
                me.search();
            },
            error: function (data, textStatus, e) {
                $.messager.alert("错误", e.message);
            }
        });
    };
    return me;
};

user.UserAdd = function () {
    var me = this;

    me.init = function () {
        me.setFields();
        $("#btnSave").click(me.submitForm);
    };

    me.submitForm = function () {
        if ($("#formUserAdd").form("validate")) {
            $("#hdnPwd").val(hex_sha1($("#pwd").val()));
            $.ajax({
                url: AppHelper.Url.getManageUrl(user.MODULE_USER + "/add"),
                type: "post",
                data: $("#formUserAdd").serialize(),
                success: function (data, textStatus) {
                    AppHelper.Ajax.defaultResult(data, textStatus, easyuiExt.Win.closeWin);
                },
                error: function (data, textStatus, e) {
                    AppHelper.Ajax.defaultResult(data, textStatus, null, e);
                }

            });
        }
    };

    me.setFields = function () {
    };

    return me;
}

user.UserEdit = function () {
    var me = new comp.EntityForm();

    me.init = function () {
        me.loadForm();
        me.setFields();
        $("#btnSave").click(me.submitForm);
    };

    me.loadForm = function () {
        var url = AppHelper.Url.getManageUrl(user.MODULE_USER + "/detail", {id: $("#hdnId").val()});
        $("#formUserEdit").form('load', url);
    };

    me.onLoadEntity = function (entity) {
    };

    me.setFields = function () {
    };

    me.submitForm = function () {
        if ($("#formUserEdit").form("validate")) {
            if ($("#pwd").val()) {
                $("#hdnPwd").val(hex_sha1($("#pwd").val()));
            }
            $.ajax({
                url: AppHelper.Url.getManageUrl(user.MODULE_USER + "/edit"),
                type: "post",
                data: $("#formUserEdit").serialize(),
                success: function (data, textStatus) {
                    AppHelper.Ajax.defaultResult(data, textStatus, easyuiExt.Win.closeWin);
                },
                error: function (data, textStatus, e) {
                    AppHelper.Ajax.defaultResult(data, textStatus, null, e);
                }

            });
        }
    };
    return me;
}
