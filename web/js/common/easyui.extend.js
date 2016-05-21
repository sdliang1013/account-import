if (typeof easyuiExt == 'undefined') {
    var easyuiExt = {
        URL_BASE: AppHelper.Url.base,
        Ajax: AppHelper.Ajax,
        Status: AppHelper.Status,
        skin: AppHelper.skin,
        Json: AppHelper.Json,
        Sys: AppHelper.Sys
    };
}
(function ($) {
    easyuiExt = $.extend(easyuiExt, {
        Msg: {
            show: function (title, msg, isAlert) {
                if (isAlert !== undefined && isAlert) {
                    $.messager.alert(title, msg);
                } else {
                    $.messager.show({
                        title: title,
                        msg: msg,
                        showType: 'show'
                    });
                }
            },
            confirm: function (title, msg, callback) {
                $.messager.confirm(title, msg, function (r) {
                    if (r) {
                        if (jQuery.isFunction(callback))
                            callback.call();
                    }
                });
            },
            process: function (isShow, title, msg) {
                if (!isShow) {
                    $.messager.progress('close');
                    return;
                }
                $.messager.progress({
                    title: title,
                    msg: msg
                });
            }
        },
        Win: {
            showPop: function (param) {
                var width = 600, height = 400;
                var title = "新窗口";
                if (param.title) {
                    title = param.title;
                }
                var defaultConf = {
                    title: title,
                    width: width,
                    height: height,
                    modal: true,
                    minimizable: false,
                    maximizable: true,
                    shadow: false,
                    cache: false,
                    closed: false,
                    collapsible: true,
                    resizable: true,
                    loadingMessage: '正在加载数据，请稍等片刻......'
                };
                var config = $.extend(defaultConf, param);
                if (param.href) {
                    config.href = param.href;
                } else if (param.content) {
                    config.content = param.content;
                } else if (param.src) {
                    config.content = "<iframe src='" + param.src + "' frameborder=0 height=100% width=100% scrolling=auto></iframe>";
                }
                var _targetWin = param.targetWin || window.top;
                easyuiExt.Win._getPopWin(_targetWin).window(config);
            },
            closePop: function (targetWin) {
                var _targetWin = window.top;
                if (targetWin) {
                    _targetWin = targetWin;
                }
                easyuiExt.Win._getPopWin(_targetWin).window('close');
            },
            /**
             * 关闭窗体
             * @param winId 窗体DIV的id
             * @param target 所属window对象
             */
            closeWin: function (winId, target) {
                if (winId) { //指定窗体ID
                    if (target) { //指定所属window对象
                        target.$("#" + winId).window('close');
                    } else {
                        var divClose = $("#" + winId) || parent.$("#" + winId);
                        if (divClose) {
                            divClose.window('close');
                        }
                    }
                } else {
                    var aClose = parent.$(".panel-tool-close");
                    if (aClose) {
                        aClose.click();
                    }
                }
            },
            /**
             * 关闭并刷新
             * @param config{win:窗体ID,grid: 刷新列表ID,tree: 刷新树ID}
             */
            closeAndReload: function (config) {
                if (config.grid) {
                    var grid = $("#" + config.grid);
                    grid = (grid.size() == 0) ? parent.$("#" + config.grid) : grid;
                    if (grid) {
                        grid.datagrid("reload");
                    }
                }
                if (config.tree) {
                    var tree = $("#" + config.tree);
                    tree = (tree.size() == 0) ? parent.$("#" + config.tree) : tree;
                    if (tree) {
                        tree.tree("reload");
                    }
                }
                if (config.win) {
                    var win = $("#" + config.win);
                    win = (win.size() == 0) ? parent.$("#" + config.win) : win;
                    win = (win.size() == 0) ? window.top.$("#" + config.win) : win;
                    if (win) {
                        win.window("close");
                    }
                }
            },
            /**
             * 获取iframe窗体内容
             * @param winDiv 窗体DIV
             * @returns {*}
             */
            getContentWin: function (winDiv) {
                var contentWin = $(winDiv).find("iframe").get(0);
                if (contentWin) {
                    return contentWin.contentWindow;
                }
                return null;
            },
            _getPopWin: function (targetWin) {
                var popWin = targetWin.$("#popWindow");
                if (popWin.size() == 0) {
                    popWin = $('<div id="popWindow"></div>');
                    $(targetWin.document.body).append(popWin);
                }
                return popWin;
            }
        },
        Form: {
            submit: function (param) {
                var fm = '#' + param.targetId;
                $(fm).form('submit', {
                    onSubmit: function () {
                        var flag = $(this).form('enableValidation').form('validate');
                        if (flag) {
                            easyuiExt.Msg.process(true, '温馨提示', '正在提交数据...');
                        }
                        this.action = this.action || param.action;
                        return flag;
                    },
                    success: function (data) {
                        easyuiExt.Msg.process(false);
                        //TODO: 修改测试
                        var result = easyuiExt.Json.valueOf(data);
                        if (easyuiExt.Ajax.isSuccess(result, easyuiExt.Status.Success)) {
                            easyuiExt.Msg.show('温馨提示', '操作成功！');
                            param.onSuccessCallBack();
                        } else {
                            easyuiExt.Msg.show('温馨提示', easyuiExt.Ajax.getErrorMsg(result), true);
                        }
                    },
                    onLoadError: function () {
                        easyuiExt.Msg.process(false);
                        easyuiExt.Msg.show('温馨提示', '由于网络或服务器太忙，提交失败，请重试！', true);
                    }
                });
            },
            clear: function (targetId) {
                var fm = '#' + targetId;
                $(fm).form('clear');
            }
        },
        DataGrid: {
            getSelected: function (grid, isMulti) {
                var multi = isMulti || false;
                var selRows = grid.datagrid("getSelections");
                if (selRows.length == 0) {
                    $.messager.alert("警告", "请至少选择一条记录!");
                    return false;
                } else if (!multi && selRows.length > 1) {
                    $.messager.alert("警告", "只能选择一条记录!");
                    return false;
                }
                return multi ? selRows : selRows[0];
            },
            getSelectedRowIndex: function (grid) {
                var row = easyuiExt.DataGrid.getSelected(grid);
                return grid.datagrid('getRowIndex', row);
            },
            formatPager: function (grid, pageSize) {
                var p = grid.datagrid('getPager');
                pageSize = AppHelper.isNull(pageSize) ? 20 : pageSize;
                p.pagination({
                    pageSize: pageSize,
                    pageList: [10, 20, 30, 50, 80],
                    beforePageText: '第',
                    afterPageText: '页    共 {pages} 页',
                    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
                });
                return p;
            },
            dateFormatter: function (value) {
                if (value == undefined) {
                    return "";
                }

                var date = new Date(value);

                //月份
                var month = date.getMonth() + 1;
                if (month < 10) {
                    month = '0' + (month);
                }
                //日期
                var day = date.getDate();
                if (day < 10) {
                    day = '0' + day;
                }

                return date.getFullYear() + '-' + month + '-' + day;
            },
            dateTimeFormatter: function (value, row, index) {
                if (value == undefined) {
                    return "";
                }

                var date = new Date(value);

                //月份
                var month = date.getMonth() + 1;
                if (month < 10) {
                    month = '0' + (month);
                }
                //日期
                var day = date.getDate();
                if (day < 10) {
                    day = '0' + day;
                }
                //小时
                var hours = date.getHours();
                if (hours < 10) {
                    hours = '0' + hours;
                }
                //分钟
                var minutes = date.getMinutes();
                if (minutes < 10) {
                    minutes = '0' + minutes;
                }
                //秒
                var seconds = date.getSeconds();
                if (seconds < 10) {
                    seconds = '0' + seconds;
                }

                return date.getFullYear() + '-' + month + '-' + day + " " + hours + ":" + minutes + ":" + seconds;
            },
            booleanFormatter: function (value, row, index) {
                if (value == undefined) {
                    return "";
                }

                if ("true" == value || "1" == value || value) {
                    return "是";
                } else if ("false" == value || "0" == value || !value) {
                    return "否";
                }

                return "";
            },
            typeFormatter: function (value, row, index) {
                if (value == undefined) {
                    return "";
                }

                if ("1" == value || value) {
                    return "视频";
                } else if ("2" == value || !value) {
                    return "图片";
                } else if ("3" == value || !value) {
                    return "图文";
                }

                return "";
            },
            genderFormatter: function (value, row, index) {
                if (value == undefined) {
                    return "";
                }

                if ("true" == value || true == value) {
                    return "男";
                } else if ("false" == value || false == value) {
                    return "女";
                }

                return "";
            },
            deletedFormatter: function (value, row, index) {
                if (value == undefined) {
                    return "";
                }

                if ("true" == value || true == value) {
                    return "冻结";
                } else if ("false" == value || false == value) {
                    return "正常";
                }

                return "";
            },
            imageObjectFormatter: function (value, rec, index) {
                if (value == undefined) {
                    var image = rec.image;
                    if (image) {
                        value = image.url;
                    }
                }
                return easyuiExt.DataGrid._getImageFormatterStr(value);
            },
            imageFormatter: function (value, rec, index) {
                return easyuiExt.DataGrid._getImageFormatterStr(value);
            },
            _getImageFormatterStr: function (value) {
                if (value == undefined) {
                    return "<img src='" + easyuiExt.URL_BASE +
                        "/_m_/skin/" + easyuiExt.skin + "/images/defaultPicture.jpg' height=100 style='width: auto;'/>";
                }
                return "<img src='" + value + "' onerror='javascript:$(this).attr(\"src\",\"" +
                    easyuiExt.URL_BASE + "/_m_/skin/" + easyuiExt.skin + "/images/defaultPicture.jpg\")' height=100 style='width: auto;'/>";
            },
            /**
             * 字典表(同时需要设置dictType)
             * @param value
             * @param row
             * @param index
             * @returns {*}
             */
            dictFormatter: function (value, row, index) {
                var me = this;
                if (value == undefined) {
                    return "";
                }
                if (!me.dictType) {
                    easyuiExt.Msg.show("错误", me.title + " 码表类型未指定!", true);
                    return "";
                }
                try {
                    if (!me._dictCache) {
                        me._dictCache = easyuiExt.Sys.dict(me.dictType);
                    }
                    var obj;
                    for (var i = 0; i < me._dictCache.length; i++) {
                        obj = me._dictCache[i];
                        if (obj.code == value) {
                            return obj.text;
                        }
                    }
                } catch (e) {
                }
                return "";
            },
            /**
             * 枚举表(同时需要设置enumName)
             * @param value
             * @param row
             * @param index
             * @returns {*}
             */
            enumFormatter: function (value, row, index) {
                var me = this;
                if (value == undefined) {
                    return "";
                }
                if (!me.enumName) {
                    easyuiExt.Msg.show("错误", me.title + " 枚举类型未指定!", true);
                    return "";
                }
                try {
                    if (!me._enumCache) {
                        me._enumCache = easyuiExt.Sys.enum(me.enumName);
                    }
                    var obj;
                    for (var i = 0; i < me._enumCache.length; i++) {
                        obj = me._enumCache[i];
                        if (obj.code == value) {
                            return obj.text;
                        }
                    }
                } catch (e) {
                }
                return "";
            },
            deleteRow: function (grid, deleteUrl) {
                easyuiExt.Msg.confirm('温馨提示', '确定要删除吗?', function () {
                    var row = easyuiExt.DataGrid.getSelected(grid);
                    if (row) {
                        if (deleteUrl.indexOf("?") == -1) {
                            deleteUrl = deleteUrl + "?id=" + row.id;
                        } else {
                            deleteUrl = deleteUrl + "&id=" + row.id;
                        }
                        $.ajax({
                            cache: false,
                            async: false,
                            type: "POST",
                            dataType: 'json',
                            url: deleteUrl,
                            success: function (data, textStatus) {
                                if (easyuiExt.Ajax.isSuccess(data, textStatus)) {
                                    easyuiExt.Msg.show('温馨提示', '操作成功！');
                                    grid.datagrid('reload');
                                } else {
                                    easyuiExt.Msg.show('温馨提示', easyuiExt.Ajax.getErrorMsg(data));
                                }
                            }
                        });
                    }
                });
            }
        }
    });
})(jQuery);