/**
 * Created by account-007 on 2016/3/11.
 */
account.AccountMgr = function () {
    var me = this;

    me.init = function () {
        me.initAccountList();
        $("#btnSearch").click(me.search);
    };
    /**
     * 初始化右键菜单
     */
    me.initAccountList = function () {
        $('#accountGrid').datagrid({
            url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/list"),
            loadMsg: '数据加载中，请稍后...',
            striped: true,
            pagination: true,
            rownumbers: true,
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
            }, "-", {
                text: '设为已派送',
                iconCls: 'icon-more',
                handler: me.setSent
            }, "-", {
                text: '设为已拒绝',
                iconCls: 'icon-more',
                handler: me.setRejected
            }, "-", {
                text: '设为套利',
                iconCls: 'icon-more',
                handler: me.setArbitrage
            }, "-", {
                text: '设为未派送',
                iconCls: 'icon-more',
                handler: me.setUnsent
            }, "-", {
                text: '设为未套利',
                iconCls: 'icon-more',
                handler: me.setUnArbitrage
            }],
            columns: [[

                {field: 'id', title: 'id', hidden: true},
                {field: 'accountName', title: '账号', width: 200},
                {field: 'qq', title: 'QQ', width: 200},
                {field: 'mobile', title: '手机号', width: 200},
                {field: 'handsel', title: '彩金', width: 100},
                {
                    field: 'sendState', title: '派送状态', width: 100,
                    formatter: easyuiExt.DataGrid.enumFormatter,
                    enumName: Constants.enum.SendState
                },
                {
                    field: 'arbitrage', title: '是否套利', width: 100,
                    formatter: easyuiExt.DataGrid.booleanFormatter
                },
                {
                    field: 'createTime', title: '导入时间', width: 100,
                    formatter: easyuiExt.DataGrid.dateFormatter
                }
            ]],
            onLoadSuccess: function (data) {
                //内容过长处理
                $('#accountGrid').datagrid('doCellTip', {
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
        var flag = me.checkField();
        if (!flag) {
            return false;
        }
        $("#accountGrid").datagrid("reload",
            AppHelper.Form.serialize("#formAccountList"));
    }

    me.checkField = function () {
        if ($("#mobile").val() && $("#mobile").val().length != 11) {
            $.messager.alert("错误", "手机号格式错误!");
            return false;
        }
        return true;
    }

    me.toAdd = function () {
        if (!AppHelper.Sys.checkPermission()) {
            return false;
        }
        var url = AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/to-add");
        $("#detailWindow").window({
            title: "增加记录",
            width: 500,
            height: 300,
            content: AppHelper.Iframe.create(url),
            onClose: me.search
        });
    };

    me.toEdit = function () {
        if (!AppHelper.Sys.checkPermission()) {
            return false;
        }
        var record = easyuiExt.DataGrid.getSelected($('#accountGrid'));
        if (!record) {
            return false;
        }
        var url = AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/to-edit", {id: record.id});
        $("#detailWindow").window({
            title: "修改记录",
            width: 500,
            height: 300,
            content: AppHelper.Iframe.create(url),
            onClose: me.search
        });
    };

    me.delete = function () {
        if (!AppHelper.Sys.checkPermission()) {
            return false;
        }
        var records = easyuiExt.DataGrid.getSelected($('#accountGrid'), true);
        if (!records || !window.confirm("确定删除吗?")) {
            return false;
        }
        $.ajax({
            url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/batch-delete", {
                ids: AppHelper.Array.getProperties(records, "id").join(",")
            }),
            type: "post",
            success: function (data, textStatus) {
                AppHelper.Ajax.defaultResult(data, textStatus, me.search);
            },
            error: function (data, textStatus, e) {
                $.messager.alert("错误", e.message);
            }
        });
    };
    /**
     * 已派送
     * @returns {boolean}
     */
    me.setSent = function () {
        var records = easyuiExt.DataGrid.getSelected($('#accountGrid'), true);
        if (!records || !window.confirm("确定设为已派送吗?")) {
            return false;
        }
        $.ajax({
            url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/set-sent", {
                ids: AppHelper.Array.getProperties(records, "id").join(",")
            }),
            type: "post",
            success: function (data, textStatus) {
                AppHelper.Ajax.defaultResult(data, textStatus, me.search);
            },
            error: function (data, textStatus, e) {
                $.messager.alert("错误", e.message);
            }
        });
    }
    /**
     * 已拒绝
     * @returns {boolean}
     */
    me.setRejected = function () {
        var records = easyuiExt.DataGrid.getSelected($('#accountGrid'), true);
        if (!records || !window.confirm("确定设为已拒绝吗?")) {
            return false;
        }
        $.ajax({
            url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/set-rejected", {
                ids: AppHelper.Array.getProperties(records, "id").join(",")
            }),
            type: "post",
            success: function (data, textStatus) {
                AppHelper.Ajax.defaultResult(data, textStatus, me.search);
            },
            error: function (data, textStatus, e) {
                $.messager.alert("错误", e.message);
            }
        });
    }
    /**
     * 套利用户
     * @returns {boolean}
     */
    me.setArbitrage = function () {
        var records = easyuiExt.DataGrid.getSelected($('#accountGrid'), true);
        if (!records || !window.confirm("确定标记为套利用户吗?")) {
            return false;
        }
        $.ajax({
            url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/set-arbitrage", {
                ids: AppHelper.Array.getProperties(records, "id").join(",")
            }),
            type: "post",
            success: function (data, textStatus) {
                AppHelper.Ajax.defaultResult(data, textStatus, me.search);
            },
            error: function (data, textStatus, e) {
                $.messager.alert("错误", e.message);
            }
        });
    }
    /**
     * 未派送
     * @returns {boolean}
     */
    me.setUnsent = function () {
        var records = easyuiExt.DataGrid.getSelected($('#accountGrid'), true);
        if (!records || !window.confirm("确定设为未派送吗?")) {
            return false;
        }
        $.ajax({
            url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/set-unsent", {
                ids: AppHelper.Array.getProperties(records, "id").join(",")
            }),
            type: "post",
            success: function (data, textStatus) {
                AppHelper.Ajax.defaultResult(data, textStatus, me.search);
            },
            error: function (data, textStatus, e) {
                $.messager.alert("错误", e.message);
            }
        });
    }
    /**
     * 未套利用户
     * @returns {boolean}
     */
    me.setUnArbitrage = function () {
        var records = easyuiExt.DataGrid.getSelected($('#accountGrid'), true);
        if (!records || !window.confirm("确定标记为未套利吗?")) {
            return false;
        }
        $.ajax({
            url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/set-unarbitrage", {
                ids: AppHelper.Array.getProperties(records, "id").join(",")
            }),
            type: "post",
            success: function (data, textStatus) {
                AppHelper.Ajax.defaultResult(data, textStatus, me.search);
            },
            error: function (data, textStatus, e) {
                $.messager.alert("错误", e.message);
            }
        });
    }
    return me;
};

