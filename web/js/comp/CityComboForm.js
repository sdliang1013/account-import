/**
 * Created by yuanyouz on 2015/12/12.
 */
comp.CityComboForm = function() {
    var me = new comp.EntityForm();

    me.provinceCombo = null;
    me.cityCombo = null;
    me.regionCombo = null;

    me.provinceComboId = null;
    me.cityComboId = null;
    me.regionComboId = null;
    me.currentProvince = null;
    me.currentCity = null;
    me.currentRegion = null;

    me.introStoreCombo = null;
    me.introMemberCombo = null;

    me.introStoreComboId = null;
    me.introMemberComboId = null;
    me.currentIntroStore = null;
    me.currentIntroMember = null;

    me.initCityAndProvinceCombo = function(provinceComboId, cityComboId,regionComboId, currentProvince, currentCity ,currentRegion) {
        me.provinceComboId = provinceComboId;
        me.cityComboId = cityComboId;
        me.regionComboId = regionComboId;
        me.currentProvince = currentProvince;
        me.currentCity = currentCity;
        me.currentRegion = currentRegion;

        me.initCityCombo();
        me.initProvinceCombo();
    }

    me.initCityCombo = function() {
        var mCurrentCity = '';
        var mCurrentRegion = '';
        if (!AppHelper.isNull(me.currentCity)) {
            mCurrentCity = me.currentCity;
        }
        if (!AppHelper.isNull(me.currentRegion)) {
            mCurrentRegion = me.currentRegion;
        }

        if (AppHelper.isNull(me.currentProvince)) {
            //未指定省份
            me.cityCombo = $('#' + me.cityComboId).combobox({
                valueField: 'id',
                textField: 'name',
                value: mCurrentCity,
                editable: false
            });
            //未指定省份
            me.regionCombo = $('#' + me.regionComboId).combobox({
                valueField: 'id',
                textField: 'name',
                value: mCurrentRegion,
                editable: false
            });
        } else {
            //已指定省份
            var url = AppHelper.Url.getManageUrl("/sys/city/sub-city", {parentId: me.currentProvince});
            me.cityCombo = $('#' + me.cityComboId).combobox({
                url: url,
                valueField: 'id',
                textField: 'name',
                value: mCurrentCity,
                editable:false
            });
                if(AppHelper.isNull(me.currentCity)){
                    //未指定城市
                    me.cityCombo = $('#' + me.regionComboId).combobox({
                        valueField: 'id',
                        textField: 'name',
                        value: mCurrentRegion,
                        editable: false
                    });
                }else{
                    //已指定城市
                    var url = AppHelper.Url.getManageUrl("/sys/city/sub-region", {parentId: me.currentCity});
                    me.regionCombo = $('#' + me.regionComboId).combobox({
                        url: url,
                        valueField: 'id',
                        textField: 'name',
                        value: mCurrentRegion,
                        editable:false
                    });
                }
        }
    }


    me.initProvinceCombo = function () {
        var mCurrentProvince = '';
        if (!AppHelper.isNull(me.currentProvince)) {
            mCurrentProvince = me.currentProvince;
        }

        var url = AppHelper.Url.getManageUrl("/sys/city/all-province", null);
        me.provinceCombo = $('#' + me.provinceComboId).combobox({
            loader:function(param,success,error){
                $.ajax({
                    url: url,
                    dataType: 'json',
                    success: function(data){
                        data.unshift({id:'',name:''});
                        success(data);
                    },
                    error: function(){
                        easyuiExt.Msg.show("温馨提示", "初始化省份选择框时出错", true);
                    }
                });
            },
            onSelect:function(record){  //onSelect 用户点击时触发的事件  在此的意义在于，用户点击一级后自动二级combobox
                var url = AppHelper.Url.getManageUrl("/sys/city/sub-city", {parentId: $("#" + me.provinceComboId).combobox("getValue")});
                me.cityCombo.combobox({
                    loader:function(param,success,error){
                        $.ajax({
                            url: url,
                            dataType: 'json',
                            success: function(data){
                                data.unshift({id:'',name:''});
                                success(data);
                            },
                            error: function(){
                                easyuiExt.Msg.show("温馨提示", "初始化城市选择框时出错", true);
                            }
                        });
                    },
                    onSelect:function(record){ //这里也使用了onSelect事件，在二级combobox被用户点击时触发三级combobox
                        var url = AppHelper.Url.getManageUrl("/sys/city/sub-region", {parentId: $("#" + me.cityComboId).combobox("getValue")});
                        me.regionCombo.combobox({
                            loader:function(param,success,error){
                                $.ajax({
                                    url: url,
                                    dataType: 'json',
                                    success: function(data){
                                        data.unshift({id:'',name:''});
                                        success(data);
                                    },
                                    error: function(){
                                        easyuiExt.Msg.show("温馨提示", "初始化县/区选择框时出错", true);
                                    }
                                });
                            },
                            valueField: 'id',
                            textField: 'name',
                            value:'',
                            editable:false
                        }).combobox("clear");
                    },

                    onLoadSuccess:function(){  //清空三级下拉框 就是成功加载完触发的事件 当一级combobox改变时，二级和三级就需要清空
                        //do nothing
                    },
                    valueField: 'id',
                    textField: 'name',
                    value:'',
                    editable:false
                }).combobox("clear"); //清空二级下拉框
                me.regionCombo = $('#' + me.regionComboId).combobox({
                    valueField: null,
                    textField: null,
                    value: null,
                    editable: false
                });
            },
            valueField: 'id',
            textField: 'name',
            value: mCurrentProvince,
            editable:false
        });
    }

    me.initMemberAndStoreCombo = function(introStoreComboId, introMemberComboId, currentIntroStore, currentIntroMember) {
        me.introStoreComboId = introStoreComboId;
        me.introMemberComboId = introMemberComboId;
        me.currentIntroStore = currentIntroStore;
        me.currentIntroMember = currentIntroMember;

        me.initIntroMemberCombo();
        me.initIntroStoreCombo();
    }

    me.initIntroMemberCombo = function() {
        var mCurrentIntroMember = '';
        if (!AppHelper.isNull(me.currentIntroMember)) {
            mCurrentIntroMember = me.currentIntroMember;
        }

        if (AppHelper.isNull(me.currentIntroStore)) {
            //未指定门店
            me.introMemberCombo = $('#' + me.introMemberComboId).combobox({
                valueField: 'id',
                textField: 'username',
                value: mCurrentIntroMember,
                editable: false
            });
        } else {
            //已指定门店
            var url = AppHelper.Url.getManageUrl("/user/sub-member", {id: me.currentIntroStore});
            me.introMemberCombo = $('#' + me.introMemberComboId).combobox({
                url: url,
                valueField: 'id',
                textField: 'username',
                value: mCurrentIntroMember,
                editable:false
            });
        }
    }

    me.initIntroStoreCombo = function () {
        var mCurrentIntroStore = '';
        if (!AppHelper.isNull(me.currentIntroStore)) {
            mCurrentIntroStore = me.currentIntroStore;
        }

        var url = AppHelper.Url.getManageUrl("/store/list", null);
        me.introStoreCombo = $('#' + me.introStoreComboId).combobox({
            loader:function(param,success,error){
                $.ajax({
                    url: url,
                    dataType: 'json',
                    success: function(data){
                        data = data.rows;
                        success(data);
                    },
                    error: function(){
                        easyuiExt.Msg.show("温馨提示", "初始化门店下拉框时出错", true);
                    }
                });
            },
            onSelect:function(record){  //onSelect 用户点击时触发的事件  在此的意义在于，用户点击一级后自动二级combobox
                var url = AppHelper.Url.getManageUrl("/user/sub-member", {id: $("#" + me.introStoreComboId).combobox("getValue")});
                me.introMemberCombo.combobox({
                    loader:function(param,success,error){
                        $.ajax({
                            url: url,
                            dataType: 'json',
                            success: function(data){
                                data.unshift({id:'',username:''});
                                success(data);
                            },
                            error: function(){
                                easyuiExt.Msg.show("温馨提示", "初始化推荐人选择框时出错", true);
                            }
                        });
                    },
                    onSelect:function(record){ //这里也使用了onSelect事件，在二级combobox被用户点击时触发三级combobox
                        //do nothing
                    },
                    onLoadSuccess:function(){  //清空三级下拉框 就是成功加载完触发的事件 当一级combobox改变时，二级和三级就需要清空
                        //do nothing
                    },
                    valueField: 'id',
                    textField: 'username',
                    value:'',
                    editable:false
                }).combobox("clear"); //清空二级下拉框

            },
            valueField: 'id',
            textField: 'storeName',
            value: mCurrentIntroStore,
            editable:false
        });
    }

    return me;
}