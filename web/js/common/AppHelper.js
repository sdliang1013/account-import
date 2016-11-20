if (typeof AppHelper == 'undefined') {
    var AppHelper = {
        "basePath": _basePath, //系统根路径
        "skin": skin //皮肤
    };
}

/**
 * 起始字符
 */
if (typeof String.prototype.isEmpty != 'function') {
    String.prototype.isEmpty = function () {
        var vTemp = $.trim(this);
        return vTemp == "";
    }
}
/**
 * 起始字符
 */
if (typeof String.prototype.startsWith != 'function') {
    String.prototype.startsWith = function (prefix, ignoreCase) {
        if (typeof prefix == 'undefined' || this.length < prefix.length) {
            return false;
        }
        var startStr = this.substr(0, prefix.length);
        if (ignoreCase) {
            startStr = startStr.toLowerCase();
            prefix = prefix.toLowerCase();
        }
        if (startStr == prefix) {
            return true;
        }
        return false;
    }
}
/**
 * 结束字符
 */
if (typeof String.prototype.endsWith != 'function') {
    String.prototype.endsWith = function (suffix, ignoreCase) {
        if (typeof suffix == 'undefined' || this.length < suffix.length) {
            return false;
        }
        var endStr = this.substring(this.length - suffix.length);
        if (ignoreCase) {
            endStr = endStr.toLowerCase();
            suffix = suffix.toLowerCase();
        }
        if (endStr == suffix) {
            return true;
        }
        return false;
    }
}
/**
 * 左补齐
 */
if (typeof String.prototype.leftPad != 'function') {
    String.prototype.leftPad = function (l, c) {
        var e = [];
        c = c || "0";
        for (var d = 0, a = l - this.length; d < a; d++) {
            e.push(c);
        }
        e.push(this);
        return e.join('');
    }
}
/**
 * 右补齐
 */
if (typeof String.prototype.rightPad != 'function') {
    String.prototype.rightPad = function (l, c) {
        var e = [this];
        c = c || "0";
        for (var d = 0, a = l - this.length; d < a; d++) {
            e.push(c);
        }
        return e.join('');
    }
}

/**
 * 是否数字
 */
if (typeof String.prototype.isNumber != 'function') {
    String.prototype.isNumber = function () {
        var pattern = /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/;
        return pattern.test(this);
    }
}
/**
 * 转成日期
 */
if (typeof String.prototype.toDate != 'function') {
    String.prototype.toDate = function () {
        var result;
        var vSrc = this;
        if (!this.isEmpty()) {
            var r = new RegExp(
                "^[1-2]\\d{3}-(0?[1-9]||1[0-2])-(0?[1-9]||[1-2][1-9]||3[0-1])$");
            if (r.test(this)) {
                var arr = vSrc.split("-");
                result = new Date(arr[0], arr[1], arr[2], 0, 0, 0);
            }
        }
        return result;
    }
}
/**
 * 转成时间
 */
