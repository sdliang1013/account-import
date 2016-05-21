/**
 * Created by user-007 on 2015/12/8.
 */
(function ($) {
    function loadExtend(target, data) {
        var opts = $.data(target, 'form').options;
        var splitChar = ".";

        if (typeof data == 'string') {
            var param = {};
            if (opts.onBeforeLoad.call(target, param) == false) return;

            $.ajax({
                url: data,
                data: param,
                dataType: 'json',
                success: function (data) {
                    _load(data);
                },
                error: function () {
                    opts.onLoadError.apply(target, arguments);
                }
            });
        } else {
            _load(data);
        }

        function _load(data) {
            var form = $(target);
            _each(data, "", function (name, val) {
                if (!_checkField(name, val)) {
                    if (!_loadBox(name, val)) {
                        form.find('input[name="' + name + '"]').val(val);
                        form.find('textarea[name="' + name + '"]').val(val);
                        form.find('select[name="' + name + '"]').val(val);
                    }
                }
            });
            opts.onLoadSuccess.call(target, data);
            form.form('validate');
        }

        /**
         * 遍历设置值
         * @param pData
         * @param pName
         * @param func(name, value)
         * @private
         */
        function _each(pData, pName, func) {
            if ($.type(pData) == "object") {
                pName = pName ? (pName + ".") : "";
                for (var prop in pData) {
                    _each(pData[prop], pName + prop.toString(), func);
                }
            } else if ($.isArray(pData)) {
                pName = pName || "";
                for (var i = 0; i < pData.length; i++) {
                    _each(pData[i], pName + "[" + i + "]", func);
                }
            } else {
                func(pName, pData);
            }
        }

        /**
         * check the checkbox and radio fields
         */
        function _checkField(name, val) {
            var cc = $(target).find('[switchbuttonName="' + name + '"]');
            if (cc.length) {
                cc.switchbutton('uncheck');
                cc.each(function () {
                    if (_isChecked($(this).switchbutton('options').value, val)) {
                        $(this).switchbutton('check');
                    }
                });
                return true;
            }
            cc = $(target).find('input[name="' + name + '"][type=radio], input[name="' + name + '"][type=checkbox]');
            if (cc.length) {
                cc._propAttr('checked', false);
                cc.each(function () {
                    if (_isChecked($(this).val(), val)) {
                        $(this)._propAttr('checked', true);
                    }
                });
                return true;
            }
            return false;
        }

        function _isChecked(v, val) {
            if (v == String(val) || $.inArray(v, $.isArray(val) ? val : [val]) >= 0) {
                return true;
            } else {
                return false;
            }
        }

        function _loadBox(name, val) {
            var field = $(target).find('[textboxName="' + name + '"],[sliderName="' + name + '"]');
            if (field.length) {
                for (var i = 0; i < opts.fieldTypes.length; i++) {
                    var type = opts.fieldTypes[i];
                    var state = field.data(type);
                    if (state) {
                        if (state.options.multiple || state.options.range) {
                            field[type]('setValues', val);
                        } else {
                            field[type]('setValue', val);
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

    $.extend($.fn.form.methods, {
        load: function (jq, data) {
            return jq.each(function () {
                loadExtend(this, data);
            });
        }
    });
})(jQuery);