account.AccountAdd = function () {
    var me = this;

    me.init = function () {
        me.setFields();
        $("#btnSave").click(me.submitForm);
    };

    me.submitForm = function () {
        if ($("#formAccountAdd").form("validate")) {
            $.ajax({
                url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/add"),
                type: "post",
                data: $("#formAccountAdd").serialize(),
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

account.AccountEdit = function () {
    var me = new comp.EntityForm();

    me.init = function () {
        me.loadForm();
        me.setFields();
        $("#btnSave").click(me.submitForm);
    };

    me.loadForm = function () {
        var url = AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/detail", {id: $("#hdnId").val()});
        $("#formAccountEdit").form('load', url);
    };

    me.onLoadEntity = function (entity) {
    };

    me.setFields = function () {
    };

    me.submitForm = function () {
        if ($("#formAccountEdit").form("validate")) {
            $.ajax({
                url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/edit"),
                type: "post",
                data: $("#formAccountEdit").serialize(),
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

account.AccountImport = function () {
    var me = this;

    me.init = function () {
        $("#btnImport").click(me.importAccount);
    }
    me.importAccount = function () {
        $("#formAccountImport").form("submit", {
            url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/import"),
            ajax: false,
            onSubmit: function () {
                return $("#formAccountImport").form("validate");
            },
            success: function (data) {
                var dataObj = AppHelper.Json.valueOf(data);
                AppHelper.Ajax.defaultResult(dataObj,
                    typeof dataObj == "object" ? AppHelper.Status.Success : "error");
            },
            error: function (data, status, e) {
                $.messager.alert("失败", e.message, "error");
            }
        });
    }
    return me;
}

account.AccountClear = function () {
    var me = this;

    me.init = function () {
        $("#btnClear").click(me.clearAccount);
    }
    me.clearAccount = function () {
        if (!$("#formAccountClear").form("validate")) {
            return false;
        }
        $.messager.confirm('确认对话框',
            "确定要清除【" + $("#dateStr").datebox("getValue") + "】之前的数据吗?",
            function (flag) {
                if (flag) {
                    $("#formAccountClear").form("submit", {
                        url: AppHelper.Url.getManageUrl(account.MODULE_ACCOUNT + "/clear"),
                        success: function (data) {
                            var dataObj = AppHelper.Json.valueOf(data);
                            AppHelper.Ajax.defaultResult(dataObj,
                                typeof dataObj == "object" ? AppHelper.Status.Success : "error");
                        },
                        error: function (data, status, e) {
                            $.messager.alert("失败", e.message, "error");
                        }
                    });
                }
            });
    }
    return me;
};