if (typeof String.prototype.toDateTime != 'function') {
    String.prototype.toDateTime = function () {
        var result;
        var vSrc = this;
        if (!this.isEmpty()) {
            var r = new RegExp(
                "^[1-2]\\d{3}-(0?[1-9]||1[0-2])-(0?[1-9]||[1-2][0-9]||3[0-1]) ((0)?[0-9]||1[0-9]||2[0-4])(:((0)?[0-9]||[1-5][0-9])){2}$");
            if (r.test(this)) {
                var arr = vSrc.replace(/[^\d]/g, "-").split("-");
                result = new Date(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
            }
        }
        return result;
    }
}
/**
 * 日期格式化
 */
if (typeof Date.prototype.format != 'function') {
    Date.prototype.format = function (fmt) {
        return fmt.replace("yyyy", this.getFullYear()).replace(
            "MM", (this.getMonth() + 1).toString().leftPad(2)).replace(
            "dd", (this.getDate()).toString().leftPad(2)).replace(
            "HH", (this.getHours()).toString().leftPad(2)).replace(
            "mm", (this.getMinutes()).toString().leftPad(2)).replace(
            "ss", (this.getSeconds()).toString().leftPad(2));
    }
}

/**
 * function的继承
 */
if (typeof Function.prototype.extend != 'function') {
    Function.prototype.extend = function (oFather, oNewProp) {
        oNewProp = oNewProp || {};
        var funcStr = this.toString();
        var paramBegin = funcStr.indexOf('(') + 1;
        var paramEnd = funcStr.indexOf(')');
        var contentBegin = funcStr.indexOf('{') + 1;
        var contentEnd = funcStr.lastIndexOf('}');
        var contentStr = funcStr.substring(contentBegin, contentEnd);
        var paramStr = funcStr.substring(paramBegin, paramEnd);
        contentStr = "this.father=" + oFather + ".apply(arguments.callee,arguments);"
        + "if(!this.father){this.father=new " + oFather + "(arguments);}"
        + "for(p in this.father){if(typeof this.father[p] == 'function'){eval('this[\"'+p+'\"]='+this.father[p]+'');}else{this[p]=this.father[p];}}"
        + contentStr;
        var _newFun = function () {
        };
        eval("_newFun = function(" + paramStr + "){" + contentStr + "};");
        for (p in oNewProp) {
            _newFun.prototype[p] = oNewProp[p];
        }
        return _newFun;
    }
}

(function ($) {
    AppHelper = $.extend(AppHelper, {
        isNull: function (obj) {
            return typeof obj == "undefined" || obj == null;
        },
        String: {
            isEmpty: function (str) {
                return AppHelper.isNull(str) || str.toString().length == 0;
            }
        },
        Array: {
            getProperties: function (jsonAry, property) {
                var ary = [];
                if (jsonAry && jsonAry.length > 0) {
                    for (var i = 0; i < jsonAry.length; i++) {
                        ary.push(jsonAry[i][property]);
                    }
                }
                return ary;
            }
        },
        Code: {
            Success: 200,
            Error: 500
        },
        Status: {
            Success: 'success'
        },
        Ajax: {
            isSuccess: function (resultJson, textStatus) {
                return !resultJson.exception && AppHelper.Status.Success == textStatus
                    && (resultJson.status ? AppHelper.Code.Success == resultJson.status : false);
            },
            getErrorMsg: function (resultJson) {
                return resultJson["exception_message"] || resultJson.exception;
            },
            getData: function (resultJson) {
                return resultJson.record || resultJson.recordset || resultJson;
            },
            getPaging: function (resultJson) {
                return resultJson.paging;
            },
            /**
             * 对结果集的默认处理
             * @param resultJson 结果对象
             * @param textStatus 状态
             * @param success 成功回调
             * @param error 失败回调
             * @returns {*}
             */
            defaultResult: function (resultJson, textStatus, success, error) {
                var data = AppHelper.Ajax.getData(resultJson);
                if (!AppHelper.Ajax.isSuccess(resultJson, textStatus)) {
                    var eMsg = AppHelper.Ajax.getErrorMsg(resultJson) || "服务器内部错误!";
                    typeof error == "function" ? error(eMsg) : $.messager.alert("错误", eMsg, "error");
                } else {
                    $.messager.alert("成功", "操作成功!", "info", success);
                }
                return data;
            }
        },
        Json: {
            /**
             * Json To String
             * @param jsonObj
             * @returns {*}
             */
            toString: function (jsonObj) {
                if (!jsonObj) {
                    return null;
                }
                if (JSON.stringify) {
                    return JSON.stringify(jsonObj);
                } else {
                    var ary = [];
                    for (p in jsonObj) {
                        if (p === '$class')
                            continue;
                        ary.push("\"" + p + "\":" + jsonObj[p] + "\"");
                    }
                    return "{" + ary.join(",") + "}";
                }
            },
            /**
             * String To Json
             * @param jsonStr
             * @returns {*}
             */
            valueOf: function (jsonStr) {
                if (!jsonStr) {
                    return null;
                }
                if (JSON.parse) {
                    return JSON.parse(jsonStr);
                } else {
                    return $.parseJSON(jsonStr);
                }
            }
        },
        Url: {
            base: AppHelper.basePath,
            manage: AppHelper.basePath + "mgr/",
            /**
             * 功能描述: 获取url的参数对象
             *
             * @param {Object}
             *            name 参数名称
             * @return {string}
             */
            getParameter: function (name, uri) {
                var uri = uri || window.location.search;
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
                var r = uri.substr(1).match(reg);
                if (r != null)return decodeURI(r[2]);
                return null;
            },
            wapperUrl: function (uri) {
                var url = uri;
                if (url != "" && !url.startsWith("http")) {
                    url = AppHelper.basePath + uri;
                }
                return url;
            },
            /**
             *
             * @param uri
             * @param paramJson
             * @returns {*}
             */
            getManageUrl: function (uri, paramJson) {
                var url = uri;
                var suffix = "shtml";//后缀
                if (!url.startsWith("http")
                    && !url.startsWith(AppHelper.Url.manage)) {
                    url = AppHelper.Url.manage + url;
                }
                if (!url.endsWith(suffix)) {
                    url += ("." + suffix);
                }
                if (!AppHelper.isNull(paramJson)) {
                    url += ("?" + $.param(paramJson));
                }
                return url;
            }
        },
        Iframe: {
            create: function (url) {
                return "<iframe src='" + url + "' frameborder=0 height=100% width=100% scrolling=auto></iframe>";
            }
        },
        Sys: {
            dict: function (dictType, dictCode, callBack) {
                if (!dictType) {
                    $.dialog.tips("dictType不能为空!", 2, "fail.png");
                    return false;
                }
                var returnObj = null;
                var async = (typeof callBack == "function");
                $.ajax({
                    url: AppHelper.Url.getManageUrl("sys/dict/load", null),
                    data: {
                        "type": dictType,
                        "code": dictCode || ""
                    },
                    async: async,
                    success: function (data, textStatus) {
                        returnObj = data;//AppHelper.Ajax.getData(data);
                        if (async) {
                            callBack(returnObj);
                        }
                    },
                    error: function (data, textStatus, e) {
                        $.messager.alert('警告', "code不能为空!");
                    }
                });
                return returnObj;
            },
            enum: function (enumName, code, callBack) {
                if (!enumName) {
                    $.dialog.tips("enumName不能为空!", 2, "fail.png");
                    return false;
                }
                var returnObj = null;
                var async = (typeof callBack == "function");
                $.ajax({
                    url: AppHelper.Url.getManageUrl("sys/enum/load", null),
                    data: {
                        "enumName": enumName,
                        "code": code || ""
                    },
                    async: async,
                    success: function (data, textStatus) {
                        returnObj = data;//AppHelper.Ajax.getData(data);
                        if (async) {
                            callBack(returnObj);
                        }
                    },
                    error: function (data, textStatus, e) {
                        $.messager.alert('警告', e.message);
                    }
                });
                return returnObj;
            },
            isAdmin: function () {
                var admin = false;
                $.ajax({
                    url: AppHelper.Url.getManageUrl(user.MODULE_USER + "/is-admin", null),
                    async: false,
                    success: function (resultJson, textStatus) {
                        if (AppHelper.Ajax.isSuccess(resultJson, textStatus)) {
                            admin = AppHelper.Ajax.getData(resultJson).admin;
                        } else {
                            $.messager.alert("错误", AppHelper.Ajax.getErrorMsg(resultJson));
                        }
                    },
                    error: function (data, textStatus, e) {
                        $.messager.alert('错误', e.message);
                    }
                });
                return admin;
            },
            checkPermission: function () {
                var admin = false;
                $.ajax({
                    url: AppHelper.Url.getManageUrl(sys.MODULE_PERMISSION+ "/check", null),
                    async: false,
                    success: function (resultJson, textStatus) {
                        if (AppHelper.Ajax.isSuccess(resultJson, textStatus)) {
                            admin = true;
                        } else {
                            $.messager.alert("错误", AppHelper.Ajax.getErrorMsg(resultJson));
                        }
                    },
                    error: function (data, textStatus, e) {
                        $.messager.alert('错误', e.message);
                    }
                });
                return admin;
            }
        },
        Win: {
            openWin: function (param) {
                // 获得窗口的垂直位置
                var iTop = (window.screen.availHeight - 50 - param.height) / 2;
                // 获得窗口的水平位置
                var iLeft = (window.screen.availWidth - 30 - param.width) / 2;
                var winParam = 'height='
                    + param.height
                    + ',innerHeight='
                    + param.height
                    + ',width='
                    + param.width
                    + ',innerWidth='
                    + param.width
                    + ',top='
                    + iTop
                    + ',left='
                    + iLeft
                    + ',scrollbars=yes,status=no,toolbar=no,menubar=no,location=no,resizable=yes,titlebar=no';
                if (param.fullscreen) {
                    winParam += ",fullscreen=yes";
                }
                window.open(param.url, param.name, winParam);
            },
            closeWin: function () {
                window.close();
            },
            getObject: function (objName, win, times) {
                win = win || window;
                times = times || 1;
                if (times++ < 4) {//最高限制3层
                    if (win[objName]) {
                        return win[objName];
                    }
                } else {
                    return null;
                }
                return AppHelper.Win.getObject(objName, win.parent, times);
            }
        },
        Form: {
            vals: function (name, split) {
                var valAry = [];
                split = split || ",";
                $("input[name='" + name + "']").each(function (idx, obj) {
                    if (obj.type != "checkbox" || obj.checked) {
                        valAry.push($(obj).val());
                    }
                });
                return valAry.join(split);
            }
            ,
            /**
             * 表单内容转成{name:value,...}
             * @param form
             * @returns {{}}
             */
            serialize: function (form) {
                var objAry = $(form).serializeArray();
                var obj = {};
                for (var i = 0; i < objAry.length; i++) {
                    var tmpObj = objAry[i];
                    obj[tmpObj.name] = tmpObj.value;
                }
                return obj;
            }
        }
        ,
        Storage: {
            keyPrefix: "_ACCOUNT_IMPORT_",
            support: function () {
                return window.sessionStorage && window.localStorage;
            }

            ,
            wrapper: function (key) {
                return this.keyPrefix + key;
            }
            ,
            setItem: function (key, value, isLocal) {
                if (!this.support()) {
                    return;
                }
                key = this.wrapper(key);
                isLocal = (typeof isLocal == "undefined") ? true : isLocal;
                return isLocal ? localStorage.setItem(key, value) : sessionStorage.setItem(key, value);
            }
            ,
            getItem: function (key, isLocal) {
                if (!this.support()) {
                    return;
                }
                key = this.wrapper(key);
                isLocal = (typeof isLocal == "undefined") ? true : isLocal;
                return isLocal ? localStorage.getItem(key) : sessionStorage.getItem(key);
            }
            ,
            removeItem: function (key, isLocal) {
                if (!this.support()) {
                    return;
                }
                key = this.wrapper(key);
                isLocal = (typeof isLocal == "undefined") ? true : isLocal;
                return isLocal ? localStorage.removeItem(key) : sessionStorage.removeItem(key);
            }
            ,
            clear: function (isLocal) {
                if (!this.support()) {
                    return;
                }
                isLocal = (typeof isLocal == "undefined") ? true : isLocal;
                return isLocal ? localStorage.clear() : sessionStorage.clear();
            }
            ,
            /**
             * 存储表单的值(以来edu.fn.js)
             * @param pageName
             * @param targetForm
             * @param isLocal
             */
            storeFormData: function (pageName, targetForm, isLocal) {
                if (!this.support()) {
                    return;
                }
                var key = pageName + targetForm.attr('name');
                key = this.wrapper(key);
                var formValue = Edu.fn.formToObject(targetForm);
                isLocal = (typeof isLocal == "undefined") ? true : isLocal;
                this.setItem(key, JSON.stringify(formValue), isLocal);
            }
            ,
            /**
             * 回填表单的值(以来edu.fn.js)
             * @param pageName
             * @param targetForm
             * @param isLocal
             */
            setFormData: function (pageName, targetForm, isLocal) {
                if (!this.support()) {
                    return;
                }
                var key = pageName + targetForm.attr('name');
                key = this.wrapper(key);
                isLocal = (typeof isLocal == "undefined") ? true : isLocal;
                var formValue = this.getItem(key, isLocal);
                Edu.fn.objectToForm(JSON.parse(formValue), targetForm);
            }
        }
        ,
        Event: {
            initEvent: function (obj) {
                if (typeof obj == "object") {
                    $.extend(obj, {
                        _events: {},
                        bind: function (eName, eHandler) {
                            this._events[eName] = eHandler;
                        },
                        unbind: function (eName) {
                            this._events[eName] = null;
                        },
                        fire: function (eName, param) {
                            var handler = this._events[eName];
                            if ($.isFunction(handler)) {
                                handler(param);
                            }
                        }
                    });
                }
            }
            ,
            /**
             * 功能描述: 获取对象与文档的相对位置
             * @param e 对象
             */
            getPosition: function (e) {
                if (e.pageX || e.pageY) {
                    return {x: e.pageX, y: e.pageY};
                }
                var x = e.offsetLeft, y = e.offsetTop, h = e.clientHeight, w = e.clientWidth;

                while (e = e.offsetParent) {
                    x += e.offsetLeft;
                    y += e.offsetTop;
                    // h += e.clientHeight;
                }
                //document.getElementById('x').
                //$("#x").val(x+","+(x+w));
                //$("#y").val(y+","+(y+h));
                var ePoint = {
                    x: x + document.body.scrollLeft - document.body.clientLeft,
                    y: y + document.body.scrollTop - document.body.clientTop,
                    h: h,
                    w: w
                };
                return ePoint;
            }
        }
        ,
        Function: {
            once: function (func) {
                return function () {
                    if (typeof func == "function") {
                        func.apply(this, arguments);
                        func = undefined;
                    }
                }
            }
        }
    })
    ;
})
(jQuery);
