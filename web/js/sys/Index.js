sys.Index = function () {
    var me = this;
    me.menuMaxNum = 10;//最多选项卡数量
    var _top;//顶栏
    var _menu;//菜单
    var _tab;//右边tab
    me.getTop = function () {
        return _top;
    };
    me.getMenu = function () {
        return _menu;
    };
    me.getTab = function () {
        return _tab;
    };
    me.init = function () {
        me.initTop();
        me.initMenu();
        me.initTab();
    };
    me.initTop = function () {
        _top = $("#divTop");
        var aLogout = $("<a style='color: honeydew;font-weight: bold;'><img class='icon2 r9_c12' style='float:left;'/> 退 出 </a>");
        aLogout.css({
            "float": "right",
            "margin-right": "30px",
            "padding-top": "20px",
            "cursor": "pointer"
        }).click(me.logout);
        _top.append(aLogout);
    };

    me.initMenu = function () {
        _menu = $("#ulSysMenu");
        _menu.tree({
            url: AppHelper.Url.wapperUrl("js/data/menu.json"),
            method: 'get',
            animate: true,
            onClick: me.onClickMenu,
            onDblClick: me.onDblClickMenu
        });
    };

    me.initTab = function () {
        _tab = $("#divTabs");
        _tab.tabs({
            fit: true
        });
    };
    /**
     * 菜单单击
     * @param node
     */
    me.onClickMenu = function (node) {
        if (node.state == "open" && !node.children) {
            if (node.attributes.permission == Constants.role.admin
                && !AppHelper.Sys.isAdmin()) {
                $.messager.alert('错误', '对不起,您没有该操作权限!');
                return false;
            }
            me.openTab({
                title: node.text,
                iconCls: node.iconCls,
                url: node.attributes.url
            });
        }
    };
    /**
     * 菜单双击
     * @param node
     */
    me.onDblClickMenu = function (node) {
        if (node.state == "closed" || node.children) {
            me.getMenu().tree("toggle", node.target);
        }
    };
    /**
     * 打开或新增选项卡
     * @param param{title,iconCls,url,update}
     * @returns {boolean}
     */
    me.openTab = function (param) {
        if (!param.title) {
            $.messager.alert('警告', '标题不能为空!');
            return false;
        }
        if (me.getTab().tabs("exists", param.title)) {
            if (param.update) {//是否重新加载
                me.updateTab(param.title, param.url);
            } else {
                me.getTab().tabs("select", param.title);
            }
        } else {
            me.getTab().tabs('add', {
                title: param.title,
                selected: true,
                closable: true,
                fit: true,
                cache: false,
                iconCls: param.iconCls || "",
                content: AppHelper.Iframe.create(AppHelper.Url.getManageUrl(param.url)),//href:只是加载页面，且不能跨域
                msg: "正在加载数据，请稍候..."
            });
        }
    }
    /**
     * 获取当前选项卡
     * @returns {*}
     */
    me.getSelectedTab = function () {
        return me.getTab().tabs("getSelected");
    };
    /**
     * 更新tab页内容
     * @param title
     * @param url
     */
    me.updateTab = function (title, url) {
        if (me.getTab().tabs('exists', title)) {
            me.getTab().tabs('select', title);
            var tab = me.getTab().tabs('getSelected');
            me.getTab().tabs('update', {
                tab: tab,
                options: {
                    content: "<iframe src='" + AppHelper.Url.wapperUrl(url) + "' frameborder=0 height=100% width=100% scrolling=no></iframe>"
                }
            });
            tab.panel('refresh');
        } else {
            $.messager.alert('警告', title + '已关闭或未打开!');
        }
    };

    me.logout = function () {
        $.ajax({
            url: AppHelper.Url.getManageUrl(user.MODULE_USER + "/logout"),
            type: "post",
            success: function (data, textStatus) {
                if (AppHelper.Ajax.isSuccess(data, textStatus)) {
                    location.href = AppHelper.basePath + "login.shtml";
                } else {
                    $.messager.alert('警告', AppHelper.Ajax.getErrorMsg(data));
                }
            },
            error: function (data, textStatus, e) {
                $.messager.alert('警告', e.message);
            }
        });
    };
    return me;
};