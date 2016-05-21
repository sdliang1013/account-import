/**
 * Created by user-007 on 2015/12/8.
 */
$.extend($.fn.validatebox.defaults.rules, {
    equals: {
        validator: function (value, param) {
            return value == $(param[0]).val();
        },
        message: '两次输入的值不一致.'
    },
    minLength: {
        validator: function (value, param) {
            return value.length >= param[0];
        },
        message: '最小长度为 {0}.'
    },
    maxLength: {
        validator: function (value, param) {
            return value.length <= param[0];
        },
        message: '输入内容最大长度为 {0}'
    },
    telephone: {
        validator: function (value, param) {
            return /^(\+|\d)[0-9\-]{7,18}\d$/.test(value);
        },
        message: '请输入有效的电话号码。'
    },
    mobile: {
        validator: function (value, param) {
            return /^(1+\d{10})$/.test(value);
        },
        message: '请输入有效的手机号。'
    },
    idCard: {
        validator: function (value, param) {
            return /^((\d{15})|(\d{18})|(\d{17}x))$/i.test(value);
        },
        message: '请输入有效的身份证件号。'
    },
    /**
     * 远程验证 remoteCustom['url','paramName','errorMsg']
     */
    remoteCustom: {
        validator: function (value, param) {
            var flag = false;
            var url = param[0];
            var paramName = param[1];
            var data = {}
            data[paramName] = value;
            $.ajax({
                url: AppHelper.Url.getManageUrl(url, null),
                async: false,
                mode: "abort",
                data: data,
                success: function (data, textStatus) {
                    flag = AppHelper.Ajax.isSuccess(data, textStatus);
                    //if (!flag) {
                    //    $.messager.show({
                    //        title: "验证失败",
                    //        msg: AppHelper.Ajax.getErrorMsg(data),
                    //        timeout: 2000
                    //    });
                    //}
                },
                error: function (data, textStatus, e) {
                    //$.messager.show({
                    //    title: "验证失败",
                    //    msg: e.message,
                    //    timeout: 2000
                    //});
                    flag = false;
                }
            });
            return flag;
        },
        message: "{2}"
    }
});